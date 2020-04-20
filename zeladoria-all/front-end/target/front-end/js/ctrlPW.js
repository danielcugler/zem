var ctrlPW = (function($, module) {
	// var module = {};
	var newOnPage = true;
	var list = $('#list li').map(function() {
		return $(this).text();
	});
	// security rules
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	var roleEdit = $.inArray('ROLE_USER_UPDATE', list) >= 0;
	// cached DOM
	// general module
	var $generalModule = $("#generalModule");
	var tamanho = $generalModule.length;
	// Form module
	var $formModule = $generalModule.find("#IForm");
	// filters
	var $oldPass = $formModule.find("#oldPass");
	var $newPass = $formModule.find("#newPass");
	var $confirmPass = $formModule.find("#confirmPass");
	var $username = $formModule.find("#username");
	// buttons
	var $saveButton = $formModule.find("#save");
	// modal
	var $modalE = $generalModule.find("#modalE");
	var $notificacoesE = $generalModule.find("#notificacoesE");
	// templates
	var $messageTpl = $("#message-template").html();

	// bind events
	$saveButton.on('click', function(event) {
		event.preventDefault();
		changePass();
	});

	// validation
	$formModule
			.validate(
					{

						errorPlacement : function(error, element) {
							$(element).parent().addClass("has-error");
							error.insertAfter(element);
							error.wrap("<p>");
							error.css('color', 'red');
						},
						rules : {
							oldPass : {
								required : true,
								minlength : 8,
								maxlength : 12
							},
							newPass : {
								required : true,
								minlength : 8,
								maxlength : 12,
								mypassword : true
							},
							confirmPass : {
								required : true,
								minlength : 8,
								maxlength : 12,
								equalTo : "#newPass"
							}

						},
						messages : {
							oldPass : {
								required : "Insira a senha atual",
								minlength : jQuery.validator
										.format("A senha deve possuir no mínimo {0} caracteres."),
								maxlength : jQuery.validator
										.format("A senha deve possuir no máximo {0} caracteres.")
							},
							newPass : {
								required : "Insira a nova senha",
								minlength : jQuery.validator
										.format("A senha deve possuir no mínimo {0} caracteres."),
								maxlength : jQuery.validator
										.format("A senha deve possuir no máximo {0} caracteres.")
							},
							confirmPass : {
								required : "Insira a confimação da senha",
								equalTo : "As senhas inseridas devem ser iguais.",
								minlength : jQuery.validator
										.format("A senha deve possuir no mínimo {0} caracteres."),
								maxlength : jQuery.validator
										.format("A senha deve possuir no máximo {0} caracteres.")
							}

						}
					});
	jQuery.validator.addMethod('mypassword', function(value, element) {
		return this.optional(element)
				|| (value.match(/[a-zA-Z]/) && value.match(/[0-9]/));
	}, 'A senha deve conter letras e números.');

	// functions

	function modal(title, body,op) {
		_render($messageTpl, $notificacoesE, {
			'title' : title,
			'body' : body
		});
		if(op==1)
			$("#bOk1").show();
		else
			$("#bOk2").show();
		$modalE.modal({
			show : true,
			backdrop : true
		});
	}
	// fields
	function getFields() {
		return {
			'username': $username.val(),
			'oldPass' : $.md5($oldPass.val()),
			'newPass' : $.md5($newPass.val()),
			'confirmPass': $.md5($confirmPass.val())
		};
	}

	function setFields(oldPass, newPass, confirmPass) {
		$oldPass.val(oldPass);
		$newPass.val(newPass);
		$confirmPass.val(confirmPass);
	}
	
	function getForm(){
		return $formModule
				.serialize();
	}

	function changePass() {
		if (!$formModule.valid()) {
			modal("Alteração de senha",
					"Por favor, preencha os campos destacados em vermelho!");
			return false;
		}
		
		$.ajax({
			type : 'POST',
			async : false,
			url : '../rest/systemuser2/changepass',
			data : getFields(),
			beforeSend : function(xhr) {
				xhr.setRequestHeader(header, token);
			},
			dataType : "json",
			contentType : "application/x-www-form-urlencoded;charset=utf-8",
		}).done(function(resp) {
			modal("Alteração de senha", resp.message,1);
		}).fail(
				function(jqXHR, textStatus, errorThrown) {
			var title = "Alteração de senha";
			var body = jqXHR.responseJSON.errorCode + ", "
					+ jqXHR.responseJSON.errorMessage;
			modal(title, body,2);
		});
	};
	module.getForm = getForm;
	module.getFields = getFields;
	module.setFields = setFields;
	return module;
})(jQuery, ctrlPW || {});