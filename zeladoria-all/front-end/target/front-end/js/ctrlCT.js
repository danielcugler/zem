var ctrlCT = (function($, module) {
	// var module = {};
	var newOnPage = true;
	var list = $('#list li').map(function() {
		return $(this).text();
	});
	// security rules
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	var roleEnabled = $.inArray('ROLE_CITIZEN_ENABLE', list) >= 0;
	// cached DOM
	// general module
	var $generalModule = $("#generalModule");
	var tamanho = $generalModule.length;
	// Search Module
	var $searchModule = $generalModule.find("#searchModule");
	// filters
	// var $serachFilters = $searchModule.find(".searchFilters");
	var $inName = $searchModule.find("#inName");
	var $inEmail = $searchModule.find("#inEmail");
	var $radioEnabled = $searchModule.find(".radio-button");
	// buttons
	// var $searchButtons = $searchModule.find("#searchButtons");
	var $addButton = $searchModule.find("#add");
	var $searchButton = $searchModule.find("#search");
	// Table module
	var $tableModule = $generalModule.find("#tabela");
	var $fieldTable = $generalModule.find("#fieldTable");
	var $notFoundDiv = $generalModule.find("#notFoundDiv");
	var $notFoundH = $generalModule.find("#notFoundH");
	var $table = $tableModule.find("#table");
	var $thead = $table.find("#cthead");
	var $tbody = $table.find("#ctbody");
	// modal
	var $modalE = $generalModule.find("#modalE");
	var $notificacoesE = $generalModule.find("#notificacoesE");
	// templates
	var $edmodalTpl = $("#edmodal-template").html();
	var $edMessageTpl = $("#edmessage-template").html();
	function tableTpl(roleEnabled) {
		var string = "{{#each .}}"
				+ "<tr>"
				+ "<td class=\"hideTD\">{{citizen_cpf}}</td>"
				+ "<td>{{name}}</td>	"
				+ "<td>{{email}}</td>	"
				+ "<td align='center'>{{phone_number}}</td>	"
				+ "<tdclass=\"hideTD\">{{enabled}}</td>"
				+ "<td align='right'> <div class='btn-group' role='group' aria-label='...'>	"
				+ "<a href='../detailedcitizen.jsp?cpf={{citizen_cpf}}' type='button'  data-toggle='tooltip' title='Detalhar' target = '_self' class='btn btn-xs btn-primary list-button'><i class='glyphicon glyphicon-list'></i></a>";

		if (roleEnabled)
			string += "{{#ifCond enabled '===' 0}}"
					+ "<a type = 'button' data-row-index='{{@index}}' data-link=\"{{getLink links 'ed'}}\" data-id=\"{{citizen_cpf}}\" data-name=\"{{name}}\" data-enabled=\"{{enabled}}\" data-toggle='tooltip' title='Inativar' class='btn btn-xs btn-danger ed-button enabled-button'><i class='glyphicon glyphicon-remove'></i></a>"
					+ "<a type = 'button' data-row-index='{{@index}}' data-link=\"{{getLink links 'ed'}}\" data-id=\"{{citizen_cpf}}\" data-name=\"{{name}}\" data-enabled=\"{{enabled}}\" style=\"display: none;\" data-toggle='tooltip' title='Ativar' class='btn btn-xs btn-success ed-button disabled-button'><i class='glyphicon glyphicon-ok'></i></a>"
					+ "{{else}}"
					+ "<a type = 'button' data-row-index='{{@index}}' data-link=\"{{getLink links 'ed'}}\" data-id=\"{{citizen_cpf}}\" data-name=\"{{name}}\" data-enabled=\"{{enabled}}\" style=\"display: none;\" data-toggle='tooltip' title='Inativar' class='btn btn-xs btn-danger ed-button enabled-button'><i class='glyphicon glyphicon-remove'></i></a>"
					+ "<a type = 'button'  data-row-index='{{@index}}' data-link=\"{{getLink links 'ed'}}\" data-id=\"{{citizen_cpf}}\" data-name=\"{{name}}\" data-enabled=\"{{enabled}}\" data-toggle='tooltip' title='Ativar' class='btn btn-xs btn-success ed-button disabled-button'><i class='glyphicon glyphicon-ok'></i></a>"
					+ "{{/ifCond}}";
		string += "</div></td></tr>" + "{{/each}}";
		return string;
	}
	// bind events
	$searchButton.on('click', {
		param : 1
	}, search);
	$tbody.on('click', '.ed-button', enableDisable);
	$notificacoesE.on('click', '#bSim', changeEnabled);

	// render
	function _render(baseTemplate, selector, data) {
		var template = Handlebars.compile(baseTemplate);
		var html = template(data);
		selector.html(html);
	}
	// functions
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
	function getChecked(radio) {
		var checked;
		radio.each(function(index, elem) {
			if ($(elem).is(':checked'))
				checked = $(elem).val();
		});
		return checked;
	}

	function getFilters(page) {
		var name = $inName.val() || null;
		var email = $inEmail.val() || null;
		var enabled = getChecked($radioEnabled);
		return {
			'page' : page,
			'name' : name,
			'email' : email,
			'enabled' : enabled
		};
	}

	function setFilters(name, email, enabled) {
		$inName.val(name);
		$inEmail.val(email);
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

	function searchMM(page, access) {
		$fieldTable.show();
		$notFoundDiv.hide();
		$
				.ajax({
					type : 'GET',
					url : "../rest/citizen2/search",
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
							_render(tableTpl(roleEnabled), $tbody, resp);
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
																		url : "../rest/citizen2/search",
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
																				tableTpl(roleEnabled),
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
																				"Cidadão",
																				jqXHR.responseJSON.errorCode
																						+ ", "
																						+ jqXHR.responseJSON.errorMessage);
																		setFilters("", "", "ambos");
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
								var title = "Cidadãos";
								var body = jqXHR.responseJSON.errorCode + ", "
										+ jqXHR.responseJSON.errorMessage;
								modal(title, body);
								setFilters("", "", "ambos");
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
		// var body = (enabled == "Enabled") ? "Modelo de mensagem " + name
		// + " inativado com sucesso!" : "Modelo de mensagem " + name
		// + " ativado com sucesso!";
		$.ajax({
			type : 'PUT',
			url : link + '/' + $('#username').text(),
			// data : {'username' : $('#username').text()},
			dataType : "json",
			"Content-Type" : "application/json",
			beforeSend : function(xhr) {
				xhr.setRequestHeader(header, token);
			},
			contentType : "application/json"
		}).done(function(resp) {
			// $('#modalE').modal('hide');
			var pg = $("#pagination").pagination('getCurrentPage');
			modalED("Cidadão", resp.message);
			searchMM($("#pagination").pagination('getCurrentPage'));
		}).fail(
				function(jqXHR, textStatus) {
					var title = "Cidadão";
					var body = jqXHR.responseJSON.errorCode + ", "
							+ jqXHR.responseJSON.errorMessage;
					modal(title, body);
				});
	}

	searchMM(1, 0);
	module.setFilters = setFilters;
	module.getFilters = getFilters;
	module.searchMM = searchMM;
	module.modal = modal;
	module.edUpdate = edUpdate;
	return module;
})(jQuery, ctrlCT || {});