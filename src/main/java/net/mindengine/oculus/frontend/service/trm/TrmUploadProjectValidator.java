package net.mindengine.oculus.frontend.service.trm;

import net.mindengine.oculus.frontend.domain.trm.TrmUploadProject;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class TrmUploadProjectValidator implements Validator {

	@SuppressWarnings("unchecked")
	@Override
	public boolean supports(Class clazz) {
		if (TrmUploadProject.class.equals(clazz))
			return true;
		return false;
	}

	@Override
	public void validate(Object obj, Errors errors) {
		TrmUploadProject command = (TrmUploadProject) obj;

		if (command.getVersion() == null || command.getVersion().isEmpty()) {
			errors.reject("trm.uploadproject.error.version.empty");
		}
	}

}
