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
