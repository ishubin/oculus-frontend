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
package net.mindengine.oculus.frontend.db.jdbc;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This factory is used to execute the sql queries and map the JDBC ResultSet to
 * a simple Java object. Used only in {@link MySimpleJdbcDaoSupport}
 * 
 * @author Ivan Shubin
 * 
 */
public class BeanMappingFactory {
	private Map<String, BeanMapper> beans;
	private File file;

	protected Log logger = LogFactory.getLog(getClass());
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

	/**
	 * This method is used for loading the java mapping beans from a specified
	 * xml file. Once the {@link #setFile(File)} method was invoked this method
	 * wil be invoked as well.
	 * 
	 * @throws Exception
	 */
	public void loadFactory() throws Exception {
		logger.info("Loading db-mapping factory");

		dbf.setNamespaceAware(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document d = db.parse(file);

		beans = new HashMap<String, BeanMapper>();

		NodeList nodeList = d.getElementsByTagName("bean");

		for (int i = 0; i < nodeList.getLength(); i++) {

			Node node = nodeList.item(i);
			loadBean(node);
		}
	}

	public void loadBean(Node node) throws Exception {
		BeanMapper beanMapper = new BeanMapper();
		beanMapper.setClassName(getAttribute(node, "class"));

		Class<?> clazz = Class.forName(beanMapper.getClassName());
		Method[] methods = clazz.getMethods();

		Map<String, FieldMapper> fields = new HashMap<String, FieldMapper>();
		NodeList nodeList = node.getChildNodes();

		for (int i = 0; i < nodeList.getLength(); i++) {

			Node n = nodeList.item(i);
			if ("property".equals(n.getNodeName())) {
				String property = getAttribute(n, "name");
				String column = getAttribute(n, "column");
				String setter = beanMapper.createSetter(property);

				FieldMapper fieldMapper = new FieldMapper();
				fieldMapper.setProperty(property);
				fieldMapper.setColumn(column);

				boolean bFound = false;
				for (int j = 0; j < methods.length && !bFound; j++) {
					if (methods[j].getName().equals(setter)) {
						fieldMapper.setMethod(methods[j]);
						bFound = true;
					}
				}
				if (!bFound) {
					throw new Exception("There is no setter method for property '" + property + "'");
				}

				fields.put(fieldMapper.getColumn(), fieldMapper);
			}
		}
		beanMapper.setFields(fields);

		beans.put(beanMapper.getClassName(), beanMapper);
	}

	public String getAttribute(Node node, String attrName) {
		Node attr = node.getAttributes().getNamedItem(attrName);
		if (attr == null)
			assert false : "Attribute " + attrName + " wasn't found";
		return attr.getNodeValue();
	}

	public BeanMapper getBeanMapper(Class<?> clazz) throws Exception {
		if (beans == null) {
			throw new Exception("BeanMappingFactory wasn't initialized");
		}

		String name = clazz.getName();
		BeanMapper bm = beans.get(name);

		if (bm == null)
			throw new Exception("Could find beanMapper for '" + name + "' bean");
		return bm;
	}

	public BeanMappingFactory() {
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) throws Exception {
		if (beans == null) {
			this.file = file;
			loadFactory();
			this.file = null;
		}
	}
}
