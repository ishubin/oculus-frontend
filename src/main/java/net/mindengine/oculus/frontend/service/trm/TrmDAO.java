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

import java.util.Collection;
import java.util.List;

import net.mindengine.oculus.frontend.domain.db.BrowseResult;
import net.mindengine.oculus.frontend.domain.trm.TaskSearchFilter;
import net.mindengine.oculus.frontend.domain.trm.TrmProperty;
import net.mindengine.oculus.frontend.domain.trm.TrmSuite;
import net.mindengine.oculus.frontend.domain.trm.TrmSuiteGroup;
import net.mindengine.oculus.frontend.domain.trm.TrmTask;
import net.mindengine.oculus.frontend.domain.trm.TrmTaskDependency;
import net.mindengine.oculus.frontend.domain.user.User;

/**
 * Used for gaining access to Test Run Manager configuration in DB
 * 
 * @author Ivan Shubin
 * 
 */
public interface TrmDAO {
	public final static String PROPERTY_TYPE_SUITE_PARAMETER = "suite_parameter";

	/**
	 * Returns the property with specified id
	 * 
	 * @param propertyId
	 *            Id of the property saved in 'properties' table in DB
	 * @return Property with specified id
	 * @throws Exception
	 */
	public TrmProperty getProperty(Long propertyId) throws Exception;

	/**
	 * Returns a list of Test Run Manager properties which match the specified
	 * criteria
	 * 
	 * @param types
	 *            A list of property types e.g. "suite_parameter"
	 * @param projectId
	 *            Id of the project
	 * @return A list of properties for Test Run Manager which match the
	 *         specified criteria
	 * @throws Exception
	 */
	public List<TrmProperty> getProperties(Long projectId, String... types) throws Exception;

	/**
	 * Creates a Test Run Manager property in DB
	 * 
	 * @param property
	 * @throws Exception
	 */
	public void createProperty(TrmProperty property) throws Exception;

	/**
	 * Deletes the Test Run Manager property from DB
	 * 
	 * @param id
	 *            Id of the property in DB
	 * @throws Exception
	 */
	public void deleteProperty(Long id) throws Exception;

	/**
	 * Changes the property name, subtype, value in DB. To prevent error the
	 * property should contain existent id.
	 * 
	 * @param property
	 *            Property to be changed in DB with existent id.
	 * @throws Exception
	 */
	public void changeProperty(TrmProperty property) throws Exception;

	/**
	 * Saves the task in the "trm_tasks" DB table
	 * 
	 * @param task
	 *            Task for Test Run Manager
	 * @return Id of the just created task
	 * @throws Exception
	 */
	public long saveTask(TrmTask task) throws Exception;

	/**
	 * Returns all user tasks saved in DB.<br>
	 * This method will return only basic task information without data
	 * containing suites and tests.
	 * 
	 * @param userId
	 *            Id of the current user
	 * @param projectId 
	 * @return All user tasks saved in DB
	 * @throws Exception
	 */
	public List<TrmTask> getUserTasks(Long userId, Long projectId) throws Exception;

	/**
	 * Return the task with the specified id.
	 * 
	 * @param taskId
	 *            Id of the task
	 * @return Task with specified id
	 * @throws Exception
	 */
	public TrmTask getTask(Long taskId) throws Exception;

	/**
	 * Deletes the specified task of the specified user.
	 * 
	 * @param taskId
	 *            Id of the task
	 * @param userId
	 *            Id of the user. Used for verification that only the tasks of
	 *            logged user could be deleted.
	 * @throws Exception
	 */
	public void deleteTask(Long taskId, Long userId) throws Exception;

	/**
	 * Saves the suite in trm_task_suites DB table
	 * 
	 * @param suite
	 *            Suite of the {@link TrmTask}
	 * @return Id of just created suite
	 * @throws Exception
	 */
	public long saveSuite(TrmSuite suite) throws Exception;

	/**
	 * Deletes the task suite from trm_task_suites DB table
	 * 
	 * @param suiteId
	 *            Id of the suite for deletion
	 * @throws Exception
	 */
	public void deleteSuite(Long suiteId) throws Exception;

	/**
	 * Returns a list of suites that belong to a specified task
	 * 
	 * @param taskId
	 *            Id of the task
	 * @return List of suites that belong to a specified task
	 * @throws Exception
	 */
	public List<TrmSuite> getTaskSuites(Long taskId) throws Exception;

	/**
	 * Returns suite for specified task and group
	 * 
	 * @param taskId
	 *            Id of the task
	 * @param groupId
	 *            Id of the suite group
	 * @return
	 * @throws Exception
	 */
	public List<TrmSuite> getTaskSuites(Long taskId, Long groupId) throws Exception;

	/**
	 * Returns a list of suites that belong to a specified task and are enabled
	 * only
	 * 
	 * @param taskId
	 *            Id of the task
	 * @return List of suites that belong to a specified task
	 * @throws Exception
	 */
	public List<TrmSuite> getTaskEnabledSuites(Long taskId) throws Exception;

	/**
	 * Returns a list of suites that belong to a specified task and group and
	 * are enabled only
	 * 
	 * @param taskId
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	public List<TrmSuite> getTaskEnabledSuites(Long taskId, Long groupId) throws Exception;

	/**
	 * Returns the suite with the specified id
	 * 
	 * @param suiteId
	 *            Id of the suite
	 * @return
	 * @throws Exception
	 */
	public TrmSuite getSuite(Long suiteId) throws Exception;

	public BrowseResult<TrmTask> searchTasks(TaskSearchFilter filter) throws Exception;

	/**
	 * Insert a suite group to DB
	 * 
	 * @param group
	 * @return
	 * @throws Exception
	 */
	public Long createSuiteGroup(TrmSuiteGroup group) throws Exception;

	public void updateSuiteGroup(TrmSuiteGroup group) throws Exception;

	public TrmSuiteGroup getSuiteGroup(Long groupId) throws Exception;

	public List<TrmSuiteGroup> getTaskSuiteGroups(Long taskId) throws Exception;

	public List<TrmSuiteGroup> getTaskEnabledSuiteGroups(Long taskId) throws Exception;
	
	/**
	 * Fetches only those users who have shared tasks
	 * @param exceptUserId Id of user which shouldn't appear in the list.
	 * @param projectId 
	 * @return
	 * @throws Exception
	 */
	public Collection<User> getUsersWithSharedTasks(Long exceptUserId, Long projectId) throws Exception;
	
	/**
	 * Returns shared tasks only of the specified user
	 * @param userId
	 * @param projectId 
	 * @return
	 * @throws Exception
	 */
	public Collection<TrmTask> getUserSharedTasks(Long userId, Long projectId) throws Exception;

	public void removeSuiteGroup(Long groupId) throws Exception;
	
	public Long createTaskDependency(Long taskId, Long refTaskId) throws Exception;
	
	public Collection<TrmTaskDependency> getTaskDependencies(Long taskId) throws Exception;
	
	/**
	 * Returns all tasks which are in dependencies for current task 
	 * @param taskId Id of the task for which all the dependencies should be fetched
	 * @return
	 * @throws Exception
	 */
	public Collection<TrmTask> getDependentTasks(Long taskId) throws Exception;
	
	public void deleteTaskDependency(Long id, Long taskId) throws Exception;

    public void saveTaskProperty(Long taskId, TrmProperty property) throws Exception;

    public List<TrmProperty> getTaskProperties(Long projectId, Long taskId, String type) throws Exception;

}
