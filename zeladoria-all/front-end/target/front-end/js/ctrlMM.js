var ctrlMM = (function($, module) {
	// var module = {};
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
	var tamanho = $generalModule.length;
	// Search Module
	var $searchModule = $generalModule.find("#searchModule");
	// filters
	var $selectName = $searchModule.find("#selectName");
	var $selectSubject = $searchModule.find("#selectSubject");
	var $radioEnabled = $searchModule.find(".radio-button");
	// buttons
	var $addButton = $searchModule.find("#add");
	var $searchButton = $searchModule.find("#search");
	// Table module
	var $tableModule = $generalModule.find("#tabela");
	var $fieldTable = $generalModule.find("#fieldTable");
	var $notFoundDiv = $generalModule.find("#notFoundDiv");
	var $notFoundH = $generalModule.find("#notFoundH");
	var $table = $tableModule.find("#table");
	var $thead = $table.find(".thead");
	var $tbody = $table.find(".tbody");
	// modal
	var $modalE = $generalModule.find("#modalE");
	var $notificacoesE = $generalModule.find("#notificacoesE");
	// templates
	var $comboNameTpl = $("#combo-name-template").html();
	var $comboSubjectTpl = $("#combo-subject-template").html();
	var $edmodalTpl = $("#edmodal-template").html();
	var $edMessageTpl = $("#edmessage-template").html();
	function tableTpl(roleEdit, roleEnabled) {
		var string = "{{#each .}}" + "<tr>"
				+ "<td class=\"hideTD\">{{messageModelId}}</td>"
				+ "<td align='center'><div data-toggle='collapse' data-target= '#"+"a"+"{{messageModelId}}' class='accordion-toggle cursor'><i class='glyphicon glyphicon-chevron-down'></i></div></td>"
				+ "<td>{{name}}</td>	" 
				+ "<td>{{subject}}</td>	"
				+ "<td class=\"hideTD\">{{messageBody}}</td>	"
				+ "<td>{{substr messageBody 0 30}}</td>	"
				+ "<tdclass=\"hideTD\">{{enabled}}</td>"
				+ "<td align='right'> <div class='btn-group' role='group' aria-label='...'>	";
		if (roleEdit)
			string += "<a href=\"../addmm.jsp?id={{messageModelId}}\" type = 'button' target = '_self' data-toggle='tooltip' title='Editar' class='btn btn-xs btn-primary edit-button'><i class='glyphicon glyphicon-pencil'></i></a>";
		if (roleEnabled)
			string += "{{#ifCond enabled '===' 0}}"
					+ "<a type = 'button' id=\"{{messageModelId}}\" data-row-index='{{@index}}' data-link=\"{{getLink links 'ed'}}\" data-id=\"{{messageModelId}}\" data-name=\"{{name}}\" data-enabled=\"{{enabled}}\" data-toggle='tooltip' title='Inativar' class='btn btn-xs btn-danger ed-button enabled-button'><i class='glyphicon glyphicon-remove'></i></a>"
					+ "<a type = 'button' id=\"{{messageModelId}}\" data-row-index='{{@index}}' data-link=\"{{getLink links 'ed'}}\" data-id=\"{{messageModelId}}\" data-name=\"{{name}}\" data-enabled=\"{{enabled}}\" style=\"display: none;\" data-toggle='tooltip' title='Ativar' class='btn btn-xs btn-success ed-button disabled-button'><i class='glyphicon glyphicon-ok'></i></a>"
					+ "{{else}}"
					+ "<a type = 'button' id=\"{{messageModelId}}\" data-row-index='{{@index}}' data-link=\"{{getLink links 'ed'}}\" data-id=\"{{messageModelId}}\" data-name=\"{{name}}\" data-enabled=\"{{enabled}}\" style=\"display: none;\" data-toggle='tooltip' title='Inativar' class='btn btn-xs btn-danger ed-button enabled-button'><i class='glyphicon glyphicon-remove'></i></a>"
					+ "<a type = 'button' id=\"{{messageModelId}}\" data-row-index='{{@index}}' data-link=\"{{getLink links 'ed'}}\" data-id=\"{{messageModelId}}\" data-name=\"{{name}}\" data-enabled=\"{{enabled}}\" data-toggle='tooltip' title='Ativar' class='btn btn-xs btn-success ed-button disabled-button'><i class='glyphicon glyphicon-ok'></i></a>"
					+ "{{/ifCond}}";
		string += "</div></td></tr>" 
			+ "<tr> <td colspan='9' class='hiddenRow'><div id='"+"a"+"{{messageModelId}}' class='accordion-body collapse'><span > <strong>Corpo da Mensagem:</strong><br/> {{messageBody}} </span></div> </td> </tr>"
			+ "{{/each}}";
		return string;
	}
	// bind events
	$searchButton.on('click', {
		param : 1
	}, search);
	$tbody.on('click', '.ed-button', enableDisable);
	$notificacoesE.on('click', '#bSim', changeEnabled);
	// functions
	function modal(title, body) {
		_render($edMessageTpl, $notificacoesE, {
			'title' : title,
			'body' : body
		});
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
	function getChecked() {
		var checked;
		$radioEnabled.each(function(index, elem) {
			if ($(elem).is(':checked'))
				checked = $(elem).val();
		});
		return checked;
	}
	// search message model function
	function getFilters(page) {
		var name = $selectName.val() || null;
		var subject = $selectSubject.val() || null;
		var enabled = getChecked();
		return {
			'page' : page,
			'name' : name,
			'subject' : subject,
			'enabled' : enabled
		};
	}

	function setFilters(name, subject, enabled) {
		$selectName.val(name);
		$selectSubject.val(subject);
		if (enabled == 'enabled')
			$('#enabled').iCheck('check');
		if (enabled == 'disabled')
			$('#disabled').iCheck('check');
		if (enabled == 'ambos')
			$('#ambos').iCheck('check');
	}
	function search(event) {
		page = event.data.param || 1;
		searchMM(page);
	}
	function makeCombos() {
		$.ajax({
			type : 'GET',
			url : "../rest/messagemodel2/combos",
			dataType : "json",
			contentType : "application/json"
		}).done(function(resp, textStatus, jqXHR) {
			_render($comboNameTpl, $selectName, resp);
			_render($comboSubjectTpl, $selectSubject, resp);
		}).fail(function( jqXHR, textStatus, errorThrown) {
			var title = "Modelos de Mensagem";
			var body = jqXHR.responseJSON.errorCode + ", "
					+ jqXHR.responseJSON.errorMessage;
			modal(title, body);
		});
	}
	function searchMM(page,access) {
		$fieldTable.show();
		$notFoundDiv.hide();
		$
				.ajax({
					type : 'GET',
					url : "../rest/messagemodel2/search",
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
							_render(tableTpl(roleEdit, roleEnabled), $tbody,
									resp);
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
													// hide
													// everything,
													// then show
													// for the
													// new page
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
																		url : "../rest/messagemodel2/search",
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
																				tableTpl(
																						roleEdit,
																						roleEnabled),
																				$tbody,
																				resp);
																		var showFrom = perPage
																				* (pageNumber - 1);
																		var showTo = showFrom
																				+ perPage;
																		
																		if(pageNumber == 1){
																			$(".prev").hide();
																		} else {
																			$(".prev").show();
																		}
																		
																		if (count - ((pageNumber-1)
																				* perPage) <= perPage)
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
																			jqXHR, textStatus, errorThrown) {
																		modal(
																				"Modelos de Mensagem",
																				jqXHR.responseJSON.errorCode
																						+ ", "
																						+ jqXHR.responseJSON.errorMessage);
																	setFilters("","","ambos");
																	});
												}
											});
						}).fail(
						function(jqXHR, textStatus, errorThrown) {

							if(access===0){
								$fieldTable.hide();
								$notFoundH.text(jqXHR.responseJSON.errorMessage);
								$notFoundDiv.show();
								/*
								$thead.hide();
								_render("<div align='center'><h4>" + jqXHR.responseJSON.errorMessage + "</h4></div>",$tableModule,{});
				*/
							return;
							}else{			
							var title = "Modelos de Mensagem";
							var body = jqXHR.responseJSON.errorCode + ", "
									+ jqXHR.responseJSON.errorMessage;
							modal(title, body);
							setFilters("","","ambos");
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
			page : $("#pagination").pagination('getCurrentPage'),
			id : $buttonED.data("id"),
			name : $buttonED.data("name"),
			enabled : $buttonED.data("enabled"),
			link : $buttonED.data("link"),
			msg : ($buttonED.data("enabled") == 0) ? "inativar" : "ativar"
		}))
		// $('#modalE').modal('show');
		$modalE.modal({
			show : true,
			backdrop : true
		});
	}
	function changeEnabled(event) {
		event.preventDefault();
		var id = $(this).data("id");
		var name = $(this).data("name");
		var enabled = $(this).data("enabled");
		var link = $(this).data("link");
		$.ajax({
			type : 'PUT',
			url : link + '/' + $('#username').text(),
			dataType : "json",
			"Content-Type" : "application/json",
			beforeSend : function(xhr) {
				xhr.setRequestHeader(header, token);
			},
			contentType : "application/json"
		}).done(function(resp) {
			var pg = $("#pagination").pagination('getCurrentPage');
			modalED("Modelo de mensagem", resp.message);
			searchMM($("#pagination").pagination('getCurrentPage'));
		}).fail(
				function(jqXHR, textStatus, errorThrown) {
			var title = "Modelo de Mensagem";
			var body = jqXHR.responseJSON.errorCode + ", "
					+ jqXHR.responseJSON.errorMessage;
			modal(title, body);
		});
	}
	makeCombos();
	searchMM(1,0);
	module.setFilters = setFilters;
	module.getFilters = getFilters;
	module.searchMM = searchMM;
	module.modal = modal;
	module.edUpdate = edUpdate;
	return module;
})(jQuery, ctrlMM || {});