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
package net.mindengine.oculus.frontend.domain.test;

import java.util.Date;
import java.util.List;

public class Test {
	private Long id;
	private String name;
	private Long projectId;
	private String projectPath;
	private String projectName;
	private String parentProjectPath;
	private String parentProjectName;
	private Long authorId;
	private String description;
	private Date date = new Date();
	private String authorName;
	private String authorLogin;
	private List<TestParameter> inputParameters;
	private List<TestParameter> outputParameters;
	private Long groupId;
	private String groupName;
	private String content;
	
	/**
	 * This field is used for automation test mapping in TestRunManager For now
	 * it is used to define class-path
	 */
	private String mapping;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	public Long getAuthorId() {
		return authorId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorLogin(String authorLogin) {
		this.authorLogin = authorLogin;
	}

	public String getAuthorLogin() {
		return authorLogin;
	}

	public void setMapping(String mapping) {
		this.mapping = mapping;
	}

	public String getMapping() {
		return mapping;
	}

	public List<TestParameter> getInputParameters() {
		return inputParameters;
	}

	public void setInputParameters(List<TestParameter> inputParameters) {
		this.inputParameters = inputParameters;
	}

	public List<TestParameter> getOutputParameters() {
		return outputParameters;
	}

	public void setOutputParameters(List<TestParameter> outputParameters) {
		this.outputParameters = outputParameters;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}

	public String getProjectPath() {
		return projectPath;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setParentProjectPath(String parentProjectPath) {
		this.parentProjectPath = parentProjectPath;
	}

	public String getParentProjectPath() {
		return parentProjectPath;
	}

	public void setParentProjectName(String parentProjectName) {
		this.parentProjectName = parentProjectName;
	}

	public String getParentProjectName() {
		return parentProjectName;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupName() {
		return groupName;
	}

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
