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
package net.mindengine.oculus.frontend.db.search;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;



import net.mindengine.oculus.frontend.utils.XmlUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ColumnFactory {
	private File file;

	private Collection<SearchColumn> columnList;
	private Class<?> columnClass;

	public Integer getColumnCount() {
		return columnList.size();
	}

	public Collection<SearchColumn> getColumnList() throws Exception {
		if (columnList == null)
			loadColumnList();
		return columnList;
	}

	public SearchColumn getColumnById(Integer id) throws Exception {
		if (columnList == null)
			loadColumnList();
		for (SearchColumn column : columnList) {
			if (column.getId().equals(id)) {
				return column;
			}
		}
		return null;
	}

	public void loadColumnList() throws Exception {
		columnList = new LinkedList<SearchColumn>();
		if (file != null) {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document d = db.parse(file);

			NodeList nodeList = d.getElementsByTagName("column");

			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				SearchColumn column = loadColumn(node);
				columnList.add(column);
			}
		}
	}

	public void setColumnList(List<SearchColumn> columnList) {
		this.columnList = columnList;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) throws Exception {
		this.file = file;
		this.columnList = null;
	}

	private Integer getColumnId(String id, SearchColumn c) {
		try {
			String methodName = "get" + id.substring(0, 1).toUpperCase() + id.substring(1);
			Method method = getColumnClass().getMethod(methodName);
			return (Integer) method.invoke(c);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public SearchColumn loadColumn(Node node) throws Exception {
		String strId = XmlUtils.getNodeAttribute(node, "id");
		SearchColumn column = instantiateColumn();
		column.setId(getColumnId(strId, column));
		column.setName(XmlUtils.getChildNodeText(node, "name"));
		column.setFieldName(XmlUtils.getChildNodeText(node, "field"));
		column.setSqlColumn(XmlUtils.getChildNodeText(node, "sql-column"));
		return column;
	}

	public SearchColumn instantiateColumn() throws Exception {
		Constructor<?> constructor = columnClass.getConstructor();
		return (SearchColumn) constructor.newInstance();
	}

	public void setColumnClass(Class<?> columnClass) {
		this.columnClass = columnClass;
	}

	public Class<?> getColumnClass() {
		return columnClass;
	}

}
