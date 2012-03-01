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
package net.mindengine.oculus.frontend.utils;

import org.apache.commons.lang.StringEscapeUtils;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlUtils {
	public static String getChildNodeText(Node root, String childName) {
		NodeList nodeList = root.getChildNodes();

		for (int i = 0; i < nodeList.getLength(); i++) {
			if (childName.equals(nodeList.item(i).getNodeName()))
				return nodeList.item(i).getTextContent();
		}
		return "";
	}

	public static String getNodeAttribute(Node node, String name) {
		NamedNodeMap map = node.getAttributes();

		Node n = map.getNamedItem(name);
		if (n != null) {
			return n.getNodeValue();
		}
		return null;
	}

	public static String escapeXml(String text) {
		return StringEscapeUtils.escapeXml(text);
	}

}
