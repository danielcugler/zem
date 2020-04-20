$(function() {
	var jcrop_api;
	var canvas;
	$('#upload').change(function() {
		$('#crop-modal').modal('show');
		$('#Image1').hide();

		$("#Image1").attr("src", "");
		if ($('#upload')[0].files[0].type == "image/jpeg") {
			$('#btnCrop').show();
		}
		$("#btn")
		$('#error').hide();
		var reader = new FileReader();
		reader.onload = function(e) {
			$('#Image1').show();
			$('#Image1').attr("src", e.target.result);
			$('#Image1').Jcrop({
				allowResize : false, 
				allowSelect : false, 
				boxWidth : 650, 
				boxHeight : 400, 
				onChange : SetCoordinates,
				onSelect : SetCoordinates,
				aspectRatio : 4 / 4,
				setSelect : [ 100, 100, 350, 350 ],
				bgFade : true, 
				bgOpacity : .3
			
			}, function() {
				
				jcrop_api = this;
			});
		}
		reader.readAsDataURL(this.files[0]);
	});
	$('#btnCrop').click(function() {
		var x1 = $('#imgX1').val();
		var y1 = $('#imgY1').val();
		var width = $('#imgWidth').val();
		var height = $('#imgHeight').val();
		canvas = $("#canvas")[0];
		var context = canvas.getContext('2d');
		var img = new Image();
		img.onload = function() {
			canvas.height = height;
			canvas.width = width;
			context.drawImage(img, x1, y1, width, height, 0, 0, width, height);

		};
		img.src = $('#Image1').attr("src");

	});
	$('#btnRemove').click(function() {
		$('#crop-modal').modal('hide');
		$("#upload").val("");
	});

	$('#send').click(function() {
		$("#imgUser").attr("src", canvas.toDataURL());
		$("#Image1").attr("src", "");
		$('#crop-modal').modal('hide');
		jcrop_api.destroy();
	});

});

function SetCoordinates(c) {
	$('#imgX1').val(c.x);
	$('#imgY1').val(c.y);
	$('#imgWidth').val(c.w);
	$('#imgHeight').val(c.h);
};
function checkFile() {
	var fileElement = document.getElementById("imgCropped");
	if ($('#imgCropped')[0].files.length == 0) {
		$('#error').show();
		$('#error').html("Please select file!")
		return false;
	}
	var fileSize = $('#imgCropped')[0].files[0].size
	var fileExtension = "";
	if (fileElement.value.lastIndexOf(".") > 0) {
		fileExtension = fileElement.value.substring(fileElement.value
				.lastIndexOf(".") + 1, fileElement.value.length);
	}
	if ((fileExtension.toLowerCase() == "png" || fileExtension.toLowerCase() == "png")
			&& (fileSize < 2000000)) {
		return true;
	} else {
		$('#error').show();
		$('#error').html("Unacceptable image file, please try again!")
		return false;
	}
}

function saveCanvasImage(crop_canvas) {
	var imageBase64 = crop_canvas.toDataURL('image/png');

	$.ajax({
		url : "../rest/up",
		data : imageBase64,
		type : 'POST',
		dataType : 'json',
		timeout : 10000,
		async : false,
		error : function() {
			console.log("WOOPS");
		},
		success : function(res) {
			if (res.ret == 0) {
				console.log("SUCCESS");
			} else {
				console.log("FAIL : " + res.msg);
			}
		}
	});
}
