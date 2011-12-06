#!/bin/sh 

#############################################################
# (C) 2010-2011 Alibaba Group Holding Limited.
#
# This program is free software; you can redistribute it and/or
# modify it under the terms of the GNU General Public License 
# version 2 as published by the Free Software Foundation. 
#############################################################

declare -r PRGNAME=`basename $0`
declare -r STAMP=`date +%Y%m%d%H%M%S`
declare -r SQLPLUS=${ORACLE_HOME}/bin/sqlplus

function logDate
{
    timeStr=`date +'%F %T'`
    echo "[$timeStr] $@"
}

function exeSql
{
    $SQLPLUS -s $1 <<EOF
    set heading off;
    set feedback off;
    select ip_addr, port from dwa.mt_db_host h where h.db_name = '$2';
EOF
    return
}

#check parameters
if [ $# -ne 2 ]; then
    logDate "Usage: $PRGNAME connection dbname"
    exit -1
fi

exeSql $1 $2

exit 0
