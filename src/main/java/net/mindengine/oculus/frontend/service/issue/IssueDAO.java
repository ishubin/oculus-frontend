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
* along with Oculus Experior.  If not, see <http://www.gnu.org/licenses/>.
******************************************************************************/
package net.mindengine.oculus.frontend.service.issue;

import java.util.Collection;

import net.mindengine.oculus.frontend.domain.db.BrowseResult;
import net.mindengine.oculus.frontend.domain.issue.Issue;
import net.mindengine.oculus.frontend.domain.issue.IssueCollation;
import net.mindengine.oculus.frontend.domain.issue.IssueCollationCondition;
import net.mindengine.oculus.frontend.domain.issue.IssueCollationTest;
import net.mindengine.oculus.frontend.domain.issue.IssueSearchData;
import net.mindengine.oculus.frontend.domain.issue.IssueSearchFilter;
import net.mindengine.oculus.frontend.domain.report.TestRunSearchData;

public interface IssueDAO {
	public long createIssue(Issue issue) throws Exception;

	public void updateIssue(Issue issue) throws Exception;

	public Issue getIssue(Long issueId) throws Exception;

	public BrowseResult<Issue> fetchIssues(String name, Integer page, Integer limit) throws Exception;

	public void linkTestRunsToIssue(Collection<TestRunSearchData> testRuns, Long issueId) throws Exception;

	public void linkTestRunToIssue(Long testRunId, Long issueId) throws Exception;

	public void linkTestsToIssue(IssueCollation issueCollation) throws Exception;

	public Collection<IssueCollation> getIssueCollations(Long issueId) throws Exception;

	public Collection<IssueCollationTest> getIssueCollationTests(Long issueCollationId) throws Exception;

	public Collection<IssueCollationCondition> getIssueCollationConditions(Long issueCollationId) throws Exception;

	public void deleteIssueCollationTests(Long issueId, Long issueCollationId, Collection<IssueCollationTest> tests) throws Exception;

	public void deleteIssueCollation(Long issueCollationId) throws Exception;

	public Collection<IssueCollation> getIssueCollationsForTest(Long testId) throws Exception;

	public Collection<IssueCollation> getIssueCollationsForTest(String name) throws Exception;

	public BrowseResult<IssueSearchData> searchIssues(IssueSearchFilter filter) throws Exception;
}
