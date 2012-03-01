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
package net.mindengine.oculus.frontend.web.controllers.customization;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import net.mindengine.oculus.frontend.domain.customization.Customization;
import net.mindengine.oculus.frontend.domain.project.Project;
import net.mindengine.oculus.frontend.service.customization.CustomizationDAO;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

public class ProjectCustomizationController extends SecureSimpleViewController {
	private String viewChooseUnit;
	private ProjectDAO projectDAO;
	private CustomizationDAO customizationDAO;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String path = request.getPathInfo().substring(9);
		Project project = projectDAO.getProjectByPath(path);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("project", project);

		verifyPermissions(request);
		String unit = request.getParameter("unit");
		map.put("unit", unit);
		map.put("title", getTitle());
		if (unit == null) {
			return new ModelAndView(viewChooseUnit, map);
		}
		/*
		 * Fetching customizations with theirs possible values
		 */
		List<Customization> customizations = customizationDAO.getCustomizations(project.getId(), unit);
		List<Map<String, Object>> customizationsMap = new LinkedList<Map<String, Object>>();
		for (Customization customization : customizations) {
			Map<String, Object> c = new HashMap<String, Object>();
			c.put("customization", customization);
			if (customization.getType().equals(Customization.TYPE_LIST) || customization.getType().equals(Customization.TYPE_CHECKLIST)) {
				c.put("possibleValues", customizationDAO.getCustomizationPossibleValues(customization.getId()));
			}
			customizationsMap.add(c);
		}
		map.put("customizations", customizationsMap);

		return new ModelAndView(getView(), map);
	}

	public void setViewChooseUnit(String viewChooseUnit) {
		this.viewChooseUnit = viewChooseUnit;
	}

	public String getViewChooseUnit() {
		return viewChooseUnit;
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
