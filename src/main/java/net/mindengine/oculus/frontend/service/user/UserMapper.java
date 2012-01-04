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
