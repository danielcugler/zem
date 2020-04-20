var selector = '#ctbody';
var str1 = "Modelo de Mensagem";
var str2 = "Modelo de mensagem";
var str3 = "o";
var templateHandlebars = ModelsTmpl.tmplMessageModelResult;
$(document)
		.ready(
				function() {
					

					var token = $("meta[name='_csrf']").attr("content");
					var header = $("meta[name='_csrf_header']").attr("content");

					var list = $('#list li').map(function() {
						return $(this).text();
					});
					var roleEdit = $.inArray('ROLE_MESSAGEMODEL_UPDATE', list) >= 0;
					var roleEnabled = $.inArray('ROLE_MESSAGEMODEL_ENABLE',
							list) >= 0;
					var boolSN = false;

					comboMM("../rest/messagemodel" + '/combos', [ ModelsTmpl.tmplComboName,
							ModelsTmpl.tmplComboSubject ], [ "#comboName",
							"#comboSubject" ]);
					searchInicial("../rest/messagemodel" + '/search/', 1, '/z/z/z',
							templateHandlebars(roleEdit, roleEnabled), selector);
					$('#buscar')
							.click(
									function(event) {
										event.preventDefault();
										var name5 = $(
												'#selectName option:selected')
												.val();
										var subject5 = $(
												'#selectAssunto option:selected')
												.val();

										var enabled = 'z'
										if ($('#enabled').is(':checked'))
											enabled = 0;
										if ($('#disabled').is(':checked'))
											enabled = 1;
										if (name5 == "-1")
											name5 = "z";
										if (subject5 == "-1")
											subject5 = "z";
										searchPag("../rest/messagemodel" + '/search/', 1, '/'
												+ name5 + '/' + subject5 + '/'
												+ enabled, templateHandlebars(
												roleEdit, roleEnabled),
												selector);
									});

					function insereparametros(urlSource, object) {
						urlSource += '/?';
						var count = 0;
						for ( var key in object) {
							if (count === 0) {

								urlSource += key + '=' + object[key];
								count++;
							} else {
								urlSource += '&' + key + '=' + object[key];
							}
						}

					}
					;

					findAll();
					
					$("#ctbody").on("click", ".edit-button", function(event) {
						event.preventDefault();
						var data = getRowData(this);
						window.location = '../inclusaoMensagem.jsp/?id=' + data.id;
					});

					$("#ctbody")
							.on(
									"click",
									".enabled-button",
									function(event) {
										event.preventDefault();
										var buttonED = $(this);
										var messageModel = getRowData(buttonED);
										enabledMessage(messageModel.name, str1,
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
																						"../rest/messagemodel"
																								+ '/ed/'
																								+ messageModel.id
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
										var messageModel = getRowData(buttonED);
										disabledMessage(messageModel.name,
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
																						"../rest/messagemodel"
																								+ '/ed/'
																								+ messageModel.id
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

					$('#incluir').click(function(event) {
						event.preventDefault();
						window.location = '../inclusaoMensagem.jsp/?botao=' + 1;
					});

					function classButtonAD(button, remove, add) {
						button.removeClass(remove).addClass(add);
						var $i = button.find("i:nth-child(1)");
						$i.removeClass('glyphicon glyphicon-remove').addClass(
								'glyphicon glyphicon-ok');
					}
					;

					function classButtonDA(button, remove, add) {
						button.removeClass(remove).addClass(add);
						var $i = button.find("i:nth-child(1)");
						$i.removeClass('glyphicon glyphicon-ok').addClass(
								'glyphicon glyphicon-remove');
					}
					;
					function getRowData(botao) {
						var $row = $(botao).closest("tr"), $tdId = parseInt($row
								.find("td:nth-child(1)").text()), $tdName = $row
								.find("td:nth-child(2)").text();
						return {
							'id' : $tdId,
							'name' : $tdName
						};
					}

				});