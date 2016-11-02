package com.researchspace.figshare.impl;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.stream.IntStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.researchspace.figshare.model.FilePart;

public class FileSplitterTest {
	File dataFile = new File("src/test/resources/ResizablePng.zip");
	FileSplitter splitter = null;
	public @Rule TemporaryFolder tempFolder = new TemporaryFolder();

	@Before
	public void setUp() throws Exception {
		splitter = new FileSplitter();
		FileUtils.copyFileToDirectory(dataFile, tempFolder.getRoot());
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSplit() throws FileNotFoundException, IOException {
		File toCopy = createByteFile(15);
		// each sub-part will be length 5
		FilePart part1 = new FilePart("PENDING", 0L, 4L, false, 1);
		FilePart part2 = new FilePart("PENDING", 5L, 9L, false, 1);
		FilePart part3 = new FilePart("PENDING", 10L, 14L, false, 1);
		File splitted1 = splitter.extract(toCopy, part1);
		File splitted2 = splitter.extract(toCopy, part2);
		File splitted3 = splitter.extract(toCopy, part3);
		byte[] in1 = FileUtils.readFileToByteArray(splitted1);
		byte[] in2 = FileUtils.readFileToByteArray(splitted2);
		byte[] in3 = FileUtils.readFileToByteArray(splitted3);
		assertTrue(ArrayUtils.isEquals(new byte[] { 0, 1, 2, 3, 4 }, in1));
		assertTrue(ArrayUtils.isEquals(new byte[] { 5, 6, 7, 8, 9 }, in2));
		assertTrue(ArrayUtils.isEquals(new byte[] { 10, 11, 12, 13, 14 }, in3));

	}

	private File createByteFile(int length) throws IOException {
		byte[] data = new byte[length];
		IntStream.range(0, length).forEach((j) -> data[j] = (byte) j);
		File rc = File.createTempFile("orig", ".dat");
		FileUtils.writeByteArrayToFile(rc, data);
		return rc;
	}

	@Test
	public void testExtract() throws FileNotFoundException, IOException {
		File toCopy = tempFolder.getRoot().listFiles()[0];
		FilePart part = new FilePart("PENDING", 0L, toCopy.length(), false, 1);
		File splitted = splitter.extract(toCopy, part);
		assertEquals("Not equal" + toCopy.length() + "," + splitted.length(), toCopy.length(), splitted.length());
	}

}
