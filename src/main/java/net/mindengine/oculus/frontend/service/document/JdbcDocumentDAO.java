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
package net.mindengine.oculus.frontend.service.document;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import net.mindengine.oculus.frontend.db.jdbc.MySimpleJdbcDaoSupport;
import net.mindengine.oculus.frontend.db.search.SearchColumn;
import net.mindengine.oculus.frontend.db.search.SqlSearchCondition;
import net.mindengine.oculus.frontend.domain.db.BrowseResult;
import net.mindengine.oculus.frontend.domain.document.Document;
import net.mindengine.oculus.frontend.domain.document.DocumentAttachment;
import net.mindengine.oculus.frontend.domain.document.DocumentSearchFilter;
import net.mindengine.oculus.frontend.domain.folder.Folder;
import net.mindengine.oculus.frontend.domain.test.TestSearchFilter;
import net.mindengine.oculus.frontend.service.customization.CustomizationUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JdbcDocumentDAO extends MySimpleJdbcDaoSupport implements DocumentDAO {
	Log logger = LogFactory.getLog(getClass());

	@SuppressWarnings("unchecked")
	@Override
	public List<Document> getDocumentsByFolderId(Long folderId) throws Exception {
		return (List<Document>) query("select * from documents where folder_id = :folderId", Document.class, "folderId", folderId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Document> getDocuments(Long projectId, Long folderId) throws Exception {
		return (List<Document>) query("select * from documents where project_id = :projectId and folder_id = :folderId", Document.class, "projectId", projectId, "folderId", folderId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Document getDocument(Long id) throws Exception {
		List<Document> list = (List<Document>) query("select * from documents where id = :id", Document.class, "id", id);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public Long createDocument(Document document) throws Exception {
		PreparedStatement ps = getConnection().prepareStatement("insert into documents " + "(name," + "description," + "content," + "date," + "project_id," + "folder_id," + "user_id," + "branch," + "type," + "size," + "type_extended) " + "values (?,?,?,?,?,?,?,?,?,?,?)");

		ps.setString(1, document.getName());
		ps.setString(2, document.getDescription());
		ps.setString(3, document.getContent());
		ps.setTimestamp(4, new Timestamp(document.getDate().getTime()));
		ps.setLong(5, document.getProjectId());
		ps.setLong(6, document.getFolderId());
		ps.setLong(7, document.getUserId());
		ps.setString(8, document.getBranch());
		ps.setString(9, document.getType());
		ps.setLong(10, document.getSize());
		ps.setString(11, document.getTypeExtended());
		logger.info(ps);
		ps.executeUpdate();

		ResultSet rs = ps.getGeneratedKeys();
		Long id = null;
		if (rs.next()) {
			id = rs.getLong(1);
		}
		if (document.getFolderId() > 0) {
			update("update folders set documents=documents+1 where id = :id", "id", document.getFolderId());
		}
		return id;
	}

	@Override
	public void updateDocument(Document document) throws Exception {
		update("update documents set " + "name = :name," + "description = :description," + "content = :content," + "type_extended = :typeExtended" + " where id = :id", "name", document.getName(), "description", document.getDescription(), "content", document.getContent(), "typeExtended", document
				.getTypeExtended(), "id", document.getId());
	}

	@Override
	public void deleteDocument(Long id, Long folderId) throws Exception {
		update("delete from documents where id = :id", "id", id);

		update("update folders set documents=documents-1 where id = :id", "id", folderId);
	}

	public SqlSearchCondition createSearchCondition(DocumentSearchFilter filter) {
		SqlSearchCondition condition = new SqlSearchCondition();
		// Name
		{
			String name = filter.getName();
			if (name != null && !name.isEmpty()) {
				if (name.contains(",")) {
					condition.append(condition.createArrayCondition(name, "d.name"));
				}
				else {
					condition.append(condition.createSimpleCondition(name, true, "d.name"));
				}
			}
		}
		// Sub-Project
		{
			String subProject = filter.getSubProject();
			if (subProject != null && !subProject.isEmpty()) {
				// checking whether it is an id of project or just a name
				if (StringUtils.isNumeric(subProject)) {
					// The id of a project was provided
					condition.append(condition.createSimpleCondition(false, "p.id", subProject));
				}
				else {
					if (subProject.contains(",")) {
						condition.append(condition.createArrayCondition(subProject, "p.name", "p.path"));
					}
					else {
						condition.append(condition.createSimpleCondition(subProject, true, "p.name", "p.path"));
					}
				}
			}
		}
		// Project
		{
			String project = filter.getProject();
			if (project != null && !project.isEmpty()) {
				// checking whether it is an id of project or just a name
				if (StringUtils.isNumeric(project)) {
					// The id of a project was provided
					condition.append(condition.createSimpleCondition(false, "pp.id", project, "p.id", project));
				}
				else {
					if (project.contains(",")) {
						condition.append(condition.createArrayCondition(project, "pp.name"));
					}
					else {
						condition.append(condition.createSimpleCondition(project, true, "pp.name"));
					}
				}
			}
		}
		// User
		{
			String user = filter.getDesigner();
			if (user != null && !user.isEmpty()) {
				if (user.contains(",")) {
					condition.append(condition.createArrayCondition(user, "u.name", "u.login"));
				}
				else {
					condition.append(condition.createSimpleCondition(user, true, "u.name", "u.login"));
				}
			}
		}
		// Document Type
		{
			String type = filter.getDocumentType();
			if (type != null && !type.isEmpty()) {
				if (type.contains(",")) {
					condition.append(condition.createArrayCondition(type, "d.type"));
				}
				else {
					condition.append(condition.createSimpleCondition(type, false, "d.type"));
				}
			}
		}
		return condition;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BrowseResult<Document> searchDocuments(DocumentSearchFilter filter) throws Exception {
		SqlSearchCondition condition = createSearchCondition(filter);

		int pageLimit = 10;
		String limit = "";
		if(filter.getPageLimit()!=null){
    		if (filter.getPageLimit() < TestSearchFilter.PAGE_LIMITS.length) {
    			pageLimit = TestSearchFilter.PAGE_LIMITS[filter.getPageLimit()];
    		}
    		limit = " limit " + (filter.getPageOffset() - 1) * pageLimit + "," + pageLimit;
		}


		String sqlSelect = "select d.*, pp.name as parentProjectName, pp.path as parentProjectPath, p.name as projectName, p.path as projectPath, u.login as authorLogin, u.name as authorName";

		String sqlCount = "select count(*) ";

		String sqlFrom = " from documents d " + "left join projects p on p.id = d.project_id " + "left join projects pp on pp.id = p.parent_id " + "left join users u on u.id = d.user_id ";

		// Customizations
		sqlFrom = CustomizationUtils.collectCustomizationSearchCondition("d.id", "test-case", sqlFrom, filter.getCustomizations(), condition);

		BrowseResult<Document> result = new BrowseResult<Document>();

		// Preparing the order by statement
		String order = "";
		Integer orderBy = filter.getOrderByColumnId();
		if (orderBy != null && orderBy >= 0) {
			SearchColumn column = filter.getColumnById(orderBy);
			if (column != null) {

				String direction;
				if (filter.getOrderDirection() >= 0) {
					direction = "asc";
				}
				else
					direction = "desc";

				order = " order by " + column.getSqlColumn() + " " + direction + " ";

			}
		}

		String sql = sqlSelect + sqlFrom + condition + order + limit;

		result.setNumberOfResults(count(sqlCount + sqlFrom + condition));
		result.setResults((List<Document>) query(sql, Document.class));
		result.setPage(filter.getPageOffset());
		result.setLimit(pageLimit);
		return result;

	}

    @Override
    public void moveDocumentToFolder(Long documentId, Folder folder, Folder sourceFolder) throws Exception {
        update("update documents set project_id = "+folder.getProjectId()+", folder_id = "+folder.getId()+" ,branch='"+folder.getBranch()+"' where id ="+documentId);
        
        update("update folders set documents=documents+1 where id = "+folder.getId());
        
        if(sourceFolder!=null && sourceFolder.getDocuments()>0){
            update("update folders set documents=documents-1 where id = "+sourceFolder.getId());
        }
    }
    @Override
    public void moveDocumentToProject(Long documentId, Long projectId, Folder sourceFolder) throws Exception {
        update("update documents set project_id = "+projectId+", folder_id = 0 ,branch=null where id ="+documentId);
        
        if(sourceFolder!=null && sourceFolder.getDocuments()>0){
            update("update folders set documents=documents-1 where id = "+sourceFolder.getId());
        }
    }

    @Override
    public Folder getDocumentFolder(Long id) throws Exception {
        return querySingle("select f.* from folders f left join documents d on d.folder_id = f.id where d.id = "+id, Folder.class);
    }

    @Override
    public Long createAttachment(DocumentAttachment attachment) throws Exception {
        PreparedStatement ps = getConnection().prepareStatement("insert into document_attachments (document_id, name, description, date, user_id, size, type) values (?,?,?,?,?,?,?)");
        ps.setLong(1, attachment.getDocumentId());
        ps.setString(2, attachment.getName());
        ps.setString(3, attachment.getDescription());
        ps.setTimestamp(4, new Timestamp(attachment.getDate().getTime()));
        ps.setLong(5, attachment.getUserId());
        ps.setLong(6, attachment.getSize());
        ps.setString(7, attachment.getType());
        
        logger.info(ps);
        ps.execute();
        
        ResultSet rs = ps.getGeneratedKeys();
        if(rs.next()){
            return rs.getLong(1);
        }
        return null;
    }

    @Override
    public void deleteAtachment(Long attachmentId) throws Exception {
        update("delete from document_attachments where id = "+attachmentId);
    }

    @Override
    public DocumentAttachment getAttachment(Long attachmentId) throws Exception {
        return querySingle("select * from document_attachments where id = "+attachmentId, DocumentAttachment.class);
    }

    @Override
    public Collection<DocumentAttachment> getDocumentAttachments(Long documentId) throws Exception {
        return query("select * from document_attachments where document_id = "+documentId, DocumentAttachment.class);
    }
}
