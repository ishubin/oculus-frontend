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
package net.mindengine.oculus.frontend.domain.trm;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.mindengine.oculus.experior.exception.TestIsNotDefinedException;
import net.mindengine.oculus.experior.test.descriptors.TestDefinition;
import net.mindengine.oculus.experior.test.descriptors.TestDependency;
import net.mindengine.oculus.experior.test.descriptors.TestParameter;
import net.mindengine.oculus.frontend.service.test.TestDAO;
import net.mindengine.oculus.frontend.utils.JSONUtils;

import antlr.RecognitionException;
import antlr.TokenStreamException;

import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.parser.JSONParser;

public class TrmSuite implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 9179194995513556585L;

	private Long id;
	private Long taskId;
	private String name;
	private String description;
	private Boolean enabled;
	/**
	 * A json formatted string which contains info about tests and suite
	 * parameters
	 */
	private String suiteData;

	/**
	 * This class is used for serialization and deserialization in "bytes" field
	 * of {@link TrmSuite} class and represents the suite of the task
	 * 
	 * @author Ivan Shubin
	 * 
	 */
	public class Suite {
		public Suite() {

		}

		private List<Test> tests;
		private Map<String, String> parameters;

		public List<Test> getTests() {
			return tests;
		}

		public void setTests(List<Test> tests) {
			this.tests = tests;
		}

		public Map<String, String> getParameters() {
			return parameters;
		}

		public void setParameters(Map<String, String> parameters) {
			this.parameters = parameters;
		}
	}

	public class Test {
		private Long customId;
		private Long testId;

		public Long getCustomId() {
			return customId;
		}

		public void setCustomId(Long customId) {
			this.customId = customId;
		}

		public Long getTestId() {
			return testId;
		}

		public void setTestId(Long testId) {
			this.testId = testId;
		}
	}

	public class TestParameterReference {
		/**
		 * The id of the prerequisite test
		 */
		private Long testId;
		/**
		 * The name of the prerequisite test parameter
		 */
		private String parameterName;
		/**
		 * The name of the parameter of the current test.
		 */
		private String dependentParameterName;

		public Long getTestId() {
			return testId;
		}

		public void setTestId(Long testId) {
			this.testId = testId;
		}

		public String getParameterName() {
			return parameterName;
		}

		public void setParameterName(String parameterName) {
			this.parameterName = parameterName;
		}

		public String getDependentParameterName() {
			return dependentParameterName;
		}

		public void setDependentParameterName(String dependentParameterName) {
			this.dependentParameterName = dependentParameterName;
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setSuiteData(String suiteData) {
		this.suiteData = suiteData;
	}

	public String getSuiteData() {
		return suiteData;
	}

	public static net.mindengine.oculus.experior.suite.Suite convertSuiteFromJSON(String str, TestDAO testDAO) throws Exception {
		net.mindengine.oculus.experior.suite.Suite suite = new net.mindengine.oculus.experior.suite.Suite();
		JSONParser parser = new JSONParser(new StringReader(str));

		Map<Long, net.mindengine.oculus.frontend.domain.test.TestParameter> cashedParameters = new HashMap<Long, net.mindengine.oculus.frontend.domain.test.TestParameter>();
		
		JSONArray tests = (JSONArray) parser.nextValue();
		for (JSONValue value : tests.getValue()) {
			JSONObject testObject = (JSONObject) value;
			suite.addTest(readTestFromJsonValue(testObject, testDAO, cashedParameters));
		}
		return suite;
	}
	
	private static TestDefinition readTestFromJsonValue(JSONObject test, TestDAO testDAO, Map<Long, net.mindengine.oculus.frontend.domain.test.TestParameter> cashedParameters) throws Exception {
        TestDefinition td = new TestDefinition();
        td.setCustomId(JSONUtils.readString(test.get("customId")));
        if(test.containsKey("id")) {
            td.setTestId(JSONUtils.readInteger(test.get("id")));
        }
        if ( test.containsKey("name")) {
            td.setName(JSONUtils.readString(test.get("name")));
        }
        if(test.containsKey("testRunDescription")){
            td.setDescription(JSONUtils.readString(test.get("testRunDescription")));
        }
        if(test.containsKey("tests")) {
            List<TestDefinition> injectedTests = new LinkedList<TestDefinition>();
            JSONArray childTests = (JSONArray)test.get("tests");
            for (JSONValue value : childTests.getValue()) {
                JSONObject childTestObject = (JSONObject) value;
                injectedTests.add(readTestFromJsonValue(childTestObject, testDAO, cashedParameters));
            }
            td.setInjectedTests(injectedTests);
        }
        
        // Generating parameters injections
        JSONArray inputParameters = (JSONArray) test.get("inputParameters");
        if(inputParameters != null) {
            for (JSONValue jsParameter : inputParameters.getValue()) {
                JSONObject parameter = (JSONObject) jsParameter;
                JSONValue jsDepends = parameter.get("depends");
                if (jsDepends == null || jsDepends.isNull()) {
                    // Creating parameter value injection
                    if (td.getParameters() == null) {
                        td.setParameters(new HashMap<String, TestParameter>());
                    }
                    TestParameter testParameter = new TestParameter();
                    testParameter.setName(JSONUtils.readString(parameter.get("name")));
                    testParameter.setValue(JSONUtils.readString(parameter.get("value")));
                    td.getParameters().put(testParameter.getName(), testParameter);
                }
                else {
                    // Creating parameter dependency
                    JSONObject depends = (JSONObject) jsDepends;
                    if (td.getDependencies() == null){
                        td.setParameterDependencies(new ArrayList<TestDependency>());
                    }
                    TestDependency testDependency = new TestDependency();
                    testDependency.setRefTestId(JSONUtils.readString(depends.get("testCustomId")));
                    
                    Long refParameterId = JSONUtils.readInteger(depends.get("parameterId"));
                    net.mindengine.oculus.frontend.domain.test.TestParameter dbTestParameter = obtainTestParameterInDb(refParameterId, testDAO, cashedParameters);
                    testDependency.setRefParameterName(dbTestParameter.getName());
                    testDependency.setDependentParameterName(JSONUtils.readString(parameter.get("name")));
                    td.getParameterDependencies().add(testDependency);
                }
            }
        }
        return td;
	}
	
	
	private static net.mindengine.oculus.frontend.domain.test.TestParameter obtainTestParameterInDb(Long refParameterId, TestDAO testDAO, 
	        Map<Long, net.mindengine.oculus.frontend.domain.test.TestParameter> cashedParameters) throws Exception {
        
	    if ( cashedParameters.containsKey(refParameterId)) { 
	        return cashedParameters.get(refParameterId);
	    }
	    else {
            net.mindengine.oculus.frontend.domain.test.TestParameter parameter = testDAO.getParameter(refParameterId);
            cashedParameters.put(refParameterId, parameter);
            return parameter;
	    }
    }

    private static TrmSuiteStub convertTestFromJSONToStub(JSONObject test) throws TokenStreamException, RecognitionException, SecurityException, ClassNotFoundException, NoSuchMethodException, TestIsNotDefinedException {
	    TrmSuiteStub stub = new TrmSuiteStub();
	    JSONValue testIdValue = test.get("id");
	    if(testIdValue!=null) {
	        stub.setTestId(JSONUtils.readInteger(testIdValue));
	    }
	    JSONValue testsValue = test.get("tests");
	    if(testsValue!=null) {
	        stub.setTests(new LinkedList<TrmSuiteStub>());
	        JSONArray testsArr = (JSONArray) testsValue;
	        for(JSONValue testValue : testsArr.getValue()){
	            stub.getTests().add(convertTestFromJSONToStub((JSONObject)testValue));
	        }
	    }
	    return stub;
	}
	public static List<TrmSuiteStub> convertSuiteFromJSONToStub(String str) throws TokenStreamException, RecognitionException, SecurityException, ClassNotFoundException, NoSuchMethodException, TestIsNotDefinedException {
	    List<TrmSuiteStub> testsList = new LinkedList<TrmSuiteStub>();

        JSONParser parser = new JSONParser(new StringReader(str));

        JSONArray tests = (JSONArray) parser.nextValue();
        for (JSONValue value : tests.getValue()) {
            JSONObject test = (JSONObject) value;
            
            testsList.add(convertTestFromJSONToStub(test));
            
        }
        return testsList;
    }

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getEnabled() {
		return enabled;
	}
}
