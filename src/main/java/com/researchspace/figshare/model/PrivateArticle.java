package com.researchspace.figshare.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PrivateArticle {
	private String id;
	
	@JsonProperty("expires_date")
	private Date expiresDate;
	
	@JsonProperty("is_active")
	private boolean isActive;

}
