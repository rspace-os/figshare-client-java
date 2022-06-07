package com.researchspace.figshare.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.researchspace.figshare.model.ArticlePresenter;
import org.junit.Test;


import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


public class StringToUrlDeserialiserTest {

    @Test
    public void convertStringToUrl() throws MalformedURLException, JsonProcessingException {
        ArticlePresenter presenter = ArticlePresenter.builder()
                .publicURL(anyURL("public"))
                .publicApiURL(anyURL("publicApi"))
                .privateURL(anyURL("private"))
                .privateApiURL(anyURL("privateapi")).build();
        ObjectMapper mapper = new ObjectMapper();
        String testJson = mapper.writeValueAsString(presenter);

        ArticlePresenter articlePresenterDeserialised = mapper.readValue(testJson, ArticlePresenter.class);
        assertEquals(articlePresenterDeserialised.getPrivateURL(), presenter.getPrivateURL());
        assertEquals(articlePresenterDeserialised.getPublicApiURL(), presenter.getPublicApiURL());
        assertEquals(articlePresenterDeserialised.getPublicURL()    , presenter.getPublicURL());
        assertEquals(articlePresenterDeserialised.getPrivateApiURL(), presenter.getPrivateApiURL());
    }

    @Test
    public void convertStringToUrlHandlesNullValues() throws MalformedURLException, JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        ArticlePresenter articlePresenterDeserialised = mapper.readValue("{}", ArticlePresenter.class);
        assertNull(articlePresenterDeserialised.getPrivateURL() );
        assertNull(articlePresenterDeserialised.getPublicApiURL());
        assertNull(articlePresenterDeserialised.getPublicURL() );
        assertNull(articlePresenterDeserialised.getPrivateApiURL());
    }

    private URL anyURL(String prefix) throws MalformedURLException {
        return new URL("https://" + prefix + ".somewhere.com");
    }

}