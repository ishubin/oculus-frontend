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
package net.mindengine.oculus.frontend.web.controllers.project;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.project.Project;
import net.mindengine.oculus.frontend.domain.test.Test;
import net.mindengine.oculus.frontend.service.exceptions.InvalidRequest;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.service.test.TestDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * This controller is used for browsing through all projects and displaying
 * their tests
 * 
 * @author Ivan Shubin
 * 
 */
public class ProjectAjaxBrowseController extends SecureSimpleViewController {
	private TestDAO testDAO;
	private ProjectDAO projectDAO;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("id");
		if (id == null || !id.startsWith("p"))
			throw new InvalidRequest();
		Long projectId = Long.parseLong(id.substring(1));

		boolean asRoot = false;
		if (request.getParameter("asRoot") != null) {
			asRoot = true;
		}
		List<Project> projects = projectDAO.getSubprojects(projectId);
		List<Test> tests = testDAO.getTestsByProjectId(projectId);

		OutputStream os = response.getOutputStream();
		response.setContentType("text/xml");
		OutputStreamWriter w = new OutputStreamWriter(os);

		w.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		w.write("<tree id=\"");
		if (projectId.equals(0L) || asRoot) {
			w.write("0");
		}
		else
			w.write(id);
		w.write("\">");
		for (Project project : projects) {
			w.write("<item text=\"");
			w.write(StringEscapeUtils.escapeXml(project.getName()));
			w.write("\" id=\"p");
			w.write(Long.toString(project.getId()));
			w.write("\" im0=\"tombs.gif\" im1=\"tombs_open.gif\" im2=\"tombs.gif\" ");
			if (project.getSubprojectsCount() > 0 || project.getTestsCount() > 0) {
				w.write(" child=\"1\" ");
			}
			w.write("></item>");
		}

		for (Test test : tests) {
			w.write("<item text=\"");
			w.write(StringEscapeUtils.escapeXml(test.getName()));
			w.write("\" id=\"t");
			w.write(Long.toString(test.getId()));
			w.write("\" im0=\"iconTest.png\" im1=\"iconTest.png\" im2=\"iconTest.png\" ");
			w.write("></item>");
		}
		w.write("</tree>");

		w.flush();
		w.close();
		return null;
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
