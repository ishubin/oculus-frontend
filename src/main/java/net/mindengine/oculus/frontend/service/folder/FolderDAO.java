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
* along with Oculus Frontend.  If not, see <http://www.gnu.org/licenses/>.
******************************************************************************/
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
