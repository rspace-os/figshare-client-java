package com.researchspace.figshare.model;

import java.util.List;

import lombok.Data;

@Data
public class FileUploadProcess {
	
	private String token, md5, name, status;
	private Long size;
	private List<FilePart> parts; 

}
