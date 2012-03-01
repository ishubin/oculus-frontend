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
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

import org.springframework.web.servlet.ModelAndView;

public class EditParameterController extends SecureSimpleViewController {
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

	public void changeParameter(HttpServletRequest request, Customization param) throws Exception {
		param.setName(request.getParameter("name"));
		param.setDescription(request.getParameter("description"));
		param.setType(request.getParameter("type"));
		param.setSubtype("");
		param.setGroupName(request.getParameter("groupName"));
		if (param.getType().equals(Customization.TYPE_LIST)) {
			param.setSubtype(request.getParameter("listSubtype"));
		}

		List<String> possibleValues = null;
		if (param.getType().equals(Customization.TYPE_LIST) || param.getType().equals(Customization.TYPE_CHECKLIST)) {
			possibleValues = fillListParameter(request);
		}
		// Setting subtype to null to non-list parameters to prevent sql
		// exception
		if (!param.getType().equals(Customization.TYPE_LIST)) {
			param.setSubtype(null);
		}

		customizationDAO.updateCustomization(param);
		if (possibleValues != null) {
			customizationDAO.saveCustomizationPossibleValues(param.getId(), possibleValues);
		}
	}

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		verifyPermissions(request);
		Map<String, Object> map = new HashMap<String, Object>();
		Long id = Long.parseLong(request.getParameter("id"));
		Customization customization = customizationDAO.getCustomization(id);
		map.put("customization", customization);
		map.put("customizationPossibleValues", customizationDAO.getCustomizationPossibleValues(id));
		map.put("title", getTitle());
		Project project = projectDAO.getProject(customization.getProjectId());

		String submit = request.getParameter("Submit");
		if (submit != null && submit.equals("Change Parameter")) {
			changeParameter(request, customization);
			return new ModelAndView("redirect:../customization/project-" + project.getPath() + "?unit=" + customization.getUnit());
		}

		map.put("project", project);
		map.put("unit", customization.getUnit());
		return new ModelAndView(getView(), map);
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
}
