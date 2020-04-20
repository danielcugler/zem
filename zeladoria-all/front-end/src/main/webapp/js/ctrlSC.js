var str1 = "Chamados";
var str2 = "Chamados";
var str3 = "o";
function base64(img) {
	var binaryString = '', bytes = new Uint8Array(img), length = bytes.length;
	for (var i = 0; i < length; i++) {
		binaryString += String.fromCharCode(bytes[i]);
	}
	return "src", 'data:image/png;base64,' + btoa(binaryString);
}
function loadInputs(call) {

	var medias = call.mediasPath;

	if (call.hasOwnProperty('unsolvedCallId')) {
		$('#cCallId').val(call.unsolvedCallId);
	} else {
		$('#cCallId').val(call.solvedCallId);
	}
	if (call.noMidia === false) {
		if (medias !== undefined) {
			for (var ii = 0; ii < medias.length; ii++)
				$('#viewMedia').append(
						"<a type = 'button' target = '_self' ><img class='view-media' src="
								+ medias[ii]
								+ " height='130' width='130'></a>&nbsp&nbsp");
		}
	} else {
		$('#viewMedia')
				.append(
						"<a type = 'button' target = '_self'>"
								+ "<img class='view-media' src='assets/login/images/login-logo.png' height='130' width='130'></a>&nbsp&nbsp");
	}
	
	if(call.addressId != null){
		$("#cStreet").append(call.addressId.streetName);
		$("#cNumber").append(call.addressId.addressNumber);
		$("#cNeighborhood").append(call.addressId.neighborhoodId.name);		
	}else{
		$("#cStreet").append("-");
		$("#cNumber").append("-");
		$("#cNeighborhood").append("-");
	}		
	$("#cDate").append(call.creationOrUpdateDate);
	$('#cEntity').append(call.entityEntityCategoryMaps.entity.name);
	$('#cCategoria').append(call.entityEntityCategoryMaps.entityCategory.name);
	$('#cClassification').append(call.callClassificationId.name);
	if (call.callProgress !== "NOVO" && call.callProgress !== "Novo") {
		$('#cDescription').val(call.description.information);
	} else {
		$('#cDescription').val(call.description.information);
	}
	if ((call.callProgress == 'FINALIZADO' || call.callProgress == 'Finalizado')
			|| (call.callStatus == 'REJEITADO' || call.callStatus == 'Rejeitado'))
		$('#cAnswer').val(call.observation.information);
	else
		$('#cAnswer').val("Ainda não há respostas para este chamado.");
	$('#cCallProgress').append('<b>' + call.callProgress + '</b>');

};
$(document).ready(
		function() {

			var upload1Base64 = null;
			var upload2Base64 = null;
			var upload3Base64 = null;
			var base64 = null;

			function geturl(key) {
				var result = new RegExp(key + "=([^&]*)", "i")
						.exec(window.location.search);
				return result && unescape(result[1]) || "";
			}

			$.ajax(
					{
						type : 'GET',
						url : '../rest/unsolvedcall3/child/' + geturl('code')
								+ "/" + geturl('call'),
						dataType : "json",
						contentType : "application/json"
					}).done(function(data) {
				loadInputs(data);
			}).fail(restError);

			$("#viewMedia").on("click", ".view-media", function(event) {
				event.preventDefault();
				alert("Oi");
				var botao = $(this);
				var callid = parseInt($('#cCallId').val());
				findMedia('../rest/callfollow', callid);

			});

		});