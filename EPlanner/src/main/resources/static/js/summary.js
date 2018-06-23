var myTabbar;
var phases = new Array();
function loadData() {
	$.ajax({
		type : 'GET',
		url : "/admin/phases/names",
		success : function(res) {
			phases = res;
		},
		async : false
	});
}
function init() {
	loadData();
	myTabbar = new dhtmlXTabBar("my_tabbar");
	myTabbar.attachEvent("onTabClick", function(id, lastId) {
		myTabbar.tabs(lastId).detachObject("table");
		myTabbar.tabs(id).attachObject("table");
		// TODO
	});
	for (var i = 0; i < phases.length; i++) {
		if (i == 0) {
			myTabbar.addTab("tab_" + phases[0], phases[0], null, null, true);
			myTabbar.tabs("tab_" + phases[0]).attachObject("table");
		} else {
			myTabbar.addTab("tab_" + phases[i], phases[i]);
		}
	}
}
function populateTable() {
	$('#table').bootstrapTable({
		url : '/admin/summary/load',
		columns : [ {
			field : 'state',
			title : 'Select',
			radio : true
		}, {
			field : 'id',
			title : 'Id',
			align : 'center',
			valign : 'middle',
			visible : false,
			searchable : false

		}, {
			field : 'customerName',
			title : 'Customer',
			align : 'center',
			valign : 'middle',
			sortable : true

		}, {
			field : 'orderNumber',
			title : 'Purchase Order Number',
			align : 'center',
			valign : 'middle',
			sortable : true

		}, {
			field : 'jobNumber',
			title : 'Job Number',
			align : 'center',
			valign : 'middle',
			sortable : true

		}, {
			field : 'designId',
			title : 'Design Id',
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
