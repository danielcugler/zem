var ctrlNB = (function($, module) {
	// var module = {};
	var newOnPage = true;
	var list = $('#list li').map(function() {
		return $(this).text();
	});
	// security rules
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	var roleEdit = $.inArray('ROLE_NEIGHBORHOOD_UPDATE', list) >= 0;
	// cached DOM
	// general module
	var $generalModule = $("#generalModule");
	var tamanho = $generalModule.length;
	// Search Module
	var $searchModule = $generalModule.find("#searchModule");
	// filters
	var $inName = $searchModule.find("#inName");
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
	var $MessageTpl = $("#message-template").html();
	function tableTpl(roleEdit) {
		var string = "{{#each .}}" + "<tr>"
				+ "<td class=\"hideTD\">{{neighborhoodlId}}</td>"
				+ "<td>{{name}}</td>	" 
				+ "<td>{{cityId.name}}</td>	"
				+ "<td align='right'> <div class='btn-group' role='group' aria-label='...'>	";
	//	if (roleEdit)
			string += "<a href=\"../addnb.jsp?id={{neighborhoodId}}\" type = 'button' target = '_self' data-toggle='tooltip' title='Editar' class='btn btn-xs btn-primary edit-button'><i class='glyphicon glyphicon-pencil'></i></a>";
		string += "</td></tr>{{/each}}";
		return string;
	}
	// bind events
	$searchButton.on('click', function(){
		search();
		});
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
	// search message model function
	function getFilters(page) {
		var name = $inName.val() || null;
		return {
			'page' : page,
			'name' : name,
			'cityId': 3551
		};
	}

	function setFilters(name) {
		$inName.val(name);
	}
	function search(page,access) {
		$
				.ajax({
					type : 'GET',
					url : "../rest/neighborhood/search",
					dataType : "json",
					data: getFilters(1),
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
																		url : "../rest/neighborhood/search",
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
	search(1,0);
	module.setFilters = setFilters;
	module.getFilters = getFilters;
	module.modal = modal;
	return module;
})(jQuery, ctrlNB || {});