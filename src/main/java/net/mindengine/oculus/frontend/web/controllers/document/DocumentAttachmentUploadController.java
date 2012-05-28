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
import net.mindengine.oculus.frontend.domain.document.DocumentAttachment;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.document.DocumentDAO;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;
import net.mindengine.oculus.frontend.service.folder.FolderDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleFormController;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

public class DocumentAttachmentUploadController extends SecureSimpleFormController {
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
            DocumentAttachment attachment = (DocumentAttachment) command;
            attachment.setUserId(user.getId());
            attachment.setDate(new Date());

            MultipartFile multipartFile = attachment.getFile();

            attachment.setSize(multipartFile.getSize());
            String fileName = multipartFile.getOriginalFilename();
            String fileType = FileUtils.getFileType(fileName);
            attachment.setType(fileType);

            if (attachment.getName() == null || attachment.getName().equals("")) {
                attachment.setName(FileUtils.getFileSimpleName(fileName));
            }

            Long attachmentId = documentDAO.createAttachment(attachment);
            attachment.setId(attachmentId);

            String path = config.getDocumentsFolder() + File.separator + FileUtils.generatePath(attachment.getDate());

            FileUtils.mkdirs(path);

            File file = new File(path + File.separator + "attachment" + attachmentId);
            file.createNewFile();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(multipartFile.getBytes());
            fos.close();
            mav.addObject("resultAction", "attachment-uploaded");
            mav.addObject("attachment", attachment);
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
