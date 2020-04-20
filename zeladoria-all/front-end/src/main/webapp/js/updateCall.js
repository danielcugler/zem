var str1 = "Chamado";
var str2 = "Chamado";
var str3 = "o";
var resizefunc = [];

$(document)
		.ready(
				function() {

					var token = $("meta[name='_csrf']").attr("content");
					var header = $("meta[name='_csrf_header']").attr("content");

					$('#IForm').validate({
						errorPlacement : function(error, element) {
							$(element).parent().addClass("has-error");
							error.insertAfter(element);
							error.wrap("<p>");
							error.css('color', 'red');
						}
					});

					function validaISalvar() {

						$('[name="selectStatus"]').each(function() {
							$(this).rules('add', {
								required : true,
								messages : {
									required : "Selecione uma opção"
								}
							});
						});

						$('[name="CComments"]')
								.each(
										function() {
											$(this)
													.rules(
															'add',
															{
																required : true,
																maxlength : 4000,
																messages : {
																	required : "Insira as Observações",
																	maxlength : jQuery.validator
																			.format("Por favor, insira menos que {0} caracteres.")
																}
															});
										});

						$('[name="CFeedback"]')
								.each(
										function() {
											$(this)
													.rules(
															'add',
															{
																required : true,
																maxlength : 1000,
																messages : {
																	required : "Insira o Parecer",
																	maxlength : jQuery.validator
																			.format("Por favor, insira menos que {0} caracteres.")
																}
															});
										});

						$('[name="CReply"]')
								.each(
										function() {
											$(this)
													.rules(
															'add',
															{
																required : true,
																maxlength : 4000,
																messages : {
																	required : "Insira a Resposta ao Usuário",
																	maxlength : jQuery.validator
																			.format("Por favor, insira menos que {0} caracteres.")
																}
															});
										});
					}

					var geturl = function getUrlVar(key) {
						var result = new RegExp(key + "=([^&]*)", "i")
								.exec(window.location.search);
						return result && unescape(result[1]) || "";
					}

					var id = geturl('id');

					findByIdUpdate('../rest/callfollow2/' + id, id, header,
							token);

					$('#saveButton')
							.click(
									function(event) {
										event.preventDefault();
										validaISalvar();

										var callStatus = null;
										var callProgress = null;
										var observation = null;
										var description = null;
										var username = $("#username").text();

										if ($("#IForm").valid()) {
											var status = $(
													'#selectStatus option:selected')
													.val();
											var coments = $('#CComments').val();
											var feedback = $('#CFeedback')
													.val();
											var feedback2 = $('#CFeedback2')
													.val();
											var reply = $('#CReply').val();
											var sendMessage = $(
													'#CSend_message').val();
											if (coments !== "") {
												observation = coments;
											} else if (feedback !== "") {
												observation = feedback;
											} else if (feedback2 !== "") {
												observation = feedback2;
											}
											if (reply !== "") {
												observation += "\n" + reply;
											}
											if (status == "Rejeitado") {
												callProgress = 6;
											} else if (status == "Em Andamento") {
												callProgress = 5;
											} else if (status == "Finalizado") {
												if (sendMessage == "ENABLED")
													callProgress = 3;
												else
													callProgress = 2;
											}

											var unsolvedCall = {
												unsolvedCallId : id,
												callStatus : callStatus,
												callProgress : callProgress,
												updatedOrModeratedBy : username,
												observation : {
													information : observation
												},
											};

											if (unsolvedCall.callProgress == 5
													|| unsolvedCall.callProgress == 6) {
												updateCallToken(
														'../rest/callfollow2/upcall',
														JSON
																.stringify(unsolvedCall),
														header, token);
											} else {
												updateCallToken(
														'../rest/callfollow2/finishcall',
														JSON
																.stringify(unsolvedCall),
														header, token);

											}
										}
										return false;
									});

					$(document).on(
							'change',
							'#selectStatus',
							function changeDiv() {
								var status = $("#selectStatus").val();
								if (status == "Rejeitado") {
									$("#Feedback").show();
									$("#Reply").hide();
									$("#Comments").hide();
									$("#Parecer2").hide();
									$("#parecer").append(
											"<span class='vermelho'>*</span>");
								} else if (status == "Em Andamento") {
									$("#Feedback").hide();
									$("#Reply").hide();
									$("#Comments").show();
									$("#Parecer2").hide();
									$("#parecer").text("Parecer");
								} else if (status == "Finalizado") {
									$("#Feedback").hide();
									$("#Reply").show();
									$("#Comments").hide();
									$("#Parecer2").show();
									$("#parecer").text("Parecer");
								}
							});

					$('#addMessage').click(function(event) {
						event.preventDefault();
						var message;
						findMessages();
						$('#modaldemensagem').modal('show');

					});

					$('#modaldemensagem')
							.on(
									'shown.bs.modal',
									function(event) {
										event.preventDefault();
										$('tr')
												.click(
														function() {
															var $row = $(this)
																	.closest(
																			"tr");
															var $tdMessage = $row
																	.find(
																			"td:nth-child(2)")
																	.text();
															message = $tdMessage;
															$(
																	'#modaldemensagem')
																	.modal(
																			'hide');

															$(
																	'#modaldemensagem')
																	.on(
																			'hidden.bs.modal',
																			function() {
																				$(
																						'#CReply')
																						.text(
																								message);
																				$(
																						'#CReply')
																						.val(
																								message);
																			});
														});
									});

					$('#modaldemensagem').on("click", ".okbutton",
							function(event) {
								event.preventDefault();
								window.location = '../callfollow.jsp';
							});

					function findByIdUpdate(URL, id, header, token) {
						$
								.ajax({
									type : 'GET',
									url : URL,
									async : false,
									dataType : "json",
									contentType : "application/json",
									success : function(data) {
										$("#CSend_message")
												.val(
														data.entityEntityCategoryMaps.entityCategory.send_message);
										if (data.callProgress == 'Encaminhado') {
											mergeView(
													'../rest/callfollow2/seen/'
															+ id, {}, header,
													token);
										}
										if (data.callProgress == 'Visualizado'
												|| data.callProgress == 'Encaminhado') {
											$("#Feedback").hide();
											$("#Reply").hide();
											$("#Comments").show();
											$(
													"#selectStatus option[value='Em Andamento']")
													.attr('selected',
															'selected');
										} else if (data.callProgress == 'Em Andamento') {
											$("#Feedback").hide();
											$("#Reply").hide();
											$("#Comments").show();
											$(
													"#selectStatus option[value='Em Andamento']")
													.attr('selected',
															'selected');
											$(
													"#selectStatus option[value='Rejeitado']")
													.attr('disabled',
															'disabled');
										} else if (data.callProgress == 'Finalizado') {
											$("#Feedback").show();
											$("#Reply").show();
											$("#Comments").hide();
											$(
													"#selectStatus option[value='Rejeitado']")
													.attr('disabled',
															'disabled');
											$(
													"#selectStatus option[value='Em Andamento']")
													.attr('disabled',
															'disabled');
											$(
													"#selectStatus option[value='Finalizado']")
													.attr('selected',
															'selected');
										}
									},
									error : function(jqxhr, status, errorMsg) {
										// Mensagem de erro
									},
									complete : function(jqXHR, status) {

									}
								});
					}

					function updateCallToken(URL, data, header, token) {
						$
								.ajax({
									type : 'POST',
									url : URL,
									dataType : "json",
									data : data,
									beforeSend : function(xhr) {
										xhr.setRequestHeader(header, token);
									},
									contentType : "application/json",
									success : function(resp) {
										if (resp.message !== 'Erro campo nome já cadastrado.') {
											updateMessage(str1, str2, str3);
											$('#modaldemensagem').modal('show');
										} else
											duplicatedMessage(resp);
										$('#modaldemensagem').modal('show');
									},
									error : function(data) {
										// Mensagem de erro
									}
								});
					}

				});