
<html>
<head>
<link href="http://edge1y.tapmodo.com/deepliq/global.css"
	rel="stylesheet" type="text/css" />
<link rel="stylesheet"
	href="http://jcrop-cdn.tapmodo.com/v0.9.12/css/jquery.Jcrop.min.css"
	type="text/css" />
<link href="http://edge1u.tapmodo.com/deepliq/jcrop_demos.css"
	rel="stylesheet" type="text/css" />
<script src="http://edge1u.tapmodo.com/global/js/jquery.min.js"></script>
<script
	src="http://jcrop-cdn.tapmodo.com/v0.9.12/js/jquery.Jcrop.min.js"></script>
<script src="http://edge1v.tapmodo.com/deepliq/jcrop_demos.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Image Crop</title>
<script language="Javascript" type="text/javascript">
	//Validation check onSubmit

	function checkFile() {
		var fileElement = document.getElementById("upload");
		if ($('#upload')[0].files.length == 0) {
			$('#error').show();
			$('#error').html("Please select file!")
			return false;
		}
		var fileSize = $('#upload')[0].files[0].size
		var fileExtension = "";
		if (fileElement.value.lastIndexOf(".") > 0) {
			fileExtension = fileElement.value.substring(fileElement.value
					.lastIndexOf(".") + 1, fileElement.value.length);
		}
		if ((fileExtension.toLowerCase() == "jpg" || fileExtension
				.toLowerCase() == "jpeg")
				&& (fileSize < 2000000)) {
			return true;
		} else {
			$('#error').show();
			$('#error').html("Unacceptable image file, please try again!")
			return false;
		}
	}

	//Jcrop logic on event It will pop in the rectangular container

	$(function() {

		$('#upload').change(function() {
			$('#Image1').hide();
			if ($('#upload')[0].files[0].type == "image/jpeg") {
				$('#btnCrop').show();
			}
			$('#error').hide();
			var reader = new FileReader();
			reader.onload = function(e) {
				$('#Image1').show();
				$('#Image1').attr("src", e.target.result);

				$('#Image1').Jcrop({

					allowResize : false, // set true if you want to enable resize.
					allowSelect : false, // set true if you want to enable select activity.
					onChange : SetCoordinates,
					onSelect : SetCoordinates,
					aspectRatio : 4 / 3,
					setSelect : [ 100, 100, 350, 350 ],
					bgFade : true, // use fade effect
					bgOpacity : .3
				// fade opacity
				});
			}
			reader.readAsDataURL(this.files[0]);
		});
		$('#btnCrop').click(
				function() {
					var x1 = $('#imgX1').val();
					var y1 = $('#imgY1').val();
					var width = $('#imgWidth').val();
					var height = $('#imgHeight').val();
					var canvas = $("#canvas")[0];

					var context = canvas.getContext('2d');
					var img = new Image();
					img.onload = function() {
						canvas.height = height;
						canvas.width = width;
						context.drawImage(img, x1, y1, width, height, 0, 0,
								width, height);
						$('#imgCropped').val(canvas.toDataURL());
					};
					img.src = $('#Image1').attr("src");
					//data = canvas.toDataURL();

				});
	});
	function SetCoordinates(c) {
		$('#imgX1').val(c.x);
		$('#imgY1').val(c.y);
		$('#imgWidth').val(c.w);
		$('#imgHeight').val(c.h);
		//$('#btnCrop').show(); 
	};
</script>

<style>
.custom-upload {
	background-color: #0080F6;
	border: 1px solid #0080F6;
	border-radius: 28px;
	cursor: pointer;
	color: #fff;
	padding: 4px 10px;
}

.custom-upload input {
	left: -9999px;
	position: absolute;
}
</style>
</head>


<body>
	<center style="margin-top: 100px">
		<table border="0" cellpadding="0" cellspacing="5">
			<form method="post" enctype="multipart/form-data"
				action="http://localhost:8080/rest/restwb/upload"
				onsubmit="return checkFile()">
				<div id="error" style="display: none"></div>
				<br /> <label class="custom-upload">Upload <input
					type="file" id="upload" name="upload" accept=".jpg,.png,.gif" />
				</label> <br /> <br />

				<tr>
					<td><img id="Image1" src="" alt="" /></td>
					<td>
						<canvas id="canvas" height="5" width="5"></canvas>
					</td>
				</tr>
		</table>
		<br /> <input type="button" id="btnCrop" value="Crop"
			style="display: none" /> <input type="hidden" name="imgX1"
			id="imgX1" /> <input type="hidden" name="imgY1" id="imgY1" /> <input
			type="hidden" name="imgWidth" id="imgWidth" /> <input type="hidden"
			name="imgHeight" id="imgHeight" /> <input type="hidden"
			name="imgCropped" id="imgCropped" /> <input type="submit"
			value="Send File" />
	</center>
	</form>
</body>
</html>