var ctrlNCin = (function($, module) {
	// security rules
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	// cached DOM
	// general module
	var $generalModule = $("#generalModule");
	var $formModule = $generalModule.find("#formModule");
	var $selectCallClassification = $formModule
			.find("#selectCallClassification");
	var $selectNeighborhood = $formModule.find("#selectNeighborhood");
	var $selectEntity = $formModule.find("#selectEntity");
	var $selectEntityCategory = $formModule.find("#selectEntityCategory");
	var $inDescription = $formModule.find("#inDescription");
	var $inStreet = $formModule.find("#inStreet");
	var $inNumber = $formModule.find("#inNumber");
	var $inComplement = $formModule.find("#inComplement");
	var $ckAnonymity = $formModule.find("#ckAnonymity");
	var $divAnonymity = $formModule.find("#divAnonymity");
	var $ckNomidia = $formModule.find("#ckNomidia");
	var $upload1 = $formModule.find("#upload1");
	var $upload2 = $formModule.find("#upload2");
	var $upload3 = $formModule.find("#upload3");
	// buttons
	var $btSave = $formModule.find("#save");
	// modal
	var $modalE = $generalModule.find("#modalE");
	var $notificacoesE = $generalModule.find("#notificacoesE");
	// templates
	var $messageTplE = $("#message-template").html();
	var $messageTplS = $("#success-template").html();
	var $messageSuccessMail = $("#success-mail-template").html();
	var $comboCcTpl = $("#combo-cc-template").html();
	var $comboNbTpl = $("#combo-nb-template").html();
	var $comboEnTpl = $("#combo-en-template").html();
	var $comboEcTpl = $("#combo-ec-template").html();
	var citizen = null;
	// bind events
	$('.file-input-wraper').click(function(event) {
		console.log("click");
		  $(this).find('input').click();
		});
	$btSave.on('click', save);
	$selectEntity.change(function() {
		$selectEntityCategory.prop("disabled", false);
		$.ajax({
			type : 'GET',
			url : "../rest/entitycategory2/en/" + $selectEntity.val(),
			dataType : "json",
			contentType : "application/json"
		}).done(function(resp, status, xhr) {
			_render($comboEcTpl, $selectEntityCategory, resp);
		}).fail(function(jqXHR, textStatus) {
			var title = "Chamado";
			var body = jqXHR.responseJSON.errorCode + ", "
					+ jqXHR.responseJSON.errorMessage;
			modal(title, body,3);
		});		
	});
	// render
	function _render(baseTemplate, selector, data) {
		var template = Handlebars.compile(baseTemplate);
		var html = template(data);
		selector.html(html);
	}
	// functions
	function modal(title, body,op, $messageTpl) {
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
				$modalE.modal({
			show : true,
			backdrop : true
		});
	}
	function modalED(title, body) {
		_render($edMessageTpl, $notificacoesE, {
			'title' : title,
			'body' : body
		});
	}

	// Validação
	$formModule.validate({
		errorPlacement : function(error, element) {
			$(element).parent().addClass("has-error");
			error.insertAfter(element);
			error.wrap("<p>");
			error.css('color', 'red');
		},
		rules : {
			inDescription : {
				minlength : 10,
				maxlength : 1000,
				required : true
			},
			inStreet : {
				maxlength : 100,
				required : true
			},
			inNumber : {
				maxlength : 5,
				number : true,
				required : true
			},
			inComplement : {
				maxlength : 15,
			},
			selectNeighborhood : {
				required : true
			},
			selectEntity : {
				required : true
			},
			selectEntityCategory : {
				required : true
			},
			selectCallClassification : {
				required : true
			}

		},
		messages : {
			inDescription : {
				required : "Insira a descrição",
				minlength : jQuery.validator
						.format("Por favor, insira mais que {0} caracteres."),
				maxlength : jQuery.validator
						.format("Por favor, insira menos que {0} caracteres.")
			},
			inStreet : {
				required : "Insira a Rua",
				maxlength : jQuery.validator
						.format("Por favor, insira menos que {0} caracteres.")
			},
			inNumber : {
				required : "Insira o numero",
				number : "Este campo aceita somente números",
				maxlength : jQuery.validator
						.format("Por favor, insira menos que {0} caracteres.")
			},
			inComplement : {
				maxlength : jQuery.validator
						.format("Por favor, insira menos que {0} caracteres.")
			},
			selectNeighborhood : {
				required : "Por favor, selecione um bairro."
			},
			selectEntity : {
				required : "Por favor, selecione uma entidade."
			},
			selectEntityCategory : {
				required : "Por favor, selecione uma categoria de entidade."
			},
			selectCallClassification : {
				required : "Por favor, selecione um classificação."
			}
		}
	});
	
	// Validação das fotos
	function verificaFotos() {
		if (!$ckNomidia.parent().hasClass("checked")) {
			if (upload1Base64 == null && upload2Base64 == null
					&& upload3Base64 == null) {
				return false;
			}
		}
		return true;
	}

	function getUrlParam(key) {
		var result = new RegExp(key + "=([^&]*)", "i")
				.exec(window.location.search);
		return result && unescape(result[1]) || "";
	}

	function makeCombos() {
		$.ajax({
			type : 'GET',
			url : "../rest/unsolvedcall2/nc/" + 3551,
			dataType : "json",
			contentType : "application/json"
		}).done(function(resp, status, xhr) {
			_render($comboEnTpl, $selectEntity, resp.en);
// _render($comboEcTpl, $selectEntityCategory, resp.ec);
			_render($comboCcTpl, $selectCallClassification, resp.cc);
			_render($comboNbTpl, $selectNeighborhood, resp.nb);
		}).fail(function(jqXHR, textStatus) {
			var title = "Chamado";
			var body = jqXHR.responseJSON.errorCode + ", "
					+ jqXHR.responseJSON.errorMessage;
			modal(title, body,3, $messageTplE);
		});
	}

	function getFields() {
		var checked = $("#addressRequired").parent('[class*="icheckbox"]').hasClass("checked");
		console.log(checked);
		
		if(checked){
			var unsolvedCall = {
					'description' : {
						'information' : $inDescription.val()
					},
					'entityEntityCategoryMaps' : {
						'entityEntityCategoryMapsPK' : {
							'entityCategoryId' : $selectEntityCategory.find(
									'option:selected').val()
									|| null,
							'entityId' : $selectEntity.find('option:selected').val()
												|| null
									
						}
					},
					'callClassificationId' : {
						'callClassificationId' : $selectCallClassification.find(
								'option:selected').val()
								|| null
					},
					'anonymity' : $ckAnonymity.is(':checked') ? 1 : 0,
					'noMidia' : $ckNomidia.is(':checked') ? 1 : 0			
				};
		} else {
			var unsolvedCall = {
					'description' : {
						'information' : $inDescription.val()
					},
					'addressId' : {
						'streetName' : $inStreet.val(),
						'addressNumber' : $inNumber.val(),
						'complement' : $inComplement.val(),
						'neighborhoodId' : {
							'neighborhoodId' : $selectNeighborhood.find(
									'option:selected').val()
									|| null
						}
					},
					'entityEntityCategoryMaps' : {
						'entityEntityCategoryMapsPK' : {
							'entityCategoryId' : $selectEntityCategory.find(
									'option:selected').val()
									|| null,
							'entityId' : $selectEntity.find('option:selected').val()
												|| null
									
						}
					},
					'callClassificationId' : {
						'callClassificationId' : $selectCallClassification.find(
								'option:selected').val()
								|| null
					},
					'anonymity' : $ckAnonymity.is(':checked') ? 1 : 0,
					'noMidia' : $ckNomidia.is(':checked') ? 1 : 0			
				};
		}
		
		if(citizen!=null && !$ckAnonymity.is(':checked'))
		unsolvedCall.citizenCpf = citizen;
		var mediasPath = [];
		if (!$ckNomidia.is(':checked')) {
			if (upload1Base64 != null)
				mediasPath.push(upload1Base64.split(',')[1]);
			if (upload2Base64 != null)
				mediasPath.push(upload2Base64.split(',')[1]);
			if (upload3Base64 != null)
				mediasPath.push(upload3Base64.split(',')[1]);
			unsolvedCall.mediasPath = mediasPath;
		}
		var user = $("#username").text();
		if (user)
			unsolvedCall.updatedOrModeratedBy = user;
		return JSON.stringify(unsolvedCall);
	}

	function setFilters(description, street, number, complement, anonymity,
			nomidia, entity, entityCategory, neighborhood, callClassification) {
		$selectEntity.selectpicker('val', entity);
		$selectEntityCategory.selectpicker('val', entityCategory);
		$selectNeighborhood.selectpicker('val', neighborhood);
		$selectCallClassification.selectpicker('val', callClassification);
		var description = $inDescription.val();
		var street = $inStreet.val();
		var number = $inNumber.val();
		var complement = $inComplement.val();
		var anonymity = $ckAnonymity.is(':checked') ? 0 : 1;
		var nomidia = $ckNomidia.is(':checked') ? 0 : 1;
	}

	// imagens
	var upload1Base64 = null;
	var upload2Base64 = null;
	var upload3Base64 = null;
	var base64 = null;
	$('#ckNomidia').on('ifUnchecked', function(event) {
		$("#upload").children().prop('disabled', false);
	});
	$('#ckNomidia').on('ifChecked', function(event) {
		$("#upload").children().prop('disabled', true);
	});

	function changeImg(button) {
		var countFiles = $(button)[0].files.length;
		var buttonFile = $(button);
		var imgPath = $(button)[0].value;
		var extn = imgPath.substring(imgPath.lastIndexOf('.') + 1)
				.toLowerCase();
		var image_holder = $("#image-holder");
		image_holder.empty();
		if (extn == "gif" || extn == "png" || extn == "jpg" || extn == "jpeg") {
			if (typeof (FileReader) != "undefined") {
				for (var i = 0; i < countFiles; i++) {
					var reader = new FileReader();
					reader.onload = function(e) {
						var img = $(buttonFile).siblings("#imgUser");
						
						// /resize
					    var img1 = new Image;
					    img.onload = function() {
					        var canvas=document.createElement("canvas");
					        var ctx=canvas.getContext("2d");
					        var iw=img.width;
					        var ih=img.height;
					        var scale=Math.min((500/iw),(500/ih));
					        var iwScaled=iw*scale;
					        var ihScaled=ih*scale;
					        canvas.width=iwScaled;
					        canvas.height=ihScaled;
					        ctx.drawImage(img1,0,0,iwScaled,ihScaled);
							$(img).attr("src", canvas.toDataURL());
							$(img).addClass("thumb-image");					        
					        }
					    img1.src = URL.createObjectURL(e.target.result);
						// resize
						

						base64 = $(img).attr("src");
					}
					image_holder.show();
					reader.readAsDataURL($(button)[0].files[i]);
				}

			} else {
				alert("Este navegador não suporta FileReader.");
			}
		} else {
			alert("Por favor, selecione apenas imagens.");
		}
	}

	$("#clean1").click(function() {
		$("#upload1").val("");
		$(".img1 #imgUser").attr("src", "http://i.imgur.com/SZuRQS4.png");
		upload1Base64 = null;
	});

	$("#clean2").click(function() {
		$("#upload2").val("");
		$(".img2 #imgUser").attr("src", "http://i.imgur.com/SZuRQS4.png");
		upload2Base64 = null;
	});

	$("#clean3").click(function() {
		$("#upload3").val("");
		$(".img3 #imgUser").attr("src", "http://i.imgur.com/SZuRQS4.png");
		upload3Base64 = null;
	});
	
	
	
	function resize(image){
		  // Create an image
		var maxW=500;
		var maxH=500;
		var img = document.createElement("img");			
		img.src = image;
	      var canvas=document.createElement("canvas");
	        var ctx=canvas.getContext("2d");
	        var iw=img.width;
	        var ih=img.height;
	        var scale=Math.min((maxW/iw),(maxH/ih));
	        var iwScaled=iw*scale;
	        var ihScaled=ih*scale;
	        canvas.width=iwScaled;
	        canvas.height=ihScaled;
	        ctx.drawImage(img,0,0,iwScaled,ihScaled);
	        var thumb=new Image();
	        return canvas.toDataURL();
	        
	}


	// var $up1=$("input:file");
	// $up1.change(function(){alert('change')});
	// $('#fileUpload').change(function(){alert('change')});
	$upload1.on('change', function() {
		var countFiles = $(this)[0].files.length;
		var buttonFile = $(this);
		var imgPath = $(this)[0].value;
		var extn = imgPath.substring(imgPath.lastIndexOf('.') + 1)
				.toLowerCase();
		var image_holder = $("#image-holder");
		image_holder.empty();
		if (extn == "gif" || extn == "png" || extn == "jpg" || extn == "jpeg") {
			if (typeof (FileReader) != "undefined") {
				for (var i = 0; i < countFiles; i++) {
					var reader = new FileReader();
					reader.onload = function(e) {
						var img = $(buttonFile).siblings("#imgUser");


						// resize
					    var img1 = new Image();
					    img1.onload = function() {
					        
					    	var canvas=document.createElement("canvas");
					        var ctx=canvas.getContext("2d");							        
					        var iw=img1.width;
					        var ih=img1.height;
					        var scale=Math.min((500/iw),(500/ih));
					        if(iw>500 && ih>500){
					        var iwScaled=iw*scale;
					        var ihScaled=ih*scale;
					        canvas.width=iwScaled;
					        canvas.height=ihScaled;
					        ctx.drawImage(img1,0,0,iwScaled,ihScaled);
							$(img).attr("src", canvas.toDataURL());
							$(img).addClass("thumb-image");
					        }
					        else{
					        	$(img).attr("src", e.target.result);
								$(img).addClass("thumb-image");								        
					        }
					        upload1Base64 = $(img).attr("src");
					        }
					    img1.src = e.target.result;
						// end resize
						// $(img).attr("src", e.target.result);
						
					}
					image_holder.show();
					reader.readAsDataURL($(this)[0].files[i]);
				}

			} else {
				// alert("Este navegador não suporta FileReader.");
			}
		} else {
			// alert("Por favor, selecione apenas imagens.");
		}
	});
	$upload1.trigger('change');
	$("#upload2").on(
			'change',
			function() {
				var countFiles = $(this)[0].files.length;
				var buttonFile = $(this);
				var imgPath = $(this)[0].value;
				var extn = imgPath.substring(imgPath.lastIndexOf('.') + 1)
						.toLowerCase();
				var image_holder = $("#image-holder");
				image_holder.empty();
				if (extn == "gif" || extn == "png" || extn == "jpg"
						|| extn == "jpeg") {
					if (typeof (FileReader) != "undefined") {
						for (var i = 0; i < countFiles; i++) {
							var reader = new FileReader();
							reader.onload = function(e) {
								var img = $(buttonFile).siblings("#imgUser");
								// resize
								// /resize
							    var img1 = new Image();
							    img1.onload = function() {
							        
							    	var canvas=document.createElement("canvas");
							        var ctx=canvas.getContext("2d");							        
							        var iw=img1.width;
							        var ih=img1.height;
							        var scale=Math.min((500/iw),(500/ih));
							        if(iw>500 && ih>500){
							        var iwScaled=iw*scale;
							        var ihScaled=ih*scale;
							        canvas.width=iwScaled;
							        canvas.height=ihScaled;
							        ctx.drawImage(img1,0,0,iwScaled,ihScaled);
									$(img).attr("src", canvas.toDataURL());
									$(img).addClass("thumb-image");
							        }
							        else{
							        	$(img).attr("src", e.target.result);
										$(img).addClass("thumb-image");								        
							        }
							        upload2Base64 = $(img).attr("src");
							        }
							    img1.src = e.target.result;
								// end resize
															
							}
							image_holder.show();
							reader.readAsDataURL($(this)[0].files[i]);
						}

					} else {
						alert("Este navegador não suporta FileReader.");
					}
				} else {
					alert("Por favor, selecione apenas imagens.");
				}
			});
	$("#upload3").on(
			'change',
			function() {
				var countFiles = $(this)[0].files.length;
				var buttonFile = $(this);
				var imgPath = $(this)[0].value;
				var extn = imgPath.substring(imgPath.lastIndexOf('.') + 1)
						.toLowerCase();
				var image_holder = $("#image-holder");
				image_holder.empty();
				if (extn == "gif" || extn == "png" || extn == "jpg"
						|| extn == "jpeg") {
					if (typeof (FileReader) != "undefined") {
						for (var i = 0; i < countFiles; i++) {
							var reader = new FileReader();
							reader.onload = function(e) {
								var img = $(buttonFile).siblings("#imgUser");

								// /resize
							    var img1 = new Image();
							    img1.onload = function() {
							        
							    	var canvas=document.createElement("canvas");
							        var ctx=canvas.getContext("2d");							        
							        var iw=img1.width;
							        var ih=img1.height;
							        var scale=Math.min((500/iw),(500/ih));
							        if(iw>500 && ih>500){
							        var iwScaled=iw*scale;
							        var ihScaled=ih*scale;
							        canvas.width=iwScaled;
							        canvas.height=ihScaled;
							        ctx.drawImage(img1,0,0,iwScaled,ihScaled);
									$(img).attr("src", canvas.toDataURL());
									$(img).addClass("thumb-image");
									
							        }
							        else{
							        	$(img).attr("src", e.target.result);
										$(img).addClass("thumb-image");								        
							        }
							        upload3Base64 = $(img).attr("src");
							        }
							    img1.src = e.target.result;
								// end resize
								
								// $(img).attr("src", e.target.result);
								
							}
							image_holder.show();
							reader.readAsDataURL($(this)[0].files[i]);
						}

					} else {
						alert("Este navegador não suporta FileReader.");
					}
				} else {
					alert("Por favor, selecione apenas imagens.");
				}
			});

	function save() {
		if (!$formModule.valid()) {
			modal("Chamado", "Por favor, preencha os campos em vermelho",3, $messageTplE);
			return;
		} else if(!verificaFotos()){
			modal("Chamado", 'Por favor, insira uma imagem ou marque a opção "Não informar mídias"',3, $messageTplE);
			return;
		}
		if (getUrlParam('cpf') === "" || $ckAnonymity.is(':checked')){
			$btSave.prop('disabled', true);
			$.ajax({
				type : 'POST',
				url : '../rest/unsolvedcall3/ann',
				dataType : "json",
				data : getFields(),
				beforeSend : function(xhr) {
					xhr.setRequestHeader(header, token);
				},
				contentType : "application/json"
			}).done(function(resp) {
				modal("Chamado", resp.message,1, $messageTplS);
			}).fail(function(jqXHR, textStatus) {
				var title = "Chamado";
				var body = jqXHR.responseJSON.errorCode + ", "
						+ jqXHR.responseJSON.errorMessage;
				modal(title, body,3, $messageTplE);
			});
		}
		else{
			$btSave.prop('disabled', true);
			$.ajax({
				type : 'POST',
				url : '../rest/unsolvedcall3/wu',
				dataType : "json",
				data : getFields(),
				beforeSend : function(xhr) {
					xhr.setRequestHeader(header, token);
				},
				contentType : "application/json"
			}).done(function(resp) {
				modal("Chamado", resp.message,1, $messageSuccessMail);
			}).fail(function(jqXHR, textStatus) {
				var title = "Chamado";
				var body = jqXHR.responseJSON.errorCode + ", "
						+ jqXHR.responseJSON.errorMessage;
				modal(title, body,3, $messageTplE);
			});
		}
	}
	
	$selectCallClassification.change(function(){
		var addressRequired = $(this).find(':selected').data("address-required");
		if(addressRequired == true) {
			$("#addressRequired").iCheck("uncheck");
			$(".no-address").addClass("hide");
		} else {
			$(".no-address").removeClass("hide");
		}
	});
	
	$("#addressRequired").on("ifChecked", function(){
		$inStreet.attr('disabled', 'disabled');
		$inNumber.attr('disabled', 'disabled');
		$inComplement.attr('disabled', 'disabled');
		$selectNeighborhood.attr('disabled', 'disabled');
	});
	
	$("#addressRequired").on("ifUnchecked", function(){
		$inStreet.removeAttr('disabled');
		$inNumber.removeAttr('disabled');
		$inComplement.removeAttr('disabled');
		$selectNeighborhood.removeAttr('disabled');
	});
	
	function init() {
		makeCombos();
		if (getUrlParam('cpf'))
			citizen = {
				'citizen_cpf' : getUrlParam('cpf'),
				'email' : getUrlParam('email'),
				'name' : getUrlParam('name')
			}
		else
			$divAnonymity.hide();	
	}
	init();	
	module.setFilters = setFilters;
	module.getFields = getFields;
	return module;
})(jQuery, ctrlNCin || {});

/*
 * var str1 = "Chamado"; var str2 = "Chamado"; var str3 = "o"; $(document)
 * .ready( function() { var token = $("meta[name='_csrf']").attr("content"); var
 * header = $("meta[name='_csrf_header']").attr("content"); var urlWeb =
 * window.location.search; combo('../rest/callclassification',
 * ModelsTmpl.tmplComboCallClassification, "#comboCallClassification");
 * combo('../rest/neighborhood/city/'+ 3551, ModelsTmpl.tmplComboNeighborhood,
 * "#comboNeighborhood"); combo( '../rest/entityclass/combosativos',
 * ModelsTmpl.tmplComboEntity, "#comboEntity"); $('#comboEntity') .change(
 * function() { $('#comboEntity', '#selectEntityCategory') .remove(); combo(
 * '../rest/entitycategory/activeentity/' + $( '#comboEntity option:selected')
 * .val(), ModelsTmpl.tmplComboEntityCategory, "#comboEntityCategory"); });
 * $('#formMensagem').validate({ errorPlacement : function(error, element) {
 * $(element).parent().addClass("has-error"); error.insertAfter(element);
 * error.wrap("<p>"); error.css('color', 'red'); }, rules : { } });
 * 
 * function validaISalvar() { // Descrição $('#inDescription') .each( function() {
 * $(this) .rules( 'add', { minlength : 10, maxlength : 1000, required : true,
 * messages : { required : "Insira a descrição", minlength : jQuery.validator
 * .format("Por favor, insira mais que {0} caracteres."), maxlength :
 * jQuery.validator .format("Por favor, insira menos que {0} caracteres.") } });
 * }); // Rua $('#inStreet') .each( function() { $(this) .rules( 'add', {
 * maxlength : 100, required : true, messages : { required : "Insira a rua",
 * maxlength : jQuery.validator .format("Por favor, insira menos que {0}
 * caracteres.") } }); }); // Numero $('#inNumber') .each( function() { $(this)
 * .rules( 'add', { maxlength : 5, required : true, number : true, messages : {
 * required : "Insira o numero", number : "Este campo aceita somente números",
 * maxlength : jQuery.validator .format("Por favor, insira menos que {0}
 * caracteres.") } }); }); // Complemento $('#inComplement') .each( function() {
 * $(this) .rules( 'add', { maxlength : 15, messages : { maxlength :
 * jQuery.validator .format("Por favor, insira menos que {0} caracteres.") } });
 * }); // Bairro $('#selectNeighborhood').each(function() {
 * $(this).rules('selectNeighborhood', { valueNotEquals : "default", messages : {
 * valueNotEquals : "Selecione o bairro", } }); }); }
 * 
 * var upload1Base64 = null; var upload2Base64 = null; var upload3Base64 = null;
 * var base64 = null;
 * 
 * function geturl(key) { var result = new RegExp(key + "=([^&]*)", "i")
 * .exec(window.location.search); return result && unescape(result[1]) || ""; }
 * var anonymityUrl = geturl('anonymity'); var citizenCpf =
 * geturl('citizenCpf');
 * 
 * var name = geturl('name'); var email = geturl('email'); var cpf =
 * geturl('cpf');
 * 
 * var internalcall = geturl('useridentification');
 * 
 * $('#nomidia').on('ifUnchecked', function(event) {
 * $("#upload").children().prop('disabled', false); });
 * $('#nomidia').on('ifChecked', function(event) {
 * $("#upload").children().prop('disabled', true); });
 * 
 * $('#modaldemensagem') .on( "click", ".exit-button", function(event) {
 * event.preventDefault(); window.location = '../identification.jsp'; });
 * 
 * $('#modaldemensagem').on("click", ".new-call-button", function(event) {
 * event.preventDefault(); $("#cDescription").val(""); $("#cStreet").val("");
 * $("#cNumber").val(""); $("#cComplement").val("");
 * $("#cSelectNeighborhood").remove(); $("#anonymity").prop('checked', false);
 * $("#nomidia").prop('checked', false); }); var updatedOrModeratedBy;
 * 
 * $("#save") .click( function() { validaISalvar();
 * 
 * if ($("#formMensagem").valid()) {
 * 
 * var street = $("#cStreet").val(); var number = $("#cNumber").val(); var
 * complement = $("#cComplement") .val(); var neighborhood_id = $(
 * "#comboNeighborhood option:selected") .val(); var neighborhood_name = $(
 * "#comboNeighborhood option:selected") .text(); var entity = $( '#comboEntity
 * option:selected') .val(); var anonymity = $('#anonymity').is( ':checked') ? 0 :
 * 1; var nomidia = $('#nomidia').is( ':checked'); var mediasPath = new Array();
 * 
 * var unsolvedCall = { 'description' : { 'information' : $( "#cDescription")
 * .val() }, 'addressId' : { 'streetName' : $("#cStreet") .val(),
 * 'addressNumber' : $( "#cNumber").val(), 'complement' : $( "#cComplement")
 * .val(), 'neighborhoodId' : { 'neighborhoodId' : neighborhood_id, 'name' :
 * neighborhood_name } }, 'entityEntityCategoryMaps' : { 'entityCategory' : {
 * 'entityCategoryId' : $( "#selectEntityCategory option:selected") .val() },
 * 'entity' : { 'entityId' : $( "#selectEntity option:selected") .val() } },
 * 'callClassificationId' : { 'callClassificationId' : $(
 * '#selectCallClassification option:selected') .val() }, 'anonymity' :
 * $('#anonymity') .is(':checked') ? 0 : 1, 'callStatus' : 0, 'noMidia' :
 * nomidia };
 * 
 * if(internalcall==1){ unsolvedCall["updatedOrModeratedBy"] =
 * $("#creator").val(); }
 * 
 * if (nomidia == false) { if (upload1Base64 != null) mediasPath
 * .push(upload1Base64); if (upload2Base64 != null) mediasPath
 * .push(upload2Base64); if (upload3Base64 != null) mediasPath
 * .push(upload3Base64); unsolvedCall.mediasPath = mediasPath; } ; if
 * (anonymityUrl !== ""){ saveNewCallToken( '../rest/unsolvedcall/anonymity',
 * JSON .stringify(unsolvedCall), urlWeb, header, token);
 * console.log(JSON.stringify(unsolvedCall)); } if (citizenCpf !== "") {
 * unsolvedCall.citizenCpf = { 'citizen_cpf' : citizenCpf }; saveNewCallToken(
 * '../rest/unsolvedcall/mobile', JSON .stringify(unsolvedCall), urlWeb, header,
 * token); console.log(JSON.stringify(unsolvedCall)); } if (name !== "") {
 * unsolvedCall.webUser = { 'name' : name, 'email' : email, 'webUserCpf' : cpf };
 * saveNewCallToken( '../rest/webuser/add', JSON .stringify(unsolvedCall),
 * urlWeb, header, token); console.log(JSON.stringify(unsolvedCall)); } } else {
 * validateError(); $('#modaldemensagem').modal('show'); return false; } });
 * 
 * function changeImg(button) { var countFiles = $(button)[0].files.length; var
 * buttonFile = $(button); var imgPath = $(button)[0].value; var extn =
 * imgPath.substring( imgPath.lastIndexOf('.') + 1).toLowerCase(); var
 * image_holder = $("#image-holder"); image_holder.empty(); if (extn == "gif" ||
 * extn == "png" || extn == "jpg" || extn == "jpeg") { if (typeof (FileReader) !=
 * "undefined") { for (var i = 0; i < countFiles; i++) { var reader = new
 * FileReader(); reader.onload = function(e) { var img = $(buttonFile).siblings(
 * "#imgUser"); $(img).attr("src", e.target.result);
 * $(img).addClass("thumb-image"); base64 = e.target.result; }
 * image_holder.show(); reader.readAsDataURL($(button)[0].files[i]); } } else {
 * alert("Este navegador não suporta FileReader."); } } else { alert("Por favor,
 * selecione apenas imagens."); } }
 * 
 * $("#clean1").click( function() { $("#upload1").val(""); $(".img1
 * #imgUser").attr("src", "http://i.imgur.com/SZuRQS4.png"); upload1Base64 =
 * null; });
 * 
 * $("#clean2").click( function() { $("#upload2").val(""); $(".img2
 * #imgUser").attr("src", "http://i.imgur.com/SZuRQS4.png"); upload2Base64 =
 * null; });
 * 
 * $("#clean3").click( function() { $("#upload3").val(""); $(".img3
 * #imgUser").attr("src", "http://i.imgur.com/SZuRQS4.png"); upload3Base64 =
 * null; });
 * 
 * $("#upload1") .on( 'change', function() { var countFiles =
 * $(this)[0].files.length; var buttonFile = $(this); var imgPath =
 * $(this)[0].value; var extn = imgPath.substring( imgPath.lastIndexOf('.') + 1)
 * .toLowerCase(); var image_holder = $("#image-holder"); image_holder.empty();
 * if (extn == "gif" || extn == "png" || extn == "jpg" || extn == "jpeg") { if
 * (typeof (FileReader) != "undefined") { for (var i = 0; i < countFiles; i++) {
 * var reader = new FileReader(); reader.onload = function(e) { var img =
 * $(buttonFile) .siblings( "#imgUser"); $(img) .attr( "src", e.target.result);
 * $(img).addClass( "thumb-image"); upload1Base64 = e.target.result; }
 * image_holder.show(); reader .readAsDataURL($(this)[0].files[i]); } } else {
 * alert("Este navegador não suporta FileReader."); } } else { alert("Por favor,
 * selecione apenas imagens."); } }); $("#upload2") .on( 'change', function() {
 * var countFiles = $(this)[0].files.length; var buttonFile = $(this); var
 * imgPath = $(this)[0].value; var extn = imgPath.substring(
 * imgPath.lastIndexOf('.') + 1) .toLowerCase(); var image_holder =
 * $("#image-holder"); image_holder.empty(); if (extn == "gif" || extn == "png" ||
 * extn == "jpg" || extn == "jpeg") { if (typeof (FileReader) != "undefined") {
 * for (var i = 0; i < countFiles; i++) { var reader = new FileReader();
 * reader.onload = function(e) { var img = $(buttonFile) .siblings( "#imgUser");
 * $(img) .attr( "src", e.target.result); $(img).addClass( "thumb-image");
 * upload2Base64 = e.target.result; } image_holder.show(); reader
 * .readAsDataURL($(this)[0].files[i]); } } else { alert("Este navegador não
 * suporta FileReader."); } } else { alert("Por favor, selecione apenas
 * imagens."); } }); $("#upload3") .on( 'change', function() { var countFiles =
 * $(this)[0].files.length; var buttonFile = $(this); var imgPath =
 * $(this)[0].value; var extn = imgPath.substring( imgPath.lastIndexOf('.') + 1)
 * .toLowerCase(); var image_holder = $("#image-holder"); image_holder.empty();
 * if (extn == "gif" || extn == "png" || extn == "jpg" || extn == "jpeg") { if
 * (typeof (FileReader) != "undefined") { for (var i = 0; i < countFiles; i++) {
 * var reader = new FileReader(); reader.onload = function(e) { var img =
 * $(buttonFile) .siblings( "#imgUser"); $(img) .attr( "src", e.target.result);
 * $(img).addClass( "thumb-image"); upload3Base64 = e.target.result; }
 * image_holder.show(); reader .readAsDataURL($(this)[0].files[i]); } } else {
 * alert("Este navegador não suporta FileReader."); } } else { alert("Por favor,
 * selecione apenas imagens."); } });
 * 
 * });
 * 
 */