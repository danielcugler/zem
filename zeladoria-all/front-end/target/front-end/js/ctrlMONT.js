var ctrlMONT = (function($, module) {
	var recordPerPage = 10;
	var tamanho = 380;
	$(document)
			.on(
					'webkitfullscreenchange mozfullscreenchange fullscreenchange MSFullscreenChange',
					function() {
						calculaTamanho();
					});

	// google.load("visualization", "1", {
	// packages : [ "corechart" ]
	// });
	google.setOnLoadCallback(drawChart);

	var emdia, emalerta, atrasados;
	var novo, encaminhado, processado, visualizado, emandamento;
	var reclamacao, denuncia, solicitacao, sugestao, elogio, pedidosdeinformacao, critica;
	var entidades = [];
	var filters = [];
	var search = [];
	search[0] = [];
	search[1] = [];
	search[2] = [];
	search[3] = [];
	search[4] = [];
	search[5] = [];
	search[6] = [];
	search[7] = [];
	search[8] = [];
	search[9] = [];

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
	// graph module
	var $graphModule = $("#graficos");
	// tableModule
	var $divTable = $generalModule.find("#tabela");
	var $tableModule = $generalModule.find("#resultado");
	var $thead = $tableModule.find("#cthead");
	var $tbody = $tableModule.find("#ctbody");
	// username
	var username = $generalModule.find('#username').text();
	// buttons
	var $hideTable = $generalModule.find("#escondeAcima");
	var $hideGraph = $generalModule.find("#escondeAbaixo");

	// checkbox
	var $selectAll = $generalModule.find(".selectAll");

	// modal
	var $modalE = $generalModule.find("#modalE");
	var $notificacoesE = $generalModule.find("#notificacoesE");
	var $modalMidia = $generalModule.find("#modaldemidia");
	// bind events
	$tbody.on('click', '.view-media', viewMedias);
	$hideTable.click(hideTable);
	$hideGraph.click(hideGraph);
	$tableModule.on('change', '.selectPriority', changePriority);
	$tableModule.on('click', 'th', changeOrder);
	// $('.filterTh').on('click', sortColumn);
	// templates
	var $messageTpl = $("#message-template").html();
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
				+ "<td class=\"hideTD\">{{unsolvedCallId}}</td>"
				+ "<td><a href=\"http://localhost:8080/callsHistory.jsp?id={{parentCallId.unsolvedCallId}}\"><u>{{parentCallId.unsolvedCallId}}</u></a></td>" // coluna
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
				+ "<td><select data-id='{{unsolvedCallId}}' class=\"form-control selectPriority selectpicker\">"
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
				+ "<td>&nbsp;&nbsp;&nbsp;<a data-id='{{parentCallId.unsolvedCallId}}' type = 'button' target = '_self' class='view-media'><img src=\"{{firstPhoto}}\" height=\"25\" width=\"25\"></a></td>"
				+ "{{else}}"
				+ "<td>&nbsp;&nbsp;&nbsp;<a type = 'button' target = '_self'><img src=\"../images/noImage.png\" height=\"25\" width=\"25\"></a></td>"
				+ "{{/ifNull}}" + "<td>{{callProgress}}</td>" // coluna status
				+ "<td class=\"hideTD\"></td>" + "</tr>" + "{{/each}}";
		return string;
	}
	console.log($divTable.height());
	// functions
	function viewMedias(event) {
		event.preventDefault();
		$.ajax({
			type : 'GET',
			url : '../rest/unsolvedcall3/media/' + $(this).data("id"),
			dataType : "json",
			contentType : "application/json"
		}).done(function(resp, status, xhr) {
			for ( var key in resp)
				resp[key] = {
					"img" : resp[key]
				};
			var photo1 = viewMedia({
				'medias' : resp
			});
			var photo2 = mediaList({
				'medias' : resp
			});
			tempViewMedia(photo1, photo2);
			$modalMidia.modal('show');
		}).fail(function(jqXHR, textStatus) {
		});
	}
	function sortColumn(event) {
		if (!$(event.target).is('.btn-group')
				|| !$(event.target).is('.dropdown-menu'))
			$('table').trigger('sorton',
					[ [ [ $(this).data('column'), 'n' ] ] ]);
	}
	$(window).resize(
			function() {
				drawChart(emdia, emalerta, atrasados, entidades, novo,
						encaminhado, processado, visualizado, emandamento,
						reclamacao, denuncia, solicitacao, sugestao, elogio,
						pedidosdeinformacao, critica);
			});
	function _render(baseTemplate, selector, data) {
		var template = Handlebars.compile(baseTemplate);
		var html = template(data);
		selector.html(html);
	}
	function modal(title, body, op) {
		_render($messageTpl, $notificacoesE, {
			'title' : title,
			'body' : body
		});
		if (op == 1)
			$notificacoesE.find("#bOk1").show();
		else if (op == 2)
			$notificacoesE.find("#bOk2").show();
		else
			$notificacoesE.find("#bOk3").show();
		$modalE.modal('show');
	}
	// Barra de divisão de seções.
	function hideTable(event) {
		event.stopPropagation();
		$divTable.toggle();
		$graphModule.show();
	}
	function hideGraph(event) {
		event.stopPropagation();
		$divTable.show();
		$graphModule.toggle();
		calculaTamanho();
		drawChart(emdia, emalerta, atrasados, entidades, novo, encaminhado,
				processado, visualizado, emandamento, reclamacao, denuncia,
				solicitacao, sugestao, elogio, pedidosdeinformacao, critica);

	}

	function calculaTamanho() {
		console.log($divTable.height());
		var janela = $(window).height();

		if ($("#graficos").is(":visible"))
			recordPerPage = 10;
		else
			recordPerPage = Math.floor((janela - 400) / $("tr").height());

		// recordPerPage = Math.floor((janela - tamanho) / $("tr").height());

		$tableModule.trigger('pageSize', recordPerPage);

		var items = $("#ctbody tr");
		var count = items.size();
		// var currentPage = getCurrentPage();
		var currentPage = $('table')[0].config.page;
		// callbackComplete(count, recordPerPage, currentPage);
	}

	// Botão Fullscreen.
	function toggle_fullscreen() {
		var fullscreenEnabled = document.fullscreenEnabled
				|| document.mozFullScreenEnabled
				|| document.webkitFullscreenEnabled;
		if (fullscreenEnabled) {
			if (!document.fullscreenElement && !document.mozFullScreenElement
					&& !document.webkitFullscreenElement
					&& !document.msFullscreenElement) {
				// launchIntoFullscreen(document.documentElement); fullscreen na
				// tela toda.
				launchIntoFullscreen(document.getElementById("showFullscreen"));
				$("#fullscreen").removeClass("icon-resize-full-1").addClass(
						"icon-resize-small-1");// fullscreen
				// em
				// determinado
				// elemento.
			} else {
				$("#fullscreen").removeClass("icon-resize-small-1").addClass(
						"icon-resize-full-1");
				exitFullscreen();
			}
		}
	}
	// Alteração da prioridade do chamado.
	function changePriority(event) {
		event.stopPropagation();
		var id = $(this).data("id");
		var priority = $(this).val();
		var username = $('#username').text();
		$.ajax(
				{
					type : 'PUT',
					url : '../rest/callmonitor2/pr/' + id + '/' + priority
							+ '/' + username,
					dataType : "json",
					beforeSend : function(xhr) {
						xhr.setRequestHeader(header, token);
					},
					contentType : "application/json"
				}).done(function(resp, textStatus, jqXHR) {
			$tableModule.trigger("destroy");
			searchMO()
		}).fail(
				function(jqXHR, textStatus, errorThrown) {
					var body = jqXHR.responseJSON.errorCode + ", "
							+ jqXHR.responseJSON.errorMessage;
					modal("Monitor de chamados", body, 2);
				});
	}
	;

	// Alteração da seta de ordenação.
	function changeOrder(event) {
		if (!$(event.target).is('.btn-group, :checkbox, ul, li')) {
			var lastColumnClicked = $("#lastColumn").val();
			column = this.id;
			var columnOrder = $(this).find("div div.sortId");
			var columnOrderId = $(columnOrder).attr("id");

			$('table').trigger('sorton',
					[ [ [ $(this).data('column'), 'n' ] ] ]);

			// se o icone fa-sort tiver aparecendo,
			// muda para o icone fa-sort-asc e
			// ordena crescente. Além disso também
			// pega a ultima coluna ordenada e dá a
			// ela a classe fa-sort.

			if ($('#' + columnOrderId).hasClass("fa-sort")) {
				$('#' + columnOrderId).removeClass("fa-sort").addClass(
						"fa-sort-asc");
				order = 0;
				$('#' + lastColumnClicked).removeClass("fa-sort-desc")
						.removeClass("fa-sort-asc").addClass("fa-sort");
			} else {
				// se for fa-sort-asc muda pra
				// fa-sort-desc e ordena
				// decrescente.
				if ($('#' + columnOrderId).hasClass("fa-sort-asc")) {
					$('#' + columnOrderId).removeClass("fa-sort-asc").addClass(
							"fa-sort-desc");
					order = 1;
				} else {
					// se for fa-sort-desc, muda pra
					// fa-sort-asc e ordena
					// crescente.
					$('#' + columnOrderId).removeClass("fa-sort-desc")
							.addClass("fa-sort-asc");
					order = 0;
				}
			}

			$("#column").val(column);
			$("#order").val(order);

			filterSource = $("#valueSource").val();
			filterEntity = $("#valueEntity").val();
			filterClassification = $("#valueClassification").val();
			filterPriority = $("#valuePriority").val();
			filterProgress = $("#valueProgress").val();
			/*
			 * searchInicialMO( "http://localhost:8080/rest/callmonitor" +
			 * '/search/', 'z/z/' + filterSource + '/' + filterEntity + '/' +
			 * filterClassification + '/' + filterPriority + '/' +
			 * filterProgress + '/' + column + '/' + order + '/z',
			 * templateHandlebars(roleEdit), selector);
			 */
			// getFilters2();
			lastColumnClicked = $("#lastColumn").val(columnOrderId);
		}
	}

	// Método responsável por exibir os checkbox com o CSS do plugin iCheck
	// assim que o tablesorter for inicializado.
	$tableModule.bind("tablesorter-initialized", function(e, table) {
		setTimeout(function() {
			$('input:not(.ios-switch)').iCheck('destroy');
			$('input:not(.ios-switch)').iCheck({
				checkboxClass : 'icheckbox_square-aero col-md-3',
				radioClass : 'iradio_square-aero',
				increaseArea : '20%'
			});
		}, 0);
	});
	
	// Opção selecionar todos os checkboxes.
	$(".selectAll").on("ifChecked", function(){
		$(".marcar").iCheck("check");
	});
	$(".selectAll").on("ifUnchecked", function(){
		$(".marcar").iCheck("uncheck");
	});

	function searchMO() {
		$
				.ajax({
					type : 'GET',
					url : '../rest/callmonitor2/search',
					dataType : "json",
					beforeSend : function(xhr) {
						xhr.setRequestHeader(header, token);
					},
					contentType : 'application/json'
				})
				.done(
						function(resp, textStatus, jqXHR) {
							_render(tableTpl(), $tbody, resp.list);
							var count = parseInt(jqXHR
									.getResponseHeader('X-Total-Count'));
							var perPage = parseInt(jqXHR
									.getResponseHeader('X-Per-Page'));
							valoresGraficos(resp.charts);
							$.tablesorter.characterEquivalents = {
								'a' : '\u00e1\u00e0\u00e2\u00e3\u00e4\u0105\u00e5', // áàâãäąå
								'A' : '\u00c1\u00c0\u00c2\u00c3\u00c4\u0104\u00c5', // ÁÀÂÃÄĄÅ
								'c' : '\u00e7\u0107\u010d', // çćč
								'C' : '\u00c7\u0106\u010c', // ÇĆČ
								'e' : '\u00e9\u00e8\u00ea\u00eb\u011b\u0119', // éèêëěę
								'E' : '\u00c9\u00c8\u00ca\u00cb\u011a\u0118', // ÉÈÊËĚĘ
								'i' : '\u00ed\u00ec\u0130\u00ee\u00ef\u0131', // íìİîïı
								'I' : '\u00cd\u00cc\u0130\u00ce\u00cf', // ÍÌİÎÏ
								'o' : '\u00f3\u00f2\u00f4\u00f5\u00f6\u014d', // óòôõöō
								'O' : '\u00d3\u00d2\u00d4\u00d5\u00d6\u014c', // ÓÒÔÕÖŌ
								'ss' : '\u00df', // ß (s sharp)
								'SS' : '\u1e9e', // ẞ (Capital sharp s)
								'u' : '\u00fa\u00f9\u00fb\u00fc\u016f', // úùûüů
								'U' : '\u00da\u00d9\u00db\u00dc\u016e' // ÚÙÛÜŮ
							};

							$tableModule
									.tablesorter(
											{
												widthFixed : true,
												sortLocaleCompare : true,
												widgets : [ 'columns', 'filter' ],
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
												initialized : function(table) {
													var targetColumn = 2; // age
													for (; targetColumn < 10; targetColumn++) {
														var opts = $.tablesorter.filter
																.getOptionSource(
																		table,
																		targetColumn);
														var html = '<li class="espacamentoFiltros col-md-12"><input type="checkbox" class="selectAll"/><div class="col-md-9" style="padding: 0;">Marcar/Desmarcar Todos</div></li><li class="espacamentoFiltros col-md-12">'
																+ opts
																		.map(
																				function(
																						elem) {
																					if (targetColumn != 5
																							&& targetColumn != 6
																							&& targetColumn != 7)
																						return '<input type="checkbox" class="marcar" data-column="'
																								+ targetColumn
																								+ '" value="'
																								+ elem.text
																								+ '"><div class="col-md-9" style="padding: 0;">'
																								+ elem.text;
																				})
																		.join(
																				'</div></li><li class="espacamentoFiltros col-md-12">')
																+ '</li><li class="espacamentoFiltros col-md-12"><button id="filterButton" class="btn btn-success btn-block">Filtrar</button></li>';
														$(
																'.search'
																		+ targetColumn)
																.append(html);
														$(
																'.search'
																		+ targetColumn)
																.on(
																		'click',
																		'#filterButton',
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
												ajaxProcessing : function(ajax) {
													if (ajax
															&& ajax
																	.hasOwnProperty('data')) {
														return [ ajax.data,
																ajax.total_rows ];
													}
												},
												output : '{startRow} - {endRow} ({totalRows})',
												updateArrows : true,
												page : 0,
												size : perPage,
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
							$tableModule.trigger('pageSize', perPage);

						}).fail(
						function(jqXHR, textStatus, errorThrown) {
							var body = jqXHR.responseJSON.errorCode + ", "
									+ jqXHR.responseJSON.errorMessage;
							modal("Monitor de chamados", body, 2);
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
				right:10,top:50,width:"60%"
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

	window.setInterval(function() {
		$.ajax({
			type : 'GET',
			url : '../rest/callmonitor2/search',
			dataType : "json",
			beforeSend : function(xhr) {
				xhr.setRequestHeader(header, token);
			},
			contentType : 'application/json'
		}).done(function(resp, textStatus, jqXHR) {
			_render(tableTpl(), $tbody, resp.list);
			$tableModule.trigger("update");
		});

	}, 300000);	

	// module.getFilters= getFilters;
	return module;
})(jQuery, ctrlMONT || {});