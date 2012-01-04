package net.mindengine.oculus.frontend.service.customization;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.mindengine.oculus.frontend.db.jdbc.MySimpleJdbcDaoSupport;
import net.mindengine.oculus.frontend.domain.customization.Customization;
import net.mindengine.oculus.frontend.domain.customization.CustomizationPossibleValue;
import net.mindengine.oculus.frontend.domain.customization.FetchedCustomizationParameter;
import net.mindengine.oculus.frontend.domain.customization.UnitCustomizationValue;

public class JdbcCustomizationDAO extends MySimpleJdbcDaoSupport implements CustomizationDAO {
	Log logger = LogFactory.getLog(getClass());

	@Override
	public Long addCustomization(Customization customization) throws Exception {
		PreparedStatement ps = getConnection().prepareStatement("insert into customizations " + "(unit, " + "project_id, " + "name, " + "description, " + "type, " + "subtype, " + "group_name) values (?,?,?,?,?,?,?)");

		ps.setString(1, customization.getUnit());
		ps.setLong(2, customization.getProjectId());
		ps.setString(3, customization.getName());
		ps.setString(4, customization.getDescription());
		ps.setString(5, customization.getType());
		ps.setString(6, customization.getSubtype());
		ps.setString(7, customization.getGroupName());
		logger.info(ps);
		ps.execute();

		ResultSet rs = ps.getGeneratedKeys();
		if (rs.next()) {
			return rs.getLong(1);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Customization> getCustomizations(Long projectId, String unit) throws Exception {
		List<Customization> list = (List<Customization>) query("select * from customizations where project_id = :projectId and unit = :unit order by group_name, id", Customization.class, "projectId", projectId, "unit", unit);
		return list;
	}

	@Override
	public void deleteCustomization(Long id) throws Exception {
		update("delete from customizations where id = :id", "id", id);

		update("delete from customization_possible_values where customization_id = :id", "id", id);

		update("delete from unit_customization_values where customization_id = :id", "id", id);

	}

	@SuppressWarnings("unchecked")
	@Override
	public Customization getCustomization(Long id) throws Exception {
		List<Customization> list = (List<Customization>) query("select * from customizations where id = :id", Customization.class, "id", id);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public void updateCustomization(Customization customization) throws Exception {
		update("update customizations set name = :name, description = :description, type = :type, subtype = :subtype, group_name = :groupName where id = :id", "name", customization.getName(), "description", customization.getDescription(), "type", customization.getType(), "subtype", customization
				.getSubtype(), "groupName", customization.getGroupName(), "id", customization.getId());
	}

	@Override
	public long saveUnitCustomizationValue(UnitCustomizationValue unitCustomizationValue) throws Exception {
		UnitCustomizationValue value = null;

		if (unitCustomizationValue.getId() == null || unitCustomizationValue.getId() < 1) {
			value = getUnitCustomizationValue(unitCustomizationValue.getCustomizationId(), unitCustomizationValue.getUnitId());
		}
		else
			value = unitCustomizationValue;

		if (value == null) {
			PreparedStatement ps = getConnection().prepareStatement("insert into unit_customization_values (unit_id, customization_id, value) values (?,?,?)");
			ps.setLong(1, unitCustomizationValue.getUnitId());
			ps.setLong(2, unitCustomizationValue.getCustomizationId());
			ps.setString(3, unitCustomizationValue.getValue());

			logger.info(ps);
			ps.execute();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				return rs.getLong(1);
			}
		}
		else {
			update("update unit_customization_values set value = :value where id = :id", "value", unitCustomizationValue.getValue(), "id", value.getId());

			return value.getId();
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public UnitCustomizationValue getUnitCustomizationValue(Long customizationId, Long unitId) throws Exception {
		List<UnitCustomizationValue> list = (List<UnitCustomizationValue>) query("select * from unit_customization_values where customization_id = :customizationId and unit_id = :unitId", UnitCustomizationValue.class, "customizationId", customizationId, "unitId", unitId);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, List<FetchedCustomizationParameter>> getGroupedCustomizationValues(Long projectId, String unit, Long unitId) throws Exception {
		List<FetchedCustomizationParameter> list = (List<FetchedCustomizationParameter>) query("select ucv.value, c.* from customizations c " + "left join unit_customization_values ucv on ucv.customization_id = c.id and ucv.unit_id = :unitId "
				+ "where c.project_id = :projectId and c.unit = :unit order by c.group_name, c.id", FetchedCustomizationParameter.class, "projectId", projectId, "unit", unit, "unitId", unitId);

		Map<String, List<FetchedCustomizationParameter>> map = new HashMap<String, List<FetchedCustomizationParameter>>();

		String group = null;
		for (FetchedCustomizationParameter fcp : list) {
			if (!fcp.getGroupName().equals(group)) {
				group = fcp.getGroupName();
				map.put(group, new LinkedList<FetchedCustomizationParameter>());
			}
			map.get(group).add(fcp);
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CustomizationPossibleValue> getCustomizationPossibleValues(Long customizationId) throws Exception {
		List<CustomizationPossibleValue> list = (List<CustomizationPossibleValue>) query("select * from customization_possible_values where customization_id = :customizationId order by possible_value asc", CustomizationPossibleValue.class, "customizationId", customizationId);
		return list;
	}

	@Override
	public void saveCustomizationPossibleValues(Long customizationId, List<String> possibleValues) throws Exception {
		if (possibleValues == null || possibleValues.isEmpty()) {
			update("delete from customization_possible_values where customization_id = :customizationId", "customizationId", customizationId);
		}
		else {
			List<CustomizationPossibleValue> dbList = getCustomizationPossibleValues(customizationId);
			/*
			 * Checking existence of every value in DB
			 */
			StringBuffer buff = new StringBuffer();
			boolean bComma = false;
			for (String pv : possibleValues) {
				if (bComma)
					buff.append(",");
				bComma = true;
				buff.append("'");
				buff.append(StringEscapeUtils.escapeSql(pv));
				buff.append("'");

				if (!containsPossibleValue(dbList, pv)) {
					update("insert into customization_possible_values (customization_id, possible_value) values (:customizationId, :possibleValue)", "customizationId", customizationId, "possibleValue", pv);
				}
			}
			/*
			 * Removing the data which wasn't specified but exists in DB
			 */
			update("delete from customization_possible_values where customization_id = :customizationId and possible_value not in (" + buff.toString() + ")", "customizationId", customizationId);
		}
	}

	private boolean containsPossibleValue(List<CustomizationPossibleValue> list, String possibleValue) {
		for (CustomizationPossibleValue cpv : list) {
			if (cpv.getPossibleValue().equals(possibleValue)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void removeAllUnitCustomizationValues(Long unitId, List<Customization> customizations) throws Exception {
		if (customizations != null && customizations.size() > 0) {
			StringBuffer buff = new StringBuffer();
			boolean bComma = false;

			for (Customization customization : customizations) {
				if (bComma)
					buff.append(",");
				bComma = true;
				buff.append(customization.getId());
			}

			update("delete from unit_customization_values where unit_id = :unitId and customization_id in (" + buff.toString() + ")", "unitId", unitId);
		}
	}
}
