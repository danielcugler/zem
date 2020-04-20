<!DOCTYPE html>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta charset="UTF-8">
<%@ page contentType="text/html; charset=UTF-8"%>
<title>Sobre o ZEM |</title>
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
<!-- <link href="assets/libs/animate-css/animate.min.css" rel="stylesheet" /> -->
<link href="assets/libs/nifty-modal/css/component.css" rel="stylesheet" />
<!-- <link href="assets/libs/magnific-popup/magnific-popup.css" -->
<!-- 	rel="stylesheet" /> -->
<!-- <link href="assets/libs/ios7-switch/ios7-switch.css" rel="stylesheet" /> -->
<!-- <link href="assets/libs/pace/pace.css" rel="stylesheet" /> -->
<!-- <link href="assets/libs/sortable/sortable-theme-bootstrap.css" -->
<!-- 	rel="stylesheet" /> -->
<link href="assets/libs/bootstrap-datepicker/css/datepicker.css"
	rel="stylesheet" />
<link href="assets/libs/jquery-icheck/skins/all.css" rel="stylesheet" />
<!-- Code Highlighter for Demo -->
<link href="assets/libs/prettify/github.css" rel="stylesheet" />

<!-- Extra CSS Libraries Start -->
<link href="assets/libs/bootstrap-select/bootstrap-select.min.css"
	rel="stylesheet" type="text/css" />
<!-- <link href="assets/libs/summernote/summernote.css" rel="stylesheet" -->
<!-- 	type="text/css" /> -->
<link href="assets/css/style.css" rel="stylesheet" type="text/css" />
<!-- Extra CSS Libraries End -->
<link href="assets/css/style-responsive.css" rel="stylesheet" />
<link href="assets/css/custom.css" rel="stylesheet" />


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
			<div id="generalModule" class="content">
				<!-- Page Heading Start -->
				<div class="page-heading">
					<h1>
						<i class='fa fa-table'></i> Sobre o ZEM

					</h1>

					<p class="hide" id="username">
						<sec:authentication property="principal.username" />
					</p>
				</div>
				<!-- Page Heading End-->
				<!-- Your awesome content goes here -->
				<div class="row">
					<div class="row">
						<div class="col-md-12">
							<div class="widget">
								<div class="widget-header transparent">
									<h2>
										<strong>Zeladoria Municipal</strong>
									</h2>
								</div>
								<div class="widget-content">
									<div class="data-table-toolbar col-md-12" style="padding-bottom: 3em;">
										<div style="margin: 1em 5em; padding: 1em; text-align: justify;">
											<div class="col-md-12">
											<p>A plataforma ZEM (Zeladoria Municipal) provê um
												ambiente de comunicação entre Prefeitura (e suas
												secretarias) e cidadãos. O propósito do ZEM é permitir que
												cidadãos possam participar ativamente da zeladoria do
												município, através da abertura de chamados que são
												encaminhados automaticamente aos setores responsáveis. Isso
												permite que diversas categorias de problemas ocorridos nos
												municípios possam chegar rapidamente ao conhecimento dos
												órgãos responsáveis, permitindo solucionar problemas de
												maneira mais eficiente; e, portanto, gerando crescimento
												para o município e benefícios aos cidadãos.</p>
											<p>Além disso, o ZEM permite que a prefeitura e suas
												secretarias tenham um canal mais efetivo de comunicação com
												os cidadãos, uma ferramenta de extrema importância na
												divulgação em massa de informações.</p>
											<p>Seja você também um agente fiscalizador de sua cidade.
												Contribua, seja um cidadão ZEM!</p>
											</div>
											<div class="col-md-2 col-xs-6 col-lg-2 col-sm-6" style="padding-top: 2em;">
												<img src="images/logoLabitec.png" class="img-responsive">
											</div>
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
			<div id="footer">
				<%@ include file="/footer.jsp"%>
			</div>
			<!-- Footer End -->


		</div>
		<!-- ============================================================== -->
		<!-- End content here -->
		<!-- ============================================================== -->

	</div>

	<!-- End of page -->
	<!-- the overlay modal element -->
	<div class="md-overlay"></div>
	<!-- End of eoverlay modal -->
	<script>
		var resizefunc = [];
	</script>
	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script src="assets/libs/jquery/jquery-2.1.4.min.js"></script>
	<script src="assets/libs/bootstrap/js/bootstrap.min.js"></script>
	<script src="assets/libs/jqueryui/jquery-ui-1.10.4.custom.min.js"></script>
	<script src="assets/libs/jquery-ui-touch/jquery.ui.touch-punch.min.js"></script>
	<script src="assets/libs/jquery-detectmobile/detect.js"></script>
<!-- 	<script -->
<!-- 		src="assets/libs/jquery-animate-numbers/jquery.animateNumbers.js"></script> -->
<!-- 	<script src="assets/libs/ios7-switch/ios7.switch.js"></script> -->
<!-- 	<script src="assets/libs/fastclick/fastclick.js"></script> -->
<!-- 	<script src="assets/libs/jquery-blockui/jquery.blockUI.js"></script> -->
	<script src="assets/libs/bootstrap-bootbox/bootbox.min.js"></script>
	<script src="assets/libs/jquery-slimscroll/jquery.slimscroll.js"></script>
<!-- 	<script src="assets/libs/jquery-sparkline/jquery-sparkline.js"></script> -->
	<script src="assets/libs/nifty-modal/js/classie.js"></script>
	<script src="assets/libs/nifty-modal/js/modalEffects.js"></script>
<!-- 	<script src="assets/libs/sortable/sortable.min.js"></script> -->
	<script src="assets/libs/bootstrap-fileinput/bootstrap.file-input.js"></script>
	<script src="assets/libs/bootstrap-select/bootstrap-select.min.js"></script>
	<script src="assets/libs/bootstrap-select2/select2.min.js"></script>
<!-- 	<script src="assets/libs/magnific-popup/jquery.magnific-popup.min.js"></script> -->
<!-- 	<script src="assets/libs/pace/pace.min.js"></script> -->
	<script
		src="assets/libs/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
	<script src="assets/libs/jquery-icheck/icheck.min.js"></script>

	<!-- Demo Specific JS Libraries -->
	<script src="assets/libs/prettify/prettify.js"></script>

	<script src="assets/js/init.js"></script>
	<!-- Page Specific JS Libraries -->
	<script src="assets/libs/bootstrap-select/bootstrap-select.min.js"></script>
	<script src="assets/libs/bootstrap-inputmask/inputmask.js"></script>
</body>
</html>