var str1 = "Chamados";
var str2 = "Chamados";
var str3 = "o";

function loadInputs(data) {
	data.jsonList = JSON.parse(data.jsonList);
	$('#CComments').val(data.jsonList.observation.information);
	$('#CDescription').val(data.jsonList.description.information);
	$('#COrigem').val(data.jsonList.callSource);
	$('#CDate').val(data.jsonList.creationDate);
	$('#CUser').val(data.jsonList.updatedOrModeratedBy.name);
	$('#selectPriority').prop('selected', false);
	$('#selectCallClassification').prop('selected', false);
	$('#selectEntityCategory').prop('selected', false);
	$("#selectPriority option[value='" + data.jsonList.priority + "']").prop(
			'selected', true);
	$(
			"#selectCallClassification option[value='"
					+ data.jsonList.callClassificationId.callClassificationId
					+ "']").prop('selected', true);
	$(
			"#selectEntityCategory option[value='"
					+ data.jsonList.entityCategoryTarget.entityCategoryId
					+ "']").prop('selected', true);
}

function replyCall(button) {
	var tr = $(button).closest("tr");
	var unsolvedCallId = $(tr).find("td:nth-child(1)");
	$("<td class=\"hideTD\">" + list.unsolvedCallId + "</td>").insertAfter(
			unsolvedCallId);
	$(unsolvedCallId).remove();
	var creationOrUpdateDate = $(tr).find("td:nth-child(6)");
	$("<td>" + list.creationOrUpdateDate + "</td>").insertAfter(
			creationOrUpdateDate);
	$(creationOrUpdateDate).remove();
	var description = $(tr).find("td:nth-child(6)");
	$("<td>" + list.description + "</td>").insertAfter(description);
	$(description).remove();
}


function findByIdReplyCall(URL, id) {
	$.ajax({
		type : 'GET',
		async : false,
		url : URL + '/' + id,
		dataType : "json", 
		contentType : "application/json",
		success : function(data) {
			replyCall(data);
		},
		error : function(jqxhr, status, errorMsg) { 
			// Mensagem de erro
		},
		complete : function(jqXHR, status) {
		}
	});
};


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

						$('[name="CAnswer"]')
								.each(
										function() {
											$(this)
													.rules(
															'add',
															{
																required : true,
																maxlength : 1000,
																messages : {
																	required : "Insira  a resposta",
																	maxlength : jQuery.validator
																			.format("Por favor, insira menos que {0} caracteres.")
																}
															});
										});

					}

					function getUrlVar(key) {
						var result = new RegExp(key + "=([^&]*)", "i")
								.exec(window.location.search);
						return result && unescape(result[1]) || "";
					}

					var id = getUrlVar('id');
					findByIdReplyCall('../rest/unsolvedcall', id);

					$('#modaldemensagem')
							.on(
									"click",
									".okbutton",
									function(event) {
										event.preventDefault();
										window.location = '../call.jsp';
									});

					$('#publicButton').click(
							function(event) {
								event.preventDefault();
								validaISalvar();
								if ($("#IForm").valid()) {
									var username = $('#username').text();
									var reply = $('#CAnswer').val();
									var id = $('#callId').text();
									saveReplyToken('../rest/unsolvedcall/rc/' + id + '/'
											+ reply + '/' + username, {},
											header, token);
									return true;
								}
							});

				});