var scatterChart = this || {};

scatterChart = (function() {
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

		var seriesArr = [];
		var options = {
			chart : {
				type : 'scatter',
				zoomType : 'xy'
			},
			xAxis : {
				title : {
					enabled : true
				},
				categories : []
			},
			title : {
				text: '',
			    style: {
			        display: 'none'
			    }
			},

			tooltip : {
				formatter : function() {
					return  this.x+ '</b><br/>' + "<strong>\u2022</strong> " + this.series.name
							 + ": " + "<strong>"+this.point.y+"</strong>";
				}

			},
			plotOptions : {
				scatter : {
					marker : {
						radius : 8,
						states : {
							hover : {
								enabled : true,
								lineColor : 'rgb(100,100,100)'
							}
						}
					},
					states : {
						hover : {
							marker : {
								enabled : false
							}
						}
					}
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
				series.data.push([ +(value[b]) ]);
				options.xAxis.categories.push(value[keyAIndex]);
			});
			seriesArr.push(series);

		});

		$("#" + containerId).highcharts(options)
	}

	return {
		init : createChart
	};

}());