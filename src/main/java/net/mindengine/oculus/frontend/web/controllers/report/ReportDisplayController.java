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
package net.mindengine.oculus.frontend.web.controllers.report;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.experior.reporter.nodes.BranchReportNode;
import net.mindengine.oculus.experior.reporter.nodes.ExceptionReportNode;
import net.mindengine.oculus.experior.reporter.nodes.ReportNode;
import net.mindengine.oculus.experior.reporter.nodes.TextReportNode;
import net.mindengine.oculus.experior.reporter.render.XmlReportRender;
import net.mindengine.oculus.frontend.domain.run.TestRun;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.issue.IssueDAO;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.service.runs.TestRunDAO;
import net.mindengine.oculus.frontend.service.test.TestDAO;
import net.mindengine.oculus.frontend.web.controllers.SimpleViewController;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.model.JSONValue;

public class ReportDisplayController extends SimpleViewController {
	private ProjectDAO projectDAO;
	private TestRunDAO testRunDAO;
	private TestDAO testDAO;
	private IssueDAO issueDAO;

	private class IdGenerator {
		private Long id = 0L;
		public synchronized String makeUniqueId() {
			id++;
			return id.toString();
		}
	}
	
	/**
	 * Updates the status of all report nodes
	 * 
	 * @param node
	 */
	public int updateReport(ReportNode node, int step, IdGenerator idGenerator) {
		node.setId(idGenerator.makeUniqueId());
		
		Map<String, Object> metaData = new HashMap<String, Object>();
		new HashMap<String, Object>();
		metaData.put("collapsed", true);

		metaData.put("nodeClassName", node.getClass().getSimpleName());
		if (node.hasLevel(ReportNode.ERROR)) {
			metaData.put("hasError", true);
		}
		else if (node.hasLevel(ReportNode.WARN)) {
			metaData.put("hasWarn", true);
		}

		metaData.put("isSimple", false);
		metaData.put("hasChildren", false);

		if( node instanceof BranchReportNode ) {
			metaData.put("type", "branch");
			BranchReportNode branchNode = (BranchReportNode) node;
			if( branchNode.getChildNodes() != null && branchNode.getChildNodes().size() > 0 ) {
				metaData.put("hasChildren", true);
				for ( ReportNode childNode : branchNode.getChildNodes() ) {
					step = updateReport(childNode, step, idGenerator);
				}
			}
			else {
				metaData.put("isSimple", true);
			}
		}
		else if ( node instanceof TextReportNode ){
			step++;
			metaData.put("step", step);
			metaData.put("type", "text");
			TextReportNode textNode = (TextReportNode) node;
			if( textNode.getDetails() == null || textNode.getDetails().trim().isEmpty() ) {
				metaData.put("isSimple", true);
			}
		}
		else if ( node instanceof ExceptionReportNode ) {
			metaData.put("type", "exception");
		}
		
		node.setMetaData(metaData);
		if (node.getParentBranch() != null) {
			metaData.put("parentId", node.getParentBranch().getId());
		}
		return step;
	}

	@Override
	public Map<String, Object> handleController(HttpServletRequest request) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();

		Long id = Long.parseLong(request.getPathInfo().substring(8));
		TestRun testRun = testRunDAO.getRunById(id);
		if (testRun == null) {
			throw new UnexistentResource("Test run with id = " + id + " doesn't exist");
		}
		
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

		updateReport(reportNode, 0, new IdGenerator());

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
    
	public TestRunDAO getTestRunDAO() {
		return testRunDAO;
	}

	public void setTestRunDAO(TestRunDAO testRunDAO) {
		this.testRunDAO = testRunDAO;
	}
}
