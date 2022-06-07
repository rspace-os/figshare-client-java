package com.researchspace.figshare.model;

import java.net.URL;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.researchspace.figshare.converters.StringToUrlDeserialiser;
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

	@JsonProperty("url_public_html")
	@JsonDeserialize(using = StringToUrlDeserialiser.class)
	private URL publicURL;

	@JsonProperty("url_private_html")
	@JsonDeserialize(using = StringToUrlDeserialiser.class)
	private URL privateURL;

	@JsonProperty("url_public_api")
	@JsonDeserialize(using = StringToUrlDeserialiser.class)
	private URL publicApiURL;

	@JsonProperty("url_private_api")
	@JsonDeserialize(using = StringToUrlDeserialiser.class)
	private URL privateApiURL;



}
