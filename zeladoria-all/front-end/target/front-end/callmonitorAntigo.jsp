<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="_csrf" content="${_csrf.token}" />
<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}" />
<title>Monitor de Chamados |</title>
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

<link rel="stylesheet" href="assets/css/multiselect-call-monitor.css">

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
				<div>

				</div>
				<!-- Page Heading End-->
				<!-- Your awesome content goes here -->
				<div class="row">
					<div class="row">
						<div class="col-md-12" id="showFullscreen">
							<div class="widget">
								<div class="widget-content">

									<!-- Início | Botão Filtros | Botão de expandir/restaurar -->
									<div class="data-table-toolbar">
										<div class="row">
											<div class="form-group">

												<div class="col-md-6 monitor-title" align="left">
													<h2>
														<i class='fa fa-table'></i> Monitor de <strong>Chamados</strong>
													</h2>
												</div>

												<div class="col-md-6" align="right">

													<!-- Início Botão Filtro													
													<button type="button" id="buttonFilter"
														class="btn btn-blue-2 icon-filter" aria-hidden="true"
														data-toggle="collapse" data-target="#showFilters" title="Filtros"></button>
														
													<div id="showFilters" class="data-table-toolbar col-md-12 row collapse">
															<fieldset>
																<legend>Filtros</legend>
																<div class="col-md-12" align="left">
																	<div class="form-group">
																	
																	<div class="col-md-5">
																			<br>
																			<div class="form-group">
																					<select align="center" multiple="multiple"
																						id="selectEntidade" name="my-select[]"
																						class="form-control multi-select">
						
																					</select>																							
																			</div>																																																						
																	</div>
																	
																	<div class="col-md-2 classification">
																			<label>Classificação</label><br>
																			<input type="checkbox" id="0" value="0"/>Reclamação<br>
																			<input type="checkbox" id="1" value="1"/>Crítica<br>
																			<input type="checkbox" id="2" value="2"/>Denúncia<br>
																			<input type="checkbox" id="3" value="3"/>Sugestão<br>
																			<input type="checkbox" id="4" value="4"/>Elogio<br>
																			<input type="checkbox" id="5" value="5"/>Solicitações<br>
																			<input type="checkbox" id="6" value="6"/>Pedidos de Informação<br>																		
																		</div>	
																		
																		<div class="col-md-2 callProgres">
																			<label>Status</label><br>																			
																			<input type="checkbox" id="0" value="0"/>Novo<br>
																			<input type="checkbox" id="1" value="1"/>Encaminhado<br>
																			<input type="checkbox" id="2" value="2"/>Processado<br>
																			<input type="checkbox" id="4" value="4"/>Visualizado<br>
																			<input type="checkbox" id="5" value="5"/>Em Andamento<br>														
																		</div>
																		
																		<div class="col-md-2">
																		<div class="col-md-12 priority">
																			<label>Prioridade</label><br>																																						
																			<input type="checkbox" id="0" value="0"/>Baixa<br>
																			<input type="checkbox" id="1" value="1"/>Média<br>
																			<input type="checkbox" id="2" value="2"/>Alta<br><br>
																		</div>
																		<div class="col-md-12 callSource">
																			<label>Origem</label><br>
																			<input type="checkbox" id="0" value="0"/>Web<br>
																			<input type="checkbox" id="1" value="1"/>Móvel<br>
																			</div>
																		</div>
																																			
																		<div class="col-md-1 filterbtn">
																			<div class="btn-group">
																			<br>
																				<button class="btn btn-primary icon-filter" id="btnFiltrar">Filtrar</button>
																			</div>
																		</div>																																					
																	</div>
																</div>																
															</fieldset>
													</div>													
													 Fim Botão Filtro -->

													<!-- Início Botão Fullscreen -->
													<button type="button"
														class="btn btn-blue-2 icon-resize-full-1"
														aria-hidden="true" id="fullscreen"
														onclick="javascript:toggle_fullscreen(event)"
														title="Expandir/Restaurar"></button>
													<!-- Fim Botão Fullscreen -->
												</div>
											</div>
										</div>
									</div>
									<!-- Fim | Botão Filtros | Botão de expandir/restaurar -->

									<!-- Início | Tabela -->
									<div id="tabela" class="table-responsive callmonitor">
										<input type="hidden" value="z" id="order" /> <input
											type="hidden" value="z" id="column" />

										<fieldset>
											<table id="resultado" class="fixo table table-hover">
												<col width="6%" />
												<!-- ID -->
												<col width="10%" />
												<!-- Origem -->
												<col width="10%" />
												<!-- Entidade -->
												<col width="15%" />
												<!-- Classificação -->
												<col width="8%" />
												<!-- Data -->
												<col width="11%" />
												<!-- Prioridade -->
												<col width="19%" />
												<!-- Descrição -->
												<col width="10%" />
												<!-- Mídia -->
												<col width="10%" />
												<!-- Status -->
												<thead id="cthead">
													<input type="hidden" id="lastColumn" value="orderdate" />
													<tr role="row">
														<th class="sorting cursor" id="0" value="0">
															<div class="col-md-6">ID</div>
															<div class="col-md-6 fa fa-sort" id="orderid"></div>
														</th>

														<th class="sorting cursor" id="1" value="1">Origem
															<div class="btn-group">
																<div class="icon-filter btn btn-group dropdown-toggle"
																	data-toggle="dropdown" aria-haspopup="true"
																	aria-expanded="false">
																	<span class="sr-only">Toggle Dropdown</span>
																</div>
																<ul class="dropdown-menu" id="filterSource" role="menu">
																	<input type="hidden" value="z" id="valueSource" />
																	<li value="0"><input type="checkbox" id="0" />Web</li>
																	<li value="1"><input type="checkbox" id="1" />Móvel</li>
																	<li><span
																		class="btn btn-success btn-block btnFilter"
																		id="btnSource">Filtrar</span></li>
																</ul>
															</div>
															<div class="fa fa-sort" id="ordersource" align="right"></div>
														</th>

														<th class="sorting cursor columnEntity" id="2" value="2">
															<!-- Entidade
														<div class="btn-group">
																<div class="icon-filter btn btn-group dropdown-toggle"
																	data-toggle="dropdown" aria-haspopup="true"
																	aria-expanded="false">
																	<span class="sr-only">Toggle Dropdown</span>
																</div>
																<ul class="dropdown-menu" id="filterEntity" role="menu">
																
																	<li><span class="btn btn-success btn-block">Filtrar</span></li>
																</ul>
															</div>
															<div class="fa fa-sort" id="orderentity" align="right"></div>  -->
														</th>

														<th class="sorting cursor" id="3" value="3">
															Classificação
															<div class="btn-group">
																<div class="icon-filter btn btn-group dropdown-toggle"
																	data-toggle="dropdown" aria-haspopup="true"
																	aria-expanded="false">
																	<span class="sr-only">Toggle Dropdown</span>
																</div>
																<ul class="dropdown-menu" id="filterClassification"
																	role="menu">
																	<input type="hidden" value="z" id="valueClassification" />
																	<li value="0"><input type="checkbox" id="0" />Reclamação</li>
																	<li value="1"><input type="checkbox" id="1" />Crítica</li>
																	<li value="2"><input type="checkbox" id="2" />Denúncia</li>
																	<li value="3"><input type="checkbox" id="3" />Sugestão</li>
																	<li value="4"><input type="checkbox" id="4" />Elogio</li>
																	<li value="5"><input type="checkbox" id="5" />Solicitações</li>
																	<li value="6"><input type="checkbox" id="6" />Pedidos
																		de Informação</li>
																	<li><span
																		class="btn btn-success btn-block btnFilter"
																		id="btnClassification">Filtrar</span></li>
																</ul>
															</div>
															<div class="fa fa-sort" id="orderclassification"
																align="right"></div>
														</th>

														<th class="sorting cursor" id="4" value="4">
															<div class="col-md-6">Data</div>
															<div class="col-md-6 fa fa-sort-desc" id="orderdate"
																align="right"></div>
														</th>

														<th class="sorting cursor" id="5" value="5">
															Prioridade
															<div class="btn-group">
																<div class="icon-filter btn btn-group dropdown-toggle"
																	data-toggle="dropdown" aria-haspopup="true"
																	aria-expanded="false">
																	<span class="sr-only">Toggle Dropdown</span>
																</div>
																<ul class="dropdown-menu" id="filterPriority"
																	role="menu">
																	<input type="hidden" value="z" id="valuePriority" />
																	<li value="0"><input type="checkbox" id="0" />Baixa</li>
																	<li value="1"><input type="checkbox" id="1" />Média</li>
																	<li value="2"><input type="checkbox" id="2" />Alta</li>
																	<li><span
																		class="btn btn-success btn-block btnFilter"
																		id="btnPriority">Filtrar</span></li>
																</ul>
															</div>
															<div class="fa fa-sort priority" id="orderpriority"
																align="right"></div>
														</th>

														<th class="sorting cursor" id="6" value="6">
															<div class="col-md-6">Descrição</div>
															<div class="col-md-6 fa fa-sort" id="orderdescription"
																align="right"></div>
														</th>

														<th class="cursor" id="media" value="media">
															<div class="col-md-6">Mídia</div>
														</th>

														<th class="sorting cursor" id="7" value="7">Status
															<div class="btn-group">
																<div class="icon-filter btn btn-group dropdown-toggle"
																	data-toggle="dropdown" aria-haspopup="true"
																	aria-expanded="false">
																	<span class="sr-only">Toggle Dropdown</span>
																</div>
																<ul class="dropdown-menu" id="filterProgress"
																	role="menu">
																	<input type="hidden" value="z" id="valueProgress" />
																	<li value="0"><input type="checkbox" id="0" />Novo</li>
																	<li value="1"><input type="checkbox" id="1" />Encaminhado</li>
																	<li value="2"><input type="checkbox" id="2" />Processado</li>
																	<li value="4"><input type="checkbox" id="4" />Vizualizado</li>
																	<li value="5"><input type="checkbox" id="5" />Andamento</li>
																	<li><span
																		class="btn btn-success btn-block btnFilter"
																		id="btnProgress">Filtrar</span></li>
																</ul>
															</div>
															<div class="fa fa-sort" id="orderprogress" align="right"></div>
														</th>
													</tr>
												</thead>
												<tbody id="ctbody">
												</tbody>
											</table>
											<br />
											<div align="right">
												<div id="pagination"></div>
											</div>
										</fieldset>
									</div>
								</div>
							</div>

							<!-- Fim | Tabela -->

							<!-- Início | Barra de divisão de seções -->
							<div class="widget">
								<div class="widget-content">
									<div class="col-md-12" align="center"
										style="background-color: #3498DB;">
										<button type="button" class="btn btn-blue-2 btn-xs"
											id="escondeAcima">
											<span class="glyphicon glyphicon-chevron-up"
												aria-hidden="true"></span>
										</button>

										<button type="button" class="btn btn-blue-2 btn-xs"
											id="escondeAbaixo">
											<span class="glyphicon glyphicon-chevron-down"
												aria-hidden="true"></span>
										</button>
									</div>
								</div>
							</div>
							<!-- Fim | Barra de divisão de seções -->

							<div class="widget">
								<div class="widget-content" id="graficos">
									<fieldset>

										<!-- Início | Gráficos -->
										<div class="col-md-12">
											<div class=col-md-6 align="center">
												<br />
												<fieldset>
													<h4>
														<b>Tempo de Atendimento</b>
													</h4>
													<div class="grid">
														<div class="col-1-2">
															<div id="graficoAtendimento" class="chart"></div>
														</div>
													</div>
												</fieldset>
											</div>

											<div class=col-md-6 align="center">
												<br />
												<fieldset>
													<h4>
														<b>Ranking de Entidades</b>
													</h4>
													<div class="grid">
														<div class="col-1-2">
															<div id="graficoEntidades" class="chart"></div>
														</div>
													</div>
												</fieldset>
											</div>

											<div class=col-md-6 align="center">
												<br />
												<fieldset>
													<h4>
														<b>Status dos Chamados</b>
													</h4>
													<div class="grid">
														<div class="col-1-2">
															<div id="graficoStatus" class="chart"></div>
														</div>
													</div>
												</fieldset>
												<br />
											</div>

											<div class=col-md-6 align="center">
												<br />
												<fieldset>
													<h4>
														<b>Classificação dos Chamados</b>
													</h4>
													<div class="grid">
														<div class="col-1-2">
															<div id="graficoClassificacao" class="chart"></div>
														</div>
													</div>
												</fieldset>
												<br />
											</div>
										</div>
									</fieldset>
									<!-- Fim | Gráficos -->

									<!-- Início | Modal de mensagens -->
									<div class='modal fade in' role='dialog' id='modaldemensagem'
										aria-hidden='true'>
										<div class='modal-dialog'>
											<div class='modal-content'>
												<div id="notificacoes"></div>
											</div>
										</div>
									</div>
									<!-- Fim | Modal de mensagens -->

									<!-- Início | Modal de visualização de mídias -->
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
									<!-- Fim | Modal de visualização de mídias -->


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
	<script type="text/javascript"
		src="https://www.gstatic.com/charts/loader.js"></script>
	<script type="text/javascript" src="http://www.google.com/jsapi?ext.js"></script>
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
	<script type="text/javascript" src="js/libs/jquery.multi-select.js"></script>
	<script type="text/javascript" src="js/template.js"></script>
	<script type="text/javascript" src="js/abstractCRUD.js"></script>
	<script type="text/javascript" src="js/controllerMO.js"></script>
</body>
</html>