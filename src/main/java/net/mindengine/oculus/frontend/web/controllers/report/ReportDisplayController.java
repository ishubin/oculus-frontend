package net.mindengine.oculus.frontend.web.controllers.report;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.domain.run.TestRun;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.issue.IssueDAO;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.service.runs.TestRunDAO;
import net.mindengine.oculus.frontend.service.test.TestDAO;
import net.mindengine.oculus.experior.reporter.nodes.DescriptionReportNode;
import net.mindengine.oculus.experior.reporter.nodes.ReportNode;
import net.mindengine.oculus.experior.reporter.render.XmlReportRender;
import net.mindengine.oculus.frontend.web.controllers.SimpleViewController;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.model.JSONValue;

public class ReportDisplayController extends SimpleViewController {
	private ProjectDAO projectDAO;
	private TestRunDAO testRunDAO;
	private TestDAO testDAO;
	private IssueDAO issueDAO;

	public TestRunDAO getTestRunDAO() {
		return testRunDAO;
	}

	public void setTestRunDAO(TestRunDAO testRunDAO) {
		this.testRunDAO = testRunDAO;
	}

	/**
	 * Updates the status of all report nodes
	 * 
	 * @param node
	 */
	public int updateReport(ReportNode node, int step) {
		Map<String, Object> metaData = new HashMap<String, Object>();
		new HashMap<String, Object>();
		metaData.put("collapsed", true);

		metaData.put("nodeClassName", node.getClass().getSimpleName());
		if (node.hasError()) {
			metaData.put("hasError", true);
		}
		else if (node.hasWarn()) {
			metaData.put("hasWarn", true);
		}

		metaData.put("isSimple", false);

		if (node.getChildren().size() == 1) {
			if (node.getChildren().get(0) instanceof DescriptionReportNode) {
				step++;
				metaData.put("isSimple", true);
				metaData.put("step", step);
			}
		}
		else if (node.getChildren().size() == 0 && !(node instanceof DescriptionReportNode)) {
			step++;
			metaData.put("isSimple", true);
			metaData.put("step", step);
		}

		node.setMetaData(metaData);
		if (node.getParent() != null) {
			metaData.put("parentId", node.getParent().getId());
		}
		for (ReportNode childNode : node.getChildren()) {
			step = updateReport(childNode, step);
		}
		return step;
	}

	@Override
	public Map<String, Object> handleController(HttpServletRequest request) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();

		Long id = Long.parseLong(request.getPathInfo().substring(8));
		TestRun testRun = testRunDAO.getRunById(id);
		if (testRun == null)
			throw new UnexistentResource("Test run with id = " + id + " doesn't exist");
		Long projectId = testRun.getProjectId();
		Long testId = testRun.getTestId();
		Long suiteRunId = testRun.getSuiteRunId();

		model.put("testRun", testRun);
		if (projectId != null && projectId > 0) {
			model.put("project", projectDAO.getProject(projectId));
		}
		if (testId != null && testId > 0) {
			model.put("test", testDAO.getTest(testId));
		}
		if (suiteRunId != null && suiteRunId > 0) {
			model.put("suiteRun", testRunDAO.getSuiteRun(suiteRunId));
		}
		model.put("testRunParameters", testRunDAO.getTestRunParameters(testRun.getId()));
		
		if(testRun.getIssueId()!=null && testRun.getIssueId()>0){
		    model.put("issue", issueDAO.getIssue(testRun.getIssueId()));
		}

		XmlReportRender reportRender = new XmlReportRender();
		ReportNode reportNode = reportRender.decode(testRun.getReport());

		updateReport(reportNode, 0);

		JSONValue jsonValue = JSONMapper.toJSON(reportNode);
		String jsonString = jsonValue.render(false);

		model.put("report", reportNode);
		model.put("json", jsonString);
		return model;
	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}

	public void setTestDAO(TestDAO testDAO) {
		this.testDAO = testDAO;
	}

	public TestDAO getTestDAO() {
		return testDAO;
	}

    public IssueDAO getIssueDAO() {
        return issueDAO;
    }

    public void setIssueDAO(IssueDAO issueDAO) {
        this.issueDAO = issueDAO;
    }
}
