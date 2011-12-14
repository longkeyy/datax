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


def instance(name, *args, **kwargs):
    """
        create an instance of a class specifed by name
        [Parameter]
        name        - classname, including module name
        *args       - parameters which class constructor needs (list)
        *kwargs     - parameters which class constructor needs (dict)
    
        [Example]
        logger = loader.instance("log.Logger")
    """
    (moduleName, className) = name.rsplit('.', 1)
    m = __import__(moduleName, globals(), locals(), [className])
    c = getattr(m, className)
    object = c(*args, **kwargs)

    return object



if __name__ == "__main__":
    pass
