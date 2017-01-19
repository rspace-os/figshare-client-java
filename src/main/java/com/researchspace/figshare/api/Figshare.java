package com.researchspace.figshare.api;

import java.io.File;
import java.util.List;

import com.researchspace.figshare.model.Account;
import com.researchspace.figshare.model.ArticlePost;
import com.researchspace.figshare.model.ArticlePresenter;
import com.researchspace.figshare.model.Category;
import com.researchspace.figshare.model.FigshareError;
import com.researchspace.figshare.model.FigshareResponse;
import com.researchspace.figshare.model.FilePresenter;
import com.researchspace.figshare.model.Location;
import com.researchspace.figshare.model.PrivateArticle;
import com.researchspace.figshare.model.PrivateArticleLink;

/**
 * Top-level access to Figshare API bindings
 * @author rspace
 *
 */
public interface Figshare  {

	/**
	 * Tests connection to Account details - i.e that OAuth token is valid.
	 * @return
	 */
	boolean test();
	
	/**
	 * POSTS a new Article
	 * @param article
	 * @return
	 */
	Location createArticle (ArticlePost article);
	
	/**
	 * GETs an Article by its ID
	 * @param articleId
	 * @return
	 */
	ArticlePresenter getArticle( Long articleId);
	
	/**
	 * Creates a private link to the Article.
	 * @param articleId
	 * @return
	 */
	PrivateArticleLink createPrivateArticleLink (Long articleId);
	
	List<PrivateArticle> getPrivateArticleLinks(Long articleId);
	
	/**
	 * DELETEs an article according to its ID
	 * @param id
	 * @return
	 */
	boolean deleteArticle (Long id);

	/**
	 * GETs basic user account info.
	 * @return
	 */
	Account account();
	
	/**
	 * Creates a new file for the given article.
	 * @param articleId
	 * @param file
	 * @return A {@link Location} with the URL to the created file.
	 */
	Location createFile(Long articleId, File file);
	
	/**
	 * Removes a file associated with an article. 
	 * @param articleId
	 * @param fileId
	 * @return <code>true if it was deleted</code>
	 */
	boolean deleteFile(Long articleId, Long fileId);
	
	/**
	 *  GETs list of {@link FilePresenter} information for files associated with an article.
	 */
	List<FilePresenter> getFiles( Long articleId);
	
	/**
	 * Fine-grained access to underlying  fileupload operations
	 * @return
	 */
	FileOperations getFileOperations ();
	
	/**
	 * Facade method to upload a file to an Article
	 * @param articleId
	 * @param toUpload
	 * @return The {@link Location} of the newly created file.
	 */
	Location uploadFile (Long articleId, File toUpload);
	
	/**
	 * FAcade method that will replace all existing files with the argument file <code>toUpload</code>
	 * <ul>
	 * <li> Delete all files linked to this article
	 * <li> Upload the new file
	 * </ul>
	 * @param articleId
	 * @param toUpload
	 * @return The {@link Location} of the newly created file.
	 */
	Location dropFilesAndReplace (Long articleId, File toUpload);
	
	
	
	
	/**
	 * Gets list of Categories. Currently parent Categories are not returned, only their ids in parentID fields.
	 * @return
	 */
	List<Category> getCategories ();

	/**
	 * Publishes article with given id. <br>
	 * Will fail if Article is missing required fields
	 * @param id
	 * @return If publishing was successful, Location object will contain URL of published article. Otherwise,
	 *  will contain a {@link FigshareError}.
	 */
	FigshareResponse<Location> publishArticle(Long id);

	/**
	 * Deletes the unique link for the given article.
	 * @param articleId
	 * @param uniqueLinkKey
	 */
	void deletePrivateArticleLink(Long articleId, String uniqueLinkKey);
	

}
