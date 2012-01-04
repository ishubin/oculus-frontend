package net.mindengine.oculus.frontend.service.report.filter;

import java.util.List;

import net.mindengine.oculus.frontend.domain.report.filter.Filter;

public interface FilterDAO {
	public long createFilter(Filter filter) throws Exception;

	public List<Filter> getUserFilters(long userId) throws Exception;

	public Filter getFilter(long filterId) throws Exception;

	public void deleteFilter(long filterId) throws Exception;
}
