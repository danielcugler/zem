var ctrlMU = (function($, module) {
	// cached DOM
	// general module
	var $generalModule = $("#generalModule");
	// form module
	var $formModule = $generalModule.find("#IForm");
	// fields
	var $email = $formModule.find("#inEmail");
	var $password = $formModule.find("#inPassword");
	// buttons
	var $btLogin = $formModule.find("#login");
	// modal
	var $modalE = $generalModule.find("#modalE");
	var $notificacoesE = $generalModule.find("#notificacoesE");
	// templates
	var $messageTpl = $("#message-template").html();
	// bind
	$btLogin.click(function(event) {
		event.preventDefault();
		login();
	})
	// render
	function _render(baseTemplate, selector, data) {
		var template = Handlebars.compile(baseTemplate);
		var html = template(data);
		selector.html(html);
	}
	// functions

	$formModule.validate({
		errorPlacement : function(error, element) {
			$(element).parent().addClass("has-error");
			error.insertAfter(element);
			error.wrap("<p>");
			error.css('color', 'red');
		},
		rules : {

			inEmail : {
				maxlength : 100,
				email : true,
				required : true
			},
			inPassword : {
				minlength : 8,
				maxlength : 12,
				required : true
			}
		},
		messages : {
			inEmail : {
				required : "Insira o email.",
				email : "Insira um email válido.",
				maxlength : jQuery.validator
						.format("Por favor, insira menos que {0} caracteres.")
			},
			inPassword : {
				required : "Insira a senha.",
				minlength : jQuery.validator
						.format("Por favor, insira mais que {0} caracteres."),
				maxlength : jQuery.validator
						.format("Por favor, insira menos que {0} caracteres.")
			}
		}
	});

	function getFields() {
		var login = {
			'email' : $email.val(),
			'password' : $password.val()
		};
		return login;
	}

	function setFields(name, password) {
		$name.val(name);
		$passowrd.val(password);
	}
	function login() {
		if(!$formModule.valid()){
			modal("Chamado interno",
					"Por favor, preencha os campos destacados em vermelho!");
			return false;
		}
		$.ajax({
			type : 'GET',
			url : '../rest/citizen2/login',
			dataType : "json",
			data : getFields(),
			contentType : "application/json",
		}).done(
				function(resp, textStatus, jqXHR) {
					window.location = '../newcall.jsp?citizen_cpf='
							+ resp.citizen_cpf + '&email=' + resp.email;
				}).fail(
				function(jqXHR, textStatus) {
					var title = "Identificação";
					var body = jqXHR.responseJSON.errorCode + ", "
							+ jqXHR.responseJSON.errorMessage;
					modal(title, body);
				});
	}
	function modal(title, body) {
		_render($messageTpl, $notificacoesE, {
			'title' : title,
			'body' : body
		});
		$modalE.modal({
			show : true,
			backdrop : true
		});
	}
	module.setFields = setFields;
	module.getFields = getFields;
	return module;

})(jQuery, ctrlMU || {});
/*
var str1 = "Usuário Móvel";
var str2 = "Usuário Móvel";
var str3 = "o";
var resizefunc = [];

function loadInputs(data) {
	data.jsonList = JSON.parse(data.jsonList);
	$('#form-username').val(data.jsonList.usernameMobile);
	$('#form-password').val(data.jsonList.passwordMobile);
}


 * 
 * $(document) .ready( function() {
 * 
 * $('.login-form') .validate( { errorPlacement : function(error, element) {
 * $(element).parent().addClass( "has-error"); error.insertAfter(element);
 * error.wrap("<p>"); error.css('color', 'red'); }, rules : {
 * 
 * inName : { maxlength : 40, required : true }, inPassword : { minlength : 8,
 * maxlength : 12, required : true } }, messages : { inName : { required :
 * "Insira o login", maxlength : jQuery.validator .format("Por favor, insira
 * menos que {0} caracteres.") }, inPassword : { required : "Insira a senha",
 * minlength : jQuery.validator .format("Por favor, insira mais que {0}
 * caracteres."), maxlength : jQuery.validator .format("Por favor, insira menos
 * que {0} caracteres.") } } });
 * 
 * $("#login").click(function() { validaISalvar();
 * 
 * if ($(".login-form").valid()) { var email = $("#cUsername").val(); var
 * password = $("#cPassword").val();
 * 
 * login('../rest/citizen/login', email, password); } });
 * 
 * });
 */