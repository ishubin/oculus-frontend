package net.mindengine.oculus.frontend.domain.api.trm;

import java.util.Map;

public class RunTaskRequest {

    private Map<String, String> parameters;
    private String build;
    private String[] agents;

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public String[] getAgents() {
        return agents;
    }

    public void setAgents(String[] agents) {
        this.agents = agents;
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }
}
