package com.researchspace.figshare.model;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PrivateArticleLinkTest {

	private static final String EXPECTED_WEB_URL = "https://figshare.com/s/12345";

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetWeblink() throws MalformedURLException {	
		PrivateArticleLink link = new PrivateArticleLink(new URL("http://figshare/someid/12345"));
		assertEquals(EXPECTED_WEB_URL, link.getWeblink().toString());	
	}

}
