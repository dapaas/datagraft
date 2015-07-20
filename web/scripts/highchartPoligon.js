var poligonChart = this || {};

poligonChart = (function() {
  "use strict";
  function createChart(containerId, data, mapdata) {

    // var data = $.csv.toObjects(csv);
    var countiesMap = Highcharts.geojson(mapdata);
    var lines = Highcharts.geojson(mapdata, 'mapline');
    var options;
    var colors = {1:'#058DC7', 2:'#50B432', 3:'#ED561B', 4:'#DDDF00', 5:'#24CBE5', 6:'#64E572', 7:'#FF6699' ,8:'#996600' ,9:'#660066' , 0: '#800000' };

    // Add state acronym for tooltip
    Highcharts.each(countiesMap, function(mapPoint) {
      mapPoint.name = mapPoint.name;
    });

    var minvalue =  Math.min.apply(Math, data.map(function(o){return o.value;}));
    var maxvalue = Math.max.apply(Math, data.map(function(o){return o.value;}));

    options = {
      chart : {
        borderWidth : 1,
        marginRight : 10
      // for the legend
      },

      legend : {
        
        layout: 'vertical',
        align: 'right',
        verticalAlign: 'middle',
        backgroundColor : (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || 'rgba(255, 255, 255, 0.85)'
      },

      mapNavigation : {
        enabled : true
      },

      colorAxis : {
        min : minvalue,
        max : maxvalue,
        type : 'logarithmic',
        minColor: '#E6E7E8',
        maxColor : colors[Math.floor((Math.random() * 10))]
      },

      plotOptions : {
        mapline : {
          showInLegend : false,
          enableMouseTracking : false
        }
      },

      series : [ {
        mapData : countiesMap,
        data : data,
        joinBy : [ 'CODE', 'code' ],
       
        tooltip : {
          pointFormat : '{point.properties.NAME}: {point.value}<br/>'
        },
        borderWidth : 0.5,
        states : {
          hover : {
            color : '#bada55'
          }
        }
      } ]
    };

    // Instanciate the map
    $("#" + containerId).highcharts('Map', options);

  }
  return {
    init : createChart
  };

}());