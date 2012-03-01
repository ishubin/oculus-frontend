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
package net.mindengine.oculus.frontend.service.comment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;

import net.mindengine.oculus.frontend.db.jdbc.MySimpleJdbcDaoSupport;
import net.mindengine.oculus.frontend.domain.comment.Comment;
import net.mindengine.oculus.frontend.domain.db.BrowseResult;

public class JdbcCommentDAO extends MySimpleJdbcDaoSupport implements CommentDAO {

	@Override
	public Long addComment(Comment comment) throws Exception {
		PreparedStatement ps = getConnection().prepareStatement("insert into comments (text,user_id,date,unit_id,unit) values(?,?,?,?,?)");
		ps.setString(1, comment.getText());
		ps.setLong(2, comment.getUserId());
		ps.setTimestamp(3, new Timestamp(comment.getDate().getTime()));
		ps.setLong(4, comment.getUnitId());
		ps.setString(5, comment.getUnit());

		logger.info(ps);
		ps.execute();
		ResultSet rs = ps.getGeneratedKeys();
		if (rs.next()) {
			return rs.getLong(1);
		}
		return null;
	}

	@Override
	public void deleteComment(Long id) throws Exception {
		update("delete from comments where id = :id", "id", id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BrowseResult<Comment> getComments(Long unitId, String unit, Integer page, Integer limit) throws Exception {
		BrowseResult<Comment> result = new BrowseResult<Comment>();
		if (page == null || page < 1)
			page = 1;
		if (limit == null || limit < 1)
			limit = 10;

		List<Comment> list = (List<Comment>) query("select c.*, u.name as user_name, u.login as user_login from comments c left join users u on u.id = c.user_id where c.unit_id = :unitId and c.unit = :unit order by date asc limit " + ((page - 1) * limit) + ", " + limit, Comment.class, "unitId",
				unitId, "unit", unit);

		Long count = count("select count(*) from comments where unit_id = :unitId and unit = :unit", "unitId", unitId, "unit", unit);

		result.setLimit(limit);
		result.setNumberOfDisplayedResults((long) list.size());
		result.setNumberOfResults(count);
		result.setPage(page);
		result.setResults(list);

		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Comment getComment(Long commentId) throws Exception {
		List<Comment> list = (List<Comment>) query("select * from comments where id = :id", Comment.class, "id", commentId);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

}
