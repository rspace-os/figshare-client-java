package com.researchspace.figshare.impl;

import static java.lang.String.format;
import static org.apache.commons.io.FilenameUtils.getBaseName;
import static org.apache.commons.io.FilenameUtils.getExtension;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;

import com.researchspace.figshare.model.FilePart;

import lombok.extern.slf4j.Slf4j;
/**
 * Splits files according to boundaries set in FilePart.
 * <p>
 * Indexes are 0-based, inclusive (from API docs)
 * </p>
 * @author rspace
 *
 */
@Slf4j
public class FileSplitter {

	File extract(File orig, FilePart part) throws IOException {
		
		File tmpDir = FileUtils.getTempDirectory();
		String randomName = RandomStringUtils.randomAlphabetic(10);
		File workFolder = new File(tmpDir, randomName); 
		FileUtils.forceMkdir(workFolder);
		FileUtils.copyFileToDirectory(orig, workFolder);
		File copy = workFolder.listFiles()[0]	;	

		int start = part.getStartOffset().intValue();
		int end = part.getEndOffset().intValue() + 1; //end offset in java is exclusive, need to add 1
		int partNo = part.getPartNo();

		int sizeOfFiles = end - start;
		byte[] buffer = new byte[sizeOfFiles];

		File newFile = new File(workFolder, createPartFileName(orig, partNo));
		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(copy))) {
			bis.skip(start);
			int tmp = 0;
			tmp = bis.read(buffer);			
			try (FileOutputStream out = new FileOutputStream(newFile)) {
				out.write(buffer, 0, tmp);// tmp is chunk size
			}
		} 
		log.debug("Split file is {} of length {}", newFile.getName(), newFile.length());
		return newFile;
	}

	private String createPartFileName(File orig, int partNo) {
		return getBaseName(orig.getName()) + "-"
		       + format("%03d", partNo) + "." + getExtension(orig.getName());
	}

}
