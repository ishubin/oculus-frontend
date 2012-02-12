package net.mindengine.oculus.frontend.domain.trm;

import java.util.List;

import net.mindengine.oculus.grid.domain.agent.AgentTag;

/**
 * Used by frontend in order to wrap tag values and replace them with images according to agent-tags-rules
 * @author ishubin
 *
 */
public class AgentTagWrapper extends AgentTag{
    
    private AgentTagValueWrapper wrappedValue;
    private List<AgentTagValueWrapper> wrappedValues;
    public AgentTagValueWrapper getWrappedValue() {
        return wrappedValue;
    }
    public void setWrappedValue(AgentTagValueWrapper wrappedValue) {
        this.wrappedValue = wrappedValue;
    }
    public List<AgentTagValueWrapper> getWrappedValues() {
        return wrappedValues;
    }
    public void setWrappedValues(List<AgentTagValueWrapper> wrappedValues) {
        this.wrappedValues = wrappedValues;
    }
    
}
