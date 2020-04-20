var selector = '#ctbody';
var str1 = "Log de Operações";
var str2 = "Log de operações";
var str3 = "o";
var templateHandlebars = ModelsTmpl.tmplLogResult;

$(document)
		.ready(
				function() {
					var boolSN = false;
					combo('../rest/log/comboit', ModelsTmpl.tmplComboIT, "#comboIT");
					combo('../rest/log/comboot', ModelsTmpl.tmplComboOT, "#comboOT");
					combo('../rest/log/combosu', ModelsTmpl.tmplComboSUSearch,
							"#comboSU");
					searchInicial('../rest/log/search/', 1, '/z/z/z/z/z',
							templateHandlebars, selector);

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
						$('#initialDate').val(today);
						$('#finalDate').val(today);
					}
					;

					loadDate();

					function alteraED(button) {
						classButtonAD(button,
								"btn btn-xs btn-red-1 enabled-button",
								"btn btn-xs btn-success disabled-button");

					}
					;

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

								var systemUser = $('#selectSU').val();
								if (systemUser === '-1')
									systemUser = 'z';
								var informationType = $('#selectIT').val();
								if (informationType === '-1')
									informationType = 'z';
								var operationType = $('#selectOT').val();
								if (operationType === '-1')
									operationType = 'z';
								var datei = $('#initialDate').val().replace(
										/\//g, '');
								var datee = $('#finalDate').val().replace(
										/\//g, '');
								if (datei === "")
									datei = 'z';
								if (datee === "")
									datee = 'z';

								$("#pagination").pagination('destroy');

								searchPag('../rest/log/search/', 1, '/' + systemUser
										+ '/' + informationType + '/'
										+ operationType + '/' + datei + '/'
										+ datee, templateHandlebars, selector);
							});

					$("#ctbody")
							.on(
									"click",
									".list-button",
									function(event) {
										event.preventDefault();
										window.location = '../detailedlog.jsp/?id=' + getId($(this));
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

						var $logid = $row.find("td:nth-child(1)").text();
						return $logid;
					}
					;
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
						;

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