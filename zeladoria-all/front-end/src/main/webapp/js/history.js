var moduleHIST = (function($, module) {
	// cached DOM
	// general module
	var $generalModule = $("#generalModule");
	var tamanho = $generalModule.length;
	// Table module
	var $tableModule = $generalModule.find("#tabela");
	var $table = $tableModule.find("#resultado");
	var $thead = $table.find("#cthead");
	var $tbody = $table.find("#ctbody");
	// buttons
	var $buttonC = $generalModule.find("#buttonC");
	var $buttonCF = $generalModule.find("#buttonCF");
	// modal
	var $modalE = $generalModule.find("#modalE");
	var $notificacoesE = $generalModule.find("#notificacoesE");
	var $modalMidia = $generalModule.find("#modaldemidia");
	// templates
	var $messageTpl = $("#message-template").html();
	var $historyTpl = $("#hist-template").html();
	function init() {
		var id = getUrlParam('id');
		getHist(id);
		var opt = getUrlParam('opt');
		if (opt === "0")
			$buttonC.show();
		else
			$buttonCF.show();
	}
	function getHist(id) {
		$.ajax({
			type : 'GET',
			url : '../rest/unsolvedcall3/hist/' + id,
			dataType : "json",
			contentType : "application/json"
		}).done(function(resp, status, xhr) {
			_render($historyTpl, $tbody, resp);
		}).fail(
				function(jqXHR, textStatus) {
					modal("Historico", jqXHR.responseJSON.errorCode + ", "
							+ jqXHR.responseJSON.errorMessage);
				});
	}
	function modal(title, body) {
		_render($messageTpl, $notificacoesE, {
			'title' : title,
			'body' : body
		});
		// $modalE.modal('show');
		$modalE.modal({
			show : true,
			backdrop : true
		});
	}
	init();
	module.modal = modal;
	module.getHist = getHist;
	module.init = init;
})(jQuery, moduleHIST || {});