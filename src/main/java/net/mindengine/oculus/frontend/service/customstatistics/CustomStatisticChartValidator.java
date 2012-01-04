package net.mindengine.oculus.frontend.service.customstatistics;

import net.mindengine.oculus.frontend.domain.customstatistics.CustomStatisticChart;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class CustomStatisticChartValidator implements Validator{

    @Override
    public boolean supports(Class arg0) {
        return true;
    }

    @Override
    public void validate(Object obj, Errors errors) {
        CustomStatisticChart chart = (CustomStatisticChart) obj;
        
        if(chart.getName()==null || chart.getName().isEmpty()){
            errors.reject("name", null, "Name cannot be empty");
        }
        else if(chart.getType()==null || chart.getType().isEmpty()){
            errors.reject("type", null, "Type is not specified");
        }
    }

}
