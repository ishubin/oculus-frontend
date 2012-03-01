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
package net.mindengine.oculus.frontend.domain.db;

import java.util.List;

/**
 * This class is used for fetching an amount of some data with description of
 * its count, page, limit.
 * 
 * @author Ivan Shubin
 * 
 * @param <T>
 *            Used in results field
 */
public class BrowseResult<T> {
	private Long numberOfResults;
	private Long numberOfDisplayedResults;
	private List<T> results;
	private Integer page;
	private Integer limit;

	public Long getNumberOfResults() {
		return numberOfResults;
	}

	public void setNumberOfResults(Long numberOfResults) {
		this.numberOfResults = numberOfResults;
	}

	public Long getNumberOfDisplayedResults() {
		return numberOfDisplayedResults;
	}

	public void setNumberOfDisplayedResults(Long numberOfDisplayedResults) {
		this.numberOfDisplayedResults = numberOfDisplayedResults;
	}

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}
}
