var selector = '#ctbody';
var str1 = "Comunicado em Massa";
var str2 = "Comunicado em massa";
var str3 = "o";
var templateHandlebars = ModelsTmpl.tmplBroadcastMessageResult;

function loadInputs(data) {
	data.jsonList = JSON.parse(data.jsonList);
	$('#cSubject').val(data.jsonList.subject);
	$('#cMessageBody').val(data.jsonList.messageBody);
	$('#cEnabled').val(data.jsonList.enabled === 'Enabled' ? 0 : 1);
	$("#comboBroadcastMessageCategory option[value='"
		+ data.jsonList.broadcastMessageCategoryId.broadcastMessageCategoryId
		+ "']").prop('selected', true);
}

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
						},
						rules : {
							nome : {
								required : true,
								minlength : 3
							}
						},
					});
					
					function validaISalvar() {
						$('[name="cSubject"]')
								.each(
										function() {
											$(this)
													.rules(
															'add',
															{
																required : true,
																maxlength : 100,
																messages : {
																	required : "Insira o assunto",
																	maxlength : jQuery.validator
																			.format("Por favor, insira menos que {0} caracteres.")
																}
															});
										});

						$("#comboBroadcastMessageCategory")
								.each(
										function() {
											$(this)
													.rules(
															'add',
															{
																selectCheckEntidade : true,
																messages : {
																	selectCheckEntidade : "Por favor, selecione a categoria."
																}
															});
										});

						$('[name="CMessage"]')
								.each(
										function() {
											$(this)
													.rules(
															'add',
															{
																required : true,
																maxlength : 4000,
																messages : {
																	required : "Insira o corpo do comunicado",
																	maxlength : jQuery.validator
																			.format("Por favor, insira menos que {0} caracteres.")
																}
															});
										});

					}
					;
					combo("../rest/broadcastmessagecategory", ModelsTmpl.tmplComboBroadcastMessageCategory,
							"#comboBroadcastMessageCategory");

					function getInputsIN() {
						var broadcastMessage = {

							'subject' : $('#cSubject').val(),
							'messageBody' : $('#cMessageBody').val(),
							'broadcastMessageCategoryId' : {
								'broadcastMessageCategoryId' : $(
										'#comboBroadcastMessageCategory option:selected')
										.val()
							},
							'createdBy' : {
								'systemUserUsername' : $('#username').text()
							},
							'enabled' : 0
						};

						return JSON.stringify(broadcastMessage);
					};

					function getInputsEDIT(id) {
						var broadcastMessage = {
							'broadcastMessageId' : id,
							'subject' : $('#cSubject').val(),
							'messageBody' : $('#cMessageBody').val(),
							'broadcastMessageCategoryId' : {
								'broadcastMessageCategoryId' : $(
										'#comboBroadcastMessageCategory option:selected')
										.val()
							},
							'createdBy' : {
								'systemUserUsername' : $('#username').text()
							},
							'enabled' : $("#cEnabled").val()
						};

						return JSON.stringify(broadcastMessage);
					};

					var geturl = function getUrlVar(key) {
						var result = new RegExp(key + "=([^&]*)", "i")
								.exec(window.location.search);
						return result && unescape(result[1]) || "";
					}

					var getId = function() {

						return geturl('id');
					};

					var id = getId();
					if (id != "") {

						$("#saveButton").removeClass("incluirAction").addClass(
								"editarAction");
						$("#publicButton").removeClass("incluirActionp")
								.addClass("editarActionp");
						findByIdADD("../rest/broadcastmessage", id);

					} else {
						var user = $('#cUser').val();
						var bmc = $(
								'#comboBroadcastMessageCategory option:selected')
								.val();
						var user = $('#cUser').val();
						$("#saveButton").removeClass("editarAction").addClass(
								"incluirAction");
						$("#publicButton").removeClass("editarActionp")
								.addClass("incluirActionp");
					}
					$('#modaldemensagem')
							.on(
									"click",
									".okbutton",
									function(event) {
										event.preventDefault();
										window.location = '../broadcastmessage.jsp';
									});

					$('.editarAction')
							.click(
									function(event) {
										event.preventDefault();
										validaISalvar();

										if ($("#IForm").valid()) {

											var subject = $('#cSubject').val();
											var messageBody = $('#cMessageBody')
													.val();
											var bmc = $(
													'#comboBroadcastMessageCategory option:selected')
													.val();
											var broadcastmessage = getInputsEDIT(id);

											mergeToken('../rest/broadcastmessage/upbm/'
													+ $('#username').text(),
													broadcastmessage, header,
													token);

										}

										return false;
									});

					$('.incluirAction')
							.click(
									function(event) {
										event.preventDefault();
										validaISalvar();
										if ($("#IForm").valid()) {

											var user = $('#username').text();
											var bmc = $(
													'#comboBroadcastMessageCategory option:selected')
													.val();

											saveToken('../rest/broadcastmessage/svbm/'
													+ $('#username').text(),
													getInputsIN(), header,
													token);
											$('.form-group').removeClass(
													"has-error");

											return true;
										} else {
											validateError();
											$('#modaldemensagem').modal('show');
											return false;
										}

									});
					
					$('.editarActionp')
							.click(
									function(event) {
										event.preventDefault();

										var subject = $('#cSubject').val();
										var messageBody = $('#cMessageBody')
												.val();
										var bmc = $(
												'#comboBroadcastMessageCategory option:selected')
												.val();
										var broadcastmessage = getInputsEDIT(id);
										mergeToken('../rest/broadcastmessage/mp/'
												+ $('#username').text(),
												broadcastmessage, header, token);

										return false;
									});

					$('.incluirActionp').click(
							function(event) {
								event.preventDefault();
								validaISalvar();
								if ($("#IForm").valid()) {

									saveToken('../rest/broadcastmessage/sp/'
											+ $('#username').text(),
											getInputsIN(), header, token);
									$('.form-group').removeClass("has-error");
									$('#modaldemensagem').modal('show');
									return true;
								}

								MsgErro();
								$('#modaldemensagem').modal('show');
								return false;
							});

				});