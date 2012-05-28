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
package net.mindengine.oculus.frontend.domain.user;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.mindengine.oculus.frontend.service.crypt.BitCrypter;
import net.mindengine.oculus.frontend.service.exceptions.PermissionDeniedException;

public class User implements Serializable {

	/**
     * 
     */
    private static final long serialVersionUID = 4110792533124191561L;
    private Long id;
	private String name;
	private String login;
	private String password;
	private String email;
	private String permissions;
	private Map<String, Boolean> hasPermissions = null;

	public String getPermissions() {
		return permissions;
	}

	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean getAdmin() {
		if (id == 1)
			return true;
		// TODO Check user is admin or not

		return false;
	}

	public boolean getModerator() {
		if (id == 1)
			return true;
		// TODO Check if user is moderator
		return false;
	}

	public boolean hasAllPermissions(List<Permission> permissions) {
		for (Permission p : permissions) {
			Boolean b = getHasPermissions().get(p.getName());
			if (b == null || b == false) {
				return false;
			}
			p.getName();
		}
		return true;
	}

	public boolean hasSelectivePermissions(List<Permission> permissions) {
		for (Permission p : permissions) {
			Boolean b = getHasPermissions().get(p.getName());
			if (b == null || b == true) {
				return true;
			}
			p.getName();
		}
		return false;
	}

	public void updatePermissions(PermissionList permissionList) {
		BitCrypter bitCrypter = new BitCrypter();
		List<Integer> permissionCodes = bitCrypter.decrypt(permissions);
		hasPermissions = new HashMap<String, Boolean>();
		for (Integer code : permissionCodes) {
			Permission p = permissionList.getPermission(code);
			if (p != null) {
				hasPermissions.put(p.getName(), true);
			}
		}
	}

	public Map<String, Boolean> getHasPermissions() {
		return hasPermissions;
	}

	public void setHasPermissions(Map<String, Boolean> hasPermissions) {
		this.hasPermissions = hasPermissions;
	}

	public boolean hasPermission(int code) {
		BitCrypter bitCrypter = new BitCrypter();
		return bitCrypter.hasCode(permissions, code);
	}

	public void verifyPermission(String name) throws Exception {
		Boolean b = hasPermissions.get(name);
		if (b == null || b == false) {
			throw new PermissionDeniedException();
		}
	}

}
