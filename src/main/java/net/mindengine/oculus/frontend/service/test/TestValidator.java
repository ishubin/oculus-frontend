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
			errors.reject("name", "Test name cannot be empty");
		}
	}

}
