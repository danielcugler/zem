var selector = '#ctbody';
var str1 = "Modelo de Mensagem";
var str2 = "Modelo de mensagem";
var str3 = "o";
function loadInputs(data) {
	var messageModel = JSON.parse(data.jsonList);
	$('#CId').val(messageModel.messageModelId);
	$('#CName').val(messageModel.name);
	$('#CSubject').val(messageModel.subject);
	$('#CMessage').val(messageModel.messageBody);
	(messageModel.enabled === "Enabled") ? $('#CEnabled').val(0) : $(
			'#CEnabled').val(1);
}
$(document)
		.ready(
				function() {

					var token = $("meta[name='_csrf']").attr("content");
					var header = $("meta[name='_csrf_header']").attr("content");

					$("#IForm").validate({

						errorPlacement : function(error, element) {
							$(element).parent().addClass("has-error");
							error.insertAfter(element);
							error.wrap("<p>");
							error.css('color', 'red');
						},
						rules : {

						}
					});

					function validaISalvar() {
						$('[name="CName"]')
								.each(
										function() {
											$(this)
													.rules(
															'add',
															{
																required : true,
																maxlength : 100,
																messages : {
																	required : "Insira o Nome",
																	maxlength : jQuery.validator
																			.format("Por favor insira menos que {0} caracteres.")
																}
															});
										});
						$('[name="CSubject"]')
								.each(
										function() {
											$(this)
													.rules(
															'add',
															{
																required : true,
																maxlength : 100,
																messages : {
																	required : "Insira o Assunto",
																	maxlength : jQuery.validator
																			.format("Por favor insira menos que {0} caracteres.")
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
																	required : "Insira o corpo da mensagem",
																	maxlength : jQuery.validator
																			.format("Por favor insira menos que {0} caracteres.")
																}
															});
										});

					};

					function getInputs(opcao) {
						var $inputId = $('#IForm #CId').val(), $inputName = $(
								'#IForm #CName').val(), $inputSubject = $(
								'#IForm #CSubject').val(), $inputMessageBody = $(
								'#IForm #CMessage').val(), $inputEnabled = $(
								'#IForm #CEnabled').val();

						var messageModelS = {
							"name" : $inputName,
							"subject" : $inputSubject,
							"messageBody" : $inputMessageBody,
							"enabled" : 0
						};
						var messageModelE = {
							"messageModelId" : $inputId,
							"name" : $inputName,
							"subject" : $inputSubject,
							"messageBody" : $inputMessageBody,
							"enabled" : $('#IForm #CEnabled').val()
						};
						if (opcao === 1)
							return JSON.stringify(messageModelS);
						if (opcao === 2)
							return JSON.stringify(messageModelE);
					};

					var geturl = function getUrlVar(key) {
						var result = new RegExp(key + "=([^&]*)", "i")
								.exec(window.location.search);
						return result && unescape(result[1]) || "";
					}

					var botaoSalvar = function() {
						var action;
						var id = geturl('id');
						if (id === '')
							action = 'incluirAction';
						else {
							findByIdADD('../rest/messagemodel', id);
							action = 'editarAction';
						}

						$('#saveButton').addClass(action);
					};

					botaoSalvar();

					$('#modaldemensagem')
							.on(
									"click",
									".okbutton",
									function(event) {
										event.preventDefault();
										window.location = '../messagemodel.jsp';
									});

					$('.editarAction').click(
							function(event) {
								event.preventDefault();
								validaISalvar();
								if ($("#IForm").valid()) {
									mergeTokenMessageModel('../rest/messagemodel/'
											+ $('#username').text(),
											getInputs(2), header, token);
									return true;
								}
								validateError();
								$('#modaldemensagem').modal('show');
								return false;

							});

					$('#modalMsg').on('hidden.bs.modal', function() {
						window.location = 'messagemodel.jsp'
					});
					$('.incluirAction').click(
							function(event) {
								event.preventDefault();
								validaISalvar();
								if ($("#IForm").valid()) {
									saveTokenMessageModel(
											'../rest/messagemodel/' + $('#username').text(),
											getInputs(1), header, token);
									return true;

								}
								validateError();
								$('#modaldemensagem').modal('show');
								return false;
							});
				});