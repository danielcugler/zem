var selector = '#ctbody';
var templateHandlebars = ModelsTmpl.tmplSUPResult;
var str1 = "Perfil";
var str2 = "Perfil";
var str3 = "o";

$(document)
		.ready(
				function() {

					var token = $("meta[name='_csrf']").attr("content");
					var header = $("meta[name='_csrf_header']").attr("content");

					var list = $('#list li').map(function() {
						return $(this).text();
					});
					var roleEdit = $.inArray('ROLE_PROFILE_UPDATE', list) >= 0;
					var roleEnabled = $.inArray('ROLE_PROFILE_ENABLE', list) >= 0;

					$('#buscar').click(
							function() {

								var name5 = $('#cNome').val();
								if (name5 === '')
									name5 = 'z';
								var enabled = 'z';
								if ($('#enabled').is(':checked'))
									enabled = 0;
								if ($('#disabled').is(':checked'))
									enabled = 1;
								$("#pagination").pagination('destroy');
								searchPag('../rest/systemuserprofile/search/', 1, '/' + name5
										+ '/' + enabled, templateHandlebars(
										roleEdit, roleEnabled), selector);
								return false;
							});
					
					searchPag('../rest/systemuserprofile/search/', 1, '/z/z', templateHandlebars(
							roleEdit, roleEnabled), selector);
					
					function getEC(button) {
						var $row = $(button).closest("tr"), $tdId = $row.find(
								"td:nth-child(1)").text(), $tdName = $row.find(
								"td:nth-child(2)").text();
						return {
							'id' : $tdId,
							'name' : $tdName
						};
					}

					$('#incluir').click(function(event) {
						event.preventDefault();
						window.location = '../addsup.jsp';
					});

					$("#ctbody")
							.on(
									"click",
									".edit-button",
									function() {
										var entityCategory = getEC(this);
										window.location = '../addsup.jsp/?id=' + entityCategory.id;
										return false;

									});

					$("#ctbody")
							.on(
									"click",
									".enabled-button",
									function(event) {
										event.stopPropagation();

										var buttonED = $(event.target).closest(
												'a');
										var entityCategory = getEC(buttonED);
										$("#notificacoesE").empty();
										enabledMessage(entityCategory.name,
												str1, str2, str3);
										$('#modalE').modal('show');
										$('#modalE')
												.on(
														'shown.bs.modal',
														function(event2) {
															event2
																	.stopPropagation();
															$("#modalE")
																	.on(
																			"click",
																			".ebSim",
																			function(
																					event3) {
																				event3
																						.stopPropagation();
																				mergeSUPToken(
																						'../rest/systemuserprofile/ed/'
																								+ entityCategory.id
																								+ '/'
																								+ $(
																										'#username')
																										.text(),
																						buttonED,
																						true,
																						header,
																						token);
																				return false;
																			});
															return false;
														});
										return false;
									});
					$("#ctbody")
							.on(
									"click",
									".disabled-button",
									function(event) {
										event.stopPropagation();
										var buttonED = $(event.target).closest(
												'a');
										var entityCategory = getEC(buttonED);
										$("#notificacoesD").empty();
										disabledMessage(entityCategory.name,
												str1, str2, str3);
										$('#modalD').modal('show');
										$('#modalD')
												.on(
														'shown.bs.modal',
														function(event2) {
															event2
																	.stopPropagation();
															$("#modalD")
																	.on(
																			"click",
																			".dbSim",
																			function(
																					event3) {
																				event3
																						.stopPropagation();
																				mergeSUPToken(
																						'../rest/systemuserprofile/ed/'
																								+ entityCategory.id
																								+ '/'
																								+ $(
																										'#username')
																										.text(),
																						buttonED,
																						false,
																						header,
																						token);
																				return false;
																			});
															return false;
														});
										return false;
									});

				});