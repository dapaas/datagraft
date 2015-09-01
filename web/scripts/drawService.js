$(function() {
	showTabsByQueryParams();
	
	
	
//show selected tab for query and query constructor in dataset query results and dataset details
  tabsControl('tab-section', 'query-tabs');

  //show selected tab for results in dataset query results
  tabsControl("result-tab-section", "tabs")
});

function showTabsByQueryParams() {
	
	
	var y = /\?y\b/i,
		r = /\?r\b/i,
		title = /\?title\b/i,
		lat = /\?lat\b/i,
		lng = /\?lng\b/i;
	var query = $('textarea[id=line_numbers]').val();
	
	
	var matchquery = query.substring(0, query.toLowerCase().indexOf("where"));
	
	var	titleProp = matchquery.match(title),
		  mapLatProp = matchquery.match(lat),
		  mapLngProp = matchquery.match(lng),
		  bubbleYprop = matchquery.match(y),
		  bubbleRprop = matchquery.match(r);
	
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