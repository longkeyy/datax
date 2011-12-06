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
import ConfigParser


def parse(filename):
    """
        parse config file, return a map that contains name to class path name mapping relationship
    """

    cf = ConfigParser.ConfigParser()
    cf.read(filename)

    names = cf.get("interceptors", "interceptor").split()

    #config file that contains all config item
    config = {}

    #parse processors
    #calculate processors name and attributes
    interceptors = {}
    for name in names:
        interceptor = {}
        for key, value in cf.items(name):
            interceptor[key] = value

        interceptors[name] = interceptor

    config["interceptors"] = interceptors

    return config


if __name__ == "__main__":
    print(parse("./config.ini"))
    pass

