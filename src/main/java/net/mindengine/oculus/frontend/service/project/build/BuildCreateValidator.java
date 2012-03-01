/*******************************************************************************
* 2012 Ivan Shubin http://mindengine.net
* 
* This file is part of MindEngine.net Oculus Frontend.
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with Oculus Frontend.  If not, see <http://www.gnu.org/licenses/>.
******************************************************************************/
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
