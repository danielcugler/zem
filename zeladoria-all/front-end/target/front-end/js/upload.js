$(document)
.ready(
function() {

	$("#data").submit(function(event) {
		event.preventDefault();
		var formData = new FormData();
		formData.append('img', $('input[type=file]')[0].files[0]);
		$.ajax({
			url : '../rest/files/upload',
			type : 'POST',
			data : formData,
			async : false,
			cache : false,
			contentType : false,
			processData : false,
			success : function(returndata) {
				alert(returndata);
			}
		});
		return false;
	});
});
