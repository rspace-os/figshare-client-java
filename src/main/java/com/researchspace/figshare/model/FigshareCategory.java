package com.researchspace.figshare.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FigshareCategory {

	/**
	 * Example category to use when testing
	 */
	public static final FigshareCategory SOFTWARE_TESTING = new FigshareCategory(29200L, 29176L, "Software testing, verification and validation");
	
	Long id;
	
	@JsonProperty("parent_id")
	Long parentId;
	String title;
}
