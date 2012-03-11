var Report = {
	showIcons: true,
	showDebug: false
};


function findReportNodeById(id, rootNode) {
	if (rootNode == null) {
		rootNode = report;
	}
	if (rootNode.id == id)
		return rootNode;

	if( rootNode.childNodes != null) {
		for ( var i = 0; i < rootNode.childNodes.length; i++) {
			var node = findReportNodeById(id, rootNode.childNodes[i]);
			if (node != null)
				return node;
		}
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

	if (node.metaData.hasError == true && node.level == 'info' && div != null) {
		$("#reportNode"+id).animate({backgroundColor:"#ffdddd"},100);
	}
	
	if (node.metaData.hasWarn && node.level == 'info' && div != null) {
		$("#reportNode"+id).animate({backgroundColor:"#ffff99"},100);
	}
	
	if ((node.metaData.hasError == true || node.metaData.hasWarn == true) && node.level == 'info' && div != null) {
		$("#reportNodeStatus"+id).fadeOut('fast');
	}
	
	$("#childrenFor"+id).slideDown('fast', function(){
		if (divChildren != null)
			divChildren.style.display = "block";
		if (img != null)
			img.src = "../images/report-collapse-button.png";

	});
}
function collapseNode(node) {
	var id = node.id;
	var divChildren = document.getElementById("childrenFor" + id);
	var div = document.getElementById("reportNode" + id);
	var divStatus = document.getElementById("reportNodeStatus" + id);
	var img = document.getElementById("imgCollapse" + id);
	node.metaData.collapsed = true;

	if (node.metaData.hasError && node.level == 'info' && div != null) {
		$("#reportNode"+id).animate({backgroundColor:"#ff9999"},100);
	}
	
	if (node.metaData.hasWarn && node.level == 'info' && div != null) {
		$("#reportNode"+id).animate({backgroundColor:"#FAA91E"},100);
	}
	
	if ((node.metaData.hasError == true || node.metaData.hasWarn == true) && node.level == 'info' && div != null) {
		$("#reportNodeStatus"+id).fadeIn('fast');
	}
	
	$("#childrenFor"+id).slideUp('fast', function(){
		if (divChildren != null)
			divChildren.style.display = "none";
		if (img != null)
			img.src = "../images/report-expand-button.png";
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

	if ( node.childNodes != null) {
		for ( var i = 0; i < node.childNodes.length; i++) {
			openHierarchy(level, node.childNodes[i], n + 1);
		}
	}
}
function getMaximumLevelOfHierarchy(node, n) {
	if (node == null)
		node = report;

	if (node.childNodes != null && node.childNodes.length > 0) {
		var maxn = n;
		var tempn = n;
		for ( var i = 0; i < node.childNodes.length; i++) {
			tempn = getMaximumLevelOfHierarchy(node.childNodes[i], n + 1);
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
	if ( rootNode.childNodes != null) {
		for ( var i = 0; i < rootNode.childNodes.length; i++) {
			expandAllSteps(rootNode.childNodes[i]);
		}
	}
}

function collapseAllSteps(rootNode) {
	if (rootNode == null)
		rootNode = report;

	if (rootNode.id != report.id) {
		collapseNode(rootNode);
	}
	if ( rootNode.childNodes != null) {
		for ( var i = 0; i < rootNode.childNodes.length; i++) {
			collapseAllSteps(rootNode.childNodes[i]);
		}
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
	if ( node.childNodes != null) {
		for ( var i = 0; i < node.childNodes.length; i++) {
			expandAllErrorSteps(node.childNodes[i]);
		}
	}
}

function toggleDebug() {
	Report.showDebug = !Report.showDebug;
	
	if (Report.showDebug) {
		showDebugNodes(true);
	} else {
		showDebugNodes(false);
	}
	
	return Report.showDebug;
}

function showDebugNodes(show){
	$(".report-node-layout").each(function(){
		var debug = $(this).attr("x-debug");
		if( debug!=null && debug=='true') {
			if( show ) {
				$(this).show('slow');
			}
			else {
				$(this).hide('fast');
			}
			
		}
	});
}

function toggleIcons() {
	Report.showIcons = !Report.showIcons;
	
	if (Report.showIcons) {
		showIcons(report, true);
	} else {
		showIcons(report, false);
	}
	
	return Report.showIcons;
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
	if ( node.childNodes != null) {
		for ( var i = 0; i < node.childNodes.length; i++) {
			showIcons(node.childNodes[i], bShow);
		}
	}
}
