var common = this || {};

/*
 * Common functions used for counting duplicate albums and creating new dataset
 * with count field, sorting of native data by album name and click function for
 * bar and line charts
 */
common = (function() {
	"use strict";
	// counting songs in data array
	function createArray(dataset) {
		var keys = Object.keys(dataset[0]);
		var lastItem = dataset[0][keys[0]],
			count = 0,
			newArray = [],
			dataLength = dataset.length;
		
		for ( var i = 0; i < dataLength; i++) {
			if (dataset[i][keys[0]] != lastItem || i == dataset.length - 1) {
				newArray.push({
					Song : lastItem,
					Count : count,
					Time : dataset[i][keys[2]],
					Date : Math.round(new Date().getFullYear() - (i*0.2)),
					Id: i
				});
				count = 0;
			}
			count++;
			lastItem = dataset[i][keys[0]];
		}
		return newArray;
	}	

//	function sort(data) {
//
//		// debugger;
//		if($.isEmptyObject(data)){
//			return;
//		} else {
//			var keys = Object.keys(data[0]);
//			// copy data into new array for sorting data
//			var newData = data.slice();
//			// sorted by song name
//			newData.sort(function(a, b) {
//				return d3.descending(a[keys.title], b[keys.title]);
//			});
//			return newData;
//		}
//	}
//	
//	function sortByDate(data) {
//		var arr = data.slice();
//		arr.sort(function(a, b) {
//			return d3.descending(a.Date, b.Date);
//		});
//		
//		return arr;
//	}
	// adding event listener click to bar chart
	function clickEvent(svgName, elName) {
		svgName.selectAll(elName)
				.on("click",
						function(d) {
							//
						});
	}
	// appending tooltip to bar chart
//	function mouseEvent(svgName, elName) {
//		var div = d3.select("body").append("div").attr("class", "tooltip")
//				.style("opacity", 0);
//		svgName.selectAll(elName)
//				.on("mouseover",
//						function(d) {
//							div.transition().duration(200).style("opacity", .9);
//							var text = (elName === ".bar") ? d.Song + "<br/>" + " recorded " + d.Count + " times." : (d3.select(this).transition().attr("r", 9), d.Song + "<br/>" + " lasts " + d.Time
//									+ " mins."); 
//							
//							div.html(text).style("left",
//									(d3.event.pageX) + "px").style("top",
//									(d3.event.pageY - 50) + "px");
//						}).on("mouseout", function(d) {
//					(elName === "circle") ? d3.select(this).transition().attr("r", 5) : null;
//					div.transition().duration(500).style("opacity", 0);
//				});
//	}
	
//	function createSVG(dims, divName) {	
//	  var offset = 10*common.windowSize().width / 100;
//    if (offset > 50) offset = 50;
//    var bottom =  170;
//		if(divName !== "#chart") {
//			 bottom = dims.margin.bottom;			
//		}
//		var svg = d3.select(divName).append("svg").attr("width", dims.width)
//				.attr("height",dims.height + dims.margin.top + bottom).append("g").attr(
//						"transform", "translate(" + offset + "," + dims.margin.top + ")");
//		return svg;
//	}
	
//	function clickTimeLineEvent(svgName, elName) {
//		var div = d3.select("body").append("div").attr("class", "timeTooltip"),
//			el = null;
//		svgName.selectAll(elName)
//				.on("click", function(d) {
//				  
//					var text = "<h4 class='alb'>" + d.title + "</h4> " ;
//					var keys = Object.keys(d);
//					for (var i=0; i<keys.length; i++){
//					  if (keys[i] != "title" && keys[i] != "date"){
//					    text += "<p>"+keys[i]+": "+d[keys[i]]+"</p>";
//					  }
//					}
//					
//					text += "<p>"+d.date+"</p>";
//					div.html(text).style("left", (d3.event.pageX - 65) + "px").style("top", (d3.event.pageY - 100) + "px");
//					if (el !== this) {
//						div.style("visibility", "visible");
//						el = this;
//					} else {
//						div.style("visibility", "hidden");
//						el = null;
//					}
//					
//		});
//	}
	
	
//	function getCSVKeys(data) {
//		if($.isEmptyObject(data)) {
//			return;			
//		}
//		var keys = Object.keys(data[0]);
//		return keys;
//	}
	
	function convertObjectToArray(keys) {
		if($.isEmptyObject(keys)) {
			return;			
		}
		var array = $.map(keys, function(value, index) {
		    return [value];
		});
		return array;		
	}
	
	function wndsize(){
		var w = 0;var h = 0;
		// IE
		if(!window.innerWidth){
		    if(!(document.documentElement.clientWidth == 0)){
		        // strict mode
		        /* w = document.documentElement.clientWidth; */
		    	w = $(".container").width();
		        h = document.documentElement.clientHeight;
		    } else{
		        // quirks mode
		        /* w = document.body.clientWidth; */
		    	w = $(".container").width();
		        h = document.body.clientHeight;
		    }
		} else {
		    // w3c
		    /* w = window.innerWidth; */
		    w = $(".container").width();
		    h = window.innerHeight;
		}
		
		
		console.log("w: " + w)
		console.log("h: " + h)
		return {
			width:w,
			height:h
		};
	}
	
	function chartOptions(ctype, ctitle, dataAll, p){
		
		if ($.isEmptyObject(dataAll)) {
			return;
		} else if (dataAll.values.length <= 0) {
			return;
		}
		
		var data = dataAll.values;
		
		var keysOrdered = convertObjectToArray(dataAll.columns);

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
		// var keys = getCSVKeys(data);

		// get the title key
		var keyAIndex = keysOrdered.filter(isAntetka);

		// get other keys
		var keyVIndex = keysOrdered.filter(isValue);
		
		var seriesArr = [];
		var options = {
			chart : {
				type : ctype,
			},
			xAxis : {
				categories: [],
				labels: {
					rotation: -1
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
		
		/*var nameVals=[],
			titleVals=[],
			vIndexVals = [];
		
		
		var newData=[];
		
		$.each(data, function(dk, dv) {
			var newObj = {}
			
			if($.inArray(dv[keyAIndex], titleVals) === -1){
				newObj[keyAIndex]=dv[keyAIndex];
				$.each(keyVIndex, function(key, value) {
					newObj[value]=dv[value];				
				});
				titleVals.push(dv[keyAIndex]);
				newData.push(newObj)
			} else {
				$.each(keyVIndex, function(key, value) {
					$.each(newData, function(k, v) {
						v[value+"_1"] = dv[value]
					})
						
				});
			}
		});*/
		
		
		/*var newData=[];
		$.each(titleVals, function(key, val) {
			// loop titles
			var newObj = {	
					title: val
			}
			//loop other keys
			$.each(keyVIndex, function(key, value) {
				var arr = [],
					otherKeysVals=[];
				var titles=[];
				
					$.each(data, function(thekey, theval){
						
						if($.inArray(val, titles)){
							arr.push(theval[value]);
							titles.push(val)
						} else {
							otherKeysVals.push(theval[value]);
						}
						
						
					});
					
					//newData[value + 1] 
					
					console.log(otherKeysVals)
					newObj[value] = arr
			});
			
			newData.push(newObj)
		})*/
		
		/*$.each(data, function(dk, dv) {
			
			var series = {
					name: dv[keyAIndex],
					data: []
			}
			$.each(keyVIndex, function(key, value) {
				series.data.push(+(dv[value]));
				series.stack = value;
			})
			if($.inArray(dv[keyAIndex], titleVals) === -1){
				titleVals.push(dv[keyAIndex]);
				//options.xAxis.categories.push(dv[keyAIndex]);
			}
			//options.xAxis.categories.push(dv[keyAIndex]);
			console.log(series)
			seriesArr.push(series);
		});*/
		
		
		
		var titleVals=[];
		
		$.each(data, function(dk, dv) {
			if($.inArray(dv[keyAIndex], titleVals) === -1){
				titleVals.push(dv[keyAIndex]);
			}
		});
		var labelSteps = data.length / titleVals.length;
		
		options.xAxis.labels.step = Math.round(labelSteps);
		
		$.each(keyVIndex, function(a, b) {
			  
			  var series = {
					  name : b, 
					  data : [] 
			  }; 
			  $.each(data, function(key, value) { 
				  series.data.push(+(value[b]));
				  options.xAxis.categories.push(value[keyAIndex]);
			  });
			  seriesArr.push(series);
			  
			});
		
		
		return options;
	}
	
	return {
		
//		sort : sort,
//		chartClick : clickEvent,
//		chartMouseEvent : mouseEvent,
//		createSVG : createSVG,
//		clickTimeLine: clickTimeLineEvent,
		windowSize: wndsize,
//		getCSVKeys: getCSVKeys,
		chartOptions: chartOptions,
		convertObjectToArray: convertObjectToArray
	};
}());