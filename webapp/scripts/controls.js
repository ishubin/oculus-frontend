
function cloneObject(obj){
    if(obj == null || typeof(obj) != 'object')
        return obj;

    var temp = obj.constructor();

    for(var key in obj)
        temp[key] = cloneObject(obj[key]);
    return temp;
}

function escapeHTML(html) {
	if(html!=null && typeof html=="string") {
		return html.replace(/&/gmi, '&amp;'). replace(/"/gmi, '&quot;'). replace(/>/gmi, '&gt;'). replace(/</gmi, '&lt;');
	}
	return "";
}
function escapeJSON(json) { return json.replace(/"/gmi,'\\"').replace(/\n/gmi,'\\n');}

function escapeURIText(text){
	if (encodeURIComponent) {
	    text = encodeURIComponent(text);
	} else {
	    text = escape(text);
	}
	return text;
}

var JSON = JSON || {}; 
// implement JSON.stringify serialization
JSON.stringify = JSON.stringify || function (obj) {

	var t = typeof (obj);
	if (t != "object" || obj === null) {

		// simple data type
		if (t == "string") obj = '"'+obj+'"';
		return String(obj);

	}
	else {

		// recurse array or object
		var n, v, json = [], arr = (obj && obj.constructor == Array);

		for (n in obj) {
			v = obj[n]; t = typeof(v);

			if (t == "string") v = '"'+v+'"';
			else if (t == "object" && v !== null) v = JSON.stringify(v);

			json.push((arr ? "" : '"' + n + '":') + String(v));
		}

		return (arr ? "[" : "{") + String(json) + (arr ? "]" : "}");
	}
};

//implement JSON.parse de-serialization
JSON.parse = JSON.parse || function (str) {
	if (str === "") str = '""';
	eval("var p=" + str + ";");
	return p;
};

function getMouseXY(e) 
{
	/*var IE = document.all?true:false;
	if (!IE) document.captureEvents(Event.MOUSEMOVE);

	var tempX = 0;
	var tempY = 0;

	if (IE) 
	{ 
		tempX = event.clientX + document.body.scrollLeft;
		tempY = event.clientY + document.body.scrollTop;
	}
	else 
	{
		tempX = e.pageX;
		tempY = e.pageY;
	}  
	if (tempX < 0){tempX = 0;}
	if (tempY < 0){tempY = 0;}  
	
	return {"x":tempX,"y":tempY};*/
	var posx = 0;
	var posy = 0;
	if (!e) var e = window.event;
	if (e.pageX || e.pageY) 	{
		posx = e.pageX;
		posy = e.pageY;
	}
	else if (e.clientX || e.clientY) 	{
		posx = e.clientX + document.body.scrollLeft
			+ document.documentElement.scrollLeft;
		posy = e.clientY + document.body.scrollTop
			+ document.documentElement.scrollTop;
	}
	
	return {x:posx,y:posy};

}

function onDisclosurePanelClick(divPanelId, divIconId) {
	$("#"+divPanelId).slideToggle("fast", function(){
		var className;
		
		if ($(this).is(':hidden')) {
            className = "disclosure-icon-close";
        } else {
            className = "disclosure-icon-open";
        }
		
		if(divIconId!=null){
			$("#"+divIconId).attr("class", className);
		}
	});
}

function showDialog(id)
{
	var div = document.getElementById(id);
	if(div!=null)
	{
		div.style.display = "block";
	}
}

function hideDialog(id)
{
	var div = document.getElementById(id);
	if(div!=null)
	{
		div.style.display = "none";
	}
}


function onWorkflowPanelToggle(id){
	$("#workflow_"+id+"_body").slideToggle("fast", function(){
		var className;
		
		if ($(this).is(':hidden')) {
            className = "small-button-show";
        } else {
            className = "small-button-hide";
        }
		
		$("#workflow_"+id+"_toggleButton").attr("class", className);
	});		
}


function onPanelToggle(id){
	$('#panel_'+id+'_body').slideToggle('fast', function(){
		var className;
		if($(this).is(':hidden')){
			className = 'small-button-show';
		}
		else className = 'small-button-hide';
		
		$("#panel_"+id+"_toggleButton").attr("class", className);
	});
	
}
function onPanelTitle(id){
	onPanelToggle(id);
}
function onPanelMaximize(divName,id)
{
	var size = window.size();
	var width = size.width-40;
	var height = size.height-50;
	
	var div = document.getElementById(divName);
	if(div)
	{
		_currentPopupDiv = div;
		
		div.style.position = "absolute";
		div.style.width = width+"px";
		div.style.height = height+"px";
		
		div.style.zIndex = 999;
		
		var center = window.center();
		
		var x = center.x - width/2;
		var y = center.y - height/2;
		
		div.style.left = x;
		div.style.top = 10;
		div.style.display = "block";
	}
	var divShadow = document.getElementById("divShadow");
	if(divShadow != null)
	{
		divShadow.style.display = "block";
	}
	
	var divBtnMaximize = document.getElementById("panel_"+id+"_maximizeButton");
	var divBtnMinimize = document.getElementById("panel_"+id+"_minimizeButton");
	divBtnMaximize.style.display = "none";
	divBtnMinimize.style.display = "block";
}
function onPanelMinimize(divName,id)
{
	var div = document.getElementById(divName);
	div.style.position = "relative";
	div.style.width = "100%";
	div.style.height = "";
	div.style.left = "0px";
	div.style.top = "0px";
	div.style.zIndex = 1;
	var divShadow = document.getElementById("divShadow");
	if(divShadow != null)
	{
		divShadow.style.display = "none";
	}
	var divBtnMaximize = document.getElementById("panel_"+id+"_maximizeButton");
	var divBtnMinimize = document.getElementById("panel_"+id+"_minimizeButton");
	divBtnMaximize.style.display = "block";
	divBtnMinimize.style.display = "none";
}

function showWarning(text)
{
	var div = document.getElementById("divGlobalWarnDialogBody");
	if(div!=null)
	{
		div.innerHTML = text;
		showPopup("divGlobalWarnDialog", 400, 400);
	}		
}
function showInfo(text)
{
	var div = document.getElementById("divGlobalInfoDialogBody");
	if(div!=null)
	{
		div.innerHTML = text;
		showPopup("divGlobalInfoDialog", 400, 400);
	}		
}


function createPaginationBlock(div, numberOfResults, page, limit, onPageFunc)
{
	var html = "";
	if(numberOfResults>limit)
	{
		var c = page;
		
		var n = Math.floor(numberOfResults/limit);
		
		var mod = numberOfResults % limit;
		
		if(mod > 0) n++;
		
		var d = Math.floor(Math.min(n,limit)/2);
		
		var p1 = c-d;
		var p2 = c+d;
	
		var delta1=0;
		var delta2=0;
		if(p1<1)
		{
		    p1 = 1;
		    delta2 = 1-p1;
		}
		if(p2>n)
		{
		    p2 = n;
		    delta1 = p2-n;
		}
	
		if((delta2+p2)>n)
		{
		    p2 = n;
		}
		else p2+=delta2;
	
		if((p1-delta1)<1)
		{
		    p1 = 1;
		}
		else p1-=delta1;
	
		html+="<table class=\"pagination\" border=\"0\"><tr>";
		html+="<td><a href=\"javascript:"+onPageFunc+"("+1+");\">&lt;&lt;</a></td>";
		for(var i = p1; i<=p2; i++)
		{
			html+="<td>";
			if(c!=i)
			{
				html+="<a href=\"javascript:"+onPageFunc+"("+i+");\">"+i+"</a>";
			}
			else
			{
				html+="<b>"+i+"</b>";
			}
			html+="</td>";
		}
		html+="<td><a href=\"javascript:"+onPageFunc+"("+n+");\">&gt;&gt;</a></td>";
		html+="</tr></table>";
	}
	div.innerHTML = html;
}



function generateCustomizationLayout(customizationGroups)
{
	var html = "";
	for(var j=0; j<customizationGroups.length; j++)
	{
		for(var i=0; i<customizationGroups[j].customizations.length; i++)
		{
			var c = customizationGroups[j].customizations[i];
			html+="<br/><b>"+escapeHTML(c.customization.name)+"</b>";
			html+="<div class='small-description'>"+escapeHTML(c.customization.description)+"</div>";
			if(c.customization.type=="text")
			{
				html+="<textarea class='border-textarea' rows='7' style='width:100%;' id='tcCustomizationParameter_"+c.customization.id+"' style='width:100%;'></textarea><br/>";
			}
			else if(c.customization.type=="assignee")
			{
				html+="<input type=\"hidden\" id=\"tcCustomizationParameter_"+c.customization.id+"\" value=\"\"/>";
				html+="<div style=\"width:134px;height:19px;margin-bottom:5px;\">";
				html+="<a class=\"pick-button\" id=\"tcCustomizationParameter_"+c.customization.id+"Link\" href=\"javascript:onPickUserClick('tcCustomizationParameter_"+c.customization.id+"','tcCustomizationParameter_"+c.customization.id+"Link');\">";
				html+="Pick User...</a>";
				html+="</div>";
			}
			else if(c.customization.type=="list")
			{
				html+="<select id=\"tcCustomizationParameter_"+c.customization.id+"\"";
				
				if(c.customization.subtype=="list")
				{
					html+=" size=\"7\" style=\"width:100%;\"/>";
				}
				else html+="/>";
				html+="<option value='' style='color:gray;'>Not Set</option>";
				for(var k=0; k<c.possibleValues.length; k++)
				{
					var pv = c.possibleValues[k];
					html+="<option value=\""+pv.id+"\">"+escapeHTML(pv.possibleValue)+"</option>";
				}
				html+="</select><br/>";
			}
			else if(c.customization.type=="checkbox")
			{
				html+="<input type='checkbox' id='tcCustomizationParameter_"+c.customization.id+"'/>";
			}
			else if(c.customization.type=="checklist")
			{
				if(c.possibleValues.length>10)
				{
					html+="<div class='customization-checklist'>";
				}
				for(var k=0; k<c.possibleValues.length; k++)
				{
					var pv = c.possibleValues[k];
					html+="<input type='checkbox' id='tcCustomizationParameter_"+c.customization.id+"_pv_"+pv.id+"'/>";
					html+="<label for='tcCustomizationParameter_"+c.customization.id+"_pv_"+pv.id+"'>"+escapeHTML(pv.possibleValue)+"</label><br/>";
				}
				if(c.possibleValues.length>10)
				{
					html+="</div>";
				}
			}
			
		}
	}	
	return html;
}


/*
 * This functions are used for list layout in test parametrization page for editing the possible values
 */
function getPVList(layoutName)
{
	return eval("_testParametrizationPossibleValueList"+layoutName);
}
function getPVDefaultValue(layoutName)
{
	return eval("_testParametrizationPossibleValueDefault"+layoutName);
}
function setPVDefaultValue(layoutName, defaultValue)
{
	return eval("_testParametrizationPossibleValueDefault"+layoutName+"=\""+escapeJSON(defaultValue)+"\"");
}
function onPVListAddPossibleValue(layoutName)
{
	var textfield = document.getElementById("pv_list_parameter_name_"+layoutName);
	
	var pv = textfield.value;
	if(pv!="")
	{
		textfield.value = "";
		var list = getPVList(layoutName);
		list[list.length] = pv;
		onPVListRenderList(layoutName);
		if(list.length==1)
		{
			setPVDefaultValue(layoutName, list[0]);
			var radio = document.getElementById("pv_list_radio_default_value"+layoutName+"_0");
			radio.checked = true;
		}
	}
	else alert("Please enter possible value");
	
}
function onPVListRemovePossibleValue(layoutName, pvId)
{
	var list = getPVList(layoutName);
	list.remove(pvId);
	onPVListRenderList(layoutName);
	//Checking if the defaultValue is still referencing to existent possible value in list
	var defaultValue = getPVDefaultValue(layoutName);
	var dvId = -1;
	for(var i=0;i<list.length && dvId<0;i++)
	{
		if(defaultValue==list[i])
		{
			dvId = i;
		}
	}
	if(dvId<0 && list.length>0)
	{
		setPVDefaultValue(layoutName, list[0]);
		var radio = document.getElementById("pv_list_radio_default_value"+layoutName+"_0");
		radio.checked = true;
	}
	
}
function onPVListChooseDefaultValue(layoutName, pvId)
{
	var list = getPVList(layoutName);
	if(pvId<list.length)
	{
		var defaultValue = getPVDefaultValue(layoutName);
		setPVDefaultValue(layoutName, list[pvId]);
	}
	
}
function onPVListRenderList(layoutName)
{
	var div = document.getElementById("divPVListPossibleValuesLayout"+layoutName);
	var list = getPVList(layoutName);
	var defaultValue = getPVDefaultValue(layoutName);
	if(list.length>0)
	{
		//Rendering the possible values table
		var str = "";
		str+="<table border=\"0\" cellspacing=\"5\">";
		str+="<tr>";
		str+="<td>Default</td>";
		str+="<td>Name</td>";
		str+="<td></td>";
		str+="</tr>";
		
		for(var i=0;i<list.length;i++)
		{
			str+="<tr>";
			str+="<td>";
			str+="<input type=\"radio\" id=\"pv_list_radio_default_value"+layoutName+"_"+i+"\" name=\"defaultValueId\" onclick=\"onPVListChooseDefaultValue('"+layoutName+"', "+i+");\" ";
			if(defaultValue==list[i])
			{
				str+="checked=\"checked\" ";
			}
			str+="value=\""+i+"\"/>";
			str+="</td>";
			str+="<td>";
			str+="<input type=\"text\" name=\"possibleValue"+i+"\" value=\""+escapeHTML(list[i])+"\"/>";
			str+="</td>";
			str+="<td>";
			str+="<a href=\"javascript:onPVListRemovePossibleValue('"+layoutName+"',"+i+");\">Remove</a>";
			str+="</td>";
			str+="</tr>";
		}
		str+="</table>";
		str+="<input type=\"hidden\" name=\"possibleValuesCount\" value=\""+list.length+"\"/>";
		div.innerHTML = str;
	}
	else div.innerHTML = "There are no possible values provided yet";
}



function showTooltip(event, webElement, id)
{
	var div = document.getElementById(id);
	
	var pos = getMouseXY(event);
	div.style.left = pos.x+10;
	div.style.top = pos.y+10;
	if(div.style.display!="block")div.style.display = "block";
}
function hideTooltip(id)
{
	var div = document.getElementById(id);
	div.style.display = "none";
}


var Tab = {
	
	switchTab: function(tabId, tabLayoutId, inactiveTabIds, inactiveLayoutIds){
		for(var i = 0; i<inactiveTabIds.length; i++){
			var element = document.getElementById(inactiveTabIds[i]);
			if(element!=null){
				element.className="custom-tab";
			}
		}
		for(var i = 0; i<inactiveLayoutIds.length; i++){
			var element = document.getElementById(inactiveLayoutIds[i]);
			if(element!=null){
				element.style.display="none";
			}
		}
	
		var element = document.getElementById(tabId);
		if(element!=null){
			element.className="custom-tab-selected";
		}
		
		element = document.getElementById(tabLayoutId);
		if(element!=null){
			element.style.display="block";
		}
	},
	
	renameTab: function(tabId, tabName){
		var link = document.getElementById(tabId+"ActiveLink");
		if(link!=null){
			link.innerHTML = tabName;
		}
		link = document.getElementById(tabId+"Header");
		if(link!=null){
			link.innerHTML = tabName;
		}
	},
	
	disableTab: function(tabId, layoutId){
		var element = document.getElementById(tabId);
		if(element!=null){
			element.style.display="block";
		}
	},
	
	enableTab: function(tabId, layoutId){
		var element = document.getElementById(tabId);
		if(element!=null){
			element.style.display="none";
		}
		
		element = document.getElementById(tabLayoutId);
		if(element!=null){
			element.style.display="none";
		}
	}
};


//Used in test-parameteres-editor
var TestParametersListEditor = {
	id: "test-parameters-list-editor",
	_uniqueId:0,
	uniqueId: function () {
		this._uniqueId++;
		return this._uniqueId;
	},
	getDefaultValue: function () {
		var id = $("." + this.id + "-pv-default:checked").val();
		
		if ( id != null && id != "") {
			return $("." + this.id + "-pv-name[x-value-id=" + id + "]").val();
		}
		return "";
	},
	getValuesList: function () {
		var valuesList = [];
		$("." + this.id + "-pv-name").each(function () {
			valuesList.push($(this).val());
		});
		return valuesList;
	},
	_tableBodyLocator: null,
	addValue: function(value) {
		var id = this.uniqueId();
		var h = "";
		h += "<tr x-id='" + id + "'>";
		h += "<td class='border'><input type='radio' class='" + this.id + "-pv-default' name='" + this.id + "-pv-default' value='" + id + "'/></td>";
		h += "<td class='border'><input type='text' class='" + this.id + "-pv-name' value='" + escapeHTML(value) + "' x-value-id='" + id + "'/></td>";
		h += "<td><a class='" + this.id + "-remove-link' x-value-id='" + id + "'>Remove</a></td>";
		h += "</tr>";
		$(this._tableBodyLocator).append(h);
		$("a." + this.id+"-remove-link[x-value-id=" + id + "]").click(function () {
			var id = $(this).attr("x-value-id");
			$(TestParametersListEditor._tableBodyLocator+" tr[x-id=" + id + "]").remove();
		});
		return id;
	},
	_controlsInit: false,
	init: function(tableBodyLocator, valuesList, defaultValue) {
		if ( !this._controlsInit ) {
			$("#possible-value-list-add-text").val("");
			$("#possible-value-list-add-submit").button();
		    $("#possible-value-list-add-submit").click(function () {
		        var value = $("#possible-value-list-add-text").val();
		        TestParametersListEditor.addValue(value);
		        $("#possible-value-list-add-text").val("");
		        return false;
		    });
		    this._controlsInit = true;
		}
	    
		this._uniqueId = 0;
		this._tableBodyLocator = tableBodyLocator;
		$(tableBodyLocator).html("");
		
		var selectedId = null;
		for ( var i=0; i < valuesList.length; i++ ) {
			var id = this.addValue(valuesList[i]);
			if ( valuesList[i] == defaultValue ) {
				selectedId = id;
			}
		}
		if ( selectedId != null ) {
			$("." + this.id + "-pv-default[value=" + selectedId + "]").click();
		}
	}
};

