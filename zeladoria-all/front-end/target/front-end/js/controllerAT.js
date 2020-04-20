var selector = '#ctbody';
var templateHandlebars = ModelsTmpl.tmplAttendanceTimeResult;
var str1 = "Tempo de atendimento";
var str2 = "Tempo de atendimento";
var str3 = "o";

$(document)
		.ready(
				function() {

					combo('../rest/attendancetime/combos', ModelsTmpl.tmplComboAttendanceTime,
							"#comboName");
					$('#buscar').click(
							function(event) {
								event.preventDefault();
								var name5 = $('#selectAT option:selected')
										.val();
								if (name5 === '-1')
									name5 = 'z';
								var enabled = 'z';
								if ($('#enabled').is(':checked'))
									enabled = 0;
								if ($('#disabled').is(':checked'))
									enabled = 1;
								searchPag('../rest/attendancetime/search/', 1, '/' + name5
										+ '/' + enabled, templateHandlebars,
										selector);
							});
					searchInicial('../rest/attendancetime/search/', 1, '/z/z',
							templateHandlebars, selector);

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
										window.location = "../addattendancetime.jsp";
									});

					$("#ctbody")
							.on(
									"click",
									".edit-button",
									function(event) {
										event.preventDefault();
										var entity = getEC(this);
										window.location = '../addattendancetime.jsp/?id=' + entity.id

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
																				mergeED(
																						'../rest/attendancetime/ed/'
																								+ entity.id,
																						buttonED,
																						true);
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

																				mergeED(
																						'../rest/attendancetime/ed/'
																								+ entity.id,
																						buttonED,
																						false);
																			});
														});
									});

				});