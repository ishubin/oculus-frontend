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
package net.mindengine.oculus.frontend.web.controllers.report.filter;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.report.filter.Filter;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;
import net.mindengine.oculus.frontend.service.report.filter.FilterDAO;
import net.mindengine.oculus.frontend.web.Session;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class FilterCreateController extends SecureSimpleViewController {
	private FilterDAO filterDAO;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Session session = Session.create(request);
		User user = session.getAuthorizedUser();
		if (user == null)
			throw new NotAuthorizedException();

		String name = request.getParameter("name");
		String description = request.getParameter("description");
		String strFilter = request.getParameter("filter");
		String redirect = request.getParameter("redirect");

		Filter filter = new Filter();
		filter.setName(name);
		filter.setDescription(description);
		filter.setDate(new Date());
		filter.setFilter(strFilter);
		filter.setUserId(user.getId());

		filterDAO.createFilter(filter);

		return new ModelAndView(new RedirectView(redirect));
	}

	public void setFilterDAO(FilterDAO filterDAO) {
		this.filterDAO = filterDAO;
	}

	public FilterDAO getFilterDAO() {
		return filterDAO;
	}

}
