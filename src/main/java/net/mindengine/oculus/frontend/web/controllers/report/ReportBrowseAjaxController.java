package net.mindengine.oculus.frontend.web.controllers.report;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.config.Config;
import net.mindengine.oculus.frontend.db.search.ColumnFactory;
import net.mindengine.oculus.frontend.db.search.SearchColumn;
import net.mindengine.oculus.frontend.domain.report.ReportSearchColumn;
import net.mindengine.oculus.frontend.domain.report.SearchFilter;
import net.mindengine.oculus.frontend.domain.report.TestRunSearchResult;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.service.runs.TestRunDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleFormController;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

public class ReportBrowseAjaxController extends SecureSimpleFormController {

    private TestRunDAO testRunDAO;
    private ProjectDAO projectDAO;
    private Config config;
    /**
     * This is a factory that is used to fetch the default list of columns to
     * display in report table in case if column list wasn't defined previously
     * in session
     */
    private ColumnFactory columnFactory;

    @SuppressWarnings( { "unchecked", "rawtypes" })
    @Override
    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
        SearchFilter filter = (SearchFilter) command;

        filter.setColumnList(columnFactory.getColumnList());
        TestRunSearchResult searchResult = testRunDAO.browseRuns(filter);
        searchResult.handleTheList();
        searchResult.setColumns(columnFactory.getColumnList());

        Map referenceData = new HashMap();
        referenceData.put("searchResult", searchResult);
        return referenceData;

    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        Object object = super.formBackingObject(request);
        SearchFilter searchFilter = (SearchFilter) object;
        searchFilter.setOrderByColumnId(ReportSearchColumn.START_TIME);
        searchFilter.setOrderDirection(-1);
        searchFilter.setPageLimit(4);
        return object;
    }

    public SearchColumn getColumnById(int id, Collection<SearchColumn> columnList) {

        for (SearchColumn column : columnList) {
            if (column.getId().equals(id)) {
                return column;
            }
        }
        return null;
    }

    @Override
    protected boolean isFormSubmission(HttpServletRequest request) {
        return true;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        ModelAndView mav = new ModelAndView(getSuccessView());
        mav.addObject("reportSearchFilter", command);
        mav.addAllObjects(referenceData(request, command, errors));
        return mav;
    }

    public TestRunDAO getTestRunDAO() {
        return testRunDAO;
    }

    public void setTestRunDAO(TestRunDAO testRunDAO) {
        this.testRunDAO = testRunDAO;
    }

    public ColumnFactory getColumnFactory() {
        return columnFactory;
    }

    public void setColumnFactory(ColumnFactory columnFactory) {
        this.columnFactory = columnFactory;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public void setProjectDAO(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    public ProjectDAO getProjectDAO() {
        return projectDAO;
    }
}
