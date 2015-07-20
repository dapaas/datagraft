var lineChart = this || {};

lineChart = (function() {
	"use strict";
	function createChart(containerId, data, ctitle) {

		var options = common.chartOptions('line', ctitle, data);

		$("#" + containerId).highcharts(options);
	}
	return {
		init : createChart
	};

}());