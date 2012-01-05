function ajaxLoaderGet(uri, callback)
{
	var xmlHttpReq = false;
//	alert(q);
// Mozilla/Safari 
	if (window.XMLHttpRequest) 
	{
		xmlHttpReq = new XMLHttpRequest();
//		xmlHttpReq.overrideMimeType('text/xml');
	}
// IE 
	else if (window.ActiveXObject) 
	{
		xmlHttpReq = new ActiveXObject("Microsoft.XMLHTTP");
	}
	//xmlHttpReq.open('POST', uri, true);
	xmlHttpReq.open('GET', uri, true);
	xmlHttpReq.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
	
	xmlHttpReq.onreadystatechange = function() 
	{
		if (xmlHttpReq.readyState == 4) {
			(callback)(xmlHttpReq.responseText);
		}
	}
	xmlHttpReq.send(null);
}