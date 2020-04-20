var selector = '#ctbody';
var str1 = "Cidadão";
var str2 = "Cidadão";
var str3 = "o";
var templateHandlebars = ModelsTmpl.tmplCitizenResult;

$(document)
		.ready(
				function() {

					var token = $("meta[name='_csrf']").attr("content");
					var header = $("meta[name='_csrf_header']").attr("content");

					var list = $('#list li').map(function() {
						return $(this).text();
					});
					var roleEnabled = $.inArray('ROLE_CITIZEN_ENABLE', list) >= 0;
					$('#search-citizen-form')
							.validate(
									{
										errorPlacement : function(error,
												element) {
											error.insertAfter(element);
											error.wrap("<p>");
											error.css('color', 'red');
										},
										rules : {
											cNome : {
												maxlength : 100
											},
											cEmail : {
												maxlength : 40
											}
										},
										messages : {
											cNome : {
												maxlength : "Nome deve ter no máximo 100 caracteres"
											},

											cEmail : {
												maxlength : "E-mail deve ter no máximo 40 caracteres"
											}
										}
									});

					$('#buscar').click(
							function(event) {
								event.preventDefault();
								var name5 = $('#cNome').val();
								if (name5 === '')
									name5 = 'z';
								var email5 = $('#cEmail').val();
								if (email5 === '')
									email5 = 'z';
								var enabled = 'z';
								if ($('#enabled').is(':checked'))
									enabled = 0;
								if ($('#disabled').is(':checked'))
									enabled = 1;
								if ($('#search-citizen-form').valid()) {
									searchPag('../rest/citizen/search/', 1, '/' + name5
											+ '/' + email5 + '/' + enabled,
											templateHandlebars(roleEnabled),
											selector);
									return true;
								} else
									return false;
							});

					searchInicial('../rest/citizen/search/', 1, '/z/z/z',
							templateHandlebars(roleEnabled), selector);

					$("#ctbody")
							.on(
									"click",
									".list-button",
									function(event) {
										event.preventDefault();
										var cpf;
										var $row = $(this).closest("tr"), $tdCpf = parseInt($row
												.find("td:nth-child(1)").text());
										window.location = '../detailedcitizen.jsp/?cpf='
												+ $tdCpf;
									});

					function getCD(button) {
						var $row = $(button).closest("tr"), $tdId = $row.find(
								"td:nth-child(1)").text(), $tdName = $row.find(
								"td:nth-child(4)").text();
						return {
							'id' : $tdId,
							'name' : $tdName
						};
					}

					$("#ctbody")
							.on(
									"click",
									".enabled-button",
									function(event) {
										event.preventDefault();
										var buttonED = $(this);
										var citizen = getCD(buttonED);
										enabledMessage(citizen.name, str1,
												str2, str3);
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
																				mergeEDToken(
																						'../rest/citizen/ed/'
																								+ citizen.id
																								+ '/'
																								+ $(
																										'#username')
																										.text(),
																						buttonED,
																						true,
																						header,
																						token);
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
										var citizen = getCD(buttonED);
										disabledMessage(citizen.name, str1,
												str2, str3);

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
																				mergeEDToken(
																						'../rest/citizen/ed/'
																								+ citizen.id
																								+ '/'
																								+ $(
																										'#username')
																										.text(),
																						buttonED,
																						false,
																						header,
																						token);
																			});
														});
									});

				});