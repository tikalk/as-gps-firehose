#!/bin/bash

if [ -z "$1" ]
  then
    echo "No profile is supplied. You need to run it as load-test.sh <profile> . Profile is used to choose the exact configuration, and current options are default and load."
    exit -1
fi

echo "Starting **Exporter** Load Test App $1..."

DIRNAME=`dirname $0`
APP_HOME=`cd $DIRNAME/..;pwd;`
export APP_HOME;

java -Duser.timezone="UTC" -Dspring.profiles.active="$1" -jar $APP_HOME/build/libs/load-test-1.0.0.jar