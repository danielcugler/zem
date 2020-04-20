$(document).ready(function() {
	$('.image-editor').cropit({
		imageState : {
			src : 'http://lorempixel.com/500/400/'
		}
	});

	$('.export').click(function() {
		var imageData = $('.image-editor').cropit('export');
		window.open(imageData);
	});
});