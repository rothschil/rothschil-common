#!/usr/bin/env bash

PID_FILE=./bin/application.pid

if [ ! -e ${PID_FILE} ]; then
    echo "not running";
    exit;
fi

PID=`cat ${PID_FILE}`
kill ${PID}
echo "killed process $PID"
rm ${PID_FILE}
