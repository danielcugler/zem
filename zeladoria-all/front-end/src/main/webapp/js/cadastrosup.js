var str1 = "Perfil";
var str2 = "Perfil";
var str3 = "o";

//Preenche os campos com os valores do perfil que está sendo editada
function loadInputs(data) {
	var systemUserProfile = JSON.parse(data.jsonList);
	$('#CId').val(systemUserProfile.systemUserProfileId);
	$('#CName').val(systemUserProfile.name);
	$('#CEnabled').val(systemUserProfile.enabled === 'Enabled' ? 0 : 1);
}

//Função selecionar todos os elementos do select
function selecionaTudo() {
	$('#selectSUP').multiSelect('select_all');
}

//Função remover todos os elementos do select
function removeTudo() {
	$('#selectSUP').multiSelect('deselect_all');
}

//Preencher o select com as permissões disponíveis
function mSelectSUP(data) {
	var list = JSON.parse(data.jsonList);
	var cont = 0;
	for (cont = 0; cont < list.length; cont++)
		$('#selectSUP').multiSelect('addOption', {
			value : list[cont].systemUserProfilePermissionId,
			text : list[cont].name,
			index : cont
		});
};

//Preencher o select com as permissões selecionadas
function mSelectedSUP(data) {
	var list = data;
	var cont = 0;
	var temp;
	for (cont = 0; cont < list.length; cont++) {
		temp = list[cont].systemUserProfilePermissionId.toString();
		$('#selectSUP').multiSelect('select', temp);
	}
};

//AJAX | Busca todas as permissões
function selectListAllSUP(URL) {
	$.ajax({
		type : 'GET',
		async : false,
		url : URL,
		dataType : "json",
		contentType : "application/json",
		success : function(data) {
			mSelectSUP(data);
		},
		error : function(jqxhr, status, errorMsg) {
			// Mensagem de erro
		}
	});
};

//AJAX | Busca as permissões que estão relacionadas ao perfil que está sendo editado
function findByIdADDSUP(URL, id) {
	$.ajax({
		type : 'GET',
		async : false,
		url : URL + '/ec/' + id,
		dataType : "json", 
		contentType : "application/json",
		success : function(data) {
			loadInputs(data);
			var list = JSON.parse(data.jsonList);
			var suppCollection = list.systemUserProfilePermissionCollection;
			mSelectedSUP(suppCollection);
		},
		error : function(jqxhr, status, errorMsg) { 
			// Mensagem de erro
		}
	});
};

// Início document ready
$(document)
		.ready(
				function() {

					var token = $("meta[name='_csrf']").attr("content");
					var header = $("meta[name='_csrf_header']").attr("content");
					var selecionadas = [];

					function findElementByText(text) {
						var jSpot = $("li:contains('" + text + "')").filter(
								function() {
									return $(this).children().length === 0;
								}).parent(); 
						return jSpot;
					}

					$('#selectSUP')
							.multiSelect(
									{

										selectableHeader : "<div class='custom-header'>Permissões existentes</div>",
										selectionHeader : "<div class='custom-header'>Permissões selecionadas</div>",
										selectableFooter : "<div class='custom-footer'><a id='selecionarTodos' onClick='selecionaTudo();'>Selecionar todos</a></div>",
										selectionFooter : "<div class='custom-footer'><a id='removerTodos' onClick='removeTudo();'>Remover todos</a></div>",

										afterSelect : function(values) {

											var index;
											var li = $("li[value='" + values[0]
													+ "']");
											var text = $(li[0]).text();
											selecionadas.push(text);

										//	if (text.contains("Inclusão")) {
											if (text.indexOf("Inclusão")!== -1) {
												text = text.replace("Inclusão",
														"Consulta");

												index = selecionadas
														.indexOf(text);
												if (index == -1) {
													selecionadas.push(text);
												}
											}

										//	if (text.contains("Edição")) {
											if (text.indexOf("Edição")!==-1) {	
											text = text.replace("Edição",
														"Consulta");

												index = selecionadas
														.indexOf(text);
												if (index == -1) {
													selecionadas.push(text);
												}
											}

											//if (text.contains("Ativação/Inativação")) {
											if (text.indexOf("Ativação/Inativação")!==-1) {
											text = text.replace(
														"Ativação/Inativação",
														"Consulta");

												index = selecionadas
														.indexOf(text);
												if (index == -1) {
													selecionadas.push(text);
												}
											}

										//	if (text.contains("Publicação")) {
											if (text.indexOf("Publicação")!==-1) {
											text = text.replace(
														"Publicação",
														"Consulta");

												index = selecionadas
														.indexOf(text);
												if (index == -1) {
													selecionadas.push(text);
												}
											}

										//	if (text.contains("Encaminhamento")) {
											if (text.indexOf("Encaminhamento")!==-1) {
											text = text.replace(
														"Encaminhamento",
														"Consulta");

												index = selecionadas
														.indexOf(text);
												if (index == -1) {
													selecionadas.push(text);
												}
											}

										//	if (text.contains("Resposta")) {
											if (text.indexOf("Resposta")!== -1) {
											text = text.replace("Resposta",
														"Consulta");

												index = selecionadas
														.indexOf(text);
												if (index == -1) {
													selecionadas.push(text);
												}
											}

											var li2 = $("li:contains('" + text
													+ "')");

											var valor = $('#selectSUP').val();
											valor.push($(li2[0]).val());

											$('#selectSUP').val(valor);

											$('#selectSUP').multiSelect(
													'refresh');

										},
										afterDeselect : function(values) {
											var li = $("li[value='" + values[0]
													+ "']");

											var index = selecionadas.indexOf($(
													li[0]).text());
											if (index > -1) {
												selecionadas.splice(index, 1);
											}
										},
									});

					$('#form-cadastro-sup').validate({
						errorPlacement : function(error, element) {
							$(element).parent().addClass("has-error");
							error.insertAfter(element);
							error.wrap("<p>");
							error.css('color', 'red');
						},
						rules : {
							nome : {
								required : true,
								maxlength : 40
							}
						},
					});
					function validaISalvar() {
						$('[name="nome"]')
								.each(
										function() {
											$(this)
													.rules(
															'add',
															{
																required : true,
																maxlength : 40,
																messages : {
																	required : "Insira o nome",
																	maxlength : jQuery.validator
																			.format("Por favor insira menos que {0} caracteres.")
																}
															});
										});

					}

					function getInputs(opcao, ec) {

						var systemUserProfile = {};

						if (opcao == 2)
							systemUserProfile.systemUserProfileId = parseInt($(
									'#form-cadastro-sup #CId').val());
						systemUserProfile.name = $('#form-cadastro-sup #CName')
								.val();
						if (opcao == 1)
							systemUserProfile.enabled = 0;
						else
							systemUserProfile.enabled = $(
									'#form-cadastro-sup #CEnabled').val() === 'Enabled' ? 0
									: 1;
						systemUserProfile.systemUserProfilePermissionCollection = [];
						for ( var key in ec)
							systemUserProfile.systemUserProfilePermissionCollection
									.push({
										"systemUserProfilePermissionId" : parseInt(ec[key])
									});
						return JSON.stringify(systemUserProfile);
					};

					function pegaElementos(values) {
						var cont = 0;
						for (cont = 0; cont < values.length; cont++) {
							findCategory(values[cont]);
						}
					};

					function removeElementos(values) {
						var cont = 0;
						var i = 0;
						var index;
						for (i = 0; i < values.length; i++) {
							for (cont = 0; cont < lista.length; cont++) {
								if (lista[cont].entityCategoryId == values[i])
									lista.splice(cont, 1);
							}
						}
					};

					function validaSelect(selecionadas) {
						var i;
						for (i = 0; i < selecionadas.length; i++) {
		/*					if (selecionadas[i].contains("Inclusão")
									|| selecionadas[i].contains("Edição")
									|| selecionadas[i]
											.contains("Ativação/Inativação")
									|| selecionadas[i].contains("Publicação")
									|| selecionadas[i]
											.contains("Encaminhamento")
									|| selecionadas[i].contains("Resposta")) {
*/
												if (selecionadas[i].indexOf("Inclusão")!==-1
							|| selecionadas[i].indexOf("Edição")!==-1
							|| selecionadas[i]
									.indexOf("Ativação/Inativação")!==-1
							|| selecionadas[i].indexOf("Publicação")!==-1
							|| selecionadas[i]
									.indexOf("Encaminhamento")!==-1
							|| selecionadas[i].indexOf("Resposta")!==-1) {

							var subStr = selecionadas[i].split(" - ");
								var text = selecionadas[i].replace(subStr[1],
										"Consulta");

								index = selecionadas.indexOf(text);

								if (index == -1) {
									return false;
								}
							}
						}
						return true;
					}

					function getUrlVar(key) {
						var result = new RegExp(key + "=([^&]*)", "i")
								.exec(window.location.search);
						return result && unescape(result[1]) || "";
					}
					var id = getUrlVar('id');

					if (id === '') {
						selectListAllSUP('../rest/systemuserprofile/combosup');
						$('#saveButton').addClass('incluirAction');
					} else {
						selectListAllSUP('../rest/systemuserprofile/combosup');
						findByIdADDSUP('../rest/systemuserprofile', id);
						$('#saveButton').addClass('editarAction');
					}

					$('#modaldemensagem')
							.on(
									"click",
									".okbutton",
									function(event) {
										event.preventDefault();
										window.location = '../sup.jsp';
									});

					$('.editarAction')
							.click(
									function(event) {
										event.preventDefault();
										validaISalvar();
										var ec = $('#selectSUP').val();

										if (validaSelect(selecionadas)) {
											if ($("#form-cadastro-sup").valid()
													&& ec !== null) {

												var systemUserProfile = getInputs(
														2, ec);
												mergeToken(
														'../rest/systemuserprofile'
																+ '/'
																+ $('#username')
																		.text(),
														systemUserProfile,
														header, token);
												return true;
											}
											if (ec == null) {
												erroSelect();
											} else {
												validateError();
											}
										} else {
											erroSelectPerfil();
										}

										$('#modaldemensagem').modal('show');
										return false;

									});

					$('#modalMsg').on('hidden.bs.modal', function() {
						window.location = 'addsup.jsp'
					});
					$('.incluirAction')
							.click(
									function(event) {
										event.preventDefault();
										validaISalvar();
										var ec = $('#selectSUP').val();

										if (validaSelect(selecionadas)) {
											if ($("#form-cadastro-sup").valid()
													&& ec !== null) {

												var systemUserProfile = getInputs(
														1, ec);
												saveSUPToken(
														'../rest/systemuserprofile'
																+ '/'
																+ $('#username')
																		.text(),
														systemUserProfile,
														header, token);
												return true;
											}
											if (ec == null) {
												erroSelect();
											} else {
												validateError();
											}
										} else {
											erroSelectPerfil();
										}
										$('#modaldemensagem').modal('show');
										return false;
									});

				});