package net.mindengine.oculus.frontend.web.controllers.api.report;


import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.mindengine.oculus.experior.reporter.ReportReason;
import net.mindengine.oculus.experior.reporter.remote.wrappers.TestRunParameter;
import net.mindengine.oculus.experior.reporter.render.XmlReportRender;
import net.mindengine.oculus.frontend.api.POST;
import net.mindengine.oculus.frontend.api.PUT;
import net.mindengine.oculus.frontend.api.Path;
import net.mindengine.oculus.frontend.api.RequestBody;
import net.mindengine.oculus.frontend.api.RequestVar;
import net.mindengine.oculus.frontend.domain.project.Project;
import net.mindengine.oculus.frontend.domain.run.SuiteRun;
import net.mindengine.oculus.frontend.domain.run.TestRun;
import net.mindengine.oculus.frontend.domain.test.Test;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.service.runs.TestRunDAO;
import net.mindengine.oculus.frontend.service.test.TestDAO;
import net.mindengine.oculus.frontend.web.controllers.api.ApiController;

import org.apache.commons.lang3.StringEscapeUtils;

public class ApiReportSuiteController extends ApiController {

    private TestRunDAO testRunDAO;
    private ProjectDAO projectDAO;
    private TestDAO testDAO;
    
    @POST @Path("/report/suite")
    public Long postTestRun(@RequestBody net.mindengine.oculus.experior.reporter.remote.wrappers.SuiteRun remoteSuiteRun) throws Exception {
        if ( remoteSuiteRun == null ) {
            throw new IllegalArgumentException("There is no data for suite run");
        }
        
        SuiteRun suiteRun = new SuiteRun();
        suiteRun.setName(remoteSuiteRun.getName());
        
        if ( remoteSuiteRun.getStartTime() != null ) {
            suiteRun.setStartTime(new Date(remoteSuiteRun.getStartTime()));
        }
        else suiteRun.setStartTime(new Date());
        
        if ( remoteSuiteRun.getEndTime() != null ) {
            suiteRun.setEndTime(new Date(remoteSuiteRun.getEndTime()));
        }
        else suiteRun.setEndTime(new Date());
        
        suiteRun.setRunnerId(remoteSuiteRun.getRunnerId());
        suiteRun.setAgentName(remoteSuiteRun.getAgentName());
        suiteRun.setParameters(serializeSuiteRunParameters(remoteSuiteRun.getParameters()));
        return testRunDAO.createSuiteRun(suiteRun);
    }
    
    @PUT @Path("/report/suite/(.*)/finished")
    public void suiteFinish(@RequestVar(1) Long id) throws Exception {
        if ( id == null || id.equals(0L) ) {
            throw new IllegalArgumentException("Id of suite run is not specified");
        }
        testRunDAO.updateSuiteEndTime(id, new Date());
    }
    
    @POST @Path("/report/suite/(.*)/test")
    public Long createTestRun(@RequestVar(1) Long suiteId, @RequestBody net.mindengine.oculus.experior.reporter.remote.wrappers.TestRun remoteTestRun) throws Exception {
        if ( suiteId == null || suiteId.equals(0L) ) {
            throw new IllegalArgumentException("Id of suite run is not specified");
        }
        TestRun tr = new TestRun();
        tr.setName(remoteTestRun.getName());
        tr.setStartTime(new Date(remoteTestRun.getStartTime()));
        tr.setEndTime(new Date(remoteTestRun.getEndTime()));
        tr.setSuiteRunId(suiteId);
        tr.setDescription(remoteTestRun.getDescription());
        tr.setReasons(encodeReasons(remoteTestRun.getReasons()));
        tr.setReport(remoteTestRun.getReport());
        tr.setStatus(remoteTestRun.getStatus().toString());
        
        tr.setProjectId(0L);
        if ( remoteTestRun.getProject() != null ) {
            Project project = projectDAO.getProjectByPath(remoteTestRun.getProject());
            if ( project != null ) {
                tr.setProjectId(project.getId());
            }
        }
        
        Test test = testDAO.getTestByNameProjectId(tr.getName(), tr.getProjectId());
        if ( test != null ) {
            tr.setTestId(test.getId());
        }
        else tr.setTestId(0L);
        
        Long testRunId = testRunDAO.createTestRun(tr);
        
        if ( remoteTestRun.getParameters() != null ) {
            saveTestRunParameters(testRunId, remoteTestRun.getParameters());
        }
        return testRunId;
    }

    private void saveTestRunParameters(Long testRunId, List<TestRunParameter> parameters) throws Exception {
        for (TestRunParameter parameter : parameters) {
            if ( parameter != null ) {
                testRunDAO.createTestRunParameter(testRunId, parameter.getName(), parameter.getValue(), parameter.getIsInput());
            }
        }
    }

    private String encodeReasons(List<ReportReason> reasons) {
        if ( reasons == null ) {
            reasons = new LinkedList<ReportReason>();
        }
        
        XmlReportRender render = new XmlReportRender();
        return render.renderReasons(reasons);
    }

    private String serializeSuiteRunParameters(Map<String, String> parameters) {
        if ( parameters != null ) {
            StringBuffer serializedParameters = new StringBuffer();
            for (Map.Entry<String, String> parameter : parameters.entrySet()) {
                serializedParameters.append("<p>");
                serializedParameters.append(StringEscapeUtils.escapeXml(parameter.getKey()));
                serializedParameters.append("<v>");
                serializedParameters.append(StringEscapeUtils.escapeXml(parameter.getValue()));
                serializedParameters.append("<p>");
            }
            return serializedParameters.toString();
        }
        else return "";
    }

    public TestRunDAO getTestRunDAO() {
        return testRunDAO;
    }

    public void setTestRunDAO(TestRunDAO testRunDAO) {
        this.testRunDAO = testRunDAO;
    }

    public TestDAO getTestDAO() {
        return testDAO;
    }

    public void setTestDAO(TestDAO testDAO) {
        this.testDAO = testDAO;
    }

    public ProjectDAO getProjectDAO() {
        return projectDAO;
    }

    public void setProjectDAO(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }
    
    
}
