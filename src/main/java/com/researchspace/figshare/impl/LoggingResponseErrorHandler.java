package com.researchspace.figshare.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * Logs a 400 response instead of throwing exception, so we can capture the
 * error message from Json
 * 
 * @author rspace
 * @see "http://springinpractice.com/2013/10/07/handlingjson-error-object-responses-with-springs-resttemplate"
 */
@Slf4j
public class LoggingResponseErrorHandler extends DefaultResponseErrorHandler {

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		log.error("Response error: {} {}", response.getStatusCode(), response.getStatusText());
		try (InputStream bodyStream = response.getBody()) {
			String body = new String(bodyStream.readAllBytes(), StandardCharsets.UTF_8);
			log.error(body);
		}
		// if forbidden we have a bad access token and should throw exception.
		if(response.getStatusCode().equals(HttpStatus.FORBIDDEN)) {
			super.handleError(response);
		}
	}

}
