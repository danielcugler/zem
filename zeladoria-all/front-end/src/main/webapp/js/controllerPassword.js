
$(document)
		.ready(
				function() {

					var token = $("meta[name='_csrf']").attr("content");
					var header = $("meta[name='_csrf_header']").attr("content");

					$("#fChangePass").validate({

						errorPlacement : function(error, element) {
							$(element).parent().addClass("has-error");
							error.insertAfter(element);
							error.wrap("<p>");
							error.css('color', 'red');
						},
						rules : {

						}
					});
					jQuery.validator.addMethod('mypassword', function(value,
							element) {
						return this.optional(element)
								|| (value.match(/[a-zA-Z]/) && value
										.match(/[0-9]/));
					}, 'A senha deve conter letras e números.');

					function validar() {
						$('[name="oldpass"]')
								.each(
										function() {
											$(this)
													.rules(
															'add',
															{
																required : true,
																minlength : 8,
																maxlength : 12,
																messages : {
																	required : "Insira a senha atual",
																	minlength : jQuery.validator
																			.format("A senha deve possuir no mínimo {0} caracteres."),
																	maxlength : jQuery.validator
																			.format("A senha deve possuir no máximo {0} caracteres.")
																}
															});
										});

						$('[name="newpass"]')
								.each(
										function() {
											$(this)
													.rules(
															'add',
															{
																required : true,
																minlength : 8,
																maxlength : 12,
																mypassword : true,
																messages : {
																	required : "Insira a nova senha",
																	minlength : jQuery.validator
																			.format("A senha deve possuir no mínimo {0} caracteres."),
																	maxlength : jQuery.validator
																			.format("A senha deve possuir no máximo {0} caracteres.")
																}
															});
										});
						$('[name="passconfirm"]')
								.each(
										function() {
											$(this)
													.rules(
															'add',
															{
																required : true,
																minlength : 8,
																maxlength : 12,
																equalTo : "#newpass",
																messages : {
																	required : "Insira a confimação da senha",
																	equalTo : "As senhas inseridas devem ser iguais.",
																	minlength : jQuery.validator
																			.format("A senha deve possuir no mínimo {0} caracteres."),
																	maxlength : jQuery.validator
																			.format("A senha deve possuir no máximo {0} caracteres.")
																}
															});
										});

					}
					;
					$('#saveButton')
							.click(
									function(event) {
										event.preventDefault();
										validar();
										if ($("#fChangePass").valid()) {

											dataString = $("#fChangePass")
													.serialize();
											JSON.stringify(dataString);
											$.ajax({
												type : 'POST',
												async : false,
												url : '../rest/systemuser/changepass',
												data : dataString,
												beforeSend : function(xhr) {
													xhr.setRequestHeader(header,token);
												},
												dataType : "json", 
												contentType : "application/x-www-form-urlencoded;charset=utf-8",
												success : function(data) {
													if (data.success === true) {
														alterPSuccess(data);
														$('#modaldemensagem').modal(
																'show');
														$('#oldpass').val("");
														$('#newpass').val("");
														$('#passconfirm').val("");
														$('.form-group').removeClass("has-error");
													} else
														forgotPError(data);
													
													$('#modaldemensagem').modal('show');
												},
												error : function(jqxhr,status,errorMsg) {
												},
											});
										}

										else {
											validateError();
											$('#modaldemensagem').modal('show');
										}
									});

					$('#modaldemensagem')
							.on(
									"click",
									".okbutton",
									function(event) {
										event.preventDefault();
										window.location = 'http://localhost:8080/index.jsp';
									});

				});