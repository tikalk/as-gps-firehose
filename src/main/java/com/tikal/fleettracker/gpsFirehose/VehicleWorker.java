package com.tikal.fleettracker.gpsFirehose;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

public class VehicleWorker implements Runnable {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(VehicleWorker.class);

	private final int schedualeIntervalInMillis;
	private final Path gpsInputFile;
	private final GpsSender gpsSender;
	private Double lastLatitude;
	private Double lastLongtitude;
	private Date lastTimestamp;

	private final SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmss");
	private final SimpleDateFormat dataDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public VehicleWorker(final int schedualeIntervalInMillis,final Path gpsInputFile, final GpsSender gpsSender) {
		this.schedualeIntervalInMillis = schedualeIntervalInMillis;
		this.gpsInputFile = gpsInputFile;
		this.gpsSender = gpsSender;
	}

	@Override
	public void run() {
		try {
			final Stream<String> linesStream;
			linesStream = Files.lines(gpsInputFile);
			linesStream.forEach(this::sendAndWait);
			linesStream.close();
		} catch (final Exception e) {
			logger.error("Failed Worker running", e);
		}

	}

	void sendAndWait(final String g) {
		try {
			final String[] split = g.split(",");
			final String imei = split[0];
			final Date cureentDate = dataDF.parse(split[1]);
//			final String currentDateStr = df.format(cureentDateStr);
			 
			
//			final String latLong = split[2]+","+split[3];
			final double currentLong = Double.valueOf(split[2]);
			final double currentLat = Double.valueOf(split[3]);	
			double distance;
			int speed=0;
			int angel=0;
			if(lastLatitude!=null){
				distance = LatLonCalculator.distance(lastLatitude, lastLongtitude, currentLat, currentLong, "K");
				final double detaTimeSec = (cureentDate.getTime()-lastTimestamp.getTime())/1000;
				speed = (int)(distance/(detaTimeSec/3600));
				angel = (int)LatLonCalculator.angleFromCoordinate(lastLatitude, lastLongtitude, currentLat, currentLong);
			}
			final String formatG = String.format(
					"$$E142,%s,AAA,35,%s,%s,%s,A,3,17,%s,%s,7.1,22,100,200,310|260|7dc1|93c8,0000,0000|0000|0000|9d3|0,,*AA,%s",
					imei, currentLat, currentLong, df.format(cureentDate), speed,angel,df.format(cureentDate));
			gpsSender.send(formatG);
			lastLatitude = currentLat;
			lastLongtitude = currentLong;
			lastTimestamp = cureentDate;
			Thread.sleep(schedualeIntervalInMillis);
		} catch (final Exception e) {
			logger.error(e.getMessage());
		}
	}

}
