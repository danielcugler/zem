<!DOCTYPE html>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="_csrf" content="${_csrf.token}" />
<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}" />
<title>Perfis de Usuário |</title>
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
<link href="assets/css/custom.css" rel="stylesheet" type="text/css" />
<!-- Extra CSS Libraries End -->
<link href="assets/css/style-responsive.css" rel="stylesheet" />
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
						<i class='fa fa-table'></i> Perfis de Usuário

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
										Consulta de <strong>Perfis</strong>
									</h2>
								</div>
								<div id="searchModule" class="widget-content">
									<div class="data-table-toolbar">
										<div class="row">
											<fieldset class="fieldset-space">
												<legend>Filtros</legend>
												<div class="col-md-12">
													<form class="form-horizontal" id="search-person-form"
														method="get" onkeypress="return event.keyCode != 13;">
														<div class="col-md-12">
															<div class="form-group form-zero">
																<div class="col-md-6 espacamento-left">
																	<label>Nome</label> <input type="text"
																		class="form-control" id="inName" name="inName"
																		maxlength="100">
																</div>
																<div class="col-md-6 espacamento-right">
																	<fieldset>
																		<legend id="leg">Estado</legend>
																		<label> <input type="radio" id="enabled"
																			class="radio-button" name="radio-button" value="0">
																			Ativo
																		</label> <label> <input type="radio" id="disabled"
																			class="radio-button" name="radio-button" value="1">
																			Inativo
																		</label> <label> <input type="radio" id="ambos"
																			class="radio-button" name="radio-button" value=""
																			checked> Ambos
																		</label>
																	</fieldset>
																</div>
															</div>
														</div>
														<div id="botoes" class="col-md-12 espacamento-left">
															<button type="button"
																class="btn btn-primary tamanho-botao" id="search">
																<span class="glyphicon glyphicon-search"
																	aria-hidden="true"></span> Buscar
															</button>
															<sec:authorize access="hasRole('ROLE_PROFILE_CREATE')">
																<a href="../addsup.jsp"
																	class="btn btn-success tamanho-botao" id="add"
																	role="button" target="_self"> <span
																	class="fa fa-plus-circle" aria-hidden="true"></span>
																	Incluir
																</a>
															</sec:authorize>
														</div>

													</form>
												</div>
											</fieldset>
										</div>
									</div>
								</div>

								<div id="tabela" class="table-responsive">
									<div align='center' style="display: none;" id="notFoundDiv">
										<h4 id="notFoundH">Not Found</h4>
									</div>
									<fieldset id="fieldTable">

										<table id="table" class="fixo table table-hover">
											<thead id="cthead">
											<col width="90%" />
											<sec:authorize
												access="hasRole('ROLE_PROFILE_ENABLE') or hasRole('ROLE_PROFILE_UPDATE')">
												<col width="10%" />
											</sec:authorize>
											<tr>
												<th class="paddingEsquerdo">Nome</th>
												<sec:authorize
													access="hasRole('ROLE_PROFILE_ENABLED') or hasRole('ROLE_PROFILE_UPDATE')">
													<th class="colunaAcoes">Ações</th>
												</sec:authorize>
											</tr>
											<thead>
											<tbody id="ctbody">
											</tbody>
										</table>
										<br />
										<div align="right">
											<div id="pagination"></div>
										</div>
									</fieldset>

								</div>
								<div class='modal fade in' role='dialog' id='modalE'
									aria-hidden='true'>
									<div class='modal-dialog'>
										<div class='modal-content'>
											<div id="notificacoesE"></div>
										</div>
									</div>
								</div>

								<div class='modal fade in' role='dialog' id='modalError'
									aria-hidden='true'>
									<div class='modal-dialog'>
										<div class='modal-content'>
											<div class='modal-header'>
												<button class='close' aria-hidden='true'
													data-dismiss='modal' type='button'>X</button>
												Atenção
											</div>
											<div class='modal-body'>Existem usuários associados ao
												perfil que está sendo inativado. Altere o perfil desses
												usuários e tente novamente!</div>
											<div class='modal-footer'>
												<a data-backdrop='false' class='btn btn-danger dbOk'
													role='button' data-dismiss='modal' id='bOk'>Ok</a>&nbsp&nbsp&nbsp
											</div>
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
	<!-- End right content -->


	<!-- End of page -->
	<!-- the overlay modal element -->
	<div class="md-overlay"></div>
	<!-- End of eoverlay modal -->

	<script id="edmodal-template" type="text/x-handlebars-template">
	<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>X</button>Ativar/Inativar o Perfil de Usuário
				 </div>
				<div class='modal-body'> Você tem certeza que deseja {{msg}} o Perfil de Usuário {{name}}?
				</div>
				<div class='modal-footer'>
				<a data-backdrop='false' class='btn btn-success dbSim' role='button' data-link='{{link}}' data-id='{{id}}' data-row='{{row}}' id='bSim'>Sim</a>&nbsp&nbsp&nbsp
				<a data-backdrop='false' class='btn btn-danger' data-dismiss='modal' role='button' id='bNao'>Não</a>
				</div>
			</script>

	<script id="edmessage-template" type="text/x-handlebars-template">
	<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>X</button> {{title}}
				 </div>
				<div class='modal-body'> {{body}}
				</div>
				<div class='modal-footer'>
				<a data-backdrop='false' class='btn btn-success dbOk' role='button' data-dismiss='modal' id='bOk'>Ok</a>&nbsp&nbsp&nbsp
				</div>
			</script>


	<script>
		var resizefunc = [];
	</script>
	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
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
	<script src="assets/libs/bootstrap-inputmask/inputmask.js"></script>
	<script src="assets/libs/summernote/summernote.js"></script>
	<script src="assets/js/pages/forms.js"></script>
	<script type="text/javascript" src="js/libs/handlebars-v3.0.0.js"></script>
	<script type="text/javascript" src="js/libs/jquery.validate.js"></script>
	<script type="text/javascript" src="js/template.js"></script>
	<script type="text/javascript" src="js/libs/jquery.simplePagination.js"></script>
	<script type="text/javascript" src="js/hadlebarsHelpers.js"></script>
	<script type="text/javascript" src="js/ctrlSUP.js"></script>
</body>
</html>