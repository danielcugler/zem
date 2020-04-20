var selector = '#ctbody';
var str1 = "Usuário";
var str2 = "Usuário";
var str3 = "o";
var templateHandlebars = ModelsTmpl.tmplUserResult;

$(document)
		.ready(
				function() {

					var token = $("meta[name='_csrf']").attr("content");
					var header = $("meta[name='_csrf_header']").attr("content");

					var list = $('#list li').map(function() {
						return $(this).text();
					});
					
					var roleEdit = $.inArray('ROLE_USER_UPDATE', list) >= 0;
					var roleEnabled = $.inArray('ROLE_USER_ENABLE', list) >= 0;
					
					comboUS('../rest/systemuserprofile/combos', ModelsTmpl.tmplComboPerfilSearch,
							"#comboPerfil");

					var boolSN = false;
					
					searchInicial('../rest/systemuser/search/', 1, '/z/z/z',
							templateHandlebars(roleEdit, roleEnabled), selector);
					
					function getUserById2(id, user) {

						$.ajax({
							type : 'GET',
							url : '../rest/systemuser/'
									+ id,
							dataType : "json",
							contentType : "application/json",
							success : function(data) {
								callback(data);

							},
							error : function(jqxhr, status, errorMsg) {
							}
						});

						function callback(data) {
							user = data.jsonList;
							disabledMessage(user.name);
						}
						return user;
					}

					$('#search-user-form')
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

											messages : {
												cNome : {
													maxlength : "Nome deve ter no máximo 100 caracteres"
												}
											}
										}
									});

					$('#buscar')
							.click(
									function(event) {
										event.preventDefault();
										var name5 = $('#cNome').val();
										if (name5 === '')
											name5 = 'z';
										var perfil5 = $(
												'#selectPerfil option:selected')
												.val();
										if (perfil5 === '-1')
											perfil5 = 'z';
										var enabled5 = 'z';
										if ($('#enabled').is(':checked'))
											enabled5 = 0;
										if ($('#disabled').is(':checked'))
											enabled5 = 1;
										
										searchPag('../rest/systemuser/search/', 1, '/'
												+ name5 + '/' + perfil5 + '/'
												+ enabled5, templateHandlebars(
												roleEdit, roleEnabled),
												selector);
										return true;
									});

					function getUS(button) {
						var $row = $(button).closest("tr"), $tdId = $row.find(
								"td:nth-child(1)").text(), $tdName = $row.find(
								"td:nth-child(2)").text();
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
										var user = getUS(buttonED);
										enabledMessage(user.name, str1, str2,
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
																						'../rest/systemuser/ed/'
																								+ user.id
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
										var user = getUS(buttonED);
										disabledMessage(user.name, str1, str2,
												str3);

										$('#modalD').modal('show');
										$('#modalD')
												.on(
														'shown.bs.modal',
														function() {
															$("#modalD")
																	.on(
																			"click",
																			".dbSim",
																			function() {
																				mergeEDToken(
																						'../rest/systemuser/ed/'
																								+ user.id
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

						window.location = '../addUser.jsp';
					});

					$("#ctbody").on(
							"click",
							".edit-button",
							function(event) {
								event.preventDefault();
								var button = $(this);
								var user = getId(button);
								window.location = '../addUser.jsp/?botao=' + 2
										+ '&id=' + user;

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

					function getId(botao) {
						var $row = $(botao).closest("tr");
						var $username = $row.find("td:nth-child(1)").text();
						return $username;
					}
					;

				});