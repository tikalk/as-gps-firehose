#!/bin/bash

echo "Starting FleetTracker Firehose"

DIRNAME=`dirname $0`
APP_HOME=`cd $DIRNAME/..;pwd;`
export APP_HOME;

java -Duser.timezone="UTC" -jar $APP_HOME/build/libs/ft-gps-firehose-1.0.0.jar
