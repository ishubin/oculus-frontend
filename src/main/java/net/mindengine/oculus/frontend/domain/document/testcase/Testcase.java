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
package net.mindengine.oculus.frontend.domain.document.testcase;

import java.io.CharArrayReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.mindengine.oculus.frontend.utils.XmlUtils;

import org.apache.commons.lang3.StringEscapeUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Testcase {

	private List<TestcaseStep> steps = new ArrayList<TestcaseStep>();

	public List<TestcaseStep> getSteps() {
		return steps;
	}

	public void setSteps(List<TestcaseStep> steps) {
		this.steps = steps;
	}

	public String generateXml() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<test-case>");
		buffer.append("<steps>");
		for (TestcaseStep step : steps) {
			buffer.append("<step>");
			buffer.append("<action>");
			buffer.append(StringEscapeUtils.escapeXml(step.getAction()));
			buffer.append("</action>");
			buffer.append("<expected>");
			buffer.append(StringEscapeUtils.escapeXml(step.getExpected()));
			buffer.append("</expected>");
			buffer.append("<comment>");
            buffer.append(StringEscapeUtils.escapeXml(step.getExpected()));
            buffer.append("</comment>");
			buffer.append("</step>");
		}
		buffer.append("</steps>");
		buffer.append("</test-case>");
		return buffer.toString();
	}

	public int getStepsAmount() {
		if (steps != null) {
			return steps.size();
		}
		return 0;
	}

	public static Testcase parse(String xmlData) throws Exception {
		Testcase testcase = new Testcase();

		try {
    		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    		dbf.setNamespaceAware(true);
    		DocumentBuilder builder = dbf.newDocumentBuilder();
    
    		Reader reader = new CharArrayReader(xmlData.toCharArray());
    		org.w3c.dom.Document doc = builder.parse(new org.xml.sax.InputSource(reader));
    
    		Node root = doc.getDocumentElement();
    
    		NodeList nodeList = root.getChildNodes();
    
    		for (int i = 0; i < nodeList.getLength(); i++) {
    			Node node = nodeList.item(i);
    			if ("steps".equals(node.getNodeName())) {
    				NodeList stepsList = node.getChildNodes();
    				for (int j = 0; j < stepsList.getLength(); j++) {
    					Node n = stepsList.item(j);
    
    					TestcaseStep step = new TestcaseStep();
    					step.setAction(XmlUtils.getChildNodeText(n, "action"));
    					step.setExpected(XmlUtils.getChildNodeText(n, "expected"));
    					step.setComment(XmlUtils.getChildNodeText(n, "comment"));
    					testcase.steps.add(step);
    				}
    			}
    		}
		}
		catch (Exception e) {
            e.printStackTrace();
        }

		return testcase;
	}
}
