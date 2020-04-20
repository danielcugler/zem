google.load("visualization", "1", {
	packages : [ "corechart" ]
});
// google.setOnLoadCallback(drawChart);
var selector = '#ctbody';
var templateHandlebars = ModelsTmpl.tmplCallMonitorResult;
var str1 = "Monitor de Chamados";
var str2 = "Monitor de Chamados";
var str3 = "o";
var recordPerPage = 10;
var tamanho = 380;

var getCurrentPage = function() {
	return $("#pagination").pagination('getCurrentPage');
}

$(document)
		.ready(
				function() {

					$(window).resize(
							function() {
								drawChart(emdia, emalerta, atrasados,
										entidades, novo, encaminhado,
										processado, visualizado, emandamento,
										reclamacao, denuncia, solicitacao,
										sugestao, elogio, pedidosdeinformacao,
										critica);
							});

					$(document)
							.on(
									'webkitfullscreenchange mozfullscreenchange fullscreenchange MSFullscreenChange',
									function() {
										calculaTamanho();
									});

					var token = $("meta[name='_csrf']").attr("content");
					var header = $("meta[name='_csrf_header']").attr("content");
					var initDate, endDate, filterSource, filterEntity, filterClassification, filterPriority, filterProgress;
					var column, order;
					$("#column").val(4);
					$("#order").val(1);

					function getFilters2() {

						var page = getCurrentPage();
						var entity = $(
								'#filterEntity input[type="checkbox"]:checked')
								.map(function() {
									return $(this).attr('id');
								}).toArray();
						var classification = $(
								'#filterClassification input[type="checkbox"]:checked')
								.map(function() {
									return $(this).attr('id');
								}).toArray();
						var callSource = $(
								'#filterSource input[type="checkbox"]:checked')
								.map(function() {
									return $(this).attr('id');
								}).toArray();
						var callProgres = $(
								'#filterProgress input[type="checkbox"]:checked')
								.map(function() {
									return $(this).attr('id');
								}).toArray();
						var priority = $(
								'#filterPriority input[type="checkbox"]:checked')
								.map(function() {
									return $(this).attr('id');
								}).toArray();
						orderParam = $("#column").val();
						order = $("#order").val();

						callMonitorFilter = {
							'callSource' : callSource,
							'callProgress' : callProgres,
							'priority' : priority,
							'classification' : classification,
							'entity' : entity,
							'orderParam' : orderParam,
							'order' : order
						};

						searchToken('../rest/callmonitor/search', JSON
								.stringify(callMonitorFilter),
								templateHandlebars(roleEdit), selector, page,
								header, token);
					}
					$("#resultado").on('click', '.btnFilter', function(e) {
						e.stopPropagation();
						getFilters2();
					});

					function searchToken(URLL, data2, templateHandlebars,
							selector, currentPage, header, token) {
						var count = 0;

						function callbackSuccess(data) {
							if (data.jsonList !== "[]"
									&& data.jsonList !== null) {
								template(data, templateHandlebars, selector);
								count = data.count;
								valoresGraficos(JSON.parse(data.graph));
							} else {
								$("#cthead").css("display", "none");
								$("#tabela").html(
										"<div align='center'><h4>"
												+ data.message + "</h4></div>");
							}
						}
						function callbackError() {
						}

						$.ajax({
							type : 'POST',
							url : URLL,
							dataType : "json",
							data : data2,
							beforeSend : function(xhr) {
								xhr.setRequestHeader(header, token);
							},
							contentType : "application/json",
							success : function(resp) {
								callbackSuccess(resp);
							},
							error : function(data) {
								// Mensagem de erro
								callbackError();
							},
							complete : function(jqXHR, status) {
								callbackComplete(count, recordPerPage,
										currentPage);

							}
						});
					}

					/*
					 * private List<String> callSource; private List<String>
					 * entity; private List<String> priority; private List<String>
					 * classification; private List<String> callProgres;
					 * 
					 * private int orderParam; private int order;
					 */

					var list = $('#list li').map(function() {
						return $(this).text();
					});

					var roleEdit = $.inArray('ROLE_CALL_UPDATE', list) >= 0;

					comboET('../rest/entityclass',
							ModelsTmpl.tmplListEntity, ".columnEntity");
					searchInicialMO('../rest/callmonitor/search/', 'z/z/z/z/z/z/z/4/1/z',
							templateHandlebars(roleEdit), selector, 1);

					// refresh every 5 seconds
					// var refresher = setInterval(function()
					// {
					// searchInicial(URL + '/search/',getCurrentPage(),
					// getFilters(),
					// templateHandlebars(roleEdit), selector);
					// }, 25000);

					// $("#buttonFilter").tooltip();
					$("#fullscreen").tooltip();

					// Multiselect
					$('#selectEntidade')
							.multiSelect(
									{
										selectableHeader : "<div class='custom-header'><label>Entidades Existentes</label></div>",
										selectionHeader : "<div class='custom-header'><label>Entidades selecionadas</label></div>",
									// selectableFooter : "<div
									// class='custom-footer'><a
									// id='selecionarTodos'
									// onClick='selecionaTudo();'>Selecionar
									// todas</a></div>",
									// selectionFooter : "<div
									// class='custom-footer'><a
									// id='removerTodos'
									// onClick='removeTudo();'>Remover
									// todas</a></div>"
									});

					// Preencher o select com as entidades disponíveis
					function mSelect(data) {
						var list2 = JSON.parse(data.jsonList);
						var cont2 = 0;
						for (cont2 = 0; cont2 < list2.length; cont2++)
							$('#selectEntidade').multiSelect('addOption', {
								value : list2[cont2].entityId,
								text : list2[cont2].name,
								index : cont2
							});
					}

					// AJAX | Busca todos as categorias de entidade
					function selectListAll() {
						$.ajax({
							type : 'GET',
							async : false,
							url : '../rest/entityclass',
							dataType : "json",
							contentType : "application/json",
							success : function(data) {
								mSelect(data);
							},
							error : function(jqxhr, status, errorMsg) {
								// Mensagem de erro
							}
						});
					}

					selectListAll();

					var getFilters = function() {

						filterSource = $("#valueSource").val();
						filterEntity = $("#valueEntity").val();
						filterClassification = $("#valueClassification").val();
						filterPriority = $("#valuePriority").val();
						filterProgress = $("#valueProgress").val();
						filterColumn = $("#column").val();
						filterOrder = $("#order").val();
						return '/z/z/' + filterSource + '/' + filterEntity
								+ '/' + filterClassification + '/'
								+ filterPriority + '/' + filterProgress + '/'
								+ filterColumn + '/' + filterOrder + '/z';
					};
					/*
					 * // Filtros da Tabela de Chamados. $("li") .click(
					 * function() { var input = $(this).closest("ul").find(
					 * "input"); $(input).val($(this).val());
					 * 
					 * filterSource = $("#valueSource").val(); filterEntity =
					 * $("#valueEntity").val(); filterClassification = $(
					 * "#valueClassification").val(); filterPriority =
					 * $("#valuePriority") .val(); filterProgress =
					 * $("#valueProgress") .val();
					 * 
					 * $("#valueColumn").val(column);
					 * $("#valueOrder").val(order);
					 * 
					 * searchInicialMO( "http://localhost:8080/rest/callmonitor" +
					 * '/search/', 'z/z/' + filterSource + '/' + filterEntity +
					 * '/' + filterClassification + '/' + filterPriority + '/' +
					 * filterProgress + '/' + column + '/' + order + '/z',
					 * templateHandlebars(roleEdit), selector); });
					 */
					// Ordenação das colunas da Tabela de Chamados.
					$("#resultado").on(
							'click',
							'.sorting',
							function(e) {
								if (!$(e.target).is('.btn-group')) {
									var lastColumnClicked = $("#lastColumn")
											.val();
									column = this.id;
									var columnOrder = $(this).find(
											"div:nth-child(1)");
									var columnOrderId = $(columnOrder).attr(
											"id");

									// se o icone fa-sort tiver aparecendo,
									// muda para o icone fa-sort-asc e
									// ordena crescente. Além disso também
									// pega a ultima coluna ordenada e dá a
									// ela a classe fa-sort.

									if ($('#' + columnOrderId).hasClass(
											"fa-sort")) {
										$('#' + columnOrderId).removeClass(
												"fa-sort").addClass(
												"fa-sort-asc");
										order = 0;
										$('#' + lastColumnClicked).removeClass(
												"fa-sort-desc").removeClass(
												"fa-sort-asc").addClass(
												"fa-sort");
									} else {
										// se for fa-sort-asc muda pra
										// fa-sort-desc e ordena
										// decrescente.
										if ($('#' + columnOrderId).hasClass(
												"fa-sort-asc")) {
											$('#' + columnOrderId).removeClass(
													"fa-sort-asc").addClass(
													"fa-sort-desc");
											order = 1;
										} else {
											// se for fa-sort-desc, muda pra
											// fa-sort-asc e ordena
											// crescente.
											$('#' + columnOrderId).removeClass(
													"fa-sort-desc").addClass(
													"fa-sort-asc");
											order = 0;
										}
									}

									$("#column").val(column);
									$("#order").val(order);

									filterSource = $("#valueSource").val();
									filterEntity = $("#valueEntity").val();
									filterClassification = $(
											"#valueClassification").val();
									filterPriority = $("#valuePriority").val();
									filterProgress = $("#valueProgress").val();
									/*
									 * searchInicialMO(
									 * "http://localhost:8080/rest/callmonitor" +
									 * '/search/', 'z/z/' + filterSource + '/' +
									 * filterEntity + '/' + filterClassification +
									 * '/' + filterPriority + '/' +
									 * filterProgress + '/' + column + '/' +
									 * order + '/z',
									 * templateHandlebars(roleEdit), selector);
									 */
									getFilters2();

									lastColumnClicked = $("#lastColumn").val(
											columnOrderId);
								}
							});

					// Barra de divisão de seções.
					$("#escondeAcima").click(function() {
						$("#tabela").toggle();
						$("#graficos").show();
					});

					$("#escondeAbaixo").click(
							function() {
								$("#tabela").show();
								$("#graficos").toggle();

								calculaTamanho();
								drawChart(emdia, emalerta, atrasados,
										entidades, novo, encaminhado,
										processado, visualizado, emandamento,
										reclamacao, denuncia, solicitacao,
										sugestao, elogio, pedidosdeinformacao,
										critica);
							});

					function getParent(botao) {
						return $(botao).closest("tr").find("td:nth-child(2)")
								.text();
					}

					$("#ctbody").on("click", ".view-media", function(event) {
						event.preventDefault();
						botao = $(this);

						findMedia('../rest/callmonitor', getParent(botao));

					});

					// Alteração da prioridade do chamado.
					function getRowData(botao) {
						return $(botao).closest("tr").find("td:nth-child(1)")
								.text();
					}

					$(".#resultado").on(
							'change',
							'.selectPriority',
							function() {
								var buttonCP = $(this);
								var priority = buttonCP.val();
								var username = $('#username').text();
								var $tdCmId = getRowData(buttonCP);
								$.ajax({
									type : 'PUT',
									url : '../rest/callmonitor/pr/' + $tdCmId + '/'
											+ priority + '/'
											+ $('#username').text(),
									dataType : "json",
									beforeSend : function(xhr) {
										xhr.setRequestHeader(header, token);
									},
									contentType : "application/json",
									success : function(data) {

										searchInicial('../rest/callmonitor/search/', $(
												"#pagination").pagination(
												'getCurrentPage'),
												'/z/z/z/z/z/z/z/z',
												templateHandlebars(roleEdit),
												selector);

									},
									error : function(data) {
									}
								});
							});

				});

// Botão Fullscreen.
function toggle_fullscreen(e) {
	e.preventDefault();
	var fullscreenEnabled = document.fullscreenEnabled
			|| document.mozFullScreenEnabled
			|| document.webkitFullscreenEnabled;

	if (fullscreenEnabled) {
		if (!document.fullscreenElement && !document.mozFullScreenElement
				&& !document.webkitFullscreenElement
				&& !document.msFullscreenElement) {
			tamanho = 250;
			launchIntoFullscreen(document.getElementById("showFullscreen"));
			$("#fullscreen").removeClass("icon-resize-full-1").addClass(
					"icon-resize-small-1");
		} else {
			$("#fullscreen").removeClass("icon-resize-small-1").addClass(
					"icon-resize-full-1");
			tamanho = 380;
			exitFullscreen();
		}
	}
	// calculaTamanho()
}

var emdia, emalerta, atrasados;
var novo, encaminhado, processado, visualizado, emandamento;
var reclamacao, denuncia, solicitacao, sugestao, elogio, pedidosdeinformacao, critica;
var entidades = [];

function drawChart(emdia, emalerta, atrasados, entidades, novo, encaminhado,
		processado, visualizado, emandamento, reclamacao, denuncia,
		solicitacao, sugestao, elogio, pedidosdeinformacao, critica) {
	// Gráfico tempo de atendimento
	var valoresAtendimento = google.visualization.arrayToDataTable([
			[ 'Situação', 'Quantidade' ], [ 'Em dia', emdia ],
			[ 'Em alerta', emalerta ], [ 'Atrasados', atrasados ] ]);

	var options = {
		titlePosition : 'none',
		pieStartAngle : 135,
		chartArea : {
			width : '90%',
			height : '90%'
		},
		slices : {
			0 : {
				color : '#98FB98'
			},
			1 : {
				color : '#F6AD6F'
			},
			2 : {
				color : '#F56D6F'
			}
		}
	};

	var chart = new google.visualization.PieChart(document
			.getElementById('graficoAtendimento'));

	chart.draw(valoresAtendimento, options);

	// Gráfico Entidades
	var valoresEntidades = google.visualization.arrayToDataTable(entidades);

	var options2 = {
		titlePosition : 'none',
		chartArea : {
			width : '70%'
		},
		legend : 'none',
		hAxis : {
			title : 'Quantidade',
			minValue : 0
		},
		vAxis : {
			title : 'Entidades'
		}
	};

	var chart2 = new google.visualization.BarChart(document
			.getElementById('graficoEntidades'));
	chart2.draw(valoresEntidades, options2);

	// Gráfico Status
	var valoresStatus = google.visualization.arrayToDataTable([
			[ 'Status', 'Quantidade' ], [ 'Novo', novo ],
			[ 'Encaminhado', encaminhado ], [ 'Visualizado', visualizado ],
			[ 'Em andamento', emandamento ], [ 'Processado', processado ] ]);

	var options3 = {
		titlePosition : 'none',
		pieStartAngle : 135,
		chartArea : {
			width : '90%',
			height : '90%'
		},
		slices : {
			0 : {
				color : '#87CEFF'
			},
			1 : {
				color : '#F56D6F'
			},
			2 : {
				color : '#C6B0D5'
			},
			3 : {
				color : '#F6AD6F'
			},
			4 : {
				color : '#98FB98'
			}
		}
	};

	var chart3 = new google.visualization.PieChart(document
			.getElementById('graficoStatus'));

	chart3.draw(valoresStatus, options3);

	// Gráfico Classificação
	var valoresClassificacao = google.visualization.arrayToDataTable([
			[ 'Classificação', 'Quantidade' ], [ 'Reclamação', reclamacao ],
			[ 'Denúncia', denuncia ], [ 'Crítica', critica ],
			[ 'Solicitação', solicitacao ], [ 'Sugestão', sugestao ],
			[ 'Pedidos de informação', pedidosdeinformacao ],
			[ 'Elogio', elogio ] ]);

	var options4 = {
		titlePosition : 'none',
		pieStartAngle : 135,
		chartArea : {
			width : '90%',
			height : '90%'
		},
		slices : {
			0 : {
				color : '#F56D6F'
			},
			1 : {
				color : '#F6AD6F'
			},
			2 : {
				color : '#BEBAB1'
			},
			3 : {
				color : '#87CEFF'
			},
			4 : {
				color : '#C6B0D5'
			},
			5 : {
				color : '#FFD2D9'
			},
			6 : {
				color : '#98FB98'
			}
		}
	};

	var chart4 = new google.visualization.PieChart(document
			.getElementById('graficoClassificacao'));

	chart4.draw(valoresClassificacao, options4);
}

function valoresGraficos(data) {
	var index;

	// Em dia
	if (data.callDelayCounter.hasOwnProperty("Em dia"))
		emdia = data.callDelayCounter["Em dia"];
	else
		emdia = 0;

	// Em alerta
	if (data.callDelayCounter.hasOwnProperty("Em alerta"))
		emalerta = data.callDelayCounter["Em alerta"];
	else
		emalerta = 0;

	// Atrasado
	if (data.callDelayCounter.hasOwnProperty("Atrasado"))
		atrasados = data.callDelayCounter.Atrasado;
	else
		atrasados = 0;

	// Entidades
	var count = 0;
	entidades = [ [ 'Entidades', 'Quantidade' ] ];
	for ( var key in data.entityCounter) {
		count++;
		var temp = [];
		temp.push(key);
		temp.push(data.entityCounter[key]);
		entidades.push(temp);
		if (count >= 5) {
			break;
		}
	}

	// Novo
	if (data.callProgressCounter.hasOwnProperty("Novo"))
		novo = data.callProgressCounter.Novo;
	else
		novo = 0;

	// Encaminhado
	if (data.callProgressCounter.hasOwnProperty("Encaminhado"))
		encaminhado = data.callProgressCounter.Encaminhado;
	else
		encaminhado = 0;

	// Processado
	if (data.callProgressCounter.hasOwnProperty("Processado"))
		processado = data.callProgressCounter.Processado;
	else
		processado = 0;

	// Visualizado
	if (data.callProgressCounter.hasOwnProperty("Visualizado"))
		visualizado = data.callProgressCounter.Visualizado;
	else
		visualizado = 0;

	// Em andamento
	if (data.callProgressCounter.hasOwnProperty("Em andamento"))
		emandamento = data.callProgressCounter["Em andamento"];
	else
		emandamento = 0;

	// Reclamação
	if (data.callClassificationCounter.hasOwnProperty("Reclamação"))
		reclamacao = data.callClassificationCounter["Reclamação"];
	else
		reclamacao = 0;

	// Denúncia
	if (data.callClassificationCounter.hasOwnProperty("Denúncia"))
		denuncia = data.callClassificationCounter["Denúncia"];
	else
		denuncia = 0;

	// Crítica
	if (data.callClassificationCounter.hasOwnProperty("Crítica"))
		critica = data.callClassificationCounter["Crítica"];
	else
		critica = 0;

	// Sugestão
	if (data.callClassificationCounter.hasOwnProperty("Sugestão"))
		sugestao = data.callClassificationCounter["Sugestão"];
	else
		sugestao = 0;

	// Elogio
	if (data.callClassificationCounter.hasOwnProperty("Elogio"))
		elogio = data.callClassificationCounter["Elogio"];
	else
		elogio = 0;

	// Pedidos de informação
	if (data.callClassificationCounter.hasOwnProperty("Pedidos de informação"))
		pedidosdeinformacao = data.callClassificationCounter["Pedidos de informação"];
	else
		pedidosdeinformacao = 0;

	// Solicitação
	if (data.callClassificationCounter.hasOwnProperty("Solicitação"))
		solicitacao = data.callClassificationCounter["Solicitação"];
	else
		solicitacao = 0;

	drawChart(emdia, emalerta, atrasados, entidades, novo, encaminhado,
			processado, visualizado, emandamento, reclamacao, denuncia,
			solicitacao, sugestao, elogio, pedidosdeinformacao, critica);
}

function calculaTamanho() {

	var janela = $(window).height();

	if ($("#graficos").is(":visible"))
		recordPerPage = 10;
	else
		recordPerPage = Math.floor((janela - tamanho) / $("tr").height());

	$("#pagination").pagination('updateItemsOnPage', recordPerPage);

	var items = $("#ctbody tr");
	var count = items.size();
	var currentPage = getCurrentPage();

	callbackComplete(count, recordPerPage, currentPage);
}

function callbackComplete(count, recordPerPage, currentPage) {

	var items = $("#ctbody tr");
	// only show the first 2 (or "first per_page") items initially
	items.show();
	items.slice(recordPerPage).hide();

	$("#pagination").pagination({
		items : count,
		itemsOnPage : recordPerPage,
		cssStyle : "light-theme",
		currentPage : currentPage,
		onInit : function() {
			if (count < recordPerPage + 1)
				$("#pagination").hide();
			else
				$("#pagination").show();
			$(".prev").hide();
		},
		onPageClick : function(pageNumber) {

			var showFrom = recordPerPage * (pageNumber - 1);
			var showTo = showFrom + recordPerPage;
			items.hide() // first hide everything, then show for the new page
			.slice(showFrom, showTo).show();
			if (pageNumber * recordPerPage > count - recordPerPage)
				$(".next").hide();
			else
				$(".next").show();
			$(".prev").show();
			$("#pagination").show();
		}
	});
};
