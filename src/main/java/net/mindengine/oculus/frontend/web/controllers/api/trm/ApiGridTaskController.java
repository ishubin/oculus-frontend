package net.mindengine.oculus.frontend.web.controllers.api.trm;

import java.util.HashMap;
import java.util.Map;

import net.mindengine.oculus.frontend.service.trm.TrmDAO;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ApiGridTaskController {
    
    private TrmDAO trmDAO;
    
    @RequestMapping(value="/task", method=RequestMethod.GET)
    public ModelAndView showTask() {
        Map<String, Object> model = new HashMap<String, Object>();
        ModelAndView mav = new ModelAndView("jsonView", model);
        return mav;
    }

    public TrmDAO getTrmDAO() {
        return trmDAO;
    }

    public void setTrmDAO(TrmDAO trmDAO) {
        this.trmDAO = trmDAO;
    }

}
