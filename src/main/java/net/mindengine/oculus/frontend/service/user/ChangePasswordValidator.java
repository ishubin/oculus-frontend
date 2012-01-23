package net.mindengine.oculus.frontend.service.user;

import net.mindengine.oculus.frontend.domain.user.ChangePasswordData;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ChangePasswordValidator implements Validator {

	@SuppressWarnings("unchecked")
	@Override
	public boolean supports(Class clazz) {
		if (clazz == ChangePasswordData.class)
			return true;
		return false;
	}

	@Override
	public void validate(Object object, Errors errors) {

		ChangePasswordData data = (ChangePasswordData) object;
		if (data.getNewPassword().length() < 4) {
			errors.reject(null, "Password should contain more than 3 characters");
		}
		if (!data.getNewPassword().equals(data.getNewPasswordConfirmation())) {
			errors.reject(null, "Please confirm password");
		}
	}

}
