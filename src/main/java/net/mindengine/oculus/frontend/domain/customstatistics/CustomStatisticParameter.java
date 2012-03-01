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
package net.mindengine.oculus.frontend.domain.customstatistics;

import java.util.Collection;

public class CustomStatisticParameter {

	private Long id;
	private Long customStatisticId;
	private String name;
	private String description;
	private String shortName;
	
	private Collection<String> possibleValues;
	
	public Long getId() {
    	return id;
    }
	public void setId(Long id) {
    	this.id = id;
    }
	public Long getCustomStatisticId() {
    	return customStatisticId;
    }
	public void setCustomStatisticId(Long customStatisticId) {
    	this.customStatisticId = customStatisticId;
    }
	public String getName() {
    	return name;
    }
	public void setName(String name) {
    	this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
    public String getShortName() {
        return shortName;
    }
    public void setPossibleValues(Collection<String> possibleValues) {
        this.possibleValues = possibleValues;
    }
    public Collection<String> getPossibleValues() {
        return possibleValues;
    }
}
