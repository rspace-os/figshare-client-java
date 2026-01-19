package com.researchspace.figshare.impl;

import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;

public class FigshareUtils {

	Logger log = LoggerFactory.getLogger(FigshareUtils.class);

	final String baseURL = "https://api.figshare.com";
	final String version = "/v2";

	 String createPath(String endpoint) {
		return baseURL + version + endpoint;
	}

	HttpEntity<String> createHttpEntity(String body, String personalToken) {
		
		if(!StringUtils.isEmpty(personalToken)) {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			addAPIKeyToHeader(headers, personalToken);
			return new HttpEntity<String>(body, headers);
		} else {
			return new HttpEntity<String>(body);
		}
		
	}

	private void addAPIKeyToHeader(HttpHeaders headers, String personalToken) {
		headers.add("Authorization", " token " + personalToken);
	}
	
	 <T> T readFromString(ResponseEntity<String> resp, Class<T> clazz) {
		ObjectMapper mapper = new ObjectMapper();
		T obj = null;
		try {
			obj = mapper.readValue(resp.getBody(), clazz);
			
		} catch (IOException e) {
			log.warn("IOException on readFromString", e);
		}
		return obj;
	}
}
