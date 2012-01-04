function mouseCoords(ev){
	if(ev.pageX || ev.pageY){
		return {x:ev.pageX, y:ev.pageY};
	}
	return {
		x:ev.clientX + document.body.scrollLeft - document.body.clientLeft,
		y:ev.clientY + document.body.scrollTop  - document.body.clientTop
	};
}

document.onmousemove = function(ev){
	ev           = ev || window.event;
	this.mousePos = mouseCoords(ev);
	
	if(dragObject!=null)
	{
		//rendering the object dragging
		
		dragObject.style.display = "block";
		dragObject.style.zIndex = "999";
		dragObject.style.left = document.mousePos.x+10;
		dragObject.style.top = document.mousePos.y+5;
		return false;
	}
	return true;
};

var dragObject     = null;
//checkes whether it is a drag-n-grop within the same area
var dragObjectIsAdded = false;
var positionLevelFrom = -1;
var positionLevelTo = -1;

document.onmouseup = function (ev){
	if(dragObject!=null)
	{
		dragObject.style.display = "none";
	}
	dragObject = null;
};

var testsToDrag = [];

function onTestInTreeMouseDown(testId, div){
	//Checking if the user has selected more than one test
	var str = "";
	testsToDrag = [];
	
	if(checkedTests.length>0)
	{
		
		for(var i=0;i<checkedTests.length;i++)
	    {
			var test = findTestInTree(checkedTests[i]);
			if(i<4)
			{
				str+="<div class=\"drag-brick drag-brick-level-"+(i+1)+"\"><img src=\"../images/iconTest.png\"/>"+escapeHTML(test.name)+"</div>";
			}
			
	        testsToDrag[testsToDrag.length] = test;
	    }
	}
	else
	{
		var test = findTestInTree(testId);
		str+="<div class=\"drag-brick\"><img src=\"../images/iconTest.png\"/>"+escapeHTML(test.name)+"</div>";
		testsToDrag[testsToDrag.length] = test;
	}
	
	var divContainer = document.getElementById("brickDragContainer");
	divContainer.innerHTML = str;
	dragObject = divContainer;
	dragObjectIsAdded = false;
}

function onAddedBrickMouseDown(div, testCustomId,  posLevelFrom){
	testsToDrag = [];
	positionLevelFrom = posLevelFrom;
	dragObjectIsAdded = true;
	var divContainer = document.getElementById("brickDragContainer");
	var str = "";
	var test = findTestByCustomId(testCustomId);
	str+="<div class=\"drag-brick drag-brick-level-1\"><img src=\"../images/iconTest.png\"/>"+escapeHTML(test.name)+"</div>";
	divContainer.innerHTML = str;
	dragObject = divContainer;
}

function onDropAreaMouseOver(dropArea, isBig){
	if(dragObject!=null)
	{
		if(isBig)
		{
			dropArea.className = "dropArea-big-highlighted";
		}
		else  dropArea.className = "dropArea-highlighted";
	}
}
function onDropAreaMouseOut(dropArea, isBig){
	if(dragObject!=null)
	{
		if(isBig)
		{
			dropArea.className = "dropArea-big";
		}
		else  dropArea.className = "dropArea";
	}
}
function onDropAreaMouseUp(dropArea, positionLevel, isBig){
	if(dragObject!=null)
	{
		if(isBig)
		{
			dropArea.className = "dropArea-big";
		}
		else dropArea.className = "dropArea";
		
		var divContainer = document.getElementById("brickDragContainer");
		if(dragObjectIsAdded)
		{
			if(positionLevelFrom!=positionLevel)
			{
				//alert("You want to move it from position "+positionLevelFrom+" to position "+positionLevel);
				if(positionLevel=='last')
				{
					positionLevel = myTests.length;
				}
				if(positionLevel>positionLevelFrom)
				{
					positionLevel--;
				}
				
				gatherAllParameterValues();
				var testArr = myTests.splice(positionLevelFrom,1);
				myTests.splice(positionLevel,0,testArr[0]);
				rerenderTests();
			}
		}
		else 
		{
			positionLevelTo = positionLevel;
			var str = "";
            for(var i=0;i<testsToDrag.length;i++)
            {
                if(i>0)str+=",";
                str+=testsToDrag[i].id;
            }
            dhtmlxAjax.post("../test/ajax-fetch", "ids="+str, onAjaxTestFetchResponse);
		}
	}
}