var str1 = "Usuário";
var str2 = "Usuário";
var str3 = "o";

function loadInputs(data) {
	//var systemUser = JSON.parse(data.jsonList);
	var systemUser = data;
	//var base64String = "http://i.imgur.com/BzLWO6F.png";
	if (systemUser.photoPath !== null)
		$("#imgUser").attr("src", systemUser.photoPath);

	
	$('#cNome').val(systemUser.name);
	$('#cEmail').val(systemUser.email);
	$('#cFone').val(systemUser.commercialPhone).mask("(99) 9999-9999?9");
	$('#cFoneP').val(systemUser.personalPhone).mask("(99) 9999-9999?9");
	$('#cSetor').val(systemUser.sector);
	$('#cCargo').val(systemUser.jobPosition);
	$('#cLogin').val(systemUser.systemUserUsername);
	$('#cLogin').prop('disabled', true);
	$('#cEnabled').val(systemUser.enabled === 'Enabled' ? 0 : 1);
	$('#comboEntity').prop('selected', false);
	$('#comboPerfil').prop('selected', false);
	$('#comboEntity option[value="' + systemUser.worksAtEntity.entityId + '"]')
			.prop('selected', true);
	$(
			'#comboPerfil option[value="'
					+ systemUser.systemUserProfileId.systemUserProfileId + '"]')
			.prop('selected', true);
}

$(document)
		.ready(
				function() {

					var token = $("meta[name='_csrf']").attr("content");
					var header = $("meta[name='_csrf_header']").attr("content");

					var imgBase64;
					jQuery("#cFone")
							.mask("(99) 9999-9999?9")
							.focusout(
									function(event) {
										var target, phone, element;
										target = (event.currentTarget) ? event.currentTarget
												: event.srcElement;
										phone = target.value.replace(/\D/g, '');
										element = $(target);
										element.unmask();
										if (phone.length > 10) {
											element.mask("(99) 99999-999?9");
										} else {
											element.mask("(99) 9999-9999?9");
										}
									});

					jQuery("#cFoneP")
							.mask("(99) 9999-9999?9")
							.focusout(
									function(event) {
										var target, phone, element;
										target = (event.currentTarget) ? event.currentTarget
												: event.srcElement;
										phone = target.value.replace(/\D/g, '');
										element = $(target);
										element.unmask();
										if (phone.length > 10) {
											element.mask("(99) 99999-999?9");
										} else {
											element.mask("(99) 9999-9999?9");
										}
									});

					$("#formUsuario").validate({

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

					jQuery.validator.addMethod("uploadFile", function(val,
							element) {

						var size = element.files[0].size;
						console.log(size);

						if (size > 1048576) {
							console.log("returning false");
							return false;
						} else {
							console.log("returning true");
							return true;
						}

					}, "File type error");

					function validaISalvar() {
						$('[name="cNome"]')
								.each(
										function() {
											$(this)
													.rules(
															'add',
															{
																required : true,
																maxlength : 100,
																messages : {
																	required : "Por favor, insira o nome.",
																	maxlength : jQuery.validator
																			.format("Por favor insira menos que {0} caracteres.")
																}
															});
										});
						$('[name="cEmail"]')
								.each(
										function() {
											$(this)
													.rules(
															'add',
															{
																required : true,
																maxlength : 100,
																email : true,
																messages : {
																	required : "Por favor, insira um e-mail",
																	email : "Insira um email válido.",
																	maxlength : jQuery.validator
																			.format("Por favor, insira menos que {0} caracteres.")
																}
															});
										});

						$('[name="cFone"]')
								.each(
										function() {
											$(this)
													.rules(
															'add',
															{
																required : true,
																messages : {
																	required : "Por favor, insira um telefone comercial."
																}
															});
										});

						$('[name="cCargo"]')
								.each(
										function() {
											$(this)
													.rules(
															'add',
															{
																required : true,
																maxlength : 100,
																messages : {
																	required : "Por favor, insira o cargo.",
																	maxlength : jQuery.validator
																			.format("Por favor, insira menos que {0} caracteres.")
																}
															});
										});

						$("#selectEntity")
								.each(
										function() {
											$(this)
													.rules(
															'add',
															{
																selectCheckEntidade : true,
																messages : {
																	selectCheckEntidade : "Por favor, selecione a entidade."
																}
															});
										});

						$("#selectPerfil")
								.each(
										function() {
											$(this)
													.rules(
															'add',
															{
																selectCheckPerfil : true,
																messages : {
																	selectCheckPerfil : "Por favor, selecione o perfil."
																}
															});
										});

						$('[name="cLogin"]')
								.each(
										function() {
											$(this)
													.rules(
															'add',
															{
																required : true,
																minlength : 3,
																maxlength : 20,
																messages : {
																	required : "Por favor, insira o login.",
																	minlength : jQuery.validator
																			.format("Por favor, insira mais que {0} caracteres."),
																	maxlength : jQuery.validator
																			.format("Por favor, insira menos que {0} caracteres.")
																}
															});
										});

						$('[name="cSenha"]')
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
																	required : "Insira a senha",
																	minlength : jQuery.validator
																			.format("A senha deve possuir no mínimo {0} caracteres."),
																	maxlength : jQuery.validator
																			.format("A senha deve possuir no máximo {0} caracteres.")
																}
															});
										});
						$('[name="cConfirm"]')
								.each(
										function() {
											$(this)
													.rules(
															'add',
															{
																required : true,
																minlength : 8,
																maxlength : 12,
																equalTo : "#cSenha",
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

					function validaISalvarEditar() {
						$('[name="cNome"]')
								.each(
										function() {
											$(this)
													.rules(
															'add',
															{
																required : true,
																maxlength : 100,
																messages : {
																	required : "Por favor, insira o nome.",
																	maxlength : jQuery.validator
																			.format("Por favor insira menos que {0} caracteres.")
																}
															});
										});
						$('[name="cEmail"]')
								.each(
										function() {
											$(this)
													.rules(
															'add',
															{
																required : true,
																maxlength : 100,
																email : true,
																messages : {
																	required : "Por favor, insira um e-mail",
																	email : "Insira um email válido.",
																	maxlength : jQuery.validator
																			.format("Por favor, insira menos que {0} caracteres.")
																}
															});
										});

						$('[name="cFone"]')
								.each(
										function() {
											$(this)
													.rules(
															'add',
															{
																required : true,
																messages : {
																	required : "Por favor, insira um telefone comercial."
																}
															});
										});

						$('[name="cCargo"]')
								.each(
										function() {
											$(this)
													.rules(
															'add',
															{
																required : true,
																maxlength : 100,
																messages : {
																	required : "Por favor, insira o cargo.",
																	maxlength : jQuery.validator
																			.format("Por favor, insira menos que {0} caracteres.")
																}
															});
										});

						$("#selectEntity").each(function() {
							$(this).rules('add', {
								required : true,
								messages : {
									required : "Selecione uma Entidade"
								}
							});
						});

						$("#selectPerfil").each(function() {
							$(this).rules('add', {
								required : true,
								messages : {
									required : "Selecione um Perfil"
								}
							});
						});

						$('[name="cLogin"]')
								.each(
										function() {
											$(this)
													.rules(
															'add',
															{
																required : true,
																minlength : 3,
																maxlength : 20,
																messages : {
																	required : "Por favor, insira o login.",
																	minlength : jQuery.validator
																			.format("Por favor, insira mais que {0} caracteres."),
																	maxlength : jQuery.validator
																			.format("Por favor, insira menos que {0} caracteres.")
																}
															});
										});

						$('[name="cSenha"]').each(function() {
							$(this).rules('add', {
								mypassword : true
							});
						});
						$('[name="cConfirm"]').each(function() {
							$(this).rules('add', {
								equalTo : "#cSenha",

							});
						});

					}
					;

					combo("../rest/systemuserprofile/combosativos",
							ModelsTmpl.tmplComboPerfil, "#comboPerfil");
					combo("../rest/entityclass/combosativos",
							ModelsTmpl.tmplComboEntity, "#comboEntity");

					function getInputs(opcao) {
						var user = {

						};

						user.name = $inputNome = $('#formUsuario #cNome').val();
						user.email = $inputEmail = $('#formUsuario #cEmail')
								.val();
						user.commercialPhone = $inputFone = $(
								'#formUsuario #cFone').val().replace(
								/[^0-9]+/g, '');

						user.personalPhone = $inputFoneP = $(
								'#formUsuario #cFoneP').val().replace(
								/[^0-9]+/g, '');

						user.systemUserProfileId = parseInt($(
								'#selectPerfil option:selected').val());
						user.jobPosition = $inputCargo = $(
								'#formUsuario #cCargo').val();
						user.sector = $inputSector = $('#formUsuario #cSetor')
								.val();
						user.worksAtEntity = {
							'entityId' : parseInt($(
									'#selectEntity option:selected').val())
						};
						user.systemUserUsername = $inputLogin = $(
								'#formUsuario #cLogin').val();
						user.password = $inputSenha = $('#formUsuario #cSenha')
								.val();
						user.photoPath = imgBase64;
						if (opcao == 2)
							user.enabled = $inputEnabled = $(
									'#formUsuario #cEnabled').val();
						else
							user.enabled = 0;
						if (opcao === 1) {
							return JSON.stringify(user);
						}
						if (opcao === 2)
							return JSON.stringify(user);
					}
					;

					function getUrlVar(key) {
						var result = new RegExp(key + "=([^&]*)", "i")
								.exec(window.location.search);
						return result && unescape(result[1]) || "";
					}

					var username = getUrlVar('id');
					var action;
					if (username === '')
						action = 'incluirAction';
					else {
						action = 'editarAction';
						$(".camposSenha").hide();
						$.ajax({
							type : 'GET',
							url : '../rest/systemuser2/' + username,
							dataType : "json",
							contentType : "application/json",
							success : function(data) {
								loadInputs(data);
							},
							error : function(jqxhr, status, errorMsg) {
								// Mensagem de erro
							}
						});						
						
					}
					$('#saveButton').addClass(action);

					$('#modaldemensagem').on("click", ".okbutton",
							function(event) {
								event.preventDefault();
								window.location = '../user.jsp';
							});

					$('.editarAction').click(
							function(event) {
								event.preventDefault();

								if (($("#cSenha").val() == "")
										&& ($("#cConfirm").val() == "")) {
									validaISalvarEditar();
								} else
									validaISalvar();

								if ($("#formUsuario").valid()) {
									mergeToken('../rest/systemuser/'
											+ $('#username').text(),
											getInputs(2), header, token);
								} else {
									validateError();

									$('#modaldemensagem').modal('show');
									return false;
								}

							});

					$('.incluirAction').click(
							function() {

								validaISalvar();
								if ($("#formUsuario").valid()) {
									saveToken('../rest/systemuser/'
											+ $('#username').text(),
											getInputs(1), header, token);
									return true;
								} else {
									validateError();
									$('#modaldemensagem').modal('show');
									return false;
								}

							});

					$('#modalMsg').on('hidden.bs.modal', function() {
						window.location = 'user.jsp'
					});

					$("#crop-modal").on("click", "#sendImage", function() {

						var canvas = $("#canvas").get(0).getContext("2d");
						var dataUrl = canvas.toDataURL('image/png');
						var formData = new FormData();
						formData.append('img', dataUrl);

						$.ajax({

							url : '../rest/files/upload',

							type : 'POST',

							data : formData,

							async : false,

							cache : false,

							contentType : false,

							processData : false,

							success : function(returndata) {

								alert(returndata);

							}

						});

						$("#crop-modal").modal("hide");
					});
					
					var jcrop_api;
					var canvas;
					$('#upload').change(function() {
						$('#crop-modal').modal('show');
						$('#Image1').hide();
						$("#Image1").attr("src", "");
						if ($('#upload')[0].files[0].type == "image/jpeg") {
							$('#btnCrop').show();
						}
						$("#btn")
						$('#error').hide();
						var reader = new FileReader();
						reader.onload = function(e) {
							var tbox = $("#crop-modal").width() * 0.35;
							$('#Image1').show();
							$('#Image1').attr("src", e.target.result);
							$('#Image1').Jcrop({
								allowResize : false,
								allowSelect : false,
								boxWidth : tbox,
								boxHeight : 400,
								onChange : SetCoordinates,
								onSelect : SetCoordinates,
								aspectRatio : 4 / 4,
								setSelect : [ 100, 100, 350, 350 ],
								bgFade : true,
								bgOpacity : .3
							}, function() {
								jcrop_api = this;
							});
						}
						reader.readAsDataURL(this.files[0]);
					});
					$('#btnCrop').click(
							function() {
								var x1 = $('#imgX1').val();
								var y1 = $('#imgY1').val();
								var width = $('#imgWidth').val();
								var height = $('#imgHeight').val();
								canvas = $("#canvas")[0];
								var context = canvas.getContext('2d');
								var img = new Image();
								img.onload = function() {
									canvas.height = height;
									canvas.width = width;
									context.drawImage(img, x1, y1, width,
											height, 0, 0, width, height);
								};
								img.src = $('#Image1').attr("src");

							});
					$('#btnRemove').click(function() {
						$('#crop-modal').modal('hide');
						$("#upload").val("");
						JcropAPI = $('#Image1').data('Jcrop');
						JcropAPI.destroy();
					});

					$('#send').click(function() {
						imgBase64 = canvas.toDataURL();
						$("#imgUser").attr("src", imgBase64);
						$("#Image1").attr("src", "");
						$('#crop-modal').modal('hide');
						jcrop_api.destroy();
					});
					
					$('#fecharModal').click(function(){
						$('#crop-modal').modal('hide');
						$("#upload").val("");
						JcropAPI = $('#Image1').data('Jcrop');
						JcropAPI.destroy();
					});

				});

function SetCoordinates(c) {
	$('#imgX1').val(c.x);
	$('#imgY1').val(c.y);
	$('#imgWidth').val(c.w);
	$('#imgHeight').val(c.h);
};
function checkFile() {
	var fileElement = document.getElementById("imgCropped");
	if ($('#imgCropped')[0].files.length == 0) {
		$('#error').show();
		$('#error').html("Please select file!")
		return false;
	}
	var fileSize = $('#imgCropped')[0].files[0].size
	var fileExtension = "";
	if (fileElement.value.lastIndexOf(".") > 0) {
		fileExtension = fileElement.value.substring(fileElement.value
				.lastIndexOf(".") + 1, fileElement.value.length);
	}
	if ((fileExtension.toLowerCase() == "png" || fileExtension.toLowerCase() == "png")
			&& (fileSize < 2000000)) {
		return true;
	} else {
		$('#error').show();
		$('#error').html("Unacceptable image file, please try again!")
		return false;
	}
}

function saveCanvasImage(crop_canvas) {
	var imageBase64 = crop_canvas.toDataURL('image/png');

	$.ajax({
		url : "../rest/up",
		data : imageBase64,
		type : 'POST',
		dataType : 'json',
		timeout : 10000,
		async : false,
		error : function() {
			console.log("WOOPS");
		},
		success : function(res) {
			if (res.ret == 0) {
				console.log("SUCCESS");
			} else {
				console.log("FAIL : " + res.msg);
			}
		}
	});
}