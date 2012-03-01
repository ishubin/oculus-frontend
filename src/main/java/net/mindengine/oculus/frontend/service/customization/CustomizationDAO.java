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
package net.mindengine.oculus.frontend.service.customization;

import java.util.List;
import java.util.Map;

import net.mindengine.oculus.frontend.domain.customization.Customization;
import net.mindengine.oculus.frontend.domain.customization.CustomizationPossibleValue;
import net.mindengine.oculus.frontend.domain.customization.FetchedCustomizationParameter;
import net.mindengine.oculus.frontend.domain.customization.UnitCustomizationValue;

public interface CustomizationDAO {
	public Long addCustomization(Customization customization) throws Exception;

	public List<Customization> getCustomizations(Long projectId, String unit) throws Exception;

	/**
	 * Fetches customization with joined values for some specific unit
	 * 
	 * @param projectId
	 * @param unit
	 * @return
	 * @throws Exception
	 */
	public Map<String, List<FetchedCustomizationParameter>> getGroupedCustomizationValues(Long projectId, String unit, Long unitId) throws Exception;

	public Customization getCustomization(Long id) throws Exception;

	public void deleteCustomization(Long id) throws Exception;

	public void updateCustomization(Customization customization) throws Exception;

	public List<CustomizationPossibleValue> getCustomizationPossibleValues(Long customizationId) throws Exception;

	public void saveCustomizationPossibleValues(Long customizationId, List<String> possibleValues) throws Exception;

	public long saveUnitCustomizationValue(UnitCustomizationValue unitCustomizationValue) throws Exception;

	public UnitCustomizationValue getUnitCustomizationValue(Long customizationId, Long unitId) throws Exception;

	public void removeAllUnitCustomizationValues(Long unitId, List<Customization> customizations) throws Exception;
}
