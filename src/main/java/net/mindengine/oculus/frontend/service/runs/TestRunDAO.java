package net.mindengine.oculus.frontend.service.runs;

import java.util.Collection;
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

}
