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
package net.mindengine.oculus.frontend.web.controllers.project.build;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.domain.comment.Comment;
import net.mindengine.oculus.frontend.domain.customization.Customization;
import net.mindengine.oculus.frontend.domain.project.build.Build;
import net.mindengine.oculus.frontend.service.comment.CommentDAO;
import net.mindengine.oculus.frontend.service.customization.CustomizationDAO;
import net.mindengine.oculus.frontend.service.customization.CustomizationUtils;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.service.project.build.BuildDAO;
import net.mindengine.oculus.frontend.service.user.UserDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

public class BuildDisplayController extends SecureSimpleViewController {
	private BuildDAO buildDAO;
	private ProjectDAO projectDAO;
	private UserDAO userDAO;
	private CustomizationDAO customizationDAO;
	private CommentDAO commentDAO;

	@Override
	public Map<String, Object> handleController(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		Long buildId = Long.parseLong(request.getParameter("id"));
		Build build = buildDAO.getBuild(buildId);
		if (build == null)
			throw new UnexistentResource("The build with id " + buildId + " doesn't exist");

		map.put("build", build);
		map.put("project", projectDAO.getProject(build.getProjectId()));
		/*
		 * Loading comments
		 */
		Integer page = 1;
		if (request.getParameter("commentsPage") != null) {
			page = Integer.parseInt(request.getParameter("commentsPage"));
		}
		map.put("comments", commentDAO.getComments(build.getId(), Comment.UNIT_BUILD, page, 20));

		/*
		 * Loading customization
		 */
		map.put("customizationGroups", CustomizationUtils.fetchCustomizationGroups(customizationDAO, userDAO, build.getProjectId(), build.getId(), Customization.UNIT_BUILD));
		map.put("title", getTitle() + build.getName());
		return map;
	}

	public BuildDAO getBuildDAO() {
		return buildDAO;
	}

	public void setBuildDAO(BuildDAO buildDAO) {
		this.buildDAO = buildDAO;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public CustomizationDAO getCustomizationDAO() {
		return customizationDAO;
	}

	public void setCustomizationDAO(CustomizationDAO customizationDAO) {
		this.customizationDAO = customizationDAO;
	}

	public CommentDAO getCommentDAO() {
		return commentDAO;
	}

	public void setCommentDAO(CommentDAO commentDAO) {
		this.commentDAO = commentDAO;
	}

}
