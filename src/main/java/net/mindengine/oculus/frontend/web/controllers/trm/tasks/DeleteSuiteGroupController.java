package net.mindengine.oculus.frontend.web.controllers.trm.tasks;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.trm.TrmSuiteGroup;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.trm.TrmDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

import org.springframework.web.servlet.ModelAndView;

public class DeleteSuiteGroupController extends SecureSimpleViewController {
	private TrmDAO trmDAO;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		verifyPermissions(request);

		TrmSuiteGroup group = trmDAO.getSuiteGroup(Long.parseLong(request.getParameter("groupId")));
		if (group == null)
			throw new UnexistentResource("This group doesn't exist");

		trmDAO.removeSuiteGroup(group.getId());

		return new ModelAndView("redirect:../test-run-manager/edit-task?id=" + group.getTaskId());
	}

	public void setTrmDAO(TrmDAO trmDAO) {
		this.trmDAO = trmDAO;
	}

	public TrmDAO getTrmDAO() {
		return trmDAO;
	}
}
