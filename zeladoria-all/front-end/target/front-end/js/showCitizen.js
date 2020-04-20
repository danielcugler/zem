var showCT = (function($, module) {
	
//cached DOM
	var $generalModule = $("#generalModule");
	var $citizenDiv= $generalModule.find("#detailedcitizen");
	// modal
	var $modalE = $generalModule.find("#modalE");
	var $notificacoesE = $generalModule.find("#notificacoesE");
//tempaltes
	var $citizenTpl = $("#citizen-template").html();
	var $messageTpl = $("#message-template").html();
	//fucntions
	function modal(title, body) {
		_render($messageTpl, $notificacoesE, {
			'title' : title,
			'body' : body
		});
		$modalE.modal('show');
		}
	function geturl(key) {
		var result = new RegExp(key + "=([^&]*)", "i")
				.exec(window.location.search);
		return result && unescape(result[1]) || "";
	}
	function _render(baseTemplate, selector, data) {
		var template = Handlebars.compile(baseTemplate);
		var html = template(data);
		selector.html(html);
	}

	
	function loadCitizen(citizen_cpf) {
		var id = citizen_cpf || getUrl('id');
		$.ajax({
			type : 'GET',
			url : '../rest/citizen2/' + id,
			dataType : "json",
			contentType : "application/json"
		}).done(function(data) {
			_render($citizenTpl,$citizenDiv , data);
		}).fail(	function(jqXHR, textStatus) {
			var body = jqXHR.responseJSON.errorCode + ", "
			+ jqXHR.responseJSON.errorMessage;
	modal("Modelo de Mensagem", body);
});
	}
	loadCitizen(geturl('cpf'));
})(jQuery,showCT || {});