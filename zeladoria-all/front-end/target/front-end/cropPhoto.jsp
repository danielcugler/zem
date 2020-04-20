<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Inclusão de Usuários | ZEM Zeladoria Municipal
	Participativa</title>
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="description" content="">
<meta name="keywords"
	content="coco bootstrap template, coco admin, bootstrap,admin template, bootstrap admin,">
<meta name="author" content="Huban Creative">

<!-- Base Css Files -->
<link
	href="assets/libs/jqueryui/ui-lightness/jquery-ui-1.10.4.custom.min.css"
	rel="stylesheet" />
<link href="assets/libs/bootstrap/css/bootstrap.min.css"
	rel="stylesheet" />
<link href="assets/libs/font-awesome/css/font-awesome.min.css"
	rel="stylesheet" />
<link href="assets/libs/fontello/css/fontello.css" rel="stylesheet" />
<link href="assets/libs/animate-css/animate.min.css" rel="stylesheet" />
<link href="assets/libs/nifty-modal/css/component.css" rel="stylesheet" />
<link href="assets/libs/magnific-popup/magnific-popup.css"
	rel="stylesheet" />
<link href="assets/libs/ios7-switch/ios7-switch.css" rel="stylesheet" />
<link href="assets/libs/pace/pace.css" rel="stylesheet" />
<link href="assets/libs/sortable/sortable-theme-bootstrap.css"
	rel="stylesheet" />
<link href="assets/libs/bootstrap-datepicker/css/datepicker.css"
	rel="stylesheet" />
<link href="assets/libs/jquery-icheck/skins/all.css" rel="stylesheet" />
<!-- Code Highlighter for Demo -->
<link href="assets/libs/prettify/github.css" rel="stylesheet" />

<!-- Extra CSS Libraries Start -->
<link href="assets/libs/bootstrap-select/bootstrap-select.min.css"
	rel="stylesheet" type="text/css" />
<link href="assets/libs/summernote/summernote.css" rel="stylesheet"
	type="text/css" />
<link href="assets/css/style.css" rel="stylesheet" type="text/css" />
<!-- Extra CSS Libraries End -->
<link href="assets/css/fileinput.css" rel="stylesheet" />
<link rel="stylesheet" href="/css/jquery.Jcrop.css" type="text/css" />

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
        <![endif]-->

<link rel="shortcut icon" href="assets/img/favicon.ico">
<link rel="apple-touch-icon" href="assets/img/apple-touch-icon.png" />
<link rel="apple-touch-icon" sizes="57x57"
	href="assets/img/apple-touch-icon-57x57.png" />
<link rel="apple-touch-icon" sizes="72x72"
	href="assets/img/apple-touch-icon-72x72.png" />
<link rel="apple-touch-icon" sizes="76x76"
	href="assets/img/apple-touch-icon-76x76.png" />
<link rel="apple-touch-icon" sizes="114x114"
	href="assets/img/apple-touch-icon-114x114.png" />
<link rel="apple-touch-icon" sizes="120x120"
	href="assets/img/apple-touch-icon-120x120.png" />
<link rel="apple-touch-icon" sizes="144x144"
	href="assets/img/apple-touch-icon-144x144.png" />
<link rel="apple-touch-icon" sizes="152x152"
	href="assets/img/apple-touch-icon-152x152.png" />
</head>
<body class="fixed-left">
	<style>
/* Apply these styles only when #preview-pane has
   been placed within the Jcrop widget */
.jcrop-holder #preview-pane {
	display: block;
	position: absolute;
	z-index: 2000;
	top: 10px;
	right: -280px;
	padding: 6px;
	border: 1px rgba(0, 0, 0, .4) solid;
	background-color: white;
	-webkit-border-radius: 6px;
	-moz-border-radius: 6px;
	border-radius: 6px;
	-webkit-box-shadow: 1px 1px 5px 2px rgba(0, 0, 0, 0.2);
	-moz-box-shadow: 1px 1px 5px 2px rgba(0, 0, 0, 0.2);
	box-shadow: 1px 1px 5px 2px rgba(0, 0, 0, 0.2);
}

/* The Javascript code will set the aspect ratio of the crop
   area based on the size of the thumbnail preview,
   specified here */
#preview-pane .preview-container {
	width: 250px;
	height: 170px;
	overflow: hidden;
}
</style>
	<!-- Modal Start -->
	<%@ include file="/logout.jsp"%>
	<!-- Modal Logout -->
	<div class="md-modal md-just-me" id="logout-modal"></div>
	<!-- Modal End -->
	<!-- Begin page -->

	<div id="wrapper">

		<!-- Top Bar Start -->
		<div class="topbar">
			<%@ include file="/topbar.jsp"%>

		</div>
		<!-- Top Bar End -->
		<!-- Left Sidebar Start -->
		<div class="left side-menu">
			<%@ include file="/menu.jsp"%>
		</div>
		<!-- Left Sidebar End -->

		<!-- Start right content -->
		<div class="content-page">
			<!-- ============================================================== -->
			<!-- Start Content here -->
			<!-- ============================================================== -->
			<div class="content">
				<!-- Page Heading Start -->
				<div class="page-heading">
					<h1>
						<i class='fa fa-table'></i> Usuários

					</h1>
				</div>
				<!-- Page Heading End-->
				<!-- Your awesome content goes here -->
				<div class="row">
					<div class="row">
						<div class="col-md-12">
							<div class="widget">
								<div class="widget-header transparent">
									<h2>
										Inclusão de <strong>Usuários</strong>
									</h2>

								</div>
								<div class="widget-content">
									<div class="data-table-toolbar">
										<div class="row">
											<div class="col-xs-12">
												<div id="wrapper">
													<input type="file" id="upload" name="upload"
														accept=".jpg,.png,.gif" />
												</div>


											</div>
										</div>
									</div>


								</div>
							</div>
						</div>

					</div>
					<div id="crop-modal" class="modal fade" aria-hidden="true">
						<div class="modal-dialog">
							<div class="modal-content">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal"
										aria-hidden="true">&times;</button>
									<h4 class="modal-title">Crop Image</h4>
								</div>
								<div class="modal-body">


									<form method="post" enctype="multipart/form-data"
										action="http://localhost:8080/rest/restwb/upload"
										onsubmit="return checkFile()">

										<div id="error" style="display: none"></div>
										<img id="Image1" src="" alt="" />

										<canvas id="canvas" height="5" width="5">
													</canvas>



										<input type="button" id="btnCrop" value="Crop" />
										<input type="hidden" name="imgX1" id="imgX1" /> <input
											type="hidden" name="imgY1" id="imgY1" /> <input
											type="hidden" name="imgWidth" id="imgWidth" /> <input
											type="hidden" name="imgHeight" id="imgHeight" /> <input
											type="hidden" name="imgCropped" id="imgCropped" /> <input
											type="button" value="Send Image" name="send" id="send" /><input
											type="submit" value="Send File" />

									</form>

								</div>
							</div>
							<!-- /.modal-content -->
						</div>
						<!-- /.modal-dialog -->
					</div>
					<!-- /.modal -->
				</div>

			</div>

			<!-- Footer Start -->
			<div id="footer">
				<%@ include file="/footer.jsp"%>
			</div>
			<!-- Footer End -->


		</div>
		<!-- ============================================================== -->
		<!-- End content here -->
		<!-- ============================================================== -->

	</div>
	</div>
	<!-- End right content -->

	</div>
	<!-- End of page -->
	<!-- the overlay modal element -->
	<div class="md-overlay"></div>
	<!-- End of eoverlay modal -->
	<script>
		var resizefunc = [];
	</script>
	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script src="assets/libs/jquery/jquery-1.11.1.min.js"></script>
	<script src="assets/libs/bootstrap/js/bootstrap.min.js"></script>
	<script src="assets/libs/jqueryui/jquery-ui-1.10.4.custom.min.js"></script>
	<script src="assets/libs/jquery-ui-touch/jquery.ui.touch-punch.min.js"></script>
	<script src="assets/libs/jquery-detectmobile/detect.js"></script>
	<script
		src="assets/libs/jquery-animate-numbers/jquery.animateNumbers.js"></script>
	<script src="assets/libs/ios7-switch/ios7.switch.js"></script>
	<script src="assets/libs/fastclick/fastclick.js"></script>
	<script src="assets/libs/jquery-blockui/jquery.blockUI.js"></script>
	<script src="assets/libs/bootstrap-bootbox/bootbox.min.js"></script>
	<script src="assets/libs/jquery-slimscroll/jquery.slimscroll.js"></script>
	<script src="assets/libs/jquery-sparkline/jquery-sparkline.js"></script>
	<script src="assets/libs/nifty-modal/js/classie.js"></script>
	<script src="assets/libs/nifty-modal/js/modalEffects.js"></script>
	<script src="assets/libs/sortable/sortable.min.js"></script>
	<script src="assets/libs/bootstrap-fileinput/bootstrap.file-input.js"></script>
	<script src="assets/libs/bootstrap-select/bootstrap-select.min.js"></script>
	<script src="assets/libs/bootstrap-select2/select2.min.js"></script>
	<script src="assets/libs/magnific-popup/jquery.magnific-popup.min.js"></script>
	<script src="assets/libs/pace/pace.min.js"></script>
	<script
		src="assets/libs/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
	<script src="assets/libs/jquery-icheck/icheck.min.js"></script>

	<!-- Demo Specific JS Libraries -->
	<script src="assets/libs/prettify/prettify.js"></script>

	<script src="assets/js/init.js"></script>
	<!-- Page Specific JS Libraries -->
	<script src="assets/libs/bootstrap-select/bootstrap-select.min.js"></script>
	<script src="assets/libs/bootstrap-inputmask/inputmask.js"></script>
	<script src="assets/libs/summernote/summernote.js"></script>
	<script src="assets/js/pages/forms.js"></script>
	<script type="text/javascript" src="js/libs/jquery.validate.js"></script>
	<script type="text/javascript" src="js/libs/handlebars-v3.0.0.js"></script>
	<script type="text/javascript" src="js/templates.js"></script>


	<script type="text/javascript" src="js/fileinput.js"></script>
	<script type="text/javascript" src="/js/libs/jquery.Jcrop.min.js"></script>
	<script type="text/javascript" src="/js/libs/jquery.color.js"></script>
	<script type="text/javascript" src="js/libs/jquery.maskedinput.js"></script>
	<!-- 	<script type="text/javascript" src="js/cadastroUsuario.js"></script> -->
	<script type="text/javascript" src="js/cropPhoto.js"></script>
	<!-- 
	<script>

    function checkFile() {
        var fileElement = document.getElementById("upload");  
        if($('#upload')[0].files.length == 0 ){
        $('#error').show();
        $('#error').html("Please select file!")
           return false;
        }
        var fileSize = $('#upload')[0].files[0].size
        var fileExtension = "";
        if (fileElement.value.lastIndexOf(".") > 0) {
            fileExtension = fileElement.value.substring(fileElement.value.lastIndexOf(".") + 1, fileElement.value.length);
        }
        if ((fileExtension.toLowerCase() =="jpg" || fileExtension.toLowerCase() =="jpeg") && (fileSize < 2000000)) {      
            return true;
        }
        else {
         $('#error').show();
         $('#error').html("Unacceptable image file, please try again!")
            return false;
        }
     }


//Jcrop logic on event It will pop in the rectangular container

         $(function () {
         
             $('#upload').change(function () {
            	 $('#crop-modal').modal('show');
                 $('#Image1').hide();
                if($('#upload')[0].files[0].type =="image/jpeg"){
                 $('#btnCrop').show();}
                 $('#error').hide();
                 var reader = new FileReader();
                 reader.onload = function (e) {
                     $('#Image1').show();
                     $('#Image1').attr("src", e.target.result);
                 
                     $('#Image1').Jcrop({
                     
                     allowResize: false , // set true if you want to enable resize.
                     allowSelect: false,  // set true if you want to enable select activity.
                         onChange: SetCoordinates,
                         onSelect: SetCoordinates,
                         aspectRatio: 4/4,
                         setSelect: [ 100, 100, 350, 350 ],
                         bgFade: true, // use fade effect
                         bgOpacity: .3 // fade opacity
                                });
                 }
                 reader.readAsDataURL(this.files[0]);
             });
             $('#btnCrop').click(function () {
                 var x1 = $('#imgX1').val();
                 var y1 = $('#imgY1').val();
                 var width = $('#imgWidth').val();
                 var height = $('#imgHeight').val();
                 var canvas = $("#canvas")[0];
             
                 var context = canvas.getContext('2d');
                 var img = new Image();
                 img.onload = function () {
                     canvas.height = height;
                     canvas.width = width;
                     context.drawImage(img, x1, y1, width, height, 0, 0, width, height);
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

 -->



</body>
</html>