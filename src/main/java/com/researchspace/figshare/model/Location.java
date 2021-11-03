package com.researchspace.figshare.model;

import static org.apache.commons.io.FilenameUtils.getName;

import java.net.URL;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {

	private URL location;
	private List<String> warnings;

	/**
	 * Gets numerical id from last path segment of URL
	 * 
	 * @return
	 */
	public Long getId() {
		if (location != null) {
			return Long.parseLong(getName(location.getPath()));
		}
		return null;
	}

}
