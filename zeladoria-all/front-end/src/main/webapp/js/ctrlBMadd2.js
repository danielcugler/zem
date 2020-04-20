var addBM = (function($, module) {
	var list = $('#list li').map(function() {
		return $(this).text();
	});
	// security rules
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	var roleEdit = $.inArray('ROLE_COMMUNICATION_UPDATE', list) >= 0;
	var roleEnabled = $.inArray('ROLE_COMMUNICATION_ENABLE', list) >= 0;
	// cached DOM
	// general module
	var $generalModule = $("#generalModule");
	// Field Module
	var $formModule = $generalModule.find("#IForm");
	var $inId = $formModule.find("#inId");
	var $inEnabled = $formModule.find("#inEnabled");
	var $inSubject = $formModule.find("#inSubject");
	var $inMessage = $formModule.find("#inMessageBody");
	var $comboBMC = $formModule.find("#comboBroadcastMessageCategory");
	//var $inLink = $formModule.find("#inLink");
	// buttons
	var $buttonModule = $formModule.find("#botoes");
	var $btAdd = $buttonModule.find("#btAdd");
	var $btEdit = $buttonModule.find("#btEdit");
	// bind actions
	$btAdd.on('click', add);
	$btEdit.on('click', edit);
	// modal
	var $modalE = $generalModule.find("#modalE");
	var $notificacoesE = $generalModule.find("#notificacoesE");
	// templates
	var $messageTpl = $("#message-template").html();
	var $bcComboTpl = $("#bmc-combo-template").html();
	//var $validateTpl = $("#validateMessage-template").html();
	//var $errorMessageTpl = $("#errorMessage-template").html();
	// functions
	function makeCombos() {
		$.ajax({
			type : 'GET',
			url : "../rest/broadcastmessagecategory/combos",
			dataType : "json",
			contentType : "application/json"
		}).done(function(resp, status, xhr) {
			_render($bcComboTpl, $comboBMC, resp);
		}).fail(function(jqXHR, textStatus) {
		});
	}
	function getUrl(key) {
		var result = new RegExp(key + "=([^&]*)", "i")
				.exec(window.location.search);
		return result && unescape(result[1]) || "";
	}
	var changeButton = function() {
		$btAdd.hide();
		$btEdit.show();
	}
	var setFields = function(id, subject, message, enabled,broadcastMessageCategoryId ,links) {
		$inId.val(id);
		$inEnabled.val(enabled);
		$inSubject.val(subject);
		$inMessage.val(message);
		$("#comboBroadcastMessageCategory option[value='"
				+ broadcastMessageCategoryId
				+ "']").prop('selected', true);
	//	$inLink.val(getByRel(links,0).link);
	}
	var getFields = function() {
		return JSON.stringify({
			'broadcastmessageId' : $inId.val(),
			'enabled' : $inEnabled.val(),
			'subject' : $inSubject.val(),
			'messageBody' : $inMessage.val(),
			'broadcastMessageCategoryId' : {
				'broadcastMessageCategoryId' : $comboBMC.val()
			}
			
		});
	}
	function edit(event) {
		event.preventDefault();
		validaISalvar();
		if ($formModule.valid()) {
			$
					.ajax({
						type : 'PUT',
						url : '../rest/broadcastmessage2/',
						dataType : "json",
						data : getFields(),
						beforeSend : function(xhr) {
							xhr.setRequestHeader(header, token);
						},
						contentType : "application/json"
					})
					.done(
							function(data) {
								modal(
										"Modelo de Mensagem",
										"Modelo de Mensagem atualização feita com sucesso!",
										1);
							}).fail(restError);

			return true;
		}
		modal("Modelo de Mensagem",
				"Por favor, preencha os campos destacados em vermelho!", 3);
		return false;

	}
	function add(event) {
		event.preventDefault();
		validaISalvar();
		if ($formModule.valid()) {
			$.ajax({
				type : 'POST',
				url : '../rest/broadcastmessage2/',
				dataType : "json",
				data : getFields(),
				beforeSend : function(xhr) {
					xhr.setRequestHeader(header, token);
				},
				contentType : "application/json"
			}).done(
					function(data) {
						modal("Modelo de Mensagem",
								"Modelo de Mensagem inserido com sucesso!", 1);
					}).fail(restError);
			return true;
		}
		modal("Modelo de Mensagem",
				"Por favor, preencha os campos destacados em vermelho!", 3);
		return false;
	}
	// render
	function _render(baseTemplate, selector, data) {
		var template = Handlebars.compile(baseTemplate);
		var html = template(data);
		selector.html(html);
	}
	// validations
	$formModule.validate({

		errorPlacement : function(error, element) {
			$(element).parent().addClass("has-error");
			error.insertAfter(element);
			error.wrap("<p>");
			error.css('color', 'red');
		},
		rules : {}
	});
	function validaISalvar() {
		$('[name="CName"]')
				.each(
						function() {
							$(this)
									.rules(
											'add',
											{
												required : true,
												maxlength : 100,
												messages : {
													required : "Insira o Nome",
													maxlength : jQuery.validator
															.format("Por favor insira menos  que {0} caracteres.")
												}
											});
						});
		$('[name="CSubject"]')
				.each(
						function() {
							$(this)
									.rules(
											'add',
											{
												required : true,
												maxlength : 100,
												messages : {
													required : "Insira o Assunto",
													maxlength : jQuery.validator
															.format("Por  favor insira menos que {0} caracteres.")
												}
											});
						});
		$('[name="CMessage"]')
				.each(
						function() {
							$(this)
									.rules(
											'add',
											{
												required : true,
												maxlength : 4000,
												messages : {
													required : "Insira o corpo da mensagem",
													maxlength : jQuery.validator
															.format("Por favor insira menos que {0}  caracteres.")
												}
											});
						});
	}
	;
	// functions
	// op: 1 sucesso, 2 erro de rest, 3 erro de validação
	function modal(title, body, op) {
		_render($messageTpl, $notificacoesE, {
			'title' : title,
			'body' : body
		});
		if (op == 1)
			$notificacoesE.find("#bOk1").show();
		else if (op == 2)
			$notificacoesE.find("#bOk2").show();
		else
			$notificacoesE.find("#bOk3").show();
		$modalE.modal('show');
	}

	function loadMessage(messageModelId) {
		var id = messageModelId || getUrl('id');
		$.ajax({
			type : 'GET',
			url : '../rest/broadcastmessage2/' + id,
			dataType : "json",
			contentType : "application/json"
		}).done(
				function(data) {
					var enabled = (data.enabled === "Enabled") ? 0 : 1;
					setFields(data.broadcastMessageId, data.subject,
							data.messageBody, data.enabled,data.broadcastMessageCategoryId.broadcastMessageCategoryId,data.links);
				}).fail(restError);
	}

	function restError(jqXHR, textStatus) {
		var body = jqXHR.responseJSON.errorCode + ", "
				+ jqXHR.responseJSON.errorMessage;
		modal("Modelo de Mensagem", body, 2);
	}

	function loadMessageTeste() {
		loadMessage(123);
	}

	function init() {
		$inEnabled.val('0');
		if (getUrl('id')) {
			changeButton();
			loadMessage()
		}
	}
	makeCombos();
	init();
	module.changeButton = changeButton;
	module.setFields = setFields;
	module.getFields = getFields;
	module.modal = modal;
	module.getUrl = getUrl;
	module.loadMessage = loadMessage;
	return module;
})(jQuery, addBM || {});
