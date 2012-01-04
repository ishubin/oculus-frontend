package net.mindengine.oculus.frontend.web.controllers.document;

import java.io.File;
import java.io.FileInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.config.Config;
import net.mindengine.oculus.frontend.domain.document.DocumentAttachment;
import net.mindengine.oculus.frontend.service.document.DocumentDAO;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.experior.utils.FileUtils;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class DocumentAttachmentDownloadController implements Controller {
    private DocumentDAO documentDAO;
    private Config config;

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String strId = request.getParameter("id");
        Long id = Long.parseLong(strId);

        DocumentAttachment attachment = documentDAO.getAttachment(id);
        if (attachment == null)
            throw new UnexistentResource("Attachment", id);
        
        String path = config.getDocumentsFolder() + File.separator + FileUtils.generatePath(attachment.getDate()) + File.separator +"attachment"+ attachment.getId();

        File file = new File(path);
        response.setBufferSize((int) file.length());
        String fileName = attachment.getName();
        if (attachment.getType() != null && !attachment.getType().isEmpty()) {
            fileName += "." + attachment.getType();
        }
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.setContentType("application/download");
        response.setContentLength((int) file.length());

        byte[] bytes = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(bytes);
        fis.close();

        FileCopyUtils.copy(bytes, response.getOutputStream());

        return null;
    }

    public void setDocumentDAO(DocumentDAO documentDAO) {
        this.documentDAO = documentDAO;
    }

    public DocumentDAO getDocumentDAO() {
        return documentDAO;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }

}