# FleetTracker GPS Firehose

This micro service is is mocking the devices by sending raw-gps lines (HTTP or Socket), to the Device Manager.
It is based on SpringBoot platform

## How to build
_______________
From the project home folder run the following command:

./gradlew clean build -x test

This will create self contained zip, that you can unzip on host container. The output zip can be downloaded from this site: http://research.microsoft.com/apps/pubs/?id=152883

We are using the 01.zip, loacted at : http://research.microsoft.com/pubs/152883/01.zip



## How to run
-----------
First thing you need to have is the input file that contain the GPS sample. You can download it from as follow:
 
wget https://7916d8d0dd6f42ebd88f2c0ce069ecf0f7b7a215.googledrive.com/host/0B4Lvfo9HRfAcM2ZkZHY2TTVZMDQ/gps.log.zip

Next thing, you need to unzip it and put the gps.log file in /var/log/fleettracker/gps.log. If you want to put it elsewhere, you wiil need to change the path on the config file.
Unzip the file as-gps-firehose/build/distributions/as-gps-firehose-1.0.0.zip 
cd to the created folder (as-gps-firehose-1.0.0)
Run the following command : 
./run-bin/gps-firehose.sh



 

