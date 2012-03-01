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
package net.mindengine.oculus.frontend.service.runs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;

import net.mindengine.oculus.frontend.db.jdbc.MySimpleJdbcDaoSupport;
import net.mindengine.oculus.frontend.db.search.SearchColumn;
import net.mindengine.oculus.frontend.db.search.SqlSearchCondition;
import net.mindengine.oculus.frontend.domain.report.SavedRun;
import net.mindengine.oculus.frontend.domain.report.SearchFilter;
import net.mindengine.oculus.frontend.domain.report.TestRunSearchData;
import net.mindengine.oculus.frontend.domain.report.TestRunSearchResult;
import net.mindengine.oculus.frontend.domain.run.CronIctTestRun;
import net.mindengine.oculus.frontend.domain.run.SuiteRun;
import net.mindengine.oculus.frontend.domain.run.TestRun;
import net.mindengine.oculus.frontend.domain.run.TestRunParameter;
import net.mindengine.oculus.experior.utils.FileUtils;

import org.apache.commons.lang.StringUtils;

public class JdbcTestRunDAO extends MySimpleJdbcDaoSupport implements TestRunDAO {
	private String basicTemplate;
	private File basicTemplateFile;

	private String basicCountTemplate;
	private File basicCountTemplateFile;

	@Override
	public TestRun getRunById(Long id) throws Exception {
		List<?> projectList = query("select * from test_runs where id = :id", TestRun.class, "id", id);

		if (projectList.size() == 1) {
			return (TestRun) projectList.get(0);
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TestRunSearchData> getTestRunsByIds(List<Integer> ids) throws Exception {
		// TODO Auto-generated method stub

		StringBuffer condition = new StringBuffer("where tr.id in (");

		boolean bHasOne = false;
		for (Integer id : ids) {
			if (bHasOne) {
				condition.append(",");
			}
			condition.append(id);
			bHasOne = true;
		}
		condition.append(")");
		if (bHasOne) {
			return (List<TestRunSearchData>) query(basicTemplate + condition.toString(), TestRunSearchData.class);
		}
		else {
			return new ArrayList<TestRunSearchData>();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public TestRunSearchResult browseRuns(SearchFilter filter) throws Exception {
		TestRunSearchResult result = new TestRunSearchResult();

		int pageLimit = 10;
		if (filter.getPageLimit() < SearchFilter.PAGE_LIMITS.length) {
			pageLimit = SearchFilter.PAGE_LIMITS[filter.getPageLimit()];
		}

		String limit = " limit " + (filter.getPageOffset() - 1) * pageLimit + "," + pageLimit;

		SqlSearchCondition condition = createCondition(filter);

		result.setNumberOfResults(count(basicCountTemplate + condition));

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

		result.setResults((List<TestRunSearchData>) query(basicTemplate + condition + order + limit, TestRunSearchData.class));

		return result;
	}

	/**
	 * Collecting all the conditions
	 * 
	 * @param filter
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public SqlSearchCondition createCondition(SearchFilter filter) throws IOException {

		SqlSearchCondition condition = new SqlSearchCondition();

		// TestName
		String testName = filter.getTestCaseName();
		if (testName != null && !testName.isEmpty()) {
			testName = testName.trim();
			// checking whether it is an id of test or just a name
			if (StringUtils.isNumeric(testName)) {
				// The id of a test was provided
				condition.append(condition.createSimpleCondition(false, "t.id", testName));
			}
			else {
				if (testName.contains(",")) {
					condition.append(condition.createArrayCondition(testName, "t.name", "tr.name"));
				}
				else {
					condition.append(condition.createSimpleCondition(testName, true, "t.name", "tr.name"));
				}
			}
		}

		// TestRun Statuses
		List<String> statuses = (List<String>) filter.getTestCaseStatusList();
		if (statuses != null && statuses.size() > 0) {
			String statusColumns[] = new String[statuses.size() * 2];
			int j = 0;
			for (int i = 0; i < statuses.size(); i++) {
				j = i * 2;
				String value = statuses.get(i);
				statusColumns[j] = "tr.status";
				statusColumns[j + 1] = value;
			}
			condition.append(condition.createSimpleCondition(false, statusColumns));
		}
		// TestRun Reason
		String reason = filter.getTestRunReason();
		if (reason != null && !reason.isEmpty()) {
			reason = "*" + reason + "*";
			condition.append(condition.createSimpleCondition(reason, true, "tr.reasons"));
		}
		// Root Project Id
		{
			String parentProject = filter.getRootProject();
			if (parentProject != null && !parentProject.isEmpty()) {
				// checking whether it is an id of project or just a name
				if (StringUtils.isNumeric(parentProject)) {
					Long projectId = Long.parseLong(parentProject);
					if (projectId > 0) {
						// The id of a project was provided
						condition.append(condition.createSimpleCondition(false, "pp.id", parentProject));
					}
				}
			}
		}
		// Project Name
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
		// Suite Run Name
		{
			String suiteRunName = filter.getSuite();
			if (suiteRunName != null && !suiteRunName.isEmpty()) {
				if (suiteRunName.contains(",")) {
					if (StringUtils.isNumeric(suiteRunName.replace(",", ""))) {
						condition.append(condition.createArrayCondition(suiteRunName, "sr.id"));
					}
					else {
						condition.append(condition.createArrayCondition(suiteRunName, "sr.name"));
					}
				}
				else {
					if (StringUtils.isNumeric(suiteRunName)) {
						condition.append(condition.createSimpleCondition(suiteRunName, true, "sr.id"));
					}
					else {
						condition.append(condition.createSimpleCondition(suiteRunName, true, "sr.name"));
					}
				}
			}
		}
		// Suite Run Start Time
		{
			String dateAfter = filter.getSuiteRunTimeAfter();
			if (dateAfter != null && !dateAfter.isEmpty()) {
				if (!dateAfter.contains(":")) {
					dateAfter += " 00:00:00";
				}

				// Checking whether the string is in date format
				try {
					Timestamp.valueOf(dateAfter);
					condition.append("tr.start_time >= '" + dateAfter + "'");
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			String dateBefore = filter.getSuiteRunTimeBefore();
			if (dateBefore != null && !dateBefore.isEmpty()) {
				if (!dateBefore.contains(":")) {
					dateBefore += " 00:00:00";
				}

				// Checking whether the string is in date format
				try {
					Timestamp.valueOf(dateBefore);
					condition.append("tr.start_time <= '" + dateBefore + "'");
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		// Suite Run Parameters
		{
			String suiteRunParameters = filter.getSuiteRunParameters();
			if (suiteRunParameters != null && !suiteRunParameters.isEmpty()) {
				// Parsing the suiteRunParameters template;
				Properties prop = new Properties();
				prop.load(new StringReader(suiteRunParameters));
				for (Entry<Object, Object> property : prop.entrySet()) {
					condition.append(condition.createSimpleCondition("*" + property.getKey() + "<v>" + property.getValue() + "*", true, "sr.parameters"));
				}
			}
		}
		// Suite Run Agent
		{
			String suiteRunAgent = filter.getSuiteRunAgent();
			if (suiteRunAgent != null && !suiteRunAgent.isEmpty()) {
				if (suiteRunAgent.contains(",")) {
					condition.append(condition.createArrayCondition(suiteRunAgent, "sr.agent_name"));
				}
				else {
					condition.append(condition.createSimpleCondition(suiteRunAgent, true, "sr.agent_name"));
				}
			}
		}
		// User designer
		{
			String designer = filter.getUserDesigner();
			if (designer != null && !designer.isEmpty()) {
				if (designer.contains(",")) {
					if (StringUtils.isNumeric(designer.replace(",", ""))) {
						condition.append(condition.createArrayCondition(designer, "ud.id"));
					}
					else {
						condition.append(condition.createArrayCondition(designer, "ud.login", "ud.name"));
					}
				}
				else {
					// checking whether it is an id of project or just a name
					if (StringUtils.isNumeric(designer)) {
						// The id of a project was provided
						condition.append(condition.createSimpleCondition(false, "ud.id", designer));
					}
					else {
						condition.append(condition.createSimpleCondition(designer, true, "ud.login", "ud.name"));
					}
				}
			}
		}

		// User runner
		{
			String runner = filter.getUserRunner();
			if (runner != null && !runner.isEmpty()) {
				if (runner.contains(",")) {
					if (StringUtils.isNumeric(runner.replace(",", ""))) {
						condition.append(condition.createArrayCondition(runner, "ur.id"));
					}
					else {
						condition.append(condition.createArrayCondition(runner, "ur.login", "ur.name"));
					}
				}
				else {
					// checking whether it is an id of project or just a name
					if (StringUtils.isNumeric(runner)) {
						// The id of a project was provided
						condition.append(condition.createSimpleCondition(false, "ur.id", runner));
					}
					else {
						condition.append(condition.createSimpleCondition(runner, true, "ur.login", "ur.name"));
					}
				}
			}
		}
		// Issue
		String issueName = filter.getIssue();
		if (issueName != null && !issueName.isEmpty()) {
			if (issueName.contains(",")) {
				condition.append(condition.createArrayCondition(issueName, "iss.name", "iss.link"));
			}
			else {
				condition.append(condition.createSimpleCondition(issueName, true, "iss.name", "iss.link"));
			}
		}
		return condition;
	}

	@Override
	public Long saveRun(SavedRun savedRun) throws Exception {

		PreparedStatement ps = getConnection().prepareStatement("insert into saved_runs (name, user_id, date) values (?, ?, ?)");

		ps.setString(1, savedRun.getName());
		ps.setLong(2, savedRun.getUserId());
		ps.setTimestamp(3, new Timestamp(savedRun.getDate().getTime()));

		logger.info(ps);
		ps.executeUpdate();

		ResultSet rs = ps.getGeneratedKeys();

		if (rs.next()) {
			return rs.getLong(1);

		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SavedRun getSavedRunById(Long id) throws Exception {
		List<SavedRun> list = (List<SavedRun>) query("select * from saved_runs where id = :id", SavedRun.class, "id", id);

		if (list.size() > 0)
			return list.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SuiteRun getSuiteRun(Long id) throws Exception {
		List<SuiteRun> list = (List<SuiteRun>) query("select * from suite_runs where id = :id", SuiteRun.class, "id", id);

		if (list.size() > 0)
			return list.get(0);
		return null;
	}

	public String getBasicCountTemplate() {
		return basicCountTemplate;
	}

	public void setBasicCountTemplate(String basicCountTemplate) {
		this.basicCountTemplate = basicCountTemplate;
	}

	public File getBasicCountTemplateFile() {
		return basicCountTemplateFile;
	}

	public void setBasicCountTemplateFile(File basicCountTemplateFile) throws FileNotFoundException, IOException {
		this.basicCountTemplateFile = basicCountTemplateFile;
		setBasicCountTemplate(FileUtils.readFile(basicCountTemplateFile));
		this.basicCountTemplateFile = null;
	}

	public String getBasicTemplate() {
		return basicTemplate;
	}

	public void setBasicTemplate(String basicTemplate) {
		this.basicTemplate = basicTemplate;
	}

	public File getBasicTemplateFile() {
		return basicTemplateFile;
	}

	public void setBasicTemplateFile(File basicTemplateFile) throws FileNotFoundException, IOException {
		this.basicTemplateFile = basicTemplateFile;
		setBasicTemplate(FileUtils.readFile(basicTemplateFile));
		this.basicTemplateFile = null;

	}

	@Override
	public void deleteCronIctTestRuns(Collection<CronIctTestRun> runs) throws Exception {
		if (runs.size() > 0) {
			StringBuffer buff = new StringBuffer();
			boolean comma = false;
			for (CronIctTestRun run : runs) {
				if (comma)
					buff.append(",");
				comma = true;
				buff.append(run.getId());
			}

			update("delete from cron_ict_test_runs where id in (" + buff.toString() + ")");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<CronIctTestRun> getCronIctTestRuns() throws Exception {
		return (Collection<CronIctTestRun>) query("select * from cron_ict_test_runs order by id asc limit 0, 100", CronIctTestRun.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<TestRunParameter> getTestRunParameters(Long testRunId) throws Exception {
		return (Collection<TestRunParameter>) query("select * from test_run_parameters where test_run_id = :testRunId", TestRunParameter.class, "testRunId", testRunId);
	}

}
