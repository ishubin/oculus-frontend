package net.mindengine.oculus.frontend.service.customstatistics;

import net.mindengine.oculus.frontend.domain.customstatistics.CustomStatistic;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class CustomStatisticValidator implements Validator{

	@SuppressWarnings("unchecked")
    @Override
	public boolean supports(Class clazz) {
		return true;
	}

	@Override
	public void validate(Object object, Errors errors) {
		if(object==null){
		    errors.reject(null,"There is no data for custom statistics");
		}
		else{
		    CustomStatistic statistic = (CustomStatistic)object;
		    if(statistic.getName()==null || statistic.getName().isEmpty()){
		        errors.reject("name", null,"Name shouldn't be empty");
		    }
		    if(statistic.getShortName()==null || statistic.getShortName().isEmpty()){
		    	errors.reject("shortName", null,"Shot name shouldn't be empty");
		    }
		}
	}
}
