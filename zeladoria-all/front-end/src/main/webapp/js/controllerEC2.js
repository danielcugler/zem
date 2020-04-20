var selector = '#ctbody';
var templateHandlebars = ModelsTmpl.tmplEntityCategoryResult;
var str1 = "Categoria de Entidade";
var str2 = "Categoria de entidade";
var str3 = "a";

$(document)
		.ready(
				function() {
					var token = $("meta[name='_csrf']").attr("content");
					var header = $("meta[name='_csrf_header']").attr("content");
					var list = $('#list li').map(function() {
						return $(this).text();
					});
					var roleEdit = $
							.inArray('ROLE_ENTITYCATEGORY_UPDATE', list) >= 0;
					var roleEnabled = $.inArray('ROLE_ENTITYCATEGORY_ENABLE',
							list) >= 0;
					$('#buscar').click(
							function(event) {
								event.preventDefault();
								var name5 = $('#cNome').val();
								if (name5 === '')
									name5 = 'z';
								var enabled = 'z';
								if ($('#enabled').is(':checked'))
									enabled = 0;
								if ($('#disabled').is(':checked'))
									enabled = 1;

								$("#pagination").pagination('destroy');
								searchPag('../rest/entitycategory/search/', 1, '/' + name5
										+ '/' + enabled, templateHandlebars(
										roleEdit, roleEnabled), selector);
							});
					searchInicial('../rest/entitycategory/search/', 1, '/z/z',
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
										window.location = '../inclusaodecategoria.jsp';
									});

					$("#ctbody")
							.on(
									"click",
									".edit-button",
									function(event) {
										event.preventDefault();
										var entityCategory = getEC(this);
										window.location = '../inclusaodecategoria.jsp/?id=' + entityCategory.id

									});

					$("#ctbody")
							.on(
									"click",
									".enabled-button",
									function(event) {
										event.preventDefault();
										var buttonED = $(this);
										var entityCategory = getEC(buttonED);
										enabledMessage(entityCategory.name,
												str1, str2, str3);
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
																						'../rest/entitycategory/ed/'
																								+ entityCategory.id
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
										var entityCategory = getEC(buttonED);
										disabledMessage(entityCategory.name,
												str1, str2, str3);

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
																						'../rest/entitycategory/ed/'
																								+ entityCategory.id
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