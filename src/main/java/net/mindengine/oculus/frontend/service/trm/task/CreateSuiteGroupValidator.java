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
package net.mindengine.oculus.frontend.service.trm.task;

import net.mindengine.oculus.frontend.domain.trm.TrmSuiteGroup;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class CreateSuiteGroupValidator implements Validator {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean supports(Class clazz) {
		if (clazz.equals(TrmSuiteGroup.class))
			return true;
		return false;
	}

	@Override
	public void validate(Object object, Errors errors) {
		TrmSuiteGroup tsg = (TrmSuiteGroup) object;
		if (tsg.getName() == null || tsg.getName().isEmpty()) {
			errors.reject(null, "The name shouldn't be empty");
		}

		if (tsg.getTaskId() == null || tsg.getTaskId() < 1) {
			errors.reject(null, "Incorrect task");
		}

	}

}
