package net.mindengine.oculus.frontend.service.folder;

import java.util.List;

import net.mindengine.oculus.frontend.domain.folder.Folder;

public interface FolderDAO {
	/**
	 * This method should be implemented not only with creating new folder but
	 * also with updating the parent folders "children" columns by increasing it
	 * 
	 * @param folder
	 * @return
	 * @throws Exception
	 */
	public Long createFolder(Folder folder) throws Exception;

	public List<Folder> getFoldersByParent(Long parentId) throws Exception;

	public Folder getFolder(Long id) throws Exception;

	public List<Folder> getFolders(Long projectId, Long folderId) throws Exception;

	/**
	 * Deletes folder and and all other folders and documents which have its id
	 * in the branch Also it updates the parents folder children counter
	 * 
	 * @param id
	 * @throws Exception
	 */
	public void deleteFolder(Long id, Long parentId) throws Exception;

	public void renameFolder(Long id, String name) throws Exception;
}
