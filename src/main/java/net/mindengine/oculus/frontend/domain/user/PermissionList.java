package net.mindengine.oculus.frontend.domain.user;

import java.util.List;

public class PermissionList {
	private List<Permission> permissions;

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

	public Permission getPermission(Integer code) {
		for (Permission p : permissions) {
			if (p.getCode() == code.intValue())
				return p;
		}
		return null;
	}

}
