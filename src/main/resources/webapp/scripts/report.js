function findReportNodeById(id, rootNode) {
	if (rootNode == null) {
		rootNode = report;
	}
	if (rootNode.id == id)
		return rootNode;

	for ( var i = 0; i < rootNode.children.length; i++) {
		var node = findReportNodeById(id, rootNode.children[i]);
		if (node != null)
			return node;
	}
	return null;
}
function expandNode(node) {
	var id = node.id;
	var divChildren = document.getElementById("childrenFor" + id);
	var div = document.getElementById("reportNode" + id);
	var divStatus = document.getElementById("reportNodeStatus" + id);
	var img = document.getElementById("imgCollapse" + id);
	node.metaData.collapsed = false;

	if (node.metaData.hasError && node.type == 'info' && div != null) {
		div.style.backgroundColor = "#ff9999";
		$("#reportNode"+id).animate({backgroundColor:"#ffdddd"},100);
	}
	
	$("#childrenFor"+id).slideDown('fast', function(){
		if (divChildren != null)
			divChildren.style.display = "block";
		if (img != null)
			img.src = "../images/report-collapse-button.png";

		if (node.metaData.hasError && node.type == 'info' && div != null) {
			divStatus.style.display = "none";
		} else if (node.metaData.hasWarn && node.type == 'info' && div != null) {
			div.style.background = "#ffffdd";
			divStatus.style.display = "none";
		}
		
	});
}
function collapseNode(node) {
	var id = node.id;
	var divChildren = document.getElementById("childrenFor" + id);
	var div = document.getElementById("reportNode" + id);
	var divStatus = document.getElementById("reportNodeStatus" + id);
	var img = document.getElementById("imgCollapse" + id);
	node.metaData.collapsed = true;

	if (node.metaData.hasError && node.type == 'info' && div != null) {
		div.style.backgroundColor = "#ffdddd";
		$("#reportNode"+id).animate({backgroundColor:"#ff9999"},100);
	}
	
	$("#childrenFor"+id).slideUp('fast', function(){
		if (divChildren != null)
			divChildren.style.display = "none";
		if (img != null)
			img.src = "../images/report-expand-button.png";
	
		if (node.metaData.hasError && node.type == 'info') {
			div.style.background = "#ff9999";
			divStatus.style.display = "block";
		} else if (node.metaData.hasWarn && node.type == 'info') {
			div.style.background = "#ffff99";
			divStatus.style.display = "block";
		}
	});
}
function onReportNodeClick(id) {
	var node = findReportNodeById(id);
	if (node.metaData.collapsed) {
		expandNode(node);
	} else {
		collapseNode(node);
	}

}
function initializeReportNodes() {
	expandAllErrorSteps(report);
}
function initializeMenuLevels() {
	var max = getMaximumLevelOfHierarchy(report, 0) - 1;

	if (max > 1) {
		var html = "";
		for ( var i = 1; i < max && i < 9; i++) {
			html += "<a class=\"menu-item\" href=\"javascript:openHierarchy("
					+ i
					+ ", report, 0);\"><img src=\"../images/report-open-level-"
					+ (i + 1) + ".png\"/><a/>";
		}

		document.getElementById("divMenuExpandLevels").innerHTML = html;
	}
}
function openHierarchy(level, node, n) {
	if (n <= level) {
		expandNode(node);
	} else
		collapseNode(node);

	for ( var i = 0; i < node.children.length; i++) {
		openHierarchy(level, node.children[i], n + 1);
	}
}
function getMaximumLevelOfHierarchy(node, n) {
	if (node == null)
		node = report;

	if (node.children != null && node.children.length > 0) {
		var maxn = n;
		var tempn = n;
		for ( var i = 0; i < node.children.length; i++) {
			tempn = getMaximumLevelOfHierarchy(node.children[i], n + 1);
			if (maxn < tempn)
				maxn = tempn;
		}
		return maxn;
	} else
		return n;
}

function expandAllSteps(rootNode) {
	if (rootNode == null)
		rootNode = report;

	if (rootNode.id != report.id) {
		expandNode(rootNode);
	}
	for ( var i = 0; i < rootNode.children.length; i++) {
		expandAllSteps(rootNode.children[i]);
	}
}

function collapseAllSteps(rootNode) {
	if (rootNode == null)
		rootNode = report;

	if (rootNode.id != report.id) {
		collapseNode(rootNode);
	}
	for ( var i = 0; i < rootNode.children.length; i++) {
		collapseAllSteps(rootNode.children[i]);
	}
}

function expandAllErrorSteps(node) {
	if (node == null)
		node = report;

	if (node.id != report.id) {
		if (node.metaData.hasError || node.metaData.hasWarn) {
			expandNode(node);
		} else {
			collapseNode(node);
		}
	}
	for ( var i = 0; i < node.children.length; i++) {
		expandAllErrorSteps(node.children[i]);
	}
}

function onShowIconsChange(checkbox) {
	if (checkbox.checked) {
		showIcons(report, true);
	} else
		showIcons(report, false);
}
function showIcons(node, bShow) {
	if (node.id != report.id) {
		var divIcon = document.getElementById("reportNodeIcon" + node.id);
		if (divIcon != null) {
			if (bShow) {
				divIcon.style.display = "block";
			} else
				divIcon.style.display = "none";
		}
	}
	for ( var i = 0; i < node.children.length; i++) {
		showIcons(node.children[i], bShow);
	}
}
