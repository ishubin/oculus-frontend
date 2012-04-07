package net.mindengine.oculus.frontend.web.controllers.test;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.domain.AjaxModel;
import net.mindengine.oculus.frontend.service.test.TestDAO;
import net.mindengine.oculus.frontend.web.controllers.SimpleAjaxController;

public class AjaxParameterFetchController extends SimpleAjaxController {

    private TestDAO testDAO;
    
    @Override
    public AjaxModel handleController(HttpServletRequest request) throws Exception {
        AjaxModel model = new AjaxModel();
        
        String parameterIds = request.getParameter("parameterIds");
        if ( parameterIds != null ) {
            List<Long> ids = new LinkedList<Long>();
            String[] arr = parameterIds.split(",");
            for ( String id : arr ) {
                ids.add(Long.parseLong(id));
            }
            
            model.setObject(testDAO.getParametersByIds(ids));
            model.setResult("fetched");
        }
        return model;
    }

    public TestDAO getTestDAO() {
        return testDAO;
    }

    public void setTestDAO(TestDAO testDAO) {
        this.testDAO = testDAO;
    }
    
}
