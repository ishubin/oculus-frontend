package net.mindengine.oculus.frontend.service.jobs.runs;

import java.util.Collection;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.regex.Pattern;

import net.mindengine.oculus.frontend.domain.issue.IssueCollation;
import net.mindengine.oculus.frontend.domain.issue.IssueCollationCondition;
import net.mindengine.oculus.frontend.domain.run.CronIctTestRun;
import net.mindengine.oculus.frontend.service.issue.IssueDAO;
import net.mindengine.oculus.frontend.service.runs.TestRunDAO;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CheckTestRunForICTJob extends TimerTask {
	private Log logger = LogFactory.getLog(getClass());
	private IssueDAO issueDAO;
	private TestRunDAO testRunDAO;

	@Override
	public void run() {
		try {
			Collection<CronIctTestRun> cronTestRuns = testRunDAO.getCronIctTestRuns();
			testRunDAO.deleteCronIctTestRuns(cronTestRuns);

			if (cronTestRuns.size() > 0) {
				for (CronIctTestRun testRun : cronTestRuns) {
					Collection<IssueCollation> collations = null;
					if (testRun.getTestId() > 0) {
						collations = issueDAO.getIssueCollationsForTest(testRun.getTestId());
					}
					else {
						collations = issueDAO.getIssueCollationsForTest(testRun.getTestName());
					}
					Iterator<IssueCollation> itCollations = collations.iterator();
					boolean isCollated = false;

					while (itCollations.hasNext() && !isCollated) {
						IssueCollation collation = itCollations.next();
						/*
						 * Checking the reasons match of the created run and
						 * issue collations
						 */
						boolean reasonCheckPass = true;
						if (collation.getReasonPattern() != null && !collation.getReasonPattern().isEmpty()) {
							if (testRun.getReason() == null) {
								testRun.setReason("");
							}
							/*
							 * Unescaping xml for test run reason value to
							 * prevent the following problem:
							 * 
							 * At the end of each test the test run reasons are
							 * taken out of the reports and are saved in one
							 * column separated by "<r>" string.
							 * In order to prevent separation collisions each reason is escaped in Xml.
							 * 
							 * But in order to use this string for issue collisions check we need to unescape it again
							 */
							String reason = StringEscapeUtils.unescapeXml(testRun.getReason());
							if (!Pattern.matches(collation.getReasonPattern(), reason)) {
								reasonCheckPass = false;
							}
						}

						if (reasonCheckPass) {
							/*
							 * Checking the issue collation conditions if it
							 * matches the test suite parameters for this test
							 * run
							 */
							Collection<IssueCollationCondition> conditions = issueDAO.getIssueCollationConditions(collation.getId());
							Iterator<IssueCollationCondition> itConditions = conditions.iterator();

							boolean conditionsCheckPassed = true;

							if (testRun.getSuiteRunParameters() == null)
								testRun.setSuiteRunParameters("");

							while (itConditions.hasNext() && conditionsCheckPassed) {
								IssueCollationCondition condition = itConditions.next();
								if (!testRun.getSuiteRunParameters().contains("<p>" + condition.getTrmPropertyName() + "<v>" + condition.getValue() + "<p>")) {
									conditionsCheckPassed = false;
								}
							}

							if (conditionsCheckPassed) {
								/*
								 * Finally updating the test run with linked
								 * issue
								 */
								issueDAO.linkTestRunToIssue(testRun.getTestRunId(), collation.getIssueId());
								isCollated = true;
							}
						}
					}
				}
			}
			logger.info("ICT Job: done");
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void setIssueDAO(IssueDAO issueDAO) {
		this.issueDAO = issueDAO;
	}

	public IssueDAO getIssueDAO() {
		return issueDAO;
	}

	public void setTestRunDAO(TestRunDAO testRunDAO) {
		this.testRunDAO = testRunDAO;
	}

	public TestRunDAO getTestRunDAO() {
		return testRunDAO;
	}

}
