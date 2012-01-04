package net.mindengine.oculus.frontend.web.controllers.issue;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.domain.AjaxModel;
import net.mindengine.oculus.frontend.service.issue.IssueDAO;
import net.mindengine.oculus.frontend.web.controllers.SimpleAjaxController;

public class IssueAjaxFetchController extends SimpleAjaxController {
	private IssueDAO issueDAO;

	@Override
	public AjaxModel handleController(HttpServletRequest request) throws Exception {
		Integer page = Integer.parseInt(request.getParameter("page"));
		String name = request.getParameter("name");
		AjaxModel model = new AjaxModel();

		model.setObject(issueDAO.fetchIssues(name, page, 10));
		return model;
	}

	public void setIssueDAO(IssueDAO issueDAO) {
		this.issueDAO = issueDAO;
	}

	public IssueDAO getIssueDAO() {
		return issueDAO;
	}
}
