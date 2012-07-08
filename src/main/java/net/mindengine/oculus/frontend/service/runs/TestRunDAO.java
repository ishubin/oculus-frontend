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
package net.mindengine.oculus.frontend.service.runs;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.mindengine.oculus.frontend.domain.report.SavedRun;
import net.mindengine.oculus.frontend.domain.report.SearchFilter;
import net.mindengine.oculus.frontend.domain.report.TestRunSearchData;
import net.mindengine.oculus.frontend.domain.report.TestRunSearchResult;
import net.mindengine.oculus.frontend.domain.run.CronIctTestRun;
import net.mindengine.oculus.frontend.domain.run.SuiteRun;
import net.mindengine.oculus.frontend.domain.run.TestRun;
import net.mindengine.oculus.frontend.domain.run.TestRunParameter;

public interface TestRunDAO {
	public TestRun getRunById(Long id) throws Exception;

	public TestRunSearchResult browseRuns(SearchFilter filter) throws Exception;

	public List<TestRunSearchData> getTestRunsByIds(List<Integer> ids) throws Exception;

	public Long saveRun(SavedRun savedRun) throws Exception;

	public SavedRun getSavedRunById(Long id) throws Exception;

	public SuiteRun getSuiteRun(Long id) throws Exception;

	public Collection<CronIctTestRun> getCronIctTestRuns() throws Exception;

	public void deleteCronIctTestRuns(Collection<CronIctTestRun> runs) throws Exception;

	public Collection<TestRunParameter> getTestRunParameters(Long testRunId) throws Exception;

    public Long createSuiteRun(SuiteRun suiteRun) throws Exception;

    public void updateSuiteEndTime(Long id, Date date) throws Exception;

    public Long createTestRun(TestRun tr) throws Exception;

    public Long createTestRunParameter(Long testRunId, String name, String value, boolean isInput) throws Exception;

    
}
