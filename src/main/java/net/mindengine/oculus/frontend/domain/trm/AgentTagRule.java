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

import java.util.regex.Pattern;

public class AgentTagRule {
    
    private Pattern pattern;
    private String imageUrl;
    
    //If specified as null then image size will not be applied, meaning that image will be shown as is.
    private Dimension imageSize;
    
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public Pattern getPattern() {
        return pattern;
    }
    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }
    public Dimension getImageSize() {
        return imageSize;
    }
    public void setImageSize(Dimension imageSize) {
        this.imageSize = imageSize;
    }

}
