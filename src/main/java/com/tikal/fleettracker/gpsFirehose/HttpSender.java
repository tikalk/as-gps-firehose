package com.tikal.fleettracker.gpsFirehose;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.goebl.david.Response;
import com.goebl.david.Webb;

@Component
@Profile("http")
public class HttpSender implements GpsSender{
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(HttpSender.class);

	@Value("${deviceManagerPostGpsUrl}")
	private String deviceManagerPostGpsUrl;
	
	private final Webb webb = Webb.create();
	
	@Override
	public void send(final String g) throws Exception{
		logger.debug("Sending to device-manager pending GPS via HTTP: {}...",g);
		final Response<Void> response = webb.post(deviceManagerPostGpsUrl).body(g).ensureSuccess().asVoid();
		logger.debug("Got from device-manager HTTP status {} for {}",response.getStatusCode(),g);
	}
}


