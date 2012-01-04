package net.mindengine.oculus.frontend.service.trm.task;

import net.mindengine.oculus.frontend.domain.trm.TrmSuite;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class CreateSuiteValidator implements Validator {

	@SuppressWarnings("unchecked")
	@Override
	public boolean supports(Class clazz) {
		if (TrmSuite.class.equals(clazz))
			return true;
		return false;
	}

	@Override
	public void validate(Object obj, Errors errors) {
		TrmSuite suite = (TrmSuite) obj;
		if (suite.getName() == null || suite.getName().isEmpty()) {
			errors.rejectValue("name", "trm.createsuite.error.name.empty");
		}
	}

}
