
var config = {
	CSS : {
		IDs : {
			lineChart : "#line",
			barChart : "#chart",
			map : "#map",
			mapCanvas : "map_canvas",
			marker : 'marker',
			timeline : "#timeline"
		},
		classes : {
			xAxis : "x axis",
			yAxis : "y axis",
			bar : "bar",
			xLaxis : "xL Laxis",
			yLaxis : "yL Laxis",
			line : "line",
			objBound : 'objectBoundingBox',
			place : "place-label",
			undergroudLine: "undergroud-line",
			DLRLine: "dlr-line",
			TramLine: "tram-line",
			OvergroundLine: "overground-line",
			AirlineLine: "airline-line"
		},
		el : {
			g : "g",
			text : "text",
			rect : "rect",
			path : "path",
			circle : "circle",
			pattern : 'pattern',
			image : 'image'
		}

	},
	dimensions : {
		margin : {
			top : 20,
			right : 20,
			bottom : 50,
			left : 40,
		},
		/*width : common.windowSize().width - common.windowSize().width / 10,*/
		width : common.windowSize().width,
		height : common.windowSize().height / 2
	},
	axis : {
		left : "left",
		right: "right",
		bottom : "bottom",
		y : "y",
		x : "x",
		dy : "dy",
		dx : "dx",
		yL : "yL",
		xL : "xL",
		dyL : "dyL",
		cy : "cy",
		cx : "cx"
	},
	attr : {
		classAtr : "class",
		transform : "transform",
		translate : "translate(0,",
		close : ")",
		rotate90 : "rotate(-90)",
		textAnchor : "text-anchor",
		end : "end",
		width : "width",
		height : "height",
		fill : "fill",
		rgb : "rgb(",
		d : "d",
		r : "r",
		id : 'id',
		patternUnits : 'patternUnits',
		xlink : 'xlink:href',
		start : "start"
	},
	values : {
		barClass : ".bar",
		dyVal : ".71em",
		dyLval : "1em"
	},
	locations : [ [ 'Ruse', 43.837499, 25.966846 ],
			[ 'Sofia', 42.707669, 23.307785 ],
			[ 'Varna', 43.210931, 27.915519 ],
			[ 'Plovdiv', 42.142278, 24.75592 ],
			[ 'Bugas', 42.510197, 27.45824 ], [ 'Paris', 48.866522, 2.343838 ],
			[ 'Viena', 48.21232, 16.364715 ],
			[ 'Lisbon', 38.720073, -9.141022 ],
			[ 'Barcelona', 41.398703, 2.177643 ],
			[ 'London', 51.502759, -0.123017 ] ],
	path : {
		topoJSON : "../data/worldMap.json",
		img : '../images/marker.png',

	},
	action : {
		click : "click",
		zoom : "zoom",
		mouseOver : "mouseOver",
		mouseOut : "mouseOut"
	}
};