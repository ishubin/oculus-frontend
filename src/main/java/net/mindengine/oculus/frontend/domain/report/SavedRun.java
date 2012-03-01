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
package net.mindengine.oculus.frontend.domain.report;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SavedRun {
	private Long id;
	private String name;
	private Date date;
	private Long userId;

	public String generateFileUrl() {
		return generateDirUrl() + File.separator + id + ".binaryobject";
	}

	public String generateDirUrl() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = sdf.format(date);
		String convertedDate = strDate;
		convertedDate = convertedDate.replace("-", File.separator);
		return convertedDate;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
