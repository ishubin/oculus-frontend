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
package net.mindengine.oculus.frontend.service.test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.mindengine.oculus.frontend.db.jdbc.MySimpleJdbcDaoSupport;
import net.mindengine.oculus.frontend.db.search.SearchColumn;
import net.mindengine.oculus.frontend.db.search.SqlSearchCondition;
import net.mindengine.oculus.frontend.domain.db.BrowseResult;
import net.mindengine.oculus.frontend.domain.test.Test;
import net.mindengine.oculus.frontend.domain.test.TestGroup;
import net.mindengine.oculus.frontend.domain.test.TestParameter;
import net.mindengine.oculus.frontend.domain.test.TestSearchColumn;
import net.mindengine.oculus.frontend.domain.test.TestSearchFilter;
import net.mindengine.oculus.frontend.service.customization.CustomizationUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JdbcTestDAO extends MySimpleJdbcDaoSupport implements TestDAO {
	Log logger = LogFactory.getLog(this.getClass());

	@Override
	public long create(Test test) throws Exception {
		PreparedStatement ps = getConnection().prepareStatement("insert into tests (name, description, project_id, author_id, date, mapping, group_id, content) values (?, ?, ?, ?, ?, ?, ?, ?)");

		ps.setString(1, test.getName());
		ps.setString(2, test.getDescription());
		ps.setLong(3, test.getProjectId());
		ps.setLong(4, test.getAuthorId());
		ps.setTimestamp(5, new Timestamp(test.getDate().getTime()));
		ps.setString(6, test.getMapping());
		ps.setLong(7, test.getGroupId());
		ps.setString(8, test.getContent());

		logger.info(ps);
		ps.executeUpdate();

		ResultSet rs = ps.getGeneratedKeys();
		Long testId = 0L;
		if (rs.next()) {
			testId = rs.getLong(1);
		}

		/*
		 * Increasing the tests_count value for current project
		 */
		update("update projects set tests_count=tests_count+1 where id = :id", "id", test.getProjectId());
		return testId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Test getTest(Long id) throws Exception {
		List<Test> list = (List<Test>) query("select t.*, tg.name as groupName from tests t left join test_groups tg on tg.id=t.group_id where t.id = :id", Test.class, "id", id);

		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public void updateTest(Long id, Test test) throws Exception {
		update("update tests set name = :name, description = :description, project_id =:projectId, mapping =:mappingId, group_id =:groupId, content = :content where id = :id", "id", id, "name", test.getName(), "description", test.getDescription(), "projectId", test.getProjectId(), "mappingId", test.getMapping(),
				"groupId", test.getGroupId(),
				"content", test.getContent());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Test> getTestsByProjectId(Long projectId) throws Exception {
		return (List<Test>) query("select *, u.name as authorName, u.login as authorLogin from tests t left join users u on u.id = t.author_id where t.project_id = :projectId order by t.name asc", Test.class, "projectId", projectId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Test> getTestsByProjectId(Long projectId, Long groupId) throws Exception {
		return (List<Test>) query("select *, u.name as authorName, u.login as authorLogin from tests t left join users u on u.id = t.author_id where t.project_id = :projectId and t.group_id = :groupId order by t.name asc", Test.class, "projectId", projectId, "groupId", groupId);
	}

	@Override
	public void delete(Long id, Long projectId) throws Exception {
		update("delete from tests where id = :id", "id", id);

		update("update projects set tests_count=tests_count-1 where id = :id", "id", projectId);

		// Deleting the test parameters
		update("delete from test_parameters where test_id = :testId", "testId", id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TestParameter> getTestParameters(Long testId) throws Exception {
		return (List<TestParameter>) query("select * from test_parameters where test_id = :testId order by sortindex asc", TestParameter.class, "testId", testId);
	}

	@SuppressWarnings("unchecked")
	public List<TestParameter> getTestParameterByType(Long testId, String type) throws Exception {
		return (List<TestParameter>) query("select * from test_parameters where test_id = :testId and type = :type order by sortindex asc", TestParameter.class, "testId", testId, "type", type);
	}

	@Override
	public List<TestParameter> getTestInputParameters(Long testId) throws Exception {
		return getTestParameterByType(testId, "input");
	}

	@Override
	public List<TestParameter> getTestOutputParameters(Long testId) throws Exception {
		return getTestParameterByType(testId, "output");
	}

	@Override
	public void saveTestParameters(Long testId, List<TestParameter> parameters) throws Exception {
	    List<TestParameter> currentParameters = this.getTestParameters(testId);
	    
	    List<Long> oldParameterIds = new LinkedList<Long>();
	    for ( TestParameter currentParameter : currentParameters ) {
	        oldParameterIds.add(currentParameter.getId());
	    }
	    
	    List<Long> newParameterIds = new LinkedList<Long>();
	    
	    for ( TestParameter parameter: parameters ) {
	        if ( parameter.getId() != null && oldParameterIds.contains(parameter.getId())) {
	            //updating current parameter
	            updateTestParameter(parameter.getId(), parameter);
	            newParameterIds.add(parameter.getId());
	        }
	        else {
	            //adding new parameter
	            parameter.setTestId(testId);
	            Long parameterId = createTestParameter(parameter);
	            newParameterIds.add(parameterId);
	        }
	    }
	    
	    //Removing unused parameters
	    for ( Long parameterId : oldParameterIds ) {
	        if ( !newParameterIds.contains(parameterId) ) {
	            deleteTestParameter(parameterId, testId);
	        }
	    }
	}
	
	private Long createTestParameter(TestParameter parameter) throws Exception {
	    PreparedStatement ps = getConnection().prepareStatement("insert into test_parameters (name,description, type, control_type, default_value, possible_values, test_id) "
	            + "values (?,?,?,?,?,?,?)");
	    ps.setString(1, parameter.getName());
	    ps.setString(2, parameter.getDescription());
	    ps.setString(3, parameter.getType());
	    ps.setString(4, parameter.getControlType());
	    ps.setString(5, parameter.getDefaultValue());
	    ps.setString(6, parameter.getPossibleValues());
	    ps.setLong(7, parameter.getTestId());

	    logger.info(ps);
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        Long parameterId = null;
        if (rs.next()) {
            parameterId = rs.getLong(1);
        }
        return parameterId;
	}

	private void updateTestParameter(Long parameterId, TestParameter testParameter) throws Exception {

		update("update test_parameters set name=:name, description=:description, type=:type, control_type=:controlType, default_value=:defaultValue, possible_values=:possibleValues " + "where id = :id", "id", parameterId, "name", testParameter.getName(), "description", testParameter
				.getDescription(), "type", testParameter.getType(), "controlType", testParameter.getControlType(), "defaultValue", testParameter.getDefaultValue(), "possibleValues", testParameter.getPossibleValues());
	}

	private void deleteTestParameter(Long testParameterId, Long testId) throws Exception {
		update("delete from test_parameters where test_id = :testId and id = :id", "testId", testId, "id", testParameterId);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public BrowseResult<Test> getTestsByProjectId(Long projectId, int page, int limit) throws Exception {
		BrowseResult<Test> browseResult = new BrowseResult<Test>();
		String strLimit = " limit " + (page - 1) * limit + "," + limit;

		browseResult.setNumberOfResults(count("select count(*) as count from tests where project_id = :projectId", "projectId", projectId));

		List<Test> list = (List<Test>) query("select * from tests where project_id = :projectId " + strLimit, Test.class, "projectId", projectId);
		browseResult.setResults(list);
		browseResult.setNumberOfDisplayedResults((long) list.size());

		browseResult.setPage(page);
		browseResult.setLimit(limit);
		return browseResult;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Test> fetchTestsFromProjects(Long projectId) throws Exception {
		List<Test> list = (List<Test>) query("select t.* from tests t, projects p1, projects p2 " + "where ((t.project_id = p2.id and p2.parent_id = p1.id) or (t.project_id = p1.id) )and p1.id = :projectId group by t.id", Test.class, "projectId", projectId);
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Test> fetchTestsWithParameterByIds(List<Long> ids) throws Exception {
		StringBuffer str = new StringBuffer();
		str.append("(");
		boolean bComma = false;
		for (Long id : ids) {
			if (bComma)
				str.append(",");
			str.append(id);
			bComma = true;
		}
		str.append(")");

		List<Test> tests = (List<Test>) query("select tests.*, projects.path as project_path from tests left join projects on tests.project_id = projects.id where tests.id in " + str.toString(), Test.class);

		for (Test test : tests) {
			List<TestParameter> parameters = (List<TestParameter>) query("select * from test_parameters where test_id = :testId order by type", TestParameter.class, "testId", test.getId());

			List<TestParameter> inputParameters = new ArrayList<TestParameter>();
			List<TestParameter> outputParameters = new ArrayList<TestParameter>();
			for (TestParameter parameter : parameters) {
				if (parameter.getType().equals(TestParameter.TYPE_INPUT)) {
					inputParameters.add(parameter);
				}
				else {
					outputParameters.add(parameter);
				}
			}
			test.setInputParameters(inputParameters);
			test.setOutputParameters(outputParameters);
		}
		return tests;
	}

	@Override
	public void linkWithTestCase(Long testId, Long testCaseId) throws Exception {
		update("update tests set test_case_id = :testCaseId where id = :id", "testCaseId", testCaseId, "id", testId);
	}

	public SqlSearchCondition createSearchCondition(TestSearchFilter filter) {
		SqlSearchCondition condition = new SqlSearchCondition();
		// Name
		{
			String name = filter.getName();
			if (name != null && !name.isEmpty()) {
				if (name.contains(",")) {
					condition.append(condition.createArrayCondition(name, "t.name"));
				}
				else {
					condition.append(condition.createSimpleCondition(name, true, "t.name"));
				}
			}
		}
		return condition;
	}

	public BrowseResult<Test> searchTestsGroupedBySubproject(TestSearchFilter filter) throws Exception {
		filter.setOrderByColumnId(TestSearchColumn.SUBPROJECT);
		filter.setOrderDirection(1);
		return searchTests(filter);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BrowseResult<Test> searchTests(TestSearchFilter filter) throws Exception {
		SqlSearchCondition condition = createSearchCondition(filter);

		Integer pageLimit = 10;

        String limit = "";
        
		if(filter.getPageLimit()!=null){
		    if (filter.getPageLimit() < TestSearchFilter.PAGE_LIMITS.length) {
	            pageLimit = TestSearchFilter.PAGE_LIMITS[filter.getPageLimit()];
	        }
		    
		    limit = " limit " + (filter.getPageOffset() - 1) * pageLimit + "," + pageLimit;
		}

		String sqlSelect = "select tg.name as groupName, pp.name as parentProjectName, pp.path as parentProjectPath, p.name as project_name, p.path as project_path, " + "u.name as authorName, u.login as authorLogin, " + "t.* ";

		String sqlCount = "select count(*) ";

		String sqlFrom = "from tests t " + "left join projects p on p.id = t.project_id " + "left join projects pp on pp.id = p.parent_id " + "left join test_groups tg on tg.id = t.group_id " + "left join users u on u.id = t.author_id ";

		// Customizations
		sqlFrom = CustomizationUtils.collectCustomizationSearchCondition("t.id", "test", sqlFrom, filter.getCustomizations(), condition);

		// Test Name
		{
			String name = filter.getName();
			if (name != null && !name.isEmpty()) {
				if (name.contains(",")) {
					condition.append(condition.createArrayCondition(name, "t.name"));
				}
				else {
					condition.append(condition.createSimpleCondition(name, true, "t.name"));
				}
			}
		}
		// Test Group Name
		{
			String name = filter.getTestGroup();
			if (name != null && !name.isEmpty()) {
				if (name.contains(",")) {
					condition.append(condition.createArrayCondition(name, "tg.name"));
				}
				else {
					condition.append(condition.createSimpleCondition(name, true, "tg.name"));
				}
			}
		}
		// Sub-Project
		{
			String subProject = filter.getSubProject();
			if (subProject != null && !subProject.isEmpty()) {
				// checking whether it is an id of project or just a name
				if (StringUtils.isNumeric(subProject)) {
					// The id of a project was provided
					condition.append(condition.createSimpleCondition(false, "p.id", subProject));
				}
				else {
					if (subProject.contains(",")) {
						condition.append(condition.createArrayCondition(subProject, "p.name", "p.path"));
					}
					else {
						condition.append(condition.createSimpleCondition(subProject, true, "p.name", "p.path"));
					}
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
					condition.append(condition.createSimpleCondition(false, "pp.id", project));
				}
				else {
					if (project.contains(",")) {
						condition.append(condition.createArrayCondition(project, "pp.name"));
					}
					else {
						condition.append(condition.createSimpleCondition(project, true, "pp.name"));
					}
				}
			}
		}
		// User
		{
			String user = filter.getDesigner();
			if (user != null && !user.isEmpty()) {
				if (user.contains(",")) {
					condition.append(condition.createArrayCondition(user, "u.name", "u.login"));
				}
				else {
					condition.append(condition.createSimpleCondition(user, true, "u.name", "u.login"));
				}
			}
		}

		BrowseResult<Test> result = new BrowseResult<Test>();

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

				order = " order by " + column.getSqlColumn() + " " + direction + " ";

				// Adding additional order by name parameter in case if it
				// wasn't already specified in first order
				if (orderBy != TestSearchColumn.TEST_NAME) {
					order += ", tg.name asc, tg.id asc, name asc ";
				}
			}
		}

		String sql = sqlSelect + sqlFrom + condition + order + limit;

		result.setNumberOfResults(count(sqlCount + sqlFrom + condition));
		result.setResults((List<Test>) query(sql, Test.class));
		result.setPage(filter.getPageOffset());
		result.setLimit(pageLimit);
		return result;
	}

	@Override
	public Test fetchTestWithParameterById(Long id) throws Exception {
		List<Long> ids = new LinkedList<Long>();
		ids.add(id);
		List<Test> tests = fetchTestsWithParameterByIds(ids);
		if (tests.size() > 0) {
			return tests.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public TestParameter getParameter(Long parameterId) throws Exception {
		List<TestParameter> list = (List<TestParameter>) query("select * from test_parameters where id = :id", TestParameter.class, "id", parameterId);

		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public Long createTestGroup(TestGroup testGroup) throws Exception {
		PreparedStatement ps = getConnection().prepareStatement("insert into test_groups (name, description, project_id) values (?, ?, ?)");
		logger.info(ps);
		ps.setString(1, testGroup.getName());
		ps.setString(2, testGroup.getDescription());
		ps.setLong(3, testGroup.getProjectId());
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();

		Long groupId = 0L;
		if (rs.next()) {
			groupId = rs.getLong(1);
		}
		return groupId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TestGroup> getProjectTestGroups(Long projectId) throws Exception {
		List<TestGroup> list = (List<TestGroup>) query("select * from test_groups where project_id = :projectId", TestGroup.class, "projectId", projectId);
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public TestGroup getTestGroup(Long groupId) throws Exception {
		List<TestGroup> list = (List<TestGroup>) query("select * from test_groups where id = :groupId", TestGroup.class, "groupId", groupId);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public void deleteTestGroup(Long groupId) throws Exception {
		update("delete from test_groups where id =:groupId", "groupId", groupId);
		update("update tests set group_id = 0 where group_id=:groupId", "groupId", groupId);
	}

	@Override
	public void saveTestGroup(TestGroup group) throws Exception {
		update("update test_groups set name = :name, description = :description where id = :id", "name", group.getName(), "description", group.getDescription(), "id", group.getId());
	}
	
	@Override
	public List<TestParameter> getParametersByIds(List<Long> ids) throws Exception {
	    if ( ids.size() > 0 ) {
    	    StringBuilder pIds = new StringBuilder();
    	    
    	    boolean comma = false;
    	    for ( Long id : ids ) {
    	        if ( comma ) {
    	            pIds.append(",");
    	        }
    	        comma = true;
    	        pIds.append(id);
    	    }
    	    
    	    List<TestParameter> list = (List<TestParameter>) query("select * from test_parameters where id in (" + pIds.toString() + ")", TestParameter.class);
    	    return list;
	    }
	    else {
	        return new LinkedList<TestParameter>();
	    }
	}

}
