var ctrlCTShow = (function($, module) {
	var list = $('#list li').map(function() {
		return $(this).text();
	});
	// security rules
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	var roleEdit = $.inArray('ROLE_USER_UPDATE', list) >= 0;
	var roleEnabled = $.inArray('ROLE_USER_ENABLE', list) >= 0;
	// cached DOM
	// general module
	var $generalModule = $("#generalModule");
	// var $serachFilters = $searchModule.find(".searchFilters");
	var $divLog = $generalModule.find("#detailedlog");
	// modal
	var $modalE = $generalModule.find("#modalE");
	var $notificacoesE = $generalModule.find("#notificacoesE");
	// templates
	var $logTpl = $("#log-template").html();
	var $messageTpl = $("#message-template").html();
	// functions
	function modal(title, body) {
		_render($edMessageTpl, $notificacoesE, {
			'title' : title,
			'body' : body
		});
		// $modalE.modal('show');
		$modalE.modal({
			show : true,
			backdrop : true
		});
	}
	
	function getUrlParam(key) {
		var result = new RegExp(key + "=([^&]*)", "i")
				.exec(window.location.search);
		return result && unescape(result[1]) || "";
	}
	
	function load() {
		$.ajax({
			type : 'GET',
			url : "../rest/log2/" + getUrlParam('id'),
			dataType : "json",
			contentType : "application/json"
		}).done(function(resp, status, xhr) {
			_render($logTpl, $divLog, resp);
		}).fail(
				function(jqXHR, textStatus) {
					var title = "Modelos de Mensagem";
					var body = jqXHR.responseJSON.errorCode + ", "
							+ jqXHR.responseJSON.errorMessage;
					modal(title, body);
				});
	}
	
	window.onbeforeunload = function(){
		alert("Clicou em voltar");
	}
	
	load();
	module.load = load;
	return module;
})(jQuery, ctrlCTShow || {});