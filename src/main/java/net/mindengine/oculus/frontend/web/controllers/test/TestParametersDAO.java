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
package net.mindengine.oculus.frontend.web.controllers.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.domain.test.Test;
import net.mindengine.oculus.frontend.domain.test.TestParameter;
import net.mindengine.oculus.frontend.service.test.TestDAO;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

public class TestParametersDAO {

    private TestDAO testDAO;
    
    public void saveTestParameters(HttpServletRequest request, Test test) throws Exception {
        /*
         * Saving input and output parameters
         */
        String jsonInputParameters = request.getParameter("__inputParametersJson");
        String jsonOutputParameters = request.getParameter("__outputParametersJson");
        
        TestParameter[] inputParameters = null;
        TestParameter[] outputParameters = null;
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        if (jsonInputParameters != null ) {
            inputParameters = mapper.readValue(jsonInputParameters, TestParameter[].class);
        }
        if (jsonOutputParameters != null ) {
            outputParameters = mapper.readValue(jsonOutputParameters, TestParameter[].class);
        }
        
        List<TestParameter> parameters = new ArrayList<TestParameter>();
        Collections.addAll(parameters, inputParameters);
        Collections.addAll(parameters, outputParameters);
        
        getTestDAO().saveTestParameters(test.getId(), parameters);
    }

    public TestDAO getTestDAO() {
        return testDAO;
    }

    public void setTestDAO(TestDAO testDAO) {
        this.testDAO = testDAO;
    }
}
