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
