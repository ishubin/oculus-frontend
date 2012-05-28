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
package net.mindengine.oculus.frontend.web.controllers.document;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.experior.utils.FileUtils;
import net.mindengine.oculus.frontend.config.Config;
import net.mindengine.oculus.frontend.domain.document.Document;
import net.mindengine.oculus.frontend.domain.document.FileUpload;
import net.mindengine.oculus.frontend.domain.folder.Folder;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.document.DocumentDAO;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.folder.FolderDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleFormController;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 * This controller is used to save and upload file documents
 * 
 * @author Ivan Shubin
 * 
 */
public class DocumentFileUploadController extends SecureSimpleFormController {
	private Config config;
	private DocumentDAO documentDAO;
	private FolderDAO folderDAO;

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		ModelAndView mav = new ModelAndView(getSuccessView());
		User user = getUser(request);
		if (user == null)
			throw new NotAuthorizedException();

		verifyPermissions(request);
		try {
			FileUpload fileUpload = (FileUpload) command;
			fileUpload.setUserId(user.getId());
			fileUpload.setType(Document._TYPE_FILE);
			if (fileUpload.getFolderId() != null && !fileUpload.getFolderId().equals(0l)) {
				Folder folder = folderDAO.getFolder(fileUpload.getFolderId());
				if (folder == null)
					throw new UnexistentResource("Fodler with id '" + fileUpload.getFolderId() + "' doesn't exist");
				fileUpload.setBranch(folder.getBranch());
			}

			fileUpload.setDate(new Date());

			MultipartFile multipartFile = fileUpload.getFile();

			fileUpload.setSize(multipartFile.getSize());
			String fileName = multipartFile.getOriginalFilename();
			String fileType = FileUtils.getFileType(fileName);
			fileUpload.setTypeExtended(fileType);

			if (fileUpload.getName() == null || fileUpload.getName().equals("")) {
				fileUpload.setName(FileUtils.getFileSimpleName(fileName));
			}

			Long documentId = documentDAO.createDocument(fileUpload);
			fileUpload.setId(documentId);

			String path = config.getDocumentsFolder() + File.separator + FileUtils.generatePath(fileUpload.getDate());

			FileUtils.mkdirs(path);

			File file = new File(path + File.separator + documentId);
			file.createNewFile();

			FileOutputStream fos = new FileOutputStream(file);
			fos.write(multipartFile.getBytes());
			fos.close();
			mav.addObject("resultAction", "file-uploaded");
			mav.addObject("document", fileUpload);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			Map<String, String> error = new HashMap<String, String>();
			error.put("type", ex.getClass().getName());
			error.put("text", StringEscapeUtils.escapeJavaScript(ex.getMessage()));
			mav.addObject("error", error);
		}

		return mav;
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public DocumentDAO getDocumentDAO() {
		return documentDAO;
	}

	public void setDocumentDAO(DocumentDAO documentDAO) {
		this.documentDAO = documentDAO;
	}

	public FolderDAO getFolderDAO() {
		return folderDAO;
	}

	public void setFolderDAO(FolderDAO folderDAO) {
		this.folderDAO = folderDAO;
	}

}
