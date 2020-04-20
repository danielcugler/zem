var str1 = "Entidade";
var str2 = "Entidade";
var str3 = "a";

// Preenche os campos com os valores da entidade que está sendo editada
function loadInputs(data) {
	var entity = JSON.parse(data.jsonList);
	$('#CId').val(entity.entityId);
	$('#CName').val(entity.name);
	$('#CEnabled').val(entity.enabled === 'ENABLED' ? 0 : 1);
	if (entity.attendanceTime != null) {
		$('#cMedium').val(entity.attendanceTime.mediumPriorityTime);
		$('#cLow').val(entity.attendanceTime.lowPriorityTime);
		$('#cHigh').val(entity.attendanceTime.highPriorityTime);
		$('#cAtEnabled').val(entity.attendanceTime.enabled);
	} else {
		$('#cMedium').val($("label[for = cDMedium]").text());
		$('#cLow').val($("label[for = cDLow]").text());
		$('#cHigh').val($("label[for = cDHigh]").text());
	}
}

// Preenche os campos com os valores de tempo de atendimento padrão
function loadDefaultAt(data) {
	var at = JSON.parse(data.jsonList);
	$("label[for = cDLow]").text(at.lowPriorityTime);
	$("label[for = cDMedium]").text(at.mediumPriorityTime);
	$("label[for = cDHigh]").text(at.highPriorityTime);
}

// Função selecionar todos os elementos do select
function selecionaTudo() {
	$('#selectEntidade').multiSelect('select_all');
}

// Função remover todos os elementos do select
function removeTudo() {
	$('#selectEntidade').multiSelect('deselect_all');
}

// Preencher o select com as categorias de entidade disponíveis
function mSelect(data) {
	var list = JSON.parse(data.jsonList);
	var cont = 0;
	for (cont = 0; cont < list.length; cont++)
		$('#selectEntidade').multiSelect('addOption', {
			value : list[cont].entityCategoryId,
			text : list[cont].name,
			index : cont
		});
};

//Preencher o select com as categorias de entidade selecionadas
function mSelected(data) {
	var list = data;
	var cont = 0;
	var temp;
	for (cont = 0; cont < list.length; cont++) {
		temp = list[cont].entityCategoryId.toString();
		$('#selectEntidade').multiSelect('select', temp);
	}
};

// AJAX | Busca todos as categorias de entidade
function selectListAll(URL) {
	$.ajax({
		type : 'GET',
		async : false,
		url : URL,
		dataType : "json",
		contentType : "application/json",
		success : function(data) {
			mSelect(data);
		},
		error : function(jqxhr, status, errorMsg) {
			// Mensagem de erro
		}
	});
};

// AJAX | Busca as categorias de entidade que estão relacionadas a entidade que está sendo editada
function findByIdADDET(URL, id) {
	$.ajax({
		type : 'GET',
		async : false,
		url : URL + '/ec/' + id,
		dataType : "json", 
		contentType : "application/json",
		success : function(data) {
			loadInputs(data);
			var list = JSON.parse(data.jsonList);
			var entityCategory = list.entityCategoryCollection;
			mSelected(entityCategory);
		},
		error : function(jqxhr, status, errorMsg) { 
			// Mensagem de erro
		}
	});
};

// AJAX | Busca os valores do tempo de atendimento padrão
function getDefaultAt(URL) {
	$.ajax({
		type : 'GET',
		async : false,
		url : URL,
		dataType : "json",
		contentType : "application/json",
		success : function(data) {
			loadDefaultAt(data);
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

					getDefaultAt('../rest/entityclass/at');
					$('#selectEntidade')
							.multiSelect(
									{
										selectableHeader : "<div class='custom-header'>Categorias existentes</div>",
										selectionHeader : "<div class='custom-header'>Categorias selecionadas</div>",
										selectableFooter : "<div class='custom-footer'><a id='selecionarTodos' onClick='selecionaTudo();'>Selecionar todas</a></div>",
										selectionFooter : "<div class='custom-footer'><a id='removerTodos' onClick='removeTudo();'>Remover todas</a></div>"
									});

					$('#form-cadastro-entity')
							.validate(
									{
										errorPlacement : function(error,
												element) {
											$(element).parent().addClass(
													"has-error");
											error.insertAfter(element);
											error.wrap("<p>");
											error.css('color', 'red');
										},
										rules : {
											nome : {
												required : true,
												maxlength : 40,
												messages : {
													required : "Insira o nome",
													maxlength : jQuery.validator
															.format("Por favor insira menos que {0} caracteres.")
												}
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

						$('[name="cHigh"]').each(function() {
							$(this).rules('add', {
								required : true,
								messages : {
									required : "Insira a prioridade",
								}
							});
						});

						$('[name="cMedium"]').each(function() {
							$(this).rules('add', {
								required : true,
								messages : {
									required : "Insira a prioridade",
								}
							});
						});

						$('[name="cLow"]').each(function() {
							$(this).rules('add', {
								required : true,
								messages : {
									required : "Insira a prioridade",
								}
							});
						});
					}

					function getInputs(opcao, ec) {

						var entityClass = {};

						var attendanceTime = {
							'highPriorityTime' : $('#cHigh').val(),
							'mediumPriorityTime' : $('#cMedium').val(),
							'lowPriorityTime' : $('#cLow').val()
						};

						if (opcao == 2) {
							entityClass.entityId = parseInt($(
									'#form-cadastro-entity #CId').val());
						}
						entityClass.name = $('#form-cadastro-entity #CName')
								.val();
						if (opcao == 1) {
							entityClass.enabled = 0;
							attendanceTime.enabled = 0;
						} else {
							entityClass.enabled = $(
									'#form-cadastro-entity #CEnabled').val() === 'Enabled' ? 0
									: 1;
							attendanceTime.enabled = $(
									'#form-cadastro-entity #cAtEnabled').val() === 'Enabled' ? 0
									: 1;
							attendanceTime.entityId = entityClass.entityId;
						}
						entityClass.entityCategoryCollection = [];
						for ( var key in ec)
							entityClass.entityCategoryCollection.push({
								"entityCategoryId" : parseInt(ec[key])
							});
						entityClass.attendanceTime = attendanceTime;
						return JSON.stringify(entityClass);
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

					function getUrlVar(key) {
						var result = new RegExp(key + "=([^&]*)", "i")
								.exec(window.location.search);
						return result && unescape(result[1]) || "";
					}
					var id = getUrlVar('id');

					if (id === '') {
						selectListAll("../rest/entitycategory/combosativos");
				//		selectListAll("http://localhost:8080/rest/entitycategory/combos");
						$('#saveButton').addClass('incluirAction');
					} else {
						selectListAll("../rest/entitycategory/combosativos");
				//		selectListAll("http://localhost:8080/rest/entitycategory/combos");
						findByIdADDET('../rest/entityclass', id);
						$('#saveButton').addClass('editarAction');
					}

					$('#modaldemensagem')
							.on(
									"click",
									".okbutton",
									function(event) {
										event.preventDefault();
										window.location = '../entity.jsp';
									});

					$('#defaultAt').on('ifUnchecked', function(event) {
						$('#cLow').val("");
						$('#cMedium').val("");
						$('#cHigh').val("");

						$("#cLow").prop('disabled', false);
						$("#cMedium").prop('disabled', false);
						$("#cHigh").prop('disabled', false);
					});

					$('#defaultAt').on('ifChecked', function(event) {
						$('#cLow').val($("label[for = cDLow]").text());
						$('#cMedium').val($("label[for = cDMedium]").text());
						$('#cHigh').val($("label[for = cDHigh]").text());

						$("#cLow").prop('disabled', true);
						$("#cMedium").prop('disabled', true);
						$("#cHigh").prop('disabled', true);
					});

					$('.editarAction').click(
							function(event) {
								event.preventDefault();
								validaISalvar();
								var ec = $('#selectEntidade').val();
								if ($formModule.valid()
										&& ec !== null) {

									var entity = getInputs(2, ec);
									mergeTokenET('../rest/entityclass/'
											+ $('#username').text(), entity,
											header, token);
									return true;
								}
								if (ec == null) {
									erroSelect();
								} else {
									validateError();
									$('#modaldemensagem').modal('show');
									return false;
								}

								

							});

					$('#modalMsg').on('hidden.bs.modal', function() {
						window.location = 'addEntity.jsp'
					});
					
					$('.incluirAction').click(
					
							function(event) {
								event.preventDefault();
								validaISalvar();

								var ec = $('#selectEntidade').val();
								if ($formModule.valid()
										&& ec !== null) {

									var entity = getInputs(1, ec);
									saveENTToken('../rest/entityclass/'
											+ $('#username').text(), entity,
											header, token);
									return true;
								}
								if (ec == null) {
									erroSelect();
								} else {
									validateError();
									$('#modaldemensagem').modal('show');
								}
								
								return false;
							});

				});