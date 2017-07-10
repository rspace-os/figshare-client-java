package com.researchspace.figshare.model;

import java.util.ArrayList;
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
	//required so that list is never empty
	private @Builder.Default List<String> tags= new ArrayList<>();
	
	private @Builder.Default List<String> references = new ArrayList<>();
	
	private Integer license;

}
