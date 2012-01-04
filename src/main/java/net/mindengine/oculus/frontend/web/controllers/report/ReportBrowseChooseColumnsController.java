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
