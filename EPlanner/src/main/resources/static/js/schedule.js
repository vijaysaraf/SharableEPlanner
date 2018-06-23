var myTabbar;
var phases = new Array();
var allEvents = new Array();
function getLocalDate(strDate) {
	var strDateArray = strDate.split("-");
	var year = strDateArray[0];
	var month = strDateArray[1];
	var day = strDateArray[2].split(" ")[0];
}
function init() {
	loadData();
	myTabbar = new dhtmlXTabBar("my_tabbar");
	myTabbar.attachEvent("onTabClick", function(id, lastId) {
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
			myTabbar.tabs("tab_" + phases[0]).attachObject("schedulerDiv");
		} else {
			myTabbar.addTab("tab_" + phases[i], phases[i]);
		}
	}
	scheduler.config.xml_date = "%Y-%m-%d %H:%i";
	scheduler.config.readonly = true;
	scheduler.config.first_hour = 8;
	scheduler.config.last_hour = 19;
	var tempEvent = getEvents(phases[0]);
	scheduler.init('schedulerDiv', tempEvent[0].start_date, "week");
	scheduler.parse(tempEvent, "json");

	// var evs = scheduler.getEvents();
}
function getEvents(phaseName) {
	var events = new Array();
	for (var i = 0; i < allEvents.length; i++) {
		if (allEvents[i].id.startsWith(phaseName))
			events[events.length] = allEvents[i];
	}
	return events;
}
function storeData(events) {
	allEvents = events;
}

function loadData() {
	$.ajax({ type : 'GET', url : "/admin/phases/names",	success : function(res) { phases = res;	}, async : false	});
	var qParam = getParameterByName("q");
	var data;
	if (qParam != null) {
		data = {
			"orderId" : qParam
		};
	} else {
		data = {
			"orderId" : "ALL"
		};
	}

	$.ajax({
		type : 'POST',
		url : "/admin/orders/events",
		data : data,
		success : function(res) {
			if (res.messageType == 'SUCCESS') {
				storeData(res.object);
			} else {
				alert(res.message)
			}
		},
		async : false
	});
}
function getParameterByName(name) {
	var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
	return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
}
