$(document).ready(function(){
		function geturl(key) {
			var result = new RegExp(key + "=([^&]*)", "i")
					.exec(window.location.search);
			return result && unescape(result[1]) || "";
		}
		
		var cpf = geturl("cpf");
		var code = geturl("code");
		
		$.ajax({
			type : 'GET',
			url : 'http://www.souzem.com.br/rest/mobile/vincularcontas/' + cpf + '/' + code,
			dataType : "json",
			contentType : "application/json",
			success : function(data) {
				if(data.message === 'Contas vinculadas com sucesso!'){
					$("#mensagem").text(data.message);
				}else{
					//$("#mensagem").css({color: "red"}, {font-weight: "bold"});
					$("#mensagem").text('Erro ao vincular contas. Tenta novamente mais tarde!');
				}
			},
			error : function(jqxhr, status, errorMsg) {
				$("#mensagem").text('Erro ao vincular contas. Tente novamente mais tarde!');
			}
		});		
	});		