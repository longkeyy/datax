#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
/**
 * (C) 2010-2011 Alibaba Group Holding Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License 
 * version 2 as published by the Free Software Foundation. 
 * 
 */
 
"""


import os
import sys
import re
import time
import subprocess
import ConfigParser
import xml.etree.ElementTree as xmltree

def log(msg):
    """
        append timestamp to msg
    """
    TIMEFORMAT = r"%Y-%m-%d %H:%M:%S"
    print("[%s] %s" % (time.strftime(TIMEFORMAT, time.gmtime()), msg))

    return


class DbMapper(object):
    """
        replace xml file with updated ip and port configuration
    """
    def process(self, ctx):
        self.updateIpAddr(ctx)

        return

    def getNewIp(self, conn, dbname):
        """
        """
        cmd = r"./bin/mapper.sh " + conn + " " + dbname
        p = subprocess.Popen(cmd, shell = True, stdout = subprocess.PIPE, stderr = subprocess.STDOUT)
        (stdoutput, stderrput) = p.communicate()
        
        if stderrput != None:
            #set warning
            log("INFO: Connect to " + conn + " error")
            return (None, None)
        
        results = []
        lines = stdoutput.split('\n')
        for line in lines:
            if line == None or len(line.strip()) == 0:
                continue
            
            results.append(line)

        #need to make sure the record is uniq
        if len(results) != 1:
            #set warning
            log("INFO: Get connection not uniq: " + ":".join(results))
            return (None, None)
        
        items = results[0].split()
        if re.search(r"\b(?:[0-9]{1,3}\.){3}[0-9]{1,3}\b", items[0]) == None:
            log("INFO: IP is not correct:" + items[0])
            return (None, None)
        log("INFO: Transform Database " + dbname + " to IP " + items[0].strip())
        return (items[0].strip(), items[1].strip())


    def updateIpAddr(self, ctx):
        """
            replace xml file with updated ip and port configuration
        """
        #check env_path, if env_path is None or connection info is None, return
        connMap = {
                        r"tbpre": r"tbpre/tbpre123@tbpre", 
                        r"taobao": r"sngps/sngps123@sngps_dwdb",
                   }
        
        configFile = r"./conf/mapper.conf"
        if os.path.exists(configFile):
             cf = ConfigParser.ConfigParser()
             cf.read(configFile)
             
             #connMap.clear()
             for key, value in cf.items(r"connection"):
                 connMap[key] = value
                
 
        pattern = re.compile(r"env_path=(\w+)")
        match = pattern.search(ctx["params"])
        conn = r"taobao"

        #check the env_path
        if match != None and \
        connMap.get(match.group(1), None) != None:
            conn = match.group(1)

        connection = connMap[conn]

        #step 1: parse xml file, get dbname
        #step 2: query ip from db
        #step 3: gen new file
        doc = xmltree.parse(ctx["jobdescpath"])
        jobs = doc.getroot()

        newIp = None
        dbname = None

        for job in jobs:
            if cmp(job.find("loader/plugin").text, "mysqlloader") == 0:
                loader = job.find("loader")
        
                newIp = None
                newPort = None
                dbname = None
            
                for item in loader:
                    if "key" not in item.attrib:
                        continue

                    if cmp(item.attrib["key"].lower(), "dbname") == 0:
                        dbname = item.attrib["value"]
                        newIp, newPort = self.getNewIp(connection, dbname)
            
            #ignore
                if newIp == None or newPort == None:
                    continue

                for item in loader:
                    if "key" not in item.attrib:
                        continue

                    if cmp(item.attrib["key"].lower(), "ip") == 0:
                        if item.attrib["value"] != newIp:
                            item.attrib["value"] = newIp
                            #set warning

                    if cmp(item.attrib["key"].lower(), "port") == 0:
                        item.attrib["value"] = newPort

            #iterate dumper
            for dumper in job.findall("dumper"):
                if cmp(dumper.find("plugin").text, "mysqldumper") != 0:
                    continue

            #reset 
                dbname = None
                newIp = None
                newPort = None
            
                for item in dumper:
                    if "key" not in item.attrib:
                        continue
                
                    if cmp(item.attrib["key"].lower(), "dbname") == 0:
                        dbname = item.attrib["value"]
                        newIp, newPort = self.getNewIp(connection, dbname)
            
                if newIp == None or newPort == None:
                    continue

                for item in dumper:
                    if "key" not in item.attrib:
                        continue

                    if cmp(item.attrib["key"].lower(), "ip") == 0:
                        if item.attrib["value"] != newIp:
                            item.attrib["value"] = newIp
                            #set warning

                    if cmp(item.attrib["key"].lower(), "port") == 0:
                        item.attrib["value"] = newPort

        doc.write(ctx["jobdescpath"], encoding = "utf-8")

        return


if __name__ == "__main__":
    os.chdir(r"../")
    p = DbMapper()
    print(p.getNewIp(r"tbpre/tbpre123@tbpre", r"coll_4"))
    print(p.getNewIp(r"tbpre/tbpre12@tbpre", r"nest"))
    print(p.getNewIp(r"tbpre/tbpre123@tbpre", r"uic_"))
    print(p.getNewIp(r"tbpre/tbpre123@tbpre", r"feel_01"))
