package net.mindengine.oculus.frontend.web.controllers.trm.tasks;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.trm.TrmSuite;
import net.mindengine.oculus.frontend.domain.trm.TrmTask;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;
import net.mindengine.oculus.frontend.service.trm.TrmDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

import org.springframework.web.servlet.ModelAndView;

public class CopySuitesController extends SecureSimpleViewController {

	private TrmDAO trmDAO;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Long taskId = Long.parseLong(request.getParameter("taskId"));
		TrmTask task = trmDAO.getTask(taskId);
		User user = getUser(request);

		if (!task.getUserId().equals(user.getId())) {
			throw new NotAuthorizedException("This task is not yours");
		}

		String suiteIds = request.getParameter("suiteIds");
		Long groupId = Long.parseLong(request.getParameter("groupId"));
		if (suiteIds != null && !suiteIds.isEmpty()) {
			String[] ids = suiteIds.split(",");

			for (String id : ids) {
				if (id.startsWith("suite")) {
					Long suiteId = Long.parseLong(id.substring(5));
					TrmSuite suite = trmDAO.getSuite(suiteId);
					suite.setTaskId(taskId);
					suite.setId(null);

					suite.setGroupId(groupId);
					trmDAO.saveSuite(suite);
				}
			}
		}
		return new ModelAndView("redirect:../test-run-manager/edit-task?id=" + taskId + "&groupId=" + groupId);
	}

	public TrmDAO getTrmDAO() {
		return trmDAO;
	}

	public void setTrmDAO(TrmDAO trmDAO) {
		this.trmDAO = trmDAO;
	}
}
