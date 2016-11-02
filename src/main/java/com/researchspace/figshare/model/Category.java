package com.researchspace.figshare.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {	

	/**
	 * Default Category
	 */
	public static final Category UNCATEGORIZED = new Category(2L, 30L, "Uncategorized");
	Long id;
	@JsonProperty("parent_id")
	Long parentId;
	String title;	
}
