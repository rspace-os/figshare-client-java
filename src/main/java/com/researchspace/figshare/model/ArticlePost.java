package com.researchspace.figshare.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
/**
 * Object to post to Figshare
 * @author rspace
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder()
public class ArticlePost {
	
	String title, description;
	
	private @Singular List<Author> authors;
	
	private @Singular List<Integer>categories;
	
	private List<String> tags;
	
	private List<String> references;
	
	private Integer license;

}
