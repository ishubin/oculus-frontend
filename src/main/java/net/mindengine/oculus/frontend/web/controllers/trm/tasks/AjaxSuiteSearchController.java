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
package net.mindengine.oculus.frontend.web.controllers.trm.tasks;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import net.mindengine.oculus.frontend.domain.trm.TrmSuite;
import net.mindengine.oculus.frontend.domain.trm.TrmSuiteGroup;
import net.mindengine.oculus.frontend.domain.trm.TrmTask;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.trm.TrmDAO;
import net.mindengine.oculus.experior.utils.XmlUtils;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

public class AjaxSuiteSearchController extends SecureSimpleViewController {

	private TrmDAO trmDAO;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		verifyPermissions(request);

		OutputStream os = response.getOutputStream();
		response.setContentType("text/xml");

		String rId = request.getParameter("id");

		OutputStreamWriter w = new OutputStreamWriter(os);

		String rootId = rId;
		if (rId.equals("mytasks0"))
			rootId = "0";

		w.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		w.write("<tree id=\"" + rootId + "\" >");

		User user = getUser(request);
		List<TrmSuiteGroup> groups = null;
		List<TrmSuite> suites = null;
		if (rId.startsWith("mytasks")) {
		    Long projectId = null;
		    String strProjectId = request.getParameter("projectId");
		    if(strProjectId!=null) {
		        projectId = Long.parseLong(strProjectId);
		    }
		    
			// Displaying a list of all user tasks
			List<TrmTask> tasks = trmDAO.getUserTasks(user.getId(), projectId);
			for (TrmTask task : tasks) {
				w.write("<item text=\"" + XmlUtils.escapeXml(task.getName()) + "\" " + "id=\"t" + task.getId() + "\" " + "im0=\"iconTask.png\" im1=\"iconTask.png\" im2=\"iconTask.png\" child=\"1\" " + " nocheckbox=\"1\" >");
				w.write("</item>");
			}
		}
		else if (rId.startsWith("t")) {
			Long taskId = Long.parseLong(rId.substring(1));
			groups = trmDAO.getTaskSuiteGroups(taskId);
			suites = trmDAO.getTaskSuites(taskId, 0L);
		}
		else if (rId.startsWith("g")) {

			int dash = rId.indexOf("_");
			Long taskId = Long.parseLong(rId.substring(1, dash));
			Long groupId = Long.parseLong(rId.substring(dash + 1));

			suites = trmDAO.getTaskSuites(taskId, groupId);
		}

		if (groups != null) {
			for (TrmSuiteGroup group : groups) {
				w.write("<item text=\"" + XmlUtils.escapeXml(group.getName()) + "\" " + "id=\"g" + group.getTaskId() + "_" + group.getId() + "\" " + "im0=\"folderClosed.gif\" im1=\"folderOpen.gif\" im2=\"folderClosed.gif\" child=\"1\" " + " nocheckbox=\"1\" >");
				w.write("</item>");
			}
		}
		if (suites != null) {
			for (TrmSuite suite : suites) {
				w.write("<item ");
				w.write("text=\"" + XmlUtils.escapeXml(suite.getName()) + "\" ");
				w.write("id=\"suite" + suite.getId() + "\" ");
				w.write("im0=\"workflow-icon-suite.png\" im1=\"workflow-icon-suite.png\" im2=\"workflow-icon-suite.png\" ");
				
				w.write(">");
				w.write("</item>");
			}
		}

		w.write("</tree>");
		w.flush();
		os.flush();
		os.close();
		return null;
	}

	public void setTrmDAO(TrmDAO trmDAO) {
		this.trmDAO = trmDAO;
	}

	public TrmDAO getTrmDAO() {
		return trmDAO;
	}

}
