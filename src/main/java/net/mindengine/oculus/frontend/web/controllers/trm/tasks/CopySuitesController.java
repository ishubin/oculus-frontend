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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.trm.TrmSuite;
import net.mindengine.oculus.frontend.domain.trm.TrmTask;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;
import net.mindengine.oculus.frontend.service.trm.TrmDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

import org.springframework.web.servlet.ModelAndView;

public class CopySuitesController extends SecureSimpleViewController {

	private TrmDAO trmDAO;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Long taskId = Long.parseLong(request.getParameter("taskId"));
		TrmTask task = trmDAO.getTask(taskId);
		User user = getUser(request);

		if (!task.getUserId().equals(user.getId())) {
			throw new NotAuthorizedException("This task is not yours");
		}

		String suiteIds = request.getParameter("suiteIds");
		if (suiteIds != null && !suiteIds.isEmpty()) {
			String[] ids = suiteIds.split(",");

			for (String id : ids) {
				if (id.startsWith("suite")) {
					Long suiteId = Long.parseLong(id.substring(5));
					TrmSuite suite = trmDAO.getSuite(suiteId);
					suite.setTaskId(taskId);
					suite.setId(null);

					trmDAO.saveSuite(suite);
				}
			}
		}
		return new ModelAndView("redirect:../grid/edit-task?id=" + taskId);
	}

	public TrmDAO getTrmDAO() {
		return trmDAO;
	}

	public void setTrmDAO(TrmDAO trmDAO) {
		this.trmDAO = trmDAO;
	}
}
