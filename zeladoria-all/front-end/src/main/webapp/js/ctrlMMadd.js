var addMM = (function($, module) {
	var list = $('#list li').map(function() {
		return $(this).text();
	});
	// security rules
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	var roleEdit = $.inArray('ROLE_MESSAGEMODEL_UPDATE', list) >= 0;
	var roleEnabled = $.inArray('ROLE_MESSAGEMODEL_ENABLE', list) >= 0;
	// cached DOM
	// general module
	var $generalModule = $("#generalModule");
	//title
	var $title=$generalModule.find("#title2");
	// Field Module
	var $formModule = $generalModule.find("#IForm");
	var $inId = $formModule.find("#CId");
	var $inEnabled = $formModule.find("#CEnabled");
	var $inName = $formModule.find("#CName");
	var $inSubject = $formModule.find("#CSubject");
	var $inMessage = $formModule.find("#CMessage");
	var $spanCount= $formModule.find('#textarea_feedback');
	// buttons
	var $buttonModule = $formModule.find(".buttonModule");
	var $btAdd = $buttonModule.find("#btAdd");
	var $btEdit = $buttonModule.find("#btEdit");
	// bind actions
	$btAdd.on('click', add);
	$btEdit.on('click', edit);
	   $inMessage.keyup(function(){
		   charCount(1000);
	   });
	// modal
	var $modalE = $generalModule.find("#modalE");
	var $notificacoesE = $generalModule.find("#notificacoesE");
	// templates
	var $messageTpl = $("#message-template").html();
	var $validateTpl = $("#validateMessage-template").html();
	var $errorMessageTpl = $("#errorMessage-template").html();
	// functions
	var changeButton = function() {
		$btAdd.hide();
		$btEdit.show();
	}
	var setFields = function(id, name, subject, message, enabled) {
		$inId.val(id);
		$inEnabled.val(enabled);
		$inName.val(name);
		$inSubject.val(subject);
		$inMessage.val(message);
	}
	var getFields = function() {
		return JSON.stringify({
			'messageModelId' : $inId.val(),
			'enabled' : $inEnabled.val(),
			'name' : $inName.val(),
			'subject' : $inSubject.val(),
			'messageBody' : $inMessage.val()
		});
	}
	function edit(event) {
		event.preventDefault();
		// validaISalvar();
		if ($formModule.valid()) {
			$
					.ajax({
						type : 'PUT',
						url : '../rest/messagemodel2',
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
										"Modelos de Mensagem",
										data.message,
										1);
							}).fail(
							function(jqXHR, textStatus, errorThrown) {
								var body = jqXHR.responseJSON.errorCode + ", "
										+ jqXHR.responseJSON.errorMessage;
								modal("Modelos de Mensagem", body, 2);
							});

			return true;
		}
		modal("Modelos de Mensagem",
				"Por favor, preencha os campos destacados em vermelho!", 3);
		return false;

	}
	function add(event) {
		event.preventDefault();
		// validaISalvar();
		if ($formModule.valid()) {
			$.ajax({
				type : 'POST',
				url : '../rest/messagemodel2/',
				dataType : "json",
				data : getFields(),
				beforeSend : function(xhr) {
					xhr.setRequestHeader(header, token);
				},
				contentType : "application/json"
			}).done(
					function(data) {
						modal("Modelos de Mensagem",
								data.message, 1);
					}).fail(
					function(jqXHR, textStatus, errorThrown) {
						var body = jqXHR.responseJSON.errorCode + ", "
								+ jqXHR.responseJSON.errorMessage;
						modal("Modelos de Mensagem", body, 2);
					});
			return true;
		}
		modal("Modelos de Mensagem",
				"Por favor, preencha os campos destacados em vermelho!", 3);
		return false;
	}
	
	 
	   function charCount(text_max){
	       var text_length = $inMessage.val().length;
	       var text_remaining = text_max - text_length;
	      $spanCount.text(text_remaining + ' caracteres restantes');
	   }
	   
	
	// validations
	$formModule.validate({

		errorPlacement : function(error, element) {
			$(element).parent().addClass("has-error");
			error.insertAfter(element);
			error.wrap("<p>");
			error.css('color', 'red');
		},
		rules : {
			CName : {
				required : true,
				maxlength : 100
			},
			CSubject : {
				required : true,
				maxlength : 100
			},
			CMessage : {
				required : true,
				maxlength : 4000
			}
		},
		messages : {
			CName : {
				required : "Insira o Nome",
				maxlength : jQuery.validator
						.format("Por favor insira menos  que {0} caracteres.")
			},
			CSubject : {
				required : "Insira o Assunto",
				maxlength : jQuery.validator
						.format("Por  favor insira menos que {0} caracteres.")
			},
			CMessage : {
				required : "Insira o corpo da mensagem",
				maxlength : jQuery.validator
						.format("Por favor insira menos que {0}  caracteres.")
			}
		}
	});
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
		var id = messageModelId || getUrlParam('id');
		$.ajax({
			type : 'GET',
			url : '../rest/messagemodel2/' + id,
			dataType : "json",
			contentType : "application/json"
		}).done(
				function(data) {
					var enabled = (data.enabled === "Enabled") ? 0 : 1;
					setFields(data.messageModelId, data.name, data.subject,
							data.messageBody, enabled);
					charCount(1000);
				}).fail(
				function(jqXHR, textStatus, errorThrown) {
					var body = jqXHR.responseJSON.errorCode + ", "
							+ jqXHR.responseJSON.errorMessage;
					modal("Modelos de Mensagem", body, 2);
				});
	}

	function restError(jqXHR, textStatus) {
		var body = jqXHR.responseJSON.errorCode + ", "
				+ jqXHR.responseJSON.errorMessage;
		modal("Modelos de Mensagem", body, 2);
	}

	function loadMessageTeste() {
		loadMessage(123);
	}

	function init() {
		$inEnabled.val('0');
		if (getUrlParam('id')) {
			document.title = "Edição de Modelo de Mensagem";
			$title.text("").append("Edição de <strong>Modelo de Mensagem</strong>");
			changeButton();
			loadMessage()
		}else
			charCount(1000);
	}

	function saveValidationTest() {
		$.ajax({
			type : 'POST',
			url : '../rest/messagemodel2/',
			dataType : "json",
			data : JSON.stringify({
				messageBody : "askjdas",
				enabled : 1
			}),
			beforeSend : function(xhr) {
				xhr.setRequestHeader(header, token);
			},
			contentType : "application/json"
		}).done(
				function(data) {
					modal("Modelos de Mensagem",
							data.message, 1);
				}).fail(
				function(jqXHR, textStatus, errorThrown) {
					var body = jqXHR.responseJSON.errorCode + ", "
							+ jqXHR.responseJSON.errorMessage;
					modal("Modelos de Mensagem", body, 2);
				});

	}

	init();
	module.saveValidationTest = saveValidationTest;
	module.changeButton = changeButton;
	module.setFields = setFields;
	module.getFields = getFields;
	module.modal = modal;
	module.loadMessage = loadMessage;
	return module;
})(jQuery, addMM || {});