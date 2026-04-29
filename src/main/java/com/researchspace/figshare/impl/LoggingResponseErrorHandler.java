package com.researchspace.figshare.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpClientErrorException;

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
		byte[] bodyBytes = response.getBody().readAllBytes();
		log.error(new String(bodyBytes, StandardCharsets.UTF_8));
		// if forbidden we have a bad access token and should throw exception.
		// We throw directly here rather than delegating to super.handleError,
		// because the stream has already been consumed above.
		if (response.getStatusCode().equals(HttpStatus.FORBIDDEN)) {
			throw HttpClientErrorException.create(
					HttpStatus.FORBIDDEN, response.getStatusText(),
					response.getHeaders(), bodyBytes, StandardCharsets.UTF_8);
		}
	}

}
