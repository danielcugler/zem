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
	var $divCitizen = $generalModule.find("#detailedcitizen");
	// modal
	var $modalE = $generalModule.find("#modalE");
	var $notificacoesE = $generalModule.find("#notificacoesE");
	// templates
	var $citizenTpl = $("#citizen-template").html();
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
			url : "../rest/citizen2/"+getUrlParam('cpf'),
			dataType : "json",
			contentType : "application/json"
		}).done(function(resp, status, xhr) {
			_render($citizenTpl, $divCitizen, resp);
		}).fail(function(jqXHR, textStatus) {
			var title = "Modelos de Mensagem";
			var body = jqXHR.responseJSON.errorCode + ", "
					+ jqXHR.responseJSON.errorMessage;
			modal(title, body);
		});
	}
	load();
	module.load=load;
	return module;
})(jQuery, ctrlCTShow || {});