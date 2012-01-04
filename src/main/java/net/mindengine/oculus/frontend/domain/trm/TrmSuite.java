package net.mindengine.oculus.frontend.domain.trm;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.mindengine.oculus.experior.exception.TestIsNotDefinedException;
import net.mindengine.oculus.experior.test.descriptors.TestDefinition;
import net.mindengine.oculus.experior.test.descriptors.TestDependency;
import net.mindengine.oculus.experior.test.descriptors.TestParameter;
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
	private Long groupId;
	private String groupName;
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

	public static net.mindengine.oculus.experior.suite.Suite convertSuiteFromJSON(String str) throws TokenStreamException, RecognitionException, SecurityException, ClassNotFoundException, NoSuchMethodException, TestIsNotDefinedException {
		net.mindengine.oculus.experior.suite.Suite suite = new net.mindengine.oculus.experior.suite.Suite();
		JSONParser parser = new JSONParser(new StringReader(str));

		JSONArray tests = (JSONArray) parser.nextValue();
		for (JSONValue value : tests.getValue()) {
			JSONObject test = (JSONObject) value;
			TestDefinition td = new TestDefinition();
			td.setCustomId(JSONUtils.readInteger(test.get("customId")));
			
			if(test.containsKey("testRunDescription")){
			    td.setDescription(JSONUtils.readString(test.get("testRunDescription")));
			}
			
			// Generating parameters injections
			JSONArray inputParameters = (JSONArray) test.get("inputParameters");
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
					if (td.getDependencies() == null)
					    
						td.setParameterDependencies(new ArrayList<TestDependency>());
					TestDependency testDependency = new TestDependency();
					testDependency.setPrerequisiteTestId(JSONUtils.readInteger(depends.get("testCustomId")));
					testDependency.setPrerequisiteParameterName(JSONUtils.readString(depends.get("parameterName")));
					testDependency.setDependentParameterName(JSONUtils.readString(parameter.get("name")));
					td.getParameterDependencies().add(testDependency);
				}
			}
			suite.addTest(td);
		}

		return suite;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupName() {
		return groupName;
	}
}
