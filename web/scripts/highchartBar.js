var barChart = this || {};

barChart = (function() {
	"use strict";
	function createChart(containerId, data, ctitle) {
		
		//debugger

		var options = common.chartOptions('column', ctitle, data);

		$("#"+containerId).highcharts(options);
	}
	return {
		init : createChart
	};

}());