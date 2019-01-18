package com.researchspace.figshare.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthorTest {

	@Test
	// from figshare suypport: 
	//'We've investigated further and the payload for authors is wrong,
	// as the 'name' filed and 'id' filed should not be used at the same time.  
	public void idAttributeNotIncludedIfNull() throws JsonProcessingException {
		ObjectMapper om = new ObjectMapper();
		Author author = new Author("name", 23);

		assertTrue(om.writeValueAsString(author).contains("id"));

		author = new Author("name", null);

		assertFalse(om.writeValueAsString(author).contains("id"));
	}

}
