var ctrlCFadd = (function($, module) {
	var newOnPage = true;
	var list = $('#list li').map(function() {
		return $(this).text();
	});
	// security rules
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	var roleEdit = $.inArray('ROLE_USER_UPDATE', list) >= 0;
	var roleEnabled = $.inArray('ROLE_USER_ENABLE', list) >= 0;
	// cached DOM
	// general module
	var $generalModule = $("#generalModule");
	// user
	var $username = $generalModule.find("#username");
	var nameUsername = $username.val();
	// Search Module
	var $formModule = $generalModule.find("#IForm");
	// fields
	var $selectStatus = $formModule.find("#selectStatus");
	var $divObservation = $formModule.find("#Observation");
	var $taObservation = $formModule.find("#taObservation");
	var $divFeedback = $formModule.find("#Feedback");
	var $taFeedback = $formModule.find("#taFeedback");
	var $divFeedback2 = $formModule.find("#Feedback2");
	var $taFeedback2 = $formModule.find("#taFeedback2");
	var $divReply = $formModule.find("#Reply");
	var $taReply = $formModule.find("#taReply");
	var $inSendMessage = $formModule.find("#inSendMessage");
	var $spFeedback = $formModule.find("#feedback");
	// buttons
	var $btEdit = $formModule.find("#btEdit");
	var $btAddMsg = $formModule.find("#btAddMsg");
	// modal
	var $modalE = $generalModule.find("#modalE");
	var $notificacoesE = $generalModule.find("#notificacoesE");
	// templates
	var $comboNameTpl = $("#combo-name-template").html();
	var $comboSubjectTpl = $("#combo-subject-template").html();
	var $edmodalTpl = $("#edmodal-template").html();
	var $edMessageTpl = $("#edmessage-template").html();
	var $messageListTpl = $("#message-list-template").html();
	// bind events
	$selectStatus.on('change', function(event) {
		event.preventDefault();
		changeDiv($selectStatus.val());
	});
	$btAddMsg.click(function(event) {
		event.preventDefault();
		showMessages();
	});
	$btEdit.click(function(event) {
		event.preventDefault();
	});
	$btEdit.click(function(event) {
		event.preventDefault();
		edit();
	});
	function edit() {
		if ($formModule.valid()) {
			var unsolvedCall = getFields();
			if (unsolvedCall.callProgress == 5
					|| unsolvedCall.callProgress == 6)
				upCall(unsolvedCall);
			else
				finishCall(unsolvedCall);
		}
		return false;
	}
	function finishCall(unsolvedCall) {
		console.log(JSON.stringify(unsolvedCall));
		return $.ajax({
			type : 'POST',
			url : '../rest/callfollow2/finishcall',
			dataType : "json",
			data : JSON.stringify(unsolvedCall),
			beforeSend : function(xhr) {
				xhr.setRequestHeader(header, token);
			},
			contentType : "application/json"
		}).done(function(resp) {
			modal("Chamado Interno", resp.message,1);
		}).fail(function() {

		});
	}
	function upCall(unsolvedCall) {
		return $.ajax({
			type : 'POST',
			url : '../rest/callfollow2/upcall',
			dataType : "json",
			data : JSON.stringify(unsolvedCall),
			beforeSend : function(xhr) {
				xhr.setRequestHeader(header, token);
			},
			contentType : "application/json"
		}).done(function(resp) {
			modal("Chamado Interno", resp.message,1);
		}).fail(function(jqXHR, textStatus) {
			var title = "Chamado Interno";
			var body = jqXHR.responseJSON.errorCode + ", "
					+ jqXHR.responseJSON.errorMessage;
			modal(title, body,3);
		});
	}
	function changeDiv(status) {
		switch (status) {
		case "0":
			$divFeedback.show();
			$divFeedback2.hide();
			$divReply.hide();
			$divObservation.hide();
			$spFeedback.append("<span class='vermelho'>*</span>");
			break;
		case "1":
			$divFeedback.hide();
			$divReply.hide();
			$divObservation.show();
			$divFeedback2.hide();
			$spFeedback.text("Parecer");
			break;
		case "2":
			$divFeedback.hide();
			$divReply.show();
			$divObservation.hide();
			$divFeedback2.show();
			$spFeedback.text("Parecer");
			break;
		}
	}
	// render
	function _render(baseTemplate, selector, data) {
		var template = Handlebars.compile(baseTemplate);
		var html = template(data);
		selector.html(html);
	}
	// functions
	function modal(title, body,op) {
		_render($edMessageTpl, $notificacoesE, {
			'title' : title,
			'body' : body
		});
		if (op == 1)
			$notificacoesE.find("#bOk1").show();
		else if (op == 2)
			$notificacoesE.find("#bOk2").show();
		else
			$notificacoesE.find("#bOk3").show();
				$modalE.modal({
			show : true,
			backdrop : true
		});
	}
	function getFields() {
		var unsolvedCall = {};
		unsolvedCall.unsolvedCallId = getUrlParam("id");
		switch ($selectStatus.val()) {
		case "0":
			unsolvedCall.callProgress = 6;
			if ($taFeedback.val())
				unsolvedCall.observation = {
					information : $taFeedback.val()
				};
			break;
		case "1":
			unsolvedCall.callProgress = 5;
			if ($taObservation.val())
				unsolvedCall.observation = {
					information : $taObservation.val()
				};
			break;
		case "2":
			if ($inSendMessage.val() === "Enabled") {
				unsolvedCall.callProgress = 3;
				if ($taReply.val())
					unsolvedCall.observation = {
						information : $taReply.val()};
				if ($taFeedback2.val())
					unsolvedCall.description = {
						information : $taFeedback2.val()
					};
			} else {
				unsolvedCall.callProgress = 2;
				if ($taReply.val())
					unsolvedCall.observation = {
						information : $taReply.val()};
				if ($taFeedback2.val())
					unsolvedCall.description = {
						information : $taFeedback2.val()
					};
			}

			break;
		}
		unsolvedCall.updatedOrModeratedBy = {
			'systemUserUsername' : nameUsername
		};
		return unsolvedCall;
	}
	function setFields(call) {
		$inSendMessage
				.val(call.entityEntityCategoryMaps.entityCategory.send_message);
		switch (call.callProgress) {
		case 'Encaminhado':
			$.ajax({
				type : 'PUT',
				url : '../rest/callfollow2/seen/' + call.unsolvedCallId,
				dataType : "json",
				beforeSend : function(xhr) {
					xhr.setRequestHeader(header, token);
				},
				contentType : "application/json",
			}).done(function(resp) {
				changeDiv("1");
				$selectStatus.val('1');
			}).fail(function(jqXHR, textStatus) {
				var title = "Chamado Interno";
				var body = jqXHR.responseJSON.errorCode + ", "
						+ jqXHR.responseJSON.errorMessage;
				modal(title, body,3);
			});
			break;
		case 'Visualizado':
			changeDiv("1");
			$selectStatus.val('1');
			break;
		case 'Em Andamento':
			changeDiv('1');
			$selectStatus.val('1');
			$selectStatus.find("option[value='0']").hide();
			break;
		case 'Finalizado':
			changeDiv('2');
			$selectStatus.val('2');
			$selectStatus.find("option[value='2']").hide();
			$selectStatus.find("option[value='1']").hide();
			$selectStatus.prop('disabled', 'disabled');
			break;
		}
	}
	// validation
	$formModule.validate({
		errorPlacement : function(error, element) {
			$(element).parent().addClass("has-error");
			error.insertAfter(element);
			error.wrap("<p>");
			error.css('color', 'red');
		},
		rules : {
			selectStatus : {
				required : true
			},
			taObservation : {
				required : true,
				maxlength : 4000
			},
			taFeedback : {
				required : true,
				maxlength : 1000
			},
			taReply : {
				required : true,
				maxlength : 4000
			}
		},
		messages : {
			selectStatus : {
				required : "Insira o Nome"
			},
			taObservation : {
				required : "Insira as Observações",
				maxlength : jQuery.validator
						.format("Por  favor insira menos que {0} caracteres.")
			},
			taFeedback : {
				required : "Insira o Parecer",
				maxlength : jQuery.validator
						.format("Por favor insira menos que {0}  caracteres.")
			},
			taReply : {
				required : "Insira a Resposta ao Usuário",
				maxlength : jQuery.validator
						.format("Por favor insira menos que {0}  caracteres.")
			}
		}
	});
	
	function getUrlParam(key) {
		var result = new RegExp(key + "=([^&]*)", "i")
				.exec(window.location.search);
		return result && unescape(result[1]) || "";
	}
	
	function load(unsolvedCallId) {
		$.ajax({
			type : 'GET',
			url : '../rest/callfollow2/' + unsolvedCallId,
			dataType : "json",
			contentType : "application/json"
		}).done(function(resp) {
			setFields(resp);
		}).fail(function(jqXHR, textStatus) {
			var title = "Chamado Interno";
			var body = jqXHR.responseJSON.errorCode + ", "
					+ jqXHR.responseJSON.errorMessage;
			modal(title, body,3);
		});
	}
	function showMessages() {
		var message;
		$.ajax({
			type : 'GET',
			url : '../rest/messagemodel2/enabled',
			dataType : "json",
			contentType : "application/json",
		}).done(function(resp) {
			_render($messageListTpl, $notificacoesE, resp);
			$(".trMM").on('click', getMessage);
			$modalE.modal({
				show : true,
				backdrop : true
			});
		}).fail(function(jqXHR, textStatus) {
			var title = "Chamado Interno";
			var body = jqXHR.responseJSON.errorCode + ", "
					+ jqXHR.responseJSON.errorMessage;
			modal(title, body,3);
		});
	}
	function getMessage(event) {
		var message = $(this).find(".messageBody").text();
		$taReply.val(message);
		$modalE.modal('hide');
	}
	function init() {
		load(getUrlParam('id'));
	}
	init();
	module.showMessages = showMessages;
	module.changeDiv = changeDiv;
	module.setFields = setFields;
	module.getFields = getFields;
	module.modal = modal;
	return module;
})(jQuery, ctrlCFadd || {});