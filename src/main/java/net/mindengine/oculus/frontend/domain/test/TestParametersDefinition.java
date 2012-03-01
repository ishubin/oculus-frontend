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
