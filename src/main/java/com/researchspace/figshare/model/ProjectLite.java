package com.researchspace.figshare.model;

import java.net.URL;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Minimal information about a project
 * @author rspace
 *
 */
@Data
public class ProjectLite {
	private Long id;
	private String title;
	private String role;
	private String storage;
	@JsonProperty("published_date")
	private Date publishedDate;
	
	private URL url;

}
