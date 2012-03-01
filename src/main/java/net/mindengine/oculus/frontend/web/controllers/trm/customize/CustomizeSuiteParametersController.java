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
package net.mindengine.oculus.frontend.web.controllers.trm.customize;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.trm.TrmProperty;
import net.mindengine.oculus.frontend.service.exceptions.InvalidRequest;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.service.trm.TrmDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * Used for following operations on suite parameters:
 * <ul>
 * <li>Displaying</li>
 * <li>Adding</li>
 * <li>Removing</li>
 * <li>Editing</li>
 * </ul>
 * 
 * The key for operation is passed with post "Submit" parameter. It can be the
 * following:
 * <ul>
 * <li>Add Parameter</li>
 * <li>Delete Parameter</li>
 * <li>Edit Parameter</li>
 * </ul>
 * 
 * If the "projectId" parameter wasn't provided in request then there will be a
 * page loaded for choosing project
 * 
 * @author Ivan Shubin
 * 
 */
public class CustomizeSuiteParametersController extends SecureSimpleViewController {
	private TrmDAO trmDAO;
	private ProjectDAO projectDAO;

	public void addParameter(HttpServletRequest request, Long projectId) throws Exception {
		TrmProperty property = new TrmProperty();
		property.setType(TrmProperty._TYPE_SUITE_PARAMETER);
		property.setName(request.getParameter("AP_Name"));
		property.setDescription(request.getParameter("AP_Description"));

		property.setProjectId(projectId);
		String subtype = request.getParameter("AP_ControlType");
		property.setSubtype(subtype);
		if (subtype == null) {
			throw new InvalidRequest("Subtype wasn't defined");
		}

		if (subtype.equals(TrmProperty.Controls._LIST)) {
			// Collecting possible values list from request for control type
			// list
			int valuesCount = Integer.parseInt(request.getParameter("AP_ListPossibleValuesCount"));

			String possibleValues = "";
			for (int i = 0; i < valuesCount; i++) {
				String pv = request.getParameter("AP_ListPossibleValue_" + i);
				if (pv == null || pv.isEmpty())
					throw new InvalidRequest("Possible Values for List Control are defined incorectly");
				possibleValues += "<value>" + StringEscapeUtils.escapeXml(pv);
			}
			if (valuesCount == 0)
				throw new InvalidRequest("You cannot specify list without possible values");
			property.setValue(possibleValues);
		}

		trmDAO.createProperty(property);
	}

	public void deleteParameter(HttpServletRequest request, Long projectId) throws Exception {
		Long parameterId = Long.parseLong(request.getParameter("deleteParameterId"));
		trmDAO.deleteProperty(parameterId);

	}

	@SuppressWarnings("unchecked")
	public void saveParameter(HttpServletRequest request, Long projectId) throws Exception {
		Long parameterId = Long.parseLong(request.getParameter("changeParameterId"));
		TrmProperty property = trmDAO.getProperty(parameterId);
		String subtype = request.getParameter("changeParameterType");
		if (subtype == null)
			throw new InvalidRequest("The control type parameter wasn't specified");
		property.setSubtype(subtype);

		if (subtype.equals(TrmProperty.Controls._LIST)) {
			/*
			 * Collecting all possible values for list control
			 */
			String value = "";
			Enumeration<String> names = request.getParameterNames();
			while (names.hasMoreElements()) {
				String name = names.nextElement();
				if (name.startsWith("changePossibleValue")) {
					String pv = request.getParameter(name);
					value += "<value>" + StringEscapeUtils.escapeXml(pv);
				}
			}
			property.setValue(value);
		}
		else
			property.setValue("");

		trmDAO.changeProperty(property);
	}

	@Override
	public Map<String, Object> handleController(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Long projectId = Long.parseLong(request.getParameter("projectId"));
		String submit = request.getParameter("Submit");
		if (submit != null) {
			if (submit.equals("Add Parameter")) {
				addParameter(request, projectId);
			}
			else if (submit.equals("Delete Parameter")) {
				deleteParameter(request, projectId);
			}
			else if (submit.equals("Save")) {
				saveParameter(request, projectId);
			}
		}
		List<TrmProperty> properties = trmDAO.getProperties(projectId, TrmProperty._TYPE_SUITE_PARAMETER);

		map.put("suiteProperties", properties);

		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (request.getParameter("projectId") == null) {
			Map model = new HashMap<String, Object>();
			model.put("projects", projectDAO.getRootProjects());
			return new ModelAndView("trm-customize-suite-parameters-choose-project", model);
		}
		else
			return super.handleRequest(request, response);
	}

	public void setTrmDAO(TrmDAO trmDAO) {
		this.trmDAO = trmDAO;
	}

	public TrmDAO getTrmDAO() {
		return trmDAO;
	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}
}
