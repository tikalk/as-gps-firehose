# AngelSense GPS Firehose

This micro service is is mocking the devices by sending raw-gps lines (HTTP or Socket), to the Device Manager.
It is based on SpringBoot platform

## How to build
_______________
From the project home folder run the following command:

./gradlew clean build -x test

This will create self contained zip, that you can unzip on host container. The output zip is located at
as-gps-firehose/build/distributions/as-gps-firehose-1.0.0.zip

## How to run
-----------
Unzip the file as-gps-firehose/build/distributions/as-gps-firehose-1.0.0.zip 
cd to the created folder (as-gps-firehose-1.0.0)
Run the following command : 
./run-bin/gps-firehose.sh



 

