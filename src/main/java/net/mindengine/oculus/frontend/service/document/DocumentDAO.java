package net.mindengine.oculus.frontend.service.document;

import java.util.Collection;
import java.util.List;

import net.mindengine.oculus.frontend.domain.db.BrowseResult;
import net.mindengine.oculus.frontend.domain.document.Document;
import net.mindengine.oculus.frontend.domain.document.DocumentAttachment;
import net.mindengine.oculus.frontend.domain.document.DocumentSearchFilter;
import net.mindengine.oculus.frontend.domain.folder.Folder;

public interface DocumentDAO {
	public List<Document> getDocuments(Long projectId, Long folderId) throws Exception;

	public List<Document> getDocumentsByFolderId(Long folderId) throws Exception;

	public Document getDocument(Long id) throws Exception;

	public Long createDocument(Document document) throws Exception;

	public void updateDocument(Document document) throws Exception;

	public void deleteDocument(Long id, Long folderId) throws Exception;

	public BrowseResult<Document> searchDocuments(DocumentSearchFilter filter) throws Exception;
	
	/**
	 * Returns folder for the specified document
	 * @param id Id of document
	 * @return
	 * @throws Exception
	 */
	public Folder getDocumentFolder(Long id) throws Exception;
	
	/**
	 * Moves specified document to folder
	 * @param documentId
	 * @param folder
	 * @param sourceFolder
	 * @throws Exception
	 */
	public void moveDocumentToFolder(Long documentId, Folder folder, Folder sourceFolder) throws Exception;
	
	/**
	 * Moves specified document to project
	 * @param documentId
	 * @param projectId
	 * @param sourceFolder
	 * @throws Exception
	 */
	public void moveDocumentToProject(Long documentId, Long projectId, Folder sourceFolder) throws Exception;
	
	
	public Long createAttachment(DocumentAttachment attachment) throws Exception;
	
	public Collection<DocumentAttachment> getDocumentAttachments(Long documentId) throws Exception;
	
	public DocumentAttachment getAttachment(Long attachmentId) throws Exception;
	
	public void deleteAtachment(Long attachmentId) throws Exception;
}
