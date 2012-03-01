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
package net.mindengine.oculus.frontend.web.controllers.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.test.TestParameter;
import net.mindengine.oculus.frontend.service.test.TestDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

import org.springframework.web.servlet.ModelAndView;

public class CopyParametersController extends SecureSimpleViewController {
	private TestDAO testDAO;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		verifyPermissions(request);

		Long testId = Long.parseLong(request.getParameter("testId"));

		String parameterIds = request.getParameter("parameterIds");
		if (parameterIds != null && !parameterIds.isEmpty()) {
			String[] ids = parameterIds.split(",");
			for (String id : ids) {
				if (id.startsWith("tp")) {
					Long parameterId = Long.parseLong(id.substring(2));
					TestParameter parameter = testDAO.getParameter(parameterId);
					if (parameter != null) {
						parameter.setId(null);
						parameter.setTestId(testId);
						testDAO.createTestParameter(parameter);
					}
				}
			}
		}

		return new ModelAndView("redirect:../test/display?id=" + testId);
	}

	public void setTestDAO(TestDAO testDAO) {
		this.testDAO = testDAO;
	}

	public TestDAO getTestDAO() {
		return testDAO;
	}

}
