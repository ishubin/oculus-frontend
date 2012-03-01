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
package net.mindengine.oculus.frontend.service.project.build;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;

import net.mindengine.oculus.frontend.db.jdbc.MySimpleJdbcDaoSupport;
import net.mindengine.oculus.frontend.db.search.SearchColumn;
import net.mindengine.oculus.frontend.db.search.SqlSearchCondition;
import net.mindengine.oculus.frontend.domain.db.BrowseResult;
import net.mindengine.oculus.frontend.domain.project.build.Build;
import net.mindengine.oculus.frontend.domain.project.build.BuildSearchFilter;
import net.mindengine.oculus.frontend.domain.test.TestSearchFilter;
import net.mindengine.oculus.frontend.service.customization.CustomizationUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JdbcBuildDAO extends MySimpleJdbcDaoSupport implements BuildDAO {
	private Log logger = LogFactory.getLog(getClass());

	@SuppressWarnings("unchecked")
	@Override
	public BrowseResult<Build> fetchBuilds(String patternName, int page, long projectId) throws Exception {
		BrowseResult<Build> result = new BrowseResult<Build>();
		patternName = "%" + patternName.replace("*", "%") + "%";
		if (page < 1)
			page = 1;
		List<Build> builds = (List<Build>) query("select * from builds where project_id = :projectId and name like :name order by date desc, id desc limit " + ((page - 1) * 10) + ", 10", Build.class, "projectId", projectId, "name", patternName);

		Long count = count("select count(*) from builds where project_id = :projectId and name like :name", "projectId", projectId, "name", patternName);

		result.setLimit(10);
		result.setNumberOfDisplayedResults((long) builds.size());
		result.setNumberOfResults(count);
		result.setPage(page);
		result.setResults(builds);

		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Build> getBuilds(long projectId) throws Exception {

		List<Build> builds = (List<Build>) query("select b.*, p.name as project_name, p.path as project_path " + "from builds b left join projects p on p.id = b.project_id " + "where b.project_id = :projectId order by date desc, id desc ", Build.class, "projectId", projectId);

		return builds;
	}

	@Override
	public void deleteBuild(long buildId) throws Exception {
		update("delete from builds where id = :id", "id", buildId);
	}

	@Override
	public long createBuild(Build build) throws Exception {
		String sql = "insert into builds (name, description, date, project_id) values (?,?,?,?)";
		PreparedStatement ps = getConnection().prepareStatement(sql);
		ps.setString(1, build.getName());
		ps.setString(2, build.getDescription());
		ps.setTimestamp(3, new Timestamp(build.getDate().getTime()));
		ps.setLong(4, build.getProjectId());

		logger.info(ps);
		ps.execute();

		ResultSet rs = ps.getGeneratedKeys();
		if (rs.next()) {
			return rs.getLong(1);
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Build getBuildByNameAndProject(String name, long projectId) throws Exception {
		List<Build> builds = (List<Build>) query("select b.*, p.name as project_name, p.path as project_path " + "from builds b left join projects p on p.id = b.project_id " + "where b.project_id = :projectId and b.name = :name ", Build.class, "projectId", projectId, "name", name);
		if (builds.size() > 0) {
			return builds.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Build getBuild(long buildId) throws Exception {
		List<Build> builds = (List<Build>) query("select b.*, p.name as project_name, p.path as project_path " + "from builds b left join projects p on p.id = b.project_id where b.id = :id", Build.class, "id", buildId);
		if (builds.size() > 0) {
			return builds.get(0);
		}
		return null;
	}

	@Override
	public void updateBuild(Build build) throws Exception {
		update("update builds set name = :name, description = :description where id = :id", "name", build.getName(), "description", build.getDescription(), "id", build.getId());
	}

	public SqlSearchCondition createSearchCondition(BuildSearchFilter filter) {
		SqlSearchCondition condition = new SqlSearchCondition();
		// Name
		{
			String name = filter.getName();
			if (name != null && !name.isEmpty()) {
				if (name.contains(",")) {
					condition.append(condition.createArrayCondition(name, "b.name"));
				}
				else {
					condition.append(condition.createSimpleCondition(name, true, "b.name"));
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
					condition.append(condition.createSimpleCondition(false, "p.id", project));
				}
				else {
					if (project.contains(",")) {
						condition.append(condition.createArrayCondition(project, "p.name"));
					}
					else {
						condition.append(condition.createSimpleCondition(project, true, "p.name"));
					}
				}
			}
		}
		return condition;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BrowseResult<Build> searchBuilds(BuildSearchFilter filter) throws Exception {
		SqlSearchCondition condition = createSearchCondition(filter);

		int pageLimit = 10;
		if (filter.getPageLimit() < TestSearchFilter.PAGE_LIMITS.length) {
			pageLimit = TestSearchFilter.PAGE_LIMITS[filter.getPageLimit()];
		}

		String limit = " limit " + (filter.getPageOffset() - 1) * pageLimit + "," + pageLimit;

		String sqlSelect = "select b.*, p.name as project_name, p.path as project_path ";

		String sqlCount = "select count(*) ";

		String sqlFrom = " from builds b " + "left join projects p on p.id = b.project_id ";

		// Customizations
		sqlFrom = CustomizationUtils.collectCustomizationSearchCondition("b.id", "build", sqlFrom, filter.getCustomizations(), condition);

		BrowseResult<Build> result = new BrowseResult<Build>();

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
		result.setResults((List<Build>) query(sql, Build.class));
		result.setPage(filter.getPageOffset());
		result.setLimit(pageLimit);
		return result;
	}

}
