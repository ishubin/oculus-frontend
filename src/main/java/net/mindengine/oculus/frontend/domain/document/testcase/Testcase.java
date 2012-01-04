package net.mindengine.oculus.frontend.domain.document.testcase;

import java.io.CharArrayReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.mindengine.oculus.frontend.domain.document.Document;
import net.mindengine.oculus.frontend.utils.XmlUtils;

import org.apache.commons.lang3.StringEscapeUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class is used to paste data between document controllers and document UI
 * (/WEB-INF/jsp/document/document-project.jsp)
 * 
 * @author <a href="mailto:ivanshubin@mind-engine.net">Ivan Shubin</a>
 * 
 */
public class Testcase {
	/**
	 * This field is used to obtain the DB information about the test-case
	 */
	private Document document;

	/**
	 * This field is used ONLY for exporting in JSON format to build the
	 * test-case customization layout in the project document page
	 */
	private Object customizationGroups;

	private List<TestcaseStep> steps = new ArrayList<TestcaseStep>();

	public List<TestcaseStep> getSteps() {
		return steps;
	}

	public void setSteps(List<TestcaseStep> steps) {
		this.steps = steps;
	}

	/**
	 * Method generates the xml based on testcase data
	 * 
	 * @return
	 */
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
					step.setExpected(XmlUtils.getChildNodeText(n, "comment"));
					testcase.steps.add(step);
				}
			}
		}

		return testcase;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public Document getDocument() {
		return document;
	}

	public void setCustomizationGroups(Object customizationGroups) {
		this.customizationGroups = customizationGroups;
	}

	public Object getCustomizationGroups() {
		return customizationGroups;
	}

}
