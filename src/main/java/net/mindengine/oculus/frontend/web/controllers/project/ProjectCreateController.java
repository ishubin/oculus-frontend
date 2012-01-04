package net.mindengine.oculus.frontend.web.controllers.project;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.customization.Customization;
import net.mindengine.oculus.frontend.domain.project.Project;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.customization.CustomizationDAO;
import net.mindengine.oculus.frontend.service.customization.CustomizationUtils;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.service.user.UserDAO;
import net.mindengine.oculus.frontend.web.Session;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleFormController;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class ProjectCreateController extends SecureSimpleFormController {

	private ProjectDAO projectDAO;
	private CustomizationDAO customizationDAO;
	private UserDAO userDAO;

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {
		Map map = new HashMap();
		String parentId = request.getParameter("parentId");
		if (parentId != null && !parentId.isEmpty()) {
			Project project = projectDAO.getProject(Long.parseLong(parentId));
			if(project!=null){
    			map.put("parentProject", project);
    
    			Long rootId = projectDAO.getProjectRootId(project.getId(), 10);
    			map.put("customizationGroups", CustomizationUtils.fetchCustomizationGroups(customizationDAO, userDAO, rootId, 0L, Customization.UNIT_PROJECT));
			}
		}
		map.put("title", getTitle());
		return map;
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		Project project = (Project) command;
		if (project == null)
			throw new UnexistentResource("");

		Session session = Session.create(request);
		User user = session.getAuthorizedUser();
		project.setAuthorId(user.getId());
		project.setDate(new Date());
		if (project.getParentId() == null || project.getParentId().equals(0)) {
			user.verifyPermission("project_managment");
		}
		else if (project.getParentId() != null && project.getParentId() > 0) {
			user.verifyPermission("subproject_managment");
		}

		project.setId(projectDAO.createProject(project));
		updateProjectCustomizationValues(request, project);

		return new ModelAndView(new RedirectView("../project/display-" + project.getPath()));
	}

	public void updateProjectCustomizationValues(HttpServletRequest request, Project project) throws Exception {
		Long rootId = projectDAO.getProjectRootId(project.getId(), 10);
		CustomizationUtils.updateUnitCustomizationValues(rootId, project.getId(), Customization.UNIT_PROJECT, customizationDAO, request);
	}

	public CustomizationDAO getCustomizationDAO() {
		return customizationDAO;
	}

	public void setCustomizationDAO(CustomizationDAO customizationDAO) {
		this.customizationDAO = customizationDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

}
