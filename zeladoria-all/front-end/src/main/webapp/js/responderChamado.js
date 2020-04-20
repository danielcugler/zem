
$(document).ready(function() {

	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");

	$('#IForm').validate({
		errorPlacement : function(error, element) {
			error.insertAfter(element);
			error.wrap("<p>");
			error.css('color', 'red');
		},
	});

	function validaISalvar() {

		$('[name="cAnswer"]').each(function() {
			$(this).rules('add', {
				required : true,
				messages : {
					required : "Campo obrigatório"
				}
			});
		});

	}

	$('#modaldemensagem').on("click", ".okbutton", function(event) {
		event.preventDefault();
		window.location = '../call.jsp';
	});

	$('#publicButton').click(function(event) {
		event.preventDefault();
		validaISalvar();
		if ($("#IForm").valid()) {
			return true;
		}

		return false;
	});

});