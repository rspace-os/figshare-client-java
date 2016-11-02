package com.researchspace.figshare.model;

import java.net.URL;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
/**
 * Object returned from GET /account/articles/{articleId}/files/{fileId}
 * @author rspace
 *
 */
@Data
public class FilePresenter {
	private String status, name;
	private Long id, size;
	
	@JsonProperty("is_link_only")
	private boolean linkOnly;
	
	@JsonProperty("viewer_type")
	private String viewerType;
	
	@JsonProperty("preview_state")
	private String previewState;
	
	@JsonProperty("download_url")
	private URL downloadURL;

	@JsonProperty("supplied_md5")
	private String suppliedMD5;

	@JsonProperty("computed_md5")
	private String computedMD5;

	@JsonProperty("upload_token")
	private String uploadToken;

	@JsonProperty("upload_url")
	private URL uploadURL;
}