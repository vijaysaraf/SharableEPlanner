var myTabbar;
var phases = new Array();
var allEvents = new Array();
function loadData() {
	$.ajax({ type : 'GET', url : "/admin/phases/names",	success : function(res) { phases = res;	}, async : false	});
}
function init() {
	loadData();
	/*myTabbar = new dhtmlXTabBar("my_tabbar");
	myTabbar.attachEvent("onTabClick", function(id, lastId) {
		alert(id);
		myTabbar.tabs(lastId).detachObject("schedulerDiv");
		myTabbar.tabs(id).attachObject("schedulerDiv");
		scheduler.clearAll();
		var phaseName = id.substring(id.indexOf("_") + 1, id.length);
		var tempEvent = getEvents(phaseName);
		scheduler.parse(tempEvent, "json");
		scheduler.setCurrentView(tempEvent[0].start_date);
	});
	for (var i = 0; i < phases.length; i++) {
		if (i == 0) {
			myTabbar.addTab("tab_" + phases[0], phases[0], null, null, true);
			myTabbar.tabs("tab_" + phases[0]).attachObject("table");
		} else {
			myTabbar.addTab("tab_" + phases[i], phases[i]);
		}
	}*/
	
}
function populateTable() {
	init();
	$('#table').bootstrapTable({
		url : '/admin/summary/load',
		columns : [ {
			field : 'state',
			title : 'Select',
			radio : true
		},{
			field : 'phaseName',
			title : 'Phase',
			align : 'center',
			valign : 'middle',
			visible : true,
			searchable : true,
			sortable : true

		}, {
			field : 'id',
			title : 'Id',
			align : 'center',
			valign : 'middle',
			visible : false,
			searchable : false

		}, {
			field : 'orderNumber',
			title : 'PO Number',
			align : 'center',
			valign : 'middle',
			sortable : true

		}, {
			field : 'jobNumber',
			title : 'Job',
			align : 'center',
			valign : 'middle',
			sortable : true

		}, {
			field : 'designId',
			title : 'Design',
			align : 'center',
			valign : 'middle',
			sortable : true

		}, {
			field : 'new',
			title : 'Is New ?',
			align : 'center',
			valign : 'middle',
			sortable : true,
			formatter : function(value, row, index) {
				if (value == true)
					return "New Design";
				else
					return "Existing Design"
			}
		}, {
			field : 'width',
			title : 'Width',
			align : 'center',
			valign : 'middle'

		}, {
			field : 'depth',
			title : 'Depth',
			align : 'center',
			valign : 'middle'

		}, {
			field : 'height',
			title : 'Height',
			align : 'center',
			valign : 'middle'

		}, {
			field : 'quantity',
			title : 'Quantity',
			align : 'center',
			valign : 'middle'
		}, {
			field : 'productName',
			title : 'Product',
			align : 'center',
			valign : 'middle',
			sortable : true

		}, {
			field : 'displayStartDate',
			title : 'Start Date',
			align : 'center',
			valign : 'middle',
			sortable : true

		}, {
			field : 'displayEndDate',
			title : 'End Date',
			align : 'center',
			valign : 'middle',
			sortable : true

		}, {
			field : 'hrsRequired',
			title : 'Calculated Hrs.',
			align : 'center',
			valign : 'middle',
			sortable : true

		} ],
		striped : true,
		cache : false,
		pagination : true,
		search : true,
		showRefresh : true,
		idField : 'id',
		clickToSelect : true,
		singleSelect : true,
		buttonsAlign : 'right',
		sortable : true
	});
}
