package net.mindengine.oculus.frontend.web.controllers.report.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.report.filter.Filter;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.report.filter.FilterDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class FilterRemoveController extends SecureSimpleViewController {
	private FilterDAO filterDAO;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User user = getUser(request);
		if (user == null)
			throw new NotAuthorizedException();
		Long filterId = Long.parseLong(request.getParameter("filterId"));
		Filter filter = filterDAO.getFilter(filterId);
		if (filter == null || !user.getId().equals(filter.getUserId()))
			throw new UnexistentResource("Filter doesn't exist");
		filterDAO.deleteFilter(filterId);

		return new ModelAndView(new RedirectView("../report/my-filters"));
	}

	public void setFilterDAO(FilterDAO filterDAO) {
		this.filterDAO = filterDAO;
	}

	public FilterDAO getFilterDAO() {
		return filterDAO;
	}

}
