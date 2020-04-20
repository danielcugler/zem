var addWU = (function($, module) {
	var list = $('#list li').map(function() {
		return $(this).text();
	});
	// security rules
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	// cached DOM
	// general module
	var $generalModule = $("#generalModule");
	// Field Module
	var $formModule = $generalModule.find("#loginForm");
	var $inName = $formModule.find("#inName");
	var $inCpf = $formModule.find("#inCpf");
	var $inEmail = $formModule.find("#inEmail");
	// buttons
	var $btAdd = $formModule.find("#btAdd");
	// bind actions
	$btAdd.on('click', add);
	// modal
	var $modalE = $generalModule.find("#modalE");
	var $notificacoesE = $generalModule.find("#notificacoesE");
	// templates
	var $messageTpl = $("#message-template").html();
	// functions
	// validations
	jQuery("#inCpf")
	.mask("999.999.999-99")
	.focusout(
			function(event) {
				var target, cpf, element;
				target = (event.currentTarget) ? event.currentTarget
						: event.srcElement;
				cpf = target.value.replace(/\D/g, '');
				element = $(target);
				element.unmask();
				element.mask("999.999.999-99");
			});
// validaCPF
jQuery.validator
	.addMethod(
			"verificaCPF",
			function(value, element) {
				value = value.replace('.', '');
				value = value.replace('.', '');
				cpf = value.replace('-', '');
				while (cpf.length < 11)
					cpf = "0" + cpf;
				var expReg = /^0+$|^1+$|^2+$|^3+$|^4+$|^5+$|^6+$|^7+$|^8+$|^9+$/;
				var a = [];
				var b = new Number;
				var c = 11;
				for (i = 0; i < 11; i++) {
					a[i] = cpf.charAt(i);
					if (i < 9)
						b += (a[i] * --c);
				}
				if ((x = b % 11) < 2) {
					a[9] = 0
				} else {
					a[9] = 11 - x
				}
				b = 0;
				c = 11;
				for (y = 0; y < 10; y++)
					b += (a[y] * c--);
				if ((x = b % 11) < 2) {
					a[10] = 0;
				} else {
					a[10] = 11 - x;
				}
				if ((cpf.charAt(9) != a[9])
						|| (cpf.charAt(10) != a[10])
						|| cpf.match(expReg))
					return false;
				return true;
			});
$formModule
	.validate(
			{
				errorPlacement : function(error,
						element) {
					$(element).parent().addClass(
							"has-error");
					error.insertAfter(element);
					error.wrap("<p>");
					error.css('color', 'red');
				},
				rules : {
					inName : {
						maxlength : 100,
						required : true
					},
					inCpf : {
						maxlength : 14,
						required : true,
						verificaCPF : true
					},
					inEmail : {
						maxlength : 100,
						required : true,
						email : true
					}
				},
				messages : {
					inName : {
						required : "Insira o nome",
						maxlength : jQuery.validator
								.format("Por favor, insira menos que {0} caracteres.")
					},
					inCpf :  {
						required : "Insira o CPF",
						verificaCPF : "CPF inválido",
						maxlength : jQuery.validator
								.format("Por favor, insira menos que {0} caracteres.")
					},
					inEmail :  {
						required : "Insira o e-mail",
						email : "Insira um email válido.",
						maxlength : jQuery.validator
								.format("Por favor, insira menos que {0} caracteres.")
					}
				}
			});
	function getUrl(key) {
		var result = new RegExp(key + "=([^&]*)", "i")
				.exec(window.location.search);
		return result && unescape(result[1]) || "";
	}
	var setFields = function(name, cpf, email) {
		$inName.val(name);
		$inCpf.val(cpf);
		$inEmail.val(email);
	}
	var getFields = function() {
		var cpf=$inCpf.val();
		cpf = cpf.replace('.', '');
		cpf = cpf.replace('.', '');
		cpf = cpf.replace('-', '');
		return JSON.stringify({
			'name' : $inName.val(),
			'citizen_cpf' : cpf,
			'email' : $inEmail.val(),
			'enabled' : 0
		});
	}
	function add(event) {
		event.preventDefault();
		if ($formModule.valid()) {
			$.ajax({
				type : 'POST',
				url : '../rest/citizen2/webuser',
				dataType : "json",
				data : getFields(),
				beforeSend : function(xhr) {
					xhr.setRequestHeader(header, token);
				},
				contentType : "application/json"
			}).done(
					function(data) {
							window.location.href = '../newcall.jsp?cpf='+data.citizen_cpf+"&email="+data.email+"&name="+data.name;
					}).fail(restError);
			return true;
		}
		modal("Chamado interno",
				"Por favor, preencha os campos destacados em vermelho!", 3);
		return false;
	}
	// render
	function _render(baseTemplate, selector, data) {
		var template = Handlebars.compile(baseTemplate);
		var html = template(data);
		selector.html(html);
	}
	// functions
	// op: 1 sucesso, 2 erro de rest, 3 erro de validação
	function modal(title, body, op) {
		_render($messageTpl, $notificacoesE, {
			'title' : title,
			'body' : body
		});
		if (op == 1)
			$notificacoesE.find("#bOk1").show();
		else if (op == 2)
			$notificacoesE.find("#bOk2").show();
		else
			$notificacoesE.find("#bOk3").show();
		$modalE.modal('show');
	}

		function restError(jqXHR, textStatus) {
		var body = jqXHR.responseJSON.errorCode + ", "
				+ jqXHR.responseJSON.errorMessage;
		modal("Categoria de Entidade", body, 2);
	}
	module.setFields = setFields;
	module.getFields = getFields;
	module.modal = modal;
	module.getUrl = getUrl;
	return module;
})(jQuery, addWU || {});

/*
var str1 = "Chamado Publico";
var str2 = "Chamado Publico";
var str3 = "o";
var resizefunc = [];

function loadInputs(data) {
	data.jsonList = JSON.parse(data.jsonList);
	$('#cName').val(data.jsonList.nameWebUser);
	$('#cCpf').val(data.jsonList.cpfWebUser);
	$('#cEmail').val(data.jsonList.observationWebUser);
}

$(document)
		.ready(
				function() {

					combo('../rest/entitycategory',
							ModelsTmpl.tmplComboEntityCategory, "#comboEntity");

					jQuery("#cCpf")
							.mask("999.999.999-99")
							.focusout(
									function(event) {
										var target, cpf, element;
										target = (event.currentTarget) ? event.currentTarget
												: event.srcElement;
										cpf = target.value.replace(/\D/g, '');
										element = $(target);
										element.unmask();
										element.mask("999.999.999-99");
									});

					// validaCPF
					jQuery.validator
							.addMethod(
									"verificaCPF",
									function(value, element) {
										value = value.replace('.', '');
										value = value.replace('.', '');
										cpf = value.replace('-', '');
										while (cpf.length < 11)
											cpf = "0" + cpf;
										var expReg = /^0+$|^1+$|^2+$|^3+$|^4+$|^5+$|^6+$|^7+$|^8+$|^9+$/;
										var a = [];
										var b = new Number;
										var c = 11;
										for (i = 0; i < 11; i++) {
											a[i] = cpf.charAt(i);
											if (i < 9)
												b += (a[i] * --c);
										}
										if ((x = b % 11) < 2) {
											a[9] = 0
										} else {
											a[9] = 11 - x
										}
										b = 0;
										c = 11;
										for (y = 0; y < 10; y++)
											b += (a[y] * c--);
										if ((x = b % 11) < 2) {
											a[10] = 0;
										} else {
											a[10] = 11 - x;
										}
										if ((cpf.charAt(9) != a[9])
												|| (cpf.charAt(10) != a[10])
												|| cpf.match(expReg))
											return false;
										return true;
									});

					$('#loginForm')
							.validate(
									{
										errorPlacement : function(error,
												element) {
											$(element).parent().addClass(
													"has-error");
											error.insertAfter(element);
											error.wrap("<p>");
											error.css('color', 'red');
										},
										rules : {
											inName : {
												maxlength : 100,
												required : true
											},
											inCpf : {
												maxlength : 11,
												required : true,
												verificaCPF : true
											},
											inEmail : {
												maxlength : 100,
												required : true,
												email : true
											}
										},
										messages : {
											inName : {
												required : "Insira o nome",
												maxlength : jQuery.validator
														.format("Por favor, insira menos que {0} caracteres.")
											},
											inCpf :  {
												required : "Insira o CPF",
												verificaCPF : "CPF inválido",
												maxlength : jQuery.validator
														.format("Por favor, insira menos que {0} caracteres.")
											},
											inEmail :  {
												required : "Insira o e-mail",
												email : "Insira um email válido.",
												maxlength : jQuery.validator
														.format("Por favor, insira menos que {0} caracteres.")
											}
										}
									});

	
					var geturl = function getUrlVar(key) {
						var result = new RegExp(key + "=([^&]*)", "i")
								.exec(window.location.search);
						return result && unescape(result[1]) || "";
					}

					var internalcall = geturl('useridentification');

					function add(citizen) {
						event.preventDefault();
						if ($formModule.valid()) {
							$
									.ajax(
											{
												type : 'POST',
												url : '../rest/citizen2/'
														+ $('#username').text(),
												dataType : "json",
												data : getFileds(),
												beforeSend : function(xhr) {
													xhr.setRequestHeader(
															header, token);
												},
												contentType : "application/json"
											})
									.done(
											function(data) {
												modal(
														"Usuário",
														"Usuário inserido com sucesso!",
														1);
											}).fail(restError);
							return true;
						}
						modal(
								"usuário",
								"Por favor, preencha os campos destacados em vermelho!",
								3);
						return false;
					}

					$("#save")
							.click(
									function() {
										validaISalvar();

										if ($("#loginForm").valid()) {

											var citizen = {
												'name' : $("#inName").val(),
												'email' : $("#inEmail").val(),
												'citizenCpf' : $("#inCpf").val(),
												'enabled' : 0
											};

											if (internalcall == 2) {
												window.location = '../newcall.jsp'
														+ '/?name='
														+ $("#cName").val()
														+ '&email='
														+ $("#cEmail").val()
														+ '&cpf='
														+ $("#cCpf").val()
														+ '&useridentification='
														+ internalcall;

											} else if (internalcall == 1) {
												window.location = '../addinternalcall.jsp'
														+ '/?name='
														+ $("#cName").val()
														+ '&email='
														+ $("#cEmail").val()
														+ '&cpf='
														+ $("#cCpf").val()
														+ '&useridentification='
														+ internalcall;
											}
										}
									});

				});
*/