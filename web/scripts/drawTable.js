var drawTable = this || {};

drawTable = (function() {
	"use strict";
	function createTable(containerId, dataAll) {

		if ($.isEmptyObject(dataAll)) {
			return;
		} else if (dataAll.values.length <= 0) {
			return;
		}

		var data = dataAll.values;

		var keyOrdered = common.convertObjectToArray(dataAll.columns);

		function isAntetka(element, i) {
			if (element === 'title') {
				return element;
			}
		}

		function isValue(element, i) {
			if (keyAIndex.indexOf(element) == -1) {
				return element;
			}
		}

		var itemNumber = 5;
		var page = 1;

		// get the title key
		var keyAIndex = keyOrdered.filter(isAntetka);

		// get other keys
		var keyVIndex = keyOrdered.filter(isValue);

		var wrapper = [], number = 1 + Math.floor(Math.random() * 1000);

		$.each(data, function(k, v) {
			var tableData = [];
			$.each(keyOrdered, function(key, value) {
				tableData.push(v[value])
			})
			wrapper.push(tableData);

		});

		$("#" + containerId).append("<div class='filter-wrapper'></div>");
		$("#" + containerId).append(
				"<table class='table table-striped table-responsive' id='"
						+ containerId + number + "'></table>");

		$('#' + containerId + number).dataTable({
			"data" : wrapper,
			"columns" : (function() {
				var obj = [];
				$.each(keyOrdered, function(key, value) {
					obj.push({
						title : value
					})
				});
				return obj;
			}())
		});

	}
	return {
		init : createTable
	};
}());