$(function() {
	showTabsByQueryParams();
});

function showTabsByQueryParams() {
	
	
	var y = /\?y\b/i,
		r = /\?r\b/i,
		title = /\?title\b/i,
		lat = /\?lat\b/i,
		lng = /\?lng\b/i;
	var query = $('textarea[id=line_numbers]').val(),
		titleProp = query.match(title),
		mapLatProp = query.match(lat),
		mapLngProp = query.match(lng),
		bubbleYprop = query.match(y),
		bubbleRprop = query.match(r);
	
	  var id = $("#queryform input[id=id]").val(),
		chart = [];
	  
	$("#tResult").empty();
	
	if (titleProp && mapLatProp && mapLngProp) {

		$('.charts, .b-chart').hide();
		/* chart.push('m'); */
		chart.push('gMap');
		chart.push('tResult');
		Service.getResultChartData(id, query, chart);

	} else if (titleProp && !mapLatProp && !mapLngProp && !bubbleYprop && !bubbleRprop) {
		$('.maps').hide();
		$(".b-chart").hide();
		chart.push('lChart');
		chart.push('sChart');
		chart.push('bChart');
		chart.push('pChart');
		chart.push('tResult');
		Service.getResultChartData(id, query, chart);

	} else if (titleProp && bubbleYprop && bubbleRprop) {
		$('.maps').hide();
		$('.charts').hide();
		chart.push('tResult');
		chart.push('bubbleChart');
		Service.getResultChartData(id, query, chart);
	} else {
		$('.charts, .b-chart').hide();
		$('.maps').hide();
		chart.push('tResult');
		Service.getResultChartData(id, query, chart);

	}
}