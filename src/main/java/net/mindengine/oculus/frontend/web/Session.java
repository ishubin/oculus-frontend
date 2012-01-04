package net.mindengine.oculus.frontend.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.mindengine.oculus.frontend.db.search.SearchColumn;
import net.mindengine.oculus.frontend.domain.report.TestRunSearchData;
import net.mindengine.oculus.frontend.domain.user.PermissionList;
import net.mindengine.oculus.frontend.domain.user.User;

import org.jfree.chart.JFreeChart;

public class Session {

	private HttpSession session;

	public Session(HttpSession session) {
		this.session = session;
	}

	public void authorizeUser(User user, PermissionList permissionList) {
		user.updatePermissions(permissionList);
		session.setAttribute("user", user);
	}

	public void saveChart(String chartId, JFreeChart chart) {
		session.setAttribute("jfreechart_" + chartId, chart);
	}

	public JFreeChart getChart(String chartId) {
		return (JFreeChart) session.getAttribute("jfreechart_" + chartId);
	}

	public void setIssueColumnList(Collection<SearchColumn> columns) {
		session.setAttribute("issueColumnList", columns);
	}

	@SuppressWarnings("unchecked")
	public Collection<SearchColumn> getIssueColumnList() {
		return (Collection<SearchColumn>) session.getAttribute("issueColumnList");
	}

	@SuppressWarnings("unchecked")
	public List<TestRunSearchData> getCollectedTestRuns() {
		List<TestRunSearchData> list = (List<TestRunSearchData>) session.getAttribute("collectedTestRuns");
		if (list == null) {
			list = new ArrayList<TestRunSearchData>();
			setCollectedTestRuns(list);
		}
		return list;
	}

	public void setCollectedTestRuns(List<TestRunSearchData> testRuns) {
		// Redefining runs ids
		if (testRuns != null) {
			int id = 0;
			for (TestRunSearchData data : testRuns) {
				data.setId(id);
				id++;
			}
		}
		session.setAttribute("collectedTestRuns", testRuns);
	}

	/**
	 * Returns the temporary message id and deletes it from the session. This
	 * method couldn't be run twice as in the second time it will return null
	 * value
	 * 
	 * @return temporary message text
	 */
	public String getTemporaryMessageId() {
		String messageId = (String) session.getAttribute("temporary_message_id");
		if (messageId != null)
			session.removeAttribute("temporary_message_id");
		return messageId;
	}

	/**
	 * Set the temporary id of message to session variable. This message lives
	 * only between two methods: set and get. The id of message is taken from
	 * messages.properties file
	 * 
	 * @param text
	 */
	public void setTemporaryMessageId(String messageId) {
		session.setAttribute("temporary_message_id", messageId);
	}

	/**
	 * Returns the temporary message text and deletes it from the session. This
	 * method couldn't be run twice as in the second time it will return null
	 * value
	 * 
	 * @return temporary message text
	 */
	public String getTemporaryMessage() {
		String message = (String) session.getAttribute("temporary_message");
		if (message != null)
			session.removeAttribute("temporary_message");
		return message;
	}

	/**
	 * Set the temporary message text to session variable. This message lives
	 * only between two methods: set and get.
	 * 
	 * @param text
	 */
	public void setTemporaryMessage(String message) {
		session.setAttribute("temporary_message", message);
	}

	public User getAuthorizedUser() {
		return (User) session.getAttribute("user");
	}

	public void cleanUserData() {
		session.removeAttribute("user");
	}

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {

		this.session = session;
	}

	public static Session create(HttpSession session) {
		return new Session(session);
	}

	public static Session create(HttpServletRequest request) {
		return new Session(request.getSession(true));
	}
}
