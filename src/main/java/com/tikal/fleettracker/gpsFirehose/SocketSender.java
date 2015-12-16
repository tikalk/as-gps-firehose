package com.tikal.fleettracker.gpsFirehose;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("socket")
public class SocketSender implements GpsSender{
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(SocketSender.class);
	private final Map<String, PrintWriter> writers = new HashMap<>();
	
	@Value("${deviceManagerNetServer}")
	private String deviceManagerNetServer;
	
	@Value("${deviceManagerNetPort}")
	private int deviceManagerNetPort;
	
	@Override
	public void send(final String g) throws Exception{
		logger.debug("Sending to device-manager pending GPS via Socket: {}...",g);	
		final String imei = g.split(",")[2];
		PrintWriter writer = writers.get(imei);
		if(writer==null){
			logger.debug("Open a new Socket for imei {}",imei);
			final Socket socket = new Socket(deviceManagerNetServer, deviceManagerNetPort);
			writer = new PrintWriter(socket.getOutputStream(),true);
			writers.put(imei, writer);
		}
		writer.println(g);
		logger.debug("Sent succfully the gps to socet. gos is {}",g);
	}

}
