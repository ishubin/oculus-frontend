package net.mindengine.oculus.frontend.web.controllers.trm.agent;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.config.Config;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;
import net.mindengine.oculus.grid.domain.agent.AgentStatus;

public class AgentsDisplayController extends SecureSimpleViewController {
	private Config config;

	@Override
	public Map<String, Object> handleController(HttpServletRequest request) throws Exception {
		AgentStatus[] agents = config.getTRMServer().getAgents();
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

}
