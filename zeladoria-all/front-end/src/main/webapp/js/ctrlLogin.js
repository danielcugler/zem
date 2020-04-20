var ctrlLogin = (function($, module) {
	// cached DOM
	// general module
	var $generalModule = $("#generalModule");
	var $loginSuccess = $("#loginsuccess-template").html();
	var $loginError = $("#loginerror-template").html();
	var $messageTpl = $("#message-template").html();
	// modal
	var $modalE = $generalModule.find("#modalE");
	var $notificacoesE = $generalModule.find("#notificacoesE");
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
	$("#username").blur(
			function() {
				if ($("#username").val().length < 3) {
					$("#erroUsuario").html(
							"Por favor, insira mais que 3 caracteres!");
				} else if ($("#username").val().length > 20) {
					$("#erroUsuario").html(
							"Por favor, insira menos que 20 caracteres!");
				} else {
					$("#erroUsuario").html("");
				}

			});

	$("#password").blur(function() {
		if ($("#password").val().length < 8) {
			$("#erroSenha").html("Por favor, insira mais que 8 caracteres!");
		} else if ($("#password").val().length > 12) {
			$("#erroSenha").html("Por favor, insira menos que 12 caracteres!");
		} else {
			$("#erroSenha").html("");
		}

	});

	var status = getUrlParam('status');

	if (status == "error") {
		$('#modalLogin').modal('show');
	}

	function getInputs() {
		var systemUser = {
			'systemUserUsername' : $('#user').val(),
			'password' : $('#password').val()
		};
		return systemUser;
	}

	$('#login').click(
			function(event) {
				event.preventDefault();
				$.ajax(
						{
							type : 'GET',
							url : '../rest/systemuser2/login/'
									+ $('#user').val() + '/'
									+ $('#password').val(),
							dataType : "json",
							contentType : "application/json"
						}).done(function(data) {
					window.location = 'index.jsp';
				}).fail(
						function(jqXHR, textStatus, errorThrown) {
							var body = jqXHR.responseJSON.errorCode + ", "
									+ jqXHR.responseJSON.errorMessage;
							modal("Autenticação", body);
						});

				// loginSU(
				// '../rest/systemuser/login',
				// $('#user').val(),
				// $('#password').val());

			});
	$('#forgot').click(
			function(event) {
				event.preventDefault();

				$.ajax(
						{
							type : 'GET',
							url : '../rest/systemuser2/password/'
									+ $('#myModal #username').val(),
							dataType : "json",
							contentType : "application/json"
						}).done(function(data) {
					modal("Autenticação", data.message);
				}).fail(
						function(jqXHR, textStatus, errorThrown) {
							var body = jqXHR.responseJSON.errorCode + ", "
									+ jqXHR.responseJSON.errorMessage;
							modal("Autenticação", body);
						});

				// forgotPassword('../rest/systemuser/password',
				// $('#myModal #username').val());

			});

})(jQuery, ctrlLogin || {});