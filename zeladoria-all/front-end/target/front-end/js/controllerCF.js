var selector = '#ctbody';
var templateHandlebars = ModelsTmpl.tmplCallFollowResult;
var str1 = "Acompanhamento de Chamado";
var str2 = "Acompanhamento de Chamado";
var str3 = "o";

$(document)
		.ready(
				function() {

					var token = $("meta[name='_csrf']").attr("content");
					var header = $("meta[name='_csrf_header']").attr("content");

					var list = $('#list li').map(function() {
						return $(this).text();
					});
					var roleEdit = $.inArray('ROLE_CALL_UPDATE', list) >= 0;
					comboEC('../rest/entitycategory',
							ModelsTmpl.tmplComboEntityCategory,
							"#comboEntityCategory");
					comboCC('../rest/callclassification',
							ModelsTmpl.tmplReportCallClassification,
							"#comboCallClassification");
					template({}, ModelsTmpl.tmplReportCallSource,
							"#comboCallSource");
					template({}, ModelsTmpl.tmplReportPriority,
							"#comboPriority");
					searchInicial('../rest/callfollow/search/1/', 1, '/z/z/z/z/z/z/z/z',
							templateHandlebars(roleEdit), selector);

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
					};

					loadDate();

					$('#search-call-form')
							.validate(
									{
										errorPlacement : function(error,
												element) {
											error.insertAfter(element);
											error.wrap("<p>");
											error.css('color', 'red');
										},
										rules : {
											cUser : {
												maxlength : 40
											},
											messages : {
												cUser : {
													maxlength : "O campo deve possuir no máximo 40 caracteres"
												}
											}
										}
									});

					function getSearchSelect(select) {
						var selectValue = $(select + ' option:selected').val();
						return (selectValue === "-1") ? 'z' : selectValue;
					}
					
					function getSearchInput(inputid) {
						var user = $(inputid).val();
						return (user === "") ? 'z' : user;
					}
					
					function getRadioValue(idEnabled, idDisabled) {
						if ($(idEnabled).is(':checked'))
							return "Atrasado";
						if ($(idDisabled).is(':checked'))
							return "Emdia";
						return 'z';
					}
					
					function getInputDate(inputDateId) {
						var date = $(inputDateId).val();
						return (date === "") ? 'z' : date.replace(/\//g, '');
					}
					
					$('#buscar')
							.click(
									function(event) {
										event.preventDefault();

										var datei = getInputDate('#fromDate'), datee = getInputDate('#toDate'), callSource = getSearchSelect('#selectCallSource'), callClassification = getSearchSelect('#selectCallClassification'), priority = getSearchSelect('#selectPriority'), user = 'root', callStatus = getRadioValue(
												'#atrasado', '#emdia');
										
										var cpf = $('#cCPF').val(), id = $('#cID').val();
										
										if(cpf === ''){
											cpf = 'z';
										}
										
										if(id === ''){
											id = 'z';
										}

										searchPag(
												'../rest/callfollow/search/1/', 1, '/'
														+ datei + '/' + datee
														+ '/' + callSource
														+ '/'
														+ callClassification
														+ '/' + priority + '/'
														+ callStatus +'/'
														+ id + '/'
														+ cpf, 
												templateHandlebars(roleEdit),
												selector);
									});

					$('#incluir').click(function(event) {
						window.location = '../editCall.jsp';
					});

					function getRowData(botao) {
						return $(botao).closest("tr").find("td:nth-child(1)")
								.text();
					}
					function getParent(botao) {
						return $(botao).closest("tr").find("td:nth-child(3)")
								.text();
					}

					$("#ctbody")
							.on(
									"click",
									".edit-button",
									function(event) {
										event.preventDefault();
										window.location = '../updateCall.jsp/?id=' + getRowData($(this));
									});

					$("#ctbody")
							.on(
									"click",
									".history-button",
									function(event) {
										event.preventDefault();
										window.location = '../callsHistory.jsp/?id=' + getParent($(this));
									});

					$("#ctbody").on("click", ".view-media", function(event) {
						event.preventDefault();
						botao = $(this);

						findMedia('../rest/callfollow', getParent(botao));

					});
				});
