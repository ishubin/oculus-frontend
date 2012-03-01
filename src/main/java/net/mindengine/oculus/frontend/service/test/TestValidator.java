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
package net.mindengine.oculus.frontend.service.test;

import net.mindengine.oculus.frontend.domain.test.Test;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class TestValidator implements Validator {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean supports(Class clazz) {
		if (clazz == Test.class) {
			return true;
		}
		return false;
	}

	@Override
	public void validate(Object object, Errors errors) {
		Test test = (Test) object;
		if (test.getName() == null || test.getName().isEmpty()) {
			errors.reject(null, "Test name cannot be empty");
		}
	}

}
