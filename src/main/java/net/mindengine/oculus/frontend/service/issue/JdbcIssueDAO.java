package net.mindengine.oculus.frontend.service.issue;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import net.mindengine.oculus.frontend.db.jdbc.MySimpleJdbcDaoSupport;
import net.mindengine.oculus.frontend.db.search.SearchColumn;
import net.mindengine.oculus.frontend.db.search.SqlSearchCondition;
import net.mindengine.oculus.frontend.domain.db.BrowseResult;
import net.mindengine.oculus.frontend.domain.issue.Issue;
import net.mindengine.oculus.frontend.domain.issue.IssueCollation;
import net.mindengine.oculus.frontend.domain.issue.IssueCollationCondition;
import net.mindengine.oculus.frontend.domain.issue.IssueCollationTest;
import net.mindengine.oculus.frontend.domain.issue.IssueSearchData;
import net.mindengine.oculus.frontend.domain.issue.IssueSearchFilter;
import net.mindengine.oculus.frontend.domain.report.TestRunSearchData;
import net.mindengine.oculus.frontend.service.customization.CustomizationUtils;

import org.apache.commons.lang.StringUtils;

public class JdbcIssueDAO extends MySimpleJdbcDaoSupport implements IssueDAO {

	@Override
	public long createIssue(Issue issue) throws Exception {
		PreparedStatement ps = getConnection().prepareStatement("insert into issues (name, link, summary, description, author_id, date, fixed, fixed_date, project_id, subproject_id) " + "values (?,?,?,?,?,?,?,?,?,?)");

		ps.setString(1, issue.getName());
		ps.setString(2, issue.getLink());
		ps.setString(3, issue.getSummary());
		ps.setString(4, issue.getDescription());
		ps.setLong(5, issue.getAuthorId());
		ps.setTimestamp(6, new Timestamp(issue.getDate().getTime()));
		ps.setInt(7, issue.getFixed());
		ps.setTimestamp(8, new Timestamp(issue.getFixedDate().getTime()));
		ps.setLong(9, issue.getProjectId());
		ps.setLong(10, issue.getSubProjectId());

		logger.info(ps);

		ps.execute();

		ResultSet rs = ps.getGeneratedKeys();
		if (rs.next()) {
			return rs.getLong(1);
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Issue getIssue(Long issueId) throws Exception {
		List<Issue> list = (List<Issue>) query("select * from issues where id = :id", Issue.class, "id", issueId);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public void updateIssue(Issue issue) throws Exception {
		update("update issues set name = :name, link = :link, summary = :summary, description = :description, subproject_id = :subProjectId where id = :id", "name", issue.getName(), "link", issue.getLink(), "summary", issue.getSummary(), "description", issue.getDescription(), "subProjectId", issue
				.getSubProjectId(), "id", issue.getId());
	}

	@SuppressWarnings("unchecked")
	@Override
	public BrowseResult<Issue> fetchIssues(String name, Integer page, Integer limit) throws Exception {
		BrowseResult<Issue> result = new BrowseResult<Issue>();
		if (page == null || page < 1)
			page = 1;
		if (limit == null || limit < 1)
			limit = 10;
		name = "%" + name.replace("*", "%") + "%";

		List<Issue> list = (List<Issue>) query("select * from issues where name like :name or link like :name or summary like :name  order by name, link asc limit " + ((page - 1) * 10) + ", " + limit, Issue.class, "name", name);

		Long count = count("select count(*) from issues issues where name like :name or link like :name or summary like :name", "name", name);
		result.setLimit(limit);
		result.setNumberOfDisplayedResults((long) list.size());
		result.setNumberOfResults(count);
		result.setPage(page);
		result.setResults(list);

		return result;
	}

	@Override
	public void linkTestRunsToIssue(Collection<TestRunSearchData> testRuns, Long issueId) throws Exception {
		StringBuffer ids = new StringBuffer();
		boolean isComma = false;

		for (TestRunSearchData testRun : testRuns) {
			if (isComma) {
				ids.append(",");
			}
			isComma = true;
			ids.append(testRun.getTestRunId());
		}

		update("update test_runs set issue_id = :issueId where id in (" + ids.toString() + ")", "issueId", issueId);
	}

	@Override
	public void linkTestRunToIssue(Long testRunId, Long issueId) throws Exception {
		update("update test_runs set issue_id = :issueId where id = " + testRunId, "issueId", issueId);
	}

	@Override
	public void linkTestsToIssue(IssueCollation issueCollation) throws Exception {
		/*
		 * Inserting into issue_collations table first because then we will use
		 * its generated index
		 */
		PreparedStatement ps = getConnection().prepareStatement("insert into issue_collations (issue_id, reason_pattern) values (?,?)");
		ps.setLong(1, issueCollation.getIssueId());
		ps.setString(2, issueCollation.getReasonPattern());

		logger.info(ps);

		ps.execute();
		ResultSet rs = ps.getGeneratedKeys();
		if (rs.next()) {
			issueCollation.setId(rs.getLong(1));
		}
		else
			throw new Exception("An error appeared while linking tests to issue");

		/*
		 * Inserting all tests into issue_collation_tests table
		 */
		for (IssueCollationTest test : issueCollation.getTests()) {
			update("insert into issue_collation_tests (issue_collation_id, test_id, test_name) values (" + issueCollation.getId() + "," + test.getTestId() + ", :testName)", "testName", test.getTestName());
		}

		update("update issues set dependent_tests = dependent_tests + :size where id = :id", "size", issueCollation.getTests().size(), "id", issueCollation.getIssueId());

		/*
		 * Inserting all conditions into issue_collation_conditions table
		 */
		for (IssueCollationCondition condition : issueCollation.getConditions()) {
			update("insert into issue_collation_conditions (issue_collation_id, trm_property_id, value) values (:issueCollationId, :trmPropertyId, :value)", "issueCollationId", issueCollation.getId(), "trmPropertyId", condition.getTrmPropertyId(), "value", condition.getValue());

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<IssueCollation> getIssueCollations(Long issueId) throws Exception {
		Collection<IssueCollation> collations = (Collection<IssueCollation>) query("select * from issue_collations where issue_id = :issueId", IssueCollation.class, "issueId", issueId);

		for (IssueCollation collation : collations) {
			collation.setTests(getIssueCollationTests(collation.getId()));
			collation.getTests().addAll(getIssueCollationUnboundTests(collation.getId()));
			collation.setConditions(getIssueCollationConditions(collation.getId()));
		}
		return collations;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<IssueCollationTest> getIssueCollationTests(Long issueCollationId) throws Exception {
		return (Collection<IssueCollationTest>) query(
				"select ict.id, ict.issue_collation_id, ict.test_id, t.name as test_name, p.name as projectName, p.path as projectPath from issue_collation_tests ict left join tests t on t.id=ict.test_id left join projects p on p.id = t.project_id where ict.issue_collation_id= :issueCollationId and ict.test_id>0",
				IssueCollationTest.class, "issueCollationId", issueCollationId);
	}

	/**
	 * Returns issue collated tests which weren't bound to the existent tests
	 * (test_id = 0). Such collated tests are checked by theirs name
	 * 
	 * @param issueCollationId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Collection<IssueCollationTest> getIssueCollationUnboundTests(Long issueCollationId) throws Exception {
		return (Collection<IssueCollationTest>) query("select * from issue_collation_tests ict where ict.issue_collation_id = :issueCollationId and ict.test_id=0", IssueCollationTest.class, "issueCollationId", issueCollationId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<IssueCollationCondition> getIssueCollationConditions(Long issueCollationId) throws Exception {
		return (Collection<IssueCollationCondition>) query("SELECT icc.*, trmp.name as trmpName FROM issue_collation_conditions icc left join trm_properties trmp on trmp.id = icc.trm_property_id where icc.issue_collation_id = :issueCollationId", IssueCollationCondition.class, "issueCollationId",
				issueCollationId);
	}

	@Override
	public void deleteIssueCollationTests(Long issueId, Long issueCollationId, Collection<IssueCollationTest> tests) throws Exception {
		StringBuffer buff = new StringBuffer();
		boolean comma = false;

		for (IssueCollationTest test : tests) {
			if (comma)
				buff.append(",");
			comma = true;
			buff.append(test.getId());
		}

		update("delete from issue_collation_tests where issue_collation_id = :issueCollationId and id in (" + buff.toString() + ")", "issueCollationId", issueCollationId);

		update("update issues set dependent_tests = dependent_tests - " + tests.size() + " where id = :issueId", "issueId", issueId);

		Long count = count("select count(*) from issue_collation_tests where issue_collation_id = :issueCollationId", "issueCollationId", issueCollationId);

		if (count == 0) {
			deleteIssueCollation(issueCollationId);
		}
	}

	@Override
	public void deleteIssueCollation(Long issueCollationId) throws Exception {
		update("delete from issue_collations where id = :issueCollationId", "issueCollationId", issueCollationId);

		update("delete from issue_collation_tests where issue_collation_id = :issueCollationId", "issueCollationId", issueCollationId);

		update("delete from issue_collation_conditions where issue_collation_id = :issueCollationId", "issueCollationId", issueCollationId);

	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<IssueCollation> getIssueCollationsForTest(Long testId) throws Exception {
		return (Collection<IssueCollation>) query("select ic.* from issue_collations ic left join issue_collation_tests ict on ict.issue_collation_id = ic.id where ict.test_id=" + testId, IssueCollation.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<IssueCollation> getIssueCollationsForTest(String name) throws Exception {
		return (Collection<IssueCollation>) query("select ic.* from issue_collations ic left join issue_collation_tests ict on ict.issue_collation_id = ic.id where ict.test_name= :testName", IssueCollation.class, "testName", name);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BrowseResult<IssueSearchData> searchIssues(IssueSearchFilter filter) throws Exception {
		String sqlTemplate = "from issues i " + "left join projects p on p.id = i.project_id " + "left join projects sp on sp.id = i.subproject_id " + "left join users u on u.id = i.author_id ";

		int pageLimit = 10;
		if (filter.getPageLimit() < IssueSearchFilter.PAGE_LIMITS.length) {
			pageLimit = IssueSearchFilter.PAGE_LIMITS[filter.getPageLimit()];
		}

		String limit = " limit " + (filter.getPageOffset() - 1) * pageLimit + "," + pageLimit;

		SqlSearchCondition condition = new SqlSearchCondition();
		/*
		 * Preparing the conditions
		 */

		// Customizations
		sqlTemplate = CustomizationUtils.collectCustomizationSearchCondition("i.id", "issue", sqlTemplate, filter.getCustomizations(), condition);

		// Issue Name
		{
			String name = filter.getName();
			if (name != null && !name.isEmpty()) {
				if (name.contains(",")) {
					condition.append(condition.createArrayCondition(name, "i.name", "i.link"));
				}
				else {
					condition.append(condition.createSimpleCondition(name, true, "i.name", "i.link"));
				}
			}
		}
		// Summary
		{
			String summary = filter.getSummary();
			if (summary != null && !summary.isEmpty()) {
				condition.append(condition.createSimpleCondition(summary, true, "i.summary"));
			}
		}
		// Details
		{
			String details = filter.getDetails();
			if (details != null && !details.isEmpty()) {
				condition.append(condition.createSimpleCondition(details, true, "i.details"));
			}
		}
		// User
		{
			String user = filter.getUser();
			if (user != null && !user.isEmpty()) {
				if (user.contains(",")) {
					condition.append(condition.createArrayCondition(user, "u.name", "u.login"));
				}
				else {
					condition.append(condition.createSimpleCondition(user, true, "u.name", "u.login"));
				}
			}
		}
		// Project
		{
			String project = filter.getProject();
			if (project != null && !project.isEmpty()) {
				// checking whether it is an id of project or just a name
				if (StringUtils.isNumeric(project)) {
					// The id of a project was provided
					condition.append(condition.createSimpleCondition(false, "p.id", project));
				}
				else {
					if (project.contains(",")) {
						condition.append(condition.createArrayCondition(project, "p.name"));
					}
					else {
						condition.append(condition.createSimpleCondition(project, true, "p.name"));
					}
				}
			}
		}
		// SubProject
		{
			String subProject = filter.getSubProject();
			if (subProject != null && !subProject.isEmpty()) {
				// checking whether it is an id of project or just a name
				if (StringUtils.isNumeric(subProject)) {
					// The id of a project was provided
					condition.append(condition.createSimpleCondition(false, "sp.id", subProject));
				}
				else {
					if (subProject.contains(",")) {
						condition.append(condition.createArrayCondition(subProject, "sp.name", "sp.path"));
					}
					else {
						condition.append(condition.createSimpleCondition(subProject, true, "sp.name", "sp.path"));
					}
				}
			}
		}

		// Preparing the order by statement
		String order = "";
		Integer orderBy = filter.getOrderByColumnId();
		if (orderBy != null && orderBy >= 0) {
			SearchColumn column = filter.getColumnById(orderBy);
			if (column != null) {

				String direction;
				if (filter.getOrderDirection() >= 0) {
					direction = "asc";
				}
				else
					direction = "desc";

				order = " order by " + column.getSqlColumn() + " " + direction;

			}
		}

		String sqlSelectTemplate = "select " + "u.name as uName, u.login as uLogin, " + "sp.name as sprName, sp.path as sprPath, " + "p.name as prName, p.path as prPath, " + "i.* " + sqlTemplate;

		String sqlSelectCountTemplate = "select count(*) " + sqlTemplate;

		String sql = sqlSelectTemplate + condition + order + limit;
		String sqlCount = sqlSelectCountTemplate + condition;

		BrowseResult<IssueSearchData> result = new BrowseResult<IssueSearchData>();

		List<IssueSearchData> list = (List<IssueSearchData>) query(sql, IssueSearchData.class);
		result.setResults(list);
		result.setNumberOfDisplayedResults(new Long(list.size()));
		result.setNumberOfResults(count(sqlCount));

		return result;
	}

}
