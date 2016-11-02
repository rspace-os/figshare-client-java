package com.researchspace.figshare.impl;



import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.researchspace.figshare.api.FileOperations;
import com.researchspace.figshare.model.FilePresenter;
import com.researchspace.figshare.model.FileUploadProcess;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileOperationsImpl implements FileOperations {
	
	private RestTemplate template;
	private String accessToken;
	private FileSplitter splitter;
	private FigshareUtils utils;

	public FileOperationsImpl(RestTemplate restTemplate, String accessToken) {
		this.template = restTemplate;
		this.accessToken = accessToken;
		this.splitter = new FileSplitter();
		this.utils = new FigshareUtils();
	}

	@Override
	public boolean markFileUploadCompleted(Long articleId, Long fileId) {
		String url = utils.createPath("/account/articles/{articleId}/files/{fileId}");
		HttpEntity<String> entity = utils.createHttpEntity("", accessToken);
		ResponseEntity<String> resp =  template.exchange(url.toString(), HttpMethod.POST, entity, String.class,
				articleId, fileId);
		log.debug(resp.toString());
		return HttpStatus.ACCEPTED.equals(resp.getStatusCode());
	}
	
	@Override
	public FilePresenter getFileUploadInfo(Long articleId, Long fileId) {
		String url = utils.createPath("/account/articles/{articleId}/files/{fileId}");
		HttpEntity<String> entity = utils.createHttpEntity("", accessToken);
		ResponseEntity<FilePresenter> resp = template.exchange(url, HttpMethod.GET, entity, 
				   FilePresenter.class, articleId, fileId);
		log.debug(resp.toString());
		return resp.getBody();		
	}
	
	@Override
	public FileUploadProcess uploadParts(FilePresenter uploadStatus, File dataFile) throws  IOException {
		URL url  = uploadStatus.getUploadURL();
		FileUploadProcess process = template.getForObject(url.toString(), FileUploadProcess.class);
		process.getParts().parallelStream().forEach((part)->{
			 File splitted = null;
			try {
				splitted = splitter.extract(dataFile, part);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 String uploadURL = url.toString() +"/"+ part.getPartNo();
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
				byte[] bytes;
				try {
					bytes = IOUtils.toByteArray(new FileSystemResource(splitted.getAbsolutePath()).getInputStream());
					HttpEntity<byte[]> entity = new HttpEntity<>(bytes, headers);
					ResponseEntity<String> resp = template.exchange(uploadURL, HttpMethod.PUT, entity, 
							  String.class, uploadStatus.getUploadToken(), part.getPartNo());	
					log.debug("Uploaded part {}: {}", part.getPartNo(), resp.getBody());	
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}   
		});	
		log.debug(process.toString());
		return process;		
	}
}
