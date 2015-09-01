var iswrite = false;
$(document).ready(function() {
 $('[data-toggle="tooltip"]').tooltip();
  $.material.init();

 $('.hover-star').rating({
    focus : function(value, link) {
      var tip = $('#feedback-rate');
      tip[0].data = tip[0].data || tip.html();
      tip.html(link.title || 'value: ' + value);
    },
    blur : function(value, link) {
      var tip = $('#feedback-rate');
      $('#feedback-rate').html(tip[0].data || '');
    }
  });


  $('#catalogresult tr td').click(function(event) {
    var v = $(this).parent().attr("id");
    $("#selectdataset input[id=id]").val(v);
    $('#selectdataset').submit();
    return true;
  });

  $('#transformationresult tr td').click(function(event) {
    var v = $(this).parent().attr("id");
    $("#selecttransformation input[id=id]").val(v);
    $('#selecttransformation').submit();
    return true;
  });

  $('#portalresult tr td').click(function(event) {
    var portal = $(this).parent().data().portal;
    document.location = Application.contextPath +"/pages/ddp/"+portal;
    return true;
  });

  $('#mycatalogresult tr td:not(:last-child)').click(function(event) {
    var v = $(this).parent().attr("id");
    $("#selectdataset input[id=id]").val(v);
    $('#selectdataset').submit();
    return true;
  });

  $('#mytransformationresult tr td:not(:last-child)').click(function(event) {
    var v = $(this).parent().attr("id");
    $("#selecttransformation input[id=id]").val(v);
    $('#selecttransformation').submit();
    return true;
  });


  $('#mytransformationresult a[id=previewdetail]').click(function(event) {
    var v = encodeURI($(this).parent().parent().parent().attr("id"));
    document.location = Application.contextPath + "/pages/transformations/details.jsp?id="+v;
    return false;
});

  $("#fbcreate").click(function() {
    $("#createoauth input[id=caction]").val("submitFB");
    $('#createoauth').submit();
    return true;
  });

  $("#gcreate").click(function() {
    $("#createoauth input[id=caction]").val("submitG");
    $('#createoauth').submit();
    return true;
  });

  $("#tcreate").click(function() {
    $("#createoauth input[id=caction]").val("submitT");
    $('#createoauth').submit();
    return true;
  });

  $("#fblogin").click(function() {
    $("#loginform input[id=action]").val("submitFB");
    $('#loginform').submit();
    return true;
  });

  $("#glogin").click(function() {
    $("#loginform input[id=action]").val("submitG");
    $('#loginform').submit();
    return true;
  });

  $("#tlogin").click(function() {
    $("#loginform input[id=action]").val("submitT");
    $('#loginform').submit();
    return true;
  });

  $("#singupdata").click(function(){
    // login
    var user = $("#singupdata").data("login");
    var isloginever =  $("#singupdata").data("isloginever");
    var hasdataset = $("#singupdata").data("hasdataset");
    if (user==null || user.length<=0){
        $('#complete-dialog-signin').modal('show');
    }else{
      if (hasdataset){
        document.location = Application.contextPath +"/pages/myassets";
      }else{
        document.location = Application.contextPath +"/pages/publish";
      }

    }
  });

  $("#publishmydata").click(function(){
    // login
    var user = $("#publishmydata").data("login");
    var isloginever =  $("#publishmydata").data("isloginever");
    var hasdataset = $("#publishmydata").data("hasdataset");
    if (user==null || user.length<=0){
      if (isloginever!=null && isloginever.length>0){
        $('#complete-dialog-signin').modal('show');
      }else{
        $('#complete-dialog-signup').modal('show');
      }

    }else{
      if (hasdataset){
        document.location = Application.contextPath +"/pages/myassets";
      }else{
        document.location = Application.contextPath +"/pages/publish";
      }

    }
  });

//  $("#searchsubmitbutton").click(function(){
//    $("#formsearch").submit();
//  });

  $("#signindialog").click(function(){
    $('#complete-dialog-signup').modal('hide');
    $('#complete-dialog-signin').modal('show');
  });


  $("#signupdialog").click(function(){
    $('#complete-dialog-signin').modal('hide');
    $('#complete-dialog-signup').modal('show');
  });


  $.tablesorter.addParser({
	    // set a unique id
	    id: 'myParser',
	    is: function(s) {
	      // return false so this parser is not auto detected
	      return false;
	    },
	    format: function(s, table, cell, cellIndex) {
	      // get data attributes from $(cell).attr('data-something');
	      // check specific column using cellIndex
	      return $(cell).attr('data-sort');
	    },
	    // set type, either numeric or text
	    type: 'text'
  });

  $("#catalogresult").tablesorter({
    headers : {
      2 : {
        sorter : "myParser"
      },
      3 : {
          sorter : false
      }
    },
    dateFormat : "pt"
  });


  $("#portalresult").tablesorter({
    headers : {
      2 : {
        sorter : "myParser"
      },
      3 : {
          sorter : false
      }
    },
    dateFormat : "pt"
  });

  $("#transformationresult").tablesorter({
    headers : {
      2 : {
        sorter : "myParser"
      }
    },
    dateFormat : "pt"
  });

  $("#mycatalogresult").tablesorter({
    headers : {
      1 : {
        sorter : "myParser"
      },
      3 : {
        sorter : false
      }
    },
    dateFormat : "pt"
  });

  $("#mytransformationresult").tablesorter({
    headers : {
      2 : {
        sorter : "myParser"
      }
    },
    dateFormat : "pt"
  });

  $('#line_numbers').linenumbers({col_width:'50px'});
  $("#querybutton").click(function(){
    $('#queryform').submit();
  });


  $("#dialog-portal").dialog({
    autoOpen : false,
    dialogClass : 'dialog-portal',
    width: '700px',
    buttons: {

  },
    title : "Configure widget",
    modal : true,
    close : function() {
    	iswrite = false;
    }
  });

  $("#keyword").tokenfield({
    delimiter: [',', ' ', ', ']
  });

//if ($("#dialogportal input[id=action]").val() == 'edit'){
      $( "#dialog-portal" ).dialog( "option", "buttons", {
      "Preview": function() {
          previewConfiguration();
      },
      "Save": function () {
          addContent();
          $(this).dialog('close');
      }
      });
//  }

  $("#dialog-preview-portal").dialog({
	    autoOpen : false,
	    dialogClass : 'dialog-wrapper',
	    width: 'auto',
	    buttons : {

	    },
	    title : "Query Preview",
	    modal : true,
	    close : function() {

	    }
	  });

  $("#exportfilebutton").click(function() {
    $("#dialogexport input[id='action']").val("export");
    $("#dialogexport").submit();
   // $("#dialog-export").dialog("close");

  });


  $("#exportraw").click(function() {
    $("#exportrawform input[id='action']").val("exportraw");
    $("#exportrawform").submit();
   // $("#dialog-export").dialog("close");

  });

  $(".confirmation").bind('click', function(e) {
	  var targetUrl = $(this).attr("href");
	  e.preventDefault();
	  fnOpenNormalDialog(targetUrl);
  });


//  //show selected tab for query and query constructor in dataset query results and dataset details
//  tabsControl('tab-section', 'query-tabs');
//
//  //show selected tab for results in dataset query results
//  tabsControl("result-tab-section", "tabs")


  //get checkbox values on change
  $( "#query-builder-results input" ).change(function() {
	  var checkboxValsMap = $("input:checkbox:checked").map(function(){
		  return $(this).val();
	  }).get();

	  var toSend = JSON.stringify(checkboxValsMap);
	  $.ajax({
	      type : "POST",
	        async: false,
	        url: Application.contextPath +"/BaseGateway/constructQuery.json",
	        data:{values: toSend}

	    }).done(function(data) {
	    	var html = "";
	    	html = data.result;
	    	$("#query-results textarea[id='line_numbers']").val(html);
	    });

  });

  $("#dialogportal textarea[id='line_numbers']").bind('change keyup keydown', function(event){
	  iswrite = true;
  });

  $( "#drawtype" ).change(function () {
    $( "#drawtype option:selected" ).each(function() {
      var val = $( this ).val();

      selectDrawType(val);


    });
  }).change();

  $("#createrawbutton").click(function(){
    //document.location = Application.contextPath +"/pages/publish/details.jsp";
    $("#createrawform input[id='israw']").val('israw');
    $("#createrawform").submit();
  });

  $("#uploadrawbutton").click(function(){
    var xhr = new XMLHttpRequest();
    url = Application.contextPath +"/BaseGateway/create.json";
    xhr.open("POST", url, true);
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
             // $("#contenttype").attr('disabled', true);
               document.location=Application.contextPath +"/pages/publish/details.jsp";
           };
         })(this);

    var formData = new FormData();

    xhr.send(formData);
  });

  $("#createnewtransformation").click(function(){
    window.open("//grafterizer.datagraft.net/transformations/new");

  });

  $("#execsrepository").click(function (){
    document.location=Application.contextPath +"/pages/publish/details.jsp";
  });

  $("#updaterepository").click(function() {
    var xhr = new XMLHttpRequest();
    url = Application.contextPath +"/BaseGateway/create.json";
    xhr.open("POST", url, true);
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
             // $("#contenttype").attr('disabled', true);
               document.location=Application.contextPath +"/pages/publish/details.jsp";
           };
         })(this);

    var formData = new FormData();

    xhr.send(formData);
  });


  $("#execdlresult").click(function (){
    $("#formexport input[id='action']").val("export");
    $("#formexport").submit();
  });

    $("#refreshbutton").click(function(){
//      $("#usetransformation").click(function() {
        if($("#usetransformation").hasClass('down')){
              $("#usetransformation").addClass('up').removeClass('down');
              $("#usetransformation").addClass('up-arrow').removeClass('down-arrow');
          }else{
              $("#usetransformation").addClass('down').removeClass('up');
              $("#usetransformation").addClass('down-arrow').removeClass('up-arrow');
              putTransformationTable();
          }
      $("#transformationpage").toggle();
      $('#dialog-refresh').modal('hide')
//      });
    });





    $("#pageTransformationLess").click( function(){
      if ($(this).hasClass( "disabled" )){
        return;
      }
      var pn = $("#transformationresult").data("page");
      if (pn == null){
        pn = pn = $("#mytransformationresult").data("page");
      }
      $("#pagetransformation input[id=page]").val(pn*1-1);
      $("#pagetransformation").submit();
    });

    $("#pageTransformationAdd").click(function(){
      if ($(this).hasClass( "disabled" )){
        return;
      }
      var pn = $("#transformationresult").data("page");
      if (pn == null){
        pn = pn = $("#mytransformationresult").data("page");
      }
      $("#pagetransformation input[id=page]").val(pn*1+1);
      $("#pagetransformation").submit();
    });

    $("#pageDatasetLess").click(function (){
      if ($(this).hasClass( "disabled" )){
        return;
      }
      var pn = $("#catalogresult").data("page");
      if (pn == null){
        pn = pn = $("#mycatalogresult").data("page");
      }
      $("#pagedataset input[id=page]").val(pn*1-1);
      $("#pagedataset").submit();
    });

    $("#pageDatasetAdd").click(function(){
      if ($(this).hasClass( "disabled" )){
        return;
      }
      var pn = $("#catalogresult").data("page");
      if (pn == null){
        pn = pn = $("#mycatalogresult").data("page");
      }
      $("#pagedataset input[id=page]").val(pn*1+1);
      $("#pagedataset").submit();
    });

    $("#transfsearchtext").keypress(function (e) {
      if (e.which == 13) {
        putTransformationTable();
        return false;
      }
    });

    
    
    
    $("#updateaccount").click(function(){
      $("#createoauth input[id=action]").val('update');
      $("#createoauth").submit();
    });
    
    $("#emailconfirm").click(function(){
        var xhr = new XMLHttpRequest();
        url = Application.contextPath +"/BaseGateway/sendconfirmemail.json";
        xhr.open("POST", url, true);
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
                 var response = jQuery.parseJSON(xhr.responseText);
                 if (response.result){
                   $(".emailmessage").html("We send new confirmation email. Please check yours outbox after few minutes.");
                 }else{
                   $(".emailmessage").html("We can't send email to you. Please try again later!");
                 }
               };
             })(this);
    
        var formData = new FormData();
        xhr.send(formData);
    });

    
    // 
    $("#changepassword").click(function(){
      var xhr = new XMLHttpRequest();
      url = Application.contextPath +"/BaseGateway/changepassword.json";
      xhr.open("POST", url, true);
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
               var response = jQuery.parseJSON(xhr.responseText);
               if (response.result == 'OK'){
                 $(".errormessage").html("Password changed successfully.");
               }else{
                 $(".errormessage").html(response.result);
               }
             };
           })(this);
  
      var formData = new FormData();
      formData.append("oldpass", $("#oldpassword").val());
      formData.append("newpass", $("#newpassword").val());
      formData.append("newconfirmpass", $("#confirmpassword").val());
      xhr.send(formData);
      
    });
});

var Application = {
  contextPath : "",
  state : ""

};


function forktransformation(id){
   var xhr = new XMLHttpRequest();
  url = Application.contextPath +"/BaseGateway/forkTransformation.json";
  xhr.open("POST", url, true);
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
           var response = jQuery.parseJSON(xhr.responseText);
           document.location=Application.contextPath +"/pages/transformations?id="+encodeURIComponent(response.result);
         };
       })(this);

  var formData = new FormData();
  formData.append("transformationId", id);

  xhr.send(formData);
}

function selectDrawType(val){
  $('.date-pattern').css('display', 'none');
  $('.poligon').css('display', 'none');
  if ('drawTable' == val){
    $("#dialogportal p").html('Tabular view shows entire result set from query as a table with corresponding columns and rows.');
    if (!iswrite || $("#dialogportal textarea[id='line_numbers']").val().length<=0){
      $("#dialogportal textarea[id='line_numbers']").val('');
      iswrite = false;
    }
  }
  if ('drawLineChart' == val){
    $("#dialogportal p").html('Query should have column "title", which is used as a X values. All other columns are plot as a lines.');
    if (!iswrite || $("#dialogportal textarea[id='line_numbers']").val().length<=0){
      $("#dialogportal textarea[id='line_numbers']").val('SELECT ?title \r\n WHERE { }');
      iswrite = false;
    }
  }
  if ('drawBarChart' == val){
    $("#dialogportal p").html('Query should have column "title", which is used as a X values. All other columns are plot as a series of bars.');
    if (!iswrite || $("#dialogportal textarea[id='line_numbers']").val().length<=0){
      $("#dialogportal textarea[id='line_numbers']").val('SELECT ?title \r\n WHERE { }');
      iswrite = false;
    }
  }
  if ('drawPieChart' == val){
    $("#dialogportal p").html('Query should have column "title", which values are used as a segment titles. All other columns are plot as a separate pies.');
    if (!iswrite || $("#dialogportal textarea[id='line_numbers']").val().length<=0){
      $("#dialogportal textarea[id='line_numbers']").val('SELECT ?title \r\n WHERE { }');
      iswrite = false;
    }
  }
  if ('drawPoligonChart' == val){
    $(".poligon").show();
    $("#dialogportal p").html('In the "Poligon chart" is required columns "code" and "value".');
    if (!iswrite || $("#dialogportal textarea[id='line_numbers']").val().length<=0){
      $("#dialogportal textarea[id='line_numbers']").val('SELECT ?code ?value \r\n WHERE { }');
      iswrite = false;
    }
  }
  if ( 'drawTimeLine' == val ){
    $(".date-pattern").show();
    $("#dialogportal p").html('In the "Time Line chart" is required the heading column to be called "title", date column called "date" and every other column are shown like additional data.');
    if (!iswrite || $("#dialogportal textarea[id='line_numbers']").val().length<=0){
      $("#dialogportal textarea[id='line_numbers']").val('SELECT ?title ?date \r\n WHERE { }');
      iswrite = false;
    }
  }
  if ('googleMaps'== val){
    $("#dialogportal p").html('Map requires columns "lng" and "lat". All other column are used as additional data for the geo location.');
    if (!iswrite || $("#dialogportal textarea[id='line_numbers']").val().length<=0){
      $("#dialogportal textarea[id='line_numbers']").val('SELECT ?title ?lng ?lat \r\n WHERE { }');
      iswrite = false;
    }
  }
  if ('drawMaps' == val){
    $("#dialogportal p").html('In the "Map chart" is required columns "title, "lng" and "lat", every other column are shown like additional data with title column called "title".');
    if (!iswrite || $("#dialogportal textarea[id='line_numbers']").val().length<=0){
      $("#dialogportal textarea[id='line_numbers']").val('SELECT ?title ?lng ?lat \r\n WHERE { }');
      iswrite = false;
    }
  }
  if ('drawScatterChart' == val){
      $("#dialogportal p").html('Query should have column "title", which is used as a X values. All other columns are used as a Y values.');
      if (!iswrite || $("#dialogportal textarea[id='line_numbers']").val().length<=0){
        $("#dialogportal textarea[id='line_numbers']").val('SELECT ?title  \r\n WHERE { }');
        iswrite = false;
      }
    }
    if ('drawBubbleChart' == val){
      $("#dialogportal p").html('Query should have columns "title" (used as a X values), "y" (used as a Y values) and "r" (used as a radius of a bubble). All other column are used as additional data for the bubble.');
      if (!iswrite || $("#dialogportal textarea[id='line_numbers']").val().length<=0){
        $("#dialogportal textarea[id='line_numbers']").val('SELECT ?title ?y ?r  \r\n WHERE { }');
        iswrite = false;
      }
    }
}

function tabsControl(tabSection, tabId) {
	var tabSectionClass = '.'+tabSection;
	var tabIdEl = '#'+tabId+' a';
	
	$(tabSectionClass).hide();
	
	$(tabIdEl).bind('click', function(e) {
		$(tabIdEl+'.current').removeClass('current');
		$(tabSectionClass+':visible').hide();
		$(this.hash).show();
		$(this).addClass('current');
		drawGoogleMaps.gmap();
	  e.preventDefault();
	}).filter(':first').click();
}

function selectContentType() {
	$( "#filetype" ).change(function () {

	    $( "#filetype option:selected" ).each(function() {
	      var val = $( this ).val();

	      if (val == 'RDF'){
	        addRDFOptions();
	      }
	      if (val == 'GRF'){
	        addXLSOptions();
	      }
	    });
	  })
	  .change();
}

function selectActual(content, type, transformation){
  $("#filetype option[value='" +content+"']").attr('selected','selected');
  selectContentType();
  if ('GRF' != content){
    $("#contenttype option[value='"+type+"']").attr('selected','selected');
  }else{
    $("#transformation").val(transformation);
  }


}

function deleteDataset(link) {
  $("#deletedataset input[id=id]").val(encodeURI(link));
  $("#deletedataset input[id=delete]").val("dataset");
  $('#deletedataset').submit();
}

function deleteTransformation(link) {
  $("#deletedataset input[id=id]").val(link);
  $("#deletedataset input[id=delete]").val("transformation");
  $('#deletedataset').submit();
}

function addRDFOptions(){
  $('#contenttype option').remove();
  var myOptions = [{ text: 'RDF/XML', value: "application/rdf+xml"},
                   {text : 'N-Triples', value: "text/plain"},
                   { text: 'Turtle', value: "text/turtle"},
                   {text : 'N3', value: "text/rdf+n3"},
                   { text: 'N-Quads', value: "text/x-nquads"},
                   {text : 'RDF/JSON', value: "application/rdf+json"},
                   { text: 'TriX', value: "application/trix"},
                   {text : 'TriG', value: "application/x-trig"},
                   { text: 'Sesame Binary RDF', value: "application/x-binary-rdf"}];
   $.each(myOptions, function(i, el)
  {
     $('#contenttype').append( new Option(el.text,el.value) );
  });
}
function addCSVOptions(){
  $('#contenttype option').remove();
  var myOptions = [{ text: 'CSV', value: "text/csv"}];
   $.each(myOptions, function(i, el)
  {
     $('#contenttype').append( new Option(el.text,el.value) );
  });
}

function addXLSOptions(){
  $('#contenttype option').remove();
  $("#transformation").css('visibility', 'visible');
  $("#contenttype").css('visibility', 'hidden')

  // get list from json service




}

function openDialogPortal(){
  $("#dialogportal input[id=portalid]").val("");
  $("#dialogportal input[id=title]").val("");
  $("#dialogportal input[id=datePattern]").val("");
  $("#dialogportal textarea[id=line_numbers]").val("");
  $("#dialogportal textarea[id=pdescription]").val("");
  $("#dialogportal textarea[id=psummary]").val("");
  $('#drawtype option[value="drawTable"]').attr('selected', 'selected');
  selectDrawType("drawTable");
  iswrite=false;
  $("#dialog-portal").dialog("open");
}

function editConfiguration(pc){
  $("#dialogportal input[id=portalid]").val(pc.id);
  $("#dialogportal input[id=title]").val(pc.title);
  $("#dialogportal input[id=datePattern]").val(pc.datePattern);

  // convert  to
  var q = pc.query.replace(/<br>/g, "\r\n");
  $("#dialogportal textarea[id=line_numbers]").val(q);
  var d = pc.description.replace(/<br>/g, "\r\n");
  $("#dialogportal textarea[id=pdescription]").val(d);
  var s = pc.summary.replace(/<br>/g, "\r\n");
  $("#dialogportal textarea[id=psummary]").val(s);

  $('#drawtype option[value="'+pc.chart+'"]').attr('selected', 'selected');
  selectDrawType(pc.chart);
  iswrite = true;

  if (pc.chart == 'drawTimeLine'){
    $(".date-pattern").show();
  }
  if (pc.chart == 'drawPoligonChart'){
    $(".poligon").show();
  }
  $("#dialog-portal").dialog("open");
}

function previewConfiguration(){
    Service.param=$("#portalparam").val();
    var query = $("#dialogportal textarea[id=line_numbers]").val();
    var chartType = "";
    var poligonId = "";
    $( "#drawtype option:selected" ).each(function() {
      chartType = $( this ).val();
    });
    if ($(".poligon").is(":visible")){
      $( "#poligon option:selected" ).each(function() {
        poligonId = $( this ).val();
      });
    }
    Service.executeQuery(query, chartType, poligonId);
}

function fnOpenNormalDialog(targetUrl) {
    $("#dialog-confirm").html("<span class='ui-icon ui-icon-alert'></span>This item will be permanently deleted and cannot be recovered. <br/>Are you sure?</p></span>");

    $('#dialog-confirm').dialog({
        resizable: false,
        modal: true,
        dialogClass : 'dialog-wrapper',
        width: 'auto',
        buttons: {
            "Yes": function () {
                $(this).dialog('close');
                window.location.href = targetUrl;
            },
                "No": function () {
                $(this).dialog('close');

            }
        }
    });
    $("#dialog-confirm").dialog("open");
}

function pageTransformationLess(){
  var pn = $("#transformationresult").data("page");
  $("#pagetransformation input[id=page]").val(pn*1-1);
  $("#pagetransformation").submit();
}

function pageTransformationAdd(){
  var pn = $("#transformationresult").data("page");
  $("#pagetransformation input[id=page]").val(pn*1+1);
  $("#pagetransformation").submit();
}

function pageDatasetLess(){
  var pn = $("#catalogresult").data("page");
  $("#pagedataset input[id=page]").val(pn*1-1);
  $("#pagedataset").submit();
}

function pageDatasetAdd(){
  var pn = $("#catalogresult").data("page");
  $("#pagedataset input[id=page]").val(pn*1+1);
  $("#pagedataset").submit();
}