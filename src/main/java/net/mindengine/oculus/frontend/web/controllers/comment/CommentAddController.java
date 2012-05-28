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
package net.mindengine.oculus.frontend.web.controllers.comment;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.comment.Comment;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.comment.CommentDAO;
import net.mindengine.oculus.frontend.service.exceptions.InvalidRequest;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;
import net.mindengine.oculus.frontend.web.Auth;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

import org.springframework.web.servlet.ModelAndView;

public class CommentAddController extends SecureSimpleViewController {
	private CommentDAO commentDAO;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User user = Auth.getUserFromRequest(request);
		if (user == null)
			throw new NotAuthorizedException();

		Long unitId = Long.parseLong(request.getParameter("unitId"));
		String unit = request.getParameter("unit");
		String text = request.getParameter("text");
		if (unitId < 1)
			throw new InvalidRequest();
		if (unit == null || unit.isEmpty())
			throw new InvalidRequest();
		if (text == null || text.isEmpty())
			throw new InvalidRequest();

		Comment comment = new Comment();
		comment.setDate(new Date());
		comment.setText(text);
		comment.setUnit(unit);
		comment.setUnitId(unitId);
		comment.setUserId(user.getId());

		commentDAO.addComment(comment);
		String redirect = request.getParameter("redirect");
		if (redirect != null && !redirect.isEmpty()) {
			return new ModelAndView("redirect:" + redirect);
		}
		return new ModelAndView("redirect:../display/home");
	}

	public void setCommentDAO(CommentDAO commentDAO) {
		this.commentDAO = commentDAO;
	}

	public CommentDAO getCommentDAO() {
		return commentDAO;
	}

}
