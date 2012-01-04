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
			err.rejectValue("name", "trm.createtask.error.name.empty");
		}
		else if(task.getProjectId()==null || task.getProjectId()==0){
		    err.rejectValue("projectId", null, "Project is not specified");
		}
	}

}
