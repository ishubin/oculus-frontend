package net.mindengine.oculus.frontend.web;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import net.mindengine.oculus.frontend.domain.user.User;

/**
 * This class is used for translating all session variables to a model which is
 * used in JSP via pageContext property. It should be called in each jsp
 * template where the session variable should be used
 * 
 * @author Ivan Shubin
 * 
 */
public class SessionViewHandler {
	public static void createSessionModel(PageContext pageContext, HttpSession httpSession) {
		User user = null;

		String temporaryMessage = null;
		if (httpSession != null) {
			Session session = Session.create(httpSession);
			user = session.getAuthorizedUser();

			temporaryMessage = session.getTemporaryMessage();
		}
		pageContext.setAttribute("user", user);

		pageContext.setAttribute("temporaryMessage", temporaryMessage);

	}
}
