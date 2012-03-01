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
* along with Oculus Experior.  If not, see <http://www.gnu.org/licenses/>.
******************************************************************************/
package net.mindengine.oculus.frontend.service.trm.task;

import net.mindengine.oculus.frontend.domain.trm.TrmTask;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class CreateTaskValidator implements Validator {

	@SuppressWarnings("unchecked")
	@Override
	public boolean supports(Class arg0) {
		if (TrmTask.class.equals(arg0))
			return true;
		return false;
	}

	@Override
	public void validate(Object obj, Errors err) {
		TrmTask task = (TrmTask) obj;
		if (task.getName() == null || task.getName().isEmpty()) {
			err.reject(null, "Name shouldn't be empty");
		}
		else if(task.getProjectId()==null || task.getProjectId()==0){
		    err.reject(null, "Project is not specified");
		}
	}

}
