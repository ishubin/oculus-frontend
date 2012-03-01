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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.service.customization.CustomizationDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

import org.springframework.web.servlet.ModelAndView;

public class DeleteParameterController extends SecureSimpleViewController {
	private CustomizationDAO customizationDAO;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long id = Long.parseLong(request.getParameter("id"));
		String projectPath = request.getParameter("projectPath");
		String unit = request.getParameter("unit");

		customizationDAO.deleteCustomization(id);

		return new ModelAndView("redirect:../customization/project-" + projectPath + "?unit=" + unit);
	}

	public void setCustomizationDAO(CustomizationDAO customizationDAO) {
		this.customizationDAO = customizationDAO;
	}

	public CustomizationDAO getCustomizationDAO() {
		return customizationDAO;
	}
}
