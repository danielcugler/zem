var selector = "#detailedcitizen";
var str1 = "Cidadão";
var str2 = "Cidadão";
var str3 = "o";
var templateHandlebars = ModelsTmpl.tmplDetailedCitizenResult;
function loadInputs(data) {
	Templates.compile(templateHandlebars, selector, data);
}

$(document).ready(
		function() {
			function geturl(key) {
				var result = new RegExp(key + "=([^&]*)", "i")
						.exec(window.location.search);
				return result && unescape(result[1]) || "";
			}
			;

			findByIdADD('../rest/citizen', geturl('cpf'));

		});