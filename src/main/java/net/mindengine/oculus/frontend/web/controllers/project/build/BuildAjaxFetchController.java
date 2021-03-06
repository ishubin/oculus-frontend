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
package net.mindengine.oculus.frontend.web.controllers.project.build;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.domain.AjaxModel;
import net.mindengine.oculus.frontend.service.project.build.BuildDAO;
import net.mindengine.oculus.frontend.web.controllers.SimpleAjaxController;

public class BuildAjaxFetchController extends SimpleAjaxController {
	private BuildDAO buildDAO;

	@Override
	public AjaxModel handleController(HttpServletRequest request) throws Exception {
		Long projectId = Long.parseLong(request.getParameter("projectId"));
		Integer page = Integer.parseInt(request.getParameter("page"));
		String name = request.getParameter("name");
		AjaxModel model = new AjaxModel();

		model.setObject(buildDAO.fetchBuilds(name, page, projectId));
		return model;
	}

	public void setBuildDAO(BuildDAO buildDAO) {
		this.buildDAO = buildDAO;
	}

	public BuildDAO getBuildDAO() {
		return buildDAO;
	}
}
