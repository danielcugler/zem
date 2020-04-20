var ctrlMONT = (function($, module) {
		
//	 google.load("visualization", "1", {
//	 packages : [ "corechart" ]
//	 });
	 google.setOnLoadCallback(drawChart);
	
		var filters = [];
		var search = [];
		search[0] = [];
		search[1] = [];
		search[2] = [];
		search[3] = [];
	 
	 
	 // security rules
	var list = $('#list li').map(function() {
		return $(this).text();
	});
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	var roleEdit = $.inArray('ROLE_CALL_UPDATE', list) >= 0;
	// cached DOM
	// general module
	var $generalModule = $("#generalModule");
	var $tableModule = $generalModule.find("#resultado");
	var $thead = $tableModule.find("#cthead");
	var $tbody = $tableModule.find("#ctbody");
	// templates
	function tableTpl2() {
		var string = "{{#each .}}"
			+ "<div >{{unsolvedCallId}}</div>"
			 + "{{/each}}";
		return string;	
	}
	function tableTpl() {
		var string = "{{#each .}}"
			
			+ "{{#ifCond delay '===' 'Azul'}}"
				+ "<tr class = 'table-blue'>"
				+ "{{/ifCond}}"
				+ "{{#ifCond delay '===' 'Verde'}}"
				+ "<tr class = 'table-green'>"
				+ "{{/ifCond}}"
				+ "{{#ifCond delay '===' 'Laranja'}}"
				+ "<tr class = 'table-orange'>"
				+ "{{/ifCond}}"
				+ "{{#ifCond delay '===' 'Vermelho'}}"
				+ "<tr class = 'table-red'>"
				+ "{{/ifCond}}"
				+ "{{#ifCond delay '===' 'Branco'}}"
				+ "<tr>"
				+ "{{/ifCond}}"
				
				+ "<td >{{parentCallId.unsolvedCallId}}</td>"
				+ "<td><a href=\"http://localhost:8080/callsHistory.jsp?id={{parentCallId.unsolvedCallId}}\"><u>{{unsolvedCallId}}</u></a></td>" // coluna
			
				// ID
				// (ID
				// do
				// último
				// filho)
				+ "{{#ifCond callSource '===' 'Web'}}"
				+ "<td><span class=\"hideTD\">{{callSource}}</span>&nbsp;&nbsp;<i class='icon-monitor-1'></i></td>" // coluna
				// origem
				+ "{{else}}"
				+ "<td><span class=\"hideTD\">{{callSource}}</span>&nbsp;&nbsp;<i class='icon-mobile-2'></i></td>" // coluna
				// origem
				+ "{{/ifCond}}"
				+ "<td>{{entityEntityCategoryMaps.entity.name}}</td>" // coluna
				// entidade
				+ "<td>{{callClassificationId.name}}</td>" // coluna
				// classificação
				+ "<td>{{parentCallId.creationOrUpdateDate}}</td>" // coluna
				// data
				+ "<td><select class=\"form-control selectPriority selectpicker\">"
				+ "{{#ifCond priority '===' 'Baixa'}}"
				+ "<option value=\"0\" selected>Baixa</option>"
				+ "<option value=\"1\">Média</option>"
				+ "<option value=\"2\">Alta</option>"
				+ "{{/ifCond}}"
				+ "{{#ifCond priority '===' 'Média'}}"
				+ "<option value=\"1\" selected>Média</option>"
				+ "<option value=\"0\">Baixa</option>"
				+ "<option value=\"2\">Alta</option>"
				+ "{{/ifCond}}"
				+ "{{#ifCond priority '===' 'Alta'}}"
				+ "<option value=\"2\" selected>Alta</option>"
				+ "<option value=\"0\">Baixa</option>"
				+ "<option value=\"1\">Média</option>"
				+ "{{/ifCond}}"
				+ "</select></td>"
				+ "<td>{{description.information}}</td>" // coluna descrição
				+ "{{#ifNull firstPhoto}}"
				+ "<td>&nbsp;&nbsp;&nbsp;<a type = 'button' target = '_self' class='view-media'><img src=\"{{firstPhoto}}\" height=\"25\" width=\"25\"></a></td>"
				+ "{{else}}"
				+ "<td>&nbsp;&nbsp;&nbsp;<a type = 'button' target = '_self'><img src=\"../images/noImage.png\" height=\"25\" width=\"25\"></a></td>"
				+ "{{/ifNull}}" + "<td>{{callProgress}}</td>" // coluna status
				
				+ "</tr>" + "{{/each}}";
		return string;
	}
	// functions
	function _render(baseTemplate, selector, data) {
		var template = Handlebars.compile(baseTemplate);
		var html = template(data);
		selector.html(html);
	}
	
	
	
	
	
	
	var arrCol1 = [];
	var arrCol2 = [];
	var arrCol3 = [];

	var arrCol4 = [];
	var arrCol5 = [];
	// $("#filtrar").click(function() {
	// $("table").on('click','.filter',function() {
	// $('.filter').click(function(){
	$('.btnFilter')
			.on(
					'click',
					function(event) {
						// if($(this).hasClass('filter1'))
						arrCol1 = $(
								'.col1 input[type="checkbox"]:checked')
								.map(function() {
									return $(this).val();
								}).toArray();
						// else if($(this).hasClass('filter2'))
						arrCol2 = $(
								'.col2 input[type="checkbox"]:checked')
								.map(function() {
									return $(this).val();
								}).toArray();
						// else if($(this).hasClass('filter3'))
						arrCol3 = $(
								'.col3 input[type="checkbox"]:checked')
								.map(function() {
									return $(this).val();
								}).toArray();
						// else if($(this).hasClass('filter4'))
						arrCol4 = $(
								'.col4 input[type="checkbox"]:checked')
								.map(function() {
									return $(this).val();
								}).toArray();
						// else if($(this).hasClass('filter5'))
						arrCol5 = $(
								'.col5 input[type="checkbox"]:checked')
								.map(function() {
									return $(this).val();
								}).toArray();

						var indexColumn1 = 0;
						var indexColumn2 = 1;
						var indexColumn3 = 2;
						var indexColumn4 = 3;
						var indexColumn5 = 4;
						var trs = $('tbody tr').hide();
						var data = arrCol1;
						var data2 = arrCol2;
						var data3 = arrCol3;
						var data4 = arrCol4;
						var data5 = arrCol5;
						trs
								.filter(
										function(i, v) {
											var $t = $(this)
													.children(
															":eq("
																	+ indexColumn1
																	+ ")");
											var $t2 = $(this)
													.children(
															":eq("
																	+ indexColumn2
																	+ ")");
											var $t3 = $(this)
													.children(
															":eq("
																	+ indexColumn3
																	+ ")");
											var $t4 = $(this)
													.children(
															":eq("
																	+ indexColumn4
																	+ ")");
											var $t5 = $(this)
													.children(
															":eq("
																	+ indexColumn5
																	+ ")");

											var bool1 = false, bool2 = false, bool3 = false, bool4 = false, bool5 = false;
											if (!arrCol1.length) {
												bool1 = true;
											}
											if (!arrCol2.length) {
												bool2 = true;
											}
											if (!arrCol3.length) {
												bool3 = true;
											}
											if (!arrCol4.length) {
												bool4 = true;
											}
											if (!arrCol5.length) {
												bool5 = true;
											}
											// if
											// ($t2.is(":contains('"
											// + data2[d2] +
											// "')"))
											for (var d = 0; d < data.length; ++d) {
												if ($($t)
														.text() === data[d]) {
													console
															.log(data[d]);
													bool1 = true;
												}
											}
											for (var d2 = 0; d2 < data2.length; ++d2) {
												if ($($t2)
														.text() === data2[d2]) {
													console
															.log(data2[d2]);
													bool2 = true;
												}
											}
											for (var d3 = 0; d3 < data3.length; ++d3) {
												if ($($t3)
														.text() === data3[d3]) {
													console
															.log(data3[d3]);
													bool3 = true;
												}
											}
											for (var d4 = 0; d4 < data4.length; ++d4) {
												if ($($t3)
														.text() === data4[d4]) {
													console
															.log(data4[d4]);
													bool4 = true;
												}
											}
											for (var d5 = 0; d5 < data5.length; ++d5) {
												if ($($t5)
														.text() === data3[d5]) {
													console
															.log(data5[d5]);
													bool5 = true;
												}
											}
											if (bool1 && bool2
													&& bool3
													&& bool4
													&& bool5)
												return true;

											return false;
										})
								// show the rows that match.
								.show();

					});

	// filter results based on query
	function filter(selector, query) {
		query = $.trim(query); // trim white space
		query = query.replace(/ /gi, '|'); // add OR for regex
		// query

		$(selector).each(
				function() {
					($(this).text().search(
							new RegExp(query, "i")) < 0) ? $(
							this).hide().removeClass('visible')
							: $(this).show()
									.addClass('visible');
				});
	}

	// filter

	function getFilters2(pg) {

		var page = pg;
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
	}
	$("#resultado").on('click', '.btnFilter', function(e) {
		e.stopPropagation();
		getFilters2(getCurrentPage());
	});
	
	
	
	
	
	
	
	
	
	

	function searchMO() {
		$
				.ajax(
						{
							type : 'GET',
							url : '../rest/callmonitor2/search',
							dataType : "json",
							// data : data2,
							beforeSend : function(xhr) {
								xhr.setRequestHeader(header, token);
							},
							contentType : 'application/json',
							success : function(resp, status, xhr) {
								_render(tableTpl(), $tbody, resp.list);
								_render(tableTpl2(),$("#teste"), resp.list);
								var count = parseInt(xhr
										.getResponseHeader('X-Total-Count'));
								var perPage = parseInt(xhr
										.getResponseHeader('X-Per-Page'));
								// valoresGraficos(resp.charts);
							}

							,
							error : function(resp) {
								callbackError();
							},
							complete : function(jqXHR, status) {

								$("#resultado")
										.tablesorter(
												{
													widthFixed : true,
													widgets : [ 'columns',
															'filter' ],
													tableClass : 'tablesorter',
													cssAsc : "tablesorter-headerSortUp",
													cssDesc : "tablesorter-headerSortDown",
													cssHeader : "tablesorter-header",
													cssHeaderRow : "tablesorter-headerRow",
													cssIcon : "tablesorter-icon",
													cssChildRow : "tablesorter-childRow",
													cssInfoBlock : "tablesorter-infoOnly",
													cssProcessing : "tablesorter-processing",
													selectorHeaders : '> thead th, > thead td',
													selectorSort : "th, td",
													selectorRemove : "tr.remove-me",
													initialized : function(
															table) {
														var targetColumn = 2; // age
														for (; targetColumn < 10; targetColumn++) {
															var opts = $.tablesorter.filter
																	.getOptionSource(
																			table,
																			targetColumn);
															var html = '<li>'
																	+ opts
																			.map(
																					function(
																							elem) {
																						if (targetColumn != 5
																								&& targetColumn != 6
																								&& targetColumn != 7
																								&& targetColumn != 8)
																							return '<input type="checkbox" data-column="'
																									+ targetColumn
																									+ '" value="'
																									+ elem.text
																									+ '">'
																									+ elem.text;
																					})
																			.join(
																					'</li><li>')
																	+ '</li>';
															$(
																	'.search'
																			+ targetColumn)
																	.append(
																			html);
															$(
																	'.search'
																			+ targetColumn)
																	.on(
																			'change',
																			'input',
																			function(
																					e) {
																				if ($(
																						this)
																						.is(
																								":checked")) {
																					search[$(
																							this)
																							.data(
																									"column")]
																							.push($(
																									this)
																									.val());
																				} else {
																					search[$(
																							this)
																							.data(
																									"column")]
																							.splice(
																									search[$(
																											this)
																											.data(
																													"column")]
																											.indexOf($(
																													this)
																													.val()),
																									1);
																				}
																				filters[$(
																						this)
																						.data(
																								"column")] = search[$(
																						this)
																						.data(
																								"column")]
																						.join("|");
																				$.tablesorter
																						.setFilters(
																								$('table'),
																								filters,
																								true);
																			});
														}
													}

												})
										.tablesorterPager(
												{
													container : $("#pager"), // starting
													ajaxProcessing : function(
															ajax) {
														if (ajax
																&& ajax
																		.hasOwnProperty('data')) {
															return [
																	ajax.data,
																	ajax.total_rows ];
														}
													},
													output : '{startRow} to {endRow} ({totalRows})',
													updateArrows : true,
													page : 0,
													size : 10,
													fixedHeight : true,
													removeRows : false,
													cssNext : '.next',
													cssPrev : '.prev',
													cssFirst : '.first',
													cssLast : '.last',
													cssGoto : '.gotoPage',
													cssPageDisplay : '.pagedisplay',
													cssPageSize : '.pagesize',
													cssDisabled : 'disabled'
												});

							}
						}).done(function() {
				});

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
		if (data.callClassificationCounter
				.hasOwnProperty("Pedidos de informação"))
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

	function drawChart(emdia, emalerta, atrasados, entidades, novo,
			encaminhado, processado, visualizado, emandamento, reclamacao,
			denuncia, solicitacao, sugestao, elogio, pedidosdeinformacao,
			critica) {
		// Gráfico tempo de atendimento
		var valoresAtendimento = google.visualization.arrayToDataTable([
				[ 'Situação', 'Quantidade' ], [ 'Em dia', emdia ],
				[ 'Em alerta', emalerta ], [ 'Atrasados', atrasados ] ]);

		var options = {
			titlePosition : 'none',
			pieStartAngle : 135,
			chartArea : {
				width : '80%',
				height : '80%'
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
		var valoresStatus = google.visualization
				.arrayToDataTable([ [ 'Status', 'Quantidade' ],
						[ 'Novo', novo ], [ 'Encaminhado', encaminhado ],
						[ 'Visualizado', visualizado ],
						[ 'Em andamento', emandamento ],
						[ 'Processado', processado ] ]);

		var options3 = {
			titlePosition : 'none',
			pieStartAngle : 135,
			chartArea : {
				width : '80%',
				height : '80%'
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
				[ 'Classificação', 'Quantidade' ],
				[ 'Reclamação', reclamacao ], [ 'Denúncia', denuncia ],
				[ 'Crítica', critica ], [ 'Solicitação', solicitacao ],
				[ 'Sugestão', sugestao ],
				[ 'Pedidos de informação', pedidosdeinformacao ],
				[ 'Elogio', elogio ] ]);

		var options4 = {
			titlePosition : 'none',
			pieStartAngle : 135,
			chartArea : {
				width : '80%',
				height : '80%'
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

	searchMO();

})(jQuery, ctrlMONT || {});