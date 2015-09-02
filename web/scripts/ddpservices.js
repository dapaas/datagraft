var Service = {
  param : "",
  countchart: 0,
  parce: 0,
  getchartdata :function(param, chart) {
    $.ajax({
      type : "POST",
        async: false,
        url: Application.contextPath +"/BaseGateway/chatrdata.json",
        data:{param: param, chartId: chart.id}
        
    }).done(function(data) {
      // load data in all users seelected chart
      $("#container").append("<div class='well' id='chart-wrapper"+chart.id+"' style='overflow:hidden;'></div>");
      if (chart.title != null){
        $("#chart-wrapper"+chart.id).append("<h2>"+chart.title+"</h2>");
      }
      $("#chart-wrapper"+chart.id).append("<p style='clear: both;'>"+chart.descriptionHtml+"</p>");
      $("#chart-wrapper"+chart.id).append("<div id='T"+chart.id+"'></div>");
      //var data = response.result;
      if ('drawLineChart' == chart.chart){
        lineChart.init("T"+chart.id, data.result, chart.title);
      }
      if ('drawBarChart' == chart.chart){
          barChart.init("T"+chart.id, data.result, chart.title);
      }
      if ('drawPieChart' == chart.chart){       
        pieChart.init("T"+chart.id, data.result);
      }
      if ('drawPoligonChart' == chart.chart){       
        poligonChart.init("T"+chart.id, data.result, data.result.poligon);
      }
      if ('drawScatterChart' == chart.chart){       
          scatterChart.init("T"+chart.id, data.result, chart.title);
      }
      if ('drawBubbleChart' == chart.chart){        
        bubbleChart.init("T"+chart.id, data.result, chart.title);
      }
      if ('drawTable' == chart.chart){
          drawTable.init("T"+chart.id, data.result);
      }     
      if ('googleMaps'== chart.chart){
          //config.locations
         drawGoogleMaps.google("T"+chart.id, data.result); 
      }
      $("#chart-wrapper"+chart.id).append("<p style='clear: both;'>"+chart.summaryHtml+"</p>");
      
      Service.parce = Service.parce + 1;
    }).fail(function(err) {});
  },
  getcharts:function(param) {
    $.ajax({
      type : "POST",
        async: false,
        url: Application.contextPath +"/BaseGateway/getcharts.json",
        data:{param: param}
        
    }).done(function(data) {
     
      var charts = data.result;
      Service.countchart = charts.length;
      Service.parce = 0;
      if (Service.countchart == 0){
        $("#chartsprogress").hide();
      }else{
        var i=0; 
        var interval = setInterval(function(){ 
          
          if (i<charts.length && Service.parce ==i){
            var por = Service.parce * (100 / Service.countchart);
            $(".progress-bar").width( (por) +"%");
            Service.getchartdata(Service.param, charts[i]);
            i++;
          }
          if (Service.parce >=  Service.countchart) {
            $("#chartsprogress").hide();
            clearInterval(interval);
          } 
        }, 500);
        
      }
    }).fail(function(err) {});
  },
  
  executeQuery: function(query, selectedChart, poligonId){
    $.ajax({
        type : "POST",
          async: false,
          url: Application.contextPath +"/BaseGateway/executeQuery.json",
          data:{query: query,
                poligonId: poligonId}
          
      }).done(function(data) {
        
        $("#dialog-preview-portal").dialog("option", "height", $(".container").width()/2);
        $("#dialog-preview-portal").dialog("option", "width", $(".container").width());
        
        $("#dialog-preview-portal").dialog("open");
        $("#dialog-preview-portal").css("height", '550px');
        var html = "";
        var error = data.error
        if ((error!= null && error.length>0)){
          
          html="<div id='containerl'> <h4>"+error+"</h4></div>  ";
          $("#dialog-preview-portal div[id=container]").html('');
          $("#dialog-preview-portal div[id=container]").html(html);
        }else{
          if ((data.result.error!=null && data.result.error.length>0)){
            html="<div id='containerl'> <h4>"+data.result.error+"</h4></div>  ";
            $("#dialog-preview-portal div[id=container]").html('');
            $("#dialog-preview-portal div[id=container]").html(html);
          }else{
            
            // var data = response.result;
            $("#dialog-preview-portal div[id=container]").html('');
            
            if (data.result.values.length <=0){
              html="<div id='containerl'> <h4>SPARQL query returns no data.</h4></div>  ";
              $("#dialog-preview-portal div[id=container]").html(html);
            }
            
            
            $("#container").append("<div id='"+selectedChart+"'></div>");
            
            if ('drawTable' == selectedChart){
              drawTable.init(selectedChart, data.result);
            }
            if ('drawLineChart' == selectedChart){
              lineChart.init(selectedChart, data.result);
            }
            if ('drawBarChart' == selectedChart){
              barChart.init(selectedChart, data.result);
            }
            if ('drawPieChart' == selectedChart){
              pieChart.init(selectedChart, data.result);
            }
            if ('drawPoligonChart' == selectedChart){
              poligonChart.init(selectedChart, data.result, data.result.poligon);
            }
            if ( 'drawScatterChart' == selectedChart ){
                scatterChart.init(selectedChart, data.result);
            }
              if ( 'drawBubbleChart' == selectedChart ){
                  bubbleChart.init(selectedChart, data.result);
            }
            if ('googleMaps'== selectedChart){
              //config.locations
              drawGoogleMaps.google(selectedChart, data.result, 400); 
            }
          }
        }
        
        
     }).fail(function(err) {});
  },
  getResultChartData: function(id, query, chartArr) {
    $.ajax({
        type : "POST",
          async: false,
          url: Application.contextPath +"/BaseGateway/getResultData.json",
          data:{query: query,
              id: id}
          
      }).done(function(responseData) {
        var data = responseData.result,
          i;
        for(i=0; i<chartArr.length; i++){
          $("#"+chartArr[i]).parent().show();
          Service.initCharts(chartArr[i], data);
          
      }
      });
  },
  initCharts: function (chartName, respData) {

      if ('tResult' == chartName){
          drawTable.init(chartName, respData);
        }
      if ('lChart' == chartName){
          lineChart.init(chartName, respData);
        }
      if ('bChart' == chartName){
          barChart.init(chartName, respData);
        }
      if ('pChart' == chartName){       
        pieChart.init(chartName, respData);
        }
      if ('sChart' == chartName){
        scatterChart.init(chartName, respData);
      }
      if ('bubbleChart' == chartName){
        bubbleChart.init(chartName, respData);
      }
      /*if ( 'tLine' == chartName ){
          // set date pattern
          timeLine.init(chartName, respData);
        }*/
      if ('gMap'== chartName){
          //config.locations
        drawGoogleMaps.google(chartName, respData); 
      }
//      if ('polChart' == chartName){       
//        poligonChart.init(chartName, respData);
//        }
      /*if ('m' == chartName){
          drawMaps.topograph(chartName, respData);
        }*/
  }
};

