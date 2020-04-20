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
<title>Inclusão de Usuários |</title>
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
<link href="assets/css/custom.css" rel="stylesheet" />
<!-- Extra CSS Libraries End -->
<link href="assets/css/fileinput.css" rel="stylesheet" />
<link rel="stylesheet" href="js/libs/jCrop/css/Jcrop.min.css"
	type="text/css" />

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
									<h2 id="title2">
										Inclusão de <strong>Usuários</strong>
									</h2>

								</div>
								<div class="widget-content">
									<div class="data-table-toolbar">
										<div class="row">
											<div class="col-md-12">
												<form id="formUsuario" method="get">

													<div class="row">
														<div class="col-md-12">
															<div class="inputfile col-md-2">
																<!-- 																<div class="fileinput fileinput-new"
																	data-provides="fileinput">
																	<div align="center">
																		<input type="file" name="upload" id="upload"
																			accept="image/*">
																	</div>
																</div>
 -->
																<!-- 	<div id="foto">
																	<input type="file" name="upload" id="upload"
																		accept="image/*">
																</div>  -->

																<div id="foto">
																	<a align="center" class="file-input-wrapper btn"><input
																		style="left: -256px; top: 75px;" name="upload"
																		id="upload" accept="image/*" type="file"><img
																		id="imgUser" src="images/user.png" alt="..."+=""></a>
																</div>


															</div>

															<div class="col-md-10">

																<label>Nome <span class="vermelho">*</span></label>

																<div class="form-group">
																	<input type="text" class="form-control" id="inName"
																		name="inName" maxlength="100">
																</div>

															</div>
															<div class="col-md-10">
																<label>E-mail <span class="vermelho">*</span></label>

																<div class="form-group">
																	<input type="text" class="form-control" id="inEmail"
																		name="inEmail" maxlength="100">
																</div>

															</div>
														</div>
													</div>
													<div class="col-md-12">
														<div class="col-md-6 espacamento-left">
															<label>Telefone Comercial <span class="vermelho">*</span></label>

															<div class="form-group">
																<input type="text" class="form-control" id="inFone"
																	name="inFone" class="telefone">
															</div>

														</div>

														<div class="col-md-6 espacamento-right">
															<label>Telefone Pessoal</label>

															<div class="form-group">
																<input type="text" class="form-control" id="inFoneP"
																	name="inFoneP" class="telefone">
															</div>

														</div>
													</div>

													<div class="col-md-12">
														<div class="col-md-6 espacamento-left">

															<div class="combo form-group" id="comboEntity">
																<label>Entidade <span class="vermelho">*</span></label> <select
																	class="form-control selectEntity" id="selectEntity"
																	name="selectEntity">
																</select>
															</div>

														</div>

														<div class="col-md-6 espacamento-right">
															<label>Setor</label>

															<div class="form-group">
																<input type="text" class="form-control" id="inSetor"
																	name="inSetor" maxlength="100">
															</div>

														</div>
													</div>

													<div class="col-md-12">
														<div class="col-md-6 espacamento-left">

															<div class="combo form-group" id="comboPerfil">
																<label>Perfil <span class="vermelho">*</span></label> <select
																	class="form-control selectPerfil" id="selectPerfil"
																	name="selectPerfil">
																</select>
															</div>
														</div>
														<div class="col-md-6 espacamento-right">
															<label>Cargo <span class="vermelho">*</span></label>

															<div class="form-group">
																<input type="text" class="form-control" id="inCargo"
																	name="inCargo" maxlength="100">
															</div>

														</div>
													</div>
													<div class="col-md-12">
														<label>Login <span class="vermelho">*</span></label> <input type="text"
															class="form-control" id="inUsername" name="inUsername"
															maxlength="20"> <input type="hidden"
															class="form-control" id="inEnabled" name="inEnabled">
													</div>
													<div class="col-md-12 camposSenha">
														<label>Senha <span class="vermelho">*</span></label>

														<div class="form-group">
															<input type="password" class="form-control" id="inPass"
																name="inPass" maxlength="12">
														</div>

													</div>
													<div class="col-md-12 camposSenha">
														<label>Confirmação da Senha <span class="vermelho">*</span></label>

														<div class="form-group">
															<input type="password" class="form-control"
																id="inConfirm" name="inConfirm" maxlength="12">
														</div>

													</div>
													
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
															id="btEdit">Salvar</a> <a href="user.jsp"
															class="btn btn-danger tamanho-botao" role="button">Cancelar</a>
													</div>

												</form>
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

								<div id="crop-modal" class="modal fade bs-example-modal-lg"
									aria-hidden="true">
									<div class="modal-dialog modal-crop-user">
										<div class="modal-content">
											<div class="modal-header">
												<button id="fecharModal" type="button" class="close"
													data-dismiss="modal" aria-hidden="true">&times;</button>
												Cortar Imagem
											</div>
											<div class="modal-body height-modal-body">
												<form method="post" enctype="multipart/form-data"
													id="imageForm">
													<div id="error" style="display: none"></div>
													<div class="col-md-12">
														<div id="box" class="col-md-8 espacamento-left">
															<img id="Image1" src="" alt="" />
														</div>
														<div class="col-md-4 espacamento-right"
															style="text-align: center">
															<label>Pré-Visualização</label>
															<canvas id="canvas" height="5" width="5"> </canvas>
														</div>
													</div>

													<div class="col-md-12"
														style="text-align: center; padding-top: 20px">
														<a id="btnRemove"
															class="btn btn-danger fileinput-exists tamanho-botao"
															data-dismiss="fileinput">Cancelar</a> <input
															type="button" class="btn btn-info tamanho-botao"
															id="btnCrop" value="Cortar" /> <input type="hidden"
															name="imgX1" id="imgX1" /> <input type="hidden"
															name="imgY1" id="imgY1" /> <input type="hidden"
															name="imgWidth" id="imgWidth" /> <input type="hidden"
															name="imgHeight" id="imgHeight" /> <input type="hidden"
															name="imgCropped" id="imgCropped" /> <input
															type="button" class="btn btn-success tamanho-botao"
															value="Salvar" name="send" id="send" />
													</div>
												</form>
											</div>
										</div>
										<!-- /.modal-content -->
									</div>
									<!-- /.modal-dialog -->
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
	<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>×</button> {{title}}
				 </div>
				<div class='modal-body'> {{body}}
				</div>
				<div class='modal-footer'>
				<a href="../user.jsp"  style="display: none;" class='btn btn-success dbOk' role='button' id='bOk1'>Ok</a>
<a class='btn btn-success dbOk' data-dismiss='modal'  style="display: none;" role='button' id='bOk2'>Ok</a>
			<a class='btn btn-danger' data-dismiss='modal' style="display: none;" id="bOk3" role='button'>Ok!</a>
&nbsp&nbsp&nbsp
				</div>
			</script>

	<script id="combo-sup-template" type="text/x-handlebars-template">		
	<option value="">Selecione um Perfil</option>
			{{#each .}}
			<option value="{{systemUserProfileId}}">{{name}}</option>
{{/each}}
			</script>


	<script id="combo-ent-template" type="text/x-handlebars-template">	
		<option value="">Selecione uma Entidade</option>
		<option value="-1">Todas</option>
			{{#each .}}
			<option value="{{entityId}}">{{name}}</option>
			{{/each}}
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
	<script type="text/javascript" src="js/libs/jquery.validate.js"></script>
	<script type="text/javascript" src="js/libs/handlebars-v3.0.0.js"></script>
	<script type="text/javascript" src="js/fileinput.js"></script>
	<script type="text/javascript" src="js/libs/jquery.md5.js"></script>	
	
	<!--<script type="text/javascript" src="js/templatesUS.js"></script> -->

	<script type="text/javascript" src="js/template.js"></script>
	<!-- <script type="text/javascript" src="/js/libs/jquery.Jcrop.min.js"></script> -->
	<script type="text/javascript" src="/js/libs/jCrop/js/Jcrop.min.js"></script>
	<script type="text/javascript" src="/js/libs/jCrop/js/jquery.color.js"></script>
	<script type="text/javascript" src="js/libs/jquery.maskedinput.js"></script>
	<script type="text/javascript" src="js/hadlebarsHelpers.js"></script>
	<script type="text/javascript" src="js/ctrlUSadd.js"></script>
</body>
</html>