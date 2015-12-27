package com.tikal.fleettracker.gpsFirehose;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Application.class);

	@Value("${deviceManagerPostGpsUrl}")
	private String deviceManagerPostGpsUrl;
	
	@Value("${schedualeIntervalInMillis}")
	private int schedualeIntervalInMillis;
	
	@Value("${gpsInputFile:/var/log/fleettracker/gps-files}")
	private String gpsInputFolder;
	
	@Autowired
	private GpsSender gpsSender;
	

	public static void main(final String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	CommandLineRunner init() {
		return this::run;
	}
	
	public void run(final String... args) throws Exception {
		Files.list(Paths.get(gpsInputFolder)).filter(f->f.toString().endsWith("txt") && !(f.toString().contains("_"))).forEach(f->new Thread(new VehicleWorker(schedualeIntervalInMillis, f, gpsSender)).start());
	}
	
	
}


