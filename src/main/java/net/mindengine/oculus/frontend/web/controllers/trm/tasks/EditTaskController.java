package net.mindengine.oculus.frontend.web.controllers.trm.tasks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.domain.trm.TrmSuite;
import net.mindengine.oculus.frontend.domain.trm.TrmSuiteGroup;
import net.mindengine.oculus.frontend.domain.trm.TrmTask;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.exceptions.InvalidRequest;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;
import net.mindengine.oculus.frontend.service.exceptions.PermissionDeniedException;
import net.mindengine.oculus.frontend.service.exceptions.RedirectException;
import net.mindengine.oculus.frontend.service.trm.TrmDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

/**
 * This controller is used only for editing basic task information and
 * adding/deleting task suites
 * 
 * @author Ivan Shubin
 * 
 */
public class EditTaskController extends SecureSimpleViewController {
	private TrmDAO trmDAO;

	private void saveTask(Long taskId, List<TrmSuite> suites, List<TrmSuiteGroup> groups, Long groupId, HttpServletRequest request) throws Exception {
		TrmTask task = new TrmTask();
		task.setId(taskId);
		
		if(groupId==null || groupId.equals(0L)) {
		    String name = request.getParameter("taskName");
    		String description = request.getParameter("taskDescription");
    		String shared = request.getParameter("shared");
    
    		if (name == null || name.isEmpty() || description == null) {
    			throw new InvalidRequest();
    		}
    
    		if (shared != null && shared.equals("on")) {
    			task.setShared(true);
    		}
    		else
    			task.setShared(false);
    
    		task.setName(name);
    		task.setDescription(description);
    		trmDAO.saveTask(task);
	    }

		if (suites != null) {
			for (TrmSuite suite : suites) {
				String checkbox = request.getParameter("enableSuite" + suite.getId());
				if (checkbox != null && checkbox.equals("on")) {
					suite.setEnabled(true);
				}
				else
					suite.setEnabled(false);
				trmDAO.saveSuite(suite);
			}
		}
		if (groups != null) {
			for (TrmSuiteGroup group : groups) {
				String checkbox = request.getParameter("enableSuiteGroup" + group.getId());
				if (checkbox != null && checkbox.equals("on")) {
					group.setEnabled(true);
				}
				else
					group.setEnabled(false);
				trmDAO.updateSuiteGroup(group);
			}
		}

	}

	@Override
	public Map<String, Object> handleController(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Long taskId = Long.parseLong(request.getParameter("id"));

		Long groupId = 0L;
		if (request.getParameter("groupId") != null) {
			groupId = Long.parseLong(request.getParameter("groupId"));
			if (groupId > 0) {
				TrmSuiteGroup group = trmDAO.getSuiteGroup(groupId);
				map.put("group", group);
			}
		}

		String submit = request.getParameter("Submit");
		List<TrmSuite> suites = trmDAO.getTaskSuites(taskId, groupId);
		List<TrmSuiteGroup> groups = null;

		/*
		 * As suite groups are available within a task only and not within
		 * another suite group there is no sense to fetch all bunch of groups
		 * here. Actually if you place the following code outside the condition
		 * body there might be a problem with saving task.
		 */
		if (groupId == 0L) {
			groups = trmDAO.getTaskSuiteGroups(taskId);
		}
		if (submit != null) {
			if (submit.equals("Save")) {
				saveTask(taskId, suites, groups, groupId, request);
			}
		}

		TrmTask task = trmDAO.getTask(taskId);

		map.put("groups", groups);

		User user = getUser(request);
		if (user == null) {
			throw new NotAuthorizedException();
		}
		if (!user.getId().equals(task.getUserId())) {
			
			if(task.getShared()){
				throw new RedirectException("../test-run-manager/shared-task?id="+task.getId());
			}
			else throw new PermissionDeniedException("You are not authorized to edit tasks of other users");
		}

		map.put("taskDependencies", trmDAO.getTaskDependencies(taskId));
		map.put("task", task);
		map.put("suites", suites);
		return map;
	}

	public void setTrmDAO(TrmDAO trmDAO) {
		this.trmDAO = trmDAO;
	}

	public TrmDAO getTrmDAO() {
		return trmDAO;
	}
}
