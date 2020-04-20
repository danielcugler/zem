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
	// cached DOM
	// general module
	var $generalModule = $("#generalModule");
	var tamanho = $generalModule.length;
	// Search Module
	var $searchModule = $generalModule.find("#search-call-form");
	// filters
	var $inFromDate = $searchModule.find("#fromDate");
	var $inToDate = $searchModule.find("#toDate");
	var $inCpf = $searchModule.find("#inCpf");
	var $inId = $searchModule.find("#inId");
	var $selectCallSource = $searchModule.find("#selectCallSource");
	var $selectPriority = $searchModule.find("#selectPriority");
	var $selectCallClassification = $searchModule
			.find("#selectCallClassification");
	var $divEntity = $searchModule.find("#divEntity");
	var $selectEntity = $searchModule.find("#selectEntity");
	var $radioStatus = $searchModule.find(".radio-button");
	var $userEntity = $("#userEntity");
	// buttons
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
	var $comboCcTpl = $("#combo-cc-template").html();
	var $comboEnTpl = $("#combo-en-template").html();
	var $edmodalTpl = $("#edmodal-template").html();
	var $edMessageTpl = $("#edmessage-template").html();
	function tableTpl() {
		var string = "{{#each .}}"
				+ "{{#ifCond delay '===' 'Azul'}}"
				+ "<tr class = 'table-blue'>"
				+ "{{/ifCond}}"
				+ "{{#ifCond delay '===' 'Verde'}}"
				+ "<tr class = 'table-green'>"
				+ "{{/ifCond}}"
				+ "{{#ifCond delay '===' 'Laranja'}}"
				+ "<tr class = 'table-orange'>"
				+ "{{/ifCond}}"
				+ "{{#ifCond delay '===' 'Vermelho'}}"
				+ "<tr class = 'table-red'>"
				+ "{{/ifCond}}"
				+ "{{#ifCond delay '===' 'Branco'}}"
				+ "<tr>"
				+ "{{/ifCond}}"
				+ "<td class=\"hideTD\">{{unsolvedCallId}}</td>"
				+ "<td align='center' class='toggleButton'><div data-toggle='collapse' data-target='#{{unsolvedCallId}}' class='accordion-toggle cursor'><i class='glyphicon glyphicon-chevron-down'></i></div></td>"
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
				+ "<td>{{callClassificationId.name}}</td>"
				+ "<td>{{parentCallId.creationOrUpdateDate}}</td>"
				+ "<td>{{expirationDate}}</td>"
				+ "<td>{{priority}}</td>"
				+ "<td>{{description.information}}</td>"
				+ "{{#ifNull firstPhoto}}"
				+ "<td align='center'><a data-id=\"{{parentCallId.unsolvedCallId}}\" type = 'button' target = '_self' class='view-media'><img src=\"{{firstPhoto}}\" height=\"25\" width=\"25\"></a></td>"
				+ "{{else}}"
				+ "<td align='center'><a type = 'button' target = '_self'><img src=\"../images/noImage.png\" height=\"25\" width=\"25\"></a></td>"
				+ "{{/ifNull}}"
				+ "<td class=\"hideTD\">{{enabled}}</td>	"
				+ "<td align='right'><div class='btn-group' role='group' aria-label='...'>";
		if (roleEdit)
			string += "<a href=\"../updateCall.jsp?id={{unsolvedCallId}}\" type = 'button' target = '_self' data-toggle='tooltip' title='Editar' class='btn btn-xs btn-primary edit-button'><i class='glyphicon glyphicon-pencil'></i></a>";
		string += "<a href=\"../callsHistory.jsp?opt=1&id={{parentCallId.unsolvedCallId}}\" type='button' data-toggle='tooltip' title='Histórico' class='btn btn-xs btn-darkblue-1 history-button'><i class='icon-hourglass-1'></i></a>"
				+ "</div></td></tr>"
	//Expansão
				+ "<tr> <td colspan='9' class='hiddenRow'><div id='{{unsolvedCallId}}' class='accordion-body collapse'><span> Observação: {{observation.information}} </span>" 
				+ " <br/> <span>Usuário: {{updatedOrModeratedBy.systemUserUsername}}</span> " 
				+ "{{#ifExist addressId}}"
				+"<br/><span>Endereço: {{addressId.streetName}}, {{addressId.addressNumber}} "
				+"{{#ifExist addressId.complement}} - {{addressId.complement}} {{/ifExist}} "
				+"- {{addressId.neighborhoodId.name}}"
				+"{{/ifExist}}"
				+ " </div> </td> </tr>"
				+ "{{/each}}";
		return string;
	}
	var teste = tableTpl();
	// bind events
	$searchButton.on('click', {
		param : 1
	}, search);
	$tbody.on('click', '.view-media', viewMedias);
	$tbody.on('click', '.toggleButton', toggleButton);
	// render
	function _render(baseTemplate, selector, data) {
		var template = Handlebars.compile(baseTemplate);
		var html = template(data);
		selector.html(html);
	}
	// functions
	$searchModule.validate({
		errorPlacement : function(error, element) {
			error.insertAfter(element);
			error.wrap("<p>");
			error.css('color', 'red');
		},
		rules : {
			inCpf : {
				maxlength : 11
			},
			inId : {
				number : true
			}
		},
		messages : {

			inCpf : {
				maxlength : "O campo deve possuir no máximo 11 caracteres"
			},
			inId : {
				number : "O campo deve possuir apenas números"
			}
		}

	});
	function viewMedias(event) {
		event.preventDefault();
		$.ajax({
			type : 'GET',
			url : '../rest/callfollow2/media/' + $(this).data("id"),
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
			'citizenCpf' : $inCpf.val(),
			'unsolvedCallId' : $inId.val(),
			'callSource' : $selectCallSource.val(),
			'callClassificationId' : $selectCallClassification.val(),
			'priority' : $selectPriority.val(),
			'callDelay' : getChecked($radioStatus),
			'entityId' : $userEntity.text() || $selectEntity.val()
		};
	}

	function setFilters(date1, date2, cpf, id, source, classification,
			priority, delay) {
		$inFromDate.val(date1);
		$inToDate.val(date2);
		$inCpf.val(cpf);
		$inId.val(id);
		$selectCallSource.val(source);
		$selectCallClassification.val(classification);
		$selectPriority.val(priority);

		if (delay == 'atrasado')
			$('#atrasado').iCheck('check');
		if (delay == 'emdia')
			$('#emdia').iCheck('check');
		if (delay == 'ambos')
			$('#ambos').iCheck('check');
	}


	function makeCombos() {
		$.ajax({
			type : 'GET',
			url : "../rest/callfollow2/combos",
			dataType : "json",
			contentType : "application/json"
		}).done(function(resp, status, xhr) {
			_render($comboCcTpl, $selectCallClassification, resp);
		}).fail(function(jqXHR, textStatus) {
		});
	}

	function search(event) {
		page = event.data.param || 1;
		if ($searchModule.valid())
			searchMM(page);
		else
			modal("Moderação de chamados",
					"Por favor, preencha os campos destacados em vermelho!");
	}
	
	function makeEntityCombo(){
		$.ajax({
			type : 'GET',
			url : "../rest/entity2/combos",
			dataType : "json",
			contentType : "application/json"
		}).done(function(resp, status, xhr) {
			_render($comboEnTpl, $selectEntity, resp);
		}).fail(function(jqXHR, textStatus) {
		});
	}
	
	function firstSearch(){
		$.ajax({
			type : 'GET',
			url : "../rest/systemuser2",
			dataType : "json",
			contentType : "application/json"
		}).done(function(resp, status, xhr) {
			if(resp.worksAtEntity != null)
				$("#userEntity").text(resp.worksAtEntity.entityId);
			else{
			$divEntity.show();
				makeEntityCombo();
			}
			searchMM(1, 0);
		}).fail(function(jqXHR, textStatus) {
		});
		}
	
	function searchMM(page, access) {
		$fieldTable.show();
		$notFoundDiv.hide();
		$
				.ajax({
					type : 'GET',
					url : "../rest/callfollow2/search2",
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
																		url : "../rest/callfollow2/search2",
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
																				"Acompanhamento de Chamados",
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
																				"ambos");
																	});
												}
											});
						})
				.fail(
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
								var title = "Acompanhamento de Chamdos";
								var body = jqXHR.responseJSON.errorCode + ", "
										+ jqXHR.responseJSON.errorMessage;
								modal(title, body);
								setFilters("", "", "", "", "", "", "", "ambos");
							}
						});
	}
	
	function toggleButton(){
		var icon = $(this).find("i");
		icon.toggleClass("glyphicon-chevron-down glyphicon-chevron-up");
	}

	makeCombos();
	firstSearch();
	module.setFilters = setFilters;
	module.getFilters = getFilters;
	module.searchMM = searchMM;
	module.modal = modal;
	return module;
})(jQuery, moduleMON || {});