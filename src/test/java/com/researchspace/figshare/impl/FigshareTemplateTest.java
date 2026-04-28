package com.researchspace.figshare.impl;

import com.researchspace.figshare.model.ArticlePost;
import com.researchspace.figshare.model.Location;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpClientErrorException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

/**
 * Unit tests for FigshareTemplate using MockRestServiceServer.
 *
 * These tests reproduce the RSDEV-1081 bug: POST requests to the Figshare API
 * were missing the Content-Type: application/json header. Combined with a
 * LoggingResponseErrorHandler that closed the response body stream, this caused
 * "java.io.IOException: stream is closed" when the server rejected the request.
 */
public class FigshareTemplateTest {

    private static final String TEST_TOKEN = "test-token";
    private static final String BASE_URL = "https://api.figshare.com/v2";
    private static final String ARTICLE_URL = BASE_URL + "/account/articles";
    private static final long ARTICLE_ID = 12345L;
    private static final long FILE_ID = 67890L;

    private FigshareTemplate figshareTemplate;
    private MockRestServiceServer mockServer;

    @Before
    public void setUp() {
        figshareTemplate = new FigshareTemplate(TEST_TOKEN);
        mockServer = MockRestServiceServer.createServer(figshareTemplate.getRestTemplate());
    }

    /**
     * Reproduces the core bug from RSDEV-1081: createArticle must send
     * Content-Type: application/json so the Figshare API accepts the request.
     * Before the fix this test failed because the header was missing.
     */
    @Test
    public void testCreateArticle_sendsContentTypeApplicationJson() {
        mockServer.expect(requestTo(ARTICLE_URL))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andRespond(withStatus(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"location\": \"https://api.figshare.com/v2/account/articles/" + ARTICLE_ID + "\"}"));

        ArticlePost article = ArticlePost.builder().title("Test article").description("Description").build();
        Location location = figshareTemplate.createArticle(article);

        assertNotNull(location);
        assertEquals(Long.valueOf(ARTICLE_ID), location.getId());
        mockServer.verify();
    }

    /**
     * When the server returns a 400 error, LoggingResponseErrorHandler logs it and
     * returns without throwing (for non-403 errors). The caller receives a null
     * location. Before the fix, the handler's stream-close bug caused a
     * ResourceAccessException with "stream is closed" instead.
     */
    @Test
    public void testCreateArticle_400ResponseIsLoggedWithoutException() {
        mockServer.expect(requestTo(ARTICLE_URL))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withBadRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"message\": \"Invalid article data\", \"code\": 400}"));

        ArticlePost article = ArticlePost.builder().title("Test").build();
        // Should not throw ResourceAccessException("stream is closed")
        Location location = figshareTemplate.createArticle(article);
        assertNull(location);
        mockServer.verify();
    }

    /**
     * 403 Forbidden must propagate as an exception (bad token scenario).
     */
    @Test(expected = HttpClientErrorException.class)
    public void testCreateArticle_403ResponseThrowsException() {
        mockServer.expect(requestTo(ARTICLE_URL))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.FORBIDDEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"message\": \"Forbidden\", \"code\": 403}"));

        ArticlePost article = ArticlePost.builder().title("Test").build();
        figshareTemplate.createArticle(article);
    }

    /**
     * createFile must also send Content-Type: application/json — it sends a JSON
     * body describing the file metadata. Before the fix this header was missing.
     */
    @Test
    public void testCreateFile_sendsContentTypeApplicationJson() throws IOException {
        File tempFile = File.createTempFile("figshare-test", ".dat");
        tempFile.deleteOnExit();
        Files.write(tempFile.toPath(), new byte[]{1, 2, 3, 4, 5});

        String fileUrl = ARTICLE_URL + "/" + ARTICLE_ID + "/files";
        mockServer.expect(requestTo(fileUrl))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andRespond(withStatus(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"location\": \"" + BASE_URL + "/account/articles/" + ARTICLE_ID + "/files/" + FILE_ID + "\"}"));

        Location fileLocation = figshareTemplate.createFile(ARTICLE_ID, tempFile);

        assertNotNull(fileLocation);
        assertEquals(Long.valueOf(FILE_ID), fileLocation.getId());
        mockServer.verify();
    }
}
