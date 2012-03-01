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


/**
 * Used by frontend in order to wrap tag values and replace them with images according to agent-tags-rules
 * @author ishubin
 *
 */
public class AgentTagValueWrapper {

    private String value;
    private String iconImage;
    private Dimension iconSize;
    public String getIconImage() {
        return iconImage;
    }
    public void setIconImage(String iconImage) {
        this.iconImage = iconImage;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public Dimension getIconSize() {
        return iconSize;
    }
    public void setIconSize(Dimension iconSize) {
        this.iconSize = iconSize;
    }
}
