var addSUP = (function($, module) {
	var selecionadas = [];
	// security rules
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	// cached DOM
	var $generalModule = $('#generalModule');
	//title
	var $title = $generalModule.find("#title2");
	var $fieldModule = $generalModule.find('#fieldModule');
	var $formModule = $generalModule.find('#form-cadastro-sup');
	var $inSystemUserProfileId = $fieldModule.find('#inId');
	var $inName = $fieldModule.find('#inName');
	var $inEnabled = $fieldModule.find('#inEnabled');
	var $selectSUPP = $fieldModule.find('#selectSUPP');
	// buttons
	var $buttonModule = $fieldModule.find("#botoes");
	var $btAdd = $buttonModule.find("#btAdd");
	var $btEdit = $buttonModule.find("#btEdit");
	// modal
	var $modalE = $generalModule.find("#modalE");
	var $notificacoesE = $generalModule.find("#notificacoesE");
	// bind actions
	$btAdd.on('click', save);
	$btEdit.on('click', edit);
	// $("#selecionarTodos").on("click", selecionaTudo);
	// $("#removerTodos").on("click", removeTudo);
	// $("#selecionarTodos").click( function(){alert("ok")});
	// $("#removerTodos").click( function(){alert("ok")});
	// templates
	var $messageTpl = $("#message-template").html();
	var $selectErrorTpl = $("#select-error-template").html();

	// functions
	// Função selecionar todos os elementos do select
	function selecionaTudo() {
		$selectSUPP.multiSelect('select_all');
	}

	// Função remover todos os elementos do select
	function removeTudo() {
		$selectSUPP.multiSelect('deselect_all');
	}
	$formModule.validate({

		errorPlacement : function(error, element) {
			$(element).parent().addClass("has-error");
			error.insertAfter(element);
			error.wrap("<p>");
			error.css('color', 'red');
		},
		rules : {
			inName : {
				required : true,
				maxlength : 40
			}
		},
		messages : {
			inName : {
				required : "Insira o nome",
				maxlength : $.validator
						.format("Por favor insira menos que {0} caracteres.")
			}
		}
	});
	$("#selectSUPP").each(function(index) {
		$(this).rules("add", {
			messages : {
				required : "Por favor, selecione uma permissão."
			}
		});
	});

	var changeButton = function() {
		$btAdd.hide();
		$btEdit.show();
	}
	function restError(jqXHR, textStatus) {
		var body = jqXHR.responseJSON.errorCode + ", "
				+ jqXHR.responseJSON.errorMessage;
		modal("Perfil de Usuário", body, 2);
	}
	// render
	function _render(baseTemplate, selector, data) {
		var template = Handlebars.compile(baseTemplate);
		var html = template(data);
		selector.html(html);
	}
	function modal(title, body, op) {
		_render($messageTpl, $notificacoesE, {
			'title' : title,
			'body' : body
		});
		if (op == 1)
			$notificacoesE.find("#bOk1").show();
		else if (op == 2)
			$notificacoesE.find("#bOk2").show();
		else
			$notificacoesE.find("#bOk3").show();
		$modalE.modal('show');
	}
	$selectSUPP
			.multiSelect({
				selectableHeader : "<div class='custom-header'>Permissões existentes</div>",
				selectionHeader : "<div class='custom-header'>Permissões selecionadas</div>",
				selectableFooter : "<div class='custom-footer'><a id='selecionarTodos'>Selecionar todas</a></div>",
				selectionFooter : "<div class='custom-footer'><a id='removerTodos'>Remover todas</a></div>",
				afterInit : function() {
					$("#selecionarTodos").on("click", selecionaTudo);
					$("#removerTodos").on("click", removeTudo);
				},
				afterSelect : function(values) {

					var index;
					var li = $("li[value='" + values[0] + "']");
					var text = $(li[0]).text();
					selecionadas.push(text);

					// if (text.contains("Inclusão")) {
					if (text.indexOf("Inclusão") !== -1) {
						text = text.replace("Inclusão", "Consulta");

						index = selecionadas.indexOf(text);
						if (index == -1) {
							selecionadas.push(text);
						}
					}

					// if (text.contains("Edição")) {
					if (text.indexOf("Edição") !== -1) {
						text = text.replace("Edição", "Consulta");

						index = selecionadas.indexOf(text);
						if (index == -1) {
							selecionadas.push(text);
						}
					}

					// if (text.contains("Ativação/Inativação")) {
					if (text.indexOf("Ativação/Inativação") !== -1) {
						text = text.replace("Ativação/Inativação", "Consulta");

						index = selecionadas.indexOf(text);
						if (index == -1) {
							selecionadas.push(text);
						}
					}

					// if (text.contains("Publicação")) {
					if (text.indexOf("Publicação") !== -1) {
						text = text.replace("Publicação", "Consulta");

						index = selecionadas.indexOf(text);
						if (index == -1) {
							selecionadas.push(text);
						}
					}

					// if (text.contains("Encaminhamento")) {
					if (text.indexOf("Encaminhamento") !== -1) {
						text = text.replace("Encaminhamento", "Consulta");

						index = selecionadas.indexOf(text);
						if (index == -1) {
							selecionadas.push(text);
						}
					}

					// if (text.contains("Resposta")) {
					if (text.indexOf("Resposta") !== -1) {
						text = text.replace("Resposta", "Consulta");

						index = selecionadas.indexOf(text);
						if (index == -1) {
							selecionadas.push(text);
						}
					}

					var li2 = $("li:contains('" + text + "')");

					var valor = $selectSUPP.val();
					valor.push($(li2[0]).val());

					$selectSUPP.val(valor);

					$selectSUPP.multiSelect();
					
				},
				afterDeselect : function(values) {
					var li = $("li[value='" + values[0] + "']");

					var index = selecionadas.indexOf($(li[0]).text());
					if (index > -1) {
						selecionadas.splice(index, 1);
					}
				}

			});

	function validaSelect(selecionadas) {
		var i;
		for (i = 0; i < selecionadas.length; i++)
			if (selecionadas[i].indexOf("Inclusão") !== -1
					|| selecionadas[i].indexOf("Edição") !== -1
					|| selecionadas[i].indexOf("Ativação/Inativação") !== -1
					|| selecionadas[i].indexOf("Publicação") !== -1
					|| selecionadas[i].indexOf("Encaminhamento") !== -1
					|| selecionadas[i].indexOf("Resposta") !== -1) {
				var subStr = selecionadas[i].split(" - ");
				var text = selecionadas[i].replace(subStr[1], "Consulta");
				index = selecionadas.indexOf(text);
				if (index == -1)
					return false;
			}
		return true;
	}

	function getUrl(key) {
		var result = new RegExp(key + "=([^&]*)", "i")
				.exec(window.location.search);
		return result && unescape(result[1]) || "";
	}
	function getFields() {
		var systemUserProfile = {};
		systemUserProfile.systemUserProfileId = $inSystemUserProfileId.val();
		systemUserProfile.name = $inName.val();
		systemUserProfile.enabled = $inEnabled.val();
		systemUserProfile.systemUserProfilePermissionCollection = [];
		var supp = $selectSUPP.val();
		for ( var key in supp)
			systemUserProfile.systemUserProfilePermissionCollection.push({
				"systemUserProfilePermissionId" : parseInt(supp[key])
			});
		return JSON.stringify(systemUserProfile);
	}
	function setFields(systemUserProfileId, name, enabled) {
		$inSystemUserProfileId.val(systemUserProfileId);
		$inName.val(name);
		$inEnabled.val(enabled);
	}
	function getSystemUserProfile(id) {
		$
				.ajax({
					type : 'GET',
					url : "../rest/systemuserprofile2/" + id,
					dataType : "json",
					contentType : "application/json"
				})
				.done(
						function(resp) {
							setFields(resp.systemUserProfileId, resp.name,
									resp.enabled);
							for (cont = 0; cont < resp.systemUserProfilePermissionCollection.length; cont++)
								$selectSUPP
										.multiSelect(
												'select',
												resp.systemUserProfilePermissionCollection[cont].systemUserProfilePermissionId
														.toString());
						}).fail(function(jqXHR, textStatus) {
				});
	}
	function loadSystemUserProfile(id) {
		selectListAll(id);
	}
	// Preencher o select com as categorias de entidade disponíveis
	function mSelect(data) {
		var cont = 0;
		for (cont = 0; cont < data.length; cont++)
			$selectSUPP.multiSelect('addOption', {
				value : data[cont].systemUserProfilePermissionId,
				text : data[cont].name,
				index : cont
			});
	}

	// Preencher o select com as categorias de entidade selecionadas
	function mSelected(data) {
		var cont = 0;
		var temp;
		for (cont = 0; cont < data.length; cont++) {
			temp = data[cont].systemUserProfilePermissionId.toString();
			$selectSUPP.multiSelect('select', temp);
		}
	}

	// AJAX | Busca todos as categorias de entidade
	function selectListAll(id) {
		$.ajax({
			type : 'GET',
			async : false,
			url : '../rest/supp2',
			dataType : "json",
			contentType : "application/json",
			success : function(data) {
				for (cont = 0; cont < data.length; cont++)
					$selectSUPP.multiSelect('addOption', {
						value : data[cont].systemUserProfilePermissionId,
						text : data[cont].name,
						index : cont
					});
				if (id)
					getSystemUserProfile(id);
			},
			error : function(jqxhr, status, errorMsg) {
				// Mensagem de erro
			}
		});
	}

	function edit(event) {
		event.preventDefault();
		var suppcol = $selectSUPP.val();
		if (!$formModule.valid()) {
			modal("Perfil de Usuário",
					"Por favor, preencha os campos destacados em vermelho!", 3);
			return false;
		}
		if (!validaSelect(selecionadas)) {
			_render($selectErrorTpl, $notificacoesE, {});
			$modalE.modal('show');
			return false;
		}
		$.ajax({
			type : 'PUT',
			url : '../rest/systemuserprofile2/',
			dataType : "json",
			data : getFields(),
			beforeSend : function(xhr) {
				xhr.setRequestHeader(header, token);
			},
			contentType : "application/json"
		}).done(
				function(data) {
					modal("Perfil de Usuário",
							data.message, 1);
					return true;
				}).fail(restError);

	}

	function save(event) {
		event.preventDefault();
		var suppcol = $selectSUPP.val();
		if (!$formModule.valid()) {
			modal("Perfil de Usuário",
					"Por favor, preencha os campos destacados em vermelho!", 3);
			return false;
		}
		if (!validaSelect(selecionadas)) {
			_render($selectErrorTpl, $notificacoesE, {});
			$modalE.modal('show');
			return false;
		}
		$.ajax({
			type : 'POST',
			url : '../rest/systemuserprofile2/',
			dataType : "json",
			data : getFields(),
			beforeSend : function(xhr) {
				xhr.setRequestHeader(header, token);
			},
			contentType : "application/json"
		}).done(
				function(data) {
					modal("Perfil de Usuário",
							data.message, 1);
					return true;
				}).fail(restError);

	}
	function init() {
		$inEnabled.val('0');
		var id = getUrl('id');
		if (id) {
			document.title = "Edição de Perfil de Usuário";
			$title.text("").append("Edição de <strong>Perfil de Usuário</strong>");
			changeButton();
			$("#atencao")
					.html(
							"<p><strong>ATENÇÃO:</strong> Para que estas alterações sejam efetivadas, os usuários deste perfil devem refazer o login no portal.</p>");
		} else {
			$("#atencao").html("");
		}
		loadSystemUserProfile(id)
	}
	init();

	module.getFields = getFields;
	return module;

})(jQuery, addSUP || {});

/*
 * var str1 = "Perfil"; var str2 = "Perfil"; var str3 = "o";
 * 
 * //Preenche os campos com os valores do perfil que está sendo editada function
 * loadInputs(data) { var systemUserProfile = JSON.parse(data.jsonList);
 * $('#CId').val(systemUserProfile.systemUserProfileId);
 * $('#CName').val(systemUserProfile.name);
 * $('#CEnabled').val(systemUserProfile.enabled === 'Enabled' ? 0 : 1); }
 * 
 * //Função selecionar todos os elementos do select function selecionaTudo() {
 * $('#selectSUP').multiSelect('select_all'); }
 * 
 * //Função remover todos os elementos do select function removeTudo() {
 * $('#selectSUP').multiSelect('deselect_all'); }
 * 
 * //Preencher o select com as permissões disponíveis function mSelectSUP(data) {
 * var list = JSON.parse(data.jsonList); var cont = 0; for (cont = 0; cont <
 * list.length; cont++) $('#selectSUP').multiSelect('addOption', { value :
 * list[cont].systemUserProfilePermissionId, text : list[cont].name, index :
 * cont }); };
 * 
 * //Preencher o select com as permissões selecionadas function
 * mSelectedSUP(data) { var list = data; var cont = 0; var temp; for (cont = 0;
 * cont < list.length; cont++) { temp =
 * list[cont].systemUserProfilePermissionId.toString();
 * $('#selectSUP').multiSelect('select', temp); } };
 * 
 * //AJAX | Busca todas as permissões function selectListAllSUP(URL) { $.ajax({
 * type : 'GET', async : false, url : URL, dataType : "json", contentType :
 * "application/json", success : function(data) { mSelectSUP(data); }, error :
 * function(jqxhr, status, errorMsg) { // Mensagem de erro } }); };
 * 
 * //AJAX | Busca as permissões que estão relacionadas ao perfil que está sendo
 * editado function findByIdADDSUP(URL, id) { $.ajax({ type : 'GET', async :
 * false, url : URL + '/ec/' + id, dataType : "json", contentType :
 * "application/json", success : function(data) { loadInputs(data); var list =
 * JSON.parse(data.jsonList); var suppCollection =
 * list.systemUserProfilePermissionCollection; mSelectedSUP(suppCollection); },
 * error : function(jqxhr, status, errorMsg) { // Mensagem de erro } }); }; //
 * Início document ready $(document) .ready( function() {
 * 
 * var token = $("meta[name='_csrf']").attr("content"); var header =
 * $("meta[name='_csrf_header']").attr("content"); var selecionadas = [];
 * 
 * function findElementByText(text) { var jSpot = $("li:contains('" + text +
 * "')").filter( function() { return $(this).children().length === 0;
 * }).parent(); return jSpot; }
 * 
 * $('#selectSUP') .multiSelect( {
 * 
 * selectableHeader : "<div class='custom-header'>Permissões existentes</div>",
 * selectionHeader : "<div class='custom-header'>Permissões selecionadas</div>",
 * selectableFooter : "<div class='custom-footer'><a id='selecionarTodos'
 * onClick='selecionaTudo();'>Selecionar todos</a></div>", selectionFooter : "<div
 * class='custom-footer'><a id='removerTodos' onClick='removeTudo();'>Remover
 * todos</a></div>",
 * 
 * afterSelect : function(values) {
 * 
 * var index; var li = $("li[value='" + values[0] + "']"); var text =
 * $(li[0]).text(); selecionadas.push(text); // if (text.contains("Inclusão")) {
 * if (text.indexOf("Inclusão")!== -1) { text = text.replace("Inclusão",
 * "Consulta");
 * 
 * index = selecionadas .indexOf(text); if (index == -1) {
 * selecionadas.push(text); } } // if (text.contains("Edição")) { if
 * (text.indexOf("Edição")!==-1) { text = text.replace("Edição", "Consulta");
 * 
 * index = selecionadas .indexOf(text); if (index == -1) {
 * selecionadas.push(text); } }
 * 
 * //if (text.contains("Ativação/Inativação")) { if
 * (text.indexOf("Ativação/Inativação")!==-1) { text = text.replace(
 * "Ativação/Inativação", "Consulta");
 * 
 * index = selecionadas .indexOf(text); if (index == -1) {
 * selecionadas.push(text); } } // if (text.contains("Publicação")) { if
 * (text.indexOf("Publicação")!==-1) { text = text.replace( "Publicação",
 * "Consulta");
 * 
 * index = selecionadas .indexOf(text); if (index == -1) {
 * selecionadas.push(text); } } // if (text.contains("Encaminhamento")) { if
 * (text.indexOf("Encaminhamento")!==-1) { text = text.replace(
 * "Encaminhamento", "Consulta");
 * 
 * index = selecionadas .indexOf(text); if (index == -1) {
 * selecionadas.push(text); } } // if (text.contains("Resposta")) { if
 * (text.indexOf("Resposta")!== -1) { text = text.replace("Resposta",
 * "Consulta");
 * 
 * index = selecionadas .indexOf(text); if (index == -1) {
 * selecionadas.push(text); } }
 * 
 * var li2 = $("li:contains('" + text + "')");
 * 
 * var valor = $('#selectSUP').val(); valor.push($(li2[0]).val());
 * 
 * $('#selectSUP').val(valor);
 * 
 * $('#selectSUP').multiSelect( 'refresh'); }, afterDeselect : function(values) {
 * var li = $("li[value='" + values[0] + "']");
 * 
 * var index = selecionadas.indexOf($( li[0]).text()); if (index > -1) {
 * selecionadas.splice(index, 1); } }, });
 * 
 * $('#form-cadastro-sup').validate({ errorPlacement : function(error, element) {
 * $(element).parent().addClass("has-error"); error.insertAfter(element);
 * error.wrap("<p>"); error.css('color', 'red'); }, rules : { nome : {
 * required : true, maxlength : 40 } }, }); function validaISalvar() {
 * $('[name="nome"]') .each( function() { $(this) .rules( 'add', { required :
 * true, maxlength : 40, messages : { required : "Insira o nome", maxlength :
 * jQuery.validator .format("Por favor insira menos que {0} caracteres.") } });
 * }); }
 * 
 * function getInputs(opcao, ec) {
 * 
 * var systemUserProfile = {};
 * 
 * if (opcao == 2) systemUserProfile.systemUserProfileId = parseInt($(
 * '#form-cadastro-sup #CId').val()); systemUserProfile.name =
 * $('#form-cadastro-sup #CName') .val(); if (opcao == 1)
 * systemUserProfile.enabled = 0; else systemUserProfile.enabled = $(
 * '#form-cadastro-sup #CEnabled').val() === 'Enabled' ? 0 : 1;
 * systemUserProfile.systemUserProfilePermissionCollection = []; for ( var key
 * in ec) systemUserProfile.systemUserProfilePermissionCollection .push({
 * "systemUserProfilePermissionId" : parseInt(ec[key]) }); return
 * JSON.stringify(systemUserProfile); };
 * 
 * function pegaElementos(values) { var cont = 0; for (cont = 0; cont <
 * values.length; cont++) { findCategory(values[cont]); } };
 * 
 * function removeElementos(values) { var cont = 0; var i = 0; var index; for (i =
 * 0; i < values.length; i++) { for (cont = 0; cont < lista.length; cont++) { if
 * (lista[cont].entityCategoryId == values[i]) lista.splice(cont, 1); } } };
 * 
 * function validaSelect(selecionadas) { var i; for (i = 0; i <
 * selecionadas.length; i++) {
 * 
 * if (selecionadas[i].indexOf("Inclusão")!==-1 ||
 * selecionadas[i].indexOf("Edição")!==-1 || selecionadas[i]
 * .indexOf("Ativação/Inativação")!==-1 ||
 * selecionadas[i].indexOf("Publicação")!==-1 || selecionadas[i]
 * .indexOf("Encaminhamento")!==-1 || selecionadas[i].indexOf("Resposta")!==-1) {
 * 
 * var subStr = selecionadas[i].split(" - "); var text =
 * selecionadas[i].replace(subStr[1], "Consulta");
 * 
 * index = selecionadas.indexOf(text);
 * 
 * if (index == -1) { return false; } } } return true; }
 * 
 * function getUrlVar(key) { var result = new RegExp(key + "=([^&]*)", "i")
 * .exec(window.location.search); return result && unescape(result[1]) || ""; }
 * var id = getUrlVar('id');
 * 
 * if (id === '') { selectListAllSUP('../rest/systemuserprofile/combosup');
 * $('#saveButton').addClass('incluirAction'); } else {
 * selectListAllSUP('../rest/systemuserprofile/combosup');
 * findByIdADDSUP('../rest/systemuserprofile', id);
 * $('#saveButton').addClass('editarAction'); }
 * 
 * $('#modaldemensagem') .on( "click", ".okbutton", function(event) {
 * event.preventDefault(); window.location = '../sup.jsp'; });
 * 
 * $('.editarAction') .click( function(event) { event.preventDefault();
 * validaISalvar(); var ec = $('#selectSUP').val();
 * 
 * if (validaSelect(selecionadas)) { if ($("#form-cadastro-sup").valid() && ec
 * !== null) {
 * 
 * var systemUserProfile = getInputs( 2, ec); mergeToken(
 * '../rest/systemuserprofile' + '/' + $('#username') .text(),
 * systemUserProfile, header, token); return true; } if (ec == null) {
 * erroSelect(); } else { validateError(); } } else { erroSelectPerfil(); }
 * 
 * $('#modaldemensagem').modal('show'); return false;
 * 
 * });
 * 
 * $('#modalMsg').on('hidden.bs.modal', function() { window.location =
 * 'addsup.jsp' }); $('.incluirAction') .click( function(event) {
 * event.preventDefault(); validaISalvar(); var ec = $('#selectSUP').val();
 * 
 * if (validaSelect(selecionadas)) { if ($("#form-cadastro-sup").valid() && ec
 * !== null) {
 * 
 * var systemUserProfile = getInputs( 1, ec); saveSUPToken(
 * '../rest/systemuserprofile' + '/' + $('#username') .text(),
 * systemUserProfile, header, token); return true; } if (ec == null) {
 * erroSelect(); } else { validateError(); } } else { erroSelectPerfil(); }
 * $('#modaldemensagem').modal('show'); return false; });
 * 
 * });
 * 
 */