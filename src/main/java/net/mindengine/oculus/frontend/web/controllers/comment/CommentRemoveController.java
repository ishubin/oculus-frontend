package net.mindengine.oculus.frontend.web.controllers.comment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.comment.Comment;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.comment.CommentDAO;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.web.Session;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

import org.springframework.web.servlet.ModelAndView;

public class CommentRemoveController extends SecureSimpleViewController {
	private CommentDAO commentDAO;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Session session = Session.create(request);
		User user = session.getAuthorizedUser();
		if (user == null)
			throw new NotAuthorizedException();

		Long commentId = Long.parseLong(request.getParameter("commentId"));
		Comment comment = commentDAO.getComment(commentId);
		if (comment == null)
			throw new UnexistentResource("The comment does not exist");

		boolean allowed = true;
		if (!user.getId().equals(comment.getId())) {
			if (!user.hasAllPermissions(getPermissions())) {
				allowed = false;
			}
		}
		if (allowed) {
			commentDAO.deleteComment(commentId);
		}
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
