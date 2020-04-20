<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Cidadãos Cadatrados | zUP - Projeto de Zeladoria Urbana
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
<link href="assets/libs/jquery-datatables/css/dataTables.bootstrap.css"
	rel="stylesheet" type="text/css" />
<link
	href="assets/libs/jquery-datatables/extensions/TableTools/css/dataTables.tableTools.css"
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
		<!-- Start right content -->
		<div class="content-page">
			<!-- ============================================================== -->
			<!-- Start Content here -->
			<!-- ============================================================== -->
			<div class="content">
				<!-- Page Heading Start -->
				<div class="page-heading">
					<h1>
						<i class='fa fa-table'></i> Cidadãos Cadastrados
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
										Lista de <strong>Cidadãos</strong>
									</h2>
									<div class="additional-btn">
										<a href="#" class="hidden reload"><i class="icon-ccw-1"></i></a>
										<a href="#" class="widget-toggle"><i
											class="icon-down-open-2"></i></a> <a href="#"
											class="widget-close"><i class="icon-cancel-3"></i></a>
									</div>
								</div>
								<div class="widget-content">
									<div class="data-table-toolbar">
										<div class="row">
											<div class="col-md-4">
												<form role="form">
													<input type="text" class="form-control"
														placeholder="Pesquisar...">
												</form>
											</div>
											<div class="col-md-8">
												<div class="toolbar-btn-action">
													<a class="btn btn-success"><i class="fa fa-plus-circle"></i>
														Adicionar</a> <a class="btn btn-danger"><i
														class="fa fa-trash-o"></i> Deletar</a> <a
														class="btn btn-primary"><i class="fa fa-refresh"></i>
														Atualizar</a>
												</div>
											</div>
										</div>
									</div>

									<div class="table-responsive">
										<table data-sortable class="table">
											<thead>
												<tr>
													<th>ID</th>
													<th style="width: 30px" data-sortable="false"><input
														type="checkbox" class="rows-check"></th>
													<th>Nome</th>
													<th>Localização</th>
													<th>Data de Nascimento</th>
													<th>Email</th>
													<th data-sortable="false">Sort : Off</th>
													<th align="center">Status</th>
													<th data-sortable="false">Ações</th>
												</tr>
											</thead>

											<tbody>
												<tr style="background-color: #87CEFF">
													<td>1</td>
													<td><input type="checkbox" class="rows-check"></td>
													<td><strong>John Doe</strong></td>
													<td>Yogyakarta, Indonesia</td>
													<td>January 01, 1985</td>
													<td><a href="mailto:#">name@domain.com</a></td>
													<td>123</td>
													<td><span class="label label-success">Ativo</span></td>
													<td>
														<div class="btn-group btn-group-xs">
															<a data-toggle="tooltip" title="Editar"
																class="btn btn-primary"><i
																class="glyphicon glyphicon-pencil"></i></a> <a
																data-toggle="tooltip" title="Desativar"
																class="btn btn-green-1"><i
																class="glyphicon glyphicon-ok"></i></a>

														</div>
													</td>
												</tr>
												<tr style="background-color: #FFFACD">
													<td>2</td>
													<td><input type="checkbox" class="rows-check"></td>
													<td><strong>Rusmanovski</strong></td>
													<td>Bali, Indonesia</td>
													<td>september 21, 1995</td>
													<td><a href="mailto:#">name@domain.com</a></td>
													<td>123</td>
													<td><span class="label label-success" align="center">Ativo</span></td>
													<td>
														<div class="btn-group btn-group-xs">
															<a data-toggle="tooltip" title="Editar"
																class="btn btn-primary"><i
																class="glyphicon glyphicon-pencil"></i></a> <a
																data-toggle="tooltip" title="Ativar"
																class="btn btn-red-1"><i
																class="glyphicon glyphicon-remove"></i></a>
														</div>
													</td>
												</tr>
												<tr style="background-color: #98FB98">
													<td>3</td>
													<td><input type="checkbox" class="rows-check"></td>
													<td><strong>Annisa</strong></td>
													<td>London, UK</td>
													<td>September 23, 1996</td>
													<td><a href="mailto:#">name@domain.com</a></td>
													<td>123</td>
													<td><span class="btn btn-label btn-pink-2 btn-xs">Finalizado</span></td>
													<td>
														<div class="btn-group btn-group-xs">
															<a data-toggle="tooltip" title="Editar"
																class="btn btn-primary"><i
																class="glyphicon glyphicon-pencil"></i></a> <a
																data-toggle="tooltip" title="Desativar"
																class="btn btn-green-1"><i
																class="glyphicon glyphicon-ok"></i></a>
														</div>
													</td>
												</tr>
												<tr style="background-color: #F4A460">
													<td>4</td>
													<td><input type="checkbox" class="rows-check"></td>
													<td><strong>Ari Rusmanto</strong></td>
													<td>Jakarta, Indonesia</td>
													<td>January 01, 1990</td>
													<td><a href="mailto:#">name@domain.com</a></td>
													<td>123</td>
													<td><span class="btn btn-label btn-pink-2 btn-xs">Processado</span></td>
													<td>
														<div class="btn-group btn-group-xs">
															<a data-toggle="tooltip" title="Editar"
																class="btn btn-primary"><i
																class="glyphicon glyphicon-pencil"></i></a> <a
																data-toggle="tooltip" title="Ativar"
																class="btn btn-red-1"><i
																class="glyphicon glyphicon-remove"></i></a>
														</div>
													</td>
												</tr>
												<tr style="background-color: #F88E90">
													<td>5</td>
													<td><input type="checkbox" class="rows-check"></td>
													<td><strong>Jenny Doe</strong></td>
													<td>New York, US</td>
													<td>March 11, 1975</td>
													<td><a href="mailto:#">name@domain.com</a></td>
													<td>123</td>
													<td><span class="btn btn-label btn-pink-2 btn-xs">Encaminhado</span></td>
													<td>
														<div class="btn-group btn-group-xs">
															<a data-toggle="tooltip" title="Editar"
																class="btn btn-primary"><i
																class="glyphicon glyphicon-pencil"></i></a> <a
																data-toggle="tooltip" title="Desativar"
																class="btn btn-green-1"><i
																class="glyphicon glyphicon-ok"></i></a>
														</div>
													</td>
												</tr>

											</tbody>
										</table>
									</div>

									<div class="data-table-toolbar">
										<ul class="pagination">
											<li class="disabled"><a href="#">&laquo;</a></li>
											<li class="active"><a href="#">1</a></li>
											<li><a href="#">2</a></li>
											<li><a href="#">3</a></li>
											<li><a href="#">4</a></li>
											<li><a href="#">5</a></li>
											<li><a href="#">&raquo;</a></li>
										</ul>
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
	<script src="assets/libs/jquery-datatables/js/jquery.dataTables.min.js"></script>
	<script src="assets/libs/jquery-datatables/js/dataTables.bootstrap.js"></script>
	<script
		src="assets/libs/jquery-datatables/extensions/TableTools/js/dataTables.tableTools.min.js"></script>
	<script src="assets/js/pages/datatables.js"></script>
</body>
</html>