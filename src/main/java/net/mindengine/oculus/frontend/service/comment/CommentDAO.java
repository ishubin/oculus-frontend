package net.mindengine.oculus.frontend.service.comment;

import net.mindengine.oculus.frontend.domain.comment.Comment;
import net.mindengine.oculus.frontend.domain.db.BrowseResult;

public interface CommentDAO {
	public Long addComment(Comment comment) throws Exception;

	public Comment getComment(Long commentId) throws Exception;

	public BrowseResult<Comment> getComments(Long unitId, String unit, Integer page, Integer limit) throws Exception;

	public void deleteComment(Long id) throws Exception;
}
