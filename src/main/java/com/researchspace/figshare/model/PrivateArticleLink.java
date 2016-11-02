package com.researchspace.figshare.model;

import static org.apache.commons.io.FilenameUtils.getName;

import java.net.MalformedURLException;
import java.net.URL;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Holds methods to create a link ot webpage from an ID returned from a webservice call.
 * @author rspace
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrivateArticleLink {
	
	public  static final String  URL_PREFIX = "https://figshare.com/s/";
	
	private URL  location;
	
	public URL getWeblink (){
		try {
			URL url = new URL(URL_PREFIX + getName(location.toString()));
			return url;
		} catch (MalformedURLException e) {
			throw new IllegalStateException(getErrorMessage());
		}
	}

	private String getErrorMessage() {
		return String.format("Could not create URl from prefix %s and id %s", URL_PREFIX, location);
	}

}
