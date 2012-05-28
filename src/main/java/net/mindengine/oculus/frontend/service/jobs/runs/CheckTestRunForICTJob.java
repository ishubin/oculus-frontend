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
package net.mindengine.oculus.frontend.service.jobs.runs;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;
import java.util.regex.Pattern;

import net.mindengine.oculus.experior.reporter.ReportReason;
import net.mindengine.oculus.experior.reporter.render.ReportRender;
import net.mindengine.oculus.experior.reporter.render.XmlReportRender;
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
						if (checkReasonsCollation(collation, testRun)) {
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

	private boolean checkReasonsCollation(IssueCollation collation, CronIctTestRun testRun) {
	    if (collation.getReasonPattern() != null && !collation.getReasonPattern().isEmpty()) {
            if (testRun.getReason() == null) {
                testRun.setReason("");
            }
            
            ReportRender reportRender = new XmlReportRender();
            if ( testRun.getReason() != null && !testRun.getReason().isEmpty() ) {
                try {
                    List<ReportReason> reasons = reportRender.decodeReasons(testRun.getReason());
                    for ( ReportReason reason : reasons) {
                        if (Pattern.matches(collation.getReasonPattern(), reason.getReason())) {
                            return true;
                        }
                    }
                }
                catch (Exception e) {
                }
            }
        }
	    return false;
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
