function template(data, templateHandlebars, selector) {
	Templates.compile(templateHandlebars, selector, data);
	return true;
};

function templateEC(templateHandlebars, selector, msg) {
	Templates.compile(templateHandlebars, selector, msg);
	return true;
};

function alteraED(button, bool) {
	if (!bool) {
		var div = $(button).closest("div");
		$(button).remove();
		$(div)
				.append(
						"<a type = 'button' data-toggle='tooltip' title='Inativar' class='btn btn-xs btn-danger enabled-button'><i class='glyphicon glyphicon-remove'></i></a>");
	} else {
		var div = $(button).closest("div");
		$(button).remove();
		$(div)
				.append(
						"<a type = 'button' data-toggle='tooltip' title='Ativar' class='btn btn-xs btn-success disabled-button'><i class='glyphicon glyphicon-ok'></i></a>");

	}
};

function sendCall(button, data) {
	if (typeof (data.jsonList) !== 'undefined')
		var list = JSON.parse(data.jsonList);
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
	var callProgress = $(tr).find("td:nth-child(11)");
	$(
			"<td><span class='btn btn-xs btn-orange-2 btn-label'>Encaminhado</span></td>")
			.insertAfter(callProgress);
	$(callProgress).remove();
}

function alteraEDCall(button, bool, data) {
	if (typeof (data.jsonList) !== 'undefined')
		var list = JSON.parse(data.jsonList);

	if (!bool) {
		var tr = $(button).closest("tr");
		var unsolvedCallId = $(tr).find("td:nth-child(1)");
		$("<td class=\"hideTD\">" + list.unsolvedCallId + "</td>").insertAfter(
				unsolvedCallId);
		$(unsolvedCallId).remove();
		var detalhamento = ($(tr).next('tr')).find("div:nth-child(1)");
		var creationOrUpdateDate = $(detalhamento).find("span:last-child");
		$("<span>Data: " + list.creationOrUpdateDate + "</span>").insertAfter(
				creationOrUpdateDate);
		$(creationOrUpdateDate).remove();

		var div = $(button).closest("div");
		$(
				"<a type = 'button' data-toggle='tooltip' title='Inativar' class='btn btn-xs btn-danger enabled-button'><i class='glyphicon glyphicon-remove'></i></a>")
				.insertAfter(button);
		$(button).remove();

	} else {

		var tr = $(button).closest("tr");
		var unsolvedCallId = $(tr).find("td:nth-child(1)");
		$("<td class=\"hideTD\">" + list.unsolvedCallId + "</td>").insertAfter(
				unsolvedCallId);
		$(unsolvedCallId).remove();
		var detalhamento = ($(tr).next('tr')).find("div:nth-child(1)");
		var creationOrUpdateDate = $(detalhamento).find("span:last-child");
		$("<span>Data: " + list.creationOrUpdateDate + "</span>").insertAfter(
				creationOrUpdateDate);
		$(creationOrUpdateDate).remove();

		var div = $(button).closest("div");
		$(
				"<a type = 'button' data-toggle='tooltip' title='Ativar' class='btn btn-xs btn-success disabled-button'><i class='glyphicon glyphicon-ok'></i></a>")
				.insertAfter(button);
		$(button).remove();
	}
};

function viewMedia(media) {
	var str = "<div align='center' class='item active'>" + "<img src='"
			+ media.medias[0].img + "' alt='Foto 1'>" + "</div>";
	var i;
	for (i = 1; i < media.medias.length; i++) {
		str += "<div align='center' class='item'>" + "<img src='"
				+ media.medias[i].img + "' alt='Foto " + i + " '>" + "</div>";
	}
	return str;
}

function mediaList(media) {
	var str = "<li data-target='#carousel-example-generic' data-slide-to='0' class='active'></li>";
	for (i = 1; i < media.medias.length; i++) {
		str += "<li data-target='#carousel-example-generic' data-slide-to='"
				+ i + "'></li>";
	}
	return str;
}

// FUNCOES CRUD

function findByIdADD(URL, id) {
	$.ajax({
		type : 'GET',
		url : URL + '/' + id,
		dataType : "json",
		contentType : "application/json",
		success : function(data) {
			loadInputs(data);
		},
		error : function(jqxhr, status, errorMsg) {
			// Mensagem de erro
		}
	});
};

function findByIdADDOperationLog(URL, id) {
	$.ajax({
		type : 'GET',
		url : URL + '/' + id,
		dataType : "json",
		contentType : "application/json",
		success : function(data) {
			loadInputsLog(data);
			var content = data.jsonList.content;
			var jsonList = JSON.stringify(JSON.parse(content), null, ' ');

			jsonList = jsonList.replace(/\\n/g, "\n ");
			jsonList = jsonList.replace(/"/g, "");
			jsonList = jsonList.replace(/{/g, "");
			jsonList = jsonList.replace(/}/g, "");
			jsonList = jsonList.replace(/,/g, "");
			jsonList = jsonList.replace(/\//g, "");
			jsonList = jsonList.replace(/\\/g, "");
			jsonList = jsonList.replace(/  : /g, "");
			jsonList = jsonList.replace(/  :/g, "");
			jsonList = jsonList.replace(/  P/g, " P");
			jsonList = jsonList.replace(/[]/g, "");
			jsonList = jsonList.replace(/ \n /g, "");
			jsonList = jsonList.replace(/ \n  /g, "");
			jsonList = jsonList.replace(/ \n/g, "");
			jsonList = jsonList.replace(/o:P/g, "o\n P");
			jsonList = jsonList.replace(/defe12aad396f90e6b179c239de260d4/g,
					"******");
			$("#json").text(jsonList);

		},
		error : function(jqxhr, status, errorMsg) {
			// Mensagem de erro
		}
	});
};

function findByIdADDOperationLog2(URL, id) {
	$.ajax({
		type : 'GET',
		url : URL + '/' + id,
		dataType : "json",
		contentType : "application/json",
		success : function(data) {
			loadInputsLog(data);
			},
		error : function(jqxhr, status, errorMsg) {
			// Mensagem de erro
		}
	});
};


function login(URL, email, password) {
	$.ajax({
		type : 'GET',
		url : URL + '/' + email + '/' + password,
		dataType : "json",
		contentType : "application/json",
		success : function(data) {
			if (data.success === true)
				window.location = '../newcall.jsp'+"?useridentification=2"
						+ '?citizenCpf=' + data.jsonList;
			else
				loginErrorMessage(data);
			$('#modaldemensagem').modal('show');
		},
		error : function(jqxhr, status, errorMsg) {
			// Mensagem de erro
		}
	});
};

function loginSU(URL, email, password) {
	$.ajax({
		type : 'GET',
		url : URL + '/' + email + '/' + password,
		dataType : "json",
		contentType : "application/json",
		success : function(data) {
			if (data.success === true)
				window.location = 'index.jsp';
			else
				loginErrorMessage(data);
			$('#modaldemensagem').modal('show');
		},
		error : function(jqxhr, status, errorMsg) {
			// Mensagem de erro
		}
	});
};

function forgotPassword(URL, user) {
	$.ajax({
		type : 'GET',
		url : URL + '/' + user,
		dataType : "json",
		contentType : "application/json",
		success : function(data) {
			if (data.success === true) {
				forgotPSuccess(data);
				$('#myModal').modal('hide');
				$('#modaldemensagem').modal('show');
			} else
				forgotPError(data);
			$('#myModal').modal('hide');
			$('#modaldemensagem').modal('show');

		},
		error : function(jqxhr, status, errorMsg) {
			// Mensagem de erro
		}
	});
};
function findMedia(URL, id) {
	$.ajax({
		type : 'GET',
		url : URL + '/media/' + id,
		dataType : "json",
		contentType : "application/json",
		success : function(data) {
			var medias = JSON.parse(data.jsonList);
/*
			var mediasStr = [];
			for ( var key in medias) {
				var binaryString = '', bytes = new Uint8Array(
						medias[key]), length = bytes.length;
				for (var i = 0; i < length; i++) {
					binaryString += String.fromCharCode(bytes[i]);
				}
				base64String = 'data:image/png;base64,'
					+ btoa(binaryString);
				mediasStr.push({ 
									'img' : base64String 
								});
			}
			var photo1 = viewMedia({
									 'medias' : mediasStr
									});
			var photo2 = mediaList({
									'medias' : mediasStr
								  });
	*/

			var mediasStr = [];
			for ( var key in medias) {
				mediasStr.push({ 
									'img' : medias[key] 
								});
			}
			var photo1 = viewMedia({
									 'medias' : mediasStr
									});
			var photo2 = mediaList({
									'medias' : mediasStr
								  });

			
			tempViewMedia(photo1, photo2);
			$('#modaldemidia').modal('show');

		},
		error : function(jqxhr, status, errorMsg) {
			// Mensagem de erro
		}
	});
};

function saveToken(URL, data, header, token) {
	$.ajax({
		type : 'POST',
		url : URL,
		dataType : "json",
		data : data,
		beforeSend : function(xhr) {
			xhr.setRequestHeader(header, token);
		},
		contentType : "application/json",
		success : function(resp) {
			if (resp.message !== "Alguém já escolheu esse login. Tente outro!") {
				saveMessage(str1, str2, str3);
				$('#modaldemensagem').modal('show');
			} else
				duplicatedMessage(resp);
				$('#modaldemensagem').modal('show');
		},
		error : function(data) {
			// Mensagem de erro
		}
	});
};

function saveTokenEntityCategory(URL, data, header, token) {
	$.ajax({
		type : 'POST',
		url : URL,
		dataType : "json",
		data : data,
		beforeSend : function(xhr) {
			xhr.setRequestHeader(header, token);
		},
		contentType : "application/json",
		success : function(resp) {
			if (resp.message !== "Categoria de entidade já cadastrada!") {
				saveMessage(str1, str2, str3);
				$('#modaldemensagem').modal('show');
			} else
				duplicatedMessage(resp);
				$('#modaldemensagem').modal('show');
		},
		error : function(data) {
			// Mensagem de erro
		}
	});
};

function saveTokenMessageModel(URL, data, header, token) {
	$.ajax({
		type : 'POST',
		url : URL,
		dataType : "json",
		data : data,
		beforeSend : function(xhr) {
			xhr.setRequestHeader(header, token);
		},
		contentType : "application/json",
		success : function(resp) {
			if (resp.message !== "Modelo de Mensagem já cadastrado!") {
				saveMessage(str1, str2, str3);
				$('#modaldemensagem').modal('show');
			} else
				duplicatedMessage(resp);
				$('#modaldemensagem').modal('show');
		},
		error : function(data) {
			// Mensagem de erro
		}
	});
};

function saveSUPToken(URL, data, header, token) {
	$.ajax({
		type : 'POST',
		url : URL,
		dataType : "json", 
		data : data,
		beforeSend : function(xhr) {
			xhr.setRequestHeader(header, token);
		},
		contentType : "application/json",
		success : function(resp) {
			if (resp.message !== "Perfil já cadastrado!") {
				saveMessage(str1, str2, str3);
				$('#modaldemensagem').modal('show');
			} else
				duplicatedMessage(resp);
			$('#modaldemensagem').modal('show');
		},
		error : function(data) { 
			// Mensagem de erro
		}
	});
};

function saveENTToken(URL, data, header, token) {
	$.ajax({
		type : 'POST',
		url : URL,
		dataType : "json", 
		data : data,
		beforeSend : function(xhr) {
			xhr.setRequestHeader(header, token);
		},
		contentType : "application/json",
		success : function(resp) {
			if (resp.message !== "Entidade já cadastrada!") {
				saveMessage(str1, str2, str3);
				$('#modaldemensagem').modal('show');
			} else
				duplicatedMessage(resp);
			$('#modaldemensagem').modal('show');
		},
		error : function(data) { 
			// Mensagem de erro
		}
	});
};

/*function updateCallToken(URL, data, header, token) {
	$.ajax({
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
			alert(data);
			// Mensagem de erro
		}
	});
};*/

function saveNewCallToken(URL, data, userIdentification, urlWeb, header, token) {
	$.ajax({
		type : 'POST',
		url : URL,
		dataType : "json", 
		data : data,
		beforeSend : function(xhr) {
			xhr.setRequestHeader(header, token);
		},
		contentType : "application/json",
		success : function(resp) {
			if (resp.jsonList == 'citizen' || resp.jsonList == 'anonymity') {
				saveMessageMobile(str1, str2, str3);
				$('#modaldemensagem').modal('show');
			} else {
				if(userIdentification == 1)
					saveMessageInternalCall(resp.jsonList, urlWeb);					
				else 
					saveMessageWeb(resp.jsonList, urlWeb);					
				
					$('#modaldemensagem').modal('show');
				
			}
		},
		error : function(data) { 
			// Mensagem de erro
		}
	});
};

function saveReplyToken(URL, data, header, token) {
	$.ajax({
		type : 'POST',
		url : URL,
		dataType : "json", 
		data : data,
		beforeSend : function(xhr) {
			xhr.setRequestHeader(header, token);
		},
		contentType : "application/json",
		success : function(resp) {
			replyMessage(str1, resp);
			$('#modaldemensagem').modal('show');
		},
		error : function(data) {
			// Mensagem de erro
		}
	});
};

function mergeToken(URL, data, header, token) {
	$.ajax({
		type : 'PUT',
		url : URL,
		dataType : "json", 
		data : data,
		beforeSend : function(xhr) {
			xhr.setRequestHeader(header, token);
		},
		contentType : "application/json",
		success : function(data) {
			editMessage(str1, str2, str3);
			$('#modaldemensagem').modal('show');
		},
		error : function(data) { 
			// Mensagem de erro
		}
	});
};

function mergeTokenMessageModel(URL, data, header, token) {
	$.ajax({
		type : 'PUT',
		url : URL,
		dataType : "json", 
		data : data,
		beforeSend : function(xhr) {
			xhr.setRequestHeader(header, token);
		},
		contentType : "application/json",
		success : function(data) {
			if (data.message !== "Modelo de Mensagem já cadastrado!") {
				editMessage(str1, str2, str3);
				$('#modaldemensagem').modal('show');
			} else
				duplicatedMessage(data);
				$('#modaldemensagem').modal('show');
		},
		error : function(data) { 
			// Mensagem de erro
		}
	});
};

function mergeTokenET(URL, data, header, token) {
	$.ajax({
		type : 'PUT',
		url : URL,
		dataType : "json", 
		data : data,
		beforeSend : function(xhr) {
			xhr.setRequestHeader(header, token);
		},
		contentType : "application/json",
		success : function(data) {
			if(data.message=="Entidade e Categoria de Entidade estão ligadas a um chamado!"){
				errorMessage(data.message);
					$('#modaldemensagem').modal('show');
			}else{
			editMessage(str1, str2, str3);			
			$('#modaldemensagem').modal('show');
			}
		},
		error : function(data) { 
			// Mensagem de erro
		}
	});
};


function mergeCSToken(URL, button, header, token, roleEdit, roleEnabled,
		roleSend, roleReply) {
	$.ajax({
		type : 'PUT',
		url : URL,
		dataType : "json", 
		beforeSend : function(xhr) {
			xhr.setRequestHeader(header, token);
		},
		contentType : "application/json",
		success : function(data) {
			Templates.compile(ModelsTmpl.sendSucessMessage,
				"#notificacoes", {});
			$('#modaldemensagem').modal('show');
			searchPag('../rest/unsolvedcall'
				+ '/search/', 1, '/z/z/z/z/z/z/z/z/z/z',
				templateHandlebars(roleEdit, roleEnabled, roleSend,
				roleReply), selector);
		},
		error : function(data) {
			Templates.compile(ModelsTmpl.sendErrorMessage,
				"#notificacoes", {});
			$('#modaldemensagem').modal('show');
		}
	});
};

function mergePU(URL, botao) {
	$.ajax({
		type : 'PUT',
		url : URL,
		dataType : "json", 
		contentType : "application/json",
		success : function(data) {
			editMessage(str1, str2, str3);
			$('#modaldemensagem').modal('show');
			$(botao).addClass("disabled");
		},
		error : function(data) { 
			// Mensagem de erro
		}
	});
};

function mergeEDToken(URL, buttonED, bool, header, token) {
	$.ajax({
		type : 'PUT',
		url : URL,
		dataType : "json", 
		beforeSend : function(xhr) {
			xhr.setRequestHeader(header, token);
		},
		contentType : "application/json",
		success : function(data) {
			alteraED(buttonED, bool);
			if (!bool)
				enabledSucessMessage(str1, str2, str3);
			else
				disabledSucessMessage(str1, str2, str3);
			$('#modaldemensagem').modal('show');
		},
		error : function(data) {
			// Mensagem de erro
		}
	});
};


function mergePRToken(URL, buttonED, bool, header, token) {
	$.ajax({
		type : 'PUT',
		url : URL,
		dataType : "json", 
		beforeSend : function(xhr) {
			xhr.setRequestHeader(header, token);
		},
		contentType : "application/json",
		success : function(data) {
		//	alteraED(buttonED, bool);
		//	if (!bool)
		//		enabledSucessMessage(str1, str2, str3);
		//	else
		//		disabledSucessMessage(str1, str2, str3);
		//	$('#modaldemensagem').modal('show');
		//alert("ok");
		},
		error : function(data) {
			// Mensagem de erro
		}
	});
};


function mergeSUPToken(URL, buttonED, bool, header, token) {
	$.ajax({
		type : 'PUT',
		url : URL,
		dataType : "json", 
		beforeSend : function(xhr) {
			xhr.setRequestHeader(header, token);
		},
		contentType : "application/json",
		success : function(data) {
			if (!bool) {
				alteraED(buttonED, bool);
				enabledSucessMessage(str1, str2, str3);
				$('#modaldemensagem').modal('show');
			} else if (data.success == false) {
				disableProfileErrorMessage(data);
				$('#modalError').modal('show');
			} else {
				disabledSucessMessage(str1, str2, str3);
				$('#modaldemensagem').modal('show');
			}
		},
		error : function(data) {
			// Mensagem de erro
		}
	});
};

function mergeEDCalltoken(URL, buttonED, bool, header, token) {
	$.ajax({
		type : 'PUT',
		url : URL,
		dataType : "json", 
		beforeSend : function(xhr) {
			xhr.setRequestHeader(header, token);
		},
		contentType : "application/json",
		success : function(data) {
			alteraEDCall(buttonED, bool, data);
			if (!bool)
				enabledSucessMessage(str1, str2, str3);
			else
				disabledSucessMessage(str1, str2, str3);
			$('#modaldemensagem').modal('show');
		},
		error : function(data) {
			// Mensagem de erro
		}
	});
};

function mergeView(URL, data, header, token) {
	$.ajax({
		type : 'PUT',
		url : URL,
		dataType : "json", 
		data : data,
		beforeSend : function(xhr) {
			xhr.setRequestHeader(header, token);
		},
		contentType : "application/json",
		success : function(data) {

		},
		error : function(data) { 
		}
	});
};

function findAll(URL, templateHandlebars, selector) {
	$.ajax({
		type : 'GET',
		url : URL,
		async : false,
		dataType : "json", 
		contentType : "application/json",
		success : function(data) {
			if (data.jsonList !== "[]") {
				template(data, templateHandlebars, selector);
			} else {
				$("#cthead").css("display", "none");
				$("#pagination").css("display", "none");
				$("#tabela").html("<div align='center'><h4>Não há registros nesta tabela.</h4></div>");
			}
		},
		error : function(jqxhr, status, errorMsg) {
			// Mensagem de erro
		},
		complete : function(jqXHR, status) {

		}
	});
}

/*function findByIdUpdate(URL, id, header, token) {
	$.ajax({
		type : 'GET',
		url : URL,
		async : false,
		dataType : "json", 
		contentType : "application/json",
		success : function(data) {
			data.jsonList = JSON.parse(data.jsonList);
			$("#CSend_message").val(
				data.jsonList.entityEntityCategoryMaps.entityCategory.send_message);
			if (data.jsonList.callProgress == 'ENCAMINHADO') {
				mergeView('../rest/callfollow/seen/'
					+ id,{}, header, token);
			}
			if (data.jsonList.callProgress == 'VISUALIZADO'
				|| data.jsonList.callProgress == 'ENCAMINHADO') {
					$("#Feedback").hide();
					$("#Reply").hide();
					$("#Comments").show();
					$("#selectStatus option[value='Em Andamento']").attr(
						'selected', 'selected');
			} else if (data.jsonList.callProgress == 'ANDAMENTO') {
				$("#Feedback").hide();
				$("#Reply").hide();
				$("#Comments").show();
				$("#selectStatus option[value='Em Andamento']").attr(
					'selected', 'selected');
				$("#selectStatus option[value='Rejeitado']").attr(
					'disabled', 'disabled');
			} else if (data.jsonList.callProgress == 'FINALIZADO') {
				$("#Feedback").show();
				$("#Reply").show();
				$("#Comments").hide();
				$("#selectStatus option[value='Rejeitado']").attr(
					'disabled', 'disabled');
				$("#selectStatus option[value='Em Andamento']").attr(
					'disabled', 'disabled');
				$("#selectStatus option[value='Finalizado']").attr(
					'selected', 'selected');
			}
		},
		error : function(jqxhr, status, errorMsg) { 
			// Mensagem de erro
		},
		complete : function(jqXHR, status) {

		}
	});
}*/

function findMessages() {
	$.ajax({
		type : 'GET',
		url : '../rest/messagemodel/searchAll',
		dataType : "json", 
		contentType : "application/json",
		success : function(data) {
			modalMessageModel(data);
		},
		error : function(jqxhr, status, errorMsg) { 
			// Mensagem de erro
		},
		complete : function(jqXHR, status) {

		}
	});
}

function search(URL, page, args, templateHandlebars, selector) {
	var count = 0;
	var recordPerPage = 0;
	function callbackSuccess(data) {
		if (data.jsonList !== "[]") {
			template(data, templateHandlebars, selector);
			count = data.count;
			recordPerPage = data.recordPerPage;
		} else {
			$("#cthead").css("display", "none");
			$("#tabela").html(
				"<div align='center'><h4>Não há registros nesta tabela.</h4></div>");
		}
	};
	function callbackError() {
	};
	function callbackComplete() {
	};

	$.ajax({
		type : 'GET',
		url : URL + page + args,
		dataType : "json", 
		contentType : "application/json",
		success : function(data) {

			callbackSuccess(data);
		},
		error : function(jqxhr, status, errorMsg) { 
			callbackError();
		},
		complete : function(jqXHR, status) {
			callbackComplete();
		}
	});
}

function searchInicial(URL, page, args, templateHandlebars, selector,currentPage) {

	var count = 0;
	var recordPerPage = 0;

	function callbackSuccess(data) {
		if (data.jsonList !== "[]" && data.jsonList !== null) {
			template(data, templateHandlebars, selector);
			count = data.count;
			recordPerPage = data.recordPerPage;
		} else {
			$("#cthead").css("display", "none");
			$("#tabela").html(
					"<div align='center'><h4>" + data.message + "</h4></div>");
		}
	};
	function callbackError() {
	};
	function callbackComplete(URL, args, templateHandlebars, selector, count,
			recordPerPage,currentPage) {
		$("#pagination").pagination({
			items : count,
			itemsOnPage : recordPerPage,
			cssStyle : "light-theme",
		    currentPage: currentPage,
			onInit : function() {
			if (count < recordPerPage + 1)
				$("#pagination").hide();
			else
				$("#pagination").show();
				$(".prev").hide();
			},
			onPageClick : function(pageNumber) { 
				search(URL, $("#pagination").pagination(
					'getCurrentPage'), args, templateHandlebars,
					selector);
				var showFrom = recordPerPage * (pageNumber - 1);
				var showTo = showFrom + recordPerPage;
				if (pageNumber * recordPerPage > count - recordPerPage)
					$(".next").hide();
				else
					$(".next").show();
				$(".prev").show();
				$("#pagination").show();
				}
		});
	};

	$.ajax({
		type : 'GET',
		url : URL + page + args,
		dataType : "json", 
		contentType : "application/json",
		success : function(data) {

			callbackSuccess(data);
		},
		error : function(jqxhr, status, errorMsg) { 
			callbackError();
		},
		complete : function(jqXHR, status) {
			callbackComplete(URL, args, templateHandlebars, selector, count,
					recordPerPage,currentPage);

		}
	});
};

//SearchInicial Monitor de Chamados
function searchInicialMO(URL, args, templateHandlebars, selector,currentPage) {

	var count = 0;
	var recordPerPage = 0;

	function callbackSuccess(data) {
		if (data.jsonList !== "[]" && data.jsonList !== null) {
			template(data, templateHandlebars, selector);
			count = data.count;
			recordPerPage = data.recordPerPage;
			valoresGraficos(JSON.parse(data.graph));
		} else {
			$("#cthead").css("display", "none");
			$("#tabela").html(
					"<div align='center'><h4>" + data.message + "</h4></div>");
		}
	};
	function callbackError() {
	};
	function callbackComplete(URL, args, templateHandlebars, selector, count,
			recordPerPage,currentPage) {
		
		var items = $("#ctbody tr");
		  // only show the first 2 (or "first per_page") items initially
        items.slice(recordPerPage).hide();
		$("#pagination").pagination({
			items : count,
			itemsOnPage : recordPerPage,
			cssStyle : "light-theme",
		    currentPage: currentPage,
			onInit : function() {
			if (count < recordPerPage + 1)
				$("#pagination").hide();
			else
				$("#pagination").show();
				$(".prev").hide();
			},
			onPageClick : function(pageNumber) { 
			
				var showFrom = recordPerPage * (pageNumber - 1);
				var showTo = showFrom + recordPerPage;
				 items.hide() // first hide everything, then show for the new page
                        .slice(showFrom, showTo).show();
				if (pageNumber * recordPerPage > count - recordPerPage)
					$(".next").hide();
				else
					$(".next").show();
				$(".prev").show();
				$("#pagination").show();
				}
		});
	};

	$.ajax({
		type : 'GET',
		url : URL + args,
		dataType : "json", 
		contentType : "application/json",
		success : function(data) {

			callbackSuccess(data);
		},
		error : function(jqxhr, status, errorMsg) { 
			callbackError();
		},
		complete : function(jqXHR, status) {
			callbackComplete(URL, args, templateHandlebars, selector, count,
					recordPerPage,currentPage);

		}
	});
};

function searchPagina(URL, map, templateHandlebars, selector) {
	var count = 0;
	var recordPerPage = 0;

	function callbackSuccess(data) {
		if (data.jsonList !== "[]" && data.jsonList !== null) {
			template(data, templateHandlebars, selector);
			count = data.count;
			recordPerPage = data.recordPerPage;
		} else {
			if (data.message !== "Operação realizada com sucesso.")
				resultNotFound(data);
			$("#modaldemensagem").modal('show');
		}
	};
	function callbackError() {
	};
	function callbackComplete(URL, map, templateHandlebars, selector, count,
			recordPerPage) {
		$("#pagination").pagination(
				{
					items : count,
					itemsOnPage : recordPerPage,
					cssStyle : "light-theme",
					onInit : function() {
						if (count < recordPerPage + 1)
							$("#pagination").hide();
						else
							$("#pagination").show();
						$(".prev").hide();
					},
					onPageClick : function(pageNumber) { 
						searchPagina(URL, $("#pagination").pagination(
								'getCurrentPage'), map, templateHandlebars,
								selector);
						var showFrom = recordPerPage * (pageNumber - 1);
						var showTo = showFrom + recordPerPage;
						if (pageNumber * recordPerPage > count - recordPerPage)
							$(".next").hide();
						else
							$(".next").show();
						$(".prev").show();
						$("#pagination").show();
					}
				});
	}
	;

	$.ajax({
		type : 'GET',
		url : URL,
		data : map,
		dataType : "json", 
		contentType : "application/json",
		success : function(data) {
			callbackSuccess(data);
		},
		error : function(jqxhr, status, errorMsg) { 
			callbackError();
		},
		complete : function(jqXHR, status) {
			callbackComplete(URL, map, templateHandlebars, selector, count,
					recordPerPage);
		}
	});
};

function searchPag(URL, page, args, templateHandlebars, selector) {
	var count = 0;
	var recordPerPage = 0;

	function callbackSuccess(data) {
		if (data.jsonList !== "[]" && data.jsonList !== null) {
			template(data, templateHandlebars, selector);
			count = data.count;
			recordPerPage = data.recordPerPage;
		} else {
			if (data.success == false)
				resultNotFound(data);
			$("#modaldemensagem").modal('show');
		}
	};
	function callbackError() {
	};
	function callbackComplete(URL, args, templateHandlebars, selector, count,
			recordPerPage) {
		$("#pagination").pagination(
				{
					items : count,
					itemsOnPage : recordPerPage,
					cssStyle : "light-theme",
					onInit : function() {
						if (count < recordPerPage + 1)
							$("#pagination").hide();
						else
							$("#pagination").show();
						$(".prev").hide();
					},
					onPageClick : function(pageNumber) { 
						search(URL, $("#pagination").pagination(
								'getCurrentPage'), args, templateHandlebars,
								selector);
						var showFrom = recordPerPage * (pageNumber - 1);
						var showTo = showFrom + recordPerPage;
						if (pageNumber * recordPerPage > count - recordPerPage)
							$(".next").hide();
						else
							$(".next").show();
						$(".prev").show();
						$("#pagination").show();
					}
				});
	}
	;

	$.ajax({
		type : 'GET',
		url : URL + page + args,
		dataType : "json", 
		contentType : "application/json",
		success : function(data) {
			callbackSuccess(data);
		},
		error : function(jqxhr, status, errorMsg) { 
			callbackError();
		},
		complete : function(jqXHR, status) {
			callbackComplete(URL, args, templateHandlebars, selector, count,
					recordPerPage);
		}
	});
}

function combo(URL, templateHandlebars, comboId) {
	$.ajax({
		type : 'GET',
		url : URL,
		dataType : "json", 
		contentType : "application/json",
		async : false,
		success : function(data) {

			Templates.compile(templateHandlebars, comboId, data);
		},
		error : function(jqxhr, status, errorMsg) { 
		}
	});
}

function findById(URL) {
	var retorno;
	$.ajax({
		type : 'GET',
		url : URL,
		dataType : "json", 
		contentType : "application/json",
		async : false,
		success : function(data) {
			data.jsonList = JSON.parse(data.jsonList);
			retorno = data.jsonList;
		},
		error : function(jqxhr, status, errorMsg) { 
		}
	});
	return retorno;
}

function comboMM(URL, templateHandlebars, comboId) {
	$.ajax({
		type : 'GET',
		url : URL,
		dataType : "json", 
		contentType : "application/json",
		success : function(data) {
			if (typeof (data.jsonList) !== 'undefined')
				data.jsonList = JSON.parse(data.jsonList);
			var data2 = [ 2 ];
			data2[0] = data.jsonList.sort(function(a, b) {
				var nameA = a.name.toLowerCase(), nameB = b.name.toLowerCase()
				if (nameA < nameB) 
					return -1;
				if (nameA > nameB)
					return 1;
				return 0; 
			});
			data2[1] = data.jsonList.sort(function(a, b) {
				var subjectA = a.subject.toLowerCase(), subjectB = b.subject
						.toLowerCase()
				if (subjectA < subjectB) 
					return -1;
				if (subjectA > subjectB)
					return 1;
				return 0; 
			});
			var combos = [ 2 ];
			combos[0] = {
				jsonList : data2[0]
			};
			combos[1] = {
				jsonList : data2[1]
			};
			for (var ii = 0; ii < templateHandlebars.length; ii++)
				Templates.compile2(templateHandlebars[ii], comboId[ii],
						combos[ii]);

		},
		error : function(jqxhr, status, errorMsg) { 
			// Mensagem de erro
		}
	});
};

function comboET(URL, templateHandlebars, comboId) {
	$.ajax({
		type : 'GET',
		url : URL,
		dataType : "json", 
		contentType : "application/json",
		success : function(data) {
			if (typeof (data.jsonList) !== 'undefined')
				data.jsonList = JSON.parse(data.jsonList);
			var jsonList = data.jsonList.sort(function(a, b) {
				var nameA = a.name.toLowerCase(), nameB = b.name.toLowerCase()
				if (nameA < nameB) 
					return -1;
				if (nameA > nameB)
					return 1;
				return 0;
			});

			Templates.compile2(templateHandlebars, comboId, {
				"jsonList" : jsonList
			});

		},
		error : function(jqxhr, status, errorMsg) { 
			// Mensagem de erro
		}
	});
};

function comboUS(URL, templateHandlebars, comboId) {
	$.ajax({
		type : 'GET',
		url : URL,
		dataType : "json", 
		contentType : "application/json",
		success : function(data) {
			if (typeof (data.jsonList) !== 'undefined')
				data.jsonList = JSON.parse(data.jsonList);
			var jsonList = data.jsonList.sort(function(a, b) {
				var nameA = a.name.toLowerCase(), nameB = b.name.toLowerCase()
				if (nameA < nameB) 
					return -1;
				if (nameA > nameB)
					return 1;
				return 0; 
			});

			Templates.compile2(templateHandlebars, comboId, {
				"jsonList" : jsonList
			});

		},
		error : function(jqxhr, status, errorMsg) { 
			// Mensagem de erro
		}
	});
};

function comboEC(URL, templateHandlebars, comboId) {
	$.ajax({
		type : 'GET',
		url : URL,
		dataType : "json", 
		contentType : "application/json",
		success : function(data) {
			if (typeof (data.jsonList) !== 'undefined')
				data.jsonList = JSON.parse(data.jsonList);
			var jsonList = data.jsonList.sort(function(a, b) {
				var nameA = a.name.toLowerCase(), nameB = b.name.toLowerCase()
				if (nameA < nameB) 
					return -1;
				if (nameA > nameB)
					return 1;
				return 0; 
			});

			Templates.compile2(templateHandlebars, comboId, {
				"jsonList" : jsonList
			});

		},
		error : function(jqxhr, status, errorMsg) { 
			// Mensagem de erro
		}
	});
};
function comboCC(URL, templateHandlebars, comboId) {
	$.ajax({
		type : 'GET',
		url : URL,
		dataType : "json", 
		contentType : "application/json",
		success : function(data) {
			if (typeof (data.jsonList) !== 'undefined')
				data.jsonList = JSON.parse(data.jsonList);
			var jsonList = data.jsonList.sort(function(a, b) {
				var nameA = a.name.toLowerCase(), nameB = b.name.toLowerCase()
				if (nameA < nameB) 
					return -1;
				if (nameA > nameB)
					return 1;
				return 0; 
			});

			Templates.compile2(templateHandlebars, comboId, {
				"jsonList" : jsonList
			});

		},
		error : function(jqxhr, status, errorMsg) { 
			// Mensagem de erro
		}
	});
};

function findByIdADDLog(URL, id) {
	$.ajax({
		type : 'GET',
		url : URL,
		dataType : "json", 
		contentType : "application/json",
		success : function(data) {
			
		},
		error : function(jqxhr, status, errorMsg) { 
			// Mensagem de erro
		}
	});
}