package net.mindengine.oculus.frontend.service.folder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import net.mindengine.oculus.frontend.db.jdbc.MySimpleJdbcDaoSupport;
import net.mindengine.oculus.frontend.domain.folder.Folder;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JdbcFolderDAO extends MySimpleJdbcDaoSupport implements FolderDAO {
	Log logger = LogFactory.getLog(getClass());

	@Override
	public Long createFolder(Folder folder) throws Exception {
		if (folder.getParentId() != null && folder.getParentId() > 0) {
			// Getting parent folder from db so the new folder can include the
			// parents id to its branch
			Folder parentFolder = getFolder(folder.getParentId());
			if (parentFolder == null)
				throw new UnexistentResource("The folder with id = " + folder.getParentId() + " doesn't exist");
			String branch = "";
			if (parentFolder.getBranch() != null) {
				branch = parentFolder.getBranch();
			}
			folder.setBranch(branch + "(" + parentFolder.getId() + ")");
			folder.setProjectId(parentFolder.getProjectId());
		}

		String sql = "insert into folders (project_id,parent_id,name,description,user_id, branch) values (?,?,?,?,?,?)";
		PreparedStatement ps = getConnection().prepareStatement(sql);

		ps.setLong(1, folder.getProjectId());
		ps.setLong(2, folder.getParentId());
		ps.setString(3, folder.getName());
		ps.setString(4, folder.getDescription());
		ps.setLong(5, folder.getUserId());
		ps.setString(6, folder.getBranch());

		logger.info(ps);
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		Long id = null;
		if (rs.next()) {
			id = rs.getLong(1);
		}
		if (id != null && id > 0) {
			if (folder.getParentId() > 0) {
				update("update folders set children=children+1 where id = :parentId", "parentId", folder.getParentId());
			}
			return id;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Folder> getFoldersByParent(Long parentId) throws Exception {
		return (List<Folder>) query("select * from folders where parent_id = :parentId order by name asc", Folder.class, "parentId", parentId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Folder getFolder(Long id) throws Exception {
		List<Folder> list = (List<Folder>) query("select * from folders where id = :id order by name asc", Folder.class, "id", id);

		if (list.size() > 0)
			return list.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Folder> getFolders(Long projectId, Long parentId) throws Exception {
		return (List<Folder>) query("select * from folders where project_id = :projectId  and parent_id =:parentId order by name asc", Folder.class, "projectId", projectId, "parentId", parentId);
	}

	@Override
	public void deleteFolder(Long id, Long parentId) throws Exception {
		update("delete from folders where id = " + id + " or branch like \"%(" + id + ")%\"");

		update("delete from documents where folder_id = \"" + id + "\" or branch like \"%(" + id + ")%\"");

		update("update folders set children=children-1 where id = :id", "id", parentId);

	}

	@Override
	public void renameFolder(Long id, String name) throws Exception {
		update("update folders set name = :name where id = :id", "id", id, "name", name);
	}
	
}
