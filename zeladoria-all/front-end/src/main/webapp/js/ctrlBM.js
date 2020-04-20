var ctrlBM = (function($, module) {
	// var module = {};
	var newOnPage = true;
	var list = $('#list li').map(function() {
		return $(this).text();
	});
	// security rules
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	var roleEdit = $.inArray('ROLE_COMMUNICATION_UPDATE', list) >= 0;
	var roleEnabled = $.inArray('ROLE_COMMUNICATION_ENABLE', list) >= 0;
	var rolePublish = $.inArray('ROLE_COMMUNICATION_PUBLISH', list) >= 0;
	// cached DOM
	// general module
	var $generalModule = $("#generalModule");
	var tamanho = $generalModule.length;
	// Search Module
	var $searchModule = $generalModule.find("#searchModule");
	// filters
	var $inSubject = $searchModule.find("#inSubject");
	var $creationDate = $searchModule.find("#creationDate");
	var $publicationDate = $searchModule.find("#publicationDate");
	var $radioCategory = $searchModule.find(".radio-category");
	var $radioEnabled = $searchModule.find(".radio-button");
	// buttons
	var $addButton = $searchModule.find("#add");
	var $searchButton = $searchModule.find("#search");
	// Table module
	var $tableModule = $generalModule.find("#tabela");
	var $fieldTable = $generalModule.find("#fieldTable");
	var $notFoundDiv = $generalModule.find("#notFoundDiv");
	var $notFoundH = $generalModule.find("#notFoundH");
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
	var $pbmodalTpl = $("#pbmodal-template").html();
	var $edMessageTpl = $("#edmessage-template").html();
	function tableTpl(roleEdit, roleEnabled, rolePublication) {
		var string = "{{#each .}}" + "<tr>"
				+ "<td class=\"hideTD\">{{broadcastMessageId}}</td>"
				+ "<td align='center'><div data-toggle='collapse' data-target= '#"+"a"+"{{broadcastMessageId}}' class='accordion-toggle cursor'><i class='glyphicon glyphicon-chevron-down'></i></div></td>"
				+ "<td>{{subject}}</td>	"
				+ "<td class=\"hideTD\">{{messageBody}}</td>	"
				+ "<td>{{substr messageBody 0 30}}</td>	"
				+ "<td>{{broadcastMessageCategoryId.name}}</td>	"
				+ "<td>{{creationDate}}</td>	"
				+ "<td>{{publicationDate}}</td>	"
				+ "<td>{{expirationDate}}</td>	"
				+ "<td class=\"hideTD\">{{enabled}}</td>	"
				+ "<td align='right'><div class='btn-group' role='group' aria-label='...'>";
		if (rolePublication)
			string += "{{#ifNull publicationDate}}"
					+ "<a type = 'button' target = '_self' data-toggle='tooltip' title='Publicar' class='btn btn-xs btn-darkblue-2 publication-button disabled' id='btPublish'><i class='glyphicon glyphicon-volume-up'></i></a>"
					+ "{{else}}"
					+ "<a type = 'button' data-id=\"{{broadcastMessageId}}\" data-name=\"{{subject}}\" target = '_self' data-toggle='tooltip' title='Publicar' class='btn btn-xs btn-darkblue-2 publication-button' id='btPublish'><i class='glyphicon glyphicon-volume-up'></i></a>"
					+ "{{/ifNull}}";
		if (roleEdit)
			string += "<a href=\"addbm.jsp?id={{broadcastMessageId}}\" data-id=\"{{broadcastMessageId}}\" data-name=\"{{subject}}\" type = 'button' target = '_self' data-toggle='tooltip' title='Editar' class='btn btn-xs btn-primary edit-button'><i class='glyphicon glyphicon-pencil'></i></a>";
		if (roleEnabled)
			string += "{{#ifCond enabled '===' 0}}"
					+ "<a type = 'button' data-row-index='{{@index}}' data-link=\"{{getLink links 'ed'}}\" data-id=\"{{broadcastMessageId}}\" data-name=\"{{subject}}\" data-enabled=\"{{enabled}}\" data-toggle='tooltip' title='Inativar' class='btn btn-xs btn-danger ed-button enabled-button'><i class='glyphicon glyphicon-remove'></i></a>"
					+ "<a type = 'button' data-row-index='{{@index}}' data-link=\"{{getLink links 'ed'}}\" data-id=\"{{broadcastMessageId}}\" data-name=\"{{subject}}\" data-enabled=\"{{enabled}}\" style=\"display: none;\" data-toggle='tooltip' title='Ativar' class='btn btn-xs btn-success ed-button disabled-button'><i class='glyphicon glyphicon-ok'></i></a>"
					+ "{{else}}"
					+ "<a type = 'button' data-row-index='{{@index}}' data-link=\"{{getLink links 'ed'}}\" data-id=\"{{broadcastMessageId}}\" data-name=\"{{subject}}\" data-enabled=\"{{enabled}}\" style=\"display: none;\" data-toggle='tooltip' title='Inativar' class='btn btn-xs btn-danger ed-button enabled-button'><i class='glyphicon glyphicon-remove'></i></a>"
					+ "<a type = 'button'  data-row-index='{{@index}}' data-link=\"{{getLink links 'ed'}}\" data-id=\"{{broadcastMessageId}}\" data-name=\"{{subject}}\" data-enabled=\"{{enabled}}\" data-toggle='tooltip' title='Ativar' class='btn btn-xs btn-success ed-button disabled-button'><i class='glyphicon glyphicon-ok'></i></a>"
					+ "{{/ifCond}}";
		string += "</div></td></tr>" 
			+ "<tr> <td colspan='9' class='hiddenRow'><div id='"+"a"+"{{broadcastMessageId}}' class='accordion-body collapse'><span> <strong>Corpo do Comunicado:</strong><br/> {{messageBody}} </span> </div> </td> </tr>"
			+ "{{/each}}";
		return string;
	}
	// bind events
	$searchButton.on('click', {
		param : 1
	}, search);
	$tbody.on('click', '.ed-button', enableDisable);
	$tbody.on('click', '#btPublish', publishClick);
	$notificacoesE.on('click', '#bSim', changeEnabled);
	$notificacoesE.on('click', '#pbSim', publishOk);

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
	function getChecked(elem) {
		var checked;
		elem.each(function(index, elem) {
			if ($(elem).is(':checked'))
				checked = $(elem).val();
		});
		return checked;
	}

	function getFilters(page) {
		var subject = $inSubject.val() || '';
		var creationDate = $creationDate.val() || '';
		var publicationDate = $publicationDate.val() || '';
		var enabled = getChecked($radioEnabled);
		var category = getChecked($radioCategory);
		return {
			'page' : page || 1,
			'subject' : subject,
			'bmc' : category,
			'creationDate' : creationDate.replace(/\//g,''),
			'publicationDate' : publicationDate.replace(/\//g,''),
			'enabled' : enabled
		};
	}
	function setFilters(subject, creation, publication, category, enabled) {
		$inSubject.val(subject);
		$creationDate.val(creation);
		$publicationDate.val(publication);
		
		if (category == 'normal')
			$('#normal').iCheck('check');
		if (enabled == 'emergency')
			$('#emergency').iCheck('check');
		if (enabled == 'ambos')
			$('#both').iCheck('check');
		
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

	function publishClick(event) {
		event.preventDefault();
		var $buttonPB = $(this);
		publish(null, $buttonPB);
	}
	function publish(info, $buttonPB) {
		_render($pbmodalTpl, $notificacoesE, (info || {
			page : $("#pagination").pagination('getCurrentPage'),
			id : $buttonPB.data("id"),
			name : $buttonPB.data("name"),
		}))
		// $('#modalE').modal('show');
		$modalE.modal({
			show : true,
			backdrop : true
		});
	}
	function searchMM(page,access) {
		var data2 = getFilters(page);
		$fieldTable.show();
		$notFoundDiv.hide();
		$
				.ajax({
					type : 'GET',
					url : "../rest/broadcastmessage2/search",
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
							_render(
									tableTpl(roleEdit, roleEnabled, rolePublish),
									$tbody, resp);
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
																		url : "../rest/broadcastmessage2/search",
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
																						roleEnabled,
																						rolePublish),
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
																			jqXHR,
																			textStatus) {
																		modal(
																				"Comunicados em Massa",
																				jqXHR.responseJSON.errorCode
																						+ ", "
																						+ jqXHR.responseJSON.errorMessage);
																		setFilters("", "", "", "", "ambos");
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
								var title = "Comunicados em Massa";
								var body = jqXHR.responseJSON.errorCode + ", "
										+ jqXHR.responseJSON.errorMessage;
								modal(title, body);
								setFilters("", "", "", "", "ambos");
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
			modalED("Comunicados em Massa", resp.message);
			searchMM($("#pagination").pagination('getCurrentPage'));
		}).fail(function(jqXHR, textStatus) {
			var title = "Comunicados em Massa";
			var body = jqXHR.responseJSON.errorCode + ", "
					+ jqXHR.responseJSON.errorMessage;
			modal(title, body);
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
			var pg = $("#pagination").pagination('getCurrentPage');
			modal("Comunicados em massa", resp.message);
			searchMM($("#pagination").pagination('getCurrentPage'));
		}).fail(function(jqXHR, textStatus) {
			var title = "Comunicados em Massa";
			var body = jqXHR.responseJSON.errorCode + ", "
					+ jqXHR.responseJSON.errorMessage;
			modal(title, body);
		});
	}
	
	function formatDate(dt){
		var day = dt.getDate().toString();
		if(day.length == 1)
			day = "0" + day;			
		var month = (dt.getMonth() + 1).toString();
		if(month.length == 1)
			month = "0" + month;		
		var year = dt.getFullYear();
		var dateFormated = day + "/" + month + "/" + year;
	return dateFormated;
	}
	function setDateFormated(){
		var dateToday= new Date();
		var today = formatDate(dateToday);
		var yesterday = formatDate(new Date(dateToday.getTime() - 30*24*60*60*1000));
		$creationDate.val(yesterday);
		$publicationDate.val(today);
	}
	setDateFormated();
	searchMM(1,0);
	module.setFilters = setFilters;
	module.getFilters = getFilters;
	module.searchMM = searchMM;
	module.modal = modal;
	module.edUpdate = edUpdate;
	return module;
})(jQuery, ctrlBM || {});