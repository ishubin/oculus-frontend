function mouseCoords(ev) {
	if (ev.pageX || ev.pageY) {
		return {
			x : ev.pageX,
			y : ev.pageY
		};
	}
	return {
		x : ev.clientX + document.body.scrollLeft - document.body.clientLeft,
		y : ev.clientY + document.body.scrollTop - document.body.clientTop
	};
}

document.onmousemove = function(ev) {
	ev = ev || window.event;
	this.mousePos = mouseCoords(ev);

	if (_dragObject != null) {
		// rendering the object dragging

		_dragObject.style.display = "block";
		_dragObject.style.zIndex = "999";
		_dragObject.style.left = document.mousePos.x + 10;
		_dragObject.style.top = document.mousePos.y + 5;
		return false;
	}
	return true;
};

var _dragObject = null;
// checkes whether it is a drag-n-grop within the same area
var _dragObjectIsAdded = false;
var _draggedTestCustomId = -1;
var _draggedTestType = '';
var _droppedTestCustomId = -1;
var _droppedTestType = '';

document.onmouseup = function(ev) {
	if (_dragObject != null) {
		_dragObject.style.display = "none";
	}
	_dragObject = null;
};

var _testsToDrag = [];

function onTestInTreeMouseDown(testId, div) {
	// Checking if the user has selected more than one test
	var str = "";
	_testsToDrag = [];

	if (checkedTests.length > 0) {

		for ( var i = 0; i < checkedTests.length; i++) {
			var test = findTestInTree(checkedTests[i]);
			if (i < 4) {
				str += "<div class=\"drag-brick drag-brick-level-" + (i + 1)
						+ "\"><img src=\"../images/iconTest.png\"/>"
						+ escapeHTML(test.name) + "</div>";
			}

			_testsToDrag[_testsToDrag.length] = test;
		}
	} else {
		var test = findTestInTree(testId);
		str += "<div class=\"drag-brick\"><img src=\"../images/iconTest.png\"/>"
				+ escapeHTML(test.name) + "</div>";
		_testsToDrag[_testsToDrag.length] = test;
	}

	var divContainer = document.getElementById("brickDragContainer");
	divContainer.innerHTML = str;
	_dragObject = divContainer;
	_dragObjectIsAdded = false;
}

function onAddedBrickMouseDown(div, testCustomId, type) {
	_testsToDrag = [];
	_draggedTestCustomId = testCustomId;
	_draggedTestType = type;
	_dragObjectIsAdded = true;

	var divContainer = document.getElementById("brickDragContainer");
	var str = "";
	var test = findTestByCustomId(testCustomId);
	str += "<div class=\"drag-brick drag-brick-level-1\"><img src=\"../images/iconTest.png\"/>"
			+ escapeHTML(test.name) + "</div>";
	divContainer.innerHTML = str;
	_dragObject = divContainer;
}

function onDropAreaMouseOver(dropArea, isBig) {
	if (_dragObject != null) {
		if (isBig) {
			dropArea.className = "dropArea-big-highlighted";
		} else
			dropArea.className = "dropArea-highlighted";
	}
}
function onDropAreaMouseOut(dropArea, isBig) {
	if (_dragObject != null) {
		if (isBig) {
			dropArea.className = "dropArea-big";
		} else
			dropArea.className = "dropArea";
	}
}
function onDropAreaMouseUp(dropArea, type, testCustomId, isBig) {
	if (_dragObject != null) {
		if (isBig) {
			dropArea.className = "dropArea-big";
		} else
			dropArea.className = "dropArea";

		var divContainer = document.getElementById("brickDragContainer");
		if (_dragObjectIsAdded) {
			//Moving existent test in suite
			
			if(_draggedTestType=='test') {
				var positionLevelFrom = findTestIdByCustomId(_draggedTestCustomId);
				
				if(type=='last') {
					//Moving to the last position
					gatherAllParameterValues();
					var positionLevelTo = myTests.length;
					var testArr = myTests.splice(positionLevelFrom, 1);
					myTests.splice(positionLevelTo, 0, testArr[0]);
					rerenderTests();
				}
				else if(type=='test') {
					//Moving before specified test in root level
					if(testCustomId != _draggedTestCustomId) {
						gatherAllParameterValues();
						var testArr = myTests.splice(positionLevelFrom, 1);
						var positionLevelTo = findTestIdByCustomId(testCustomId);
						myTests.splice(positionLevelTo, 0, testArr[0]);
						rerenderTests();
					}
					else {
						return true;
					}
				}
				else if (type=='lastInGroup') {
					//Moving to the last position in specified testGroup
					gatherAllParameterValues();
					var testArr = myTests.splice(positionLevelFrom, 1);
					var testGroup = findTestByCustomId(testCustomId);
					testGroup.tests.splice(testGroup.tests.length,0, testArr[0]);
					rerenderTests();
				}
				else if (type=='child') {
					//Moving before specified test in testGroup
					
					gatherAllParameterValues();
					var testArr = myTests.splice(positionLevelFrom, 1);
					var testGroup = findParentForCustomId(testCustomId);
					var id = findTestIdByCustomId(testGroup.customId);
					var positionLevelTo = findTestIdByCustomId(testCustomId);
					myTests[id].tests.splice(positionLevelTo, 0, testArr[0]);
					rerenderTests();
				}
			}
			else if(_draggedTestType=='child') {
				var positionLevelFrom = findTestIdByCustomId(_draggedTestCustomId);
				var testGroupFrom = findParentForCustomId(_draggedTestCustomId);
				
				if(type=='lastInGroup') {
					var testGroupTo = findTestByCustomId(testCustomId);
					
					//Moving to the last position of specified testGroup
					gatherAllParameterValues();
					var testArr = testGroupFrom.tests.splice(positionLevelFrom, 1);
					testGroupTo.tests.splice(testGroupTo.tests.length, 0, testArr[0]);
					rerenderTests();
				}
				else if (type=='child') {
					//Moving to the last position of specified testGroup
					if(_draggedTestCustomId!=testCustomId) {
						var testGroupTo = findParentForCustomId(testCustomId);
						
						gatherAllParameterValues();
						var testArr = testGroupFrom.tests.splice(positionLevelFrom, 1);
						var positionLevelTo = findTestIdByCustomId(testCustomId);
						testGroupTo.tests.splice(positionLevelTo, 0, testArr[0]);
						rerenderTests();
					}
				}
				else if (type=='last') {
					//Moving to the last position in root level
					gatherAllParameterValues();
					var testArr = testGroupFrom.tests.splice(positionLevelFrom, 1);
					myTests.splice(myTests.length, 0, testArr[0]);
					rerenderTests();
				}
				else if (type=='test') {
					//Moving before specified test in root level
					gatherAllParameterValues();
					var testArr = testGroupFrom.tests.splice(positionLevelFrom, 1);
					var positionLevelTo = findTestIdByCustomId(testCustomId);
					myTests.splice(positionLevelTo, 0, testArr[0]);
					rerenderTests();
				}
			}
			
		} else {
			//Adding new test to suite
			
			_droppedTestCustomId = testCustomId;
			_droppedTestType = type;
			
			var str = "";
			for ( var i = 0; i < _testsToDrag.length; i++) {
				if (i > 0)
					str += ",";
				str += _testsToDrag[i].id;
			}
			showGlobalLoadingPopup();
			dhtmlxAjax.post("../test/ajax-fetch", "ids=" + str,
					onAjaxTestFetchResponse);
		}
		return false;
	}
}