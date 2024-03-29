package com.researchspace.figshare.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.researchspace.figshare.api.Figshare;
import com.researchspace.figshare.api.FileOperations;
import com.researchspace.figshare.model.Account;
import com.researchspace.figshare.model.ArticlePost;
import com.researchspace.figshare.model.ArticlePresenter;
import com.researchspace.figshare.model.FigshareCategory;
import com.researchspace.figshare.model.FigshareError;
import com.researchspace.figshare.model.FigshareResponse;
import com.researchspace.figshare.model.FilePresenter;
import com.researchspace.figshare.model.FigshareLicense;
import com.researchspace.figshare.model.Location;
import com.researchspace.figshare.model.PrivateArticle;
import com.researchspace.figshare.model.PrivateArticleLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;

public final class FigshareTemplate implements Figshare {

	Logger log = LoggerFactory.getLogger(FigshareTemplate.class);

	private String accessToken;

	private FileOperations fileOps;
	private FigshareUtils utils;

	protected RestTemplate getRestTemplate() {
		return restTemplate;
	}

	/**
	 * Set alternative RestTemplate
	 * @param restTemplate
	 */
	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	private RestTemplate restTemplate;
	
	/**
	 * Set  token for use e.g. in testing.
	 * 
	 * @param accessToken
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public FigshareTemplate(String accessToken) {
		this.accessToken = accessToken;
		init();
	}

	protected void postConstructionConfiguration() {
		init();
	}

	private void init() {
		this.restTemplate = new RestTemplate();
		configureRestTemplate();
		this.fileOps = new FileOperationsImpl(restTemplate, accessToken);
		this.utils = new FigshareUtils();
	}

	public void setFileOps(FileOperations fileOps) {
		this.fileOps = fileOps;
	}

	@Override
	public boolean test() {
		return account().getEmail() != null;
	}

	@Override
	public Location createArticle(ArticlePost article) {
		String url = utils.createPath("/account/articles");
		String json = marshalObject(article);
		HttpEntity<String> entity = utils.createHttpEntity(json, accessToken);
		ResponseEntity<String> resp = getRestTemplate().postForEntity(url, entity, String.class);
		log.debug(resp.toString());
		// can't convert directly as content type of returned is text/html?
		Location location = utils.readFromString(resp, Location.class);
		return location;
	}

	@Override
	public Location createFile(Long articleId, File file) {
		String url = utils.createPath("/account/articles/{articleId}/files");
		ObjectNode node = createFileJson(file);
		HttpEntity<String> entity = utils.createHttpEntity(marshalObject(node), accessToken);
		ResponseEntity<String> resp = getRestTemplate().postForEntity(url, entity, String.class, articleId);
		log.debug(resp.toString());
		// can't convert directly as content type of returned is text/html?
		Location location = utils.readFromString(resp, Location.class);
		return location;
	}

	private ObjectNode createFileJson(File file) {
		JsonNodeFactory nodeFactory = new JsonNodeFactory(false);
		ObjectNode node = nodeFactory.objectNode();
		node.put("size", file.length());
		node.put("name", file.getName());
		return node;
	}

	@Override
	public Account account() {
		String url = utils.createPath("/account");
		ResponseEntity<Account> resp = getRestTemplate().exchange(url, HttpMethod.GET, createEmptyEntity(), Account.class);
		log.debug(resp.toString());
		return resp.getBody();
	}

	@Override
	public boolean deleteArticle(Long id) {
		String url = utils.createPath("/account/articles/{id}");
		ResponseEntity<String> resp = getRestTemplate().exchange(url, HttpMethod.DELETE, createEmptyEntity(), String.class, id);
		log.debug(resp.toString());
		return NO_CONTENT.equals(resp.getStatusCode());
	}
	@Override
	public boolean deleteFile(Long articleId, Long fileId) {
		String url = utils.createPath("/account/articles/{id}/files/{file_id}");
		ResponseEntity<String> resp = getRestTemplate().exchange(url, HttpMethod.DELETE, createEmptyEntity(), String.class, articleId, fileId);
		log.debug(resp.toString());
		return NO_CONTENT.equals(resp.getStatusCode());
	}

	private String marshalObject(Object object) {
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

	@Override
	public FileOperations getFileOperations() {
		return fileOps;
	}

	@Override
	public Location uploadFile(Long articleId, File toUpload) {
		Location newFile = createFile(articleId, toUpload);
		FilePresenter status = fileOps.getFileUploadInfo(articleId, newFile.getId());
		try {
			fileOps.uploadParts(status, toUpload);
		} catch (IOException e) {
			throw new RestClientException(
					String.format("Problem uploading file %s - %s", toUpload.getName(), e.getMessage()), e);
		}
		fileOps.markFileUploadCompleted(articleId, newFile.getId());
		return newFile;
	}
	
	@Override
	public Location dropFilesAndReplace(Long articleId, File toUpload) {
		List<FilePresenter> filesToDelete = getFiles(articleId);
		filesToDelete.stream().forEach((file)->deleteFile(articleId, file.getId()));
		return uploadFile(articleId, toUpload);
	}

	@Override
	public List<FilePresenter> getFiles(Long articleId) {
		String url = utils.createPath("/account/articles/{id}/files");
		ParameterizedTypeReference<List<FilePresenter>> pt = new ParameterizedTypeReference<List<FilePresenter>>() {
		};
		ResponseEntity<List<FilePresenter>> resp = getRestTemplate().exchange(url, HttpMethod.GET, createEmptyEntity(), pt,
				articleId);
		return resp.getBody();
	}

	@Override
	public ArticlePresenter getArticle(Long articleId) {
		String url = utils.createPath("/account/articles/{id}");
		ResponseEntity<ArticlePresenter> resp = getRestTemplate().exchange(url, HttpMethod.GET, createEmptyEntity(),
				ArticlePresenter.class, articleId);
		log.debug(resp.getBody().toString());
		return resp.getBody();
	}
	
	protected void configureRestTemplate() {
		restTemplate.setErrorHandler(new LoggingResponseErrorHandler());
	}

	protected ByteArrayHttpMessageConverter getByteArrayMessageConverter() {
		ByteArrayHttpMessageConverter converter = new ByteArrayHttpMessageConverter();
		converter.setSupportedMediaTypes(Arrays.asList(MediaType.ALL));
		return converter;
	}

	@Override
	public List<FigshareCategory> getCategories(boolean useAccountCategories) {
		String url = utils.createPath("/categories");
		// Flag here is used to retrieve private categories for this account if set, see here: https://docs.figshare.com/#private_categories_list
		if(useAccountCategories) {
			url = utils.createPath("/account/categories");
		}

		ParameterizedTypeReference<List<FigshareCategory>> pt = new ParameterizedTypeReference<List<FigshareCategory>>() {
		};
		ResponseEntity<List<FigshareCategory>> resp = getRestTemplate().exchange(url, HttpMethod.GET, createEmptyEntity(), pt);
		List<FigshareCategory> cats = resp.getBody();
		return cats;
	}
	
	@Override
	public List<FigshareLicense> getLicenses(boolean useAccountLicenses) {
		String url = utils.createPath("/licenses");
		// Flag here is used to retrieve private licenses for this account if set, see here: https://docs.figshare.com/#private_licenses_list
		if(useAccountLicenses) {
			url = utils.createPath("/account/licenses");
		}
		ParameterizedTypeReference<List<FigshareLicense>> pt = new ParameterizedTypeReference<List<FigshareLicense>>() {
		};
		ResponseEntity<List<FigshareLicense>> resp = getRestTemplate().exchange(url, HttpMethod.GET, createEmptyEntity(), pt);
		List<FigshareLicense> licenses = resp.getBody();
		//set default license which is CC-BY as of Feb 2017
		licenses.stream().filter((l)->l.getUrl().toString().equals(DEFAULT_LICENSE_URL))
		                 .findFirst().ifPresent(l -> l.setDefaultLicense(true));
		    
		return licenses;
	}

	@Override
	public FigshareResponse<Location> publishArticle(Long id) {
		String url = utils.createPath("/account/articles/{id}/publish");
		ResponseEntity<String> resp = getRestTemplate().postForEntity(url, createEmptyEntity(), String.class, id);
		if(resp.getStatusCode().is2xxSuccessful()) {
			Location location = utils.readFromString(resp, Location.class);
			return new FigshareResponse<Location>( location, null);
		} else {			
			FigshareError error = utils.readFromString(resp, FigshareError.class);
			error.setStatus(resp.getStatusCode());
			return new FigshareResponse<Location>( null, error);
		}
	}

	@Override
	public PrivateArticleLink createPrivateArticleLink(Long articleId) {
		String url = utils.createPath("/account/articles/{id}/private_links");
		ResponseEntity<String> resp = getRestTemplate().postForEntity(url, createEmptyEntity(), String.class, articleId);
		PrivateArticleLink location = utils.readFromString(resp, PrivateArticleLink.class);
		log.debug(resp.getBody().toString());
		return location;
	}

	@Override
	public List<PrivateArticle> getPrivateArticleLinks(Long articleId) {
		String url = utils.createPath("/account/articles/{id}/private_links");
		ParameterizedTypeReference<List<PrivateArticle>> pt = new ParameterizedTypeReference<List<PrivateArticle>>() {
		};
		ResponseEntity<List<PrivateArticle>> resp = getRestTemplate().exchange(url, HttpMethod.GET, createEmptyEntity(), pt, articleId);
		log.debug(resp.getBody().toString());
		return resp.getBody();
	}
	
	@Override
	public void deletePrivateArticleLink(Long articleId, String uniqueLinkKey) {
		String url = utils.createPath("/account/articles/{id}/private_links/{private_link_id}");
		ResponseEntity<String> resp = getRestTemplate().exchange(url,
				HttpMethod.DELETE, createEmptyEntity(), String.class, articleId, uniqueLinkKey);
		log.debug(resp.getStatusCode().name());
	}

	// personal token used for testing and is added to the Authorisation Header.
	private HttpEntity<String> createEmptyEntity() {
		HttpEntity<String> entity = utils.createHttpEntity("", accessToken);
		return entity;
	}

}
