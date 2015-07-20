var bubbleChart = this || {};

bubbleChart = (function() {
	"use strict";
	function createChart(containerId, dataAll, ctitle) {

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

		function isR(element, i) {
			if (element === 'r') {
				return element;
			}
		}

		function isY(element, i) {
			if (element === 'y') {
				return element;
			}
		}

		function isValue(element, i) {
			if (keyAIndex.indexOf(element) == -1 && keyRIndex.indexOf(element) == -1 && keyYIndex.indexOf(element) == -1) {
				return element;
			}
		}

		// get data keys
		//var keys = common.getCSVKeys(data);

		// get the title key
		var keyAIndex = keyOrdered.filter(isAntetka);

		// get the radius key
		var keyRIndex = keyOrdered.filter(isR);

		// get data key
		var keyYIndex = keyOrdered.filter(isY);

		// get other keys
		var keyVIndex = keyOrdered.filter(isValue);

		var names = [];
		$.each(data, function(key, value) {
			names.push(value[keyAIndex])
		})

		var seriesArr = [];
		var options = {
			chart : {
				type : 'bubble',
				zoomType : 'xy'
			},
			xAxis : {
				title : {
					enabled : true
				},
				categories : []
			},

			tooltip : {
				formatter : function() {
					return this.x + '</b><br/>' + "\u2022 " + this.series.name
							+ ": " + this.point.y;
				}

			},
			title : {
				text: '',
			    style: {
			        display: 'none'
			    }
			},
			series : seriesArr
		}

		$.each(keyVIndex, function(a, b) {

			var series = {
				name : b,
				data : []

			}

			$.each(data, function(key, value) {
				series.data.push([ +(value[b]), +(value[keyRIndex]) ]);
				options.xAxis.categories.push(value[keyAIndex]);
			});

			seriesArr.push(series);
		});
		
		$("#" + containerId).highcharts(options);
	}
	return {
		init : createChart
	};

}());