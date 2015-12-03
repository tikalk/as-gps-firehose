package com.tikal.angelsense.gpsFirehose;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.goebl.david.Response;
import com.goebl.david.Webb;

@SpringBootApplication
public class Application {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Application.class);

	@Value("${deviceManagerPostGpsUrl}")
	private String deviceManagerPostGpsUrl;
	
	@Value("${schedualeIntervalInMillis}")
	private int schedualeIntervalInMillis;
	
	@Value("${gpsInputFile:/var/log/angelsense/gps.log}")
	private String gpsInputFile;
	
	@Value("#{'${filterImei}'.split(',')}") 
	private Set<String> filterImei;
	
	private Webb webb;

	public static void main(final String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	CommandLineRunner init() {
		webb = Webb.create();
		return this::run;
	}
	
	public void run(final String... args) throws Exception {
		final Stream<String> linesStream;
		if(filterImei==null || filterImei.isEmpty())
			linesStream = Files.lines(Paths.get(gpsInputFile));
		else
			linesStream = Files.lines(Paths.get(gpsInputFile)).filter(l->filterImei.contains(l.split(",")[1]));
		linesStream.forEach(this::doSend);
		linesStream.close();
	}
	
	private void doSend(final String g) {
		try {
			logger.debug("Sending to device-manager pending GPS via HTTP: {}...",g);
			final Response<Void> response = webb.post(deviceManagerPostGpsUrl).body(g).ensureSuccess().asVoid();
			logger.debug("Got from device-manager HTTP status {} for {}",response.getStatusCode(),g);
			Thread.sleep(schedualeIntervalInMillis);
		} catch (final Exception e) {
			logger.error(e.getMessage());
		}
	}
}


