var addBM = (function($, module) {
	var list = $('#list li').map(function() {
		return $(this).text();
	});
	// security rules
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	var roleEdit = $.inArray('ROLE_COMMUNICATION_UPDATE', list) >= 0;
	var roleEnabled = $.inArray('ROLE_COMMUNICATION_ENABLE', list) >= 0;
	// cached DOM
	// general module
	var $generalModule = $("#generalModule");
	// title
	var $title = $generalModule.find("#title2");
	// Field Module
	var $formModule = $generalModule.find("#IForm");
	var $inId = $formModule.find("#inId");
	var $inEnabled = $formModule.find("#inEnabled");
	var $inSubject = $formModule.find("#inSubject");
	var $inMessage = $formModule.find("#inMessageBody");
	var $selectBMC = $formModule.find("#selectBroadcastMessageCategory");
	var $selectExp = $formModule.find("#selectExpirationDate");
	var $spanCount = $formModule.find("#textarea_feedback");
	var $expirationDays = $formModule.find('#days');
	// buttons
	var $buttonModule = $formModule.find("#botoes");
	var $btAdd = $buttonModule.find("#btAdd");
	var $btEdit = $buttonModule.find("#btEdit");
	var $btSaveSendAdd = $buttonModule.find("#btSaveSendAdd");
	var $btSaveSendEdit = $buttonModule.find("#btSaveSendEdit");
	
	var datePublication = null;
	
	// bind actions
	$btAdd.on('click', add);
	$btEdit.on('click', edit);
	$btSaveSendAdd.on('click', saveSendAdd);
	$btSaveSendEdit.on('click', saveSendEdit);
	$inMessage.keyup(function() {
		charCount(4000);
	});
	// modal
	var $modalE = $generalModule.find("#modalE");
	var $notificacoesE = $generalModule.find("#notificacoesE");
	// templates
	var $messageTpl = $("#message-template").html();
	var $bcComboTpl = $("#bmc-combo-template").html();
	// functions

	function getDays(fromDate) {
		from = moment(fromDate, 'DD/MM/YYYY'); // format in which you have the
												// date
		to = moment(new Date()); // format in which you have the date
		return duration = moment.duration(to.diff(from)).days(); // you may
																	// use
																	// duration
	}
	function makeCombos() {
		$.ajax({
			type : 'GET',
			url : "../rest/broadcastmessagecategory/combos",
			dataType : "json",
			contentType : "application/json"
		}).done(function(resp, status, xhr) {
			_render($bcComboTpl, $selectBMC, resp);
		}).fail(function(jqXHR, textStatus) {
		});
	}
	var changeButton = function() {
		$btAdd.hide();
		$btSaveSendAdd.hide();
		$btEdit.show();
		$btSaveSendEdit.show();
	}
	var setFields = function(id, subject, message, enabled,
			broadcastMessageCategoryId, expirationDate, publicationDate) {
		$inId.val(id);
		$inEnabled.val(enabled);
		$inSubject.val(subject);
		$inMessage.val(message);
		$selectBMC.val(broadcastMessageCategoryId);
		$expirationDays.text(expirationDate);
		$expirationDays.show();
		$selectExp
				.append("<option value='-1'> Manter validade atual </option>");
		$selectExp.val('-1');
		if (publicationDate != null)
			$btSaveSendEdit.addClass("disabled");
	}
	
	var getFields = function() {
		return JSON.stringify({
			'broadcastMessageId': $inId.val(),
			'enabled': $inEnabled.val(),
			'subject': $inSubject.val(),
			'messageBody': $inMessage.val(),
			'broadcastMessageCategoryId': {
				'broadcastMessageCategoryId': $selectBMC.val()
			},
			'expirationDateVal': parseInt($selectExp.val()),
			'daysExpiration': $selectExp.val()
		});
	}
	
	function edit(event) {
		event.preventDefault();
		// validaISalvar();
		if ($formModule.valid()) {
			$.ajax(
					{
						type : 'PUT',
						url : '../rest/broadcastmessage2/upbm/',
						dataType : "json",
						data : getFields(),
						beforeSend : function(xhr) {
							xhr.setRequestHeader(header, token);
						},
						contentType : "application/json"
					}).done(
					function(data) {
						modal("Comunicados em Massa",
								data.message,
								1);
					}).fail(restError);

			return true;
		}
		modal("Comunicados em Massa",
				"Por favor, preencha os campos destacados em vermelho!", 3);
		return false;
	}
	
	function charCount(text_max) {
		var text_length = $inMessage.val().length;
		var text_remaining = text_max - text_length;
		$spanCount.text(text_remaining + ' caracteres restantes');
	}
	
	function add(event) {
		event.preventDefault();
		if ($formModule.valid()) {
			$
					.ajax(
							{
								type : 'POST',
								url : '../rest/broadcastmessage2/svbm/',
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
										"Comunicados em Massa",
										data.message,
										1);
							}).fail(restError);
			return true;
		}
		modal("Comunicados em Massa",
				"Por favor, preencha os campos destacados em vermelho!", 3);
		return false;
	}

	function saveSendAdd(event) {
		event.preventDefault();
		if ($formModule.valid()) {
			$
					.ajax(
							{
								type : 'POST',
								url : '../rest/broadcastmessage2/sp/',
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
										"Comunicados em Massa",
										data.message,
										1);
							}).fail(restError);
			return true;
		}
		modal("Comunicados em Massa",
				"Por favor, preencha os campos destacados em vermelho!", 3);
		return false;
	}

	function saveSendEdit(event) {
		event.preventDefault();
		if ($formModule.valid()) {
			$
					.ajax(
							{
								type : 'PUT',
								url : '../rest/broadcastmessage2/mp/',
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
										"Comunicados em Massa",
										data.message,
										1);
							}).fail(restError);
			return true;
		}
		modal("Comunicados em Massa",
				"Por favor, preencha os campos destacados em vermelho!", 3);
		return false;
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
			CSubject : {
				required : true,
				maxlength : 100
			},
			CMessage : {
				required : true,
				maxlength : 4000
			},
			selectBroadcastMessageCategory : {
				required : true
			}
		},
		messages : {
			CSubject : {
				required : "Insira o Assunto",
				maxlength : jQuery.validator
						.format("Por  favor insira menos que {0} caracteres.")
			},
			CMessage : {
				required : "Insira o corpo da mensagem",
				maxlength : jQuery.validator
						.format("Por favor insira menos que {0}  caracteres.")
			},
			selectBroadcastMessageCategory : {
				required : "Por favor selecione uma Categoria."
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

	function loadMessage(messageModelId) {
		var id = messageModelId || getUrlParam('id');
		$
				.ajax({
					type : 'GET',
					url : '../rest/broadcastmessage2/' + id,
					dataType : "json",
					contentType : "application/json"
				})
				.done(
						function(data) {
							var enabled = (data.enabled === "Enabled") ? 0 : 1;
							setFields(
									data.broadcastMessageId,
									data.subject,
									data.messageBody,
									data.enabled,
									data.broadcastMessageCategoryId.broadcastMessageCategoryId,
									data.expirationDate, data.publicationDate);
							charCount(4000);
							if(data.publicationDate != null){
								datePublication = data.publicationDate;
								$expirationDays.val(data.expirationDate);
							} else 
								formatDate(new Date(), data.daysExpiration);
						}).fail(restError);
	}

	function restError(jqXHR, textStatus) {
		var body = jqXHR.responseJSON.errorCode + ", "
				+ jqXHR.responseJSON.errorMessage;
		modal("Comunicados em Massa", body, 2);
	}

	function loadMessageTeste() {
		loadMessage(123);
	}
	
	function formatDate(dt, expirationDays){
		dt.setDate(dt.getDate() + parseInt(expirationDays));
		var day = dt.getDate().toString();
		if(day.length == 1)
			day = "0" + day;			
		var month = (dt.getMonth() + 1).toString();
		if(month.length == 1)
			month = "0" + month;		
		var year = dt.getFullYear();
		var hours = dt.getHours().toString();
		if(hours.length == 1)
			hours = "0" + hours;
		var minutes = dt.getMinutes().toString();
		if(minutes.length == 1)
			minutes = "0" + minutes;
		var dateFormated = day + "/" + month + "/" + year + " - " + hours + ":" + minutes;
		//console.log(dateFormated);
		$expirationDays.val(dateFormated);
	}
	
	$selectExp.change(function(){
		if(datePublication != null){
			var dateParts = datePublication.split(" ");
			var datePartsDay = dateParts[0].split("/");
			var datePartsHour = dateParts[1].split(":");
//			console.log(dateParts);		
//			console.log(datePartsDay);	
//			console.log(datePartsHour);	
			formatDate(new Date(datePartsDay[2], datePartsDay[1] - 1, datePartsDay[0], datePartsHour[0], datePartsHour[1]), $selectExp.val());
		} else
			formatDate(new Date(), $selectExp.val());	
	});

	function init() {
		if (getUrlParam('id')) {
			document.title = "Edição de Comunicado em Massa";
			$title.text("").append(
					"Edição de <strong>Comunicado em Massa</strong>");
			changeButton();
			loadMessage();
		} else {
			charCount(4000);
			formatDate(new Date(), $selectExp.val());
		}
			
	}
	makeCombos();
	init();
	module.changeButton = changeButton;
	module.setFields = setFields;
	module.getFields = getFields;
	module.modal = modal;
	module.loadMessage = loadMessage;
	return module;
})(jQuery, addBM || {});
