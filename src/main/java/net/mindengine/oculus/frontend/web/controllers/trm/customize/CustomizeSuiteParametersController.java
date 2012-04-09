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
package net.mindengine.oculus.frontend.web.controllers.trm.customize;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.project.Project;
import net.mindengine.oculus.frontend.domain.trm.TrmProperty;
import net.mindengine.oculus.frontend.service.exceptions.InvalidRequest;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.service.trm.TrmDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

import org.apache.commons.lang.StringEscapeUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
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

	@Override
	public Map<String, Object> handleController(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Long projectId = Long.parseLong(request.getParameter("projectId"));
		
		Project project = projectDAO.getProject(projectId);
		if ( project == null ) {
            throw new IllegalArgumentException("Project with id " + projectId + " doesn't exist");
        }
		
		map.put("project", project);
        
		String submit = request.getParameter("Submit");
		if (submit != null && submit.equals("Save")) {
			String jsonParameters = request.getParameter("parameters");
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			if ( jsonParameters != null ) {
			    TrmProperty[] propertiesFromJson = mapper.readValue(jsonParameters, TrmProperty[].class);
			    List<TrmProperty> properties = new LinkedList<TrmProperty>();
			    
			    if ( propertiesFromJson != null ) {
			        Collections.addAll(properties, propertiesFromJson);
			    }
			    
			    trmDAO.saveTrmPropertiesForProject(projectId, properties, TrmProperty._TYPE_SUITE_PARAMETER);
			}
		}
		List<TrmProperty> properties = trmDAO.getProperties(projectId, TrmProperty._TYPE_SUITE_PARAMETER);

		map.put("suiteProperties", properties);
		
		return map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
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
