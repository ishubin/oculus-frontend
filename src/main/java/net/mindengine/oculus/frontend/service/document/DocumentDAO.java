/*******************************************************************************
* 2012 Ivan Shubin http://mindengine.net
* 
* This file is part of MindEngine.net Oculus Frontend.
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with Oculus Experior.  If not, see <http://www.gnu.org/licenses/>.
******************************************************************************/
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
