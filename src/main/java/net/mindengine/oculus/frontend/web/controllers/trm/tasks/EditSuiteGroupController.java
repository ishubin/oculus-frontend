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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.trm.TrmSuiteGroup;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.service.trm.TrmDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleFormController;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

public class EditSuiteGroupController extends SecureSimpleFormController {

	private TrmDAO trmDAO;
	private ProjectDAO projectDAO;

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {

		Long groupId = Long.parseLong(request.getParameter("groupId"));
		TrmSuiteGroup group = trmDAO.getSuiteGroup(groupId);
		if (group == null)
			throw new UnexistentResource("This group doesn't exist");
		return group;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {

		Map map = new HashMap<String, Object>();

		Long groupId = Long.parseLong(request.getParameter("groupId"));
		TrmSuiteGroup group = trmDAO.getSuiteGroup(groupId);

		map.put("task", trmDAO.getTask(group.getTaskId()));
		map.put("suiteGroupCommand", "Edit");
		return map;
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

		verifyPermissions(request);

		TrmSuiteGroup group = (TrmSuiteGroup) command;
		trmDAO.updateSuiteGroup(group);

		return new ModelAndView("redirect:../grid/edit-suite-group?groupId=" + group.getId());
	}

	public TrmDAO getTrmDAO() {
		return trmDAO;
	}

	public void setTrmDAO(TrmDAO trmDAO) {
		this.trmDAO = trmDAO;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}
}
