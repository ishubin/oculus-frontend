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

import net.mindengine.oculus.frontend.db.search.SearchColumn;

public class TaskSearchColumn extends SearchColumn {
	public final static Integer TASK_NAME = 1;
	public final static Integer TASK_DATE = 2;
	public final static Integer USER_NAME = 3;

	public Integer getTASK_NAME() {
		return TASK_NAME;
	}

	public Integer getTASK_DATE() {
		return TASK_DATE;
	}

	public Integer getUSER_NAME() {
		return USER_NAME;
	}
}
