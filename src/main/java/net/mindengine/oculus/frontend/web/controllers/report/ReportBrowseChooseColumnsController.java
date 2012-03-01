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
package net.mindengine.oculus.frontend.web.controllers.report;

import java.util.Collection;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.db.search.ColumnFactory;
import net.mindengine.oculus.frontend.db.search.SearchColumn;
import net.mindengine.oculus.frontend.service.exceptions.InvalidRequest;
import net.mindengine.oculus.frontend.web.Session;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * UNUSED! This class should be removed from the project
 * 
 * @author ishubin
 * 
 */
public class ReportBrowseChooseColumnsController extends SecureSimpleViewController {

	private ColumnFactory columnFactory;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		/*
		 * String redirect = request.getParameter("redirect"); if(redirect==null
		 * || redirect.isEmpty()) { throw new InvalidRequest(); } Session
		 * session = Session.create(request); Collection<SearchColumn>
		 * choosedColumns = new LinkedList<SearchColumn>();
		 * Collection<SearchColumn> columns = columnFactory.getColumnList();
		 * for(SearchColumn column: columns) { String reqValue =
		 * request.getParameter("chkConfigureColumns_"+column.getId());
		 * if(reqValue!=null && reqValue.equals("on")) {
		 * choosedColumns.add(column); } }
		 * session.setColumnList(choosedColumns);
		 */
		return new ModelAndView(new RedirectView(request.getParameter("redirect")));
	}

	public void setColumnFactory(ColumnFactory columnFactory) {
		this.columnFactory = columnFactory;
	}

	public ColumnFactory getColumnFactory() {
		return columnFactory;
	}
}
