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
package net.mindengine.oculus.frontend.domain.project;

import java.util.Date;

import net.mindengine.oculus.frontend.web.controllers.project.ProjectCreateController;
import net.mindengine.oculus.frontend.web.controllers.project.ProjectEditController;

import org.springframework.web.multipart.MultipartFile;

public class Project {
	private Long id;
	private Long parentId;
	private String name;
	private String description;
	private String path;
	private Long subprojectsCount;
	private Long testsCount;
	private String parentName;
	private String parentPath;
	private String icon;
	private Long authorId;
	private String authorName;
	private String authorLogin;
	private Date date;
	/**
	 * This field is used only in {@link ProjectEditController} and
	 * {@link ProjectCreateController}
	 */
	private MultipartFile iconFile;

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setSubprojectsCount(Long subprojectsCount) {
		this.subprojectsCount = subprojectsCount;
	}

	public Long getSubprojectsCount() {
		return subprojectsCount;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getParentName() {
		return parentName;
	}

	public void setTestsCount(Long testsCount) {
		this.testsCount = testsCount;
	}

	public Long getTestsCount() {
		return testsCount;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIcon() {
		return icon;
	}

	public void setIconFile(MultipartFile iconFile) {
		this.iconFile = iconFile;
	}

	public MultipartFile getIconFile() {
		return iconFile;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	public Long getAuthorId() {
		return authorId;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}

	public String getParentPath() {
		return parentPath;
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

}
