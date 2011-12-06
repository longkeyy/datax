#!/usr/bin/env python
# -*- coding: UTF-8 -*-

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

import config
import loader 

class InterceptorRegister(object):
    """
        provide a general processor framework to handle xml
    """
    @classmethod
    def instance(cls):
        if not hasattr(cls, '_instance'):
            cls._instance = Interceptors()

        return cls._instance


class Interceptors(object):
    """
               
    """
    def __init__(self):
        self.__configInterceptor(os.path.abspath(r"./conf/interceptor.conf"))
        return

    def __configInterceptor(self, filename):
        """
            parse config file, and construct processors
        """
        
        self.__interceptors= []
        
        #if config-file not exists, just return
        if not os.path.exists(filename):
            return

        configure = config.parse(filename)
        
        for name in configure["interceptors"]:
            #print(name, configure["interceptors"][name]["class"])
            self.__interceptors.append(loader.instance(configure["interceptors"][name]["class"]))

        return

    def process(self, xml):
        """
            pass xml file to the processor list
        """
        for interceptor in self.__interceptors:
            interceptor.process(xml)

        return


if __name__ == "__main__":
    InterceptorRegister.instance()
    pass

