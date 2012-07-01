package net.mindengine.oculus.frontend.web.controllers.api.trm;

import net.mindengine.oculus.frontend.api.GET;
import net.mindengine.oculus.frontend.api.Path;
import net.mindengine.oculus.frontend.service.trm.TrmDAO;
import net.mindengine.oculus.frontend.web.controllers.api.ApiController;
import net.mindengine.oculus.grid.domain.agent.AgentStatus;


public class ApiGridStatusTaskController extends ApiController {    
    private TrmDAO trmDAO;
    
    
    @GET @Path("/grid/status/agents") 
    public AgentStatus[] showAgents() throws Exception {
        return getConfig().lookupGridServer().getAgents();
    }
    
    
    public TrmDAO getTrmDAO() {
        return trmDAO;
    }
    public void setTrmDAO(TrmDAO trmDAO) {
        this.trmDAO = trmDAO;
    }

}
