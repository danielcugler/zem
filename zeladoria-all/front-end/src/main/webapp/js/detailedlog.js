var selector = "#detailedlog";
var str1 = "Log de Operações";
var str2 = "Log de Operações";
var str3 = "o";
var templateHandlebars = ModelsTmpl.tmplDetailedLogResult;

function loadInputsLog(data) {
	Templates.compile3(templateHandlebars, selector, data);
}

$(document).ready(
		function() {
			function geturl(key) {
				var result = new RegExp(key + "=([^&]*)", "i")
						.exec(window.location.search);
				return result && unescape(result[1]) || "";
			}

			findByIdADDOperationLog2('../rest/log', geturl('id'));
		});