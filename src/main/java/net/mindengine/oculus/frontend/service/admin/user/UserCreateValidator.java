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
package net.mindengine.oculus.frontend.service.admin.user;

import net.mindengine.oculus.frontend.domain.user.User;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UserCreateValidator implements Validator {

	@SuppressWarnings("rawtypes")
    @Override
	public boolean supports(Class clazz) {
		return User.class.equals(clazz);
	}

	@Override
	public void validate(Object object, Errors errors) {
		User user = (User) object;
		if (user != null) {
			if (user.getName() == null || "".equals(user.getName()))
				errors.reject(null, "Name shouldn't be empty");

			if (user.getLogin() == null || "".equals(user.getLogin()))
				errors.reject(null, "Login shouldn't be empty");

			if (user.getLogin() != null) {
				if (!StringUtils.containsOnly(user.getLogin(), "qwertyuiopasdfghjklzxcvbnm1234567890")) {
					errors.reject(null, "Login should contain only lowercase symbols");
				}
			}

			if (user.getEmail() == null || "".equals(user.getEmail()))
				errors.reject(null, "Email shouldn't be empty");
		}
		else
			errors.reject(null, "Input users data");

	}

}
