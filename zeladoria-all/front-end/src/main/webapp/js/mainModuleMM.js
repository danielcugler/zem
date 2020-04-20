app = {};

var MainModule = (function($, module) {

	// security rules
	var list = $('#list li').map(function() {
		return $(this).text();
	});

	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	var roleEdit = $.inArray('ROLE_MESSAGEMODEL_UPDATE', list) >= 0;
	var roleEnabled = $.inArray('ROLE_MESSAGEMODEL_ENABLE', list) >= 0;
				// cached DOM
		// general module
		var $generalModule = $("#generalModule");
		// Search Module
		var $searchModule = $generalModule.find("#searchModule");
		// buttons
		var $addButton = $searchModule.find("#add");
		var $searchButton = $searchModule.find("#search");
		// Table module
		var $tableModule = $generalModule.find(".tableModule");
		var $table = $tableModule.find("#table");
		var $thead = $table.find(".thead");
		var $tbody = $table.find(".tbody");
		// modal
		var $modalE = $generalModule.find("#modalE");
		var $notificacoesE = $generalModule.find("#notificacoesE");
		// filters
		var $comboName = $searchModule.find("#comboName");
		var $comboSubject = $searchModule.find("#comboSubject");
		var $radioEnabled = $searchModule.find(".radio-button");
		// templates
		var $edmodalTpl = $("#edmodal-template").html();
		var $edMessageTpl = $("#edmessage-template").html();
	var tableTpl = function (roleEdit, roleEnabled, content, edit, enabled) {
		if (roleEdit)
			content += edit;
		if (roleEnabled)
			content += enabled;
		content += "</div></td></tr>" + "{{/each}}";
		return content;
	};
	var $comboNameTpl = $("#combo-name-template").html();
	var $comboSubjectTpl = $("#combo-subject-template").html();
	var tableTpl = function (roleEdit, roleEnabled) {
		var string = "{{#each .}}" + "<tr>"
				+ "<td class=\"hideTD\">{{messageModelId}}</td>"
				+ "<td>{{name}}</td>	" + "<td>{{subject}}</td>	"
				+ "<td class=\"hideTD\">{{messageBody}}</td>	"
				+ "<td>{{substr messageBody 0 30}}</td>	"
				+ "<tdclass=\"hideTD\">{{enabled}}</td>"
				+ "<td> <div class='btn-group' role='group' aria-label='...'>	";
		if (roleEdit)
			string += "<a href=\"http://localhost:8080/addmessagemodel.jsp?id={{messageModelId}}\" type = 'button' target = '_self' data-toggle='tooltip' title='Editar' class='btn btn-xs btn-primary edit-button'><i class='glyphicon glyphicon-pencil'></i></a>";
		if (roleEnabled)
			string += "{{#ifCond enabled '===' 0}}"
					+ "<a type = 'button' data-row-index='{{@index}}' data-link=\"{{getLink links 'ed'}}\" data-id=\"{{messageModelId}}\" data-name=\"{{name}}\" data-enabled=\"{{enabled}}\" data-toggle='tooltip' title='Inativar' class='btn btn-xs btn-danger ed-button enabled-button'><i class='glyphicon glyphicon-remove'></i></a>"
					+ "<a type = 'button' data-row-index='{{@index}}' data-link=\"{{getLink links 'ed'}}\" data-id=\"{{messageModelId}}\" data-name=\"{{name}}\" data-enabled=\"{{enabled}}\" style=\"display: none;\" data-toggle='tooltip' title='Ativar' class='btn btn-xs btn-success ed-button disabled-button'><i class='glyphicon glyphicon-ok'></i></a>"
					+ "{{else}}"
					+ "<a type = 'button' data-row-index='{{@index}}' data-link=\"{{getLink links 'ed'}}\" data-id=\"{{messageModelId}}\" data-name=\"{{name}}\" data-enabled=\"{{enabled}}\" style=\"display: none;\" data-toggle='tooltip' title='Inativar' class='btn btn-xs btn-danger ed-button enabled-button'><i class='glyphicon glyphicon-remove'></i></a>"
					+ "<a type = 'button'  data-row-index='{{@index}}' data-link=\"{{getLink links 'ed'}}\" data-id=\"{{messageModelId}}\" data-name=\"{{name}}\" data-enabled=\"{{enabled}}\" data-toggle='tooltip' title='Ativar' class='btn btn-xs btn-success ed-button disabled-button'><i class='glyphicon glyphicon-ok'></i></a>"
					+ "{{/ifCond}}";
		string += "</div></td></tr>" + "{{/each}}";
		return string;
	};

	// bind events
	$searchButton.on('click', {
		param : 1
	}, search);
	mainModule.tbody.on('click', '.ed-button', enableDisable);
	mainModule.notificacoesE.on('click', '#bSim', changeEnabled);
	
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
		
	var moduleMM = (function($, moduleM) {
		var getChecked = function() {
			var checked;
			$radioEnabled.each(function(index, elem) {
				if ($(elem).is(':checked'))
					checked = $(elem).val();
			});
			return checked;
		}
		var makeCombos = function() {
			console.log("combos");
			$.ajax({
				type : 'GET',
				url : "http://localhost:8080/rest/messagemodel2/combos",
				dataType : "json",
				contentType : "application/json"
			}).done(function(resp, status, xhr) {
				console.log(status);
				mainModule._render($comboNameTpl, $comboName, resp);
				mainModule._render($comboSubjectTpl, $comboSubject, resp);
			}).fail(function(jqXHR, textStatus) {
				console.log(jqXHR.statusText);
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
			$('#modalE').modal('show');
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
				url : link,
				dataType : "json",
				"Content-Type" : "application/json",
				beforeSend : function(xhr) {
					xhr.setRequestHeader(header, token);
				},
				contentType : "application/json"
			}).done(function(resp) {
				// $('#modalE').modal('hide');
				var pg = $("#pagination").pagination('getCurrentPage');
				modal("Modelo de mensagem", resp.message);
				searchMM($("#pagination").pagination('getCurrentPage'));
			}).fail(function(erro) {
				alert(erro);
			});
		}

		function getFilters() {
			return;
		}
		function setFilters() {
			return;
		}
		function validate() {
			return;
		}

		makeCombos();

		function getFilters2(page) {
			var name = $comboName.find('option:selected').val() || 'z';
			var subject = $comboSubject.find('option:selected').val() || 'z';
			var enabled = getChecked();
			return {
				'page' : page,
				'name' : name,
				'subject' : subject,
				'enabled' : enabled
			};
		}
		function search(event) {
			page = event.data.param || 1;
			searchMM(page);
		}
		function search(data2, url) {
			$
					.ajax({
						type : 'GET',
						url : url,
						data : data2,
						dataType : "json",
						contentType : "application/json"
					})
					.done(
							function(resp, status, xhr) {
								var count = parseInt(xhr
										.getResponseHeader('X-Total-Count'));
								var perPage = parseInt(xhr
										.getResponseHeader('X-Per-Page'));
								mainModule._render(tableTpl(roleEdit, roleEnabled),
										mainModule.tbody, resp);
								var trs = $tbody.find(".tr");
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
																			url : url,
																			data : getFilters2(page),
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
																							+ jqXHR.responseJSON.errorMessage,
																					smodal);
																		});
													}
												});
							}).fail(
							function(jqXHR, textStatus) {
								var title = "Modelo de Mensagem";
								var body = jqXHR.responseJSON.errorCode + ", "
										+ jqXHR.responseJSON.errorMessage;
								modal(title, body, smodal);
							});

		}
		function searchMM(page) {
			// var url = getFilters(page);
			var url = "http://localhost:8080/rest/messagemodel2/search2";
			var data2 = getFilters2(page);
			search(data2, url);
		}
		searchMM(1);

		moduleM.makeCombos = makeCombos;
		moduleM.setFilters = setFilters;
		moduleM.getFilters = getFilters;
		moduleM.validate = validate;
		moduleM.token = token;
		moduleM.header = header;
		moduleM.roleEdit = roleEdit;
		moduleM.roleEnabled = roleEnabled;
		return moduleM;

	})(jQuery, moduleMM || {})

})(jQuery, MainModule || {});

