var selector = '#ctbody';
var templateHandlebars = ModelsTmpl.tmplMassCommunicationsResult;
var str1 = "Chamados";
var str2 = "Chamados";
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
					var roleEnabled = $.inArray('ROLE_CALL_ENABLE', list) >= 0;
					var roleSend = $.inArray('ROLE_CALL_SEND', list) >= 0;
					var roleReply = $.inArray('ROLE_CALL_REPLY', list) >= 0;

					comboEC('../rest/entitycategory',
							ModelsTmpl.tmplReportEntityCategory,
							"#comboEntityCategory");
					comboCC('../rest/callclassification',
							ModelsTmpl.tmplReportCallClassification,
							"#comboCallClassification");
					template({}, ModelsTmpl.tmplReportCallSource,
							"#comboCallSource");
					template({}, ModelsTmpl.tmplReportPriority,
							"#comboPriority");
					searchInicial('../rest/unsolvedcall/search/', 1,
							'/z/z/z/z/z/z/z/z/z/z',
							templateHandlebars(roleEdit, roleEnabled, roleSend,
									roleReply), selector);

					$('.popovers').popover();

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
					function getSelectCallClassification(select) {

						var callclassification = $(
								"#selectCallClassification option:selected")
								.val();
						if (callclassification === "-1")
							return 'z';
						else
							return callclassification;
					}
					function getSearchInput(inputid) {
						var user = $(inputid).val();
						return (user === "") ? 'z' : user;
					}
					function getRadioValue(idEnabled, idDisabled) {
						if ($(idEnabled).is(':checked'))
							return 0;
						if ($(idDisabled).is(':checked'))
							return 1;
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

										var datei = getInputDate('#fromDate'), datee = getInputDate('#toDate'), callSource = getSearchSelect('#selectCallSource'), callClassification = getSelectCallClassification('#selectCallClassification'), entityCategory = getSearchSelect('#selectEntityCategory'), priority = getSearchSelect('#selectPriority'), user = getSearchInput('#cUser'), callStatus = getRadioValue(
												'#enabled', '#disabled');

										var cpf = $('#cCPF').val(), id = $(
												'#cID').val();

										if (cpf === '') {
											cpf = 'z';
										}

										if (id === '') {
											id = 'z';
										}

										searchPag('../rest/unsolvedcall/search/',
												1, 
												'/' + datei + '/' + datee
													+ '/' + callSource
													+ '/'
													+ callClassification
													+ '/' + entityCategory
													+ '/' + priority + '/'
													+ user + '/'
													+ callStatus + '/' + id
													+ '/' + cpf,
												templateHandlebars(roleEdit,
														roleEnabled, roleSend,
														roleReply), 
												selector);
									});
					$("#ctbody")
							.on(
									"click",
									".enabled-button",
									function(event) {
										event.preventDefault();
										var buttonED = $(this);
										var username = $('#username').text();
										var $tdCmId = getRowData(buttonED);
										enabledMessage($tdCmId, str1, str2,
												str3);

										$('#modalE').modal('show');
										$('#modalE')
												.on(
														'shown.bs.modal',
														function() {
															$("#modalE")
																	.on(
																			"click",
																			".ebSim",
																			function() {
																				mergeEDCalltoken(
																						'../rest/unsolvedcall/ed/'
																								+ $tdCmId
																								+ '/'
																								+ username,
																						buttonED,
																						true,
																						header,
																						token);
																				var tdButtons = $(
																						buttonED)
																						.closest(
																								"tr")
																						.find(
																								"td:nth-child(13)");
																				$(
																						tdButtons)
																						.find(
																								"a:nth-child(3)")
																						.addClass(
																								'disabled');
																			})
														});

									});

					$("#ctbody")
							.on(
									"click",
									".disabled-button",
									function(event) {
										event.preventDefault();
										var buttonED = $(this);
										var username = $('#username').text();
										var $tdCmId = getRowData(buttonED);
										disabledMessage($tdCmId, str1, str2,
												str3);

										$('#modalD').modal('show');
										$('#modalD')
												.on(
														'shown.bs.modal',
														function(button) {
															$("#modalD")
																	.on(
																			"click",
																			".dbSim",
																			function(
																					button) {
																				mergeEDCalltoken(
																						'../rest/unsolvedcall/ed/'
																								+ $tdCmId
																								+ '/'
																								+ username,
																						buttonED,
																						false,
																						header,
																						token);
																				var tdButtons = $(
																						buttonED)
																						.closest(
																								"tr")
																						.find(
																								"td:nth-child(13)");
																				$(
																						tdButtons)
																						.find(
																								"a:nth-child(3)")
																						.removeClass(
																								'disabled');
																			});
														});

									});

					$('#incluir').click(function(event) {
						window.location = '../rest/callclassification';
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
										window.location = '../editCall.jsp?id=' + getRowData($(this));
									});
					$("#ctbody").on(
							"click",
							".reply-button",
							function(event) {
								event.preventDefault();
								var buttonED = $(this);
								var $tdCmId = getRowData(buttonED);
								disabledMessage($tdCmId, str1, str2, str3);
								window.location = '../replyCall.jsp/?id='
										+ getRowData($(this));
							});

					$("#ctbody")
							.on(
									"click",
									".send-button",
									function(event) {
										event.preventDefault();
										var buttonED = $(this);
										var username = $('#username').text();
										var $tdCmId = getRowData(buttonED);
										var $tdEntity = $(buttonED).closest(
												"tr").find("td:nth-child(5)")
												.text();
										confirmSend($tdEntity);
										$('#modalD').modal('show');
										$('#modalD')
												.on(
														'shown.bs.modal',
														function(button) {
															$("#modalD")
																	.on(
																			"click",
																			"#bSim",
																			function(
																					button) {

																				mergeCSToken(
																						'../rest/unsolvedcall/send/'
																								+ $tdCmId
																								+ '/'
																								+ username,
																						buttonED,
																						header,
																						token,
																						roleEdit,
																						roleEnabled,
																						roleSend,
																						roleReply);

																				var tdButtons = $(
																						buttonED)
																						.closest(
																								"tr")
																						.find(
																								"td:nth-child(13)");
																				$(
																						tdButtons)
																						.find(
																								"a:nth-child(1)")
																						.addClass(
																								'disabled');
																				$(
																						tdButtons)
																						.find(
																								"a:nth-child(2)")
																						.addClass(
																								'disabled');
																				$(
																						tdButtons)
																						.find(
																								"a:nth-child(3)")
																						.addClass(
																								'disabled');
																				$(
																						tdButtons)
																						.find(
																								"a:nth-child(4)")
																						.addClass(
																								'disabled');
																			});
														});

									});
					$("#ctbody").on(
							"click",
							".history-button",
							function(event) {
								event.preventDefault();
								window.location = '../callsHistory.jsp/?id='
										+ getParent($(this));
							});

					$("#ctbody").on("click", ".view-media", function(event) {
						event.preventDefault();
						botao = $(this);

						findMedia('../rest/unsolvedcall', getParent(botao));

					});
				});
