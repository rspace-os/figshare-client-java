package com.researchspace.figshare.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.researchspace.figshare.model.Account;
import com.researchspace.figshare.model.ArticlePost;
import com.researchspace.figshare.model.ArticlePresenter;
import com.researchspace.figshare.model.Author;
import com.researchspace.figshare.model.FigshareCategory;
import com.researchspace.figshare.model.FigshareResponse;
import com.researchspace.figshare.model.FigshareLicense;
import com.researchspace.figshare.model.Location;
import com.researchspace.figshare.model.PrivateArticle;
import com.researchspace.figshare.model.PrivateArticleLink;

/**
 * Makes test calls using personal token
 *
 * @author rspace
 */

@FigshareSpringTest
public class FigshareAcceptanceTest extends AbstractJUnit4SpringContextTests {

    Logger log = LoggerFactory.getLogger(FigshareAcceptanceTest.class);

    private Figshare templateProxy;
    @Autowired
    private Figshare template;

    File dataFile = new File("src/test/resources/ResizablePng.zip");
    File smallFile = new File("src/test/resources/bytes.dat");

    // proxy object to interpose a delay between requests
    private class DelayAPIExecutor<T> implements InvocationHandler {
        private static final int ONE_SECOND = 1000;
        private T object;

        public DelayAPIExecutor(T object) {
            super();
            this.object = object;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Thread.currentThread().sleep(ONE_SECOND);
            try {
                return method.invoke(object, args);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
        }
    }

    @Before
    public void setUp() throws Exception {
        templateProxy = setUpDelayExecutingProxy();
        FileUtils.writeByteArrayToFile(smallFile, new byte[]{1, 2, 3, 4, 5});
    }

    private Figshare setUpDelayExecutingProxy() {
        return (Figshare) Proxy
                .newProxyInstance(FigshareAcceptanceTest.class.getClassLoader(),
                        template.getClass().getInterfaces(), new DelayAPIExecutor<Figshare>(template));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testTest() {
        assertTrue(templateProxy.test());
    }

    @Test
    public void testCategories() {
        List<FigshareCategory> categories = templateProxy.getCategories(false);
        assertTrue(categories.size() > 20);
        // this is to see if any parent categories are returned in the list - currently they're not.
        List<FigshareCategory> parents = new ArrayList<>();
        categories.stream().forEach(cat -> {
            parents.addAll(categories.stream().filter(
                    c -> c.getId().equals(cat.getParentId())).collect(Collectors.toList()));
        });
        parents.stream().forEach(c -> System.out.println(c));
    }

    @Test
    public void testLicenses() {
        List<FigshareLicense> licenses = templateProxy.getLicenses(false);
        assertTrue(licenses.size() > 5);
        licenses.stream().forEach(lic -> System.out.println(lic));
    }

    @Test
    public void testProfile() {
        Account account = templateProxy.account();
        assertNotNull(account.getEmail());
    }

    @Test
    public void testCreateAndDeleteArticle() {
        ArticlePost article = createFullArticle();
        Location articleLoc = templateProxy.createArticle(article);
        assertNotNull(articleLoc.getId());
        ArticlePresenter article2 = templateProxy.getArticle(articleLoc.getId());
        assertEquals(article.getDescription(), article2.getDescription());
        assertTrue(article2.getLicense().getUrl().toString().toUpperCase().contains("GPL"));
        assertEquals("Software testing, verification and validation", article2.getCategories().get(0).getTitle());
        assertNotNull(article2.getPrivateURL());
        assertTrue(templateProxy.deleteArticle(articleLoc.getId()));
    }

    @Test
    public void testCreateAndDeleteFile() {
        ArticlePost article = createFullArticle();
        Location articleLoc = templateProxy.createArticle(article);
        Location file = null;
        try {
            file = templateProxy.uploadFile(articleLoc.getId(), smallFile);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        assertEquals(1, templateProxy.getFiles(articleLoc.getId()).size());
        if (file != null) {
            assertTrue(templateProxy.deleteFile(articleLoc.getId(), file.getId()));
            assertEquals(0, templateProxy.getFiles(articleLoc.getId()).size());
        }
        assertTrue(templateProxy.deleteArticle(articleLoc.getId()));
    }

    @Test
    public void testDropFilesAndReplace() {
        ArticlePost article = createFullArticle();
        Location articleLoc = templateProxy.createArticle(article);
        Location file1 = null, file2 = null;
        try {
            file1 = templateProxy.uploadFile(articleLoc.getId(), smallFile);
            file2 = templateProxy.uploadFile(articleLoc.getId(), dataFile);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        assertEquals(2, templateProxy.getFiles(articleLoc.getId()).size());
        Location newFile = templateProxy.dropFilesAndReplace(articleLoc.getId(), smallFile);
        assertFalse(newFile.getId().equals(file1.getId()));
        assertFalse(newFile.getId().equals(file2.getId()));
        assertEquals(1, templateProxy.getFiles(articleLoc.getId()).size());
        assertTrue(templateProxy.deleteArticle(articleLoc.getId()));
    }

    // note this test requires the testing Figshare account to be connected to an Orcid account.
    // It will fail otherwise.
    @Test
    public void testPrivateLinkArticleCRUD() {
        ArticlePost article = createFullArticle();
        Location articleLoc = templateProxy.createArticle(article);
        assertNotNull(articleLoc.getId());
        try {
            assertEquals(0, templateProxy.getPrivateArticleLinks(articleLoc.getId()).size());
            PrivateArticleLink privateLink = templateProxy.createPrivateArticleLink(articleLoc.getId());
            assertNotNull(privateLink.getWeblink());
            assertEquals(1, templateProxy.getPrivateArticleLinks(articleLoc.getId()).size());
            // calling again just overwrites
            privateLink = templateProxy.createPrivateArticleLink(articleLoc.getId());
            List<PrivateArticle> links = templateProxy.getPrivateArticleLinks(articleLoc.getId());
            String key = links.get(0).getId();
            templateProxy.deletePrivateArticleLink(articleLoc.getId(), key);
            assertEquals(0, templateProxy.getPrivateArticleLinks(articleLoc.getId()).size());
        } finally {
            // tidy up
            templateProxy.deleteArticle(articleLoc.getId());
        }
    }

    @Test
    @Ignore
    //TODO this seems to get published, so we should try to unpublish or cancel this test
    public void testPublishArticle() {
        ArticlePost article = createFullArticle();
        Location articleLoc = templateProxy.createArticle(article);
        assertNotNull(articleLoc.getId());
        // set to fail, so we don't publish lots of things in tests
        try {
            FigshareResponse<Location> published = templateProxy.publishArticle(articleLoc.getId());
            assertNotNull(published.getError());
            log.warn(published.getError().toString());
        } finally {
            // tidy up
            templateProxy.deleteArticle(articleLoc.getId());
        }
    }


    private ArticlePost createFullArticle() {
        ArticlePost article = ArticlePost.builder().title("Title").description("Description")
                .author(new Author("Bob Jones", null))
                .tags(Arrays.asList(new String[]{"rspace"}))
                .categories(Arrays.asList(new Long[]{ FigshareCategory.SOFTWARE_TESTING.getId()}))
                .license(4) //GPL
                .build();
        return article;
    }

}
