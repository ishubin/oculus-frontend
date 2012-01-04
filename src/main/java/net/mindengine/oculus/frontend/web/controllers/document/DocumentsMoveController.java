package net.mindengine.oculus.frontend.web.controllers.document;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.domain.AjaxModel;
import net.mindengine.oculus.frontend.domain.folder.Folder;
import net.mindengine.oculus.frontend.service.document.DocumentDAO;
import net.mindengine.oculus.frontend.service.folder.FolderDAO;
import net.mindengine.oculus.frontend.web.controllers.SimpleAjaxController;

public class DocumentsMoveController extends SimpleAjaxController {

    private DocumentDAO documentDAO;
    private FolderDAO folderDAO;
    
    
    public class MoveResponse{
        private String[] documents;
        private String destination;
        public String[] getDocuments() {
            return documents;
        }
        public void setDocuments(String[] documents) {
            this.documents = documents;
        }
        public String getDestination() {
            return destination;
        }
        public void setDestination(String destination) {
            this.destination = destination;
        }
    }
    
    @Override
    public AjaxModel handleController(HttpServletRequest request) throws Exception {
        verifyPermissions(request);

        AjaxModel model = new AjaxModel();

        String documents = request.getParameter("documents");
        String destination = request.getParameter("destination");

        if (documents == null || documents.isEmpty() || destination == null || destination.isEmpty()) {
            throw new IllegalArgumentException();
        }

        
        String[] docs = documents.split(",");
        
        Long destId = Long.parseLong(destination.substring(1));
        
        
        if (destination.startsWith("p")) {
            for (String doc : docs) {
                if (doc.startsWith("dtc") || doc.startsWith("dfl")) {
                    Long docId = Long.parseLong(doc.substring(3));
                    
                    documentDAO.moveDocumentToProject(docId, destId, documentDAO.getDocumentFolder(docId));
                }
            }
        }
        else if (destination.startsWith("f")) {
            for (String doc : docs) {
                if (doc.startsWith("dtc") || doc.startsWith("dfl")) {
                    Long docId = Long.parseLong(doc.substring(3));
                    
                    documentDAO.moveDocumentToFolder(docId, folderDAO.getFolder(destId), documentDAO.getDocumentFolder(docId));
                }
            }
        }
        else throw new IllegalArgumentException();
        
        MoveResponse response = new MoveResponse();
        response.setDestination(destination);
        response.setDocuments(docs);
        model.setResult("moved");
        model.setObject(response);
        return model;
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
