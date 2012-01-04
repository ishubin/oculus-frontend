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
                <c:when test="${attachment!=null}">
                    {
                        "result":"${resultAction}",
                        "object":{
                            "id":"${attachment.id}",
                            "documentId":"${attachment.documentId}",
                            "name":"${attachment.name}",
                            "size":"${attachment.size}",
                            "type":"${attachment.type}"
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