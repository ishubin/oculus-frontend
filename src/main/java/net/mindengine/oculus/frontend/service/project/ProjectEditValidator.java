package net.mindengine.oculus.frontend.service.project;

import net.mindengine.oculus.frontend.domain.project.Project;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ProjectEditValidator implements Validator {
	private ProjectDAO projectDAO;

	@SuppressWarnings("unchecked")
	@Override
	public boolean supports(Class clazz) {
		return Project.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		Project project = (Project) obj;

		if (project == null) {
			errors.reject(null, "Input projects data");
		}
		else {

			if (project.getName() == null || "".equals(project.getName())) {
				errors.rejectValue("name", "project.create.name.empty");
			}
			if (project.getPath() == null || "".equals(project.getPath())) {
				errors.rejectValue("path", "project.create.path.empty");
			}
			else {
				String path = project.getPath();
				// [97-122][65-90][48-57][95][45]
				boolean bFinished = false;
				boolean bCheck = false;
				for (int i = 0; i < path.length() && !bFinished; i++) {
					char ch = path.charAt(i);
					int code = (int) ch;
					bCheck = ((code >= 97 && code <= 122) || (code >= 65 && code <= 90) || (code >= 48 && code <= 57) || code == 95 || code == 45);
					if (!bCheck)
						bFinished = true;
				}
				if (bFinished)
					errors.rejectValue("path", "project.create.path.special_symbols");

				// Verifying the project path if it is unique
				if (project.getId() == null || project.getId() == 0) {

					try {
						Project tp = projectDAO.getProjectByPath(project.getPath());
						if (tp != null) {
							errors.rejectValue("path", "project.create.path.already_exists");
						}
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void main(String[] args) {

		String path = "1234567890_-";
		// ath = "abcdefghijklmnopqrstuvwxyz";
		for (int i = 0; i < path.length(); i++) {
			char ch = path.charAt(i);
			int code = (int) ch;
			System.out.println("'" + ch + "' = " + code);
		}

	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}

}
