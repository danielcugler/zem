var addSU = (function($, module) {
	// photo

	var jcrop_api;
	var canvas;
	$("#upload")
			.fileinput(
					{
						overwriteInitial : true,
						maxFileSize : 1500,
						showClose : false,
						showCaption : false,
						browseLabel : '',
						removeLabel : '',
						browseIcon : '<i class="glyphicon glyphicon-folder-open"></i>',
						removeIcon : '<i class="glyphicon glyphicon-remove"></i>',
						removeTitle : 'Cancel or reset changes',
						elErrorContainer : '#kv-avatar-errors-1',
						msgErrorClass : 'alert alert-block alert-danger',
						defaultPreviewContent : '<img src="/images/user.png" alt="" style="width:160px">',
						layoutTemplates : {
							main2 : '{preview} {remove} {browse}'
						},
						allowedFileExtensions : [ "jpg", "png", "gif" ]
					});

	$('#btnCrop').click(function() {
		var x1 = $('#imgX1').val();
		var y1 = $('#imgY1').val();
		var width = $('#imgWidth').val();
		var height = $('#imgHeight').val();
		canvas = $("#canvas")[0];
		var context = canvas.getContext('2d');
		// context.clearRect(0, 0, canvas.width, canvas.height);
		var img = new Image();
		img.onload = function() {
			canvas.height = height;
			canvas.width = width;
			context.drawImage(img, x1, y1, width, height, 0, 0, width, height);
		};
		img.src = $('#Image1').attr("src");

	});
	
	$('#btnRemove').click(function() {
		$('#crop-modal').modal('hide');
		$("#upload").val("");
		JcropAPI = $('#Image1').data('Jcrop');
		JcropAPI.destroy();
	});
	
	$('#fecharModal').click(function() {
		$('#crop-modal').modal('hide');
		$("#upload").val("");
		JcropAPI = $('#Image1').data('Jcrop');
		JcropAPI.destroy();
	});

	$('#send').click(function() {
		imgBase64 = canvas.toDataURL();
		$("#imgUser").attr("src", imgBase64);
		$("#Image1").attr("src", "");
		$('#crop-modal').modal('hide');
		// jcrop_api.destroy();
		JcropAPI = $('#Image1').data('Jcrop');
		JcropAPI.destroy();
	});	

	function SetCoordinates(c) {
		$('#imgX1').val(c.x);
		$('#imgY1').val(c.y);
		$('#imgWidth').val(c.w);
		$('#imgHeight').val(c.h);
	}
	;
	function checkFile() {
		var fileElement = document.getElementById("imgCropped");
		if ($('#imgCropped')[0].files.length == 0) {
			$('#error').show();
			$('#error').html("Please select file!")
			return false;
		}
		var fileSize = $('#imgCropped')[0].files[0].size
		var fileExtension = "";
		if (fileElement.value.lastIndexOf(".") > 0) {
			fileExtension = fileElement.value.substring(fileElement.value
					.lastIndexOf(".") + 1, fileElement.value.length);
		}
		if ((fileExtension.toLowerCase() == "png" || fileExtension
				.toLowerCase() == "png")
				&& (fileSize < 2000000)) {
			return true;
		} else {
			$('#error').show();
			$('#error').html("Unacceptable image file, please try again!")
			return false;
		}
	}

	// end photo

	var list = $('#list li').map(function() {
		return $(this).text();
	});
	// security rules
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	var roleEdit = $.inArray('ROLE_USER_UPDATE', list) >= 0;
	// cached DOM
	// general module
	var $generalModule = $("#generalModule");
	//title
	var $title = $generalModule.find("#title2");
	// Field Module
	var $formModule = $generalModule.find("#formUsuario");
	var $userImg = $formModule.find("#imgUser");
	var $inName = $formModule.find('#inName');
	var $inEmail = $formModule.find('#inEmail');
	var $foto = $formModule.find('#foto');
	var $inFone = $formModule.find('#inFone');
	var $inFoneP = $formModule.find('#inFoneP');
	var $inSector = $formModule.find('#inSetor');
	var $inCargo = $formModule.find('#inCargo');
	var $inUsername = $formModule.find('#inUsername');
	var $inEnabled = $formModule.find('#inEnabled');
	var $inPass = $formModule.find('#inPass');
	var $inConfirm = $formModule.find('#inConfirm');
	var $selectEntity = $formModule.find('#selectEntity');
	var $selectPerfil = $formModule.find('#selectPerfil');
	var $upload = $formModule.find('#upload');
	var $camposSenha = $formModule.find('.camposSenha');
	// buttons
	var $buttonModule = $formModule.find("#botoes");
	var $btAdd = $buttonModule.find("#btAdd");
	var $btEdit = $buttonModule.find("#btEdit");
	// bind actions
	$btAdd.on('click', add);
	$btEdit.on('click', edit);
	$foto.on('change', $upload, function(event) {
		event.preventDefault();
		if ($("#upload")[0].files[0] == undefined)
			return;
		$cropModal.modal('show');
		$('#Image1').hide();
		$("#Image1").attr("src", "");
		if ($('#upload')[0].files[0].type == "image/jpeg") {
			$('#btnCrop').show();
		}
		$("#btn")
		$('#error').hide();
		var reader = new FileReader();
		reader.onload = function(e) {
			var tbox = $("#crop-modal").width() * 0.35;
			$('#Image1').show();
			$('#Image1').attr("src", e.target.result);
			$('#Image1').Jcrop({
				allowResize : false,
				allowSelect : false,
				boxWidth : tbox,
				boxHeight : 400,
				onChange : SetCoordinates,
				onSelect : SetCoordinates,
				aspectRatio : 4 / 4,
				setSelect : [ 100, 100, 350, 350 ],
				bgFade : true,
				bgOpacity : .3
			}, function() {
				jcrop_api = $("#upload")[0];
			});
		}
		reader.readAsDataURL($("#upload")[0].files[0]);
	});
	// modal
	var $modalE = $generalModule.find("#modalE");
	var $notificacoesE = $generalModule.find("#notificacoesE");
	// templates
	var $messageTpl = $("#message-template").html();
	var $comboEntTpl = $("#combo-ent-template").html();
	var $comboSupTpl = $("#combo-sup-template").html();
	// modal crop
	var $cropModal = $generalModule.find('#crop-modal');
	// functions

	// render
	function _render(baseTemplate, selector, data) {
		var template = Handlebars.compile(baseTemplate);
		var html = template(data);
		selector.html(html);
	}
	// validations
	jQuery.validator.addMethod('mypassword', function(value, element) {
		var bool = this.optional(element)
				|| (value.match(/[a-zA-Z]/) && value.match(/[0-9]/));
		return bool;

		// !(this.optional(element) ||
		// (value.match(/[a-z].*[0-9]|[0-9].*[a-z]/i)))[0]);
	}, 'A senha deve conter letras e números.');
	$formModule.validate({

		errorPlacement : function(error, element) {
			$(element).parent().addClass("has-error");
			error.insertAfter(element);
			error.wrap("<p>");
			error.css('color', 'red');
		},
		rules : {
			inName : {
				required : true,
				maxlength : 100
			},
			inEmail : {
				required : true,
				email : true,
				maxlength : 100
			},
			inFone : {
				required : true
			},
			inCargo : {
				required : true,
				maxlength : 100
			},
			inUsername : {
				required : true,
				minlength : 3,
				maxlength : 20
			},
			inPass : {
				mypassword : true
			},
			inConfirm : {
				equalTo : "#inPass"
			},
			selectPerfil : {
				required : true
			}, 
			selectEntity : {
				required : true
			} 

		},
		messages : {
			inName : {
				required : "Insira o Nome",				
				maxlength : jQuery.validator
						.format("Por favor insira menos  que {0} caracteres.")
			},
			inEmail : {
				required : "Insira o Email",
				email : "Insira um e-mail válido.",
				maxlength : jQuery.validator
						.format("Por  favor insira menos que {0} caracteres.")
			},
			inFone : {
				required : "Insira o telefone comercial",
			},
			inCargo : {
				required : "Insira o Cargo",
				maxlength : jQuery.validator
						.format("Por  favor insira menos que {0} caracteres.")
			},
			inUsername : {
				required : "Por favor, insira o login.",
				minlength : jQuery.validator
						.format("Por favor, insira mais que {0} caracteres."),
				maxlength : jQuery.validator
						.format("Por favor, insira menos que {0} caracteres.")
			},
			inConfirm : {
				equalTo : "Senha digitada diferente!"
			},
			selectPerfil : {
				required : "Selecione um perfil"
			},
			selectEntity : {
				required : "Selecione uma opção de entidade"
			},
			inPass : {
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
	var changeButton = function() {
		$btAdd.hide();
		$btEdit.show();
		$camposSenha.hide();
		$inUsername.attr("disabled",true);
	}
	var setFields = function(user2) {
		$("#imgUser").attr("src", user2.photoPath || 'images/user.jpg');
		$inName.val(user2.name);
		$inEmail.val(user2.email);
		$inFone.val(user2.commercialPhone).mask("(99) 9999-9999?9");
		$inFoneP.val(user2.personalPhone).mask("(99) 9999-9999?9");
		$inSector.val(user2.sector);
		$inCargo.val(user2.jobPosition);
		$inUsername.val(user2.systemUserUsername);
		$inEnabled.val(user2.enabled);
		// $inPass.val(user.password);
		if(user2.worksAtEntity != null)
			$selectEntity.val(user2.worksAtEntity.entityId);
		else
			$selectEntity.val(-1);
		$selectPerfil.val(user2.systemUserProfileId.systemUserProfileId);
	}
	var getFields = function() {
		var entidade = $selectEntity.val();
		if(entidade < 0) entidade = "";
		
//		console.log("Senha com MD5 no front-end")
//		console.log($.md5($inPass.val()));
		
		return JSON.stringify({
			'name' : $inName.val(),
			'email' : $inEmail.val(),
			'commercialPhone' : $inFone.val().replace(/[^0-9]+/g, ''),
			'personalPhone' : $inFoneP.val().replace(/[^0-9]+/g, ''),
			'sector' : $inSector.val(),
			'jobPosition' : $inCargo.val(),
			'systemUserUsername' : $inUsername.val(),
			'enabled' : $inEnabled.val(),
			'password' : $.md5($inPass.val()),
			'worksAtEntity' : {
				'entityId' : entidade
			},
			'systemUserProfileId' : {
				'systemUserProfileId' : $selectPerfil.val()
			},
			'photoPath' : ($("#imgUser").prop('src')).split(",", 2)[1]
		});
	}

	function makeCombos() {
		var promises = [];
		var promise = $.ajax({
			type : 'GET',
			url : "../rest/systemuserprofile2/combosativos",
			dataType : "json",
			beforeSend : function(xhr) {
				xhr.setRequestHeader(header, token);
			},
			contentType : "application/json"
		}).done(function(data) {
			_render($comboSupTpl, $selectPerfil, data);
		}).fail(restError);
		var promise2 = $.ajax({
			type : 'GET',
			url : "../rest/entity2/combosativos",
			dataType : "json",
			beforeSend : function(xhr) {
				xhr.setRequestHeader(header, token);
			},
			contentType : "application/json"
		}).done(function(data) {
			_render($comboEntTpl, $selectEntity, data);
		}).fail(restError);
		promises.push(promise);
		promises.push(promise2);
		return promises;
	}
	function restError(jqXHR, textStatus) {
		var body = jqXHR.responseJSON.errorCode + ", "
				+ jqXHR.responseJSON.errorMessage;
		modal("Modelo de Mensagem", body, 2);
	}

	function loadUser(username) {
		var promises = [];
		var promise1 = $.ajax({
			type : 'GET',
			url : "../rest/systemuserprofile2/combosativos",
			dataType : "json",
			beforeSend : function(xhr) {
				xhr.setRequestHeader(header, token);
			},
			contentType : "application/json"
		}).fail(restError);
		var promise2 = $.ajax({
			type : 'GET',
			url : "../rest/entity2/combos",
			dataType : "json",
			beforeSend : function(xhr) {
				xhr.setRequestHeader(header, token);
			},
			contentType : "application/json"
		}).fail(restError);
		var promise3 = $.ajax({
			type : 'GET',
			url : '../rest/systemuser2/' + username,
			dataType : "json",
			contentType : "application/json"
		}).fail(restError);
		promises.push(promise1);
		promises.push(promise2);
		promises.push(promise3);

		$.when.apply($, promises).done(function(resp1, resp2, resp3) {
			_render($comboSupTpl, $selectPerfil, resp1[0]);
			_render($comboEntTpl, $selectEntity, resp2[0]);
			setFields(resp3[0]);
		});
	}

	function init() {
		$inFone.focusout(function() {
			var phone, element;
			element = $(this);
			element.unmask();
			phone = element.val().replace(/\D/g, '');
			if (phone.length > 10) {
				element.mask("(99) 99999-999?9");
			} else {
				element.mask("(99) 9999-9999?9");
			}
		}).trigger('focusout');
		$inFoneP.focusout(function() {
			var phone, element;
			element = $(this);
			element.unmask();
			phone = element.val().replace(/\D/g, '');
			if (phone.length > 10) {
				element.mask("(99) 99999-999?9");
			} else {
				element.mask("(99) 9999-9999?9");
			}
		}).trigger('focusout');

		$inEnabled.val('0');
		var username = getUrl('id');
		if (username) {
			document.title = "Edição de Usuário";
			$title.text("").append("Edição de <strong>Usuário</strong>");
			changeButton();
			loadUser(username);
		} else
			makeCombos();
	}

	function edit(event) {
		event.preventDefault();
		if ($formModule.valid()) {
			$.ajax({
				type : 'PUT',
				url : '../rest/systemuser2/',
				dataType : "json",
				data : getFields(),
				beforeSend : function(xhr) {
					xhr.setRequestHeader(header, token);
				},
				contentType : "application/json"
			}).done(function(data) {
				modal("Usuários", data.message, 1);
			}).fail(restError);

			return true;
		}
		modal("Usuários",
				"Por favor, preencha os campos  destacados em vermelho!", 3);
		return false;
	}

	function add(event) {
		event.preventDefault();
		if ($formModule.valid()) {
			$.ajax({
				type : 'POST',
				url : '../rest/systemuser2/',
				dataType : "json",
				data : getFields(),
				beforeSend : function(xhr) {
					xhr.setRequestHeader(header, token);
				},
				contentType : "application/json"
			}).done(function(data) {
				modal("Usuários", data.message, 1);
			}).fail(restError);
			return true;
		}
		modal("Usuários",
				"Por favor, preencha os campos destacados em vermelho!", 3);
		return false;
	}
	// functions // op: 1 sucesso, 2 erro de rest, 3 erro de validação
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

	/*
	 * $upload.fileinput({ initialPreview: [ '/images/user.png' ],
	 * initialPreviewAsData: true, initialPreviewConfig: [ {caption: "user.png",
	 * size: 930321, width: "120px", key: 1} ], overwriteInitial: true,
	 * showRemove: true, showUpload: false, // <------ just set this from true
	 * to false showCancel: false, showCaption: false });
	 */

	init();

	module.changeButton = changeButton;
	module.setFields = setFields;
	module.getFields = getFields;
	module.getUrl = getUrl;
	return module;
})(jQuery, addSU || {});

