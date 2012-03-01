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
package net.mindengine.oculus.frontend.web.controllers.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.config.Config;
import net.mindengine.oculus.frontend.db.search.ColumnFactory;
import net.mindengine.oculus.frontend.domain.db.BrowseResult;
import net.mindengine.oculus.frontend.domain.test.Test;
import net.mindengine.oculus.frontend.domain.test.TestSearchColumn;
import net.mindengine.oculus.frontend.domain.test.TestSearchFilter;
import net.mindengine.oculus.frontend.service.customization.CustomizationDAO;
import net.mindengine.oculus.frontend.service.customization.CustomizationUtils;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.service.report.ExcelTestSearchReportGenerator;
import net.mindengine.oculus.frontend.service.test.TestDAO;
import net.mindengine.oculus.frontend.service.user.UserDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleFormController;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

public class TestSearchExportXLSController extends SecureSimpleFormController {

    private TestDAO testDAO;
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
        TestSearchFilter filter = (TestSearchFilter) super.formBackingObject(request);
        filter.setPageLimit(1);
        filter.setPageOffset(1);
        filter.setOrderByColumnId(TestSearchColumn.TEST_NAME);
        filter.setOrderDirection(-1);
        return filter;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        TestSearchFilter filter = (TestSearchFilter) command;
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
        BrowseResult<Test> tests = testDAO.searchTests(filter);

        Long projectId = null;
        try{
            projectId = Long.parseLong(filter.getProject());
        }
        catch (Exception e) {
            
        }

        reportGenerator.writeExcelReports(tests, projectId, request, response);
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

    public void setTestDAO(TestDAO testDAO) {
        this.testDAO = testDAO;
    }

    public TestDAO getTestDAO() {
        return testDAO;
    }

    public void setReportGenerator(ExcelTestSearchReportGenerator reportGenerator) {
        this.reportGenerator = reportGenerator;
    }

    public ExcelTestSearchReportGenerator getReportGenerator() {
        return reportGenerator;
    }
}
