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
