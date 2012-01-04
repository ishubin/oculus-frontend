package net.mindengine.oculus.frontend.web.controllers.document;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.config.Config;
import net.mindengine.oculus.frontend.db.search.ColumnFactory;
import net.mindengine.oculus.frontend.domain.db.BrowseResult;
import net.mindengine.oculus.frontend.domain.document.Document;
import net.mindengine.oculus.frontend.domain.document.DocumentSearchColumn;
import net.mindengine.oculus.frontend.domain.document.DocumentSearchFilter;
import net.mindengine.oculus.frontend.service.customization.CustomizationDAO;
import net.mindengine.oculus.frontend.service.customization.CustomizationUtils;
import net.mindengine.oculus.frontend.service.document.DocumentDAO;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.service.report.ExcelTestSearchReportGenerator;
import net.mindengine.oculus.frontend.service.user.UserDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleFormController;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

public class TestCaseSearchExportXLSController extends SecureSimpleFormController {

    private DocumentDAO documentDAO;
    private ProjectDAO projectDAO;
    private CustomizationDAO customizationDAO;
    private UserDAO userDAO;
    private ExcelTestSearchReportGenerator reportGenerator;
    
    /**
     * This is a factory that is used to fetch the default list of columns to
     * display in report table in case if column list wasn't defined previously
     * in session
     */
    private ColumnFactory columnFactory;

    private Config config;

    @Override
    protected boolean isFormSubmission(HttpServletRequest request) {
        return true;
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        DocumentSearchFilter filter = (DocumentSearchFilter) super.formBackingObject(request);
        filter.setPageLimit(1);
        filter.setPageOffset(1);
        filter.setOrderByColumnId(DocumentSearchColumn.TESTCASE_NAME);
        filter.setOrderDirection(-1);
        return filter;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        DocumentSearchFilter filter = (DocumentSearchFilter) command;
        filter.setColumns(columnFactory.getColumnList());

        
        if (filter.getProject() != null && filter.getProject().equals("0")) {
            filter.setProject("");
        }

        filter.setPageLimit(null);
        filter.setPageOffset(null);
        /*
         * Collecting customization search criteria parameters
         */
        filter.setCustomizations(CustomizationUtils.collectCustomizationSearchCriteriaParameters(request));

        /*
         * Fetching tests from database
         */
        BrowseResult<Document> documents = documentDAO.searchDocuments(filter);

        Long projectId = null;
        try{
            projectId = Long.parseLong(filter.getProject());
        }
        catch (Exception e) {
            
        }

        reportGenerator.writeExcelReports(documents, projectId, request, response);
        return null;
    }
    
    
    public void setColumnFactory(ColumnFactory columnFactory) {
        this.columnFactory = columnFactory;
    }

    public ColumnFactory getColumnFactory() {
        return columnFactory;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }

    public void setProjectDAO(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    public ProjectDAO getProjectDAO() {
        return projectDAO;
    }

    public void setCustomizationDAO(CustomizationDAO customizationDAO) {
        this.customizationDAO = customizationDAO;
    }

    public CustomizationDAO getCustomizationDAO() {
        return customizationDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }


    public void setReportGenerator(ExcelTestSearchReportGenerator reportGenerator) {
        this.reportGenerator = reportGenerator;
    }

    public ExcelTestSearchReportGenerator getReportGenerator() {
        return reportGenerator;
    }

    public DocumentDAO getDocumentDAO() {
        return documentDAO;
    }

    public void setDocumentDAO(DocumentDAO documentDAO) {
        this.documentDAO = documentDAO;
    }
}
