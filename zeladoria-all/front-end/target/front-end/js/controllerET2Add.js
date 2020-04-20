var moduleENadd = (function($, module) {
	// security rules
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	// cached DOM
	var $generalModule = $('#generalModule');
	var $fieldModule = $generalModule.find('#fieldModule');
	var $formModule = $generalModule.find('#form-cadastro-entity');
	var $inEntityId = $fieldModule.find('#inEntityId');
	var $inName = $fieldModule.find('#inName');
	var $inEnabled = $fieldModule.find('#inEnabled');
	var $inLow = $fieldModule.find('#inLow');
	var $inMedium = $fieldModule.find('#inMedium');
	var $inHigh = $fieldModule.find('#inHigh');
	var $ckPriority = $fieldModule.find('#ckPriority');
	var $selectEntityCategory = $fieldModule.find('#selectEntityCategory');
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
	// templates
	var $messageTpl = $("#message-template").html();
	// functions

	$ckPriority.on('ifChecked', function(event) {
		$inLow.val($ckPriority.data('low'));
		$inMedium.val($ckPriority.data('medium'));
		$inHigh.val($ckPriority.data('high'));
		$inLow.prop('disabled', true);
		$inMedium.prop('disabled', true);
		$inHigh.prop('disabled', true);
	});

	$ckPriority.on('ifUnchecked', function(event) {
		$inLow.val("");
		$inMedium.val("");
		$inHigh.val("");
		$inMedium.prop('disabled', false);
		$inHigh.prop('disabled', false);
		$inLow.prop('disabled', false);
	});

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
			},
			inHigh : {
				required : true
			},
			inMedium : {
				required : true

			},
			inLow : {
				required : true

			}
		},
		messages : {
			inLow : "Insira a prioridade baixa",
			inMedium : "Insira a prioridade média",
			inHigh : "Insira a prioridade alta",
			inName : {
				required : "Insira o nome",
				maxlength : $.validator
						.format("Por favor insira menos que {0} caracteres.")
			}

		}
	});

	var changeButton = function() {
		$btAdd.hide();
		$btEdit.show();
	}
	function restError(jqXHR, textStatus) {
		var body = jqXHR.responseJSON.errorCode + ", "
				+ jqXHR.responseJSON.errorMessage;
		modal("Entidade", body, 2);
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
	$selectEntityCategory
			.multiSelect({
				selectableHeader : "<div class='custom-header'>Categorias existentes</div>",
				selectionHeader : "<div class='custom-header'>Categorias selecionadas</div>",
				selectableFooter : "<div class='custom-footer'><a id='selecionarTodos'>Selecionar todas</a></div>",
				selectionFooter : "<div class='custom-footer'><a id='removerTodos'>Remover todas</a></div>"
			});
	$("#selecionarTodos").on("click", selecionaTudo);
	$("#removerTodos").on("click", removeTudo);
	function getUrl(key) {
		var result = new RegExp(key + "=([^&]*)", "i")
				.exec(window.location.search);
		return result && unescape(result[1]) || "";
	}
	function getFields() {
		var entity = {};
		entity.entityId = $inEntityId.val();
		entity.name = $inName.val();
		entity.attendanceTime = {};
		entity.attendanceTime.highPriorityTime = $inHigh.val();
		entity.attendanceTime.mediumPriorityTime = $inMedium.val();
		entity.attendanceTime.lowPriorityTime = $inLow.val();
		entity.attendanceTime.enabled = 0;
		entity.enabled = $inEnabled.val();
		entity.entityCategoryCollection = [];
		var ec = $selectEntityCategory.val();
		for ( var key in ec)
			entity.entityCategoryCollection.push({
				"entityCategoryId" : parseInt(ec[key])
			});
		return JSON.stringify(entity);
	}
	function setFields(resp) {
		$inEntityId.val(resp.entityId);
		$inName.val(resp.name);
		$inHigh.val(resp.attendanceTime.highPriorityTime);
		$inMedium.val(resp.attendanceTime.mediumPriorityTime);
		$inLow.val(resp.attendanceTime.lowPriorityTime);
		$inEnabled.val(resp.enabled);
	}

	function getEntity(id) {
		
		$.ajax({
			type : 'GET',
			async : false,
			url : '../rest/entitycategory2',
			dataType : "json",
			contentType : "application/json"
		}).done(function(data) {
			for (cont = 0; cont < data.length; cont++)
				$selectEntityCategory.multiSelect('addOption', {
					value : data[cont].entityCategoryId,
					text : data[cont].name,
					index : cont
				});
		
		
		$
				.ajax({
					type : 'GET',
					url : "../rest/entity2/" + id,
					dataType : "json",
					contentType : "application/json"
				})
				.done(
						function(resp) {
							setFields(resp);
							for (cont = 0; cont < resp.entityCategoryCollection.length; cont++)
								$selectEntityCategory
										.multiSelect(
												'select',
												resp.entityCategoryCollection[cont].entityCategoryId
														.toString());

						}).fail(function(jqXHR, textStatus) {
				});
		}).fail(restError);
	}

	

	// Função selecionar todos os elementos do select
	function selecionaTudo() {
		$selectEntityCategory.multiSelect('select_all');
	}

	// Função remover todos os elementos do select
	function removeTudo() {
		$selectEntityCategory.multiSelect('deselect_all');
	}

	// Preencher o select com as categorias de entidade disponíveis
	function mSelect(data) {
		var cont = 0;
		for (cont = 0; cont < data.length; cont++)
			$selectEntityCategory.multiSelect('addOption', {
				value : data[cont].entityCategoryId,
				text : data[cont].name,
				index : cont
			});
	}

	// Preencher o select com as categorias de entidade selecionadas
	function mSelected(data) {
		var cont = 0;
		var temp;
		for (cont = 0; cont < data.length; cont++) {
			temp = data[cont].entityCategoryId.toString();
			$selectEntityCategory.multiSelect('select', temp);
		}
	}

	// AJAX | Busca todos as categorias de entidade
	function selectListAll() {
		$.ajax({
			type : 'GET',
			async : false,
			url : '../rest/entitycategory2',
			dataType : "json",
			contentType : "application/json"
		}).done(function(data) {
			for (cont = 0; cont < data.length; cont++)
				$selectEntityCategory.multiSelect('addOption', {
					value : data[cont].entityCategoryId,
					text : data[cont].name,
					index : cont
				});
		}).fail(restError);
	}


	// AJAX | Busca os valores do tempo de atendimento padrão
	function getDefaultAt() {
		$.ajax({
			type : 'GET',
			async : false,
			url : '../rest/attendancetime2/at',
			dataType : "json",
			contentType : "application/json",
			success : function(resp) {
				$ckPriority.data("low", resp.lowPriorityTime);
				$ckPriority.data("medium", resp.mediumPriorityTime);
				$ckPriority.data("high", resp.highPriorityTime);
			},
			error : function(jqxhr, status, errorMsg) {
				// Mensagem de erro
			}
		});
	}

	function edit(event) {
		event.preventDefault();
		// validaISalvar();
		var ec = $selectEntityCategory.val();
		if ($formModule.valid() && ec) {
			$.ajax({
				type : 'PUT',
				url : '../rest/entity2/' + $('#username').text(),
				dataType : "json",
				data : getFields(),
				beforeSend : function(xhr) {
					xhr.setRequestHeader(header, token);
				},
				contentType : "application/json"
			}).done(function(data) {
				modal("Entidade", "Entidade atualizada com sucesso!", 1);
				return true;
			}).fail(restError);
		} else {
			if (ec)
				modal("Entidade",
						"Por favor, selecione pelo menos uma categoria de entidade na lista!", 3);
			else
				modal(
						"Entidade",
						"Por favor, preencha os campos destacados em vermelho!",
						3);
			return false;
		}
	}

	function save(event) {
		event.preventDefault();
		// validaISalvar();
		var ec = $selectEntityCategory.val();
		if ($formModule.valid() && ec) {
			$.ajax({
				type : 'POST',
				url : '../rest/entity2/' + $('#username').text(),
				dataType : "json",
				data : getFields(),
				beforeSend : function(xhr) {
					xhr.setRequestHeader(header, token);
				},
				contentType : "application/json"
			}).done(function(data) {
				modal("Entidade", "Entidade inserida com sucesso!", 1);
				return true;
			}).fail(restError);
		} else {
			if (ec)
				modal(
						"Entidade",
						"Por favor, preencha os campos destacados em vermelho!",
						3);
			else
				modal("Entidade",
						"Por favor, selecione pelo menos uma categoria de entidade na lista!", 3);
			return false;

		}
	}
	function init() {
		$inEnabled.val('0');
		var id = getUrl('id');
		getDefaultAt();
		if (id) {
			changeButton();
			getEntity(id)
		} else
			selectListAll();
	}
	init();

	module.getFields = getFields;
	return module;

})(jQuery, moduleENadd || {});
/*
 * var str1 = "Entidade"; var str2 = "Entidade"; var str3 = "a"; // Preenche os
 * campos com os valores da entidade que está sendo editada function
 * loadInputs(data) { var entity = JSON.parse(data.jsonList);
 * $('#CId').val(entity.entityId); $('#CName').val(entity.name);
 * $('#CEnabled').val(entity.enabled === 'ENABLED' ? 0 : 1); if
 * (entity.attendanceTime != null) {
 * $('#cMedium').val(entity.attendanceTime.mediumPriorityTime);
 * $('#cLow').val(entity.attendanceTime.lowPriorityTime);
 * $('#cHigh').val(entity.attendanceTime.highPriorityTime);
 * $('#cAtEnabled').val(entity.attendanceTime.enabled); } else {
 * $('#cMedium').val($("label[for = cDMedium]").text());
 * $('#cLow').val($("label[for = cDLow]").text()); $('#cHigh').val($("label[for =
 * cDHigh]").text()); } } // Preenche os campos com os valores de tempo de
 * atendimento padrão function loadDefaultAt(data) { var at =
 * JSON.parse(data.jsonList); $("label[for = cDLow]").text(at.lowPriorityTime);
 * $("label[for = cDMedium]").text(at.mediumPriorityTime); $("label[for =
 * cDHigh]").text(at.highPriorityTime); } // Função selecionar todos os
 * elementos do select function selecionaTudo() {
 * $('#selectEntidade').multiSelect('select_all'); } // Função remover todos os
 * elementos do select function removeTudo() {
 * $('#selectEntidade').multiSelect('deselect_all'); } // Preencher o select com
 * as categorias de entidade disponíveis function mSelect(data) { var list =
 * JSON.parse(data.jsonList); var cont = 0; for (cont = 0; cont < list.length;
 * cont++) $('#selectEntidade').multiSelect('addOption', { value :
 * list[cont].entityCategoryId, text : list[cont].name, index : cont }); };
 * 
 * //Preencher o select com as categorias de entidade selecionadas function
 * mSelected(data) { var list = data; var cont = 0; var temp; for (cont = 0;
 * cont < list.length; cont++) { temp = list[cont].entityCategoryId.toString();
 * $('#selectEntidade').multiSelect('select', temp); } }; // AJAX | Busca todos
 * as categorias de entidade function selectListAll(URL) { $.ajax({ type :
 * 'GET', async : false, url : URL, dataType : "json", contentType :
 * "application/json", success : function(data) { mSelect(data); }, error :
 * function(jqxhr, status, errorMsg) { // Mensagem de erro } }); }; // AJAX |
 * Busca as categorias de entidade que estão relacionadas a entidade que está
 * sendo editada function findByIdADDET(URL, id) { $.ajax({ type : 'GET', async :
 * false, url : URL + '/ec/' + id, dataType : "json", contentType :
 * "application/json", success : function(data) { loadInputs(data); var list =
 * JSON.parse(data.jsonList); var entityCategory =
 * list.entityCategoryCollection; mSelected(entityCategory); }, error :
 * function(jqxhr, status, errorMsg) { // Mensagem de erro } }); }; // AJAX |
 * Busca os valores do tempo de atendimento padrão function getDefaultAt(URL) {
 * $.ajax({ type : 'GET', async : false, url : URL, dataType : "json",
 * contentType : "application/json", success : function(data) {
 * loadDefaultAt(data); }, error : function(jqxhr, status, errorMsg) { //
 * Mensagem de erro } }); }; // Início document ready $(document) .ready(
 * function() {
 * 
 * var token = $("meta[name='_csrf']").attr("content"); var header =
 * $("meta[name='_csrf_header']").attr("content");
 * 
 * getDefaultAt('../rest/entityclass/at'); $('#selectEntidade') .multiSelect( {
 * selectableHeader : "<div class='custom-header'>Categorias existentes</div>",
 * selectionHeader : "<div class='custom-header'>Categorias selecionadas</div>",
 * selectableFooter : "<div class='custom-footer'><a id='selecionarTodos'
 * onClick='selecionaTudo();'>Selecionar todas</a></div>", selectionFooter : "<div
 * class='custom-footer'><a id='removerTodos' onClick='removeTudo();'>Remover
 * todas</a></div>" });
 * 
 * $('#form-cadastro-entity') .validate( { errorPlacement : function(error,
 * element) { $(element).parent().addClass( "has-error");
 * error.insertAfter(element); error.wrap("<p>"); error.css('color', 'red'); },
 * rules : { nome : { required : true, maxlength : 40, messages : { required :
 * "Insira o nome", maxlength : jQuery.validator .format("Por favor insira menos
 * que {0} caracteres.") } } }, }); function validaISalvar() {
 * 
 * $('[name="nome"]') .each( function() { $(this) .rules( 'add', { required :
 * true, maxlength : 40, messages : { required : "Insira o nome", maxlength :
 * jQuery.validator .format("Por favor insira menos que {0} caracteres.") } });
 * });
 * 
 * $('[name="cHigh"]').each(function() { $(this).rules('add', { required : true,
 * messages : { required : "Insira a prioridade", } }); });
 * 
 * $('[name="cMedium"]').each(function() { $(this).rules('add', { required :
 * true, messages : { required : "Insira a prioridade", } }); });
 * 
 * $('[name="cLow"]').each(function() { $(this).rules('add', { required : true,
 * messages : { required : "Insira a prioridade", } }); }); }
 * 
 * function getInputs(opcao, ec) {
 * 
 * var entityClass = {};
 * 
 * var attendanceTime = { 'highPriorityTime' : $('#cHigh').val(),
 * 'mediumPriorityTime' : $('#cMedium').val(), 'lowPriorityTime' :
 * $('#cLow').val() };
 * 
 * if (opcao == 2) { entityClass.entityId = parseInt($( '#form-cadastro-entity
 * #CId').val()); } entityClass.name = $('#form-cadastro-entity #CName') .val();
 * if (opcao == 1) { entityClass.enabled = 0; attendanceTime.enabled = 0; } else {
 * entityClass.enabled = $( '#form-cadastro-entity #CEnabled').val() ===
 * 'Enabled' ? 0 : 1; attendanceTime.enabled = $( '#form-cadastro-entity
 * #cAtEnabled').val() === 'Enabled' ? 0 : 1; attendanceTime.entityId =
 * entityClass.entityId; } entityClass.entityCategoryCollection = []; for ( var
 * key in ec) entityClass.entityCategoryCollection.push({ "entityCategoryId" :
 * parseInt(ec[key]) }); entityClass.attendanceTime = attendanceTime; return
 * JSON.stringify(entityClass); };
 * 
 * function pegaElementos(values) { var cont = 0; for (cont = 0; cont <
 * values.length; cont++) { findCategory(values[cont]); } };
 * 
 * function removeElementos(values) { var cont = 0; var i = 0; var index; for (i =
 * 0; i < values.length; i++) { for (cont = 0; cont < lista.length; cont++) { if
 * (lista[cont].entityCategoryId == values[i]) lista.splice(cont, 1); } } };
 * 
 * function getUrlVar(key) { var result = new RegExp(key + "=([^&]*)", "i")
 * .exec(window.location.search); return result && unescape(result[1]) || ""; }
 * var id = getUrlVar('id');
 * 
 * if (id === '') { selectListAll('../rest/entitycategory/combosativos'); //
 * selectListAll("http://localhost:8080/rest/entitycategory/combos");
 * $('#saveButton').addClass('incluirAction'); } else {
 * selectListAll('../rest/entitycategory/combosativos'); //
 * selectListAll("http://localhost:8080/rest/entitycategory/combos");
 * findByIdADDET('../rest/entityclass', id);
 * $('#saveButton').addClass('editarAction'); }
 * 
 * $('#modaldemensagem') .on( "click", ".okbutton", function(event) {
 * event.preventDefault(); window.location = '../entity.jsp'; });
 * 
 * $('#defaultAt').on('ifUnchecked', function(event) { $('#cLow').val("");
 * $('#cMedium').val(""); $('#cHigh').val("");
 * 
 * $("#cLow").prop('disabled', false); $("#cMedium").prop('disabled', false);
 * $("#cHigh").prop('disabled', false); });
 * 
 * $('#defaultAt').on('ifChecked', function(event) { $('#cLow').val($("label[for =
 * cDLow]").text()); $('#cMedium').val($("label[for = cDMedium]").text());
 * $('#cHigh').val($("label[for = cDHigh]").text());
 * 
 * $("#cLow").prop('disabled', true); $("#cMedium").prop('disabled', true);
 * $("#cHigh").prop('disabled', true); });
 * 
 * $('.editarAction').click( function(event) { event.preventDefault();
 * validaISalvar(); var ec = $('#selectEntidade').val(); if
 * ($("#form-cadastro-entity").valid() && ec !== null) {
 * 
 * var entity = getInputs(2, ec); mergeTokenET('../rest/entityclass/' +
 * $('#username').text(), entity, header, token); return true; } if (ec == null) {
 * erroSelect(); } else { validateError(); }
 * 
 * $('#modaldemensagem').modal('show'); return false;
 * 
 * });
 * 
 * $('#modalMsg').on('hidden.bs.modal', function() { window.location =
 * '../addEntity.jsp' });
 * 
 * $('.incluirAction').click( function(event) { event.preventDefault();
 * validaISalvar(); var ec = $('#selectEntidade').val(); if
 * ($("#form-cadastro-entity").valid() && ec !== null) {
 * 
 * var entity = getInputs(1, ec); saveENTToken('../rest/entityclass/' +
 * $('#username').text(), entity, header, token); return true; } if (ec == null) {
 * erroSelect(); } else { validateError(); }
 * $('#modaldemensagem').modal('show'); return false; });
 * 
 * });
 */