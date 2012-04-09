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
package net.mindengine.oculus.frontend.service.trm;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import net.mindengine.oculus.frontend.db.jdbc.MySimpleJdbcDaoSupport;
import net.mindengine.oculus.frontend.db.search.SearchColumn;
import net.mindengine.oculus.frontend.db.search.SqlSearchCondition;
import net.mindengine.oculus.frontend.domain.db.BrowseResult;
import net.mindengine.oculus.frontend.domain.test.TestSearchFilter;
import net.mindengine.oculus.frontend.domain.trm.TaskSearchFilter;
import net.mindengine.oculus.frontend.domain.trm.TrmProperty;
import net.mindengine.oculus.frontend.domain.trm.TrmSuite;
import net.mindengine.oculus.frontend.domain.trm.TrmSuiteGroup;
import net.mindengine.oculus.frontend.domain.trm.TrmTask;
import net.mindengine.oculus.frontend.domain.trm.TrmTaskDependency;
import net.mindengine.oculus.frontend.domain.user.User;

public class JdbcTrmDAO extends MySimpleJdbcDaoSupport implements TrmDAO {

	@Override
	public Long createProperty(TrmProperty property) throws Exception {
	    PreparedStatement ps = getConnection().prepareStatement("insert into trm_properties (name, description, type, subtype, project_id, value) values (?,?,?,?,?,?)");
		ps.setString(1, property.getName());
		ps.setString(2, property.getDescription());
		ps.setString(3, property.getType());
		ps.setString(4, property.getSubtype());
		ps.setLong(5, property.getProjectId());
		ps.setString(6, property.getValue());
		
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		if (rs.next() ) {
		    return rs.getLong(1);
		}
		
		return null;
	}

	@Override
	public void deleteProperty(Long id) throws Exception {
		update("delete from trm_properties where id = :id", "id", id);
		
		update("delete from trm_task_properties where property_id = :id", "id", id);
	}

	@Override
	public void saveTaskProperty(Long taskId, TrmProperty property) throws Exception {
	    Long id = queryForLong("select id from trm_task_properties where task_id=" + taskId + " and property_id = " + property.getId());
	    if(id!=null) {
	        update("update trm_task_properties set value = :value where id = :id", 
	                "value", property.getTaskValue(),
	                "id", id);
	    }
	    else {
	        update("insert into trm_task_properties (task_id, property_id, value) values (:taskId, :propertyId, :value)", 
	                "taskId", taskId, 
	                "propertyId", property.getId(),
	                "value", property.getTaskValue());
	    }
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TrmProperty> getProperties(Long projectId, String... types) throws Exception {
		String sql = "select * from trm_properties where project_id = " + projectId;
		Object args[] = null;
		if (types != null && types.length > 0) {
			args = new Object[types.length * 2];
			sql += " and ( ";
			int j = 0;
			for (int i = 0; i < types.length; i++) {
				if (i > 0)
					sql += " or";
				sql += " type = :type" + i;

				args[j] = "type" + i;
				args[j + 1] = types[i];
				j += 2;
			}
			sql += ")";
		}
		return (List<TrmProperty>) query(sql, TrmProperty.class, args);
	}

	
	@Override
	public List<TrmProperty> getTaskProperties(Long projectId, Long taskId, String type) throws Exception {
	    String sql  = "select tt.value as taskValue, tp.* from trm_properties tp left join trm_task_properties tt on (tt.property_id = tp.id and tt.task_id = :taskId) where tp.project_id = :projectId and tp.type = :type;";
	    return (List<TrmProperty>) query(sql, TrmProperty.class, 
	            "projectId", projectId, "taskId", taskId, "type", type);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public TrmProperty getProperty(Long propertyId) throws Exception {
		List<TrmProperty> list = (List<TrmProperty>) query("select * from trm_properties where id = :id", TrmProperty.class, "id", propertyId);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public void changeProperty(TrmProperty property) throws Exception {
		update("update trm_properties set name = :name, subtype = :subtype, value = :value where id = :id", "id", property.getId(), "name", property.getName(), "subtype", property.getSubtype(), "value", property.getValue());
	}

	@Override
	public long saveTask(TrmTask task) throws Exception {
		if (task.getDate() == null) {
			task.setDate(new Date());
		}

		if (task.getId() == null) {
			PreparedStatement ps = getConnection().prepareStatement("insert into trm_tasks (name, description, date, user_id, project_id, agents_filter, build) " + "values (?, ?, ?, ?, ?, ?, ?)");
			ps.setString(1, task.getName());
			ps.setString(2, task.getDescription());
			ps.setTimestamp(3, new Timestamp(task.getDate().getTime()));
			ps.setLong(4, task.getUserId());
			ps.setLong(5, task.getProjectId());
			ps.setString(6, task.getAgentsFilter());
			ps.setString(7, task.getBuild());
			
			ps.execute();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				return rs.getLong(1);
			}
		}
		else {
			update("update trm_tasks set name =:name, description = :description, shared =:shared, agents_filter=:filter, build=:build where id = :id", "id", task.getId(), "name", task.getName(), "description", task.getDescription(), "shared", task.getShared(), "filter", task.getAgentsFilter(), "build", task.getBuild());
		}
		return task.getId();
	}

	@SuppressWarnings("unchecked")
	@Override
	public TrmTask getTask(Long taskId) throws Exception {
		List<TrmTask> list = (List<TrmTask>) query("select u.login as userLogin, u.name as userName, t.* from trm_tasks t left join users u on u.id = t.user_id where t.id = :id", TrmTask.class, "id", taskId);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TrmTask> getUserTasks(Long userId, Long projectId) throws Exception {
	    
	    StringBuffer buffer = new StringBuffer("select id,name,description,user_id,date from trm_tasks where user_id = ").append(userId);
	    if(projectId!=null) {
	        buffer.append(" and project_id = ").append(projectId);
	    }
	    buffer.append(" order by date desc");
		List<TrmTask> list = (List<TrmTask>) query(buffer.toString(), TrmTask.class);
		return list;
	}

	@Override
	public void deleteTask(Long taskId, Long userId) throws Exception {
		update("delete from trm_tasks where id = :taskId and user_id = :userId", "taskId", taskId, "userId", userId);

		update("delete from trm_task_suite_groups where task_id = :taskId", "taskId", taskId);

		update("delete from trm_task_suites where task_id = :taskId", "taskId", taskId);
		
		update("delete from trm_task_properties where task_id = :taskId", "taskId", taskId);
	}

	@Override
	public void deleteSuite(Long suiteId) throws Exception {
		update("delete from trm_task_suites where id = :id", "id", suiteId);
	}

	@Override
	public long saveSuite(TrmSuite suite) throws Exception {
		if (suite.getId() == null) {
			PreparedStatement ps = getConnection().prepareStatement("insert into trm_task_suites (name, description, task_id, suiteData, group_id) values(?,?,?,?,?)");
			ps.setString(1, suite.getName());
			ps.setString(2, suite.getDescription());
			ps.setLong(3, suite.getTaskId());
			ps.setString(4, suite.getSuiteData());
			ps.setLong(5, suite.getGroupId());
			ps.execute();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				return rs.getLong(1);
			}
		}
		else {
			update("update trm_task_suites set name = :name, description = :description, suiteData = :suiteData, enabled =:enabled, group_id =:groupId where id = :id", "id", suite.getId(), "name", suite.getName(), "description", suite.getDescription(), "suiteData", suite
					.getSuiteData(), "enabled", suite.getEnabled(), "groupId", suite.getGroupId());
		}
		return suite.getId();
	}

	@SuppressWarnings("unchecked")
	@Override
	public TrmSuite getSuite(Long suiteId) throws Exception {
		List<TrmSuite> list = (List<TrmSuite>) query("select sg.name as groupName, s.* from trm_task_suites s left join trm_task_suite_groups sg on sg.id = s.group_id where s.id = :id", TrmSuite.class, "id", suiteId);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TrmSuite> getTaskSuites(Long taskId) throws Exception {
		List<TrmSuite> list = (List<TrmSuite>) query("select tts.* from trm_task_suites tts where task_id = :taskId", TrmSuite.class, "taskId", taskId);

		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TrmSuite> getTaskEnabledSuites(Long taskId) throws Exception {

		List<TrmSuite> list = (List<TrmSuite>) query("select tts.* from trm_task_suites tts where task_id = :taskId and enabled=1", TrmSuite.class, "taskId", taskId);

		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TrmSuite> getTaskEnabledSuites(Long taskId, Long groupId) throws Exception {
		List<TrmSuite> list = (List<TrmSuite>) query("select tts.* from trm_task_suites tts where task_id = :taskId and group_id = :groupId and enabled=1", TrmSuite.class, "taskId", taskId, "groupId", groupId);

		return list;
	}

	public SqlSearchCondition createSearchCondition(TaskSearchFilter filter) {
		SqlSearchCondition condition = new SqlSearchCondition();
		// Name
		{
			String name = filter.getName();
			if (name != null && !name.isEmpty()) {
				if (name.contains(",")) {
					condition.append(condition.createArrayCondition(name, "t.name"));
				}
				else {
					condition.append(condition.createSimpleCondition(name, true, "t.name"));
				}
			}
		}
		// User
		{
			String user = filter.getUser();
			if (user != null && !user.isEmpty()) {
				if (user.contains(",")) {
					condition.append(condition.createArrayCondition(user, "u.name", "u.login"));
				}
				else {
					condition.append(condition.createSimpleCondition(user, true, "u.name", "u.login"));
				}
			}
		}
		// Shared
		{
			if (filter.getOnlyShared() != null && filter.getOnlyShared()) {
				condition.append(condition.createSimpleCondition("1", false, "t.shared"));
			}
		}
		return condition;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BrowseResult<TrmTask> searchTasks(TaskSearchFilter filter) throws Exception {
		SqlSearchCondition condition = createSearchCondition(filter);

		int pageLimit = 10;
		if (filter.getPageLimit() < TestSearchFilter.PAGE_LIMITS.length) {
			pageLimit = TestSearchFilter.PAGE_LIMITS[filter.getPageLimit()];
		}

		String limit = " limit " + (filter.getPageOffset() - 1) * pageLimit + "," + pageLimit;

		String sqlSelect = "select u.login as userLogin, u.name as userName, t.* ";

		String sqlCount = "select count(*) ";

		String sqlFrom = "from trm_tasks t " + "left join users u on t.user_id = u.id ";
		BrowseResult<TrmTask> result = new BrowseResult<TrmTask>();

		// Preparing the order by statement
		String order = "";
		Integer orderBy = filter.getOrderByColumnId();
		if (orderBy != null && orderBy >= 0) {
			SearchColumn column = filter.getColumnById(orderBy);
			if (column != null) {

				String direction;
				if (filter.getOrderDirection() >= 0) {
					direction = "asc";
				}
				else
					direction = "desc";

				order = " order by " + column.getSqlColumn() + " " + direction + " ";

			}
		}

		String sql = sqlSelect + sqlFrom + condition + order + limit;

		result.setNumberOfResults(count(sqlCount + sqlFrom + condition));
		result.setResults((List<TrmTask>) query(sql, TrmTask.class));
		result.setPage(filter.getPageOffset());
		result.setLimit(pageLimit);
		return result;
	}

	@Override
	public Long createSuiteGroup(TrmSuiteGroup group) throws Exception {
		PreparedStatement ps = getConnection().prepareStatement("insert into trm_task_suite_groups (name, description, task_id, enabled) values (?,?,?,?)");

		ps.setString(1, group.getName());
		ps.setString(2, group.getDescription());
		ps.setLong(3, group.getTaskId());
		ps.setBoolean(4, group.getEnabled());

		logger.info(ps);
		ps.execute();
		ResultSet rs = ps.getGeneratedKeys();
		if (rs.next()) {
			return rs.getLong(1);
		}
		return null;
	}

	@Override
	public void updateSuiteGroup(TrmSuiteGroup group) throws Exception {
		update("update trm_task_suite_groups set name=:name, description=:description, enabled = :enabled where id=:id", "name", group.getName(), "description", group.getDescription(), "enabled", group.getEnabled(), "id", group.getId());

	}

	@SuppressWarnings("unchecked")
	@Override
	public TrmSuiteGroup getSuiteGroup(Long groupId) throws Exception {

		List<TrmSuiteGroup> list = (List<TrmSuiteGroup>) query("select * from trm_task_suite_groups where id = :id", TrmSuiteGroup.class, "id", groupId);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TrmSuiteGroup> getTaskSuiteGroups(Long taskId) throws Exception {
		List<TrmSuiteGroup> list = (List<TrmSuiteGroup>) query("select * from trm_task_suite_groups where task_id = :taskId", TrmSuiteGroup.class, "taskId", taskId);
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TrmSuiteGroup> getTaskEnabledSuiteGroups(Long taskId) throws Exception {
		List<TrmSuiteGroup> list = (List<TrmSuiteGroup>) query("select * from trm_task_suite_groups where task_id = :taskId and enabled = 1", TrmSuiteGroup.class, "taskId", taskId);
		return list;
	}

	@Override
	public void removeSuiteGroup(Long groupId) throws Exception {
		update("delete from trm_task_suite_groups where id =:groupId", "groupId", groupId);

		// removing all task suites which are in this group

		update("delete from trm_task_suites where group_id =:groupId", "groupId", groupId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TrmSuite> getTaskSuites(Long taskId, Long groupId) throws Exception {
		List<TrmSuite> list = (List<TrmSuite>) query("select tts.* from trm_task_suites tts where task_id = :taskId and group_id = :groupId", TrmSuite.class, "taskId", taskId, "groupId", groupId);

		return list;
	}

    @Override
    public Long createTaskDependency(Long taskId, Long refTaskId) throws Exception {
        PreparedStatement ps =  getConnection().prepareStatement("insert into trm_task_dependencies (task_id, ref_task_id) values (?,?)");
        ps.setLong(1, taskId);
        ps.setLong(2, refTaskId);
        
        logger.info(ps);
        ps.execute();
        
        ResultSet rs = ps.getGeneratedKeys();
        if(rs.next()) {
            return rs.getLong(1);
        }
        return null;
    }

    @Override
    public void deleteTaskDependency(Long id, Long taskId) throws Exception {
        update("delete from trm_task_dependencies where id = "+id+" and task_id = "+taskId);
    }

    @Override
    public Collection<TrmTaskDependency> getTaskDependencies(Long taskId) throws Exception {
        return query("select td.*, t.name as refTaskName, t.user_id as ownerId from trm_task_dependencies td left join trm_tasks t on t.id = td.ref_task_id where td.task_id = "+taskId, TrmTaskDependency.class);
    }

    @Override
    public Collection<TrmTask> getUserSharedTasks(Long userId, Long projectId) throws Exception {
        StringBuffer query = new StringBuffer("select * from trm_tasks where user_id = ").append(userId);
        if(projectId!=null) {
            query.append(" and project_id = ").append(projectId);
        }
        query.append(" and shared=1 order by name asc");
        return query(query.toString(), TrmTask.class);
    }

    @Override
    public Collection<User> getUsersWithSharedTasks(Long exceptUserId, Long projectId) throws Exception {
        StringBuffer query = new StringBuffer("select u.* from trm_tasks t left join users u on u.id = t.user_id where t.shared = 1 and t.user_id<>").append(exceptUserId);
        if(projectId!=null) {
            query.append(" and t.project_id = ");
            query.append(projectId);
        }
        query.append(" group by t.user_id order by u.name asc");
        return query(query.toString(), User.class);
    }

    @Override
    public Collection<TrmTask> getDependentTasks(Long taskId) throws Exception {
        return query("SELECT t.* FROM trm_task_dependencies td left join trm_tasks t on t.id = td.ref_task_id where td.task_id="+taskId, TrmTask.class);
    }

    @Override
    public void saveTrmPropertiesForProject(Long projectId, List<TrmProperty> properties, String propertyType) throws Exception {
        List<TrmProperty> oldProperties = this.getProperties(projectId, propertyType);
        
        List<Long> oldPropertyIds = new LinkedList<Long>();
        for ( TrmProperty property : oldProperties ) {
            oldPropertyIds.add(property.getId());
        }
        
        List<Long> newPropertyIds = new LinkedList<Long>();
        for (TrmProperty property : properties ) {
            property.setProjectId(projectId);
            property.setType(propertyType);
            
            if ( property.getId() !=null && oldPropertyIds.contains(property.getId()) ) {
                //Updating property
                changeProperty(property);
                newPropertyIds.add(property.getId());
            }
            else {
                //Adding new property
                newPropertyIds.add(createProperty(property));
            }
        }
        
        //Deleting unused properties
        for ( Long propertyId : oldPropertyIds ) {
            if ( !newPropertyIds.contains(propertyId) ) {
                deleteProperty(propertyId);
            }
        }
    }
}
