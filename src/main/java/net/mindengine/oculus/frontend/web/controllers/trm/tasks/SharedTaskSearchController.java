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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.config.Config;
import net.mindengine.oculus.frontend.db.search.ColumnFactory;
import net.mindengine.oculus.frontend.domain.trm.TaskSearchColumn;
import net.mindengine.oculus.frontend.domain.trm.TaskSearchFilter;
import net.mindengine.oculus.frontend.service.trm.TrmDAO;
import net.mindengine.oculus.frontend.service.user.UserDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleFormController;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

public class SharedTaskSearchController extends SecureSimpleFormController {
	private TrmDAO trmDAO;
	private UserDAO userDAO;
	/**
	 * This is a factory that is used to fetch the default list of columns to
	 * display in report table in case if column list wasn't defined previously
	 * in session
	 */
	private ColumnFactory columnFactory;

	private Config config;

	@SuppressWarnings( { "unchecked", "rawtypes" })
	@Override
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		TaskSearchFilter filter = (TaskSearchFilter) command;
		filter.setColumns(columnFactory.getColumnList());
		Map map = new HashMap<String, Object>();

		map.put("columns", columnFactory.getColumnList());

		filter.setOnlyShared(true);
		map.put("tasks", trmDAO.searchTasks(filter));
		map.put("columnFactory", columnFactory);
		map.put("searchFilter", filter);
		map.put("title", getTitle());
		return map;
	}

	@Override
	protected boolean isFormSubmission(HttpServletRequest request) {
		return true;
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		TaskSearchFilter filter = (TaskSearchFilter) super.formBackingObject(request);
		filter.setPageLimit(1);
		filter.setPageOffset(1);
		filter.setOrderByColumnId(TaskSearchColumn.TASK_NAME);
		filter.setOrderDirection(-1);
		return filter;
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		ModelAndView mav = new ModelAndView(getSuccessView());
		mav.addAllObjects(referenceData(request, command, errors));
		return mav;
	}

	public ColumnFactory getColumnFactory() {
		return columnFactory;
	}

	public void setColumnFactory(ColumnFactory columnFactory) {
		this.columnFactory = columnFactory;
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public TrmDAO getTrmDAO() {
		return trmDAO;
	}

	public void setTrmDAO(TrmDAO trmDAO) {
		this.trmDAO = trmDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

}
