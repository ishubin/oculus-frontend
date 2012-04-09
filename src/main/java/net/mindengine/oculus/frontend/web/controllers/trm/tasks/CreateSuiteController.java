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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.trm.TrmSuite;
import net.mindengine.oculus.frontend.domain.trm.TrmTask;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.exceptions.InvalidRequest;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.trm.TrmDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleFormController;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class CreateSuiteController extends SecureSimpleFormController {
	private TrmDAO trmDAO;
	
	@SuppressWarnings( { "unchecked", "rawtypes" })
	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {
		Map map = new HashMap();
		Long taskId = Long.parseLong(request.getParameter("taskId"));

		TrmTask task = trmDAO.getTask(taskId);
		if (task == null)
			throw new UnexistentResource("The task does'nt exist");
		map.put("task", task);

		return map;
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		TrmSuite suite = new TrmSuite();
		return suite;
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		Long taskId = Long.parseLong(request.getParameter("taskId"));
		if (taskId <= 0)
			throw new InvalidRequest();

		/*
		 * Verifying that this task belongs to the logged user
		 */
		TrmTask task = trmDAO.getTask(taskId);
		User user = getUser(request);
		if (user == null || !task.getUserId().equals(user.getId()))
			throw new NotAuthorizedException();

		TrmSuite suite = (TrmSuite) command;
		suite.setTaskId(taskId);
		Long suiteId = trmDAO.saveSuite(suite);

		return new ModelAndView(new RedirectView("../grid/edit-suite?id=" + suiteId));
	}

	public void setTrmDAO(TrmDAO trmDAO) {
		this.trmDAO = trmDAO;
	}

	public TrmDAO getTrmDAO() {
		return trmDAO;
	}

}
