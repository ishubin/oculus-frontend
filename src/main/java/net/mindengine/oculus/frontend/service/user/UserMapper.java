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
package net.mindengine.oculus.frontend.service.user;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.mindengine.oculus.frontend.domain.user.User;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

public class UserMapper implements ParameterizedRowMapper<User> {
	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		User user = new User();
		user.setId(rs.getLong("id"));
		user.setLogin(rs.getString("login"));
		user.setPassword(rs.getString("password"));
		user.setName(rs.getString("name"));
		return user;
	}
}
