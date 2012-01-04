package net.mindengine.oculus.frontend.service.issue;

import net.mindengine.oculus.frontend.domain.issue.Issue;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class IssueValidator implements Validator {

	@SuppressWarnings("unchecked")
	@Override
	public boolean supports(Class clazz) {
		if (clazz.equals(Issue.class)) {
			return true;
		}
		return false;
	}

	@Override
	public void validate(Object object, Errors errors) {
		Issue issue = (Issue) object;
		if ((issue.getName() == null || issue.getName().isEmpty()) && (issue.getLink() == null || issue.getLink().isEmpty())) {
			errors.reject("issue.create.name.and.link.empty");
		}
	}
}
