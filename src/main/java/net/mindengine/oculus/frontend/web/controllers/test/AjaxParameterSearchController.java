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
* along with Oculus Experior.  If not, see <http://www.gnu.org/licenses/>.
******************************************************************************/
package net.mindengine.oculus.frontend.web.controllers.test;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.project.Project;
import net.mindengine.oculus.frontend.domain.test.Test;
import net.mindengine.oculus.frontend.domain.test.TestGroup;
import net.mindengine.oculus.frontend.domain.test.TestParameter;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.service.test.TestDAO;
import net.mindengine.oculus.experior.utils.XmlUtils;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

import org.springframework.web.servlet.ModelAndView;

public class AjaxParameterSearchController extends SecureSimpleViewController {

	private TestDAO testDAO;
	private ProjectDAO projectDAO;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

		OutputStream os = response.getOutputStream();
		response.setContentType("text/xml");

		String rId = request.getParameter("id");

		OutputStreamWriter w = new OutputStreamWriter(os);

		String rootId = rId;
		w.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		w.write("<tree id=\"" + rootId + "\" >");

		List<Project> projects = null;
		List<TestGroup> groups = null;
		List<Test> tests = null;
		List<TestParameter> parameters = null;
		if (rId.equals("0")) {
			// Fetching a list of projects
			projects = projectDAO.getRootProjects();
		}
		else if (rId.startsWith("p")) {
			Long projectId = Long.parseLong(rId.substring(1));
			projects = projectDAO.getSubprojects(projectId);

			tests = testDAO.getTestsByProjectId(projectId, 0L);
			groups = testDAO.getProjectTestGroups(projectId);
		}
		else if (rId.startsWith("g")) {
			// g123_456 where 123 - id of a group and 456 - id of a project
			int dash = rId.indexOf("_");
			Long groupId = Long.parseLong(rId.substring(1, dash));
			Long projectId = Long.parseLong(rId.substring(dash + 1));

			tests = testDAO.getTestsByProjectId(projectId, groupId);
		}
		else if (rId.startsWith("t")) {
			Long testId = Long.parseLong(rId.substring(1));
			parameters = testDAO.getTestInputParameters(testId);
			parameters.addAll(testDAO.getTestOutputParameters(testId));
		}

		if (projects != null) {
			for (Project project : projects) {
				w.write("<item text=\"" + XmlUtils.escapeXml(project.getName()) + "\" " + "id=\"p" + project.getId() + "\" im0=\"tombs.gif\" im1=\"tombs_open.gif\" im2=\"tombs.gif\" child=\"1\" " + " nocheckbox=\"1\" >");
				w.write("</item>");
			}
		}
		if (groups != null) {
			for (TestGroup group : groups) {
				w.write("<item ");
				w.write("text=\"" + XmlUtils.escapeXml(group.getName()) + "\" ");
				w.write("id=\"g" + group.getId() + "_" + group.getProjectId() + "\" ");
				w.write("im0=\"folderClosed.gif\" im1=\"folderOpen.gif\" im2=\"folderClosed.gif\" ");
				w.write("child=\"1\" nocheckbox=\"1\"  >");
				w.write("</item>");
			}
		}
		if (tests != null) {
			for (Test test : tests) {
				w.write("<item ");
				w.write("text=\"" + XmlUtils.escapeXml(test.getName()) + "\" ");
				w.write("id=\"t" + test.getId() + "\" ");
				w.write("im0=\"iconTest.png\" im1=\"iconTest.png\" im2=\"iconTest.png\" ");
				w.write("child=\"1\" nocheckbox=\"1\"  >");
				w.write("</item>");
			}
		}
		if (parameters != null) {
			for (TestParameter parameter : parameters) {
				w.write("<item ");
				w.write("text=\"" + XmlUtils.escapeXml(parameter.getName()) + "\" ");
				w.write("id=\"tp" + parameter.getId() + "\" ");

				if (parameter.getType().equals(TestParameter.TYPE_INPUT)) {
					w.write("im0=\"iconParameterInput.png\" im1=\"iconParameterInput.png\" im2=\"iconParameterInput.png\" ");
				}
				else
					w.write("im0=\"iconParameterOutput.png\" im1=\"iconParameterOutput.png\" im2=\"iconParameterOutput.png\" ");
				w.write(" >");
				w.write("</item>");
			}
		}

		w.write("</tree>");
		w.flush();
		os.flush();
		os.close();
		return null;
	}

	public void setTestDAO(TestDAO testDAO) {
		this.testDAO = testDAO;
	}

	public TestDAO getTestDAO() {
		return testDAO;
	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public ProjectDAO getProjectDAO() {
		return this.projectDAO;
	}

}
