package com.researchspace.figshare.model;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AccountTest {
	File accountJson = new File("src/test/resources/account.json");

	@Test
	public void institutionalAccountInfoParse() throws IOException {
		String json = FileUtils.readFileToString(accountJson, "UTF-8");
		ObjectMapper reader = new ObjectMapper();
		
		Account account = reader.readValue(json, Account.class);
		assertEquals("c.george@lilliput.ac.uk", account.getInstitutionUserId());
		assertEquals(1344, account.getGroupId().intValue());		
	}

}
