var addEC = (function($, module) {
	var list = $('#list li').map(function() {
		return $(this).text();
	});
	// security rules
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	var roleEdit = $.inArray('ROLE_ENTITYCATEGORY_UPDATE', list) >= 0;
	var roleEnabled = $.inArray('ROLE_ENTITYCATEGORY_ENABLE', list) >= 0;
	// cached DOM
	// general module
	var $generalModule = $("#generalModule");
	//title
	var $title = $generalModule.find("#title2");
	// Field Module
	var $formModule = $generalModule.find("#IForm");
	var $inEntityCategoryId = $formModule.find("#inEntityCategoryId");
	var $inName = $formModule.find("#inName");
	var $inSendMessage = $formModule.find("#inSendMessage");
	var $inEnabled = $formModule.find("#inEnabled");
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
	function getUrl(key) {
		var result = new RegExp(key + "=([^&]*)", "i")
				.exec(window.location.search);
		return result && unescape(result[1]) || "";
	}
	var changeButton = function() {
		$btAdd.hide();
		$btEdit.show();
	}
	var setFields = function(id, name, send_message, enabled) {
		$inEntityCategoryId.val(id);
		$inName.val(name);
		if (send_message === 'ENABLED') {
			$inSendMessage.attr('checked', true);
			$('.icheckbox_square-aero').addClass("checked");
		}
		$inEnabled.val(enabled === 'Enabled' ? 0 : 1);
	}
	var getFields = function() {
		return JSON.stringify({
			'entityCategoryId': $inEntityCategoryId.val(),
			'name' : $inName.val(),
			'send_message' : $inSendMessage.is(
			':checked') ? 0 : 1,
			'enabled' : $inEnabled.val()
		});
	}
	function edit(event) {
		event.preventDefault();
		validaISalvar();
		if ($formModule.valid()) {
			$
					.ajax({
						type : 'PUT',
						url : '../rest/entitycategory2/' + $('#username').text(),
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
										"Categoria de Entidade",
										data.message,
										1);
							}).fail(restError);

			return true;
		}
		modal("Categoria de Entidade",
				"Por favor, preencha os campos destacados em vermelho!", 3);
		return false;
	}
	
	function add(event) {
		event.preventDefault();
		validaISalvar();
		if ($formModule.valid()) {
			$.ajax({
				type : 'POST',
				url : '../rest/entitycategory2/' + $('#username').text(),
				dataType : "json",
				data : getFields(),
				beforeSend : function(xhr) {
					xhr.setRequestHeader(header, token);
				},
				contentType : "application/json"
			}).done(
					function(data) {
						modal("Categoria de Entidade",
								data.message, 1);
					})
					.fail(restError);
			return true;
		}
		modal("Categoria de Entidade",
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
		$('[name="inName"]')
				.each(
						function() {
							$(this)
									.rules(
											'add',
											{
												required : true,
												maxlength : 40,
												messages : {
													required : "Insira o Nome",
													maxlength : jQuery.validator
															.format("Por favor insira menos que {0} caracteres.")
												}
											});
						});

	};

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

	function loadMessage(entitycatyegoryId) {
		var id = entitycatyegoryId || getUrl('id');
		$.ajax({
			type : 'GET',
			url : '../rest/entitycategory2/' + id,
			dataType : "json",
			contentType : "application/json"
		}).done(
				function(data) {
					var enabled = (data.enabled === "Enabled") ? 0 : 1;
					setFields(data.entityCategoryId, data.name,
							data.send_message, enabled);
				}).fail(restError);
	}

	function restError(jqXHR, textStatus) {
		var body = jqXHR.responseJSON.errorCode + ", "
				+ jqXHR.responseJSON.errorMessage;
		modal("Categoria de Entidade", body, 2);
	}

	function loadMessageTeste() {
		loadMessage(123);
	}

	function init() {
		$inEnabled.val('0');
		if (getUrl('id')) {
			document.title = "Edição de Categoria de Entidade";
			$title.text("").append("Edição de <strong>Categoria de Entidade</strong>");
			changeButton();
			loadMessage()
		}
	}

	init();
	module.changeButton = changeButton;
	module.setFields = setFields;
	module.getFields = getFields;
	module.modal = modal;
	module.getUrl = getUrl;
	module.loadMessage = loadMessage;
	return module;
})(jQuery, addEC || {});