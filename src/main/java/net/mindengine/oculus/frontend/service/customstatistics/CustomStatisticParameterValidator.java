package net.mindengine.oculus.frontend.service.customstatistics;

import net.mindengine.oculus.frontend.domain.customstatistics.CustomStatisticParameter;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class CustomStatisticParameterValidator implements Validator{

    @SuppressWarnings("unchecked")
    @Override
    public boolean supports(Class clazz) {
        return true;
    }

    @Override
    public void validate(Object obj, Errors errors) {
        
        if(obj == null){
            errors.reject(null, "There is no data");
        }
        else{
            CustomStatisticParameter parameter = (CustomStatisticParameter)obj;
            if(parameter.getName()==null || parameter.getName().isEmpty()){
                errors.reject("name",null, "Name shouldn't be empty");
            }
            if(parameter.getShortName()==null || parameter.getShortName().isEmpty()){
                errors.reject("shortName", null, "Short name shouldn't be empty");
            }
        }
    }

}
