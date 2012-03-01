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

import net.mindengine.oculus.frontend.domain.db.BrowseResult;
import net.mindengine.oculus.frontend.domain.user.User;

public interface UserDAO {
	public User authorizeUser(String login, String password) throws Exception;

	public void createUser(User user) throws Exception;

	public User getUserByEmail(String email) throws Exception;

	public User getUserByLogin(String login) throws Exception;

	public User getUserById(Long id) throws Exception;

	public void updateUser(Long id, User user) throws Exception;

	public void deleteUser(Long id) throws Exception;

	public void changeUserPassword(Long id, String password) throws Exception;

	public BrowseResult<User> fetchUsers(String name, Integer page, Integer limit) throws Exception;

}
