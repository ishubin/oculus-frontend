<%@ include file="/include.jsp" %>
<html>

<body>
    <form name="formResponse">
    
	    <textarea name="response">
		    <c:choose>
		        <c:when test="${error!=null}">
		            {
		                "result":"error",
		                "type":"${error.type}",
		                "text":"${error.text}"
		            }
		        </c:when>
		        <c:when test="${document!=null}">
		            {
		                "result":"${resultAction}",
		                "object":{
		                    "id":"${document.id}",
		                    "folderId":"${document.folderId}",
		                    "projectId":"${document.projectId}",
		                    "name":"${document.name}",
		                    "typeExtended":"${document.typeExtended}"
		                }
		            }
		        </c:when>
		        <c:otherwise>
		            The error appeared in controller
		        </c:otherwise>
		        
		    </c:choose>
		</textarea>
	</form>
</body>


</html>