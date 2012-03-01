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
