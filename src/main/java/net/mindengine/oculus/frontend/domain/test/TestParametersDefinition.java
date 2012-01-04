package net.mindengine.oculus.frontend.domain.test;

import java.util.List;

import net.mindengine.oculus.frontend.web.controllers.test.TestCustomizeController;

/**
 * Used only as a model in {@link TestCustomizeController}. Contains the input
 * parameters list and output parameters list
 * 
 * @author Ivan Shubin
 * 
 */
public class TestParametersDefinition {
	private List<TestParameter> inputParameters;
	private List<TestParameter> outputParameters;

	public List<TestParameter> getInputParameters() {
		return inputParameters;
	}

	public void setInputParameters(List<TestParameter> inputParameters) {
		this.inputParameters = inputParameters;
	}

	public List<TestParameter> getOutputParameters() {
		return outputParameters;
	}

	public void setOutputParameters(List<TestParameter> outputParameters) {
		this.outputParameters = outputParameters;
	}

	public int getOutputParametersCount() {
		if (outputParameters != null) {
			return outputParameters.size();
		}
		return 0;
	}

	public int getInputParametersCount() {
		if (inputParameters != null) {
			return inputParameters.size();
		}
		return 0;
	}
}
