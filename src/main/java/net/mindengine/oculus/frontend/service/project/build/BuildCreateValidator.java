package net.mindengine.oculus.frontend.service.project.build;

import net.mindengine.oculus.frontend.domain.project.build.Build;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class BuildCreateValidator implements Validator {
	private BuildDAO buildDAO;

	@SuppressWarnings("unchecked")
	@Override
	public boolean supports(Class clazz) {
		if (clazz == Build.class)
			return true;
		return false;
	}

	@Override
	public void validate(Object object, Errors errors) {
		Build build = (Build) object;
		if (build.getName().isEmpty()) {
			errors.rejectValue("name", "Name should not be empty");
		}
		else {
			try {
				if (buildDAO.getBuildByNameAndProject(build.getName(), build.getProjectId()) != null) {
					errors.rejectValue("name", "Build with such name already exists");
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setBuildDAO(BuildDAO buildDAO) {
		this.buildDAO = buildDAO;
	}

	public BuildDAO getBuildDAO() {
		return buildDAO;
	}

}
