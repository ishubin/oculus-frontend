<%@tag import="org.springframework.validation.ObjectError"%>
<%@tag import="java.util.Iterator"%>
<%@tag import="java.util.List"%>
<%@tag import="org.springframework.validation.FieldError"%>
<%@tag import="org.springframework.validation.BindingResultUtils"%>
<%@tag import="org.springframework.validation.BindingResult"%>
<%@ tag body-content="scriptless" %>
<%@ attribute name="field" required="true" %>
<%@ attribute name="command" required="true" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
if(field==null) {
    field = "";
}
BindingResult br = (BindingResult) request.getAttribute("org.springframework.validation.BindingResult."+command);
if(br!=null) {
    
    boolean foundError = false;
	List errorsList = br.getAllErrors();
	if(errorsList!=null) {
	    Iterator it = errorsList.iterator();
	    while(it.hasNext() && !foundError) {
	        ObjectError error = (ObjectError)it.next();
	        if(field.equals(error.getObjectName())){
	            if(error.getDefaultMessage()!=null) {
	                out.println(error.getDefaultMessage());
	                foundError = true;
	            }    
	        }
	    }
	}
	if(!foundError) {
	    errorsList = br.getAllErrors();
	    if(errorsList!=null) {
		    Iterator it = errorsList.iterator();
		    if(it.hasNext()) {
		        ObjectError error = (ObjectError)it.next();
		        if(error.getDefaultMessage()!=null) {
	                out.println(error.getDefaultMessage());
	            }		        
		    }
		}
	}
}

%>


