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
package net.mindengine.oculus.frontend.web.controllers.customization;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.customization.Customization;
import net.mindengine.oculus.frontend.domain.project.Project;
import net.mindengine.oculus.frontend.service.customization.CustomizationDAO;
import net.mindengine.oculus.frontend.service.exceptions.InvalidRequest;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

import org.springframework.web.servlet.ModelAndView;

public class AddParameterController extends SecureSimpleViewController {
	private ProjectDAO projectDAO;
	private CustomizationDAO customizationDAO;

	public List<String> fillListParameter(HttpServletRequest request) {
		List<String> list = new LinkedList<String>();
		Integer num = Integer.parseInt(request.getParameter("listPossibleValuesLength"));

		for (int i = 0; i < num; i++) {

			list.add(request.getParameter("listPossibleValue_" + i));
		}
		return list;
	}

	public void addParameter(HttpServletRequest request, Project project, String unit) throws Exception {
		Customization param = new Customization();
		param.setName(request.getParameter("name"));
		param.setDescription(request.getParameter("description"));
		param.setUnit(unit);
		param.setProjectId(project.getId());
		param.setType(request.getParameter("type"));
		param.setGroupName(request.getParameter("groupName"));
		List<String> possibleValues = null;
		if (param.getType() != null && param.getType().equals(Customization.TYPE_LIST)) {
			param.setSubtype(request.getParameter("listSubtype"));
			possibleValues = fillListParameter(request);
		}
		else if (param.getType() != null && param.getType().equals(Customization.TYPE_CHECKLIST)) {
			possibleValues = fillListParameter(request);
		}
		Long customizationId = customizationDAO.addCustomization(param);
		if (possibleValues != null) {
			customizationDAO.saveCustomizationPossibleValues(customizationId, possibleValues);
		}
	}

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		verifyPermissions(request);
		Map<String, Object> map = new HashMap<String, Object>();
		Long projectId = Long.parseLong(request.getParameter("projectId"));
		String unit = request.getParameter("unit");
		if (unit == null || unit.isEmpty())
			throw new InvalidRequest();
		map.put("unit", unit);
		map.put("title", getTitle());

		Project project = projectDAO.getProject(projectId);

		// TODO Validation of required fields

		String submit = request.getParameter("Submit");
		if (submit != null && submit.equals("Add Parameter")) {
			addParameter(request, project, unit);
			return new ModelAndView("redirect:../customization/project-" + project.getPath() + "?unit=" + unit);
		}

		map.put("project", project);
		return new ModelAndView(getView(), map);
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public CustomizationDAO getCustomizationDAO() {
		return customizationDAO;
	}

	public void setCustomizationDAO(CustomizationDAO customizationDAO) {
		this.customizationDAO = customizationDAO;
	}
}
