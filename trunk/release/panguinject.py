#!/usr/bin/env python
# -*- coding: UTF-8 -*-

from xml.dom import minidom

from ConfigParser import ConfigParser
import urllib2
import urllib
import socket

### Some functions for Pangudumper
YT2_CFG_URL = "http://datax.yunti2.aliyun-inc.com/datax"

def getYT2Config(cmd):
    cfg = {}
    cfg['flag'] = 0
    cfg['destruct_limit'] = 3
    cfg['storage'] = ''
    cfg['line_limit'] = -1
    cfg['byte_limit'] = -1
    cfg['cluster'] = 'localcluster:10240'

    try:
        url = YT2_CFG_URL + "/config.php"
        url = url + "?cmd=" + urllib.quote(cmd)
        
        config = ConfigParser()
        
        socket.setdefaulttimeout(3)
        fp = urllib2.urlopen(url)
        
        config.readfp(fp)

        for section in config.sections():
            for name, value in config.items(section):
                cfg[name] = value
    except Exception,e:
        print "Faild to get YT2 Config: " 
        print e
    return cfg;

def reportStatus(cmd, status):
    try:
        url = YT2_CFG_URL + "/status.php"
        url = url + "?cmd=" + urllib.quote(cmd) + "&status=" + str(status)
        socket.setdefaulttimeout(10)
        result = urllib2.urlopen(url).read()
        return result;
    except:
        print "Faild to report job status to " + url
        return -1;

### end functions for Pangudumper

def injectPangudumper(jobDescFilePath, y2Cfg):
    try:
        jobDescFile = open(jobDescFilePath, "r");
        jobDesc = jobDescFile.read()
        jobDescFile.close()

        dom = minidom.parseString(jobDesc)
        dumpers = dom.getElementsByTagName("dumper")
        dumperCount = len(dumpers)

        if dumperCount == 1:
            dumper = dumpers[0]
            plugin = dumper.getElementsByTagName("plugin")[0].childNodes[0].data
            
            if 'hdfsdumper' == plugin or 'hdfswriter' == plugin:
                pangudumper = dumper.cloneNode(10)
                
                # set pangudumper plugin
                if 'hdfsdumper' == plugin:
                    pangudumper.getElementsByTagName("plugin")[0].childNodes[0].data = "pangudumper"
                elif 'hdfswriter' == plugin:
                    pangudumper.getElementsByTagName("plugin")[0].childNodes[0].data = "panguwriter"
                
                # set the pangudumper id attribute
                pangudumper.setAttribute("id", "pangudumper")
                pangudumper.setAttribute("destructlimit", str(y2Cfg['destruct_limit']))

                params = pangudumper.getElementsByTagName("param")

                # set storage, lineLimit, byteLimit
                for param in params:
                    if (y2Cfg['storage'] != ""):
                        storageParam = param.cloneNode(10)
                        storageParam.setAttribute("key", "storageClassName")
                        storageParam.setAttribute("value", str(y2Cfg['storage']))
                        pangudumper.appendChild(storageParam)
                    if (int(y2Cfg['line_limit']) > 0):
                        lineLimitParam = param.cloneNode(10)
                        lineLimitParam.setAttribute("key", "lineLimit")
                        lineLimitParam.setAttribute("value", str(y2Cfg['line_limit']))
                        pangudumper.appendChild(lineLimitParam)
                    if (int(y2Cfg['byte_limit']) > 0):
                        byteLimitParam = param.cloneNode(10)
                        byteLimitParam.setAttribute("key", "byteLimit")
                        byteLimitParam.setAttribute("value", str(y2Cfg['byte_limit']))
                        pangudumper.appendChild(byteLimitParam)
                    break;
                
                # replace DIR param to pangu format
                
                for param in params:
                    key = param.getAttribute("key")
                    if key == 'DIR':
                        value = param.getAttribute("value")
                        ihdfs = value.find("hdfs://")
                        if ihdfs != -1:
                            islash = value[ihdfs + 7:].find("/")
                            panguDir = "pangu://" + str(y2Cfg['cluster']) + "/" + value[ihdfs + 7 + islash + 1:]
                            param.setAttribute("value", panguDir)
                
                            # append pangudumper to the xml dom
                            dom.getElementsByTagName("job")[0].appendChild(pangudumper)
                            #print dom.toxml("UTF-8")
                            jobDescFile = file(jobDescFilePath, "w")
                            jobDescFile.write(dom.toxml("UTF-8"))
                            jobDescFile.close()
    except Exception,e:
        print "Inject pangu dumper error."
        print e

if __name__ == '__main__':
    injectPangudumper("../jobs/table1_10,table15_20_tmp_mysql_hdfs.xml");
