var addMON = (function($, module) {
	var list = $('#list li').map(function() {
		return $(this).text();
	});
	// security rules
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	var roleEdit = $.inArray('ROLE_CALL_UPDATE', list) >= 0;
	var roleEnabled = $.inArray('ROLE_CALL_ENABLE', list) >= 0;
	// cached DOM
	// general module
	var $generalModule = $("#generalModule");
	// Field Module
	var $formModule = $generalModule.find("#IForm");
	var $inId = $formModule.find("#inId");
	var $inDescription = $formModule.find("#inDescription");
	var $inObservation = $formModule.find("#inObservation");
	var $selectEntity = $formModule.find("#selectEntity");
	var $selectEntityCategory = $formModule.find("#selectEntityCategory");
	var $selectPriority = $formModule.find("#selectPriority");
	var $selectCallClassification = $formModule
			.find("#selectCallClassification");
	// var $inLink = $formModule.find("#inLink");
	// buttons
	var $buttonModule = $formModule.find("#botoes");
	var $btAdd = $buttonModule.find("#btAdd");
	var $btEdit = $buttonModule.find("#btEdit");
	var $btSend = $buttonModule.find("#btSend");
	var $btSendEdit = $buttonModule.find("#btSendEdit");
	// bind actions
	$btAdd.on('click', function(){});
	$btEdit.on('click', function(){});
	$btSend.on('click', function(){});
	$btSendEdit.on('click', function(){});
	// modal
	var $modalE = $generalModule.find("#modalE");
	var $notificacoesE = $generalModule.find("#notificacoesE");
	// templates
	var $comboCcTpl = $("#combo-cc-template").html();
	var $comboEcTpl = $("#combo-ec-template").html();
	var $comboEnTpl = $("#combo-en-template").html();
	var $messageTpl = $("#message-template").html();
	// functions
	function makeCombos() {
		$.ajax({
			type : 'GET',
			url : "../rest/unsolvedcall3/combos2",
			dataType : "json",
			contentType : "application/json"
		}).done(function(resp, status, xhr) {
			_render($comboCcTpl, $selectCallClassification, resp.cc);
			_render($comboEcTpl, $selectEntityCategory, resp.ec);
			_render($comboEnTpl, $selectEntity, resp.en);
		}).fail(function(jqXHR, textStatus) {
		});
	}
	function getUrl(key) {
		var result = new RegExp(key + "=([^&]*)", "i")
				.exec(window.location.search);
		return result && unescape(result[1]) || "";
	}
	var changeButton = function() {
		$btAdd.hide();
		$btEdit.show();
		$btSend.hide();
		$btSendEdit.show();
	}
	var setFields = function(call) {
		$inId.val(call.unsolvedCallId);
		$inDescription.val(call.description.information);
		$selectEntity.val(call.entityEntityCategoryMaps.entity.entityId);
		$selectEntityCategory.val(call.entityEntityCategoryMaps.entityCategory.entityCategoryId);
		switch(call.priority){
		case 'Baixa':
		$selectPriority.val(0);
		break;
		case 'Média':
			$selectPriority.val(1);
			break;
		case 'Alta':
			$selectPriority.val(2);
			break;
	}
		$selectCallClassification.val(call.callClassificationId.callClassificationId);
	}
	var getFields = function() {
		return JSON.stringify({
			'unsolvedCAllIdId' : $inId.val(),
			'description' : {'information':$inDescription.val()},
			'observation' : {'information':$inObservation.val()},
			'callPriority' : $selectPriority.val(),
			'messageBody' : $inMessage.val(),
			'broadcastMessageCategoryId' : {
				'broadcastMessageCategoryId' : $comboBMC.val()
			}

		});
	}
	function edit(event) {
		event.preventDefault();
		validaISalvar();
		if ($formModule.valid()) {
			$
					.ajax(
							{
								type : 'PUT',
								url : '../rest/broadcastmessage2/'
										+ $('#username').text(),
								dataType : "json",
								data : getFields(),
								beforeSend : function(xhr) {
									xhr.setRequestHeader(header, token);
								},
								contentType : "application/json"
							})
					.done(
							function(data) {
								modal(
										"Modelo de Mensagem",
										"Modelo de Mensagem atualização feita com sucesso!",
										1);
							}).fail(restError);

			return true;
		}
		modal("Modelo de Mensagem",
				"Por favor, preencha os campos destacados em vermelho!", 3);
		return false;

	}
	function add(event) {
		event.preventDefault();
		validaISalvar();
		if ($formModule.valid()) {
			$.ajax({
				type : 'POST',
				url : '../rest/broadcastmessage2/' + $('#username').text(),
				dataType : "json",
				data : getFields(),
				beforeSend : function(xhr) {
					xhr.setRequestHeader(header, token);
				},
				contentType : "application/json"
			}).done(
					function(data) {
						modal("Modelo de Mensagem",
								"Modelo de Mensagem inserido com sucesso!", 1);
					}).fail(restError);
			return true;
		}
		modal("Modelo de Mensagem",
				"Por favor, preencha os campos destacados em vermelho!", 3);
		return false;
	}
	// render
	function _render(baseTemplate, selector, data) {
		var template = Handlebars.compile(baseTemplate);
		var html = template(data);
		selector.html(html);
	}
	// validations
	$formModule.validate({

		errorPlacement : function(error, element) {
			$(element).parent().addClass("has-error");
			error.insertAfter(element);
			error.wrap("<p>");
			error.css('color', 'red');
		},
		rules : {
			selectPriority : {
				required : true
			},
			selectEntity : {
				required : true
			},selectEntityCategory : {
				required : true
			},
			selectCallClassification : {
				required : true
			},
			inDescription : {
				required : true,
				maxlength : 1000
			},
			inObservation : {
				maxlength : 1000
			}
		},
		messages : {
			inDescription : {
				required : "Insira  a descrição",
				maxlength : jQuery.validator
						.format("Por favor, insira menos que {0} caracteres.")
			},
			inObservation : {
				maxlength : jQuery.validator
						.format("Por favor, insira menos que {0} caracteres.")
			},
			selectPriority : {
				required : "Selecione uma opção"
			},
			selectEntity : {
				required : "Selecione uma opção"
			},selectEntityCategory : {
				required : "Selecione uma opção"
			},
			selectCallClassification : {
				required : "Selecione uma opção"
			}

		}
	});

	// functions
	// op: 1 sucesso, 2 erro de rest, 3 erro de validação
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

	function load(unsolvedCallId) {
		$
				.ajax({
					type : 'GET',
					url : '../rest/unsolvedcall3/' + unsolvedCallId,
					dataType : "json",
					contentType : "application/json"
				})
				.done(
						function(data) {
							var enabled = (data.enabled === "Enabled") ? 0 : 1;
							setFields(data);
						}).fail(restError);
	}

	function restError(jqXHR, textStatus) {
		var body = jqXHR.responseJSON.errorCode + ", "
				+ jqXHR.responseJSON.errorMessage;
		modal("Modelo de Mensagem", body, 2);
	}

	function loadMessageTeste() {
		loadMessage(123);
	}

	function init() {
		var id=getUrl('id');
		if (id) {
			changeButton();
			load(id)
		}
	}
	makeCombos();
	init();
	module.changeButton = changeButton;
	module.setFields = setFields;
	module.getFields = getFields;
	module.modal = modal;
	module.getUrl = getUrl;
	module.load = load;
	return module;
})(jQuery, addMON || {});

/*
 * 
 * 
 * 
 * var str2 = "Chamados"; var str3 = "o";
 * 
 * function getPriority(priority) { switch (priority) { case "Baixa": return 0;
 * case "Média": return 1; case "Alta": return 2; } };
 * 
 * function loadInputs(data) {
 * 
 * if (data.observation != null) {
 * $('#CComments').val(data.observation.information); $('#CCommentsId')
 * .val(data.observation.additionalInformationId); }
 * $('#CDescription').val(data.description.information);
 * $('#CDescriptionId').val(data.description.additionalInformationId);
 * $('#COrigem').val(data.callSource); $('#CDate').val(data.creationDate);
 * $('#CUser').val('X' || data.updatedOrModeratedBy.name);
 * $('#selectPriority').prop('selected', false);
 * $('#selectCallClassification').prop('selected', false);
 * $('#selectEntityCategory').prop('selected', false); $( "#selectPriority
 * option[value='" + getPriority(data.priority) + "']").prop( 'selected', true); $(
 * "#selectCallClassification option[value='" +
 * data.callClassificationId.callClassificationId + "']").prop('selected',
 * true);
 *  $( "#selectEntity option[value='" +
 * data.entityEntityCategoryMaps.entity.entityId + "']").prop('selected', true);
 * 
 * combo("../rest/entitycategory/activeentity/" +
 * data.entityEntityCategoryMaps.entity.entityId,
 * ModelsTmpl.tmplComboEntityCategory, "#comboEntityCategory");
 *  $( "#selectEntityCategory option[value='" +
 * data.entityEntityCategoryMaps.entityCategory.entityCategoryId +
 * "']").prop('selected', true);
 *  } $(document) .ready( function() {
 * 
 * var token = $("meta[name='_csrf']").attr("content"); var header =
 * $("meta[name='_csrf_header']").attr("content");
 * 
 * $('#IForm').validate({ errorPlacement : function(error, element) {
 * $(element).parent().addClass("has-error"); error.insertAfter(element);
 * error.wrap("<p>"); error.css('color', 'red'); }, });
 * 
 * function validaISalvar() {
 * 
 * $('[name="selectEntityCategory"]').each(function() { $(this).rules('add', {
 * required : true, messages : { required : "Selecione uma opção" } }); });
 * 
 * 
 * combo( "../rest/entityclass/combosativos", ModelsTmpl.tmplComboEntity,
 * "#comboEntity"); combo("../rest/callclassification",
 * ModelsTmpl.tmplComboCallClassification, "#comboCallClassification");
 * template({}, ModelsTmpl.tmplComboPriority, "#comboPriority"); var id =
 * getUrlVar('id');
 * 
 * $('#selectEntity') .change( function() { $('#selectEntity',
 * '#selectEntityCategory') .remove(); combo(
 * '../rest/entitycategory/activeentity/' + $( '#selectEntity option:selected')
 * .val(), ModelsTmpl.tmplComboEntityCategory, "#comboEntityCategory"); });
 * 
 * function getInputsIN() { var broadcastMessage = {
 * 
 * 'subject' : $('#cSubject').val(), 'messageBody' : $('#cMessageBody').val(),
 * 'enabled' : 'ENABLED' };
 * 
 * return JSON.stringify(broadcastMessage); } ;
 * 
 * function getInputsEDIT(id) { var callclaid = 0; switch
 * ($("#selectCallClassification option:selected") .val()) { case 'Crítica':
 * call = 0; case 'Denúncia': call = 1; case 'Elogio': call = 2; case
 * 'Pedidosdeinformacao': call = 3; case 'Solicitações': call = 4; case
 * 'Sugestão': call = 5; } ; var call = {
 * 
 * 'unsolvedCallId' : id, 'subject' : $('#cSubject').val(), 'messageBody' :
 * $('#cMessageBody').val(), 'entityEntityCategoryMaps' : { 'entityCategory' : {
 * 'entityCategoryId' : $( "#selectEntityCategory option:selected") .val() },
 * 'entity' : { 'entityId' : $( "#selectEntity option:selected") .val() } },
 * 'callClassificationId' : { 'callClassificationId' : $(
 * "#selectCallClassification option:selected") .val() }, 'updatedOrModeratedBy' : {
 * 'systemUserUsername' : $('#username').text() }, 'callClassificationId' : {
 * 'callClassificationId' : callclaid }, 'parentCallId' : { 'unsolvedCallId' :
 * id }, 'enabled' : 0 };
 * 
 * return JSON.stringify(call); } ;
 * 
 * function getUrlVar(key) { var result = new RegExp(key + "=([^&]*)", "i")
 * .exec(window.location.search); return result && unescape(result[1]) || ""; }
 * 
 * findByIdADD("../rest/unsolvedcall3", id);
 * 
 * function getCall(id) { var callclaid = 0; switch
 * ($("#selectCallClassification option:selected") .val()) { case 'Crítica':
 * call = 0; case 'Denúncia': call = 1; case 'Elogio': call = 2; case
 * 'Pedidosdeinformacao': call = 3; case 'Solicitações': call = 4; case
 * 'Sugestão': call = 5; } ; var call = { 'unsolvedCallId' : id, 'description' : {
 * 'additionalInformationId' : $('#CDescriptionId') .val(), 'information' :
 * $('#CDescription').val() }, 'observation' : { 'additionalInformationId' :
 * $('#CCommentsId') .val(), 'information' : $('#CComments').val() },
 * 'callClassificationId' : $( "#selectCallClassification option:selected")
 * .val(), 'priority' : $("#selectPriority option:selected") .val(),
 * 'entityEntityCategoryMaps' : { 'entityCategory' : { 'entityCategoryId' : $(
 * "#selectEntityCategory option:selected") .val() }, 'entity' : { 'entityId' : $(
 * "#selectEntity option:selected") .val() } }, 'updatedOrModeratedBy' : {
 * 'systemUserUsername' : $('#username').text() }, 'callClassificationId' : {
 * 'callClassificationId' : callclaid }, 'parentCallId' : { 'unsolvedCallId' :
 * id } };
 * 
 * return JSON.stringify(call); } ;
 * 
 * $('#modaldemensagem') .on( "click", ".okbutton", function(event) {
 * event.preventDefault(); window.location = '../call2.jsp'; });
 * 
 * $('.saveAction') .click( function(event) { event.preventDefault();
 * validaISalvar(); if ($("#IForm").valid()) {
 * 
 * var entityCategoryTarget = $( "#selectEntityCategory option:selected")
 * .val(); var updatedOrModeratedBy = $( '#CUser').val(); var
 * callClassificationId = $( "#selectCallClassification option:selected")
 * .val(); var call = getCall(id);
 * 
 * saveToken("../rest/unsolvedcall", call, header, token);
 *  }
 * 
 * return false; });
 * 
 * $('.saveSendAction') .click( function(event) { event.preventDefault();
 * validaISalvar(); if ($("#IForm").valid()) { var entityCategoryTarget = $(
 * "#selectEntityCategory option:selected") .val(); var updatedOrModeratedBy = $(
 * '#CUser').val(); var callClassificationId = $( "#selectCallClassification
 * option:selected") .val(); var call = getCall(id);
 * 
 * saveToken('../rest/unsolvedcall/savesend', call, header, token);
 *  }
 * 
 * return false; });
 * 
 * });
 * 
 */
