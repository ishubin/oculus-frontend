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
package net.mindengine.oculus.frontend.web.controllers.trm.tasks;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.config.Config;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;
import net.mindengine.oculus.grid.domain.task.Task;
import net.mindengine.oculus.grid.service.ClientServerRemoteInterface;
import net.mindengine.oculus.frontend.web.Session;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

/**
 * Fetches all users tasks which are available on TRMServer
 * 
 * @author Ivan Shubin
 * 
 */
public class MyActiveTasksController extends SecureSimpleViewController {
	private Config config;

	@Override
	public Map<String, Object> handleController(HttpServletRequest request) throws Exception {
		User user = Session.create(request).getAuthorizedUser();
		if (user == null)
			throw new NotAuthorizedException();
		Map<String, Object> map = new HashMap<String, Object>();


		return map;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public Config getConfig() {
		return config;
	}
}
