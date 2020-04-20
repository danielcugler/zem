<%@ page contentType="text/html; charset=UTF-8"%>
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
<title>Moderação de Chamados |</title>
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

<style>
.hiddenRow {
	padding: 0 !important;
}
</style>

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
						<i class='fa fa-table'></i> Chamados

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
										Moderação de <strong>Chamados</strong>
									</h2>

								</div>
								<div class="widget-content">
									<div class="data-table-toolbar">
										<div class="row">
											<fieldset class="fieldset-space">
												<legend>Filtros</legend>
												<div class="col-md-12">
													<form class="form-horizontal" id="search-call-form"
														method="get" onkeypress="return event.keyCode != 13;">
														<div class="col-md-12" style="padding-top: 10px;">
															<div class="form-group form-zero">
																<div class="col-md-4" style="padding: 0;">
																	<div class="col-md-6 has-feedback espacamento-left">
																		<label class="control-label">Data Inicial</label> <input
																			id="fromDate" class="form-control from_date"
																			placeholder="Data Inicial" /> <i
																			class="glyphicon glyphicon-calendar form-control-feedback"
																			contenteditable="false" type="text"></i>
																	</div>
																	<div class="col-md-6  has-feedback espacamento-left">
																		<label class="control-label">Data Final</label> <input
																			id="toDate" name="toDate" class="form-control to_date"
																			placeholder="Data Final" contenteditable="false"
																			type="text" /> <i
																			class="glyphicon glyphicon-calendar form-control-feedback"></i>
																	</div>
																</div>
																<div class="col-md-4 combo" id="comboCallSource">
																	<div class=''>
																		<label>Origem</label><br> <select
																			class="form-control selectCallSource"
																			id="selectCallSource" name="selectCallSource">
																			<option value="">Selecione a Origem</option>
																			<option value="1">Móvel</option>
																			<option value="0">Web</option>
																		</select>
																	</div>
																</div>
																<div class="col-md-4 combo espacamento-right"
																	id="comboEntityCategory">
																	<div class=>
																		<label>Categoria de Entidade</label><br> <select
																			class="form-control selectEntityCategory"
																			id="selectEntityCategory" name="selectEntityCategory">

																		</select>
																	</div>

																</div>
															</div>
														</div>

														<div class="col-md-12" style="padding-top: 10px;">
															<div class="form-group form-zero">

																<div class="col-md-4 combo espacamento-left"
																	id="comboCallClassification">
																	<div class=>
																		<label>Classificação</label><br> <select
																			class="form-control selectCallClassification"
																			id="selectCallClassification"
																			name="selectCallClassification">
																		</select>
																	</div>
																</div>

																<div class="col-md-4 combo" id="comboPriority">
																	<div class=''>
																		<label>Prioridade</label><br> <select
																			class="form-control selectPriority"
																			id="selectPriority" name="selectPriority">
																			<option value="">Selecione uma Prioridade</option>
																			<option value="0">Baixa</option>
																			<option value="1">Média</option>
																			<option value="2">Alta</option>
																		</select>
																	</div>
																</div>
																<div class="col-md-4 espacamento-right">
																	<label>Usuário</label> <input type="text"
																		class="form-control" id="inUser" name="inUser"
																		maxlength="40">
																</div>
															</div>
														</div>

														<div class="col-md-12" style="padding-top: 10px;">
															<div class="form-group form-zero">
																<div class="col-md-4 espacamento-left">
																	<label>CPF</label> <input type="text"
																		class="form-control" id="inCpf" name="inCpf"
																		maxlength="20">
																</div>
																<div class="col-md-4">
																	<label>Identificador</label> <input type="text"
																		class="form-control" id="inId" name="inId"
																		maxlength="20">
																</div>
																<div class="col-md-4 espacamento-right">
																	<fieldset>
																		<legend>Estado: </legend>

																		<input type="radio" name="radio-button" value="0"
																			id="enabled" class="radio-button" /> Ativo <input
																			type="radio" name="radio-button" class="radio-button"
																			value="1" id="disabled" /> Rejeitado <input
																			type="radio" name="radio-button" value="" id="ambos"
																			checked="checked" class="radio-button" /> Todos

																	</fieldset>
																</div>
															</div>
														</div>

														<div class="col-md-6 espacamento-left" id="botoes">
															<a class="btn btn-primary tamanho-botao" id="search"
																role="button"><i class="glyphicon glyphicon-search"></i>
																Buscar</a>
														</div>

													</form>
												</div>
											</fieldset>
										</div>
									</div>
								</div>

								<div id="tabela" class="table-responsive">
									<fieldset>
										<table id="resultado" class="fixo table table-hover scrollTabela">
											<col width="3%" />
											<!-- Seta de expansão -->
											<col width="6%" />
											<!-- ID -->
											<col width="7%"/>
											<!-- Origem -->
											<col width="10%" />
											<!-- Destino -->
											<col width="16%" />
											<!-- Classificação -->
											<col width="9%" />
											<!-- Prioridade -->
											<col width="25%" />
											<!-- Descrição -->
											<col width="7%" />
											<!-- Mídia -->
											<col width="120px" />
											<!-- Status -->
											<sec:authorize
												access="hasRole('ROLE_CALL_SEND') or hasRole('ROLE_CALL_UPDATE') or hasRole('ROLE_CALL_REPLY') or hasRole('ROLE_CALL_ENABLE')">
												<col width="150px" />
												<!-- Ações -->
											</sec:authorize>

											<thead id="cthead">
												<tr>
													<th class="paddingEsquerdo"></th>
													<th class="centerTabela">ID</th>
													<th class="centerTabela" style="min-width:70px;">Origem</th>
													<th class="paddingEsquerdo">Destino</th>
													<th class="paddingEsquerdo">Classificação</th>
													<th class="paddingEsquerdo">Prioridade</th>
													<th class="paddingEsquerdo">Descrição</th>
													<th class="centerTabela">Mídia</th>
													<th class="centerTabela">Status</th>
													<th class="colunaAcoes">Ações</th>

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

								<div class="modal fade in" tabindex="-1" role="dialog"
									id="modalMsg">
									<div class="modal-dialog ">
										<div class="modal-content">
											<br /> <br />
											<div id=modalText></div>
											<br>
											<div id="modalButtons"></div>
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
	<script id="combo-cc-template" type="text/x-handlebars-template">
<option value="">Selecione uma Classificação</option>
			{{#each .}}
			<option value="{{callClassificationId}}">{{name}}</option>
{{/each}}
	</script>

	<script id="combo-ec-template" type="text/x-handlebars-template">
<option value="">Selecione uma Categoria</option>
			{{#each .}}
			<option value="{{entityCategoryId}}">{{name}}</option>
			{{/each}}
	</script>

	<script id="sdmodal-template" type="text/x-handlebars-template">
	<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>×</button> Encaminhar chamado
				 </div>
				<div class='modal-body'> Você tem certeza que deseja encaminhar o chamado {{idp}}?
				</div>
				<div class='modal-footer'>
				<a class='btn btn-success pbSim' role='button' data-id='{{id}}' id='sbSim'>Sim</a>&nbsp&nbsp&nbsp
				<a class='btn btn-danger' data-dismiss='modal' role='button' id='sbNao'>Não</a>
				</div>
			</script>


	<script id="edmodal-template" type="text/x-handlebars-template">
	<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>×</button> Ativar/Inativar chamado
				 </div>
				<div class='modal-body'> Você tem certeza que deseja {{msg}} o chamado {{idp}}?
				</div>
				<div class='modal-footer'>
				<a class='btn btn-success dbSim' role='button' data-link='{{link}}' data-id='{{id}}' id='bSim'>Sim</a>&nbsp&nbsp&nbsp
				<a class='btn btn-danger' data-dismiss='modal' role='button' id='bNao'>Não</a>
				</div>
			</script>

	<script id="edmessage-template" type="text/x-handlebars-template">
	<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>×</button> {{title}}
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
	<script type="text/javascript" src="js/libs/jquery.simplePagination.js"></script>


	<!--<script type="text/javascript" src="js/templatesCM.js"></script> -->
	<script type="text/javascript" src="js/hadlebarsHelpers.js"></script>
	<!--	<script type="text/javascript" src="js/crudClienteCM.js"></script> -->
	<!--	<script type="text/javascript" src="js/messagesCM.js"></script> -->
	<script type="text/javascript" src="js/ctrlMON.js"></script>

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
										$(this).valid();
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