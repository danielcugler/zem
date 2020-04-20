var replyCall = (function($, module) {

	var str1 = "Resposta";
	var str2 = "Resposta";
	var str3 = "a";
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");

	// cachedDOM
	// general module
	var $generalModule = $("#generalModule");

	var tamanho = $generalModule.length;

	// Search Module
	var $formModule = $generalModule.find("#IForm");
	var $labelSource = $formModule.find("#labelSource");
	var $labelData = $formModule.find("#labelData");
	var $labelUser = $formModule.find("#labelUser");
	var $labelEntity = $formModule.find("#labelEntity");
	var $labelClassification = $formModule.find("#labelClassification");
	var $labelPriority = $formModule.find("#labelPriority");
	var $textAreaParecer = $formModule.find("#textAreaParecer");
	var $textAreaAnswer = $formModule.find("#textAreaAnswer");
	// modal
	var $modalE = $generalModule.find("#modalE");
	var $notificacoesE = $generalModule.find("#notificacoesE");
	// templates
	var $messageTpl = $("#message-template").html();
	var $errorTpl = $("#error-template").html();

	function modal(title, body,op) {
		var tpl;
		if(op==1)
			tpl=$messageTpl;
		else
			tpl=$errorTpl;
		_render(tpl, $notificacoesE, {
			'title' : title,
			'body' : body
		});
		$modalE.modal({
			show : true,
			backdrop : true
		});
	}
	var username = $("#username").text();
	var id = getUrlVar("id");

	// validação
	$formModule.validate({
		errorPlacement : function(error, element) {
			$(element).parent().addClass("has-error");
			error.insertAfter(element);
			error.wrap("<p>");
			error.css('color', 'red');
		},
		rules : {
			textAreaAnswer : {
				required : true,
				maxlength : 4000
			}
		},
		messages : {
			textAreaAnswer : {
				required : "Insira a Resposta",
				maxlength : jQuery.validator
						.format("Por favor insira menos  que {0} caracteres.")
			}
		}
	});

	var getFields = function() {
		return JSON.stringify({
			'observation' : textAreaAnswer.text()
		});
	}

	var setFields = function(replyCall) {
		$labelSource.text(replyCall.callSource);
		$labelData.text(replyCall.creationOrUpdateDate);
		$labelUser.text(replyCall.commercialPhone);
		$labelEntity.text(replyCall.entityEntityCategoryMaps.entity.name);
		$labelClassification.text(replyCall.callClassificationId.name);
		$labelPriority.text(replyCall.priority);
		if(replyCall.observation)
		$textAreaParecer.val(replyCall.observation.information);
	}

	function loadReplyCall(id) {
		$.ajax({
			type : 'GET',
			url : '../rest/unsolvedcall3/' + id,
			dataType : "json",
			beforeSend : function(xhr) {
				xhr.setRequestHeader(header, token);
			},
			contentType : "application/json"
		}).done(function(data) {
			setFields(data);
		}).fail(function(jqXHR, textStatus,
				errorThrown) {
			modal(
					"Responder Chamado",
					jqXHR.responseJSON.errorCode
							+ ", "
							+ jqXHR.responseJSON.errorMessage,0);
		});
	}

	loadReplyCall(id);

	function restError(jqXHR, textStatus) {
		var body = jqXHR.responseJSON.errorCode + ", "
				+ jqXHR.responseJSON.errorMessage;
		modal("Modelo de Mensagem", body, 2);
	}

	function getUrlVar(key) {
		var result = new RegExp(key + "=([^&]*)", "i")
				.exec(window.location.search);
		return result && unescape(result[1]) || "";
	}



	$("#buttonSaveAndSend")
			.click(
					function(event) {
						event.preventDefault();
						if ($formModule.valid()) {
							var reply = $textAreaAnswer.val();
							$
									.ajax(
											{
												type : 'POST',
												url : '../rest/unsolvedcall3/rc/'
														+ id
														+ '/'
														+ reply,
												dataType : "json",
												data : data,
												beforeSend : function(xhr) {
													xhr.setRequestHeader(
															header, token);
												},
												contentType : "application/json"
											})
									.done(
											function(resp) {
												modal('Responder Chamado',
														resp.message,1);
											})
									.fail(
											function(jqXHR, textStatus,
													errorThrown) {
												modal(
														"Responder Chamado",
														jqXHR.responseJSON.errorCode
																+ ", "
																+ jqXHR.responseJSON.errorMessage,0);
											});
							return true;
						}
					});

	module.loadReplyCall = loadReplyCall;
	module.setFields = setFields;
	module.getFields = getFields;
	module.getUrlVar = getUrlVar;
	return module;

})(jQuery, replyCall || {});