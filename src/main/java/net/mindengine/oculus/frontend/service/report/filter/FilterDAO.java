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
package net.mindengine.oculus.frontend.service.report.filter;

import java.util.List;

import net.mindengine.oculus.frontend.domain.report.filter.Filter;

public interface FilterDAO {
	public long createFilter(Filter filter) throws Exception;

	public List<Filter> getUserFilters(long userId) throws Exception;

	public Filter getFilter(long filterId) throws Exception;

	public void deleteFilter(long filterId) throws Exception;
}
