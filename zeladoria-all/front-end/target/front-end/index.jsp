<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Dashboard |</title>
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
<link href="assets/libs/rickshaw/rickshaw.min.css" rel="stylesheet"
	type="text/css" />
<link href="assets/libs/morrischart/morris.css" rel="stylesheet"
	type="text/css" />
<link
	href="assets/libs/jquery-jvectormap/css/jquery-jvectormap-1.2.2.css"
	rel="stylesheet" type="text/css" />
<link href="assets/libs/jquery-clock/clock.css" rel="stylesheet"
	type="text/css" />
<link href="assets/libs/bootstrap-calendar/css/bic_calendar.css"
	rel="stylesheet" type="text/css" />
<link href="assets/libs/sortable/sortable-theme-bootstrap.css"
	rel="stylesheet" type="text/css" />
<link href="assets/libs/jquery-weather/simpleweather.css"
	rel="stylesheet" type="text/css" />
<link href="assets/libs/bootstrap-xeditable/css/bootstrap-editable.css"
	rel="stylesheet" type="text/css" />
<link href="assets/css/style.css" rel="stylesheet" type="text/css" />
<!-- Extra CSS Libraries End -->
<link href="assets/css/style-responsive.css" rel="stylesheet" />

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

	<!-- Modal Password -->

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
			<jsp:include page="/menu.jsp" />

		</div>
		<!-- Left Sidebar End -->


		<!-- Start right content -->
		<div class="content-page">
			<!-- ============================================================== -->
			<!-- Start Content here -->
			<!-- ============================================================== -->
			<div class="content">
				<!-- Start info box -->
				<div class="row top-summary">
					<div class="col-lg-3 col-md-6">
						<div class="widget green-1 animated fadeInDown">
							<div class="widget-content padding">
								<div class="widget-icon">
									<i class="fa fa-users"></i>
								</div>
								<div class="text-box">
									<p class="maindata">
										TOTAL DE <b>CIDADÃOS</b> <br>&nbsp;
									</p>
									<h2>
										<span id="countCitizen" class="animate-number"
											data-duration="3000">0</span>
									</h2>
									<div class="clearfix"></div>
								</div>
							</div>
							<div class="widget-footer">
								<div class="row">
									<div class="col-sm-12">
										<!-- 										<i class="fa fa-caret-up rel-change"></i> Aumento de <b>39%</b> -->
									</div>
								</div>
								<div class="clearfix"></div>
							</div>
						</div>
					</div>

					<div class="col-lg-3 col-md-6">
						<div class="widget darkblue-2 animated fadeInDown">
							<div class="widget-content padding">
								<div class="widget-icon">
									<i class="icon-globe-inv"></i>
								</div>
								<div class="text-box">
									<p class="maindata">
										TOTAL DE CHAMADOS <br> <b>EM ABERTO<br>
										</b>
									</p>
									<h2>
										<span id="countCallOpen" class="animate-number"
											data-value="6399" data-duration="2000">0</span>
									</h2>

									<div class="clearfix"></div>
								</div>
							</div>
							<div class="widget-footer">
								<div class="row">
									<div class="col-sm-12">
										<!-- 										<i class="fa fa-caret-down rel-change"></i> Queda de <b>11%</b> -->
									</div>
								</div>
								<div class="clearfix"></div>
							</div>
						</div>
					</div>

					<div class="col-lg-3 col-md-6">
						<div class="widget orange-4 animated fadeInDown">
							<div class="widget-content padding">
								<div class="widget-icon">
									<i class="fa fa-check-circle-o"></i>
								</div>
								<div class="text-box">
									<p class="maindata">
										TOTAL DE CHAMADOS<br> <b>FINALIZADOS</b>
									</p>
									<h2>
										<span id="countFinalizedCall" class="animate-number"
											data-value="70389" data-duration="2000">0</span>
									</h2>
									<div class="clearfix"></div>
								</div>
							</div>
							<div class="widget-footer">
								<div class="row">
									<div class="col-sm-12">
										<!-- 										<i class="fa fa-caret-down rel-change"></i> Aumento de <b>7%</b> -->
									</div>
								</div>
								<div class="clearfix"></div>
							</div>
						</div>
					</div>

					<div class="col-lg-3 col-md-6">
						<div class="widget lightblue-1 animated fadeInDown">
							<div class="widget-content padding">
								<div class="widget-icon">
									<i class="fa fa-exclamation-circle"></i>
								</div>
								<div class="text-box">
									<p class="maindata">
										TOTAL DE CHAMADOS<br> <b>EM ATRASO</b>
									</p>
									<h2>
										<span id="countCallDelay" class="animate-number"
											data-value="18648" data-duration="2000">0</span>
									</h2>
									<div class="clearfix"></div>
								</div>
							</div>
							<div class="widget-footer">
								<div class="row">
									<div class="col-sm-12">
										<!-- 										<i class="fa fa-caret-up rel-change"></i>Queda de <b>6%</b> -->
									</div>
								</div>
								<div class="clearfix"></div>
							</div>
						</div>
					</div>

				</div>
				<!-- End of info box -->

				<div class="col-md-12" style="padding: 0;">
					<div class="widget">
						<div class="widget-header">
							<h2>
								<i class="fa fa-bar-chart"></i> Ranking de <strong>Bairros</strong>
							</h2>
						</div>
						<div class="widget-content">
							<div class="row stacked">
								<div class="col-sm-12 mini-stats">
									<div id="neighborhood-chart"></div>
								</div>
							</div>
							<div class="clearfix"></div>
						</div>
					</div>
				</div>

				<div class="col-md-12" style="padding: 0;">
					<div class="col-md-6" style="padding-left: 0; height: 480px;">
						<div class="widget" style="height: 480px;">
							<div class="widget-header">
								<h2>
									<i class="icon-chart-line"></i><strong>Andamento </strong> dos
									Chamados
								</h2>
							</div>
							<div class="widget-content sales-report-data">
								<span class="pull-left">Novo</span> <span class="pull-right"
									id="newCall"></span>
								<div class="progress progress-xs">
									<div class="progress-bar" id="newCallBar"
										style="background-color: #73829B; border-color: #73829B;"></div>
								</div>
								<div class="clearfix"></div>
								<span class="pull-left">Encaminhado</span><span
									class="pull-right" id="forwardedCall"></span>
								<div class="progress progress-xs">
									<div class="progress-bar" id="forwardedCallBar"
										style="background-color: #CF990A; border-color: #CF990A;"></div>
								</div>
								<div class="clearfix"></div>
								<span class="pull-left">Visualizado</span><span
									class="pull-right" id="visualizedCall"></span>
								<div class="progress progress-xs">
									<div
										style="background-color: rgb(135, 206, 255); border-color: rgb(135, 206, 255);"
										class="progress-bar" id="visualizedCallBar">></div>
								</div>
								<div class="clearfix"></div>
								<span class="pull-left">Em Andamento</span><span
									class="pull-right" id="inProgressCall"></span>
								<div class="progress progress-xs">
									<div class="progress-bar" id="inProgressCallBar"
										style="background-color: #76DEB5; border-color: #76DEB5;"></div>
								</div>
								<div class="clearfix"></div>
								<span class="pull-left">Processado</span><span
									class="pull-right" id="proccessedCall"></span>
								<div class="progress progress-xs">
									<div class="progress-bar" id="proccessedCallBar"
										style="background-color: #170AD1; border-color: #170AD1;"></div>
								</div>
								<div class="clearfix"></div>
								<span class="pull-left">Rejeitado</span><span class="pull-right"
									id="rejectedCall"></span>
								<div class="progress progress-xs">
									<div style="background-color: #E80000; border-color: #E80000;"
										class="progress-bar" id="rejectedCallBar"></div>
								</div>
								<div class="clearfix"></div>
								<span class="pull-left">Finalizado</span><span
									class="pull-right" id="finalizedCall"></span>
								<div class="progress progress-xs">
									<div class="progress-bar" id="finalizedCallBar"
										style="background-color: #0E9107; border-color: #0E9107;"></div>
								</div>
								<div class="clearfix"></div>
							</div>
						</div>
					</div>
					<div class="col-md-6" style="padding-right: 0; height: 480px;">
						<div class="widget" style="height: 480px;">
							<div class="widget-header">
								<h2>
									<i class="fa fa-pie-chart"></i><strong> Qualificação </strong>
									dos Chamados
								</h2>
							</div>
							<div class="widget-content">
								<div id="qualification-chart"></div>
								<div id="media-label" style="text-align:center; font-size: 20px;">Média Geral: </div>								
							</div>
						</div>
					</div>
				</div>
				<div class="row"></div>

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
		<!-- End right content -->

	</div>
	<div id="contextMenu" class="dropdown clearfix">
		<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu"
			style="display: block; position: static; margin-bottom: 5px;">
			<li><a tabindex="-1" href="javascript:;" data-priority="high"><i
					class="fa fa-circle-o text-red-1"></i> High Priority</a></li>
			<li><a tabindex="-1" href="javascript:;" data-priority="medium"><i
					class="fa fa-circle-o text-orange-3"></i> Medium Priority</a></li>
			<li><a tabindex="-1" href="javascript:;" data-priority="low"><i
					class="fa fa-circle-o text-yellow-1"></i> Low Priority</a></li>
			<li><a tabindex="-1" href="javascript:;" data-priority="none"><i
					class="fa fa-circle-o text-lightblue-1"></i> None</a></li>
		</ul>
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

	<!-- 	<!-- Google Charts -->
	-->
	<!-- 	<script type="text/javascript" -->
	<!-- 		src="https://www.gstatic.com/charts/loader.js"></script> -->

	<!-- Demo Specific JS Libraries -->
	<script src="assets/libs/prettify/prettify.js"></script>

	<script src="assets/js/init.js"></script>

	<!-- Page Specific JS Libraries -->
	<script src="assets/libs/raphael/raphael-min.js"></script>
	<script src="assets/libs/morrischart/morris.min.js"></script>
	<script src="https://code.highcharts.com/highcharts.js"></script>
	<script src="https://code.highcharts.com/modules/exporting.js"></script>
	
	<script type="text/javascript" src="js/abstractCRUD.js"></script>
	<script type="text/javascript" src="js/dashboard.js"></script>

	<!-- Scripts não utilizados -->
	<!-- 	<script src="assets/libs/d3/d3.v3.js"></script> -->
	<!-- 	<script src="assets/libs/rickshaw/rickshaw.min.js"></script> -->

	<!-- 	<script src="assets/libs/jquery-knob/jquery.knob.js"></script> -->
	<!-- 	<script -->
	<!-- 		src="assets/libs/jquery-jvectormap/js/jquery-jvectormap-1.2.2.min.js"></script> -->
	<!-- 	<script -->
	<!-- 		src="assets/libs/jquery-jvectormap/js/jquery-jvectormap-us-aea-en.js"></script> -->
	<!-- 	<script src="assets/libs/jquery-clock/clock.js"></script> -->
	<!-- 	<script -->
	<!-- 		src="assets/libs/jquery-easypiechart/jquery.easypiechart.min.js"></script> -->
	<!-- 	<script -->
	<!-- 		src="assets/libs/jquery-weather/jquery.simpleWeather-2.6.min.js"></script> -->
	<!-- 	<script -->
	<!-- 		src="assets/libs/bootstrap-xeditable/js/bootstrap-editable.min.js"></script> -->
	<!-- 	<script src="assets/libs/bootstrap-calendar/js/bic_calendar.min.js"></script> -->
	<!-- 	<script src="assets/js/apps/calculator.js"></script> -->
	<!-- 	<script src="assets/js/apps/todo.js"></script> -->
	<!-- 	<script src="assets/js/apps/notes.js"></script> -->
	<!-- 	<script src="assets/js/pages/index.js"></script> -->
</body>
</html>