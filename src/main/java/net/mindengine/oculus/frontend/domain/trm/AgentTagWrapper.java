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
