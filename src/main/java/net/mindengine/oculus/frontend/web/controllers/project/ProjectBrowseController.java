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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.project.ProjectBrowseFilter;
import net.mindengine.oculus.frontend.domain.project.ProjectBrowseResult;
import net.mindengine.oculus.frontend.service.document.DocumentDAO;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleFormController;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * Displays only the root projects.
 * 
 * @author Ivan Shubin
 * 
 */
public class ProjectBrowseController extends SecureSimpleFormController {
	private ProjectDAO projectDAO;
	private DocumentDAO documentDAO;

	private ProjectBrowseFilter filter;

	@SuppressWarnings("unchecked")
	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {
		Map map = new HashMap();

		filter.setOnlyRoot(true);
		ProjectBrowseResult projectBrowseResult = projectDAO.browseProjects(filter);
		map.put("projectBrowseResult", projectBrowseResult);
		map.put("title", getTitle());
		return map;
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Object object = super.formBackingObject(request);
		setFilter((ProjectBrowseFilter) object);
		return object;
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		ModelAndView mav = new ModelAndView(getSuccessView());
		setFilter((ProjectBrowseFilter) command);
		mav.addObject("projectBrowseFilter", command);
		mav.addAllObjects(referenceData(request));
		return mav;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public DocumentDAO getDocumentDAO() {
		return documentDAO;
	}

	public void setDocumentDAO(DocumentDAO documentDAO) {
		this.documentDAO = documentDAO;
	}

	public void setFilter(ProjectBrowseFilter filter) {
		this.filter = filter;
	}

	public ProjectBrowseFilter getFilter() {
		return filter;
	}
}
