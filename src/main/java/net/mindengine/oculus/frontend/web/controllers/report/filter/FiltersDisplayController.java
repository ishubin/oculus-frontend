package net.mindengine.oculus.frontend.web.controllers.report.filter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.domain.report.filter.Filter;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;
import net.mindengine.oculus.frontend.service.report.filter.FilterDAO;
import net.mindengine.oculus.frontend.web.Session;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

public class FiltersDisplayController extends SecureSimpleViewController {
	private FilterDAO filterDAO;

	@Override
	public Map<String, Object> handleController(HttpServletRequest request) throws Exception {
		Session session = Session.create(request);
		User user = session.getAuthorizedUser();
		if (user == null)
			throw new NotAuthorizedException();

		Map<String, Object> map = new HashMap<String, Object>();
		List<Filter> filters = filterDAO.getUserFilters(user.getId());

		map.put("filters", filters);
		map.put("title", getTitle());
		return map;
	}

	public void setFilterDAO(FilterDAO filterDAO) {
		this.filterDAO = filterDAO;
	}

	public FilterDAO getFilterDAO() {
		return filterDAO;
	}

}
