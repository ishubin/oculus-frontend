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
package net.mindengine.oculus.frontend.web.controllers.trm.tasks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.config.Config;
import net.mindengine.oculus.frontend.domain.trm.TrmProperty;
import net.mindengine.oculus.frontend.domain.trm.TrmSuite;
import net.mindengine.oculus.frontend.domain.trm.TrmTask;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.exceptions.InvalidRequest;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;
import net.mindengine.oculus.frontend.service.exceptions.PermissionDeniedException;
import net.mindengine.oculus.frontend.service.exceptions.RedirectException;
import net.mindengine.oculus.frontend.service.trm.AgentTagRulesContainer;
import net.mindengine.oculus.frontend.service.trm.TrmDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;
import net.mindengine.oculus.grid.domain.agent.AgentStatus;
import net.mindengine.oculus.grid.service.ClientServerRemoteInterface;

/**
 * This controller is used only for editing basic task information and
 * adding/deleting task suites
 * 
 * @author Ivan Shubin
 * 
 */
public class EditTaskController extends SecureSimpleViewController {
    private TrmDAO trmDAO;
    private Config config;
    private AgentTagRulesContainer agentTagRulesContainer;

	private void saveTask(Long taskId, List<TrmSuite> suites, List<TrmProperty> taskProperties, HttpServletRequest request) throws Exception {
		TrmTask task = new TrmTask();
		task.setId(taskId);
		task.setAgentsFilter(request.getParameter("agentsFilter"));
		task.setBuild(request.getParameter("build"));
		
	    String name = request.getParameter("taskName");
		String description = request.getParameter("taskDescription");
		String shared = request.getParameter("shared");

		if (name == null || name.isEmpty() || description == null) {
			throw new InvalidRequest();
		}

		if (shared != null && shared.equals("on")) {
			task.setShared(true);
		}
		else
			task.setShared(false);

		task.setName(name);
		task.setDescription(description);
		trmDAO.saveTask(task);
	    
		if (suites != null) {
			for (TrmSuite suite : suites) {
				String checkbox = request.getParameter("enableSuite" + suite.getId());
				if (checkbox != null && checkbox.equals("on")) {
					suite.setEnabled(true);
				}
				else
					suite.setEnabled(false);
				trmDAO.saveSuite(suite);
			}
		}
		
		if(taskProperties!=null && taskProperties.size()>0) {
		    saveTaskProperties(taskId, taskProperties, request);
		}
	}

	private void saveTaskProperties(Long taskId, List<TrmProperty> taskProperties, HttpServletRequest request) throws Exception {
        for(TrmProperty property : taskProperties) {
            
            String value = request.getParameter("sp_" + property.getId());
            if(property.getSubtype().equals(TrmProperty.Controls._BOOLEAN)) {
                if(value!=null && value.equals("on")) {
                    value = "true";
                }
                else {
                    value = "false";
                }
            }
            
            if(value!=null) {
                property.setTaskValue(value);
                trmDAO.saveTaskProperty(taskId, property);
            }
        }
    }

    @Override
	public Map<String, Object> handleController(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Long taskId = Long.parseLong(request.getParameter("id"));

		String submit = request.getParameter("Submit");
		List<TrmSuite> suites = trmDAO.getTaskSuites(taskId);
		
		TrmTask task = trmDAO.getTask(taskId);
		map.put("title", "Grid Task - " + task.getName());
		List<TrmProperty> taskProperties = trmDAO.getTaskProperties(task.getProjectId(), taskId, TrmProperty._TYPE_SUITE_PARAMETER);
		
		if (submit != null) {
			if (submit.equals("Save")) {
				saveTask(taskId, suites, taskProperties, request);
			}
		}
		
		task.setParameters(taskProperties);
		
		try {
		    //In case there is no connection to Grid user should still be able to edit his tasks
		    ClientServerRemoteInterface server = config.lookupGridServer();
		    AgentStatus[] agents = server.getAgents();
            agentTagRulesContainer.wrapAgentTags(agents);
            map.put("agents", agents);
            map.put("agentTags", agentTagRulesContainer.fetchAllAgentWrappedTags(agents));
		}
		catch (Exception e) {
            
        }
		
		User user = getUser(request);
		if (user == null) {
			throw new NotAuthorizedException();
		}
		if (!user.getId().equals(task.getUserId())) {
			
			if(task.getShared()){
				throw new RedirectException("../grid/shared-task?id="+task.getId());
			}
			else throw new PermissionDeniedException("You are not authorized to edit tasks of other users");
		}

		map.put("taskDependencies", trmDAO.getTaskDependencies(taskId));
		map.put("task", task);
		map.put("suites", suites);
		return map;
	}

	public void setTrmDAO(TrmDAO trmDAO) {
		this.trmDAO = trmDAO;
	}

	public TrmDAO getTrmDAO() {
		return trmDAO;
	}

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public AgentTagRulesContainer getAgentTagRulesContainer() {
        return agentTagRulesContainer;
    }

    public void setAgentTagRulesContainer(AgentTagRulesContainer agentTagRulesContainer) {
        this.agentTagRulesContainer = agentTagRulesContainer;
    }
}
