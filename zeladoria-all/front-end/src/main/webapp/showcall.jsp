<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Acompanhamento de Chamado | ZEM</title>
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="description" content="Zeladoria Urbana Participativa">
<meta name="keywords"
	content="zeladoria urbaba participativa, zeladoria urbana">
<meta name="author" content="LabITec">
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
<link href="assets/css/style-responsive.css" rel="stylesheet" />
<link href="assets/css/custom.css" rel="stylesheet" type="text/css" />
<link href="css/simplePagination.css" rel="stylesheet" type="text/css" />
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

	<div id="wrapper">

		<!-- Top Bar Start -->

		<!-- Top Bar End -->
		<!-- Left Sidebar Start -->

		<!-- Left Sidebar End -->

		<!-- Start right content -->
		<div class="">
			<!-- ============================================================== -->
			<!-- Start Content here -->
			<!-- ============================================================== -->
			<div class="content">
				<!-- Page Heading Start -->
				<div class="page-heading">
					<h1>
						<i class='icon-megaphone-1'></i> Chamado Público

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
										&nbsp&nbsp Acompanhamento do <strong>Chamado</strong>
									</h2>
								</div>
								<div class="widget-content">
									<div class="data-table-toolbar">
										<div class="row">
											<div class="col-md-12">
												<form id="formMensagem"
													onkeypress="return event.keyCode != 13;">


													<div id="col-md-4">

														<div class="filter" id="campos">

															<div class="col-md-12">

																<fieldset>
																	<legend> Informações do chamado</legend>
																	<input type="hidden" id="cCallId"></input>
																	<div class="col-md-5">
																		<p>
																		<div id="cDate">
																			<label>Data de Registro:&nbsp</label>
																		</div>
																		</p>

																		<p>
																		<div id="cEntity">
																			<label>Entidade:&nbsp</label>
																		</div>
																		</p>

																		<p>
																		<div id="cStreet">
																			<label>Rua:&nbsp</label>
																		</div>
																		</p>
																	</div>

																	<div class="col-md-4">
																		<p>
																		<div id="cCallProgress">
																			<label><b>Estado:</b>&nbsp</label>
																		</div>
																		</p>

																		<p>
																		<div id="cClassification">
																			<label>Classificação:&nbsp</label>
																		</div>
																		</p>

																		<p>
																		<div id="cNumber">
																			<label>Número:&nbsp</label>
																		</div>
																		</p>
																	</div>

																	<div class="col-md-3">
																		<p>
																		<div id="cHide">
																			<label style="visibility: hidden;">1 </label>
																		</div>
																		</p>
																		<p>
																		<div id="cHide">
																			<label style="visibility: hidden;">2 </label>
																		</div>
																		</p>
																		<p>
																		<div id="cNeighborhood">
																			<label>Bairro:&nbsp</label>
																		</div>
																		</p>
																	</div>

																	<div class="col-md-12">
																		<label>Descrição</label>
																		<textarea id="cDescription" rows="8" cols="40"
																			class="form-control" maxlength="4000"
																			name="cDescription" disabled="true"></textarea>
																	</div>

																</fieldset>
															</div>

															<div class="col-md-12">
																<br />
																<fieldset>
																	<legend> Mídias</legend>
																	<div class="col-md-12">
																		<div id="viewMedia"></div>
																	</div>
																</fieldset>
															</div>

														</div>

													</div>
												</form>
											</div>

											<div class="col-md-12">

												<div class="col-md-12">
													<br /> <label>Resposta</label>
													<textarea id="cAnswer" rows="8" cols="40"
														class="form-control" maxlength="4000" name="cAnswer"
														disabled="true"></textarea>
												</div>

											</div>

											<div class='modal fade in' role='dialog' id='modaldemidia'
												aria-hidden='true'>
												<div class='modal-dialog'>
													<div class='modal-content'>
														<div class='modal-header'>
															<button class='close' aria-hidden='true'
																data-dismiss='modal' type='button'>×</button>
															Visualização de Mídia
														</div>
														<div class='modal-body'>
															<div id="carousel-example-generic" class="carousel slide"
																data-ride="carousel" data-interval="false">
																<ol class="carousel-indicators" id="lista-midia">

																</ol>

																<!-- Wrapper for slides -->
																<div class="carousel-inner" role="listbox"
																	id="carousel-inner"></div>

																<!-- Controls -->
																<a class="left carousel-control"
																	href="#carousel-example-generic" role="button"
																	data-slide="prev"> <span
																	class="glyphicon glyphicon-chevron-left"
																	aria-hidden="true"></span> <span class="sr-only">Previous</span>
																</a> <a class="right carousel-control"
																	href="#carousel-example-generic" role="button"
																	data-slide="next"> <span
																	class="glyphicon glyphicon-chevron-right"
																	aria-hidden="true"></span> <span class="sr-only">Next</span>
																</a>
															</div>
														</div>
													</div>
												</div>
											</div>




										</div>
									</div>
								</div>
							</div>








							<div class='modal fade in' role='dialog' id='modalD'
								aria-hidden='true'>
								<div class='modal-dialog'>
									<div class='modal-content'>
										<div id="notificacoesD"></div>
									</div>
								</div>
							</div>

							<div class='modal fade in' role='dialog' id='modalE'
								aria-hidden='true'>
								<div class='modal-dialog'>
									<div class='modal-content'>
										<div id="notificacoesE"></div>
									</div>
								</div>
							</div>

							<div class='modal fade in' role='dialog' id='modaldemensagem'
								aria-hidden='true'>
								<div class='modal-dialog'>
									<div class='modal-content'>
										<div id="notificacoes"></div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>

			</div>

		</div>

		<!-- Footer Start -->

		<!-- Footer End -->


	</div>
	<!-- ============================================================== -->
	<!-- End content here -->
	<!-- ============================================================== -->

	</div>
	</div>
	<!-- End right content -->


	<!-- End of page -->
	<!-- the overlay modal element -->
	<div class="md-overlay"></div>
	<!-- End of eoverlay modal -->
	<script>
		var resizefunc = [];
	</script>
	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<!-- <script src="assets/libs/jquery/jquery-1.11.1.min.js"></script> -->
	<script src="assets/libs/jquery/jquery-2.1.4.min.js"></script>

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

	<script src="assets/libs/summernote/summernote.js"></script>
	<script src="assets/js/pages/forms.js"></script>

	<script type="text/javascript" src="js/libs/bootstrap.min.js"></script>
	<script type="text/javascript" src="js/libs/handlebars-v3.0.0.js"></script>



	<!-- <script type="text/javascript" src="js/templatesMM.js"></script> -->
	<!--<script type="text/javascript" src="js/abstractCRUD.js"></script> -->
	<!--<script type="text/javascript" src="js/crudClienteMM.js"></script> -->
	<!--<script type="text/javascript" src="js/controllerMM.js"></script> -->

	<script type="text/javascript" src="js/template.js"></script>


	<script type="text/javascript" src="js/abstractCRUD.js"></script>
	<!--<script type="text/javascript" src="js/crudClienteMM.js"></script> -->
	<script type="text/javascript" src="js/ctrlSC.js"></script>
	<!-- <script type="text/javascript" src="js/messagesMM.js"></script> -->
</body>
</html>