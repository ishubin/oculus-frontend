package net.mindengine.oculus.frontend.service.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;

import net.mindengine.oculus.frontend.db.jdbc.MySimpleJdbcDaoSupport;
import net.mindengine.oculus.frontend.db.search.SearchColumn;
import net.mindengine.oculus.frontend.db.search.SqlSearchCondition;
import net.mindengine.oculus.frontend.domain.db.BrowseResult;
import net.mindengine.oculus.frontend.domain.project.Project;
import net.mindengine.oculus.frontend.domain.project.ProjectBrowseFilter;
import net.mindengine.oculus.frontend.domain.project.ProjectBrowseResult;
import net.mindengine.oculus.frontend.domain.project.ProjectSearchFilter;
import net.mindengine.oculus.frontend.domain.test.TestSearchFilter;
import net.mindengine.oculus.frontend.service.customization.CustomizationUtils;
import net.mindengine.oculus.experior.utils.FileUtils;

import org.apache.commons.lang.StringUtils;

public class JdbcProjectDAO extends MySimpleJdbcDaoSupport implements ProjectDAO {
	private String projectBrowseBasicTemplate;
	private File fileProjectBrowseBasicTemplate;

	private String projectBrowseBasicCountTemplate;
	private File fileProjectBrowseBasicCountTemplate;

	@Override
	public Long createProject(Project project) throws Exception {

		PreparedStatement ps = getConnection().prepareStatement("insert into projects (name, description, path, parent_id, icon, author_id, date) values (?,?,?,?,?,?,?)");

		ps.setString(1, project.getName());
		ps.setString(2, project.getDescription());
		ps.setString(3, project.getPath());
		ps.setLong(4, project.getParentId());
		ps.setString(5, project.getIcon());
		ps.setLong(6, project.getAuthorId());
		ps.setTimestamp(7, new Timestamp(project.getDate().getTime()));
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		Long projectId = null;
		if (rs.next()) {
			projectId = rs.getLong(1);
		}

		if (project.getParentId() > 0) {
			// Increasing the parents project subprojects_count var
			update("update projects set subprojects_count = subprojects_count+1 where id = :id", "id", project.getParentId());
		}

		return projectId;
	}

	@Override
	public Project getProjectByPath(String path) throws Exception {
		List<?> projectList = query("select * from projects where path = :path", Project.class, "path", path);

		if (projectList.size() == 1) {
			return (Project) projectList.get(0);
		}
		return null;
	}

	@Override
	public Project getProject(Long id) throws Exception {
		List<?> projectList = query("select * from projects where id = :id", Project.class, "id", id);

		if (projectList.size() == 1) {
			return (Project) projectList.get(0);
		}
		return null;
	}

	@Override
	public void updateProject(Long id, Project project) throws Exception {
		update("update projects set name = :name, description = :desc, path = :path, icon = :icon where id = :id", "name", project.getName(), "desc", project.getDescription(), "path", project.getPath(), "icon", project.getIcon(), "id", id);
	}

	@Override
	public void deleteProject(Long id) throws Exception {
		Project project = this.getProject(id);
		if (project != null) {
			update("delete from projects where id = :id", "id", id);
			if (project.getParentId() > 0) {
				// Increasing the parents project subprojects_count var
				update("update projects set subprojects_count = subprojects_count-1 where id = :id", "id", project.getParentId());

			}
			else {
				// Delete dashboard
				update("delete from dashboards where project_id =:projectId", "projectId", id);
			}
			update("delete from documents where project_id =:projectId", "projectId", id);
			update("delete from tests where project_id =:projectId", "projectId", id);

			// deleting all suite statistics
			update("delete from suite_statistics where project_id =:projectId", "projectId", id);

			// TODO update tests with group_id = 0 for all test_groups which
			// should be deleted
			// TODO delete from test_groups

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Project> getSubprojects(Long parentId) throws Exception {
		List<Project> projectList = (List<Project>) query("select * from projects where parent_id = :parentId order by name asc", Project.class, "parentId", parentId);
		return projectList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ProjectBrowseResult browseProjects(ProjectBrowseFilter filter) throws Exception {
		if (filter == null)
			throw new Exception("The filter is not defined");

		SqlSearchCondition condition = new SqlSearchCondition();
		if (filter.getOnlyRoot()) {
			condition.append("p.parent_id=0");
		}

		int pageLimit = 10;
		if (filter.getPageLimit() < ProjectBrowseFilter.PAGE_LIMITS.length) {
			pageLimit = ProjectBrowseFilter.PAGE_LIMITS[filter.getPageLimit()];
		}
		String order = " order by pp.name, p.name asc ";

		String limit = " limit " + (filter.getPageOffset() - 1) * pageLimit + "," + pageLimit;
		String query = projectBrowseBasicTemplate + condition + order + limit;

		// DB query
		List<Project> projects = (List<Project>) query(query, Project.class);

		ProjectBrowseResult result = new ProjectBrowseResult();
		result.setProjects(projects);

		// DB count query
		result.setNumberOfResults(count(projectBrowseBasicCountTemplate + condition));

		return result;
	}

	public String getProjectBrowseBasicTemplate() {
		return projectBrowseBasicTemplate;
	}

	public void setProjectBrowseBasicTemplate(String projectBrowseBasicTemplate) {
		this.projectBrowseBasicTemplate = projectBrowseBasicTemplate;
	}

	public File getFileProjectBrowseBasicTemplate() {
		return fileProjectBrowseBasicTemplate;
	}

	public void setFileProjectBrowseBasicTemplate(File fileProjectBrowseBasicTemplate) throws FileNotFoundException, IOException {
		projectBrowseBasicTemplate = FileUtils.readFile(fileProjectBrowseBasicTemplate);
	}

	public String getProjectBrowseBasicCountTemplate() {
		return projectBrowseBasicCountTemplate;
	}

	public void setProjectBrowseBasicCountTemplate(String projectBrowseBasicCountTemplate) {
		this.projectBrowseBasicCountTemplate = projectBrowseBasicCountTemplate;
	}

	public File getFileProjectBrowseBasicCountTemplate() {
		return fileProjectBrowseBasicCountTemplate;
	}

	public void setFileProjectBrowseBasicCountTemplate(File fileProjectBrowseBasicCountTemplate) throws FileNotFoundException, IOException {
		projectBrowseBasicCountTemplate = FileUtils.readFile(fileProjectBrowseBasicCountTemplate);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BrowseResult<Project> getSubprojects(Long parentId, int page, int limit) throws Exception {
		BrowseResult<Project> browseResult = new BrowseResult<Project>();

		String strLimit = " limit " + (page - 1) * limit + "," + limit;

		browseResult.setNumberOfResults(count("select count(*) as count from projects where parent_id = :parentId", "parentId", parentId));

		List<Project> list = (List<Project>) query("select * from projects where parent_id = :parentId " + strLimit, Project.class, "parentId", parentId);
		browseResult.setResults(list);
		browseResult.setNumberOfDisplayedResults((long) list.size());

		browseResult.setPage(page);
		browseResult.setLimit(limit);
		return browseResult;
	}

	@Override
	public List<Project> getRootProjects() throws Exception {
		return getSubprojects(0L);
	}

	@Override
	public long getProjectRootId(Long id, Integer maxIterations) throws Exception {
		if (maxIterations == 0)
			throw new Exception("You have reached the maximum number of iterations");
		long parentId = getSimpleJdbcTemplate().queryForLong("select parent_id from projects where id = " + id);
		if (parentId == 0L) {
			return id;
		}
		else
			return getProjectRootId(parentId, maxIterations - 1);
	}

	public SqlSearchCondition createSearchCondition(ProjectSearchFilter filter) {
		SqlSearchCondition condition = new SqlSearchCondition();
		// Name
		{
			String name = filter.getName();
			if (name != null && !name.isEmpty()) {
				if (name.contains(",")) {
					condition.append(condition.createArrayCondition(name, "p.name"));
				}
				else {
					condition.append(condition.createSimpleCondition(name, true, "p.name"));
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
					condition.append(condition.createSimpleCondition(false, "pp.id", project));
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

		return condition;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BrowseResult<Project> searchSubprojects(ProjectSearchFilter filter) throws Exception {
		SqlSearchCondition condition = createSearchCondition(filter);
		// Will search only for subprojects
		condition.append(" p.parent_id>0 ");
		int pageLimit = 10;
		if (filter.getPageLimit() < TestSearchFilter.PAGE_LIMITS.length) {
			pageLimit = TestSearchFilter.PAGE_LIMITS[filter.getPageLimit()];
		}

		String limit = " limit " + (filter.getPageOffset() - 1) * pageLimit + "," + pageLimit;

		String sqlSelect = "select p.*, pp.name as parentName, pp.path as parentPath, u.login as authorLogin, u.name as authorName";

		String sqlCount = "select count(*) ";

		String sqlFrom = " from projects p " + "left join projects pp on pp.id = p.parent_id " + "left join users u on u.id = p.author_id ";

		// Customizations
		sqlFrom = CustomizationUtils.collectCustomizationSearchCondition("p.id", "project", sqlFrom, filter.getCustomizations(), condition);

		BrowseResult<Project> result = new BrowseResult<Project>();

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
		result.setResults((List<Project>) query(sql, Project.class));
		result.setPage(filter.getPageOffset());
		result.setLimit(pageLimit);
		return result;
	}

}
