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

import java.util.List;

import net.mindengine.oculus.frontend.domain.db.BrowseResult;
import net.mindengine.oculus.frontend.domain.test.Test;
import net.mindengine.oculus.frontend.domain.test.TestGroup;
import net.mindengine.oculus.frontend.domain.test.TestParameter;
import net.mindengine.oculus.frontend.domain.test.TestSearchFilter;

public interface TestDAO {
	public long create(Test test) throws Exception;

	public Test getTest(Long id) throws Exception;

	public void updateTest(Long id, Test test) throws Exception;

	public List<Test> getTestsByProjectId(Long projectId) throws Exception;

	public List<Test> getTestsByProjectId(Long projectId, Long groupId) throws Exception;

	public BrowseResult<Test> getTestsByProjectId(Long projectId, int page, int limit) throws Exception;

	public void delete(Long id, Long projectId) throws Exception;

	public TestParameter getParameter(Long parameterId) throws Exception;

	public List<TestParameter> getTestParameters(Long testId) throws Exception;

	public List<TestParameter> getTestInputParameters(Long testId) throws Exception;

	public List<TestParameter> getTestOutputParameters(Long testId) throws Exception;

	/**
	 * Updates all parameters of specified test.
	 * @param testId
	 * @param parameters
	 * @throws Exception
	 */
	public void saveTestParameters(Long testId, List<TestParameter> parameters) throws Exception;
	

	/**
	 * Fetches tests from the project and all its sub-projects
	 * 
	 * @param projectId
	 * @return Tests fetched from project and all its sub-projects
	 * @throws Exception
	 */
	public List<Test> fetchTestsFromProjects(Long projectId) throws Exception;

	public List<Test> fetchTestsWithParameterByIds(List<Long> ids) throws Exception;

	public Test fetchTestWithParameterById(Long id) throws Exception;

	public void linkWithTestCase(Long testId, Long testCaseId) throws Exception;

	public BrowseResult<Test> searchTests(TestSearchFilter filter) throws Exception;

	public BrowseResult<Test> searchTestsGroupedBySubproject(TestSearchFilter filter) throws Exception;

	public Long createTestGroup(TestGroup testGroup) throws Exception;

	public TestGroup getTestGroup(Long groupId) throws Exception;

	public List<TestGroup> getProjectTestGroups(Long projectId) throws Exception;

	public void deleteTestGroup(Long groupId) throws Exception;

	public void saveTestGroup(TestGroup group) throws Exception;

    public List<TestParameter> getParametersByIds(List<Long> ids) throws Exception;
}
