package net.mindengine.oculus.frontend.service;

import net.mindengine.oculus.frontend.domain.user.LoginData;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class LoginValidator implements Validator {

	protected Log logger = LogFactory.getLog(getClass());

	@SuppressWarnings("unchecked")
	@Override
	public boolean supports(Class clazz) {
		return clazz.equals(LoginData.class);
	}

	@Override
	public void validate(Object object, Errors errors) {
		LoginData loginData = (LoginData) object;

		logger.info("Validating the logins form data");
		if (loginData.getLogin() == null || "".equals(loginData.getLogin())) {
			errors.rejectValue("login", "login.error.login.empty");
		}
	}

}
