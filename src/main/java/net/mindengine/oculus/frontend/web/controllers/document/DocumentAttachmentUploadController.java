package net.mindengine.oculus.frontend.web.controllers.document;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.config.Config;
import net.mindengine.oculus.frontend.domain.document.Document;
import net.mindengine.oculus.frontend.domain.document.DocumentAttachment;
import net.mindengine.oculus.frontend.domain.folder.Folder;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.document.DocumentDAO;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.folder.FolderDAO;
import net.mindengine.oculus.experior.utils.FileUtils;
import net.mindengine.oculus.frontend.web.Session;
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
        Session session = Session.create(request);
        User user = session.getAuthorizedUser();
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
