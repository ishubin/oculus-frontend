package net.mindengine.oculus.frontend.web.controllers.trm.agent;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.config.Config;
import net.mindengine.oculus.frontend.domain.trm.AgentTagRule;
import net.mindengine.oculus.frontend.domain.trm.AgentTagValueWrapper;
import net.mindengine.oculus.frontend.domain.trm.AgentTagWrapper;
import net.mindengine.oculus.frontend.service.trm.AgentTagRulesContainer;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;
import net.mindengine.oculus.grid.domain.agent.AgentStatus;
import net.mindengine.oculus.grid.domain.agent.AgentTag;

public class AgentsDisplayController extends SecureSimpleViewController {
	private Config config;
	private AgentTagRulesContainer agentTagRulesContainer;

	@Override
	public Map<String, Object> handleController(HttpServletRequest request) throws Exception {
		AgentStatus[] agents = config.getGridServer().getAgents();
		wrapAgentTags(agents);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("agents", agents);
		
		return map;
	}

	private void wrapAgentTags(AgentStatus[] agents) {
	    if(agents!=null) {
            for(AgentStatus agent : agents) {
                AgentTag[] tags = agent.getAgentInformation().getTags();
                if(tags!=null) {
                    AgentTagWrapper[] wrappedTags = new AgentTagWrapper[tags.length];
                    for(int i=0; i<tags.length; i++) {
                        wrappedTags[i] = new AgentTagWrapper();
                        wrappedTags[i].setName(tags[i].getName());
                        wrappedTags[i].setType(tags[i].getType());
                        if(tags[i].getValue()!=null) {
                            wrappedTags[i].setWrappedValue(wrapTagValue(tags[i].getName(), tags[i].getValue()));
                        }
                        if(tags[i].getValues()!=null) {
                            List<AgentTagValueWrapper> wrappedValues = new LinkedList<AgentTagValueWrapper>();
                            for(String value : tags[i].getValues()) {
                                wrappedValues.add(wrapTagValue(tags[i].getName(), value));
                            }
                            wrappedTags[i].setWrappedValues(wrappedValues);
                        }
                    }
                    agent.getAgentInformation().setTags(wrappedTags);
                }
            }
        }
    }

    private AgentTagValueWrapper wrapTagValue(String name, String value) {
        AgentTagValueWrapper wrappedValue = new AgentTagValueWrapper();
        wrappedValue.setValue(value);
        
        //Fetching image for this tag value
        List<AgentTagRule> rules = agentTagRulesContainer.getRules();
        if(rules!=null) {
            String path = name + "/" + value;
            for(AgentTagRule rule: rules) {
                Matcher m = rule.getPattern().matcher(path);
                if(m.matches()) {
                    wrappedValue.setIconImage(rule.getImageUrl());
                    break;
                }
            }
        }
        return wrappedValue;
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
