var selector = '#ctbody';
var str1 = "Comunicado em Massa";
var str2 = "Comunicado em massa";
var str3 = "o";
var templateHandlebars = ModelsTmpl.tmplBroadcastMessageResult;

function publish(button) {
	$(button).addClass("disabled");

};

function publicarToken(url, id, publicacao, button, URL, header, token) {

	$.ajax({
		type : 'PUT',
		url : url,
		beforeSend : function(xhr) {
			xhr.setRequestHeader(header, token);
		},
		contentType : "application/json",
		success : function(data) {
			publish(button, id);
			$(publicacao).text(data.jsonList);
			publishSucessMessage(str1, str2, str3);
			$('#modaldemensagem').modal('show');
		},
		error : function(data) { 
			// Mensagem de erro
		}
	});

};

$(document)
		.ready(
				function() {
					
					var token = $("meta[name='_csrf']").attr("content");
				    var header = $("meta[name='_csrf_header']").attr("content");
					
					var list = $('#list li').map(function(){ return $(this).text(); });
					var roleEdit = $.inArray('ROLE_COMMUNICATION_UPDATE', list)>=0;
					var roleEnabled = $.inArray('ROLE_COMMUNICATION_ENABLE', list)>=0;
					var rolePublication = $.inArray('ROLE_COMMUNICATION_PUBLISH', list)>=0;
					var boolSN = false;
					searchInicial('../rest/broadcastmessage/search/', 1, '/z/z/z/z/z',
							templateHandlebars(roleEdit,roleEnabled,rolePublication), selector);
					function alteraED(button) {
						classButtonAD(button,
								"btn btn-xs btn-red-1 enabled-button",
								"btn btn-xs btn-success disabled-button");

					}

					$('#search-broadcast-form')
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

					$('#buscar').click(
							function(event) {
								event.preventDefault();
								var subject5 = $('#cSubject').val();
								if (subject5 === "")
									subject5 = 'z';

								var datei = $('#creationDate').val().replace(
										/\//g, '');
								var datee = $('#publicationDate').val()
										.replace(/\//g, '');
								if (datei === "")
									datei = 'z';
								if (datee === "")
									datee = 'z';
								var enabled = 'z';
								if ($('#enabled').is(':checked'))
									enabled = 0;
								if ($('#disabled').is(':checked'))
									enabled = 1;
								var bmc = 'z';
								if ($('#normal').is(':checked'))
									bmc = 0;
								if ($('#emergency').is(':checked'))
									bmc = 1;

								$("#pagination").pagination('destroy');

								searchPag('../rest/broadcastmessage/search/', 1, '/' + subject5
										+ '/' + bmc + '/' + enabled + '/'
										+ datei + '/' + datee,
										templateHandlebars(roleEdit,roleEnabled,rolePublication), selector);
							});

					$("#ctbody")
							.on(
									"click",
									".enabled-button",
									function(event) {
										event.preventDefault();
										var $row = $(this).closest("tr");
										var $tdBmId = parseInt($row.find(
												"td:nth-child(1)").text());
										var $tdSubject = $row.find(
												"td:nth-child(2)").text();

										var button = $(this);
										var buttonED = $(this);
										var user;
										var bool = false;
										enabledMessage($tdSubject, str1, str2,
												str3);
										$('#modalE').modal('show');

										$('#modalE')
												.on(
														'shown.bs.modal',
														function(button) {
															$("#modalE")
																	.on(
																			"click",
																			".ebSim",
																			function(
																					button) {

																				mergeEDToken('../rest/broadcastmessage/ed/'+ $tdBmId+'/'+$('#username').text(), buttonED, true, header, token);

																			});

														});

									});

					$("#ctbody")
							.on(
									"click",
									".disabled-button",
									function(event) {
										event.preventDefault();
										var user;
										var $row = $(this).closest("tr");
										var $tdBmId = parseInt($row.find(
												"td:nth-child(1)").text());
										var $tdSubject = $row.find(
												"td:nth-child(2)").text();
										disabledMessage($tdSubject, str1, str2,
												str3);
										var button = $(this);
										var buttonED = $(this);
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
																				mergeEDToken('../rest/broadcastmessage/ed/'+ $tdBmId+'/'+$('#username').text(), buttonED, false, header, token);
																			});

														});

									});

					$('#incluir')
							.click(
									function(event) {
										window.location = "../addbroadcast.jsp"; 
									});

					$("#ctbody")
							.on(
									"click",
									".publication-button",
									function(event) {
										event.preventDefault();
										var $row = $(this).closest("tr");
										var $tdBmId = parseInt($row.find(
												"td:nth-child(1)").text());
										var $tdSubject = $row.find(
												"td:nth-child(2)").text();
										var $tdpublicationDate = $row
												.find("td:nth-child(7)");

										publishMessage($tdSubject, str1, str2,
												str3);
										var button = $(this);
										var buttonED = $(this);
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
																			publicarToken('../rest/broadcastmessage/publicar/' + $tdBmId+'/'+$('#username').text(),$tdBmId, $tdpublicationDate, buttonED, "../rest/broadcastmessage", header, token);
																			});

														});

									});

					$("#ctbody")
							.on(
									"click",
									".edit-button",
									function(event) {
										event.preventDefault();
										var buttonEdit = $(this);
										var id = getRowData(buttonEdit).id;

										window.location = '../addbroadcast.jsp/?id=' + id;
									});


					function classButtonAD(button, remove, add) {
						button.removeClass(remove).addClass(add);
						var $i = button.find("i:nth-child(1)");
						$i.removeClass('glyphicon glyphicon-remove').addClass(
								'glyphicon glyphicon-ok');
					}

					function classButtonDA(button, remove, add) {
						button.removeClass(remove).addClass(add);
						var $i = button.find("i:nth-child(1)");
						$i.removeClass('glyphicon glyphicon-ok').addClass(
								'glyphicon glyphicon-remove');
					}
					function getRowData(button) {
						var $row = $(button).closest("tr");
						var $tdBmId = parseInt($row.find("td:nth-child(1)")
								.text());
						var $tdSubject = $row.find("td:nth-child(2)").text();
						return {
							'id' : $tdBmId,
							'subject' : $tdSubject
						}
					}

					function getId(botao) {
						var $row = $(botao).closest("tr");

						var $username = $row.find("td:nth-child(1)").text();
						return $username;
					}
					
					function pegaDadosTabela(botao, opcao) {
						var $row = $(botao).closest("tr"), $tdBroadcastMessageId = $row
								.find("td:nth-child(1)"), $tdSubject = $row
								.find("td:nth-child(2)"), $tdMessageBody = $row
								.find("td:nth-child(3)"), $tdBroadcastMessageCategory = $row
								.find("td:nth-child(4)"), $tdCreationDate = $row
								.find("td:nth-child(5)"), $tdHome_address_geograpical_coordinates = $row
								.find("td:nth-child(6)"), $tdName = $row
								.find("td:nth-child(7)"), $tdNeighborhood_id = $row
								.find("td:nth-child(8)"), $tdNeighborhood_name = $row
								.find("td:nth-child(9)"), $tdPhone_number = $row
								.find("td:nth-child(10)"), $tdPhoto = $row
								.find("td:nth-child(11)"), $tdStreet_name = $row
								.find("td:nth-child(12)"), $buttonEnabled = ($row
								.find("button:nth-child(2)")
								.hasClass('enabled-button')) ? true : false;
					

						var user = {
							"address_number" : $tdAddress_number.text(),
							"citizen_cpf" : $tdCpf.text(),
							"city_name" : $tdCity_name.text(),
							"email" : $tdEmail.text(),
							"enabled" : false,
							"home_address_geograpical_coordinates" : $tdHome_address_geograpical_coordinates
									.text(),
							"name" : $tdName.text(),
							"neighborhood_id" : $tdNeighborhood_id.text(),
							"neighborhood_name" : $tdNeighborhood_name.text(),
							"phone_number" : $tdPhone_number.text(),
							"photo" : $tdPhoto.text(),
							"street_name" : $tdStreet_name.text()

						};
						
						switch (opcao) {
						case 1:
							return user;
						case 2:
							user['enabled'] = 'ENABLED';
							return user;
						case 3:
							user['enabled'] = 'DISABLED';
							return;
						}
					}

				});