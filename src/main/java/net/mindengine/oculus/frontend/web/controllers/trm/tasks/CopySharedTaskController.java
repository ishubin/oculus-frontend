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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import net.mindengine.oculus.frontend.domain.trm.TrmSuite;
import net.mindengine.oculus.frontend.domain.trm.TrmSuiteGroup;
import net.mindengine.oculus.frontend.domain.trm.TrmTask;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.trm.TrmDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

public class CopySharedTaskController extends SecureSimpleViewController {

	private TrmDAO trmDAO;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		verifyPermissions(request);

		Long taskId = Long.parseLong(request.getParameter("taskId"));
		TrmTask task = trmDAO.getTask(taskId);
		if (task == null || !task.getShared())
			throw new UnexistentResource("The task with id " + taskId + " doesn't exist");

		List<TrmSuite> suites = trmDAO.getTaskSuites(taskId);

		// Copying the task
		task.setId(null);
		task.setDate(new Date());
		task.setName(request.getParameter("name"));
		task.setDescription(request.getParameter("description"));

		User user = getUser(request);
		task.setUserId(user.getId());

		Long newTaskId = trmDAO.saveTask(task);
		task.setId(newTaskId);

		// key is the id of group from shared task, value - is id of new group
		Map<Long, Long> groupMap = new HashMap<Long, Long>();

		// Copying groups
		List<TrmSuiteGroup> groups = trmDAO.getTaskSuiteGroups(taskId);
		for (TrmSuiteGroup group : groups) {
			group.setTaskId(newTaskId);
			Long oldGroupId = group.getId();
			group.setId(null);
			Long newGroupId = trmDAO.createSuiteGroup(group);
			groupMap.put(oldGroupId, newGroupId);
		}

		// Copying suites
		for (TrmSuite suite : suites) {
			suite.setId(null);
			suite.setTaskId(newTaskId);
			if (suite.getGroupId() > 0) {
				Long newGroupId = groupMap.get(suite.getGroupId());
				if (newGroupId == null)
					newGroupId = 0L;
				suite.setGroupId(newGroupId);
			}

			trmDAO.saveSuite(suite);
		}

		return new ModelAndView("redirect:../grid/edit-task?id=" + newTaskId);
	}

	public void setTrmDAO(TrmDAO trmDAO) {
		this.trmDAO = trmDAO;
	}

	public TrmDAO getTrmDAO() {
		return trmDAO;
	}
}
