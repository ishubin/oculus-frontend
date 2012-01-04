package net.mindengine.oculus.frontend.domain.report;

import java.util.Collection;
import java.util.List;

import net.mindengine.oculus.frontend.db.search.SearchColumn;

public class TestRunSearchResult {
	private Long numberOfResults;
	private List<TestRunSearchData> results;
	private Collection<SearchColumn> columns;

	private Long page;

	public TestRunSearchResult() {
	}

	/**
	 * This method is used to run through all the test runs in the list and give
	 * them ids
	 */
	public void handleTheList() {
		int id = 0;
		for (TestRunSearchData data : results) {
			data.setId(id);
			id++;
		}
	}

	public Long getNumberOfResults() {
		return numberOfResults;
	}

	public void setNumberOfResults(Long numberOfResults) {
		this.numberOfResults = numberOfResults;
	}

	public Integer getNumberOfDisplayedResults() {
		return results.size();
	}

	public List<TestRunSearchData> getResults() {
		return results;
	}

	public void setResults(List<TestRunSearchData> results) {
		this.results = results;
	}

	public Long getPage() {
		return page;
	}

	public void setPage(Long page) {
		this.page = page;
	}

	public Collection<SearchColumn> getColumns() {
		return columns;
	}

	public void setColumns(Collection<SearchColumn> columns) {
		this.columns = columns;
	}

	public int getColumnsAmount() {
		if (columns != null)
			return columns.size();
		return 0;
	}
}
