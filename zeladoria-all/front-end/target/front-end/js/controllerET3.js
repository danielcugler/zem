var MainModule = (function($) {
	var module = {};
	var newOnPage = true;
	var list = $('#list li').map(function() {
		return $(this).text();
	});
	// security rules
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	var roleEdit = $.inArray('ROLE_ENTITY_UPDATE', list) >= 0;
	var roleEnabled = $.inArray('ROLE_ENTITY_ENABLE', list) >= 0;
	// cached DOM
	// general module
	var $generalModule = $("#generalModule");
	var tamanho = $generalModule.length;
	// Search Module
	var $searchModule = $generalModule.find("#searchModule");
	// filters
	var $serachFilters = $searchModule.find(".searchFilters");
	var $comboName = $serachFilters.find("#comboName");
	var $radioEnabled = $serachFilters.find(".radio-button");
	// buttons
	var $searchButtons = $searchModule.find("#searchButtons");
	var $addButton = $searchModule.find("#add");
	var $searchButton = $searchModule.find("#search");
	// Table module
	var $tableModule = $generalModule.find(".tableModule");
	var $table = $tableModule.find("table");
	var $thead = $table.find(".thead");
	var $tbody = $table.find(".tbody");
	// modal
	var $modalE = $generalModule.find("#modalE");
	var $notificacoesE = $generalModule.find("#notificacoesE");
	// templates
	var $comboNameTpl = $("#combo-name-template").html();
	var $edmodalTpl = $("#edmodal-template").html();
	var $edMessageTpl = $("#edmessage-template").html();
	function tableTpl(roleEdit, roleEnabled) {
		var string = "{{#each .}}" + "<tr>"
				+ "<td class=\"hideTD\">{{entityId}}</td>"
				+ "<td>{{name}}</td>	" + "<td>{{attendanceTime.lowPriorityTime}}</td>	"
				+ "<td>{{attendanceTime.mediumPriorityTime}}</td>	"
				+ "<td>{{attendanceTime.highPriorityTime}}</td>	"
				+ "<tdclass=\"hideTD\">{{enabled}}</td>"
				+ "<td> <div class='btn-group' role='group' aria-label='...'>	";
		if (roleEdit)
			string += "<a href=\"http://localhost:8080/addEntity.jsp/?id={{entityId}}\" type = 'button' target = '_self' data-toggle='tooltip' title='Editar' class='btn btn-xs btn-primary edit-button'><i class='glyphicon glyphicon-pencil'></i></a>";
		if (roleEnabled)
			string += "{{#ifCond enabled '===' 0}}"
					+ "<a type = 'button' data-row-index='{{@index}}' data-link=\"{{getLink links 'ed'}}\" data-id=\"{{entityId}}\" data-name=\"{{name}}\" data-enabled=\"{{enabled}}\" data-toggle='tooltip' title='Inativar' class='btn btn-xs btn-danger ed-button enabled-button'><i class='glyphicon glyphicon-remove'></i></a>"
					+ "<a type = 'button' data-row-index='{{@index}}' data-link=\"{{getLink links 'ed'}}\" data-id=\"{{entityId}}\" data-name=\"{{name}}\" data-enabled=\"{{enabled}}\" style=\"display: none;\" data-toggle='tooltip' title='Ativar' class='btn btn-xs btn-success ed-button disabled-button'><i class='glyphicon glyphicon-ok'></i></a>"
					+ "{{else}}"
					+ "<a type = 'button' data-row-index='{{@index}}' data-link=\"{{getLink links 'ed'}}\" data-id=\"{{entityId}}\" data-name=\"{{name}}\" data-enabled=\"{{enabled}}\" style=\"display: none;\" data-toggle='tooltip' title='Inativar' class='btn btn-xs btn-danger ed-button enabled-button'><i class='glyphicon glyphicon-remove'></i></a>"
					+ "<a type = 'button'  data-row-index='{{@index}}' data-link=\"{{getLink links 'ed'}}\" data-id=\"{{entityId}}\" data-name=\"{{name}}\" data-enabled=\"{{enabled}}\" data-toggle='tooltip' title='Ativar' class='btn btn-xs btn-success ed-button disabled-button'><i class='glyphicon glyphicon-ok'></i></a>"
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
		$modalE.modal('show');
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
		var name = $comboName.find('option:selected').val() || 'z';
		var enabled = getChecked();
		return "http://localhost:8080/rest/entity2/search/" + page + "/"
				+ name + "/" + enabled;
	}
	function getFilters2(page) {
		var name = $comboName.find('option:selected').val() || 'z';
		var enabled = getChecked();
		return {'page':page,'name':name,'enabled':enabled};
	}
	
	function setFilters(name, enabled) {
		var name = $comboName.val(name);
		var enabled = $($radioEnabled[1]).iCheck('check');
	}
	function search(event) {
		page = event.data.param || 1;
		searchMM(page);
	}
	function makeCombos() {
		$.ajax({
			type : 'GET',
			url : "http://localhost:8080/rest/entity2/combos",
			dataType : "json",
			contentType : "application/json"
		}).done(function(resp, status, xhr) {
			_render($comboNameTpl, $comboName, resp);
		}).fail(function(jqXHR, textStatus) {
		});
	}
	function searchMM(page) {
	//	var url = getFilters(page);
		var url="http://localhost:8080/rest/entity2/search2";
	var data2= getFilters2(page);
		$
				.ajax({
					type : 'GET',
					url : url,
					data: data2,
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
													var url = getFilters(pageNumber);
													$
															.ajax(
																	{
																		type : 'GET',
																		url : url,
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
																		if (pageNumber
																				* perPage > count
																				- perPage)
																			$(
																					".next")
																					.hide();
																		else
																			$(
																					".next")
																					.show();
																		$(
																				".prev")
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
																				"Modelo de Mensagem",
																				jqXHR.responseJSON.errorCode
																						+ ", "
																						+ jqXHR.responseJSON.errorMessage);
																	});
												}
											});
						}).fail(
						function(jqXHR, textStatus) {
							var title = "Modelo de Mensagem";
							var body = jqXHR.responseJSON.errorCode + ", "
									+ jqXHR.responseJSON.errorMessage;
							modal(title, body);
						});
	}
	function enableDisable(event) {
		event.preventDefault();
		var $buttonED = $(this);
		edUpdate(null,$buttonED);
	}

	function edUpdate(info,$buttonED) {
		_render($edmodalTpl, $notificacoesE, (info || {
			page : $("#pagination").pagination('getCurrentPage'),
			id : $buttonED.data("id"),
			name : $buttonED.data("name"),
			enabled : $buttonED.data("enabled"),
			link : $buttonED.data("link"),
			msg:(enabled == "Enabled") ? "inativar" : "ativar"
		}))
		$('#modalE').modal('show');
	}
	function changeEnabled(event) {
		event.preventDefault();
		var id = $(this).data("id");
		var name = $(this).data("name");
		var enabled = $(this).data("enabled");
		var link = $(this).data("link");
		var body = (enabled == "Enabled") ? "Modelo de mensagem " + name
				+ " inativado com sucesso!" : "Modelo de mensagem " + name
				+ " ativado com sucesso!";
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
			// $('#modalE').modal('hide');
			var pg = $("#pagination").pagination('getCurrentPage');
			modal("Modelo de mensagem", body);
			searchMM($("#pagination").pagination('getCurrentPage'));
		}).fail(function(erro) {
			alert(erro);
		});
	}
	makeCombos();
	searchMM(1);
	module.setFilters = setFilters;
	module.getFilters = getFilters;
	module.searchMM = searchMM;
	module.modal = modal;
	module.edUpdate=edUpdate;
	return module;
})(jQuery);