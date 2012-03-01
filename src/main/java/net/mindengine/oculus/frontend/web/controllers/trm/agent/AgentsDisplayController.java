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
* along with Oculus Experior.  If not, see <http://www.gnu.org/licenses/>.
******************************************************************************/
package net.mindengine.oculus.frontend.web.controllers.trm.agent;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.config.Config;
import net.mindengine.oculus.frontend.service.trm.AgentTagRulesContainer;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;
import net.mindengine.oculus.grid.domain.agent.AgentStatus;

public class AgentsDisplayController extends SecureSimpleViewController {
	private Config config;
	private AgentTagRulesContainer agentTagRulesContainer;

	@Override
	public Map<String, Object> handleController(HttpServletRequest request) throws Exception {
		AgentStatus[] agents = config.getGridServer().getAgents();
		agentTagRulesContainer.wrapAgentTags(agents);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("agents", agents);
		
		return map;
	}

    public void setConfig(Config config) {
		this.config = config;
	}

	public Config getConfig() {
		return config;
	}

    public AgentTagRulesContainer getAgentTagRulesContainer() {
        return agentTagRulesContainer;
    }

    public void setAgentTagRulesContainer(AgentTagRulesContainer agentTagRulesContainer) {
        this.agentTagRulesContainer = agentTagRulesContainer;
    }
}
