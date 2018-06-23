var dhxWins, w1, products, phases;
function initWindow() {
	dhxWins = new dhtmlXWindows();
	// dhxWins.attachViewportTo("container");
	w1 = dhxWins.createWindow("w1", 20, 30, 350, 450);
	w1.attachObject("newWindow");
	w1.setText("Add Calculation");
	// w1.setSkin("dhx_terrace");
	w1.button("close").disable();
	w1.button("minmax").disable();
	w1.denyResize();

}

function populateTable() {
	loadSelectBoxes();
	$('#table').bootstrapTable({
		url : '/admin/calculations/load',
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
			field : 'productName',
			title : 'Product Type',
			align : 'center',
			valign : 'middle',
			sortable : true
		}, {
			field : 'phaseName',
			title : 'Phase',
			align : 'center',
			valign : 'middle',
			sortable : true
		}, {
			field : 'calculatedManHours',
			title : 'Man Hours',
			align : 'center',
			valign : 'middle'
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
	document.getElementById("productType").focus();
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
			'productType' : $("#productType").val(),
			'phaseId' : $("#phaseId").val(),
			'calculatedManHours' : $("#calculatedManHours").val(),
			'productName' : $('#productType').find('option:selected').text(),
			'phaseName' : $('#phaseId').find('option:selected').text()
		}
	} else {
		var data = {
			'id' : getSelected().id,
			'productType' : $("#productType").val(),
			'phaseId' : $("#phaseId").val(),
			'calculatedManHours' : $("#calculatedManHours").val(),
			'productName' : $('#productType').find('option:selected').text(),
			'phaseName' : $('#phaseId').find('option:selected').text()
		}
	}
	saveOrUpdate(data);
}
function saveOrUpdate(data) {
	preSubmit();
	dhtmlx.ajax().post("/admin/calculations/save", data, function(returnData) {
		var res = $.parseJSON(returnData);
		if (res.messageType == 'SUCCESS') {
			hideWindow();
			displayMessage(res.message);
			var savedCalculation = res.object;
			$('#table').bootstrapTable('refresh');
		} else {
			displayError(res.message);
		}
	});
	postSubmit();
}
function cleanForm() {
	document.getElementById("productType").value = "";
	document.getElementById("phaseId").value = "0";
	document.getElementById("calculatedManHours").value = "0";
	/*
	 * $("#error").hide(); $("#error").html("");
	 */
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
	document.getElementById("productType").value = selected.productType;
	document.getElementById("phaseId").value = selected.phaseId;
	document.getElementById("calculatedManHours").value = selected.calculatedManHours;
}
function deleteRecord() {
	var selected = getSelected();
	if (selected == null) {
		displayMessage('Please select a record to delete');
		return;
	} else {
		var data = {
			'id' : selected.id,
			'productType' : selected.productType,
			'phaseId' : selected.phaseId
		}
		dhtmlx.ajax().post("/admin/calculations/delete/", data,
				function(returnData) {
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