var dhxWins, w1, customers, products;
function initWindow() {
	dhxWins = new dhtmlXWindows();
	// dhxWins.attachViewportTo("container");
	w1 = dhxWins.createWindow("w1", 20, 30, 450, 550);
	w1.attachObject("newWindow");
	w1.setText("Add User");
	// w1.setSkin("dhx_terrace");
	w1.button("close").disable();
	w1.button("minmax").disable();
	w1.denyResize();

}
function populateTable() {
	loadSelectBoxes();

	$('#table').bootstrapTable({
		url : '/admin/orders/load',
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
function loadSelectBoxes() {
	dhtmlx.ajax("/admin/references/load", function(returnData) {
		products = $.parseJSON(returnData);
		$.each(products, function(i, d) {
			$('#productType').append(
					'<option value="' + d.id + '">' + d.name + '</option>');
		});
	});
	dhtmlx.ajax("/admin/customers/load", function(returnData) {
		customers = $.parseJSON(returnData);
		$.each(customers, function(i, d) {
			$('#customerId').append(
					'<option value="' + d.id + '">' + d.name + '</option>');
		});
	});
}
function showWindow(selectedMode) {
	if (selectedMode == 'edit') {
		if (getSelected() == null) {
			displayMessage('Please select a record to edit');
			return;
		} else {
			setValues();
		}
	}
	$('#addBtn').prop('disabled', true);
	$('#editBtn').prop('disabled', true);
	$('#deleteBtn').prop('disabled', true);

	if (dhxWins == null || w1 == null)
		initWindow();
	document.getElementById("customerId").focus();
	dhxWins.window('w1').center();
	dhxWins.window('w1').show();
	mode = selectedMode;

}

function hideWindow() {
	cleanForm();
	dhxWins.window('w1').hide();
	$('#addBtn').prop('disabled', false);
	$('#editBtn').prop('disabled', false);
	$('#deleteBtn').prop('disabled', false);
}

function saveRecord() {
	if (mode == 'add') {
		var data = {
			'customerId' : $("#customerId").val(),
			'orderNumber' : $("#orderNumber").val(),
			'jobNumber' : $("#jobNumber").val(),
			'designId' : $("#designId").val(),
			'new' : $("#isNew").val() == 'Y' ? true : false,
			'width' : $("#width").val(),
			'depth' : $("#depth").val(),
			'height' : $("#height").val(),
			'quantity' : $("#quantity").val(),
			'productType' : $("#productType").val(),
			'displayStartDate' : $("#displayStartDate").val()
		}
	} else {
		var data = {
			'id' : getSelected().id,
			'customerId' : $("#customerId").val(),
			'orderNumber' : $("#orderNumber").val(),
			'jobNumber' : $("#jobNumber").val(),
			'designId' : $("#designId").val(),
			'new' : $("#isNew").val() == 'Y' ? true : false,
			'width' : $("#width").val(),
			'depth' : $("#depth").val(),
			'height' : $("#height").val(),
			'quantity' : $("#quantity").val(),
			'productType' : $("#productType").val(),
			'displayStartDate' : $("#displayStartDate").val()
		}
	}
	saveOrUpdate(data);
}
function saveOrUpdate(data) {
	preSubmit();
	dhtmlx.ajax().post("/admin/orders/save", data, function(returnData) {
		var res = $.parseJSON(returnData);
		if (res.messageType == 'SUCCESS') {
			hideWindow();
			displayMessage(res.message);
			var savedOrder = res.object;
			$('#table').bootstrapTable('refresh');
		} else {
			displayError(res.message);
		}
	});
	postSubmit();
}
function cleanForm() {
	document.getElementById("customerId").value = "";
	document.getElementById("orderNumber").value = "";
	document.getElementById("jobNumber").value = "";
	document.getElementById("designId").value = "";
	document.getElementById("isNew").value = "Y";
	document.getElementById("width").value = "";
	document.getElementById("depth").value = "";
	document.getElementById("height").value = "";
	document.getElementById("quantity").value = "";
	document.getElementById("productType").value = "";
	document.getElementById("displayStartDate").value = "";
}
function preSubmit() {
	$('#saveBtn').prop('disabled', true);
	$('#cancelBtn').prop('disabled', true);
}
function postSubmit() {
	$('#saveBtn').prop('disabled', false);
	$('#cancelBtn').prop('disabled', false);
}
function displayMessage(res) {
	alert(res);
}
function displayError(res) {
	alert(res);
}
function getSelected() {
	var selectedRecords = $('#table').bootstrapTable('getSelections')
	if (selectedRecords != 'undefined' && selectedRecords.length == 1) {
		return selectedRecords[0];
	} else {
		return null;
	}

}
function setValues() {
	var selected = getSelected();
	document.getElementById("customerId").value = selected.customerId;
	document.getElementById("orderNumber").value = selected.orderNumber;
	document.getElementById("jobNumber").value = selected.jobNumber;
	document.getElementById("designId").value = selected.designId;
	document.getElementById("isNew").value = (selected.new == true) ? "Y"
			: "N";
	document.getElementById("width").value = selected.width;
	document.getElementById("depth").value = selected.depth;
	document.getElementById("height").value = selected.height;
	document.getElementById("quantity").value = selected.quantity;
	document.getElementById("productType").value = selected.productType;
	document.getElementById("displayStartDate").value = selected.displayStartDate;
}
function deleteRecord() {
	var selected = getSelected();
	if (selected == null) {
		displayMessage('Please select a record to delete');
		return;
	} else {
		var data = {
			'id' : selected.id,
			'orderNumber' : selected.orderNumber,
			'customerId' : selected.customerId,
			'orderNumber' : selected.orderNumber,
			'jobNumber' : selected.jobNumber,
			'designId' : selected.designId,
			'width' : selected.width,
			'depth' : selected.depth,
			'height' : selected.height,
			'quantity' : selected.quantity,
			'productType' : selected.productType,
			'displayStartDate' : selected.displayStartDate

		}
		dhtmlx.ajax().post("/admin/orders/delete/", data, function(returnData) {
			var res = $.parseJSON(returnData);
			if (res.messageType == 'SUCCESS') {
				displayMessage(res.message);
				$('#table').bootstrapTable('refresh');
			} else {
				displayError(res.message);
			}
		});
	}

}
function showEvents() {
	var selected = getSelected();
	if (selected == null) {
		displayMessage('Please select a record to proceed');
		return;
	} else {
		window.open("/admin/schedule?q=" + selected.id);
	}
}