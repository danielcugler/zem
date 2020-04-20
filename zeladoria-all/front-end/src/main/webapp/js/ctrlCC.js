var ctrlMM = (function($, module) {
	// var module = {};
	var newOnPage = true;
	var list = $('#list li').map(function() {
		return $(this).text();
	});
	// security rules
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	var roleEdit = $.inArray('ROLE_CALLCLASSIFICATION_UPDATE', list) >= 0;
	// cached DOM
	// general module
	var $generalModule = $("#generalModule");
	var tamanho = $generalModule.length;
	// Search Module
	var $searchModule = $generalModule.find("#searchModule");
	// filters
	var $selectName = $searchModule.find("#selectName");
	var $selectAddressRequired = $searchModule.find("#selectAddressRequired");
	var $radioEnabled = $searchModule.find(".radio-button");
	// buttons
	var $addButton = $searchModule.find("#add");
	var $searchButton = $searchModule.find("#search");
	// Table module
	var $tableModule = $generalModule.find("#tabela");
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
	function tableTpl(roleEdit) {
		var string = "{{#each .}}" + "<tr>"
				+ "<td class=\"hideTD\">{{callClassificationId}}</td>"
				+ "<td>{{name}}</td>	" 
				+"{{#ifCond addressRequired '===' false}}"
				+ "<td>Não</td>	"
				+ "{{else}}"
				+ "<td>Sim</td>	"
				+ "{{/ifCond}}";				
		if (roleEdit){
			string += "<td align='right'> <div class='btn-group' role='group' aria-label='...'>	";
			string += "<a href=\"../addcc.jsp?id={{callClassificationId}}\" type = 'button' target = '_self' data-toggle='tooltip' title='Editar' class='btn btn-xs btn-primary edit-button'><i class='glyphicon glyphicon-pencil'></i></a>";
		string += "</td>";
		}
		string += "</tr>" 
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
//		var addressRequired = $selectAddressRequired.val() || null;
	//	var enabled = getChecked();
		return {
			'page' : page,
			'name' : name
	//		,'addressRequired' : addressRequired,
	//		'enabled' : enabled
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
			url : "../rest/callclassification2/combos",
			dataType : "json",
			contentType : "application/json"
		}).done(function(resp, textStatus, jqXHR) {
			_render($comboNameTpl, $selectName, resp);
		}).fail(function( jqXHR, textStatus, errorThrown) {
			var title = "Classificação de chamados";
			var body = jqXHR.responseJSON.errorCode + ", "
					+ jqXHR.responseJSON.errorMessage;
			modal(title, body);
		});
	}
	function searchMM(page,access) {
		$
				.ajax({
					type : 'GET',
					url : "../rest/callclassification2/search",
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
							_render(tableTpl(roleEdit), $tbody,
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
																						roleEdit
																				),
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
								$thead.hide();
								_render("<div align='center'><h4>" + jqXHR.responseJSON.errorMessage + "</h4></div>",$tableModule,{});
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
			url : link,
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