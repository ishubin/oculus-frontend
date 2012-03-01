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

import net.mindengine.oculus.frontend.config.Config;
import net.mindengine.oculus.frontend.domain.AjaxModel;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;
import net.mindengine.oculus.frontend.web.Session;
import net.mindengine.oculus.frontend.web.controllers.SimpleAjaxController;
import net.mindengine.oculus.grid.domain.task.TaskInformation;
import net.mindengine.oculus.grid.service.ClientServerRemoteInterface;

public class AjaxRemoveCompletedTaskController extends SimpleAjaxController {
	private Config config;

	@Override
	public AjaxModel handleController(HttpServletRequest request) throws Exception {
		Session session = Session.create(request);
		Long taskId = Long.parseLong(request.getParameter("taskId"));
		User user = session.getAuthorizedUser();
		if (user == null)
			throw new NotAuthorizedException();

		ClientServerRemoteInterface server = config.getGridServer();
		TaskInformation task = server.getTask(taskId);

		if (user.getId().equals(task.getTaskUser().getId())) {
			AjaxModel model = new AjaxModel();
			server.removeCompletedTask(taskId);
			model.setObject(taskId);
			model.setResult("removed");
			return model;
		}
		else
			throw new NotAuthorizedException();
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public Config getConfig() {
		return config;
	}
}
