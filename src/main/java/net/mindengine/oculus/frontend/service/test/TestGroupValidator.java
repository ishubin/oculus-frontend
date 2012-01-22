package net.mindengine.oculus.frontend.service.test;

import net.mindengine.oculus.frontend.domain.test.TestGroup;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class TestGroupValidator implements Validator {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean supports(Class clazz) {
		if (clazz == TestGroup.class) {
			return true;
		}
		return false;
	}

	@Override
	public void validate(Object obj, Errors errors) {

		TestGroup group = (TestGroup) obj;
		if (group.getName() == null || group.getName().isEmpty()) {
			errors.reject(null, "Group name cannot be empty");
		}
	}

}
