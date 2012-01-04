package net.mindengine.oculus.frontend.web.controllers.comment;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.comment.Comment;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.comment.CommentDAO;
import net.mindengine.oculus.frontend.service.exceptions.InvalidRequest;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;
import net.mindengine.oculus.frontend.web.Session;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

import org.springframework.web.servlet.ModelAndView;

public class CommentAddController extends SecureSimpleViewController {
	private CommentDAO commentDAO;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Session session = Session.create(request);
		User user = session.getAuthorizedUser();
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
