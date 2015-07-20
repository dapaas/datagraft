var pieChart = this || {};

pieChart = (function() {
	"use strict";
	function createChart(containerId, dataAll) {

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

		// get data keys
		//var keys = common.getCSVKeys(data);

		// get the title key
		var keyAIndex = keyOrdered.filter(isAntetka);

		// get other keys
		var keyVIndex = keyOrdered.filter(isValue);

		var names = [];
		$.each(data, function(key, value) {
			names.push(value[keyAIndex])
		})

		var seriesArr = [];
		var number = 1 + Math.floor(Math.random() * 1000);

		$.each(keyVIndex, function(a, b) {
			var options = {
				chart : {
					type : 'pie',
					plotBackgroundColor : null,
					plotBorderWidth : null,
					plotShadow : false
				},
				plotOptions : {
					pie : {
						showInLegend : true,
						dataLabels : {
							formatter : function() {
								return this.point.y + " ( "
										+ Math.round(this.point.percentage)
										+ " % )";
							},
							enabled : true
						}
					}
				},
				legend : {
					enabled : true,
					labelFormatter : function() {
						return names[this.x];
					}
				},
				tooltip : {
					formatter : function() {
						return names[this.point.index] + '</b><br/>'
								+ "\u2022 " + b + ": " + this.point.y;
					}
				},
				title : {
					text : b
				},
				series : [ {
					name : b,
					data : (function() {
						var pieData = [], l = [];
						$.each(data, function(key, value) {
							pieData.push(+(value[b]));
						});

						return pieData;
					}())
				} ]
			};

			$("#" + containerId).append(
					"<div class='col-lg-6 col-md-6 col-sm-12 col-xs-12' id='piechart-"
							+ b + number + "'></div>");
			$("#piechart-" + b + number).highcharts(options);
		});

	}
	return {
		init : createChart
	};

}());