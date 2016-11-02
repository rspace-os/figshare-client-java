package com.researchspace.figshare.api;

import java.io.File;
import java.io.IOException;

import com.researchspace.figshare.model.FileUploadProcess;
import com.researchspace.figshare.model.FilePresenter;

public interface FileOperations {
	
	FilePresenter getFileUploadInfo (Long articleId, Long fileId);


	FileUploadProcess uploadParts(FilePresenter uploadStatus, File toUpload) throws  IOException;
	/**
	 * Returns <code>true</code> if accepted for processing, <code>false</code> otherwise
	 * @param articleId
	 * @param fileId
	 * @return
	 */
	boolean markFileUploadCompleted( Long articleId, Long fileId);

}
