package com.researchspace.figshare.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
/**
 * Article returned from Figshare
 * @author rspace
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder()
public class ArticlePresenter {
	
	String title, description;
	
	private @Singular List<Author> authors;
	
	private List<FigshareCategory>categories;
	
	private List<String> tags;
	
	private List<String> references;
	
	private FigshareLicense license;

}
