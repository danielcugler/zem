<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Chamado Público |</title>
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="description" content="Zeladoria Urbana Participativa">
<meta name="keywords"
	content="zeladoria urbaba participativa, zeladoria urbana">
<meta name="author" content="LabITec">
<meta name="_csrf" content="${_csrf.token}" />
<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}" />
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
	<!-- Modal Start -->

	<!-- Modal Logout -->
	<div class="md-modal md-just-me" id="logout-modal"></div>
	<!-- Modal End -->
	<!-- Begin page -->

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
			<div id="generalModule" class="content">
				<!-- Page Heading Start -->
				<div class="page-heading">
					<h1>
						<i class='icon-megaphone-1'></i> Registro de Chamado Público
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
										&nbsp&nbspRegistro de <strong>Chamado</strong>
									</h2>
								</div>
								<div class="widget-content">
									<div class="data-table-toolbar">
										<form id="formModule" onkeypress="return event.keyCode != 13;">
											<div class="row">
												<div class="col-md-12">
													<div id="col-md-4">
														<div class="filter" id="campos">
															<div class="col-md-12" style="padding-left: 0;">

																<div class="col-md-4">
																	<div class="combo form-group"
																		id="comboCallClassification">
																		<label>Classificação <span class="vermelho">*</span></label><br> <select
																			class="form-control selectCC"
																			id="selectCC"
																			name="selectCC">
																		</select>
																	</div>
																</div>
															</div>
															<div class="col-md-12">
																<div class="col-md-12" style="padding-left: 0;">
																	<div class="col-md-11" style="padding-left: 0;">
																		<label>Descrição <span class="vermelho">*</span></label>
																	</div>
																	<div class="col-md-1">
																		<div class="col-md-10"></div>
																		<div class="col-md-2">
																			<button type="button" class="btn btn-blue-2 btn-xs"
																				data-toggle="tooltip" data-placement="left"
																				title="É recomendável que se forneça o máximo de informações possíveis na descrição, com o intuito de agilizar a resolução do chamado.">

																				<i class="icon-info"></i>
																			</button>
																		</div>
																	</div>
																</div>
																<div class="form-group col-md-12" style="padding-left: 0;">
																	<textarea id="inDescription" rows="10" cols="40"
																		class="form-control" maxlength="4000"
																		name="inDescription"></textarea>
																</div>
															</div>
															
															<div class="col-md-12 form-group no-address hide">
																<input class="form-control" type="checkbox" name="addressRequired" id="addressRequired">
																<label>Não é necessário informar endereço</label>
															</div>
															
															<div class="col-md-12">
																<div class="col-md-6" style="padding-left: 0;">
																	<label>Logradouro <span class="vermelho">*</span></label>
																	<div class="form-group">
																		<input type="text" class="form-control" id="inStreet"
																			name="inStreet" maxlength="100">
																	</div>
																</div>
																<div class="col-md-3" style="padding-left: 0;padding-right:0;">
																	<div class="col-md-4" style="padding-left: 0;">
																		<label>Número <span class="vermelho">*</span></label>

																		<div class="form-group">
																			<input type="text" class="form-control" id="inNumber"
																				name="inNumber" maxlength="5">
																		</div>
																	</div>
																	<div class="col-md-8" style="padding-left: 0;">
																		<label>Complemento</label>

																		<div class="form-group">
																			<input type="text" class="form-control"
																				id="inComplement" name="inComplement" maxlength="20">
																		</div>

																	</div>
																</div>
																<div class="col-md-3" style="padding-left: 0;">
																	<div class="combo form-group" id="comboNB">
																		<label>Bairro <span class="vermelho">*</span></label><br> <select
																			class="form-control selectNB" name="selectNB"
																			id="selectNB">
																		</select>
																	</div>
																</div>
															</div>
															<div class="col-md-12">
																<div class="col-md-4" style="padding-left: 0;">
																	<div class="combo form-group" id="comboEN">
																		<label>Entidade <span class="vermelho">*</span></label><br> <select
																			class="form-control selectEntity"
																			id="selectEN" name="selectEN">
																		</select>
																	</div>
																</div>
																<div class="col-md-4" style="padding-left: 0;">
																	<div class="combo form-group" id="comboEC">
																		<label>Categoria de Entidade <span class="vermelho">*</span></label><br> <select
																			class="form-control selectEC"
																			id="selectEC" name="selectEC"
																			disabled>
																			<option value="" selected>Selecione uma Categoria</option>
																		</select>
																	</div>
																</div>
													
															<div class="col-md-4">
																<input id="test" style="visibility: hidden;" /><br />
																<input type="checkbox" id="ckAnonymity"> <label
																	for="anonymity">Chamado sigiloso?</label>
															</div>
															</div>

													
															<br />
															<div class="col-md-12 midia">
																<br /> <input type="checkbox" id="ckNomidia"
																	name="ckNomidia"> <label>Não informar
																	mídias</label>
															</div>
															<div class="col-md-12" id="upload">
																<p id="erro"></p>
																<fieldset>
																	<legend> Mídias</legend>
																	<div class="show-image img1">
																		<div class="foto" id="foto">
																														
																	 		<a align="center" class="file-input-wrapper btn"><input
																				style="left: -256px; top: 75px;" name="upload1"
																				id="upload1" accept="image/*" type="file"><img
																				id="imgUser" src="images/camera.png" alt="..."></a>
																				
																				 
																		</div>
																		<button type="button" id="clean1"
																			class="btn btn-xs btn-danger delete">
																			<span class="glyphicon glyphicon-remove"></span>
																		</button>
																	</div>
																	<div class="show-image img2">
																		<div class="foto" id="foto">
																			<a align="center" class="file-input-wrapper btn"><input
																				style="left: -256px; top: 75px;" name="upload2"
																				id="upload2" accept="image/*" type="file"><img
																				id="imgUser" src="images/camera.png" alt="..."></a>
																		</div>
																		<button type="button" id="clean2"
																			class="btn btn-xs btn-danger delete">
																			<span class="glyphicon glyphicon-remove"></span>
																		</button>
																	</div>
																	<div class="show-image img3">
																		<div class="foto" id="foto">
																			<a align="center" class="file-input-wrapper btn"><input
																				style="left: -256px; top: 75px;" name="upload3"
																				id="upload3" accept="image/*" type="file"><img
																				id="imgUser" src="images/camera.png" alt="..."></a>
																		</div>
																		<button type="button" id="clean3"
																			class="btn btn-xs btn-danger delete">
																			<span class="glyphicon glyphicon-remove"></span>
																		</button>
																	</div>
																</fieldset>

															</div>

															<div class="col-md-12">
																<p style="margin-top: 10px;">
																	<label class="vermelho"><i>* Campos
																			obrigatórios</i></label>
																</p>
															</div>
														</div>
													</div>
												</div>


												<div id="botoes" align="center">
													<br />
													<button type="button" class="btn btn-success" id="save">
														<span class="" aria-hidden="true"></span> Salvar
													</button>
													<a class="btn btn-danger" id="cancel" type="button"
														target="_self" href="identification.jsp"> <span
														class="" aria-hidden="true"></span> Cancelar
													</a>
												</div>
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


	<!-- End right content -->


	<!-- End of page -->
	<!-- the overlay modal element -->
	<div class="md-overlay"></div>
	<!-- End of eoverlay modal -->
	<script>
		var resizefunc = [];
	</script>

	<script id="message-template" type="text/x-handlebars-template">
	<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>X</button> {{title}}
				 </div>
				<div class='modal-body'> {{body}}
				</div>
				<div class='modal-footer'>
				<a href="../index.jsp"  style="display: none;" class='btn btn-success dbOk' role='button' id='bOk1'>Ok</a>
<a class='btn btn-success dbOk' data-dismiss='modal'  style="display: none;" role='button' id='bOk2'>Ok</a>
			<a class='btn btn-danger' data-dismiss='modal' style="display: none;" id="bOk3" role='button'>Ok!</a>
&nbsp&nbsp&nbsp
				</div>
			</script>
	<script id="combo-cc-template" type="text/x-handlebars-template">
<option value="" selected>Selecione uma	Classificação</option>			
			{{#each .}}
			<option value="{{callClassificationId}}" data-address-required="{{addressRequired}}">{{name}}</option>
			{{/each}}
	</script>

	<script id="combo-nb-template" type="text/x-handlebars-template">	
<option value="" selected>Selecione um Bairro</option>																					
			{{#each .}}
			<option value="{{neighborhoodId}}">{{name}}</option>"
			{{/each}}
	</script>

	<script id="combo-en-template" type="text/x-handlebars-template">
			<option value="" selected>Selecione uma Entidade</option>
			{{#each .}}
			<option value="{{entityId}}">{{name}}</option>
			{{/each}}			
</script>

	<script id="combo-ec-template" type="text/x-handlebars-template">
	<option value="" selected>Selecione uma Categoria</option>
																			
			{{#each .}}
			<option value="{{entityCategoryId}}">{{name}}</option>{{/each}}
</script>

	<script id="success-template" type="text/x-handlebars-template">

<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>X</button>
			Registro de Chamado Público</div>
			<div class='modal-body'>Chamado Registrado com sucesso.
			</div>
			<div class='modal-footer new-call-button'><a onClick="window.location.reload()" class='btn btn-danger' data-dismiss='modal' role='button'>Novo Chamado</a>
			<a href="../index.jsp" class='btn btn-danger exit-button' role='button'>Sair</a>
			</div>
</script>

<script id="success-mail-template" type="text/x-handlebars-template">
	<div class='modal-header'>
		<button class='close' aria-hidden='true' data-dismiss='modal' type='button'>X</button>
		Registro de Chamado Público
	</div>
	<div class='modal-body'>
		<p>Chamado Registrado com sucesso.</p>
		<p>Em breve o usuário informado receberá um e-mail com o link para acompanhamento.</p>
	</div>
	<div class='modal-footer new-call-button'>
		<a onClick="window.location.reload()" class='btn btn-danger' data-dismiss='modal' role='button'>Novo Chamado</a>
		<a href="../index.jsp" class='btn btn-danger exit-button' role='button'>Sair</a>
	</div>
</script>

	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<!--<script src="assets/libs/jquery/jquery-1.11.1.min.js"></script>-->
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

<!-- <script type="text/javascript" src="js/template.js"></script>  -->
	<script type="text/javascript" src="js/libs/jquery.validate.js"></script>
	<!-- <script type="text/javascript" src="js/abstractCRUD.js"></script> -->
	<script type="text/javascript" src="js/ctrlNC.js"></script>
</body>
</html>