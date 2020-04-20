var selector = '#ctbody';
var templateHandlebars = ModelsTmpl.tmplEntityResult;
var str1 = "Entidade";
var str2 = "Entidade";
var str3 = "a";

$(document)
		.ready(

				function() {

					var token = $("meta[name='_csrf']").attr("content");
					var header = $("meta[name='_csrf_header']").attr("content");

					var list = $('#list li').map(function() {
						return $(this).text();
					});
					var roleEdit = $.inArray('ROLE_ENTITY_UPDATE', list) >= 0;
					var roleEnabled = $.inArray('ROLE_ENTITY_ENABLE', list) >= 0;
					comboET('../rest/entityclass/combos', ModelsTmpl.tmplComboName,
							"#comboName");
					$('#buscar').click(
							function(event) {
								event.preventDefault();
								var name5 = $('#selectName option:selected')
										.val();
								if (name5 === '-1')
									name5 = 'z';
								var enabled = 'z';
								if ($('#enabled').is(':checked'))
									enabled = 0;
								if ($('#disabled').is(':checked'))
									enabled = 1;
								searchPag('../rest/entityclass/search/', 1, '/' + name5
										+ '/' + enabled, templateHandlebars(
										roleEdit, roleEnabled), selector);
							});
					searchInicial('../rest/entityclass/search/', 1, '/z/z',
							templateHandlebars(roleEdit, roleEnabled), selector);

					function getEC(button) {
						var $row = $(button).closest("tr"), $tdId = $row.find(
								"td:nth-child(1)").text(), $tdName = $row.find(
								"td:nth-child(2)").text();
						return {
							'id' : $tdId,
							'name' : $tdName
						};
					}

					$('#incluir')
							.click(
									function(event) {
										event.preventDefault();
										window.location = '../addEntity.jsp';
									});

					$("#ctbody")
							.on(
									"click",
									".edit-button",
									function(event) {
										event.preventDefault();
										var entity = getEC(this);
										window.location = '../addEntity.jsp/?id=' + entity.id

									});

					$("#ctbody")
							.on(
									"click",
									".enabled-button",
									function(event) {
										event.preventDefault();
										var buttonED = $(this);
										var entity = getEC(buttonED);
										enabledMessage(entity.name, str1, str2,
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
																				mergeEDToken(
																						'../rest/entityclass/ed/'
																								+ entity.id
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
										var entity = getEC(buttonED);
										disabledMessage(entity.name, str1,
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
																						'../rest/entityclass/ed/'
																								+ entity.id
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