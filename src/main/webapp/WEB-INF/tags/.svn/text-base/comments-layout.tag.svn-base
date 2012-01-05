<%@ tag body-content="scriptless" %>
<%@ attribute name="comments" required="true" type="net.mind_engine.oculus.domain.db.BrowseResult"%>
<%@ attribute name="redirect" required="true"%>
<%@ attribute name="unitId" required="true"%>
<%@ attribute name="unit" required="true"%>
<%@ attribute name="user" required="true" type="net.mind_engine.oculus.domain.user.User"%>

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>

<tag:panel align="center" logo="../images/workflow-icon-comment.png" title="Comments (${comments.numberOfResults})" id="CommentsLayout" width="100%" disclosure="true" closed="false">
    <c:if test="${user!=null}">
        <form action="../comment/add" method="post">
            <input type="hidden" name="unitId" value="${unitId }"/>
            <input type="hidden" name="unit" value="${unit}"/>
            <input type="hidden" name="redirect" value="${redirect}"/>
            <textarea class="border-textarea" rows="7" style="width:100%;" name="text"></textarea>
            <br/>
            <tag:submit value="Leave Comment" name="Submit"></tag:submit>
        </form>
    </c:if>
    <br/>
    <tag:pagination-simple numberOfResults="${comments.numberOfResults}" url="${redirect}" currentPage="${comments.page}" pageLimit="${comments.limit}" pageUrlParameter="commentsPage"></tag:pagination-simple>
    <br/>
    <br/>
    <script language="javascript">
    function removeComment(id)
    {
        if(confirm("Are you sure you want to remove this comment?"))
        {
            document.forms.formRemoveComment.commentId.value = id;
            document.forms.formRemoveComment.submit();
        }
    }
    </script>
    <form name="formRemoveComment" action="../comment/remove" method="post">
        <input type="hidden" name="commentId" value=""/>
        <input type="hidden" name="redirect" value="${redirect}"/>
    </form>
    <c:forEach items="${comments.results}" var="comment">
        <table class="comments-layout" border="0" cellpadding="4" cellspacing="0" width="100%">
            <tr>
                <td class="comments-layout" rowspan="2" width="40px" valign="top">
                    <img src="../images/user.png"/>
                </td>
                <td class="comments-layout-header">
                    <c:choose>
                        <c:when test="${comment.userId>0}">
                            <a href="../user/profile-${comment.userLogin}"><tag:escape text="${comment.userName}"></tag:escape> </a>
                        </c:when>
                        <c:otherwise>
                            <span style="color:gray;"><i>Unknown user</i></span>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td class="comments-layout-date" width="100px" align="right">
                    <tag:date date="${comment.date}"/>
                </td>
            </tr>
            <tr>
                <td class="comments-layout" colspan="2">
                    <tag:bbcode-render>${comment.text}</tag:bbcode-render>
                    
                    <c:if test="${user.id == comment.userId || user.hasPermissions.comment_managment == true}">
                        <br/><br/>
                        <a href="javascript:removeComment(${comment.id});"><img src="../images/button-close-2.png"/> Remove</a>
                    </c:if>
                    <br/>
                </td>
            </tr>
        </table>
        
    </c:forEach>
    
</tag:panel>