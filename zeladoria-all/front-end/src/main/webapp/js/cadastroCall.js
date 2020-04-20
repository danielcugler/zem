var str2 = "Chamados";
var str3 = "o";

function getPriority(priority) {
	switch (priority) {
	case "Baixa":
		return 0;
	case "Média":
		return 1;
	case "Alta":
		return 2;
	}
};

function loadInputs(data) {
	data.jsonList = JSON.parse(data.jsonList);
	if (data.jsonList.observation != null) {
		$('#CComments').val(data.jsonList.observation.information);
		$('#CCommentsId')
				.val(data.jsonList.observation.additionalInformationId);
	}
	$('#CDescription').val(data.jsonList.description.information);
	$('#CDescriptionId').val(data.jsonList.description.additionalInformationId);
	$('#COrigem').val(data.jsonList.callSource);
	$('#CDate').val(data.jsonList.creationDate);
	$('#CUser').val('X' || data.jsonList.updatedOrModeratedBy.name);
	$('#selectPriority').prop('selected', false);
	$('#selectCallClassification').prop('selected', false);
	$('#selectEntityCategory').prop('selected', false);
	$(
			"#selectPriority option[value='"
					+ getPriority(data.jsonList.priority) + "']").prop(
			'selected', true);
	$(
			"#selectCallClassification option[value='"
					+ data.jsonList.callClassificationId.callClassificationId
					+ "']").prop('selected', true);

	$(
			"#selectEntity option[value='"
					+ data.jsonList.entityEntityCategoryMaps.entity.entityId
					+ "']").prop('selected', true);

	combo("../rest/entitycategory/activeentity/"
			+ data.jsonList.entityEntityCategoryMaps.entity.entityId,
			ModelsTmpl.tmplComboEntityCategory, "#comboEntityCategory");

	$(
			"#selectEntityCategory option[value='"
					+ data.jsonList.entityEntityCategoryMaps.entityCategory.entityCategoryId
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
					});

					function validaISalvar() {

						$('[name="selectEntityCategory"]').each(function() {
							$(this).rules('add', {
								required : true,
								messages : {
									required : "Selecione uma opção"
								}
							});
						});
						$('[name="selectPriority"]').each(function() {
							$(this).rules('add', {
								required : true,
								messages : {
									required : "Selecione uma opção"
								}
							});
						});
						$('[name="selectCallSource"]').each(function() {
							$(this).rules('add', {
								required : true,
								messages : {
									required : "Selecione uma opção"
								}
							});
						});
						$('[name="selectCallClassification"]').each(function() {
							$(this).rules('add', {
								required : true,
								messages : {
									required : "Selecione uma opção"
								}
							});
						});

						$('[name="CDescription"]')
								.each(
										function() {
											$(this)
													.rules(
															'add',
															{
																required : true,
																maxlength : 1000,
																messages : {
																	required : "Insira  a descrição",
																	maxlength : jQuery.validator
																			.format("Por favor, insira menos que {0} caracteres.")
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
																maxlength : 1000,
																messages : {
																	maxlength : jQuery.validator
																			.format("Por favor, insira menos que {0} caracteres.")
																}
															});
										});

					}
					;

					combo(
							"../rest/entityclass/combosativos",
							ModelsTmpl.tmplComboEntity, "#comboEntity");
					combo("../rest/callclassification",
							ModelsTmpl.tmplComboCallClassification,
							"#comboCallClassification");
					template({}, ModelsTmpl.tmplComboPriority, "#comboPriority");
					var id = getUrlVar('id');

					$('#selectEntity')
							.change(
									function() {
										$('#selectEntity',
												'#selectEntityCategory')
												.remove();
										combo(
												'../rest/entitycategory/activeentity/'
														+ $(
																'#selectEntity  option:selected')
																.val(),
												ModelsTmpl.tmplComboEntityCategory,
												"#comboEntityCategory");
									});

					function getInputsIN() {
						var broadcastMessage = {

							'subject' : $('#cSubject').val(),
							'messageBody' : $('#cMessageBody').val(),
							'enabled' : 'ENABLED'
						};

						return JSON.stringify(broadcastMessage);
					}
					;

					function getInputsEDIT(id) {
						var callclaid = 0;
						switch ($("#selectCallClassification option:selected")
								.val()) {
						case 'Crítica':
							call = 0;
						case 'Denúncia':
							call = 1;
						case 'Elogio':
							call = 2;
						case 'Pedidosdeinformacao':
							call = 3;
						case 'Solicitações':
							call = 4;
						case 'Sugestão':
							call = 5;
						}
						;
						var call = {

							'unsolvedCallId' : id,
							'subject' : $('#cSubject').val(),
							'messageBody' : $('#cMessageBody').val(),
							'entityEntityCategoryMaps' : {
								'entityCategory' : {
									'entityCategoryId' : $(
											"#selectEntityCategory option:selected")
											.val()
								},
								'entity' : {
									'entityId' : $(
											"#selectEntity option:selected")
											.val()
								}
							},
							'callClassificationId' : {
								'callClassificationId' : $(
										"#selectCallClassification option:selected")
										.val()
							},
							'updatedOrModeratedBy' : {
								'systemUserUsername' : $('#username').text()
							},
							'callClassificationId' : {
								'callClassificationId' : callclaid
							},
							'parentCallId' : {
								'unsolvedCallId' : id
							},
							'enabled' : 0
						};

						return JSON.stringify(call);
					}
					;

					function getUrlVar(key) {
						var result = new RegExp(key + "=([^&]*)", "i")
								.exec(window.location.search);
						return result && unescape(result[1]) || "";
					}

					findByIdADD("../rest/unsolvedcall", id);

					function getCall(id) {
						var callclaid = 0;
						switch ($("#selectCallClassification option:selected")
								.val()) {
						case 'Crítica':
							call = 0;
						case 'Denúncia':
							call = 1;
						case 'Elogio':
							call = 2;
						case 'Pedidosdeinformacao':
							call = 3;
						case 'Solicitações':
							call = 4;
						case 'Sugestão':
							call = 5;
						}
						;
						var call = {
							'unsolvedCallId' : id,
							'description' : {
								'additionalInformationId' : $('#CDescriptionId')
										.val(),
								'information' : $('#CDescription').val()
							},
							'observation' : {
								'additionalInformationId' : $('#CCommentsId')
										.val(),
								'information' : $('#CComments').val()
							},
							'callClassificationId' : $(
									"#selectCallClassification option:selected")
									.val(),
							'priority' : $("#selectPriority option:selected")
									.val(),
							'entityEntityCategoryMaps' : {
								'entityCategory' : {
									'entityCategoryId' : $(
											"#selectEntityCategory option:selected")
											.val()
								},
								'entity' : {
									'entityId' : $(
											"#selectEntity option:selected")
											.val()
								}
							},
							'updatedOrModeratedBy' : {
								'systemUserUsername' : $('#username').text()
							},
							'callClassificationId' : {
								'callClassificationId' : callclaid
							},
							'parentCallId' : {
								'unsolvedCallId' : id
							}
						};

						return JSON.stringify(call);
					}
					;

					$('#modaldemensagem')
							.on(
									"click",
									".okbutton",
									function(event) {
										event.preventDefault();
										window.location = '../call.jsp';
									});

					$('.saveAction')
							.click(
									function(event) {
										event.preventDefault();
										validaISalvar();
										if ($("#IForm").valid()) {

											var entityCategoryTarget = $(
													"#selectEntityCategory option:selected")
													.val();
											var updatedOrModeratedBy = $(
													'#CUser').val();
											var callClassificationId = $(
													"#selectCallClassification option:selected")
													.val();
											var call = getCall(id);

											saveToken("../rest/unsolvedcall", call, header, token);

										}

										return false;
									});

					$('.saveSendAction')
							.click(
									function(event) {
										event.preventDefault();
										validaISalvar();
										if ($("#IForm").valid()) {
											var entityCategoryTarget = $(
													"#selectEntityCategory option:selected")
													.val();
											var updatedOrModeratedBy = $(
													'#CUser').val();
											var callClassificationId = $(
													"#selectCallClassification option:selected")
													.val();
											var call = getCall(id);

											saveToken('../rest/unsolvedcall/savesend', call,
													header, token);

										}

										return false;
									});

				});