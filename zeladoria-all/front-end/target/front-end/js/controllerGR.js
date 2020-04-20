var str1 = "Relatório";
var str2 = "Relatório";
var str3 = "o";
var checkDate = 0, checkCallClassification = 0, checkCallSource = 0, checkEntity = 0, checkCategory = 0, checkCallProgress = 0, checkCallPriority = 0, checkNeighborhood = 0;

$(document)
		.ready(
				function() {
					comboCC('../rest/callclassification',
							ModelsTmpl.tmplReportCallClassification,
							"#comboCallClassification");
					template({}, ModelsTmpl.tmplReportCallSource,
							"#comboCallSource");
					comboET('../rest/entityclass/combos',
							ModelsTmpl.tmplReportEntity, "#comboEntity");
					$('#comboEntity')
							.change(
									function() {
										$('#comboEntity',
												'#selectEntityCategory')
												.remove();
										combo(
												'../rest/entitycategory/entity/'
														+ $(
																'#comboEntity  option:selected')
																.val(),
												ModelsTmpl.tmplReportEntityCategory,
												"#comboEntityCategory");
									});

					template({}, ModelsTmpl.tmplReportProgress,
							"#comboProgress");
					template({}, ModelsTmpl.tmplReportPriority,
							"#comboPriority");
					template({}, ModelsTmpl.tmplReportChartType,
							"#comboChartType");

					function loadDate() {
						var today = new Date();
						var dd = today.getDate();
						var mm = today.getMonth() + 1; 
						var yyyy = today.getFullYear();

						if (dd < 10) {
							dd = '0' + dd
						}

						if (mm < 10) {
							mm = '0' + mm
						}

						today = dd + '/' + mm + '/' + yyyy;
						$('#fromDate').val(today);
						$('#toDate').val(today);
					}
					;

					loadDate();

					$('#search-call-form').validate({
						errorPlacement : function(error, element) {
							error.insertAfter(element);
							error.wrap("<p>");
							error.css('color', 'red');
						},
						rules : {

						}
					});

					function getSearchSelect(select) {
						var selectValue = $(select + ' option:selected').val();
						return (selectValue === "-1") ? '-1' : selectValue;
					}

					function getSearchInput(inputid) {
						var user = $(inputid).val();
						return (user === "") ? '-1' : user;
					}

					function getRadioValue(idEnabled, idDisabled) {
						if ($(idEnabled).is(':checked'))
							return "consolidado";
						if ($(idDisabled).is(':checked'))
							return "relacionado";
						return 'z';
					}

					function getCheckboxValue() {

						$('#data:checked').each(function() {
							checkDate = 1;
						});
						$('#classificacao:checked').each(function() {
							checkCallClassification = 1;
						});
						$('#origem:checked').each(function() {
							checkCallSource = 1;

						});
						$('#entidade:checked').each(function() {
							checkEntity = 1;
						});
						$('#categoria:checked').each(function() {
							checkCategory = 1;
						});
						$('#andamento:checked').each(function() {
							checkCallProgress = 1;
						});
						$('#prioridade:checked').each(function() {
							checkCallPriority = 1;
						});
						$('#bairro:checked').each(function() {
							checkNeighborhood = 1;
						});
					}

					function getInputDate(inputDateId) {
						var date = $(inputDateId).val();
						return (date === "") ? 'z' : date.replace(/\//g, '');
					}

					function validaCheckbox() {
						if ($('[name=checkReportFields]:checked').length == 0) {
							$("#error")
									.html(
											"Por favor, selecione pelo menos um campo.");
							return false;
						} else {
							if ($('[name=checkReportFields]:checked').length > 0) {
								$("#error").html("");
								return true;
							}
						}
					}

					$('#pdf')
							.click(
									function(event) {
										event.preventDefault();
										if (validaCheckbox() == true) {
											$("#error").html("");
											var initDate = getInputDate('#fromDate'), endDate = getInputDate('#toDate'), callClassification = getSearchSelect('#selectCallClassification'), callSource = getSearchSelect('#selectCallSource'), entity = getSearchSelect('#selectEntity'), category = getSearchSelect('#selectEntityCategory'), callProgress = getSearchSelect('#selectProgress'), priority = getSearchSelect('#selectPriority'), chart = getSearchSelect('#selectChartType'), user = $(
													"#username").text(), reportType = getRadioValue(
													'#consolidado',
													'#relacionado');
													checkDate = 0,
													checkCallClassification = 0,
													checkCallSource = 0,
													checkEntity = 0,
													checkCategory = 0,
													checkCallProgress = 0,
													checkCallPriority = 0,
													checkNeighborhood = 0;

											getCheckboxValue();

											sendReport('../rest/report/generate', user, reportType,
													'pdf', initDate, endDate,
													callClassification,
													callSource, callProgress,
													entity, category, priority,
													chart, checkDate,
													checkCallClassification,
													checkCallSource,
													checkEntity, checkCategory,
													checkCallProgress,
													checkCallPriority,
													checkNeighborhood);

										} else {
											validateError();
											$("#modaldemensagem").modal("show");
										}

									});

					$('#csv')
							.click(
									function(event) {
										event.preventDefault();
										if (validaCheckbox() == true) {
											$("#error").html("");
											var initDate = getInputDate('#fromDate'), endDate = getInputDate('#toDate'), callClassification = getSearchSelect('#selectCallClassification'), callSource = getSearchSelect('#selectCallSource'), entity = getSearchSelect('#selectEntity'), category = getSearchSelect('#selectEntityCategory'), callProgress = getSearchSelect('#selectProgress'), priority = getSearchSelect('#selectPriority'), chart = getSearchSelect('#selectChartType'), user = $(
													"#username").text(), reportType = getRadioValue(
													'#consolidado',
													'#relacionado');
													checkDate = 0,
													checkCallClassification = 0,
													checkCallSource = 0,
													checkEntity = 0,
													checkCategory = 0,
													checkCallProgress = 0,
													checkCallPriority = 0,
													checkNeighborhood = 0;

											getCheckboxValue();

											sendReport('../rest/report/generate', user, reportType,
													'csv', initDate, endDate,
													callClassification,
													callSource, callProgress,
													entity, category, priority,
													chart, checkDate,
													checkCallClassification,
													checkCallSource,
													checkEntity, checkCategory,
													checkCallProgress,
													checkCallPriority,
													checkNeighborhood);
										} else {
											validateError();
											$("#modaldemensagem").modal("show");
										}
									});

				});

function sendReport(URL, user, reportType, format, initDate, endDate,
		callClassification, callSource, callProgress, entity, category,
		priority, chart, checkDate, checkCallClassification, checkCallSource,
		checkEntity, checkCategory, checkCallProgress, checkCallPriority,
		checkNeighborhood) {
	$.ajax({
		type : 'GET',
		async : false,
		url : URL + '/' + user + '/' + reportType + '/' + format + '/'
				+ initDate + '/' + endDate + '/' + callClassification + '/'
				+ callSource + '/' + callProgress + '/' + entity + '/'
				+ category + '/' + priority + '/' + chart + '/' + checkDate
				+ '/' + checkCallClassification + '/' + checkCallSource + '/'
				+ checkEntity + '/' + checkCategory + '/' + checkCallProgress
				+ '/' + checkCallPriority + '/' + checkNeighborhood,
		dataType : "json", 
		contentType : "application/json",
		success : function(data) {
			if (data.message === "Resultado não encontrado.") {
				errorResultReportMessage(data);
				$("#modaldemensagem").modal('show');
			}else{
				if(data.message === "Operação realizada com sucesso."){
					successReportGeneratedMessage(data);
					$("modaldemensagem").modal('show');
				}				
			}
		},
		error : function(jqxhr, status, errorMsg) {
		}
	});
};
