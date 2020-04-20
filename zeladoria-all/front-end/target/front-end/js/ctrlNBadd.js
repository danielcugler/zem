var addMM = (function($, module) {
	var list = $('#list li').map(function() {
		return $(this).text();
	});
	// security rules
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	// cached DOM
	// general module
	var $generalModule = $("#generalModule");
	// Field Module
	var $formModule = $generalModule.find("#IForm");
	var $inId = $formModule.find("#inId");
	var $inName = $formModule.find("#inName");
	// buttons
	var $buttonModule = $formModule.find(".buttonModule");
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
	var $validateTpl = $("#validateMessage-template").html();
	var $errorMessageTpl = $("#errorMessage-template").html();
	// functions
	var changeButton = function() {
		$btAdd.hide();
		$btEdit.show();
	}
	var setFields = function(data) {
		$inId.val(data.neighborhoodId);
		$inName.val(data.name);
	}
	var getFields = function() {
		return JSON.stringify({
			'neighborhoodlId' : $inId.val(),
			'name' : $inName.val(),
			'cityId' : {'cityId': 3551}
		});
	}
	function edit(event) {
		event.preventDefault();
		// validaISalvar();
		if ($formModule.valid()) {
			$
					.ajax({
						type : 'PUT',
						url : '../rest/neighborhood/' + $('#username').text(),
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
										"Inclusão de Bairros",
										"Bairros atualizado com sucesso!",
										1);
							}).fail(
							function(jqXHR, textStatus, errorThrown) {
								var body = jqXHR.responseJSON.errorCode + ", "
										+ jqXHR.responseJSON.errorMessage;
								modal("Modelos de Mensagem", body, 2);
							});

			return true;
		}
		modal("Inclusão de Bairros",
				"Por favor, preencha os campos destacados em vermelho!", 3);
		return false;

	}
	function add(event) {
		event.preventDefault();
		// validaISalvar();
		if ($formModule.valid()) {
			$.ajax({
				type : 'POST',
				url : '../rest/neighborhood/' + $('#username').text(),
				dataType : "json",
				data : getFields(),
				beforeSend : function(xhr) {
					xhr.setRequestHeader(header, token);
				},
				contentType : "application/json"
			}).done(
					function(data) {
						modal("Inclusão de Bairros",
								"Bairro inserido com sucesso!", 1);
					}).fail(
					function(jqXHR, textStatus, errorThrown) {
						var body = jqXHR.responseJSON.errorCode + ", "
								+ jqXHR.responseJSON.errorMessage;
						modal("Inclusão de Bairros", body, 2);
					});
			return true;
		}
		modal("Inclusão de Bairros",
				"Por favor, preencha os campos destacados em vermelho!", 3);
		return false;
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
			inName : {
				required : true,
				maxlength : 100
			}
		},
		messages : {
			inName : {
				required : "Insira o Nome",
				maxlength : jQuery.validator
						.format("Por favor insira menos  que {0} caracteres.")
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

	function loadMessage(neighborhoodId) {
		var neighborhood
		$.ajax({
			type : 'GET',
			url : '../rest/neighborhood/load',
			data: {'cityId': 3551,'neighborhoodId': parseInt(neighborhoodId)},
			dataType : "json",
			contentType : "application/json"
		}).done(
				function(data) {
					setFields(data);
				}).fail(
				function(jqXHR, textStatus, errorThrown) {
					var body = jqXHR.responseJSON.errorCode + ", "
							+ jqXHR.responseJSON.errorMessage;
					modal("Inclusão de Bairros", body, 2);
				});
	}

	function restError(jqXHR, textStatus) {
		var body = jqXHR.responseJSON.errorCode + ", "
				+ jqXHR.responseJSON.errorMessage;
		modal("Inclusão de Bairros", body, 2);
	}

	function loadMessageTeste() {
		loadMessage(123);
	}

	function init() {
		var id=getUrlParam('id');
		if (id) {
			changeButton();
			loadMessage(id)
		}
	}


	init();
	module.changeButton = changeButton;
	module.setFields = setFields;
	module.getFields = getFields;
	module.modal = modal;
	module.loadMessage = loadMessage;
	return module;
})(jQuery, addMM || {});