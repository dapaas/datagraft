(function($){
	$.fn.linenumbers = function(in_opts){
		var opt = $.extend({
			col_width: '25px',
			start: 1,
			digits: 4.
		},in_opts);
		return this.each(function(){
			var new_textarea_width = (parseInt($(this).css('width'))-parseInt(opt.col_width))+'px';
			$(this).before('<div style="width:3.0em"><textarea style="color: #AAAAAA;padding-right: 0.2em;text-align: right;white-space:pre;overflow:hidden;" disabled="disabled"></textarea>');
			$(this).after('<div style="clear:both;"></div></div>');
			/*$(this).css({'width':new_textarea_width});*/
			var lnbox = $(this).parent().find('textarea[disabled="disabled"]');
			$(this).bind('blur focus change keyup keydown',function(){
				var lines = "\n"+$(this).val();
				lines = lines.match(/[^\n]*\n[^\n]*/gi);
				var line_number_output='';
				$.each(lines,function(k,v){
					if(k!=0){
						line_number_output += "\n";
					}
					lencheck = k+opt.start+'!';
					line_number_output += (k+opt.start)+':'+v.replace(/\n/gi,'').replace(/./gi,'').substr(opt.digits+1);
				});
				$(lnbox).val(line_number_output);
			    $(lnbox).scrollTop($(this).scrollTop());
			});
			$(this).scroll(function(){
			    $(lnbox).scrollTop($(this).scrollTop());
			});
			$(this).trigger('keyup');
		});
	};
})(jQuery);