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
			errors.reject("name", "The name shouldn't be empty");
		}

		if (tsg.getTaskId() == null || tsg.getTaskId() < 1) {
			errors.reject("taskId", "Incorrect task");
		}

	}

}
