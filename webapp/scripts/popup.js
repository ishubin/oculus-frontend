var _currentPopupDiv = null;


window.size = function()
{
	var w = 0;
	var h = 0;

	//IE
	if(!window.innerWidth)
	{
		//strict mode
		if(!(document.documentElement.clientWidth == 0))
		{
			w = document.documentElement.clientWidth;
			h = document.documentElement.clientHeight;
		}
		//quirks mode
		else
		{
			w = document.body.clientWidth;
			h = document.body.clientHeight;
		}
	}
	//w3c
	else
	{
		w = window.innerWidth;
		h = window.innerHeight;
	}
	return {width:w,height:h};
}

window.center = function()
{
	var hWnd = (arguments[0] != null) ? arguments[0] : {width:0,height:0};

	var _x = 0;
	var _y = 0;
	var offsetX = 0;
	var offsetY = 0;

	//IE
	if(!window.pageYOffset)
	{
		//strict mode
		if(!(document.documentElement.scrollTop == 0))
		{
			offsetY = document.documentElement.scrollTop;
			offsetX = document.documentElement.scrollLeft;
		}
		//quirks mode
		else
		{
			offsetY = document.body.scrollTop;
			offsetX = document.body.scrollLeft;
		}
	}
	//w3c
	else
	{
		offsetX = window.pageXOffset;
		offsetY = window.pageYOffset;
	}

	_x = ((this.size().width-hWnd.width)/2)+offsetX;
	_y = ((this.size().height-hWnd.height)/2)+offsetY;

	return{x:_x,y:_y};
}
window.offset = function()
{
	var hWnd = (arguments[0] != null) ? arguments[0] : {width:0,height:0};

	var _x = 0;
	var _y = 0;
	var offsetX = 0;
	var offsetY = 0;

	//IE
	if(!window.pageYOffset)
	{
		//strict mode
		if(!(document.documentElement.scrollTop == 0))
		{
			offsetY = document.documentElement.scrollTop;
			offsetX = document.documentElement.scrollLeft;
		}
		//quirks mode
		else
		{
			offsetY = document.body.scrollTop;
			offsetX = document.body.scrollLeft;
		}
	}
	//w3c
	else
	{
		offsetX = window.pageXOffset;
		offsetY = window.pageYOffset;
	}

	_x = offsetX;
	_y = offsetY;

	return{x:_x,y:_y};
}

function showPopup(divName, width,height, zIndex)
{
	if(zIndex==null) zIndex = 999;
	var div = document.getElementById(divName);
	if(div)
	{
		_currentPopupDiv = div;
		
		div.style.position = "absolute";
		div.style.width = width+"px";
		div.style.height = height+"px";
		
		div.style.zIndex = zIndex;
		
		var center = window.center();
		
		var x = center.x - width/2;
		var y = center.y - height/2;
		if(y<10) y = 10;
		if(x<10) x = 10;
		
		div.style.left = x;
		div.style.top = y;
		//$(div).fadeIn('fast');
		$(div).slideDown('fast');
	}
	
	var divShadow = document.getElementById("divShadow");
	if(divShadow != null)
	{
		$(divShadow).fadeIn('fast');
	}
}
function closePopup(divName)
{
	var div = document.getElementById(divName);
	$(div).slideUp('fast');

	var divShadow = document.getElementById("divShadow");
	if(divShadow != null){
		$(divShadow).fadeOut('fast');
	}
}
function closeCurrentPopup()
{
	if(_currentPopupDiv)
	{
		_currentPopupDiv.style.display = "none";
	}
}


function getElementPosition(element)
{
	x=0;
	y=0;
	if(element)
	{
		obj=element;
		if(obj.offsetParent)
		{
			do{
				x+=obj.offsetLeft;
				y+=obj.offsetTop;
			}while(obj=obj.offsetParent)
		}
	}
	return [x,y];
}


