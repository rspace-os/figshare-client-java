package com.researchspace.figshare.model;

import java.util.List;

import lombok.Data;

@Data
public class FileUploadProcess {
	
	private String token, md5, name, status;
	private Long size;
	private List<FilePart> parts; 
	
	/*
	 * {"token":"dc71eb4f-6fe4-4fa8-8828-0d42f2b280a2",
	 * "md5":"","size":922487,
	 * "name":"6530385/ResizablePng.zip",
	 * "status":"PENDING",
	 * "parts":[{"partNo":1,"startOffset":0,"endOffset":922486,"status":"PENDING","locked":false}]}
	 */

}
