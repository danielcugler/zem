var ctrlRT = (function($, module) {
	// var module = {};
	var newOnPage = true;
	var list = $('#list li').map(function() {
		return $(this).text();
	});
	// security rules
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	var $username = $('#username');
	// cached DOM
	// general module
	var $generalModule = $("#generalModule");
	var tamanho = $generalModule.length;
	// Form Module
	var $formModule = $generalModule.find("#IForm");
	// check
	var $check = $("input:checkbox[name=checkReportFields]");
	// radio
	var $radioType = $formModule.find(".radio-button");
	// filters
	var $selectEN = $formModule.find("#selectEN");
	var $selectEC = $formModule.find("#selectEC");
	var $selectCP = $formModule.find("#selectCP");
	var $selectCC = $formModule.find("#selectCC");
	var $selectCS = $formModule.find("#selectCS");
	var $selectPR = $formModule.find("#selectPR");
	var $selectGR = $formModule.find("#selectGR");
	var $fromDate = $formModule.find('#fromDate');
	var $toDate = $formModule.find('#toDate');
	var $radioEnabled = $formModule.find(".radio-button");
	// buttons
	var $pdfButton = $formModule.find("#pdf");
	var $csvButton = $formModule.find("#csv");
	// modal
	var $modalE = $generalModule.find("#modalE");
	var $notificacoesE = $generalModule.find("#notificacoesE");
	// templates
	var $comboCCTpl = $("#combo-cc-template").html();
	var $comboENTpl = $("#combo-en-template").html();
	var $comboECTpl = $("#combo-ec-template").html();
	var $comboCPTpl = $("#combo-cp-template").html();
	var $edmodalTpl = $("#edmodal-template").html();
	var $edMessageTpl = $("#edmessage-template").html();
	var $messageTpl = $("#message-template").html();
	// bind events
	$pdfButton.on('click', pdf);
	$csvButton.on('click', csv);
	$selectEN.on('change', function(event) {
		makeSelectEC();
	});
	// validação
	$formModule.validate({ // initialize the plugin
		errorPlacement : function(error, element) {
			$(element).parents().find("#divCheck").addClass("has-error");
			error.insertAfter($(element).parents().find("#divCheck"));
			error.wrap("<p align='center'>");
			error.css('color', 'red');
		},
		rules : {
			checkReportFields : {
				required : true
			}
		},
		messages : {
			checkReportFields : {
				required : "Selecione pelo menos um campo do relatório"
			}
		}
	});

	// functions
	function makeSelectEC() {
		$selectEC.prop("disabled", false);
		$.ajax({
			type : 'GET',
			url : "../rest/report2/ec/" + $selectEN.val(),
			dataType : "json",
			contentType : "application/json"
		}).done(function(resp, status, xhr) {
			_render($comboECTpl, $selectEC, resp);
		}).fail(
				function(jqXHR, textStatus) {
					var title = "Relatórios";
					var body = jqXHR.responseJSON.errorCode + ", "
							+ jqXHR.responseJSON.errorMessage;
					modal(title, body);
				});

	}
	function getType() {
		var checked;
		$radioType.each(function(index, elem) {
			if ($(elem).is(':checked'))
				checked = $(elem).val();
		});
		return checked;
	}
	function modal(title, body) {
		_render($edMessageTpl, $notificacoesE, {
			'title' : title,
			'body' : body
		});
		$modalE.modal({
			show : true,
			backdrop : true
		});
	}

	function getFilters(format) {
		var checkValues = []
		$check.each(function(i, e) {
			if ($(e).is(':checked'))
				checkValues.push(parseInt($(e).val()))
		});
		return {
			'iniDate' : $fromDate.val(),
			'endDate' : $toDate.val(),
			'callClassification' : $selectCC.val(),
			'entity' : $selectEN.val(),
			'category' : $selectEC.val(),
			'callProgress' : $selectCP.val(),
			'callSource' : $selectCS.val(),
			'priority' : $selectPR.val(),
			'chart' : $selectGR.val(),
			'fields' : checkValues,
			'format' : format,
			'type' : getType(),
			'user' : $username.text()
		};
	}
	function setFilters() {
		$selectCC.val();
		$selectEN.val();
		$selectEC.val();
		$selectCP.val();
		$selectCS.val();
		$selectPR.val();
		$selectGR.val();
	}

	function objectParametize(obj, delimeter, q) {
		var str = new Array();
		if (!delimeter)
			delimeter = '&';
		for ( var key in obj) {
			switch (typeof obj[key]) {
			case 'string':
			case 'number':
				str[str.length] = key + '=' + obj[key];
				break;
			case 'array':
				var out = new Array();
				out.join('&');
				obj[key].forEach(function(value) {
					out.push(obj[key] + '=' + value);
				});
				str[str.length] = out.join('&');
				break;
			case 'object':
				var out = new Array();
				out.join('&');
				obj[key].forEach(function(value) {
					out.push(key + '=' + value);
				});
				str[str.length] = out.join('&');
				break;
			}
		}
		return (q === true ? '?' : '') + str.join(delimeter);
	}

	function makeRTPDF() {
		var params = objectParametize(getFilters("pdf"), '&', true);
		var request = new XMLHttpRequest();
		request.open("GET", "../rest/report2/pdf" + params);
		request.responseType = "blob";
		request.onload = function() {
			if (request.readyState === 4) { // if complete
				if (request.status === 400) { // check if "NOT FOUND" (400)
					// error
					modal("Gerador de Relatórios",
							"Nenhum resultado encontrado");
					return;
				}
			}
			var msie = window.navigator.userAgent.indexOf("MSIE");
			if (msie > 0) {
				window.navigator.msSaveBlob(this.response, "report.pdf");
			} else {
				var url = window.URL.createObjectURL(this.response);
				var a = document.createElement("a");
				document.body.appendChild(a);
				a.href = url;
				a.download = this.response.name || "download-" + $.now()
				window.open(url);
			}
		}
		request.onreadystatechange = function() {
			if (request.readyState === 4) { // if complete
				if (request.status === 400) { // check if "NOT FOUND" (400)
					// error
					modal("Gerador de Relatórios",
							"Nenhum resultado encontrado");
				}
			}
		}
		request.send();
	}

	function makeRTCSV() {
		var params = objectParametize(getFilters("csv"), '&', true);
		var request = new XMLHttpRequest();
		request.open("GET", "../rest/report2/csv" + params);
		request.responseType = "blob";
		request.onload = function() {
			if (request.readyState === 4) { // if complete
				if (request.status === 400) { // check if "NOT FOUND" (400)
					// error
					modal("Gerador de Relatórios",
							"Nenhum resultado encontrado");
					return;
				}
			}
			var msie = window.navigator.userAgent.indexOf("MSIE");
			if (msie > 0) {
				window.navigator.msSaveBlob(this.response, "report.csv");
			} else {
				var url = window.URL.createObjectURL(this.response);
				var a = document.createElement("a");
				document.body.appendChild(a);
				a.href = url;
				a.download = "report-" + $.now() + ".csv"
				a.click();
			}
		}
		request.onreadystatechange = function() {
			if (request.readyState === 4) { // if complete
				if (request.status === 400) { // check if "NOT FOUND" (400)
					// error
					modal("Gerador de Relatórios",
							"Nenhum resultado encontrado");
				}
			}
		}
		request.send();
	}

	function makeRT(format) {
		$.ajax({
			type : 'GET',
			url : "../rest/report2",
			data : getFilters(format),
			responseType : 'arraybuffer'
		}).done(function(resp, textStatus, jqXHR) {
			// window.location = resp;

			/*
			 * var blob = new Blob([resp], { type: 'application/pdf' });
			 * 
			 * if (window.navigator && window.navigator.msSaveOrOpenBlob) {
			 * window.navigator.msSaveOrOpenBlob(blob); // for IE } else { var
			 * fileURL = URL.createObjectURL(blob); var newWin =
			 * window.open(fileURL); newWin.focus(); newWin.reload(); }
			 */

			var a = document.createElement("a");
			document.body.appendChild(a);
			var file = new Blob([ resp.data ], {
				type : 'application/pdf'
			});
			var fileURL = URL.createObjectURL(file);
			a.href = fileURL;
			a.download = "report.pdf";
			a.click();

		}).fail(
				function(jqXHR, textStatus, errorThrown) {
					var title = "Gerador de relatórios";
					var body = jqXHR.responseJSON.errorCode + ", "
							+ jqXHR.responseJSON.errorMessage;
					modal(title, body);
				});

	}
	function pdf(event) {
		if (!$formModule.valid()) {
			$("#errorReportFields").css("border", "2px solid red");
			modal("Relatório",
					"Por favor, preencha os campos destacados em vermelho!");
			return false;
		} else
			makeRTPDF();
	}
	function csv(event) {
		if (!$formModule.valid()) {
			$("#errorReportFields").css("border", "2px solid red");
			modal("Relatório",
					"Por favor, preencha os campos destacados em vermelho!");
			return false;
		} else
			makeRTCSV();
	}
	function makeCombos() {
		$.ajax({
			type : 'GET',
			url : "../rest/report2/combos",
			dataType : "json",
			contentType : "application/json"
		}).done(function(resp, textStatus, jqXHR) {
			_render($comboCCTpl, $selectCC, resp.cc);
			_render($comboENTpl, $selectEN, resp.en);
			_render($comboCPTpl, $selectCP, resp.cp);
		}).fail(
				function(jqXHR, textStatus, errorThrown) {
					var title = "Gerador de relatórios";
					var body = jqXHR.responseJSON.errorCode + ", "
							+ jqXHR.responseJSON.errorMessage;
					modal(title, body);
				});
	}
	
	// Opção selecionar todos os checkboxes.
	$("#selectAll").on("ifChecked", function(){
		$(".check-report").iCheck("check");
	});
	$("#selectAll").on("ifUnchecked", function(){
		$(".check-report").iCheck("uncheck");
	});

	makeCombos();
	module.getFilters = getFilters;
	return module;
})(jQuery, ctrlRT || {});