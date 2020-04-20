var str1 = "Chamado Publico";
var str2 = "Chamado Publico";
var str3 = "o";
var resizefunc = [];

function loadInputs(data) {
	data.jsonList = JSON.parse(data.jsonList);
	$('#cName').val(data.jsonList.nameWebUser);
	$('#cCpf').val(data.jsonList.cpfWebUser);
	$('#cEmail').val(data.jsonList.observationWebUser);
}

$(document)
		.ready(
				function() {

					combo('../rest/entitycategory',
							ModelsTmpl.tmplComboEntityCategory, "#comboEntity");
					
					jQuery("#cCpf")
					.mask("999.999.999-99")
					.focusout(
							function(event) {
								var target, cpf, element;
								target = (event.currentTarget) ? event.currentTarget
										: event.srcElement;
								cpf = target.value.replace(/\D/g, '');
								element = $(target);
								element.unmask();
								element.mask("999.999.999-99");
							});

					// validaCPF
					jQuery.validator
							.addMethod(
									"verificaCPF",
									function(value, element) {
										value = value.replace('.', '');
										value = value.replace('.', '');
										cpf = value.replace('-', '');
										while (cpf.length < 11)
											cpf = "0" + cpf;
										var expReg = /^0+$|^1+$|^2+$|^3+$|^4+$|^5+$|^6+$|^7+$|^8+$|^9+$/;
										var a = [];
										var b = new Number;
										var c = 11;
										for (i = 0; i < 11; i++) {
											a[i] = cpf.charAt(i);
											if (i < 9)
												b += (a[i] * --c);
										}
										if ((x = b % 11) < 2) {
											a[9] = 0
										} else {
											a[9] = 11 - x
										}
										b = 0;
										c = 11;
										for (y = 0; y < 10; y++)
											b += (a[y] * c--);
										if ((x = b % 11) < 2) {
											a[10] = 0;
										} else {
											a[10] = 11 - x;
										}
										if ((cpf.charAt(9) != a[9])
												|| (cpf.charAt(10) != a[10])
												|| cpf.match(expReg))
											return false;
										return true;
									});

					$('#loginForm').validate({
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

						// Validação de Login em Registro de usuario.
						$('#cName')
								.each(
										function() {
											$(this)
													.rules(
															'add',
															{
																maxlength : 100,
																required : true,
																messages : {
																	required : "Insira o nome",
																	maxlength : jQuery.validator
																			.format("Por favor, insira menos que {0} caracteres.")
																}
															});
										});

						// Validação de senha em registro de usuário
						$('#cCpf')
								.each(
										function() {
											$(this)
													.rules(
															'add',
															{
																maxlength : 11,
																required : true,
																verificaCPF : true,
																messages : {
																	required : "Insira o CPF",
																	verificaCPF : "CPF inválido",
																	maxlength : jQuery.validator
																			.format("Por favor, insira menos que {0} caracteres.")
																}
															});
										});

						// Validação de email em registro de usuário
						$('#cEmail')
								.each(
										function() {
											$(this)
													.rules(
															'add',
															{
																maxlength : 100,
																required : true,
																email : true,
																messages : {
																	required : "Insira o e-mail",
																	email : "Insira um email válido.",
																	maxlength : jQuery.validator
																			.format("Por favor, insira menos que {0} caracteres.")
																}
															});
										});

					}
					
					var geturl = function getUrlVar(key) {
						var result = new RegExp(key + "=([^&]*)", "i")
								.exec(window.location.search);
						return result && unescape(result[1]) || "";
					}
					
					var internalcall = geturl('useridentification');

					$("#save")
							.click(
									function() {
										validaISalvar();

										if ($("#loginForm").valid()) {

											var webUser = {
												'publicIdentificationKey' : 1,
												'name' : $("#cName").val(),
												'email' : $("#cEmail").val(),
												'webUserCpf' : $("#cCpf").val()
											};
											
											if(internalcall == 2){
												window.location = '../newcall.jsp'
														+ '/?name='
														+ $("#cName").val()
														+ '&email='
														+ $("#cEmail").val()
														+ '&cpf='
														+ $("#cCpf").val()
														+ '&useridentification='
														+ internalcall;
												
											} else if (internalcall==1){
												window.location = '../addinternalcall.jsp'
													+ '/?name='
													+ $("#cName").val()
													+ '&email='
													+ $("#cEmail").val()
													+ '&cpf='
													+ $("#cCpf").val()
													+ '&useridentification='
													+ internalcall;
											}
										}
									});

				});