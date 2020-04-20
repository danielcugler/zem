<!DOCTYPE html>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta charset="UTF-8">
<meta name="_csrf" content="${_csrf.token}" />
<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}" />
<title>Inclusão de Entidade |</title>
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

<link
	href="http://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css"
	rel="stylesheet" />

<!-- Extra CSS Libraries Start -->
<link href="assets/libs/bootstrap-select/bootstrap-select.min.css"
	rel="stylesheet" type="text/css" />
<link href="assets/libs/summernote/summernote.css" rel="stylesheet"
	type="text/css" />
<link href="assets/css/style.css" rel="stylesheet" type="text/css" />
<!-- Extra CSS Libraries End -->
<link href="assets/css/style-responsive.css" rel="stylesheet" />
<link href="assets/css/custom.css" rel="stylesheet" />

<link rel="stylesheet" href="assets/css/multiselect.css">

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
						<i class='fa fa-table'></i> Entidade

					</h1>
				</div>
				<!-- Page Heading End-->
				<!-- Your awesome content goes here -->
				<div class="row">
					<div class="row">
						<div class="col-sm-12 col-md-12">
							<div class="widget">
								<div class="widget-header transparent">
									<h2 id="title2">
										Inclusão de <strong>Entidade</strong>
									</h2>
								</div>
								<div class="widget-content">
									<div class="data-table-toolbar">
										<div class="row">
											<div id="fieldModule" class="col-sm-12 col-md-12">
												<form id="form-cadastro-entity" method="get"
													onkeypress="return event.keyCode != 13;">
													<div class="form-group">

														<div class="col-sm-12 col-md-12">
															<label>Ícone</label>
															<ul
																style="list-style: outside none none; padding-left: 0px">
																<li class="dropdown"><a
																	class="btn btn-primary btn-lg dropdown-toggle" href="#"
																	data-toggle="dropdown" aria-expanded="false"
																	style='width: 50px'> <i id="entityIcon"
																		class="ion-arrow-right-c"></i>
																</a>
																	<div class="dropdown-menu grid-dropdown">
																		<ul class="espacamento-left"
																			style="text-align: center">

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i
																					class='ion-arrow-right-c'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i
																					class='ion-information-circled'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-help-buoy'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-flag'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-heart'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-settings'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-wrench'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-hammer'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-clipboard'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-scissors'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-bookmark'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-paperclip'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-compose'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-briefcase'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-medkit'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-at'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-cloud'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-calendar'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-compass'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-map'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-locked'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-key'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-chatbubble'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-chatbubbles'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-person'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i
																					class='ion-person-stalker'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-woman'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-man'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-fork'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-wineglass'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-coffee'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-pizza'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-wifi'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-calculator'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-eye'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-flash'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-image'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-easel'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-paintbrush'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-monitor'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-bug'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-film-marker'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-music-note'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-speakerphone'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-cash'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-pricetags'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-trophy'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-university'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-earth'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-lightbulb'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-fireball'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-leaf'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-umbrella'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-nuclear'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-model-s'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-plane'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-android-cart'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-ios-alarm'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i
																					class='ion-ios-game-controller-b'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-ios-football'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-ios-bell'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-ios-paw'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-android-call'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i
																					class='ion-android-bicycle'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-android-bus'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i
																					class='ion-android-train'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-android-boat'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i
																					class='ion-android-restaurant'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i
																					class='ion-android-happy'></i></a></li>

																			<li class='listaIcone'><a
																				class='btn btn-default btn-lg icone'
																				style='width: 50px'> <i class='ion-android-home'></i></a></li>

																		</ul>
																	</div></li>
															</ul>
														</div>

														<div class="col-sm-12 col-md-12">
															<input type="hidden" id="username" name="username"
																value="<sec:authentication property='principal.username'/>" />

															<input type="hidden" class="form-control" id="inEntityId"
																name="Id"> <input type="hidden"
																class="form-control" id="inEnabled" name="Enabled"
																value="0" />

															<div class="col-sm-12 col-md-12 espacamento-left">
																<label>Nome <span class="vermelho">*</span></label>

																<div class="form-group">
																	<input type="text" class="form-control" id="inName"
																		name="inName" maxlength="40">
																</div>
															</div>

														</div>

														<div class="col-md-12">

															<label> Prioridade <span class="vermelho">*</span></label>
															(em dias)


														</div>
														<div class="col-md-12" style="margin-bottom: 5px;">
															<div class="col-sm-3 col-md-3 espacamento-left">
																<p>&nbsp;Alta:</p>
																<div class="form-group">
																	<input type="text" class="form-control" id="inHigh"
																		name="inHigh" maxlength="2">
																</div>
															</div>

															<div class="col-sm-3 col-md-3">
																<p>Média:</p>
																<div class="form-group">
																	<input type="text" class="form-control" id="inMedium"
																		name="inMedium" maxlength="2">
																</div>
															</div>

															<div class="col-sm-3 col-md-3">
																<p>Baixa:</p>
																<div class="form-group">
																	<input type="text" class="form-control" id="inLow"
																		name="inLow" maxlength="2">
																</div>
															</div>

															<div class="col-sm-3 col-md-3" style="padding-top: 35px">
																<input type="checkbox" id="ckPriority" name="ckPriority" />
																Usar padrão do sistema
															</div>

															<input type="hidden" class="form-control"
																id="#cAtEnabled" name="cAtEnabled"> <label
																style="display: none" for="cDLow"></label> <label
																style="display: none" for="cDMedium"></label> <label
																style="display: none" for="cDHigh"></label>
														</div>
													</div>


													<div class="form-group">
														<div class="col-sm-12 col-md-12">
															<label>Lista de categorias <span class="vermelho">*</span></label>
															<select align="center" multiple="multiple"
																id="selectEntityCategory" name="my-select[]"
																class="form-control multi-select">

															</select>
														</div>
													</div>
												</form>
												<div class="col-md-12">
													<p style="margin-top: 10px;">
														<label class="vermelho"><i>* Campos
																obrigatórios</i></label>
													</p>
												</div>
												<div class="col-md-12 buttonModule" align="center"
													id="botoes">
													<a class="btn btn-success tamanho-botao" role="button"
														type="button" id="btAdd">Salvar</a> <a
														class="btn btn-success tamanho-botao"
														style="display: none;" role="button" type="button"
														id="btEdit">Salvar</a> <a href="entity.jsp"
														class="btn btn-danger tamanho-botao" role="button">Cancelar</a>
												</div>
											</div>
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

	<script id="message-template" type="text/x-handlebars-template">
		<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>X</button>
			{{title}}
		</div>
		<div class='modal-body'>
			{{body}}
		</div>
		<div class='modal-footer'>
			<a href="../entity.jsp"  style="display: none;" class='btn btn-success dbOk' role='button' id='bOk1'>Ok</a>
			<a class='btn btn-success dbOk' data-dismiss='modal'  style="display: none;" role='button' id='bOk2'>Ok</a>
			<a class='btn btn-danger' data-dismiss='modal' style="display: none;" id="bOk3" role='button'>Ok!</a>
			&nbsp&nbsp&nbsp
		</div>
	</script>

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
	<script type="text/javascript" src="js/libs/jquery.multi-select.js"></script>
	<script type="text/javascript" src="js/template.js"></script>
	<!--<script type="text/javascript" src="js/templatesET.js"></script>-->
	<!--<script type="text/javascript" src="js/messagesET.js"></script>-->
	<script type="text/javascript" src="js/abstractCRUD.js"></script>
	<!--<script type="text/javascript" src="js/crudClienteET.js"></script>-->
	<script type="text/javascript" src="js/ctrlENadd.js"></script>

</body>
</html>