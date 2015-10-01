$(document).ready(function() {
  /* ".rdf,.rdfs,.xml,.owl,.nt,.ttl,.n3,.nq,.rj,.trix,.trig,.brf,.csv"*/
  
  $("#dropzone_dataset").dropzone(
      {
        url: Application.contextPath +"/upload",
        createImageThumbnails: true,
        maxThumbnailFilesize: 1,
        thumbnailWidth: 100,
        thumbnailHeight: 100,
        maxFilesizeRDF: 100, // MB
        maxFilesizeGRF: 10, // MB
        addRemoveLinks: true,
        dictRemoveFile: "Remove",
        addPropertiesLinks: false,
        acceptedFiles: ".rdf,.rdfs,.xml,.owl,.nt,.ttl,.n3,.nq,.rj,.trix,.trig,.brf,.csv"
      }
  );
  
  $("#tr_dropzone_dataset").dropzone(
      {
        url: Application.contextPath +"/upload",
        createImageThumbnails: true,
        maxThumbnailFilesize: 1,
        thumbnailWidth: 100,
        thumbnailHeight: 100,
        maxFilesizeRDF: 100, // MB
        maxFilesizeGRF: 10, // MB
        addRemoveLinks: true,
        dictRemoveFile: "Remove",
        addPropertiesLinks: false,
        acceptedFiles: ".csv"
      }
  );
  
  $("#savedataset").click(function(){
    $('#complete-dialog-spinner').modal('show');
    // open dialog
    
      var opts = {
      lines: 12,            // The number of lines to draw
      length: 7,            // The length of each line
      width: 5,             // The line thickness
      radius: 10,           // The radius of the inner circle
      rotate: 0,            // Rotation offset
      corners: 1,           // Roundness (0..1)
      color: '#5264ae',        // #rgb or #rrggbb
      direction: 1,         // 1: clockwise, -1: counterclockwise
      speed: 1,             // Rounds per second
      trail: 100,           // Afterglow percentage
      opacity: 1/4,         // Opacity of the lines
      fps: 20,              // Frames per second when using setTimeout()
      zIndex: 2e9,          // Use a high z-index by default
      className: 'spinner', // CSS class to assign to the element
      top: '30%',           // center vertically
      left: '50%',          // center horizontally
      position: 'relative'  // element position
    }
    var spinner = new Spinner(opts).spin();
    $("#containerl").empty();
    $("#containerl").append("<h4>Saving data page. Please wait ...</h4>");
    $("#containerl").append(spinner.el);
    
    
    
    var xhr = new XMLHttpRequest();
    url = Application.contextPath +"/BaseGateway/create.json";
    xhr.open("POST", url, true);
    // xhr.withCredentials = !!this.options.withCredentials;
    headers = {
           "Accept": "application/json",
           "Cache-Control": "no-cache",
           "X-Requested-With": "XMLHttpRequest"
         };
    for (headerName in headers) {
           headerValue = headers[headerName];
           xhr.setRequestHeader(headerName, headerValue);
         }
   xhr.onerror = (function(_this) {
           return function() {
            
           };
         })(this);
   xhr.onload = (function(_this) {
           return function(e) {
             spinner.stop();
             response = jQuery.parseJSON(xhr.responseText);
             if (response.error){
               $("#containerl").empty();
               $("#containerl").append("<h4>"+response.error.message+"</h4>");
             }else{
               $("#contenttype").attr('disabled', true);
               if( $("#setupdpp").hasClass('down')){
                 document.location=Application.contextPath +"/pages/publish/details.jsp";
               }else{
                 document.location=Application.contextPath +"/pages/myassets";
               }
             }
           };
         })(this);


    var formData = new FormData();
    
    formData.append("datapageid", $("#datapageid").val());
    formData.append("datasetname", $("#datasetname").val());
    formData.append("description", $("#description").val());
    formData.append("keyword", $("#keyword").tokenfield('getTokensList'));
    formData.append("licensing", $("#licensing").val());
    formData.append("usagerights", $("#usagerights").val());
    formData.append("bytesize", $("#bytesize").val());
    formData.append("portalparam", $("#portalparam").val());
    formData.append("portaltitle", $("#portaltitle").val());
    if ($("#contenttype").val()){
      formData.append("filecontenttype", $("#contenttype").val());
    }
    formData.append("public", $("#ispublic").is(":checked"));
    formData.append("israw", $("#savedataset").data("israw"));
    xhr.send(formData);

  });
  
//$("#deletedataset").click(function(){
//  document.location = Application.contextPath +"/pages/myassets";
//});
 
  $("#transfsearchsubmitbutton").click(function(){
    putTransformationTable(); 
  })
  
  $("#transformationpage").toggle();
  $("#usetransformation").click(function() {
	  if($(this).hasClass('down')){
          $(this).addClass('up').removeClass('down');
          $(this).addClass('up-arrow').removeClass('down-arrow');
      }else{
          $(this).addClass('down').removeClass('up');
          $(this).addClass('down-arrow').removeClass('up-arrow');
          putTransformationTable();
      }
	$("#transformationpage").toggle();
  });
  

  if ($("#content").hasClass("down")){
    $("#content").toggle();  
  }

 if ($("#setupdpp").hasClass("down")){
   $("#setupdpp").toggle();  
 }
  
  if ($("#dropzone_dataset").hasClass("down")){
    $("#dropzone_dataset").toggle();  
    
  }
  
  
  $("#updatedataset").click(function() {
    if($("#dropzone_dataset").hasClass('down')){
      $("#dropzone_dataset").addClass('up').removeClass('down');
      $(this).parent().addClass('theme-text'); //css("color", "rgba(0, 149, 135, 1)");
      $(this).addClass('theme-bg');
    }else{
        $("#dropzone_dataset").addClass('down').removeClass('up');
        $(this).parent().removeClass('theme-text'); //css("color", "initial");
        $(this).removeClass('theme-bg');
    }
  $("#dropzone_dataset").toggle();
  });
  
  
//  $("#adminpoligonaction").click(function(){
//    var fd = new FormData();
//    fd.append("title", $("#adminpoligon input[id=title]").val());
//    fd.append("file", $("#adminpoligon input[id=inputFile]")[0].files[0]);
//    
//    $.ajax({
//      url: Application.contextPath +"/admin/upload",
//      type: "POST",
//      data: fd,
//      enctype: 'multipart/form-data',
//      processData: false,  // tell jQuery not to process the data
//      contentType: false   // tell jQuery not to set contentType
//    }).done(function( data ) {
//        
//        console.log( data );
//    });
//  })
});

//function editContent(pc){
//  $("#dialogportal input[id=portalid]").val(pc.id);
//  $("#dialogportal input[id=title]").val(pc.title);
//  $("#dialogportal input[id=datePattern]").val(pc.datePattern);
//  
//  // convert  to 
//  var q = pc.query.replace(/<br>/g, "\r\n");
//  $("#dialogportal textarea[id=line_numbers]").val(q);
//  var d = pc.description.replace(/<br>/g, "\r\n");
//  $("#dialogportal textarea[id=pdescription]").val(d);
//  var s = pc.summary.replace(/<br>/g, "\r\n");
//  $("#dialogportal textarea[id=psummary]").val(s);
//  
//  $('#drawtype option[value="'+pc.chart+'"]').attr('selected', 'selected');
//  
//  iswrite = true;
//  
//  if (pc.chart == 'drawTimeLine'){
//    $(".date-pattern").show();
//  }
//  $( "#dialog-portal" ).dialog( "option", "buttons", { 
//    "Preview": function() { 
//        previewConfiguration();
//    },
//    "Save": function () {
//        addContent();
//        $(this).dialog('close');
//    }
//  });
//  $("#dialog-portal").dialog("open");
//}

function putTransformationTable(){
  $.ajax({
    type : "POST",
      async: false,
      url: Application.contextPath +"/BaseGateway/getTransformations.json",
      data: {
        q: ($("#transfsearchtext")) ? $("#transfsearchtext").val() : ''
        
      }
      }).done(function(data) {
        $("#transform-table tr").remove();
        $.map(data.result, function(el) {
        $("#transform-table tbody").append(
            "<tr>"+
              "<td>"+el.title+" </td>"+
              "<td>"+((el.description == null)? "" : el.description)+" </td>"+
              "<td id='"+el.id+"'><a href=\"javascript:selectTransformation('"+el.id+"')\" ><i class='mdi-action-search'></i></a> </td>"+
            "</tr>"
            );
        });
      });
}

function selectTransformation(tid){
  var t = encodeURI(tid);
    document.location = Application.contextPath +"/pages/transformations/details.jsp?id="+t;      
}

function addContent(){
  var xhr = new XMLHttpRequest();
  if ($("#dialogportal input[id=portalid]").val()!=null && $("#dialogportal input[id=portalid]").val().length>0){
    url = Application.contextPath +"/BaseGateway/editConfiguration.json";
  }else{
    url = Application.contextPath +"/BaseGateway/addConfiguration.json";
  }
  
  xhr.open("POST", url, true);
  // xhr.withCredentials = !!this.options.withCredentials;
  headers = {
         "Accept": "application/json",
         "Cache-Control": "no-cache",
         "X-Requested-With": "XMLHttpRequest"
       };
  for (headerName in headers) {
         headerValue = headers[headerName];
         xhr.setRequestHeader(headerName, headerValue);
       }
 xhr.onerror = (function(_this) {
         return function() {
           $(this).dialog("close");
         };
       })(this);
 xhr.onload = (function(_this) {
         return function(e) {
          // put tr in content table
           
          var response = xhr.responseText;
          if (xhr.getResponseHeader("content-type") && ~xhr.getResponseHeader("content-type").indexOf("application/json")) {
            try {
              response = JSON.parse(response);
            } catch (_error) {
              e = _error;
              response = "Invalid JSON response from server.";
            }
          }

          response = response.result;

          if ($("#dialogportal input[id=portalid]").val()!=null && $("#dialogportal input[id=portalid]").val().length>0){
            $("#portal-table tbody tr[id="+response.id+"] td").remove();
            html =  "<td >"+response.chartLabel+"</td>"+
            "<td >"+response.title+"</td>"+
            "<td class=\"tableAction\">"+
              "<a class=\"aimg\"  title=\"Edit\" href=\"javascript:editConfiguration({id: '"+response.id+"', title: '"+response.title+"', description: '"+escape(response.descriptionHtml)+"', summary: '"+escape(response.summaryHtml)+"',  datePattern: '"+escape(response.datePattern)+"', query: '"+escape(response.queryHtml)+"', chart: '"+response.chart+"'} )\" >"+
                "<i class=\"mdi-content-create theme-text\"></i></a> "+
                    "<a  class=\"confirmation aimg\" href=\"javascript:deleteContent('"+response.id +"')\" title=\"Delete\">"+
                      "<i class=\"mdi-content-remove-circle-outline theme-text\"></i></a>"+
                  "</td>";
            $("#portal-table tbody tr[id="+response.id+"]").append(html);
          }else{
            var html = "<tr id=\""+response.id +"\">"+
            "<td >"+response.chartLabel+"</td>"+
            "<td >"+response.title+"</td>"+
            "<td class=\"tableAction\">"+
              "<a class=\"aimg\" title=\"Edit\" href=\"javascript:editConfiguration({id: '"+response.id+"', title: '"+response.title+"', description: '"+escape(response.descriptionHtml)+"', summary: '"+escape(response.summaryHtml)+"',  datePattern: '"+escape(response.datePattern)+"', query: '"+escape(response.queryHtml)+"', chart: '"+response.chart+"'} )\" >"+
                "<i class=\"mdi-content-create theme-text\"></i></a> "+
                    "<a  class=\"confirmation aimg\" href=\"javascript:deleteContent('"+response.id +"')\" title=\"Delete\">"+
                      "<i class=\"mdi-content-remove-circle-outline theme-text\"></i></a>"+
                  "</td>"+
          "</tr>";
            $("#portal-table tbody").append(html);
          }
          $("#dialog-portal").dialog("close");
         };
       })(this);
 var formData = new FormData();
 if ($("#dialogportal input[id=portalid]").val()!=null && $("#dialogportal input[id=portalid]").val().length>0){
   formData.append("portalid", $("#dialogportal input[id=portalid]").val());
 }
 formData.append("portalparam", $("#portalparam").val());
 formData.append("portaltitle", $("#dialogportal input[id=portaltitle]").val());
 formData.append("query", $("#dialogportal textarea[id=line_numbers]").val());
 formData.append("drawtype", $("#dialogportal select[id=drawtype]").val());
 formData.append("title", $("#dialogportal input[id=title]").val());
 formData.append("description", $("#dialogportal textarea[id=pdescription]").val());
 formData.append("summary", $("#dialogportal textarea[id=psummary]").val());
 formData.append("datePattern", $("#dialogportal input[id=datePattern]").val());
 formData.append("poligon", $("#dialogportal select[id=poligon]").val());

 xhr.send(formData);
  
}


function deleteContent(trid){
  var xhr = new XMLHttpRequest();
    url = Application.contextPath +"/BaseGateway/deleteConfiguration.json";
  
  xhr.open("POST", url, true);
  // xhr.withCredentials = !!this.options.withCredentials;
  headers = {
         "Accept": "application/json",
         "Cache-Control": "no-cache",
         "X-Requested-With": "XMLHttpRequest"
       };
  for (headerName in headers) {
         headerValue = headers[headerName];
         xhr.setRequestHeader(headerName, headerValue);
       }
 xhr.onerror = (function(_this) {
         return function() {
           $(this).dialog("close");
         };
       })(this);
 xhr.onload = (function(_this) {
         return function(e) {
          // put tr in content table
           
          var response = xhr.responseText;
          if (xhr.getResponseHeader("content-type") && ~xhr.getResponseHeader("content-type").indexOf("application/json")) {
            try {
              response = JSON.parse(response);
            } catch (_error) {
              e = _error;
              response = "Invalid JSON response from server.";
            }
          }

          response = response.result;

          if (response){
            $("#portal-table tbody tr[id="+trid+"] td").remove();
          }
          $("#dialog-portal").dialog("close");
         };
       })(this);
 var formData = new FormData();
 formData.append("portalid",trid);
 formData.append("portalparam", $("#portalparam").val());
 

 xhr.send(formData);
}