package net.mindengine.oculus.frontend.service.customization;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.db.search.SqlSearchCondition;
import net.mindengine.oculus.frontend.domain.customization.Customization;
import net.mindengine.oculus.frontend.domain.customization.CustomizationCriteria;
import net.mindengine.oculus.frontend.domain.customization.CustomizationPossibleValue;
import net.mindengine.oculus.frontend.domain.customization.FetchedCustomizationParameter;
import net.mindengine.oculus.frontend.domain.customization.UnitCustomizationValue;
import net.mindengine.oculus.frontend.service.user.UserDAO;

import org.apache.commons.lang.StringEscapeUtils;

public class CustomizationUtils {
	public static String collectCustomizationSearchCondition(String unitColumn, String unitType, String sqlFrom, Map<Long, CustomizationCriteria> customizations, SqlSearchCondition condition) {
		if (customizations != null) {
			int i = 0;
			for (Map.Entry<Long, CustomizationCriteria> entry : customizations.entrySet()) {
				i++;

				sqlFrom += " left join customizations c" + i + " on c" + i + ".project_id = pp.id and c" + i + ".unit = '" + unitType + "' and c" + i + ".id = " + entry.getKey() + " ";
				sqlFrom += " left join unit_customization_values ucv" + i + " on ucv" + i + ".customization_id = c" + i + ".id and ucv" + i + ".unit_id = " + unitColumn + " ";

				if (entry.getValue().getChecklist()) {
					/*
					 * This code is for check-list customization condition
					 */

					if (entry.getValue().getValues().size() > 0) {
						StringBuffer checklistExp = new StringBuffer("(");
						Iterator<String> it = entry.getValue().getValues().iterator();
						String expType;
						if (entry.getValue().getFetchConditionType().equals(CustomizationCriteria.FETCH_TYPE_AT_LEAST_ONE)) {
							expType = " or ";
						}
						else
							expType = " and ";
						boolean bSep = false;
						while (it.hasNext()) {
							if (bSep) {
								checklistExp.append(expType);
							}
							String value = it.next();
							checklistExp.append(" (ucv" + i + ".value like '%(" + value + ")%') ");
							bSep = true;
						}
						checklistExp.append(")");
						condition.append(checklistExp.toString());
					}
				}
				else if (entry.getValue().getValues().size() == 1) {
					/*
					 * this code is for simple customization
					 */
					Iterator<String> it = entry.getValue().getValues().iterator();
					String value = it.next();
					condition.append(condition.createSimpleCondition(value, true, "ucv" + i + ".value"));
				}
			}
		}
		return sqlFrom;
	}

	public static Map<Long, CustomizationCriteria> collectCustomizationSearchCriteriaParameters(HttpServletRequest request) {
		Map<Long, CustomizationCriteria> map = new HashMap<Long, CustomizationCriteria>();

		Enumeration<?> params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String param = (String) params.nextElement();
			if (param.startsWith("customization_")) {
				String strCustomization = param.substring(14);
				int idPV = strCustomization.indexOf("_pv_");
				int idFP = strCustomization.indexOf("_fetch_type");
				boolean checklist = false;
				Long customizationId = null;
				Integer fetchConditionType = null;
				String value = null;
				if (idPV > 0) {
					// The parameter is part of checklist
					checklist = true;
					customizationId = Long.parseLong(strCustomization.substring(0, idPV));
					if ("on".equals(request.getParameter(param))) {
						value = strCustomization.substring(idPV + 4);
					}
				}
				else if (idFP > 0) {
					// The parameter is a part of checklist fetch type
					// specification
					customizationId = Long.parseLong(strCustomization.substring(0, idFP));
					checklist = true;
					String strFP = request.getParameter(param);
					if ("1".equals(strFP)) {
						fetchConditionType = CustomizationCriteria.FETCH_TYPE_STRICT;
					}
					else
						fetchConditionType = CustomizationCriteria.FETCH_TYPE_AT_LEAST_ONE;
				}
				else {
					customizationId = Long.parseLong(strCustomization);
					
					value = StringEscapeUtils.escapeSql(request.getParameter(param));
				}
				if (!map.containsKey(customizationId)) {
					CustomizationCriteria criteria = new CustomizationCriteria();
					criteria.setValues(new LinkedList<String>());
					map.put(customizationId, criteria);
				}
				CustomizationCriteria criteria = map.get(customizationId);
				if (value != null) {
					criteria.getValues().add(value);
				}
				if (fetchConditionType != null) {
					criteria.setFetchConditionType(fetchConditionType);
				}
				criteria.setChecklist(checklist);
			}
		}
		return map;
	}

	public static List<Map<String, Object>> fetchCustomizationGroups(CustomizationDAO customizationDAO, UserDAO userDAO, Long rootProjectId, Long unitId, String unit) throws Exception {
		return fetchCustomizationGroups(customizationDAO, userDAO, rootProjectId, unitId, unit, null);
	}

	public static List<Map<String, Object>> fetchCustomizationGroups(CustomizationDAO customizationDAO, UserDAO userDAO, Long rootProjectId, String unit, Map<Long, CustomizationCriteria> customizationCriteriaMap) throws Exception {
		return fetchCustomizationGroups(customizationDAO, userDAO, rootProjectId, 0L, unit, customizationCriteriaMap);
	}

	private static List<Map<String, Object>> fetchCustomizationGroups(CustomizationDAO customizationDAO, UserDAO userDAO, Long rootProjectId, Long unitId, String unit, Map<Long, CustomizationCriteria> cmap) throws Exception {
		Map<String, List<FetchedCustomizationParameter>> fcpgs = customizationDAO.getGroupedCustomizationValues(rootProjectId, unit, unitId);
		List<Map<String, Object>> customizationGroups = new LinkedList<Map<String, Object>>();
		for (String groupName : fcpgs.keySet()) {
			Map<String, Object> customizationGroup = new HashMap<String, Object>();
			customizationGroup.put("name", groupName);

			/*
			 * Fetching possible values for customizations and users for
			 * assignee
			 */
			List<FetchedCustomizationParameter> fcps = fcpgs.get(groupName);
			List<Map<String, Object>> customizationMaps = new LinkedList<Map<String, Object>>();
			// customizationGroup.put("customizations", fcps.get(groupName));
			for (FetchedCustomizationParameter fcp : fcps) {
				/*
				 * Looking for the customization parameter value in the
				 * specified map
				 */
				if (cmap != null) {
					if (cmap.containsKey(fcp.getId())) {
						CustomizationCriteria cc = cmap.get(fcp.getId());
						if (!cc.getChecklist() && cc.getValues() != null) {
							Iterator<String> it = cc.getValues().iterator();
							if (it.hasNext()) {
								fcp.setValue(it.next());
							}
						}
					}
				}

				Map<String, Object> customizationMap = new HashMap<String, Object>();
				customizationMap.put("customization", fcp);

				if (cmap != null) {
					if (cmap.containsKey(fcp.getId())) {
						CustomizationCriteria cc = cmap.get(fcp.getId());
						if (cc.getFetchConditionType().equals(CustomizationCriteria.FETCH_TYPE_STRICT)) {
							customizationMap.put("fetchConditionType", "1");
						}
						else
							customizationMap.put("fetchConditionType", "2");
					}
				}

				if (fcp.getType().equals(Customization.TYPE_LIST) || fcp.getType().equals(Customization.TYPE_CHECKLIST)) {
					/*
					 * Fetching possible values for this customization and
					 * determining if there are possible values which were set
					 * within the customization
					 */
					List<CustomizationPossibleValue> possibleValues = customizationDAO.getCustomizationPossibleValues(fcp.getId());

					/*
					 * Filling the selected ids
					 */
					List<Long> selectedIds = null;
					if (cmap != null) {
						selectedIds = new LinkedList<Long>();

						if (cmap.containsKey(fcp.getId())) {
							CustomizationCriteria cc = cmap.get(fcp.getId());
							if (cc.getValues() != null) {
								Iterator<String> it = cc.getValues().iterator();
								while (it.hasNext()) {
									selectedIds.add(Long.parseLong(it.next()));
								}
							}
						}
					}
					else
						selectedIds = fcp.fetchPossibleValueIds();

					if (selectedIds.size() > 0) {
						for (CustomizationPossibleValue possibleValue : possibleValues) {
							if (selectedIds.contains(possibleValue.getId())) {
								possibleValue.setIsSet(true);
							}
						}
					}
					customizationMap.put("possibleValues", possibleValues);
				}
				else if (fcp.getType().equals(Customization.TYPE_ASSIGNEE)) {
					if (fcp.getValue() != null && !fcp.getValue().isEmpty() && userDAO != null) {
						try {
							Long userId = Long.parseLong(fcp.getValue());
							customizationMap.put("assignedUser", userDAO.getUserById(userId));
						}
						catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				customizationMaps.add(customizationMap);
			}
			customizationGroup.put("customizations", customizationMaps);
			if (groupName == null || groupName.isEmpty()) {
				customizationGroup.put("isMain", true);
			}
			else
				customizationGroup.put("isMain", false);
			customizationGroups.add(customizationGroup);
		}
		return customizationGroups;
	}

	public static void updateUnitCustomizationValues(Long rootProjectId, Long unitId, String unit, CustomizationDAO customizationDAO, HttpServletRequest request) throws Exception {
		List<Customization> customizations = customizationDAO.getCustomizations(rootProjectId, unit);

		for (Customization customization : customizations) {
			List<CustomizationPossibleValue> possibleValues = null;
			if (customization.getType().equals(Customization.TYPE_CHECKLIST)) {
				possibleValues = customizationDAO.getCustomizationPossibleValues(customization.getId());
			}
			UnitCustomizationValue ucv = CustomizationUtils.getUnitCustomizationValue(unitId, request, customization, possibleValues);
			if (ucv != null) {
				customizationDAO.saveUnitCustomizationValue(ucv);
			}
		}
	}

	/**
	 * Fetches the unit customization value from the post request
	 * 
	 * @param request
	 * @param customization
	 * @return
	 */
	public static UnitCustomizationValue getUnitCustomizationValue(Long unitId, HttpServletRequest request, Customization customization, List<CustomizationPossibleValue> possibleValues) {
		if (customization.getType().equals(Customization.TYPE_CHECKBOX)) {
			String value = request.getParameter("customization_" + customization.getId());
			if (value == null)
				value = "false";
			if (value.equals("on"))
				value = "true";
			return new UnitCustomizationValue(unitId, customization.getId(), value);
		}
		else if (customization.getType().equals(Customization.TYPE_CHECKLIST)) {
			StringBuffer value = new StringBuffer();
			/*
			 * The possible value ids are represented in the following format:
			 * (34234)(34235)(5345)(5345) Important that each id should be
			 * placed in the brackets
			 */
			for (CustomizationPossibleValue cpv : possibleValues) {
				if ("on".equals(request.getParameter("customization_" + customization.getId() + "_pv_" + cpv.getId()))) {
					value.append("(");
					value.append(cpv.getId());
					value.append(")");
				}
			}
			return new UnitCustomizationValue(unitId, customization.getId(), value.toString());
		}
		else {
			String value = request.getParameter("customization_" + customization.getId());
			if (value == null)
				value = "";
			return new UnitCustomizationValue(unitId, customization.getId(), value);
		}
	}

}
