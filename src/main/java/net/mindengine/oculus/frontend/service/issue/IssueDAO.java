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
