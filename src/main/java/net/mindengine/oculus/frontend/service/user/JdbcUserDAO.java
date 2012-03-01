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
package net.mindengine.oculus.frontend.service.user;

import java.util.List;

import net.mindengine.oculus.frontend.db.jdbc.MySimpleJdbcDaoSupport;
import net.mindengine.oculus.frontend.domain.db.BrowseResult;
import net.mindengine.oculus.frontend.domain.user.User;

public class JdbcUserDAO extends MySimpleJdbcDaoSupport implements UserDAO {

	@Override
	public User authorizeUser(String login, String password) throws Exception {
		List<?> usersList = query("select * from users where login = :login  and password = :password", User.class, "login", login, "password", password);

		if (usersList.size() == 1) {
			return (User) usersList.get(0);
		}

		return null;
	}

	@Override
	public void createUser(User user) throws Exception {
		update("insert into users (login, name, email, password) values (:login,:name,:email, :password)", "login", user.getLogin(), "name", user.getName(), "email", user.getEmail(), "password", user.getPassword());
	}

	@Override
	public User getUserByEmail(String email) throws Exception {
		List<?> usersList = query("select * from users where email = :email", User.class, "email", email);

		if (usersList.size() == 1) {
			return (User) usersList.get(0);
		}
		return null;
	}

	@Override
	public User getUserByLogin(String login) throws Exception {
		List<?> usersList = query("select * from users where login = :login", User.class, "login", login);

		if (usersList.size() == 1) {
			return (User) usersList.get(0);
		}
		return null;
	}

	@Override
	public User getUserById(Long id) throws Exception {
		List<?> usersList = query("select * from users where id = :id", User.class, "id", id);

		if (usersList.size() == 1) {
			return (User) usersList.get(0);
		}
		return null;
	}

	@Override
	public void updateUser(Long id, User user) throws Exception {
		update("update users set login = :login, name = :name, email = :email, permissions = :permissions where id = :id", "login", user.getLogin(), "name", user.getName(), "email", user.getEmail(), "permissions", user.getPermissions(), "id", user.getId());
	}

	@Override
	public void deleteUser(Long id) throws Exception {
		update("delete from users where id = :id", "id", id);
	}

	@Override
	public void changeUserPassword(Long id, String password) throws Exception {
		update("update users set password = :password where id = :id", "id", id, "password", password);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BrowseResult<User> fetchUsers(String name, Integer page, Integer limit) throws Exception {
		BrowseResult<User> result = new BrowseResult<User>();
		if (page == null || page < 1)
			page = 1;
		if (limit == null || limit < 1)
			limit = 10;
		name = "%" + name.replace("*", "%") + "%";

		List<User> users = (List<User>) query("select * from users where name like :name or login like :name order by name asc limit " + ((page - 1) * 10) + ", " + limit, User.class, "name", name);

		Long count = count("select count(*) from users where name like :name or login like :name", "name", name);

		result.setLimit(limit);
		result.setNumberOfDisplayedResults((long) users.size());
		result.setNumberOfResults(count);
		result.setPage(page);
		result.setResults(users);

		return result;
	}
}
