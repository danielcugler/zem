var MainModule = (function($) {
	var module = {};
	var newOnPage = true;
	var list = $('#list li').map(function() {
		return $(this).text();
	});
	// security rules
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	// var roleEdit = $.inArray('ROLE_MESSAGEMODEL_UPDATE', list) >= 0;
	// var roleEnabled = $.inArray('ROLE_MESSAGEMODEL_ENABLE', list) >= 0;
	// cached DOM
	// general module
	var $generalModule = $("#generalModule");
	var tamanho = $generalModule.length;
	// Search Module
	var $fieldsModule = $generalModule.find("#IForm");
	// fields
	var $inputId = $fieldsModule.find("#CId");
	var $inputEnabled = $fieldsModule.find("#CEnabled");
	var $inputName = $fieldsModule.find("#CName");
	var $inputSubject = $fieldsModule.find("#CSubject");
	var $inputMessageBody = $fieldsModule.find("#CMessage");
	// buttons
	var $saveButton = $fieldsModule.find("#saveButton");
	// modal
	var $modalE = $generalModule.find("#modalE");
	var $notificacoesE = $generalModule.find("#notificacoesE");
	// templates
	var $comboNameTpl = $("#combo-name-template").html();
	var $comboSubjectTpl = $("#combo-subject-template").html();
	var $edmodalTpl = $("#edmodal-template").html();
	var $edMessageTpl = $("#edmessage-template").html();
	// bind events

	// render
	function _render(baseTemplate, selector, data) {
		var template = Handlebars.compile(baseTemplate);
		var html = template(data);
		selector.html(html);
	}
	// functions
	function modal(title, body) {
		_render($edMessageTpl, $notificacoesE, {
			'title' : title,
			'body' : body
		});
		$modalE.modal('show');
	}
	$("#IForm").validate({

		errorPlacement : function(error, element) {
			$(element).parent().addClass("has-error");
			error.insertAfter(element);
			error.wrap("<p>");
			error.css('color', 'red');
		},
		rules : {

		}
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
															.format("Por favor insira menos que {0} caracteres.")
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
															.format("Por favor insira menos que {0} caracteres.")
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
															.format("Por favor insira menos que {0} caracteres.")
												}
											});
						});

	}
	;

	function modal(title, body) {
		_render($edMessageTpl, $notificacoesE, {
			'title' : title,
			'body' : body
		});
		$modalE.modal('show');
	}

	// search message model function
	function getFields(page) {
		return JSON.stringify({
			'messageModelId' : $inputId.val() || null,
			'enabled' : $inputEnabled.val() || 0,
			'name' : $inputName.val(),
			'subject' : $inputSubject.val(),
			'messageBody' : $inputMessageBody.val(),
		});
	}
	function setFields(message) {
		$inputId.val(message.messageModelId);
		$inputEnabled.val(message.enabled);
		$inputName.val(message.name);
		$inputSubject.val(message.subject);
		$inputMessageBody.val(message.messageBody);
	}

	var geturl = function getUrlVar(key) {
		var result = new RegExp(key + "=([^&]*)", "i")
				.exec(window.location.search);
		return result && unescape(result[1]) || "";
	}

	function startPage() {
		var id = geturl('id');
		if (id === '')
			$saveButton.on('click', function(event) {
				event.preventDefault();
				add();
			});
		else {
			getMessage(id);
			$saveButton.on('click', function(event) {
				event.preventDefault();
				edit();
			});
		}
	}
	;
	function getMessage(id) {
		$.ajax({
			type : 'GET',
			url : "http://localhost:8080/rest/messagemodel2/" + id,
			dataType : "json",
			contentType : "application/json"
		}).done(function(resp, status, xhr) {
			setFields(resp);
		}).fail(function(jqXHR, textStatus) {
			modal("Modelo de mensagem", jqXHR.responseJSON.errorMessage);
		});
	}
	function add(messageModel) {
		var message = messageModel || getFields();
		$.ajax(
				{
					type : 'POST',
					url : "http://localhost:8080/rest/messagemodel2/"
							+ $('#username').text(),
					data : message,
					dataType : "json",
					"Content-Type" : "application/json",
					beforeSend : function(xhr) {
						xhr.setRequestHeader(header, token);
					},
					contentType : "application/json"
				}).done(function(resp, status, xhr) {
					$modalE.one('hidden.bs.modal', function () {
						window.location.href = "http://localhost:8080/messagemodel2.jsp";	});
					modal("Modelo de mensagem", resp.message);
		}).fail(function(jqXHR, textStatus) {
			modal("Modelo de mensagem", jqXHR.responseJSON.errorMessage);
		});
	}
	function edit(messageModel) {
		var message = messageModel || getFields();
		$.ajax(
				{
					type : 'PUT',
					url : "http://localhost:8080/rest/messagemodel2/"
							+ $('#username').text(),
					data : message,
					dataType : "json",
					"Content-Type" : "application/json",
					beforeSend : function(xhr) {
						xhr.setRequestHeader(header, token);
					},
					contentType : "application/json"
				}).done(function(resp, status, xhr) {
					$modalE.one('hidden.bs.modal', function () {
						window.location.href = "http://localhost:8080/messagemodel2.jsp";	});	
					modal("Modelo de mensagem", resp.message);								
		}).fail(function(jqXHR, textStatus) {
			modal("Modelo de mensagem", jqXHR.responseJSON.errorMessage);
		});
	}
	startPage();
	module.setFields = setFields;
	module.getFields = getFields;
	return module;
})(jQuery);