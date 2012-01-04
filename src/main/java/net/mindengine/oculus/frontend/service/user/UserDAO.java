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
