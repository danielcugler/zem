var str1 = "Categoria de Entidade";
var str2 = "Categoria de entidade";
var str3 = "a";
function loadInputs(data) {
	var entityCategory = JSON.parse(data.jsonList);
	$('#CId').val(entityCategory.entityCategoryId);
	$('#nome').val(entityCategory.name);
	$('#envio').val(entityCategory.send_message);
	if (entityCategory.send_message === 'Enabled') {
		$('input[name=envio]').attr('checked', true);
		$('.icheckbox_square-aero').addClass("checked");
	}
	$('#CEnabled').val(entityCategory.enabled === 'Enabled' ? 0 : 1);
}

$(document)
		.ready(
				function() {
					var token = $("meta[name='_csrf']").attr("content");
					var header = $("meta[name='_csrf_header']").attr("content");
					$('#fCadastro').validate({
						errorPlacement : function(error, element) {
							$(element).parent().addClass("has-error");
							error.insertAfter(element);
							error.wrap("<p>");
							error.css('color', 'red');
						},
						rules : {
							nome : {
								required : true
							}
						},
					});
					function validaISalvar() {
						$('[name="nome"]')
								.each(
										function() {
											$(this)
													.rules(
															'add',
															{
																required : true,
																maxlength : 40,
																messages : {
																	required : "Insira o Nome",
																	maxlength : jQuery.validator
																			.format("Por favor insira menos que {0} caracteres.")
																}
															});
										});

					};

					$('#modaldemensagem')
							.on(
									"click",
									".okbutton",
									function(event) {
										event.preventDefault();
										window.location = '../entitycategory.jsp';
									});

					function getInputsEDIT() {
						var entityCategory = {
							'entityCategoryId' : $('#CId').val(),
							'name' : $('#nome').val(),
							'send_message' : $('#fCadastro #envio').is(
									':checked') ? 0 : 1,
							'enabled' : $('#CEnabled').val()
						};
						return JSON.stringify(entityCategory);
					};

					function getInputsINC() {
						var entityCategory = {
							'name' : $('#nome').val(),
							'send_message' : $('#fCadastro #envio').is(
									':checked') ? 0 : 1,
							'enabled' : 0
						};
						return JSON.stringify(entityCategory);
					};

					var geturl = function getUrlVar(key) {
						var result = new RegExp(key + "=([^&]*)", "i")
								.exec(window.location.search);
						return result && unescape(result[1]) || "";
					}
					var id = geturl('id');
					if (id != "") {
						$("#saveButton").removeClass("incluirAction").addClass(
								"editarAction");
						findByIdADD("../rest/entitycategory", id);
					} else {
						$("#saveButton").removeClass("editarAction").addClass(
								"incluirAction");
					}
					$('.editarAction').click(
							function(event) {
								event.preventDefault();
								validaISalvar();
								if ($("#fCadastro").valid()) {
									mergeToken('../rest/entitycategory/'
											+ $('#username').text(),
											getInputsEDIT(), header, token);
								} else {
									validateError();
									$('#modaldemensagem').modal('show');
								}

							});

					$('.incluirAction').click(
							function(event) {
								event.preventDefault();
								validaISalvar();
								if ($("#fCadastro").valid()) {
									saveTokenEntityCategory(
											'../rest/entitycategory/' + $('#username').text(),
											getInputsINC(), header, token);
								} else {
									validateError();
									$('#modaldemensagem').modal('show');
								}
							});

				});