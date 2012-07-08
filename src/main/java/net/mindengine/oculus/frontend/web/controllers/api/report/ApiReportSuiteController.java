package net.mindengine.oculus.frontend.web.controllers.api.report;


import java.util.Date;
import java.util.Map;

import net.mindengine.oculus.frontend.api.POST;
import net.mindengine.oculus.frontend.api.PUT;
import net.mindengine.oculus.frontend.api.Path;
import net.mindengine.oculus.frontend.api.RequestBody;
import net.mindengine.oculus.frontend.api.RequestVar;
import net.mindengine.oculus.frontend.domain.run.SuiteRun;
import net.mindengine.oculus.frontend.service.runs.TestRunDAO;
import net.mindengine.oculus.frontend.web.controllers.api.ApiController;

import org.apache.commons.lang3.StringEscapeUtils;

public class ApiReportSuiteController extends ApiController {

    private TestRunDAO testRunDAO;
    
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
        if ( id == null ) {
            throw new IllegalArgumentException("Id of suite run is not specified");
        }
        testRunDAO.updateSuiteEndTime(id, new Date());
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
    
    
}
