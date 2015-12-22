package com.tikal.fleettracker.gpsFirehose;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.stream.Stream;

public class VehicleWorker implements Runnable {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(VehicleWorker.class);

	private final String deviceManagerPostGpsUrl;
	private final int schedualeIntervalInMillis;
	private final Path gpsInputFile;
	private final GpsSender gpsSender;

	private final SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmss");
	// 2008-02-08 17:36:47
	private final SimpleDateFormat dataDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public VehicleWorker(final String deviceManagerPostGpsUrl, final int schedualeIntervalInMillis,
			final Path gpsInputFile, final GpsSender gpsSender) {
		this.deviceManagerPostGpsUrl = deviceManagerPostGpsUrl;
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
			final String date = df.format(dataDF.parse(split[1]));
			final String formatG = String.format(
					"$$E142,%s,AAA,35,%s,%s,%s,A,3,17,25,89,7.1,22,100,200,310|260|7dc1|93c8,0000,0000|0000|0000|9d3|0,,*AA,%s",
					split[0], split[3], split[2], date, date);
			gpsSender.send(formatG);
			Thread.sleep(schedualeIntervalInMillis);
		} catch (final Exception e) {
			logger.error(e.getMessage());
		}
	}

}
