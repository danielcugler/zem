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
	var $inOrigin = $formModule.find("#inOrigin");
	var $inDate = $formModule.find("#inDate");
	var $inUser = $formModule.find("#inUser");
	var $inStreet =  $formModule.find("#inStreet");
	var $inNumber =  $formModule.find("#inNumber");
	var $inComplement =  $formModule.find("#inComplement");
	var $selectNeighborhood =  $formModule.find("#selectNB");
	var $selectEntity = $formModule.find("#selectEntity");
	var $selectEntityCategory = $formModule.find("#selectEntityCategory");
	var $selectPriority = $formModule.find("#selectPriority");
	var $selectCallClassification = $formModule
			.find("#selectCallClassification");
	// var $inLink = $formModule.find("#inLink");
	// buttons
	var $buttonModule = $formModule.find("#botoes");
	var $btSave = $buttonModule.find("#btSave");
	var $btSaveSend = $buttonModule.find("#btSaveSend");
	// bind actions
	$btSave.on('click', save);
	$btSaveSend.on('click', saveSend);
	$selectEntity.change(function() {
		loadSelectEC($selectEntity.val());
	});
	// modal
	var $modalE = $generalModule.find("#modalE");
	var $notificacoesE = $generalModule.find("#notificacoesE");
	// templates
	var $comboCcTpl = $("#combo-cc-template").html();
	var $comboEcTpl = $("#combo-ec-template").html();
	var $comboEnTpl = $("#combo-en-template").html();
	var $comboNbTpl = $("#combo-nb-template").html();
	var $messageTpl = $("#message-template").html();
	// functions
	function loadSelectEC(entityId,entityCategoryId){
		$.ajax({
			type : 'GET',
			url : "../rest/entitycategory2/en/" + entityId,
			dataType : "json",
			contentType : "application/json"
		}).done(function(resp, status, xhr) {
			_render($comboEcTpl, $selectEntityCategory, resp);
			if(entityCategoryId)
				$selectEntityCategory
				.val(entityCategoryId);

		}).fail(function(jqXHR, textStatus) {
			var title = "Chamado";
			var body = jqXHR.responseJSON.errorCode + ", "
					+ jqXHR.responseJSON.errorMessage;
			modal(title, body,3);
		});	
	}
	
	function load(id) {
		return $.ajax({
			type : 'GET',
			url : "../rest/unsolvedcall3/combos2",
			dataType : "json",
			contentType : "application/json"
		}).done(function(resp, status, xhr) {
			_render($comboCcTpl, $selectCallClassification, resp.cc);
			//_render($comboEcTpl, $selectEntityCategory, resp.ec);
			_render($comboEnTpl, $selectEntity, resp.en);

		if(id)
			getCall(id);
		}).fail(function(jqXHR, textStatus) {
		});
	}
	
	function loadNeighborhoodCombo(){
		return $.ajax({
			type: 'GET',
			url: "../rest/unsolvedcall3/neighborhood/" + 3551,
			dataType: "json",
			contentType: "application/json"
		}).done(function(resp, status, xhr){
			_render($comboNbTpl, $selectNeighborhood, resp.nb);
		}).fail(function(jqXHR, textStatus){
			console.log("Falha em loadNeighborhoodCombo()");
		});
	}
	
	function getUrl(key) {
		var result = new RegExp(key + "=([^&]*)", "i")
				.exec(window.location.search);
		return result && unescape(result[1]) || "";
	}

	var setFields = function(call) {
		$inOrigin.val(call.callSource);
		$inDate.val(call.creationOrUpdateDate);
		if(call.updatedOrModeratedBy)
		$inUser.val(call.updatedOrModeratedBy.systemUserUsername);
		else 
			$inUser.val("Usuário móvel");

		$inId.val(call.unsolvedCallId);
		$inDescription.val(call.description.information);
		if(call.observation != null)
			$inObservation.val(call.observation.information);
		$selectEntity.val(call.entityEntityCategoryMaps.entity.entityId);
		loadSelectEC(call.entityEntityCategoryMaps.entity.entityId,call.entityEntityCategoryMaps.entityCategory.entityCategoryId);
		$inStreet.val(call.addressId.streetName);
		$inNumber.val(call.addressId.addressNumber);
		$inComplement.val(call.addressId.complement);
		$selectNeighborhood.val(call.addressId.neighborhoodId.neighborhoodId);
		switch (call.priority) {
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
		$selectCallClassification
				.val(call.callClassificationId.callClassificationId);
		
	}
	var getFields = function() {
		return JSON.stringify({
			'unsolvedCallId' : $inId.val(),
			'description' : {
				'information' : $inDescription.val()
			},
			'observation' : {
				'information' : $inObservation.val()
			},
			'priority' : $selectPriority.val(),
			'callClassificationId' : {
				'callClassificationId' : $selectCallClassification.val()
			},
			'entityEntityCategoryMaps' : {
				'entityEntityCategoryMapsPK': {
					'entityId' : $selectEntity.val(),
					'entityCategoryId' : $selectEntityCategory.val()
				}
			},
			'updatedOrModeratedBy' : {
				'systemUserUsername' : $('#username').text()
			},
			'addressId' : {
				'streetName' : $inStreet.val(),
				'addressNumber' : $inNumber.val(),
				'complement' : $inComplement.val(),
				'neighborhoodId' : {
					'neighborhoodId': $selectNeighborhood.val()
				}
			}
		});
	}
	
	function saveSend(event) {
		event.preventDefault();
		if ($formModule.valid()) {
			$.ajax({
				type : 'PUT',
				url : '../rest/unsolvedcall3/savesend',
				dataType : "json",
				data : getFields(),
				beforeSend : function(xhr) {
					xhr.setRequestHeader(header, token);
				},
				contentType : "application/json"
			}).done(function(data) {
				modal("Moderação de chamados", data.message, 1);
			}).fail(restError);

			return true;
		}
		modal("Moderação de chamados",
				"Por favor, preencha os campos destacados em vermelho!", 3);
		return false;
	}
	
	function save(event) {
		event.preventDefault();
		if ($formModule.valid()) {
			$.ajax({
				type : 'PUT',
				url : '../rest/unsolvedcall3/',
				dataType : "json",
				data : getFields(),
				beforeSend : function(xhr) {
					xhr.setRequestHeader(header, token);
				},
				contentType : "application/json"
			}).done(function(data) {
				modal("Moderação de chamados", data.message, 1);
			}).fail(restError);
			return true;
		}
		modal("Moderação de chamados",
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
			},
			selectEntityCategory : {
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
			},
			selectEntityCategory : {
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

	function getCall(unsolvedCallId) {
		$.ajax({
			type : 'GET',
			url : '../rest/unsolvedcall3/' + unsolvedCallId,
			dataType : "json",
			contentType : "application/json"
		}).done(function(data) {
			var enabled = (data.enabled === "Enabled") ? 0 : 1;
			setFields(data);
		}).fail(restError);
	}

	function restError(jqXHR, textStatus) {
		var body = jqXHR.responseJSON.errorCode + ", "
				+ jqXHR.responseJSON.errorMessage;
		modal("Moderação de chamados", body, 2);
	}

	function loadMessageTeste() {
		loadMessage(123);
	}

	function init() {
		var id = getUrl('id');
		load(id);
		loadNeighborhoodCombo();
	}

	init();
	module.setFields = setFields;
	module.getFields = getFields;
	module.modal = modal;
	module.getUrl = getUrl;
	module.load = load;
	return module;
})(jQuery, addMON || {});