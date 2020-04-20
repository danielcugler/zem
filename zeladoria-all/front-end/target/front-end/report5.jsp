<!DOCTYPE html>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta charset="UTF-8">
<%@ page contentType="text/html; charset=UTF-8"%>
<title>Gerar Relatório |</title>
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
			<div class="content">
				<!-- Page Heading Start -->
				<div class="page-heading">
					<h1>
						<i class='fa fa-table'></i> Relatórios

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
										Gerar <strong>Relatório</strong>
									</h2>
								</div>
								<div class="widget-content">
									<div class="data-table-toolbar">
										<div class="row">
											<form id="IForm" method="get" class="form-horizontal">
												<div class="col-md-12" style="padding-top: 10px;">
													<div class="col-md-4" style="padding-top: 25px;">
														<div class="col-md-4" style="padding-left: 0;">
															<label>Tipo de Relatório</label>
														</div>
														<div class="col-md-4" style="padding-left: 0;">
															<input type="radio" name="radio-type" value="consolidado"
																id="consolidado" checked="checked" /> Consolidado
														</div>
														<div class="col-md-4" style="padding-left: 0;">
															<input type="radio" name="radio-type" value="relacionado"
																id="relacionado" /> Relacionado
														</div>
													</div>
													<div class="col-md-4">
														<div class="col-md-6 has-feedback espacamento-left">
															<label class="control-label">Data Inicial</label> <input
																id="fromDate" class="form-control from_date"
																placeholder="Data Inicial" /> <i
																class="glyphicon glyphicon-calendar form-control-feedback"
																contenteditable="false" type="text"></i>
														</div>
														<div class="col-md-6 has-feedback espacamento-right">
															<label class="control-label">Data Final</label> <input
																id="toDate" class="form-control to_date"
																placeholder="Data Final" contenteditable="false"
																type="text" /> <i
																class="glyphicon glyphicon-calendar form-control-feedback"></i>
														</div>
													</div>
													<div class="col-md-4 combo" id="comboCallClassification"></div>
												</div>
												
												<div class="col-md-12" style="padding-top: 10px;">
													<div class="col-md-4 combo" id="comboCallSource"></div>
													<div class="col-md-4 combo" id="comboProgress"></div>
													<div class="col-md-4 combo" id="comboEntity"></div>
												</div>

												<div class="col-md-12" style="padding-top: 10px;">
													<div class="col-md-4">
														<div class="combo" id="comboEntityCategory">
															<label>Categoria de Entidade </label> <select
																class="form-control selectEntityCategory selectpicker"
																id="selectEntityCategory" name="selectEntityCategory">
																<option value="-1">[Todas]</option>
															</select>
														</div>
													</div>
													<div class="col-md-4 combo" id="comboPriority"></div>

													<div class="col-md-4 combo" id="comboChartType"></div>
												</div>

												<div class="col-md-12" style="padding-top: 10px;">
													<div class="col-md-12">
														<div>
															<label> Campos do Relatório</label>
														</div>
													</div>
													<div class="col-md-12">
														<div class="col-md-3">
															<input type="checkbox" class="check" id="data"
																name="checkReportFields" value="data" /> Data
														</div>
														<div class="col-md-3">
															<input type="checkbox" class="check" id="classificacao"
																name="checkReportFields" value="classificacao" />
															Classificação
														</div>
														<div class="col-md-3">
															<input type="checkbox" class="check" id="origem"
																name="checkReportFields" value="origem" /> Origem
														</div>
														<div class="col-md-3">
															<input type="checkbox" class="check" id="entidade"
																name="checkReportFields" value="entidade" /> Entidade
														</div>
													</div>
													<div class="col-md-12" style="padding-top:10px;"></div>
													<div class="col-md-12">
														<div class="col-md-3">
															<input type="checkbox" class="check" id="categoria"
																name="checkReportFields" value="categoria" /> Categoria
														</div>
														<div class="col-md-3">
															<input type="checkbox" class="check" id="andamento"
																name="checkReportFields" value="andamento" /> Andamento
														</div>
														<div class="col-md-3">
															<input type="checkbox" class="check" id="prioridade"
																name="checkReportFields" value="prioridade" />
															Prioridade
														</div>
														<div class="col-md-3">
															<input type="checkbox" class="check" id="bairro"
																name="checkReportFields" value="bairro" /> Bairro
														</div>
													</div>
											
													<div class="col-md-12" style="padding-top:10px;"></div>
													<div class="col-md-12" id="error"></div>
												</div>

												<div class="col-md-12" align="center" id="botoes">
													<button class="btn btn-success tamanho-botao" role="button" type="button"
														id="pdf">Gerar PDF</button>
													<button class="btn btn-info tamanho-botao" role="button" type="button"
														id="csv">Gerar CSV</button>
												</div>

												<br />
											</form>
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
	<script type="text/javascript" src="js/libs/handlebars-v3.0.0.js"></script>
	<script type="text/javascript" src="js/libs/jquery.validate.js"></script>
	<script type="text/javascript" src="js/template.js"></script>
	<script type="text/javascript" src="js/abstractCRUD.js"></script>
	<script type="text/javascript" src="js/controllerGR.js"></script>
	<script>
		$.getScript(
				"assets/libs/bootstrap-datepicker/js/bootstrap-datepicker.js",
				function() {

					var startDate = new Date('01/01/2012');
					var FromEndDate = new Date();
					var ToEndDate = new Date();

					ToEndDate.setDate(ToEndDate.getDate() + 365);

					$('.from_date').datepicker({

						weekStart : 1,
						startDate : '01/01/2012',
						endDate : FromEndDate,
						format : "dd/mm/yyyy",
						autoclose : true
					}).on(
							'changeDate',
							function(selected) {
								startDate = new Date(selected.date.valueOf());
								startDate.setDate(startDate.getDate(new Date(
										selected.date.valueOf())));
								$('.to_date').datepicker('setStartDate',
										startDate);
							});
					$('.to_date').datepicker({

						weekStart : 1,
						startDate : startDate,
						endDate : ToEndDate,
						format : "dd/mm/yyyy",
						autoclose : true
					})
							.on(
									'changeDate',
									function(selected) {
										FromEndDate = new Date(selected.date
												.valueOf());
										FromEndDate.setDate(FromEndDate
												.getDate(new Date(selected.date
														.valueOf())));
										$('.from_date').datepicker(
												'setEndDate', FromEndDate);
									});

				});
	</script>



</body>
</html>