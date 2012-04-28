<%@ include file="/include.jsp"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<script src="../scripts/trmdragndrop.js"></script>
<script>
	var currentState = 0;
	var tree = null;
	//this variable stores all tests returned in response in the test tree
	var tests = [];

	Array.prototype.remove = function(from, to) {
		var rest = this.slice((to || from) + 1 || this.length);
		this.length = from < 0 ? this.length + from : from;
		return this.push.apply(this, rest);
	};
	var checkedTests = new Array();

	function updateAddTestsLayout() {
	}
	function changeSelection(id, state) {
		var i = 0;
		var foundId = -1;
		for (i = 0; i < checkedTests.length; i++) {
			if (checkedTests[i] == id) {
				foundId = i;
				if (state == 0) {
					//Removing from selection
					checkedTests.remove(i);
					updateAddTestsLayout();
					return;
				}
			}
		}
		if (state == 1 && foundId < 0) {
			var i = checkedTests.length;
			checkedTests[i] = id;
		}
		updateAddTestsLayout();
	}
	function changeSelections(ids, state) {
		var i = 0;
		for (i = 0; i < ids.length; i++) {

			var input = document.getElementById("chkTest_" + ids[i]);
			input.checked = state;
			changeSelection(ids[i], state);
		}
	}
	function blockTree() {
		var div1 = document.getElementById("divTreeLoadingIcon");
		div1.style.display = "block";
	}
	function unblockTree() {
		var div1 = document.getElementById("divTreeLoadingIcon");
		div1.style.display = "none";
	}
	function onAjaxTestIdsFetchResponse(loader) {
		var str = loader.xmlDoc.responseText;
		var obj = eval("(" + str + ")");

		if (obj.result == "fetched") {
			changeSelections(obj.object, currentState);
		} else
			alert(str);

		unblockTree();
	}

	function createParameter(name, value) {
		var param = new Object();
		param.name = name;
		param.value = value;
		return param;
	}
	/**
	 * Loading the tests into the divTestList layout with specified filter 
	 */
	function loadTestList(page) {
		checkedTests = [];
		updateAddTestsLayout();
		if (page == null)
			page = 1;
		blockTree();
		var ps = new Array();
		var form = document.forms.searchFilter;

		for ( var i = 0; i < form.elements.length; i++) {
			if (form.elements[i].name != null
					&& form.elements[i].name.length > 0
					&& form.elements[i].name != ''
					&& form.elements[i].value != null
					&& form.elements[i].value.length > 0
					&& form.elements[i].value != '') {
				if (form.elements[i].type == "checkbox") {
					if (form.elements[i].checked) {
						var ename = form.elements[i].name;
						if (ename.indexOf("_pv_") == -1) {
							ps[ps.length] = createParameter(
									form.elements[i].name, "true");
						} else {
							ps[ps.length] = createParameter(
									form.elements[i].name, "on");
						}
					}
				} else {
					ps[ps.length] = createParameter(form.elements[i].name,
							form.elements[i].value);
				}
			}
		}

		ps[ps.length] = createParameter("page", page);

		var str = "automated=1";
		
		for ( var i = 0; i < ps.length; i++) {
			str += "&" + ps[i].name + "=" + escape(ps[i].value);
		}

		dhtmlxAjax.post("../test/ajax-search", str, onAjaxTestSearchResponse);
	}
	function onAjaxTestSearchResponse(loader) {
		var str = loader.xmlDoc.responseText;
		var obj = eval("(" + str + ")");

		if (obj.result != "error") {
			renderSearchResult(obj.object);
		} else
			alert(obj.error);
	}
	function onPageClick(page) {
		loadTestList(page);
	}
	function renderSearchResult(result) {
		tests = [];
		unblockTree();
		createPaginationBlock(document.getElementById("divPagination"),
				result.numberOfResults, result.page, result.limit,
				"onPageClick");

		var div = document.getElementById("divTestList");
		var html = "";
		html += "Number of results: <b>" + result.numberOfResults + "</b>";
		html += "<ul class='test-search-list-root'>";
		for ( var i = 0; i < result.testProjects.length; i++) {
			var project = result.testProjects[i];
			var projectTestIds = "[";
			var bComma = false;

			for ( var j = 0; j < project.testGroups.length; j++) {
				var testGroup = project.testGroups[j];
				for ( var k = 0; k < testGroup.tests.length; k++) {
					if (bComma)
						projectTestIds += ",";
					bComma = true;
					projectTestIds += testGroup.tests[k].id;
				}
			}
			projectTestIds += "]";

			html += "<li><span class='test-search-list-project'>";
			html += "<input id='chkTestProject_" + i
					+ "' type='checkbox' onchange='changeSelections("
					+ projectTestIds + ",this.checked);'/>";
			html += "<img src='../images/workflow-icon-project.png'/> ";
			html += escapeHTML(project.name);
			html += "</span>";
			html += "<ul>";

			for ( var j = 0; j < project.testGroups.length; j++) {
				var testGroup = project.testGroups[j];
				var testGroupTestIds = "[";
				bComma = false;
				for ( var k = 0; k < testGroup.tests.length; k++) {
					if (bComma)
						testGroupTestIds += ",";
					bComma = true;
					testGroupTestIds += testGroup.tests[k].id;
				}
				testGroupTestIds += "]";

				html += "<li><span class='test-search-list-group'>";
				html += "<input id='chkTestGroup_" + j
						+ "' type='checkbox' onchange='changeSelections("
						+ testGroupTestIds + ",this.checked);'/>";
				html += "<img src='../images/workflow-icon-test-group.png'/> ";
				if (testGroup.name != null) {
					html += escapeHTML(testGroup.name);
				} else
					html += "<span style='color:gray;'>Ungrouped</span>";

				html += "</span>";
				html += "<ul>";
				for ( var k = 0; k < testGroup.tests.length; k++) {
					tests[tests.length] = testGroup.tests[k];
					html += "<li>";
					html += "<div onMouseDown='onTestInTreeMouseDown("
							+ testGroup.tests[k].id + ",this); return false;'>";
					html += "<input id='chkTest_" + testGroup.tests[k].id
							+ "' type='checkbox' onchange='changeSelection("
							+ testGroup.tests[k].id + ",this.checked);'/>";
					//html+="<a href='../test/display?id="+testGroup.tests[k].id+"' target='_blank' onMouseMove='if(dragObject==null)showTooltip(event,this,\"testTooltip"+testGroup.tests[k].id+"\");' onMouseOut='hideTooltip(\"testTooltip"+testGroup.tests[k].id+"\");'>";
					html += "<a href='../test/display?id="
							+ testGroup.tests[k].id + "' target='_blank'>";
					html += "<img src='../images/workflow-icon-test.png'/>";
					html += escapeHTML(testGroup.tests[k].name);
					html += "</a>";
					html += "</div>";
					html += "<div id='testTooltip"+testGroup.tests[k].id+"' style='display:none;' class='tooltip'><pre>"
							+ escapeHTML(testGroup.tests[k].description)
							+ "</pre></div>";
					html += "</li>";
				}

				html += "</ul>";
				html += "</li>";
			}

			html += "</ul>";
			html += "</li>";
		}
		html += "</ul>";

		div.innerHTML = html;
	}
	function findTestInTree(testId) {
		for ( var i = 0; i < tests.length; i++) {
			if (tests[i].id == testId)
				return tests[i];
		}
	}
	function hideTestDetails() {

	}

	function showTestDetails(testId) {
		var div = document.getElementById("divTestDetailsContent");
		var str = "";
		var test = findTestInTree(testId);

		var desc = test.description;
		if (desc == null)
			desc = "";
		str += "<b>" + test.name + "</b><br/><br/>"
				+ desc.replace(/\n/g, '<br />');
		str += "<br/><br/>";
		div.innerHTML = str;
		showPopup("divTestDetailsPopup", 400, 500);
	}
</script>

<div id="brickDragContainer" class="drag-container"
	style="display: none;"></div>

<div id="divTestDetailsPopup"
	style="position: absolute; display: none; width: 400px; height: 500px;">
	<tag:panel title="Test Details" align="center"
		closeDivName="divTestDetailsPopup" width="400px" height="500px">
		<div id="divTestDetailsContent" style="width: 100%;"></div>
	</tag:panel>
</div>

<div id="treeNavigationPanel"
	style="height: 300px; width: 240px; overflow: auto; background: white; border: 1px solid #aaa; padding: 4px; margin: 0px;">
	<div id="divTreeLoadingIcon" class="tree-box-loading"
		style="display: none; width: 250px;">
		<img src="../images/loading.gif" /> Loading...
	</div>
	<div id="divPagination"></div>
	<div id="divTestList" class="test-search-list"
		style="width: 100%; height: 100%;"></div>
</div>
<script>
	$(document).ready(function() {
		$("#treeNavigationPanel").stickySidebar({
			speed : 0,
			padding : 30,
			constrain : true
		});
		$(window).bind('resize', function() {
			$("#treeNavigationPanel").height($(window).height() - 115);
		}).trigger('resize');
	});
</script>