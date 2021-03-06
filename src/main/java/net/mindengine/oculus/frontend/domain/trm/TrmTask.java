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

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.mindengine.oculus.grid.domain.task.MultiTask;

/**
 * This POJO is used to define the Test Run Manager task. The task will be
 * created by the user and saved to DB. When the user will run the task - Oculus
 * system will convert this task to {@link MultiTask} and send it to
 * {@link TRMServer}
 * 
 * @author Ivan Shubin
 * 
 */
public class TrmTask implements Serializable {
	/**
     * 
     */
	private static final long serialVersionUID = -4562690083864045456L;
	private Long id;
	private String name;
	private String description;
	private Long userId;
	private Long projectId;
	private Date date;
	private Boolean shared;
	private String userLogin;
	private String userName;
	private String agentsFilter;
	private String build;

	private List<TrmSuite> suites;
	private Collection<TrmProperty> parameters;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setSuites(List<TrmSuite> suites) {
		this.suites = suites;
	}

	public List<TrmSuite> getSuites() {
		return suites;
	}

	public void setShared(Boolean shared) {
		this.shared = shared;
	}

	public Boolean getShared() {
		return shared;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setParameters(Collection<TrmProperty> parameters) {
        this.parameters = parameters;
    }

    public Collection<TrmProperty> getParameters() {
        return parameters;
    }

    public String getAgentsFilter() {
        return agentsFilter;
    }

    public void setAgentsFilter(String agentsFilter) {
        this.agentsFilter = agentsFilter;
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

}
