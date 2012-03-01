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
			errors.reject(null, "Name or link should be provided");
		}
	}
}
