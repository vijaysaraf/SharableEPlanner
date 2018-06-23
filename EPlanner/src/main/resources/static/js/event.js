var dhxWins, w1, orders, phases;
function initWindow() {
	dhxWins = new dhtmlXWindows();
	// dhxWins.attachViewportTo("container");
	w1 = dhxWins.createWindow("w1", 20, 30, 350, 450);
	w1.attachObject("newWindow");
	w1.setText("Add Event");
	// w1.setSkin("dhx_terrace");
	w1.button("close").disable();
	w1.button("minmax").disable();
	w1.denyResize();

}
function populateTable() {
	loadSelectBoxes();
	$('#table').bootstrapTable({
		url : '/admin/events/load',
		columns : [ {
			field : 'state',
			title : 'Select',
			radio : true
		}, {
			field : 'id',
			title : 'Id',
			align : 'center',
			valign : 'middle',
			searchable : true

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
			field : 'text',
			title : 'Text',
			align : 'center',
			valign : 'middle'
		}, {
			field : 'details',
			title : 'Details',
			align : 'center',
			valign : 'middle'
		}, {
			field : 'orderId',
			title : 'Order',
			align : 'center',
			valign : 'middle',
			sortable : true
		}, {
			field : 'phaseName',
			title : 'Phase',
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
	dhtmlx.ajax("/admin/orders/load", function(returnData) {
		orders = $.parseJSON(returnData);
		$.each(orders, function(i, d) {
			$('#orderId').append(
					'<option value="' + d.id + '">' + d.orderNumber
							+ '</option>');
		});
	});
	dhtmlx.ajax("/admin/phases/load", function(returnData) {
		phases = $.parseJSON(returnData);
		$.each(phases, function(i, d) {
			$('#phaseId').append(
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
	document.getElementById("startDate").focus();
	dhxWins.window('w1').center();
	dhxWins.window('w1').show();
	mode = selectedMode;

}

function hideWindow() {
	cleanForm();
	dhxWins.window('w1').hide();
	$('#startDate').prop('disabled', false);
	$('#endDate').prop('disabled', false);
	$('#text').prop('disabled', false);
	$('#details').prop('disabled', false);
	$('#orderId').prop('disabled', false);
	$('#phaseId').prop('disabled', false);
}

function saveRecord() {
	if (mode == 'add') {
		var data = {
			'startDate' : $("#startDate").val(),
			'endDate' : $("#endDate").val(),
			'text' : $("#text").val(),
			'details' : $("#details").val(),
			'orderId' : $("#orderId").val(),
			'phaseId' : $("#phaseId").val()
		}
	} else {
		var data = {
			'id' : getSelected().id,
			'startDate' : $("#startDate").val(),
			'endDate' : $("#endDate").val(),
			'text' : $("#text").val(),
			'details' : $("#details").val(),
			'orderId' : $("#orderId").val(),
			'phaseId' : $("#phaseId").val()
		}
	}
	saveOrUpdate(data);
}
function saveOrUpdate(data) {
	preSubmit();
	dhtmlx.ajax().post("/admin/events/save", data, function(returnData) {
		var res = $.parseJSON(returnData);
		if (res.messageType == 'SUCCESS') {
			hideWindow();
			displayMessage(res.message);
			var savedEvent = res.object;
			$('#table').bootstrapTable('refresh');
		} else {
			displayError(res.message);
		}
	});
	postSubmit();
}
function cleanForm() {
	document.getElementById("startDate").value = "";
	document.getElementById("endDate").value = "";
	document.getElementById("text").value = "";
	document.getElementById("details").value = "";
	document.getElementById("orderId").value = "";
	document.getElementById("phaseId").value = "";

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
	/*
	 * $("#info").html(res); $("#info").show(); $("#info").toggle(5000);
	 */
	alert(res);
}
function displayError(res) {
	/*
	 * $("#error").show(); $("#error").html(res);
	 */
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
	document.getElementById("startDate").value = selected.displayStartDate;
	document.getElementById("endDate").value = selected.displayStartDate;
	document.getElementById("text").value = selected.text;
	document.getElementById("details").value = selected.details;
	document.getElementById("orderId").value = selected.orderId;
	document.getElementById("phaseId").value = selected.phaseId;
}
function deleteRecord() {
	var selected = getSelected();
	if (selected == null) {
		displayMessage('Please select a record to delete');
		return;
	} else {
		var data = {
			'id' : selected.id,
			//'startDate' : selected.displayStartDate,
			//'endDate' : selected.displayStartDate,
			'text' : selected.text,
			'details' : selected.details,
			'orderId' : selected.orderId,
			'phaseId' : selected.phaseId
		}
		dhtmlx.ajax().post("/admin/events/delete/", data, function(returnData) {
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