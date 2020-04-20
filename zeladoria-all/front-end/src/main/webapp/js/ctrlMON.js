var moduleMON = (function($, module) {
	// var module = {};
	var newOnPage = true;
	var list = $('#list li').map(function() {
		return $(this).text();
	});
	// security rules
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	var roleEdit = $.inArray('ROLE_CALL_UPDATE', list) >= 0;
	var roleEnabled = $.inArray('ROLE_CALL_ENABLE', list) >= 0;
	var roleSend = $.inArray('ROLE_CALL_SEND', list) >= 0;
	var roleReply = $.inArray('ROLE_CALL_REPLY', list) >= 0;
	// cached DOM
	// general module
	var $generalModule = $("#generalModule");
	var tamanho = $generalModule.length;
	// Search Module
	var $searchModule = $generalModule.find("#search-call-form");
	// filters
	var $inFromDate = $searchModule.find("#fromDate");
	var $inToDate = $searchModule.find("#toDate");
	var $inUser = $searchModule.find("#inUser");
	var $inCpf = $searchModule.find("#inCpf");
	var $inId = $searchModule.find("#inId");
	var $selectCallSource = $searchModule.find("#selectCallSource");
	var $selectPriority = $searchModule.find("#selectPriority");
	var $selectCallClassification = $searchModule
			.find("#selectCallClassification");
	var $selectEntityCategory = $searchModule.find("#selectEntityCategory");
	var $radioEnabled = $searchModule.find(".radio-button");
	// buttons
	// var $searchButtons = $searchModule.find("#searchButtons");
	var $searchButton = $searchModule.find("#search");
	// Table module
	var $tableModule = $generalModule.find("#tabela");
	var $fieldTable = $generalModule.find("#fieldTable");
	var $notFoundDiv = $generalModule.find("#notFoundDiv");
	var $notFoundH = $generalModule.find("#notFoundH");
	var $table = $tableModule.find("#resultado");
	var $thead = $table.find("#cthead");
	var $tbody = $table.find("#ctbody");
	// modal
	var $modalE = $generalModule.find("#modalE");
	var $notificacoesE = $generalModule.find("#notificacoesE");
	var $modalMidia = $generalModule.find("#modaldemidia");
	// templates
	var $sendModalTpl = $("#sdmodal-template").html();
	var $comboCcTpl = $("#combo-cc-template").html();
	var $comboEcTpl = $("#combo-ec-template").html();
	var $edmodalTpl = $("#edmodal-template").html();
	var $edMessageTpl = $("#edmessage-template").html();
	function tableTpl() {
		var string = "{{#each .}}"
				+ "<tr>"
				+ "<td class=\"hideTD\">{{unsolvedCallId}}</td>"
				+ "<td align='center' class='toggleButton'><div data-toggle='collapse' data-target='#{{unsolvedCallId}}' data-parent='#accordion' class='accordion-toggle cursor'><i class='glyphicon glyphicon-chevron-down'></i></div></td>"
				+ "{{#ifUndefined parentCallId}}"
				+ "<td align='center'>{{unsolvedCallId}}</td>"
				+ "{{else}}"
				+ "<td align='center'>{{parentCallId.unsolvedCallId}}</td>"
				+ "{{/ifUndefined}}"
				+ "{{#ifCond callSource '===' 'Web'}}"
				+ "<td align='center'><i class='icon-monitor-1'></i></td>"
				+ "{{else}}"
				+ "<td align='center'><i class='icon-mobile-2'></i></td>"
				+ "{{/ifCond}}"
				+ "<td>{{entityEntityCategoryMaps.entity.name}}</td>	"
				+ "<td>{{callClassificationId.name}}</td>	"
				+ "<td>{{priority}}</td>"
				+ "<td>{{substr description.information 0 60}}</td>	"
				+ "{{#ifNull firstPhoto}}"
				+ "<td align='center'>&nbsp;&nbsp;&nbsp;<a data-id=\"{{parentCallId.unsolvedCallId}}\" type = 'button' target = '_self' class='view-media'><img src=\"{{firstPhoto}}\" height=\"26\" width=\"26\"></a></td>"
				+ "{{else}}"
				+ "<td align='center'>&nbsp;&nbsp;&nbsp;<a type = 'button' target = '_self'><img src=\"../images/noImage.png\" height=\"26\" width=\"26\"></a></td>"
				+ "{{/ifNull}}"
				+ "{{#ifCond callProgress '===' 'Processado'}}"
				+ "<td align='center'><span  class='btn btn-xs btn-blue-3 btn-label'>Processado</span></td>"
				+ "{{/ifCond}}"
				+ "{{#ifCond callProgress '===' 'Finalizado'}}"
				+ "<td align='center'><span class='btn btn-xs btn-green-2 btn-label'>Finalizado</span></td>"
				+ "{{/ifCond}}"
				+ "{{#ifCond callProgress '===' 'Rejeitado'}}"
				+ "<td align='center'><span class='btn btn-xs btn-lightblue-2 btn-label'>Rejeitado</span></td>"
				+ "{{/ifCond}}"
				+ "{{#ifCond callProgress '===' 'Encaminhado'}}"
				+ "<td align='center'><span class='btn btn-xs btn-orange-2 btn-label'>Encaminhado</span></td>"
				+ "{{/ifCond}}"
				+ "{{#ifCond callProgress '===' 'Novo'}}"
				+ "<td align='center'><span  class='btn btn-xs btn-danger btn-label'>Novo</span></td>"
				+ "{{/ifCond}}"
				+ "<td class=\"hideTD\">{{enabled}}</td>"
				+ "<td align='right'><div class='btn-group' role='group' aria-label='...'>"

				// Botões de ação do callProgress Encaminhado.
				+ "{{#ifCond callProgress '===' 'Encaminhado'}}"
				+ "<a type = 'button' target = '_self' data-toggle='tooltip' title='Editar' class='btn btn-xs btn-darkblue-1 edit-button disabled'><i class='glyphicon glyphicon-pencil'></i></a>"
				+ "{{#ifCond callStatus '===' 'Ativo'}}"
				+ "<a type = 'button' data-toggle='tooltip' title='Rejeitar' class='btn btn-xs btn-danger enabled-button disabled'><i class='glyphicon glyphicon-remove'></i></a>"
				+ "{{else}}"
				+ "<a type = 'button' data-toggle='tooltip' title='Ativar' class='btn btn-xs btn-success disabled-button disabled'><i class='glyphicon glyphicon-ok'></i></a>"
				+ "{{/ifCond}}"
				+ "<a data-id=\"{{unsolvedCallId}}\" data-idp=\"{{parentCallId.unsolvedCallId}}\" type='button' data-toggle='tooltip' title='Encaminhar' class='btn btn-xs btn-blue-3 send-button disabled'><i class='glyphicon glyphicon-circle-arrow-right'></i></a>"
				+ "<a type='button' data-toggle='tooltip' title='Responder' class='btn btn-xs btn-orange-2 reply-button disabled'><i class='glyphicon glyphicon-send'></i></a>"
				+ "{{/ifCond}}"

				// Botões de ação do callProgress Finalizado.
				+ "{{#ifCond callProgress '===' 'Finalizado'}}"
				+ "<a type = 'button' target = '_self' data-toggle='tooltip' title='Editar' class='btn btn-xs btn-darkblue-1 edit-button disabled'><i class='glyphicon glyphicon-pencil'></i></a>"
				+ "{{#ifCond callStatus '===' 'Ativo'}}"
				+ "<a type = 'button' data-toggle='tooltip' title='Rejeitar' class='btn btn-xs btn-danger enabled-button disabled'><i class='glyphicon glyphicon-remove'></i></a>"
				+ "{{else}}"
				+ "<a type = 'button' data-toggle='tooltip' title='Ativar' class='btn btn-xs btn-success disabled-button disabled'><i class='glyphicon glyphicon-ok'></i></a>"
				+ "{{/ifCond}}"
				+ "<a data-id=\"{{unsolvedCallId}}\" data-idp=\"{{parentCallId.unsolvedCallId}}\" type='button' data-toggle='tooltip' title='Encaminhar' class='btn btn-xs btn-blue-3 send-button disabled'><i class='glyphicon glyphicon-circle-arrow-right'></i></a>"
				+ "<a type='button' data-toggle='tooltip' title='Responder' class='btn btn-xs btn-orange-2 reply-button disabled'><i class='glyphicon glyphicon-send'></i></a>"
				+ "{{/ifCond}}";

		// Botões de ação do callProgress Novo.
		string += "{{#ifCond callProgress '===' 'Novo'}}";
		if (roleEdit)
			string += "<a href=\"../editCall.jsp?id={{unsolvedCallId}}\" type = 'button' target = '_self' data-toggle='tooltip' title='Editar' class='btn btn-xs btn-primary edit-button'><i class='glyphicon glyphicon-pencil'></i></a>";
		if (roleEnabled)
			string += "{{#ifCond callStatus '===' 'Ativo'}}"
					+ "<a data-idp=\"{{parentCallId.unsolvedCallId}}\" data-id=\"{{unsolvedCallId}}\" data-enabled=\"0\" type = 'button' data-toggle='tooltip' title='Rejeitar' class='btn btn-xs btn-danger ed-button'><i class='glyphicon glyphicon-remove'></i></a>"
					+ "{{else}}"
					+ "<a data-idp=\"{{parentCallId.unsolvedCallId}}\" data-id=\"{{unsolvedCallId}}\" data-enabled=\"1\" type = 'button' data-toggle='tooltip' title='Ativar' class='btn btn-xs btn-success ed-button'><i class='glyphicon glyphicon-ok'></i></a>"
					+ "{{/ifCond}}";
		if (roleSend)
			string += "{{#ifCond callStatus '===' 'Ativo'}}"
					+ "<a data-idp=\"{{parentCallId.unsolvedCallId}}\" data-id=\"{{unsolvedCallId}}\" type='button' data-toggle='tooltip' title='Encaminhar' class='btn btn-xs btn-blue-3 send-button'><i class='glyphicon glyphicon-circle-arrow-right'></i></a>"
					+ "{{else}}"
					+ "<a data-idp=\"{{parentCallId.unsolvedCallId}}\" data-id=\"{{unsolvedCallId}}\" type='button' data-toggle='tooltip' title='Encaminhar' class='btn btn-xs btn-blue-3 send-button disabled'><i class='glyphicon glyphicon-circle-arrow-right'></i></a>"
					+ "{{/ifCond}}";
		if (roleReply)
			string += "<a href=\"../replyCall.jsp?id={{unsolvedCallId}}\" type='button' data-toggle='tooltip' title='Responder' class='btn btn-xs btn-orange-2 reply-button'><i class='glyphicon glyphicon-send'></i></a>";
		string += "{{/ifCond}}"

		// Botões de ação do callProgress Processado.
		+ "{{#ifCond callProgress '===' 'Processado'}}";
		if (roleEdit)
			string += "<a href=\"../editCall.jsp?id={{unsolvedCallId}}\" type = 'button' target = '_self' data-toggle='tooltip' title='Editar' class='btn btn-xs btn-primary edit-button'><i class='glyphicon glyphicon-pencil'></i></a>";
		string += "{{#ifCond callStatus '===' 'Ativo'}}"
				+ "<a data-idp=\"{{parentCallId.unsolvedCallId}}\" data-id=\"{{unsolvedCallId}}\" data-enabled=\"0\" type = 'button' data-toggle='tooltip' title='Rejeitar' class='btn btn-xs btn-danger enabled-button ed-button'><i class='glyphicon glyphicon-remove'></i></a>";
		string += "{{else}}"
				+ "<a data-idp=\"{{parentCallId.unsolvedCallId}}\" data-id=\"{{unsolvedCallId}}\" data-enabled=\"1\" type = 'button' data-toggle='tooltip' title='Ativar' class='btn btn-xs btn-success disabled-button ed-button'><i class='glyphicon glyphicon-ok'></i></a>"
				+ "<a type='button' data toggle='tooltip' title='Encaminhar' class='btn btn-xs btn-blue-3 send-button disabled'><i class='glyphicon glyphicon-circle-arrow-right'></i></a>"
				+ "{{/ifCond}}";
		if (roleSend)
			string += "{{#ifCond callStatus '===' 'Ativo'}}"
					+ "<a data-idp=\"{{parentCallId.unsolvedCallId}}\" data-id=\"{{unsolvedCallId}}\" type='button' data-toggle='tooltip' title='Encaminhar' class='btn btn-xs btn-blue-3 send-button disabled'><i class='glyphicon glyphicon-circle-arrow-right'></i></a>"
					+ "{{else}}"
					+ "<a data-idp=\"{{parentCallId.unsolvedCallId}}\" data-id=\"{{unsolvedCallId}}\" type='button' data-toggle='tooltip' title='Encaminhar' class='btn btn-xs btn-blue-3 send-button disabled'><i class='glyphicon glyphicon-circle-arrow-right'></i></a>"
					+ "{{/ifCond}}";
		if (roleReply)
			string += "<a href=\"../replyCall.jsp?id={{unsolvedCallId}}\" type='button' data-toggle='tooltip' title='Responder' class='btn btn-xs btn-orange-2 reply-button'><i class='glyphicon glyphicon-send'></i></a>";
		string += "{{/ifCond}}"

		// Botões de ação do callProgress Rejeitado.
		+ "{{#ifCond callProgress '===' 'Rejeitado'}}";
		if (roleEdit)
			string += "<a href=\"../editCall.jsp?id={{unsolvedCallId}}\" type = 'button' target = '_self' data-toggle='tooltip' title='Editar' class='btn btn-xs btn-primary edit-button disabled'><i class='glyphicon glyphicon-pencil'></i></a>";
		string += "{{#ifCond callStatus '===' 'Ativo'}}"
				+ "<a data-idp=\"{{parentCallId.unsolvedCallId}}\" data-id=\"{{unsolvedCallId}}\" data-enabled=\"0\" type = 'button' data-toggle='tooltip' title='Rejeitar' class='btn btn-xs btn-danger enabled-button ed-button'><i class='glyphicon glyphicon-remove'></i></a>"
				+ "{{else}}"
				+ "<a data-idp=\"{{parentCallId.unsolvedCallId}}\" data-id=\"{{unsolvedCallId}}\" data-enabled=\"1\" type = 'button' data-toggle='tooltip' title='Ativar' class='btn btn-xs btn-success disabled-button ed-button'><i class='glyphicon glyphicon-ok'></i></a>"
				+ "<a type='button' data toggle='tooltip' title='Encaminhar' class='btn btn-xs btn-blue-3 send-button disabled'><i class='glyphicon glyphicon-circle-arrow-right'></i></a>"
				+ "{{/ifCond}}";
		if (roleReply)
			string += "<a href=\"../replyCall.jsp?id={{unsolvedCallId}}\" type='button' data-toggle='tooltip' title='Responder' class='btn btn-xs btn-orange-2 reply-button disabled'><i class='glyphicon glyphicon-send'></i></a>"
					+ "{{/ifCond}}"

					+ "<a href=\"../callsHistory.jsp?opt=0&id={{parentCallId.unsolvedCallId}}\" type='button' data-toggle='tooltip' title='Histórico' class='btn btn-xs btn-darkblue-1 history-button'><i class='icon-hourglass-1'></i></a>"
					+ "</div></td></tr>"
		//Expansão
					+"<tr> <td colspan='12' class='hiddenRow'><div id='{{unsolvedCallId}}' class='accordion-body collapse'><span> Descrição: {{description.information}} </span>" 
					+" <br/> <span>Usuário: {{updatedOrModeratedBy.systemUserUsername}}</span> <br/> <span>Data: {{creationOrUpdateDate}}</span>  " 
					+"{{#ifExist addressId}}"
					+"<br/><span>Endereço: {{addressId.streetName}}, {{addressId.addressNumber}} "
					+"{{#ifExist addressId.complement}} - {{addressId.complement}} {{/ifExist}} "
					+"- {{addressId.neighborhoodId.name}}" 
					+"{{/ifExist}}"
					+ "</div>" 
					+ " </td> </tr>"
					+ "{{/each}}";
		return string;
	}
	var teste = tableTpl();
	// bind events
	$searchButton.on('click', {
		param : 1
	}, search);
	$tbody.on('click', '.ed-button', enableDisable);
	$tbody.on('click', '.view-media', viewMedias);
	$tbody.on('click', '.send-button', sendCall);
	$tbody.on('click', '.toggleButton', toggleButton);	
	$notificacoesE.on('click', '#bSim', changeEnabled);
	$notificacoesE.on('click', '#sbSim', sendOk);	
	
	// render
	function _render(baseTemplate, selector, data) {
		var template = Handlebars.compile(baseTemplate);
		var html = template(data);
		selector.html(html);
	}
		
	function strToDate(dateStr) {
	    var parts = dateStr.split("/");
	    return new Date(parts[2], parts[1] - 1, parts[0]);
	}
	
	$.validator.addMethod("validateCurrentDate", function(value, element, params) {
		var currentDate = new Date();
		currentDate.setDate(currentDate.getDate());
		currentDate.setHours(23,59,0,0);
		var inDate = strToDate(value);
		var bool = inDate <= currentDate;
		return bool;
		}, jQuery.validator.format("Data inválida, maior que data atual"));
	
	// var $inCpf = $searchModule.find("#inCpf");
	// var $inId = $searchModule.find("#inId");

	$searchModule.validate({
		errorPlacement : function(error, element) {
			error.insertAfter(element);
			error.wrap("<p>");
			error.css('color', 'red');
		},
		rules : {
			inUser : {
				maxlength : 40
			},
			toDate :{
				validateCurrentDate : true
			},
			inCpf : {
				maxlength : 11
			},
			inId : {
				number : true
			}
		},
		messages : {
			inUser : {
				maxlength : "O campo deve possuir no máximo 40 caracteres"
			},
			inCpf : {
				maxlength : "O campo deve possuir no máximo 11 caracteres"
			},inId : {
				number : "O campo deve possuir apenas números"
			}
		}

	});
	// functions
	function sendCall(event) {
		event.preventDefault();
		_render($sendModalTpl, $notificacoesE, ({
			id : $(this).data("id"),
			idp : $(this).data("idp")
		}))
		$modalE.modal({
			show : true,
			backdrop : true
		});
	}

	function sendOk(event) {
		event.preventDefault();
		var id = $(this).data("id");
		$.ajax(
				{
					type : 'PUT',
					url : '../rest/unsolvedcall3/send/' + id,
					contentType : "application/json",
					dataType : "json",
					beforeSend : function(xhr) {
						xhr.setRequestHeader(header, token);
					}

				}).done(function(resp) {
			var pg = $("#pagination").pagination('getCurrentPage');
			modalED("Monitor de Chamados", resp.message);
			searchMM($("#pagination").pagination('getCurrentPage'));
		}).fail(function(erro) {
			alert(erro);
		});
	}

	function viewMedias(event) {
		event.preventDefault();
		$.ajax({
			type : 'GET',
			url : '../rest/unsolvedcall3/media/' + $(this).data("id"),
			dataType : "json",
			contentType : "application/json"
		}).done(function(resp, status, xhr) {
			for ( var key in resp)
				resp[key] = {
					"img" : resp[key]
				};
			var photo1 = viewMedia({
				'medias' : resp
			});
			var photo2 = mediaList({
				'medias' : resp
			});
			tempViewMedia(photo1, photo2);
			$modalMidia.modal('show');
		}).fail(function(jqXHR, textStatus) {
		});
	}

	function modal(title, body) {
		_render($edMessageTpl, $notificacoesE, {
			'title' : title,
			'body' : body
		});
		// $modalE.modal('show');
		$modalE.modal({
			show : true,
			backdrop : true
		});
	}
	function modalED(title, body) {
		_render($edMessageTpl, $notificacoesE, {
			'title' : title,
			'body' : body
		});
	}

	function getChecked(elem) {
		var checked;
		elem.each(function(index, elem) {
			if ($(elem).is(':checked'))
				checked = $(elem).val();
		});
		return checked;
	}
	// search message model function
	function getFilters(page) {
		return {
			'page' : page,
			'iniDate' : $inFromDate.val(),
			'endDate' : $inToDate.val(),
			'username' : $inUser.val(),
			'citizenCpf' : $inCpf.val(),
			'unsolvedCallId' : $inId.val(),
			'callSource' : $selectCallSource.val(),
			'entityCategoryId' : $selectEntityCategory.val(),
			'callClassificationId' : $selectCallClassification.val(),
			'priority' : $selectPriority.val(),
			'callStatus' : getChecked($radioEnabled)
		};
	}

	function setFilters(date1, date2, user, cpf, id, source, entityCategory,
			classification, priority, enabled) {
		$inFromDate.val(date1)
		$inToDate.val(date2)
		$inUser.val(user)
		$inCpf.val(cpf)
		$inId.val(id)
		$selectCallSource.val(source)
		$selectEntityCategory.val(entityCategory)
		$selectCallClassification.val(classification)
		$selectPriority.val(priority)

		if (enabled == 'enabled')
			$('#enabled').iCheck('check');
		if (enabled == 'disabled')
			$('#disabled').iCheck('check');
		if (enabled == 'ambos')
			$('#ambos').iCheck('check');
	}
	function search(event) {
		page = event.data.param || 1;
		if($searchModule.valid())
		searchMM(page);
		else
			modal("Moderação de chamados",
					"Por favor, preencha os campos destacados em vermelho!");
			
	}

	function makeCombos() {
		$.ajax({
			type : 'GET',
			url : "../rest/unsolvedcall3/combos",
			dataType : "json",
			contentType : "application/json"
		}).done(function(resp, status, xhr) {
			_render($comboCcTpl, $selectCallClassification, resp.cc);
			_render($comboEcTpl, $selectEntityCategory, resp.ec);
		}).fail(function(jqXHR, textStatus) {
		});
	}

	function searchMM(page, access) {
		$fieldTable.show();
		$notFoundDiv.hide();
		$
				.ajax({
					type : 'GET',
					url : "../rest/unsolvedcall3/search",
					data : getFilters(page),
					dataType : "json",
					contentType : "application/json"
				})
				.done(
						function(resp, status, xhr) {

							var count = parseInt(xhr
									.getResponseHeader('X-Total-Count'));
							var perPage = parseInt(xhr
									.getResponseHeader('X-Per-Page'));
							_render(tableTpl(), $tbody, resp);
							var trs = $tbody.find(".tr");
							;
							$("#pagination")
									.pagination(
											{
												items : count,
												itemsOnPage : perPage,
												cssStyle : "light-theme",
												currentPage : page,
												onInit : function() {
													var showFrom = perPage
															* (page - 1);
													var showTo = showFrom
															+ perPage;
													trs.hide() // first
													.slice(showFrom, showTo)
															.show();
													(count < perPage + 1) ? $(
															"#pagination")
															.hide() : $(
															"#pagination")
															.show();
													$(".prev").hide();
												},
												onPageClick : function(
														pageNumber) {
													$
															.ajax(
																	{
																		type : 'GET',
																		url : "../rest/unsolvedcall3/search",
																		data : getFilters(pageNumber),
																		dataType : "json",
																		contentType : "application/json"
																	})
															.done(
																	function(
																			resp,
																			status,
																			xhr) {
																		var count = parseInt(xhr
																				.getResponseHeader('X-Total-Count'));
																		var perPage = parseInt(xhr
																				.getResponseHeader('X-Per-Page'));
																		_render(
																				tableTpl(),
																				$tbody,
																				resp);
																		var showFrom = perPage
																				* (pageNumber - 1);
																		var showTo = showFrom
																				+ perPage;
																		if (pageNumber == 1) {
																			$(
																					".prev")
																					.hide();
																		} else {
																			$(
																					".prev")
																					.show();
																		}

																		if (count
																				- ((pageNumber - 1) * perPage) <= perPage)
																			$(
																					".next")
																					.hide();
																		else
																			$(
																					".next")
																					.show();
																		$(
																				"#pagination")
																				.show();
																	})
															.fail(
																	function(
																			jqXHR,
																			textStatus) {
																		modal(
																				"Moderação de Chamados",
																				jqXHR.responseJSON.errorCode
																						+ ", "
																						+ jqXHR.responseJSON.errorMessage);
																		setFilters(
																				"",
																				"",
																				"",
																				"",
																				"",
																				"",
																				"",
																				"",
																				"",
																				"ambos");
																	});
												}
											});
						}).fail(
						function(jqXHR, textStatus) {
							if (access === 0) {
								$fieldTable.hide();
								$notFoundH.text(jqXHR.responseJSON.errorMessage);
								$notFoundDiv.show();
								/*
								$thead.hide();
								_render("<div align='center'><h4>"
										+ jqXHR.responseJSON.errorMessage
										+ "</h4></div>", $tableModule, {});
								*/
								return;
							} else {
								var title = "Moderação de Chamados";
								var body = jqXHR.responseJSON.errorCode + ", "
										+ jqXHR.responseJSON.errorMessage;
								modal(title, body);
								setFilters("", "", "", "", "", "", "", "", "",
										"ambos");
							}

						});
	}
	function enableDisable(event) {
		event.preventDefault();
		var $buttonED = $(this);
		edUpdate(null, $buttonED);
	}

	function edUpdate(info, $buttonED) {
		_render($edmodalTpl, $notificacoesE, (info || {
			id : $buttonED.data("id"),
			idp : $buttonED.data("idp"),
			msg : ($buttonED.data("enabled") == 0) ? "rejeitar" : "ativar"
		}));		
		$modalE.modal({
			show : true,
			backdrop : true
		});		
	}
	function changeEnabled(event) {
		event.preventDefault();
		$.ajax(
				{
					type : 'PUT',
					url : '../rest/unsolvedcall3/ed/' + $(this).data("id"),
					dataType : "json",
					"Content-Type" : "application/json",
					beforeSend : function(xhr) {
						xhr.setRequestHeader(header, token);
					},
					contentType : "application/json"
				}).done(function(resp) {
			// $('#modalE').modal('hide');
			var pg = $("#pagination").pagination('getCurrentPage');
			modalED("Moderação de Chamados", resp.message);
			searchMM($("#pagination").pagination('getCurrentPage'));
		}).fail(function(erro) {
			alert(erro);
		});
	}

	function publishOk(event) {
		event.preventDefault();
		var id = $(this).data("id");
		var name = $(this).data("name");
		$.ajax(
				{
					type : 'PUT',
					url : '../rest/broadcastmessage2/publicar/' + id,
					contentType : "application/json",
					dataType : "json",
					beforeSend : function(xhr) {
						xhr.setRequestHeader(header, token);
					}

				}).done(function(resp) {
			// $('#modalE').modal('hide');
			var pg = $("#pagination").pagination('getCurrentPage');
			modal("Moderação de Chamados", resp.message);
			searchMM($("#pagination").pagination('getCurrentPage'));
		}).fail(function(erro) {
			alert(erro);
		});
	}
	
	function toggleButton(){
		var icon = $(this).find("i");
		icon.toggleClass("glyphicon-chevron-down glyphicon-chevron-up");
	}

	makeCombos();
	searchMM(1, 0);
	module.setFilters = setFilters;
	module.getFilters = getFilters;
	module.searchMM = searchMM;
	module.modal = modal;
	module.edUpdate = edUpdate;
	return module;
})(jQuery, moduleMON || {});