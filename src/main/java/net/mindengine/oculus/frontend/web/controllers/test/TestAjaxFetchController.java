package net.mindengine.oculus.frontend.web.controllers.test;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.domain.AjaxModel;
import net.mindengine.oculus.frontend.domain.test.Test;
import net.mindengine.oculus.frontend.service.exceptions.InvalidRequest;
import net.mindengine.oculus.frontend.service.test.TestDAO;
import net.mindengine.oculus.frontend.web.controllers.SimpleAjaxController;

/**
 * Used on Edit Suite page for fetching all tests data when the user ads the
 * selected tests from the list to the suite. Fetches tests with their
 * parameters.
 * 
 * @author Ivan Shubin
 * 
 */
public class TestAjaxFetchController extends SimpleAjaxController {
	private TestDAO testDAO;

	@Override
	public AjaxModel handleController(HttpServletRequest request) throws Exception {
		AjaxModel model = new AjaxModel();
		String ids = request.getParameter("ids");
		if (ids == null)
			throw new InvalidRequest();
		String[] idsArray = ids.split(",");
		List<Long> idsList = new ArrayList<Long>();
		for (String id : idsArray) {
			if (!id.isEmpty()) {
				idsList.add(Long.parseLong(id));
			}
		}

		List<Test> tests = testDAO.fetchTestsWithParameterByIds(idsList);
		model.setObject(tests);
		return model;
	}

	public void setTestDAO(TestDAO testDAO) {
		this.testDAO = testDAO;
	}

	public TestDAO getTestDAO() {
		return testDAO;
	}
}
