$(document).ready(
		function() {

			var code = getUrlVar('code');
			var link = "../showcall.jsp?code=" + code;

			$('#url').attr('href', link);

			function getUrlVar(key) {
				var result = new RegExp(key + "=([^&]*)", "i")
						.exec(window.location.search);
				return result && unescape(result[1]) || "";
			}

		});