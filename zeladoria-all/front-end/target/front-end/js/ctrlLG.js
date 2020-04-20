var ctrlLG = (function($, module) {
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
	var $searchModule = $generalModule.find("#search-form");
	// filters
	var $selectSU = $searchModule.find("#selectSU");
	var $selectOT = $searchModule.find("#selectOT");
	var $selectIT = $searchModule.find("#selectIT");
	var $fromDate = $searchModule.find("#fromDate");
	var $toDate = $searchModule.find("#toDate");
	// buttons
	var $searchButton = $searchModule.find("#search");
	var $cleanFiltersButton = $searchModule.find("#cleanFilters");
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
	// templates
	var $MessageTpl = $("#message-template").html();
	var $tableTpl = $("#table-template").html();
	var $comboOTTpl = $("#comboOT-template").html();
	var $comboITTpl = $("#comboIT-template").html();
	var $comboSUTpl = $("#comboSU-template").html();		

	// bind
	$searchButton.on('click', function(event) {
		event.stopPropagation();
		if($searchModule.valid()){
		setFiltersLocalStorage();
		search(1);
		return true;
		}
		modal("Log de Operações", "Por favor, preencha os campos destacados em vermelho!");
		return false;
	});
	
	$cleanFiltersButton.on('click', function(event){
		event.stopPropagation();
		$selectSU.val("");
		$selectIT.val("");
		$selectOT.val("");
		$fromDate.val("");
		$toDate.val("");
		setFiltersLocalStorage();
		search(1);
	});
	
	function getUrlParam(key) {
		var result = new RegExp(key + "=([^&]*)", "i")
				.exec(window.location.search);
		return result && unescape(result[1]) || "";
	}

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
	
	function validateDate(fromDate,toDate){
		var fDate = strToDate(from);
		var tDate = strToDate(toDate);
		var maxDate = after30(fDate);
		if(tDate > maxDate)
			return false;
		else return true;
	}
	
	$.validator.addMethod("validateDate", function(value, element, params) {
		var strTo = $toDate.val();
		var validDate = before30(strToDate(strTo));
		var inDate = strToDate(value);
		return inDate >= validDate;
		}, jQuery.validator.format("Data inválida"));
 
 
	$.validator.addMethod("validateCurrentDate", function(value, element, params) {
		var currentDate = new Date();
		currentDate.setDate(currentDate.getDate());
		currentDate.setHours(23,59,0,0);
		var inDate = strToDate(value);
		var bool = inDate <= currentDate;
		return bool;
		}, jQuery.validator.format("Data inválida, maior que data atual"));
	 
	
	
	// functions
	$searchModule.validate({

		errorPlacement : function(error, element) {
			$(element).parent().addClass("has-error");
			error.insertAfter(element);
			error.wrap("<p>");
			error.css('color', 'red');
		},
		rules : {
			toDate : {
				required : true,
				validateCurrentDate : true
			},
			fromDate : {
				required : true,
				validateDate : true
			}
		},
		messages : {
			toDate : {
				required : "Insira a data final",
				validateCurrentDate : "Erro data inválida, data maior que a atual"
			},
			fromDate : {
				required : "Insira a data inicial",
				validateDate : "Erro intervalo maior que 30 dias entre as datas"				
			}
			},success: function(label,element) {
				$(element).parent().removeClass("has-error");
			}
	});

	function formatDate(data){
		var dd = data.getDate();
		var mm = data.getMonth()+1; //January is 0!
		var yyyy = data.getFullYear();
		if(dd<10)
		    dd='0'+dd;
		if(mm<10)
		    mm='0'+mm;
		return dd+'/'+mm+'/'+yyyy;
		}

		function before30(data){
		     return new Date(new Date().setDate(data.getDate() - 30));
		}


		
		function after30(datai){
			var d = datai;
			  d.setDate( d.getDate() + 30 ); // add 30 days
			  return d;
		}
	
	function modal(title, body) {
		_render($MessageTpl, $notificacoesE, {
			'title' : title,
			'body' : body
		});
		$modalE.modal({
			show : true,
			backdrop : true
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
		return {
			'page' : page,
			'username' : $selectSU.val(),
			'informationType' : $selectIT.val(),
			'operationType' : $selectOT.val(),
			'fromDate' : $fromDate.val(),
			'toDate' : $toDate.val()
		};
	}

	function setInitialFilters(username, informationType, operationType,
			fromDate, toDate) {
		if(username != ""){
			var string = "[value=" + username + "]";
			$("#selectSU option" + string).prop('selected', true);
		}	
		$selectIT.val(informationType);
		$selectOT.val(operationType);
		$fromDate.val(fromDate);
		$toDate.val(toDate);
	}

function showDate(){
	console.log($toDate.val());
	console.log($fromDate.val());
}
	function initial(){
		var today = new Date();
		var before = before30(today);
		$fromDate.val(formatDate(before));
		$toDate.val(formatDate(today));
	}
	function setFilters(username, informationType, operationType, fromDate,
			toDate) {
		$selectSU.val(username);
		$selectIT.val(informationType);
		$selectOT.val(operationType);
		$fromDate.val(fromDate);
		$toDate.val(toDate);
	}

	function makeCombos() {
		$.ajax({
			type : 'GET',
			url : "../rest/log2/combos",
			dataType : "json",
			contentType : "application/json"
		}).done(function(resp, status, xhr) {
			_render($comboSUTpl, $selectSU, resp.su);
			_render($comboITTpl, $selectIT, resp.it);
			_render($comboOTTpl, $selectOT, resp.ot);
			
			if(getUrlParam("detailed")){
				//console.log("User: " + localStorage.getItem("user"));
				setInitialFilters(localStorage.getItem("user"), localStorage
						.getItem("information"), localStorage.getItem("operation"),
						localStorage.getItem("initialDate"), localStorage
								.getItem("finalDate"));						
				search(1);
			}
		}).fail(
				function(jqXHR, textStatus) {
					var title = "Log Operação";
					var body = jqXHR.responseJSON.errorCode + ", "
							+ jqXHR.responseJSON.errorMessage;
					modal(title, body);
				});
	}
	function search(page, access) {
		$fieldTable.show();
		$notFoundDiv.hide();
		$
				.ajax({
					type : 'GET',
					url : "../rest/log2/search",
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
							_render($tableTpl, $tbody, resp);
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
																		url : "../rest/log2/search",
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
																				$tableTpl,
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
																				"Log de Operações",
																				jqXHR.responseJSON.errorCode
																						+ ", "
																						+ jqXHR.responseJSON.errorMessage);
																		/*setFilters(
																				"",
																				"",
																				"",
																				"",
																				"");*/
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
								var title = "Log de Operações";
								var body = jqXHR.responseJSON.errorCode + ", "
										+ jqXHR.responseJSON.errorMessage;
								modal(title, body);
								/*setFilters("", "", "", "", "");*/
							}
						});
	}

	function setFiltersLocalStorage() {
		localStorage.setItem("user", $selectSU.val());
		localStorage.setItem("information", $selectIT.val());
		localStorage.setItem("operation", $selectOT.val());
		localStorage.setItem("initialDate", $fromDate.val());
		localStorage.setItem("finalDate", $toDate.val());
	}
	initial();
	search(1);
	makeCombos();	
	module.search = search;
	module.getFilters = getFilters;
	module.setFilters = setFilters;
	module.showDate = showDate;
	return module;
})(jQuery, ctrlLG || {});