var drawGoogleMaps = this || {};

drawGoogleMaps = (function() {
	"use strict";
	function googleMap(containerId, datas) {
		var point = {
			lat : 37.76487,
			lng : -122.41948
		};
		
		if ($.isEmptyObject(datas)) {
			return;
		} else if (datas.values.length <= 0) {
			return;
		}
		
		if (datas.values.length > 0){
			point = datas.values[0];
		}
		
		var dims = config.dimensions,
			contFullHeight = dims.height + dims.margin.top + dims.margin.bottom,
			contHeight = contFullHeight - dims.margin.top, 
			number = 1 + Math.floor(Math.random() * 1000),
			keysOrdered = common.convertObjectToArray(datas.columns);
		
		
		$("#" + containerId).css({"width": "100%", "height": contFullHeight})
		$("#" + containerId).append("<div id='gmap"+number+"' style='height:"+contHeight+"px'></div>");

		var map = new google.maps.Map(
				$("#gmap"+number)[0], {
					zoom : 7,
					center : new google.maps.LatLng(point.lat, point.lng)
				});
		datas.values.map(function(data) {
					var overlay = new google.maps.OverlayView();

					overlay.draw = function() {
					
						var marker = new google.maps.Marker({
							position : new google.maps.LatLng(data.lat,	data.lng),
							map : map
						});
						var text = "<h4 class='alb'>" + data.title + "</h4> ";
						//var keys = Object.keys(data);
						var keys = keysOrdered;
						for (var i = 0; i < keys.length; i++) {
							if (keys[i] != "title" && keys[i] != "lng"
									&& keys[i] != "lat") {
								text += "<p>" + keys[i] + ": " + data[keys[i]]
										+ "</p>";
							}
						}
						var infoWindow = new google.maps.InfoWindow({
							content : text
						});

						google.maps.event.addListener(marker, 'click',
								function() {
									infoWindow.open(map, marker);
								});
					};
					overlay.setMap(map);
				});
	}
	return {
		google : googleMap
	};
}());