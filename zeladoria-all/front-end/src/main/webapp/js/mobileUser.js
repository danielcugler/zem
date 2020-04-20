var str1 = "Usuário Móvel";
var str2 = "Usuário Móvel";
var str3 = "o";
var resizefunc = [];

function loadInputs(data) {
	data.jsonList = JSON.parse(data.jsonList);
	$('#form-username').val(data.jsonList.usernameMobile);
	$('#form-password').val(data.jsonList.passwordMobile);
}

$(document)
		.ready(
				function() {

					$('.login-form').validate({
						errorPlacement : function(error, element) {
							$(element).parent().addClass("has-error");
							error.insertAfter(element);
							error.wrap("<p>");
							error.css('color', 'red');
						},
					});

					function validaISalvar() {

						$('[name="form-username"]')
								.each(
										function() {
											$(this)
													.rules(
															'add',
															{
																maxlength : 40,
																required : true,
																messages : {
																	required : "Insira o login",
																	maxlength : jQuery.validator
																			.format("Por favor, insira menos que {0} caracteres.")
																}
															});
										});

						$('[name="form-password"]')
								.each(
										function() {
											$(this)
													.rules(
															'add',
															{
																minlength : 8,
																maxlength : 12,
																required : true,
																messages : {
																	required : "Insira a senha",
																	minlength : jQuery.validator
																			.format("Por favor, insira mais que {0} caracteres."),
																	maxlength : jQuery.validator
																			.format("Por favor, insira menos que {0} caracteres.")
																}
															});
										});

					}

					$("#login")
							.click(
									function() {
										validaISalvar();

										if ($(".login-form").valid()) {
											var email = $("#cUsername").val();
											var password = $("#cPassword")
													.val();

											login(
													'../rest/citizen/login',
													email, password);
										}
									});

				}); 

