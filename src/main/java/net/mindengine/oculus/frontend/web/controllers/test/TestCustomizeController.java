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
package net.mindengine.oculus.frontend.web.controllers.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.domain.project.Project;
import net.mindengine.oculus.frontend.domain.test.Test;
import net.mindengine.oculus.frontend.domain.test.TestParameter;
import net.mindengine.oculus.frontend.domain.test.TestParametersDefinition;
import net.mindengine.oculus.frontend.service.exceptions.InvalidRequest;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.service.test.TestDAO;
import net.mindengine.oculus.frontend.web.Session;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * Used for editing the test input/output parameters
 * 
 * @author Ivan Shubin
 * 
 */
public class TestCustomizeController extends SecureSimpleViewController {
	private TestDAO testDAO;
	private ProjectDAO projectDAO;

	public TestParameter getTestParameterFromRequest(HttpServletRequest request) throws Exception {
		TestParameter testParameter = new TestParameter();
		testParameter.setName(request.getParameter("name"));
		testParameter.setDescription(request.getParameter("description"));
		testParameter.setType(request.getParameter("type"));
		String controlType = request.getParameter("controlType");
		testParameter.setControlType(controlType);

		if (testParameter.getType() == null)
			throw new InvalidRequest();

		if (controlType != null) {
			if (controlType.equals(TestParameter.CONTROL_TEXT)) {
				testParameter.setDefaultValue(request.getParameter("defaultValueText"));
			}
			else if (controlType.equals(TestParameter.CONTROL_LIST)) {
				int valuesCount = Integer.parseInt(request.getParameter("possibleValuesCount"));
				int defaultValueId = Integer.parseInt(request.getParameter("defaultValueId"));

				String possibleValues = "";
				for (int i = 0; i < valuesCount; i++) {
					String pv = request.getParameter("possibleValue" + i);
					if (pv == null || pv.isEmpty())
						throw new InvalidRequest("Possible Values for List Control are defined incorectly");
					possibleValues += "<value>" + StringEscapeUtils.escapeXml(pv);
				}
				if (valuesCount == 0)
					throw new InvalidRequest("You cannot specify list without possible values");

				testParameter.setPossibleValues(possibleValues);
				testParameter.setDefaultValue(request.getParameter("possibleValue" + defaultValueId));
			}
			else if (controlType.equals(TestParameter.CONTROL_BOOLEAN)) {
				testParameter.setDefaultValue(request.getParameter("defaultValueBoolean"));
			}
			else if (!testParameter.getType().equals(TestParameter.TYPE_OUTPUT)) {
				throw new InvalidRequest();
			}
		}
		else if (testParameter.getType().equals(TestParameter.TYPE_INPUT)) {
			throw new InvalidRequest();
		}
		else {
			testParameter.setControlType(TestParameter.CONTROL_UNDEFINED);
		}

		return testParameter;
	}

	public void addNewParameter(HttpServletRequest request, Long testId) throws Exception {
		TestParameter testParameter = getTestParameterFromRequest(request);
		testParameter.setTestId(testId);
		testDAO.createTestParameter(testParameter);
	}

	public void saveParameter(HttpServletRequest request, Long testId) throws Exception {
		TestParameter testParameter = getTestParameterFromRequest(request);
		testParameter.setTestId(testId);
		Long parameterId = Long.parseLong(request.getParameter("parameterId"));
		testDAO.updateTestParameter(parameterId, testParameter);
	}

	public void deleteParameter(HttpServletRequest request, Long testId) throws Exception {
		Long testParameterId = Long.parseLong(request.getParameter("deleteParameterId"));
		testDAO.deleteTestParameter(testParameterId, testId);
		Session session = Session.create(request);
		session.setTemporaryMessage("Test parameter was removed successfully");
	}

	@Override
	public Map<String, Object> handleController(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		Long testId = Long.parseLong(request.getParameter("id"));
		if (testId == null)
			throw new InvalidRequest("The test ID is not specified");

		// Handling the submit request
		String submit = request.getParameter("Submit");
		if (submit != null) {
			if (submit.equals("Add Parameter")) {
				// Adding new parameter
				addNewParameter(request, testId);
			}
			else if (submit.equals("Delete Parameter")) {
				deleteParameter(request, testId);
			}
			else if (submit.equals("Save")) {
				saveParameter(request, testId);
			}
		}

		TestParametersDefinition definition = new TestParametersDefinition();
		List<TestParameter> parameters = testDAO.getTestParameters(testId);
		Test test = testDAO.getTest(testId);
		List<TestParameter> inputParameters = new ArrayList<TestParameter>();
		List<TestParameter> outputParameters = new ArrayList<TestParameter>();

		for (TestParameter parameter : parameters) {
			if (TestParameter.TYPE_INPUT.equals(parameter.getType())) {
				inputParameters.add(parameter);
			}
			else if (TestParameter.TYPE_OUTPUT.equals(parameter.getType())) {
				outputParameters.add(parameter);
			}
		}

		definition.setInputParameters(inputParameters);
		definition.setOutputParameters(outputParameters);

		map.put("parametersDefinition", definition);
		map.put("test", test);
		Project project = projectDAO.getProject(test.getProjectId());
		map.put("project", project);
		map.put("parentProject", projectDAO.getProject(project.getParentId()));
		map.put("title", getTitle());
		return map;
	}

	public void setTestDAO(TestDAO testDAO) {
		this.testDAO = testDAO;
	}

	public TestDAO getTestDAO() {
		return testDAO;
	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}
}
