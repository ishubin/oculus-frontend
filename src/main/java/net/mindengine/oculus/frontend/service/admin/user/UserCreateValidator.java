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
