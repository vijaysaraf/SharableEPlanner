var dhxWins, w1, C;
function initWindow() {
	dhxWins = new dhtmlXWindows();
	//dhxWins.attachViewportTo("container");
	w1 = dhxWins.createWindow("w1", 20, 30, 350, 450);
	w1.attachObject("newWindow");
	w1.setText("Add Phase");
	// w1.setSkin("dhx_terrace");
	w1.button("close").disable();
	w1.button("minmax").disable();
	w1.denyResize();

}
function populateTable() {
	loadSelectBoxes();
	$('#table').bootstrapTable({
		url : '/admin/phases/load',
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
			searchable:false

		}, {
			field : 'name',
			title : 'Name',
			align : 'center',
			valign : 'middle',
			sortable : true

		}, {
			field : 'defaultManPower',
			title : 'Man Power (Default)',
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
		sortable:true
	});
	for (var i = 1; i < 51; i++) {
		$('#defaultManPower').append(
				'<option value="' + i + '">' + i + '</option>');
	}
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
	document.getElementById("name").focus();
	dhxWins.window('w1').center();
	dhxWins.window('w1').show();
	mode = selectedMode;

}
function loadSelectBoxes() {
	dhtmlx.ajax("/admin/phases/names", function(returnData) {
		phases = $.parseJSON(returnData);
		$.each(phases, function(i, d) {
			$('#name').append(
					'<option value="' + d + '">' + d + '</option>');
		});
	});
	
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
			'id' : $("#name").val(),
			'name' : $("#name").val(),
			'defaultManPower' : $("#defaultManPower").val()
		}
	} else {
		var data = {
			'id' : getSelected().id,
			'name' : $("#name").val(),
			'defaultManPower' : $("#defaultManPower").val()
		}
	}
	saveOrUpdate(data);
}
function saveOrUpdate(data) {
	preSubmit();
	dhtmlx.ajax().post("/admin/phases/save", data, function(returnData) {
		var res = $.parseJSON(returnData);
		if (res.messageType == 'SUCCESS') {
			hideWindow();
			displayMessage(res.message);
			var savedPhase = res.object;
			$('#table').bootstrapTable('refresh');
		} else {
			displayError(res.message);
		}
	});
	postSubmit();
}
function cleanForm() {
	document.getElementById("name").value = "";
	document.getElementById("defaultManPower").value = "0";
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
	document.getElementById("name").value = selected.name;
	document.getElementById("defaultManPower").value = selected.defaultManPower;
}
function deleteRecord() {
	var selected = getSelected();
	if (selected == null) {
		displayMessage('Please select a record to delete');
		return;
	} else {
		var data = {
			'id' : selected.id,
			'name' : selected.name
		}
		dhtmlx.ajax().post("/admin/phases/delete/", data, function(returnData) {
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