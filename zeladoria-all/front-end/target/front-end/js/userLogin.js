var str1 = "Tempo de Atendimento";
var str2 = "Tempo de Atendimento";
var str3 = "o";
$(document)
		.ready(
				function() {

					var geturl = function getUrlVar(key) {
						var result = new RegExp(key + "=([^&]*)", "i")
								.exec(window.location.search);
						return result && unescape(result[1]) || "";
					}
					
					$("#username").blur(function(){
						if($("#username").val().length < 3){
							$("#erroUsuario").html("Por favor, insira mais que 3 caracteres!");
						} else if ($("#username").val().length > 20){
							$("#erroUsuario").html("Por favor, insira menos que 20 caracteres!");
						} else {
							$("#erroUsuario").html("");
						}
						
					});
					
					$("#password").blur(function(){
						if($("#password").val().length < 8){
							$("#erroSenha").html("Por favor, insira mais que 8 caracteres!");
						} else if ($("#password").val().length > 12){
							$("#erroSenha").html("Por favor, insira menos que 12 caracteres!");
						} else {
							$("#erroSenha").html("");
						}
						
					});

					var status = geturl('status');

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

					$('#login')
							.click(
									function(event) {
										event.preventDefault();

										loginSU(
												'../rest/systemuser/login',
												$('#user').val(),
												$('#password').val());

									});
					$('#forgot')
							.click(
									function(event) {
										event.preventDefault();
										forgotPassword(
												'../rest/systemuser/password',
												$('#myModal #username').val());

									});

				});