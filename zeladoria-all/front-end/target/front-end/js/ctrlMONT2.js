google.load("visualization", "1", {
	packages : [ "corechart" ]
});
google.setOnLoadCallback(drawChart);

var URL = "../rest/callmonitor2";
var selector = '#ctbody';
var templateHandlebars = ModelsTmpl.tmplCallMonitorResult2;
var str1 = "Monitor de Chamados";
var str2 = "Monitor de Chamados";
var str3 = "o";

function callSourceTemp(call) {
	if (call.callSource == 'Web') {
		return "<i class='icon-monitor-1'></i>";
	} else {
		return "<i class='icon-mobile-1'></i>";
	}
}

function mediasTemp(call) {
	var base64String;
	if (call.images !== null) {
		base64String = 'data:image/png;base64,' + call.images[0];
	} else {
		base64String = "../images/noImage.png";
	}
	return '<a type = "button" target = "_self" class="view-media"><img src="'
			+ base64String + '" height="26" width="26"></a>';
}

/*
 * <select class=\"form-control selectPriority selectpicker\">" + "{{#ifCond
 * priority '===' 'Baixa'}}" + "<option value=\"0\">Baixa</option>" + "<option
 * value=\"1\">Média</option>" + "<option value=\"2\">Alta</option>" +
 * "{{/ifCond}}" + "{{#ifCond priority '===' 'Média'}}" + "<option
 * value=\"1\">Média</option>" + "<option value=\"0\">Baixa</option>" + "<option
 * value=\"2\">Alta</option>" + "{{/ifCond}}" + "{{#ifCond priority '==='
 * 'Alta'}}" + "<option value=\"2\">Alta</option>" + "<option
 * value=\"0\">Baixa</option>" + "<option value=\"1\">Média</option>" +
 * "{{/ifCond}}" +"</select>
 */

function priorityTemp(call) {
	var template = '<select class="form-control selectPriority selectpicker">';
	if (call.priority == 'Baixa') {
		template += "<option value=\"0\">Baixa</option>"
				+ "<option value=\"1\">Média</option>"
				+ "<option value=\"2\">Alta</option>";
	} else if (call.priority == 'Média') {
		template += "<option value=\"1\">Média</option>"
				+ "<option value=\"0\">Baixa</option>"
				+ "<option value=\"2\">Alta</option>";
	} else {
		template += "<option value=\"2\">Alta</option>"
				+ "<option value=\"0\">Baixa</option>"
				+ "<option value=\"1\">Média</option>";
	}
	template += '</select>';
	return template;
}
function myFunc2(model) {
	if (model.isValid) {
		return "<span style='color: green'>Foo::" + model.foo
				+ " is valid! </span>";
	} else {
		return "not valid";
	}
}
function myFunc2(model) {
	if (model.isValid) {
		return "<span style='color: green'>Foo::" + model.foo
				+ " is valid! </span>";
	} else {
		return "not valid";
	}
}

$(document)
		.ready(
				function() {
					var username = $('#username').text();
					var filters = [];
					var search = [];
					search[0] = [];
					search[1] = [];
					search[2] = [];
					search[3] = [];
					var oTable;
					var token = $("meta[name='_csrf']").attr("content");
					var header = $("meta[name='_csrf_header']").attr("content");
					var initDate, endDate, filterSource, filterEntity, filterClassification, filterPriority, filterProgress;
					var column, order;
					$("#column").val(4);
					$("#order").val(1);
					getFilters2(1);
					var roleEdit = $.inArray('ROLE_CALL_UPDATE', list) >= 0;

					// comboET("http://localhost:8080/rest/entityclass",
					// ModelsTmpl.tmplListEntity, ".columnEntity");
					/*
					 * //filterrr $('thead th').click(function() { var $table =
					 * $("table"); var $tableBody = $table.find("tbody"); var
					 * column; var rows, sortedRows, firstName, secondName,
					 * sortAscending; var $th = $("table th"); var classs =
					 * $(this).attr('class'); $th.each(function() {
					 * $(this).removeClass("desc").removeClass("asc").addClass("uns");
					 * }); $(this).removeClass("uns"); if (classs.indexOf("uns") >
					 * -1) { sortAscending = true;
					 * $(this).removeClass("uns").addClass("asc"); } else if
					 * (classs.indexOf("desc") > -1) {
					 * $(this).removeClass("desc").addClass("asc");
					 * sortAscending = true; } else {
					 * $(this).removeClass("asc").addClass("desc");
					 * 
					 * sortAscending = false; }
					 * 
					 * function updateData() { $tableBody.remove('tr');
					 * $tableBody.append(sortedRows); }
					 * 
					 * function cmp(a, b) { first = $(a).find('td:nth-child(' +
					 * column + ')').text(); second = $(b).find('td:nth-child(' +
					 * column + ')').text(); if (first < second) { return
					 * (sortAscending) ? -1 : 1 } else if (first > second) {
					 * return (sortAscending) ? 1 : -1 } else { return 0 } }
					 * 
					 * function cmpNum(a, b) { firstName =
					 * parseInt($(a).find('td:nth-child(' + column +
					 * ')').text()); secondName =
					 * parseInt($(b).find('td:nth-child(' + column +
					 * ')').text()); if (firstName < secondName) { return
					 * (sortAscending) ? -1 : 1 } else if (firstName >
					 * secondName) { return (sortAscending) ? 1 : -1 } else {
					 * return 0 } } rows = $tableBody.find("tr"); column =
					 * $(this).index() + 1; if (column < 4) sortedRows =
					 * rows.sort(cmp); else sortedRows = rows.sort(cmpNum);
					 * updateData(); }) //ordenacao
					 * 
					 * 
					 */

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
								'page':page,						
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

					function searchToken(URL, data2, templateHandlebars,
							selector, currentPage, header, token) {
						var count = 0;
						var recordPerPage = 0;

						function callbackSuccess(data2) {
							if (data2.jsonList !== "[]"
									&& data2.jsonList !== null) {
								template(data2, templateHandlebars, selector);
								count = data2.count;
								valoresGraficos(JSON.parse(data2.graph));
							} else {
								$("#cthead").css("display", "none");
								$("#tabela")
										.html(
												"<div align='center'><h4>"
														+ data2.message
														+ "</h4></div>");
							}

						}
						;
						function callbackError() {
						}
						;
						function callbackComplete(URL, data2,
								templateHandlebars, selector, count,
								recordPerPage, currentPage) {
						}

						$
								.ajax(
										{
											type : 'GET',
											url : '../rest/callmonitor2/search',
											//url: URL,
											dataType : "json",
											data : data2,
											beforeSend : function(xhr) {
												xhr.setRequestHeader(header,
														token);
											},
											contentType : "application/json",
											success : function(resp) {
												callbackSuccess(resp);
											},
											error : function(resp) {
												callbackError();
											},
											complete : function(jqXHR, status) {

												$("#resultado")
														.tablesorter(
																{
																	widthFixed : true,
																	widgets : [
																			'columns',
																			'filter' ], // ***
																	// CSS
																	// CLASS
																	// NAMES
																	// ***
																	tableClass : 'tablesorter',
																	cssAsc : "tablesorter-headerSortUp",
																	cssDesc : "tablesorter-headerSortDown",
																	cssHeader : "tablesorter-header",
																	cssHeaderRow : "tablesorter-headerRow",
																	cssIcon : "tablesorter-icon",
																	cssChildRow : "tablesorter-childRow",
																	cssInfoBlock : "tablesorter-infoOnly",
																	cssProcessing : "tablesorter-processing",

																	// ***
																	// SELECTORS
																	// ***
																	// jQuery
																	// selectors
																	// used to
																	// find the
																	// header
																	// cells.
																	selectorHeaders : '> thead th, > thead td',

																	// jQuery
																	// selector
																	// of
																	// content
																	// within
																	// selectorHeaders
																	// that is
																	// clickable
																	// to
																	// trigger a
																	// sort.
																	selectorSort : "th, td",

																	// rows with
																	// this
																	// class
																	// name will
																	// be
																	// removed
																	// automatically
																	// before
																	// updating
																	// the table
																	// cache -
																	// used by
																	// "update",
																	// "addRows"
																	// and
																	// "appendCache"
																	selectorRemove : "tr.remove-me",
																	initialized : function(
																			table) {
																		var targetColumn = 2; // age
																		// column
																		// get
																		// all
																		// values
																		// from
																		// the
																		// age
																		// column
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
																							function(e) {																					
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
																	// page
																	// of
																	// the
																	// pager
																	// (zero
																	// based
																	// index)
																	// process
																	// ajax so
																	// that the
																	// data
																	// object is
																	// returned
																	// along
																	// with the
																	// total
																	// number of
																	// rows;
																	// example:
																	// {
																	// "data" :
																	// [{ "ID":
																	// 1,
																	// "Name":
																	// "Foo",
																	// "Last":
																	// "Bar" }],
																	// "total_rows"
																	// : 100
																	// }
																	ajaxProcessing : function(
																			ajax) {
																		if (ajax
																				&& ajax
																						.hasOwnProperty('data')) {
																			// return
																			// [
																			// "data",
																			// "total_rows"
																			// ];
																			return [
																					ajax.data,
																					ajax.total_rows ];
																		}
																	},

																	// output
																	// string -
																	// default
																	// is
																	// '{page}/{totalPages}';
																	// possible
																	// variables:
																	// {page},
																	// {totalPages},
																	// {startRow},
																	// {endRow}
																	// and
																	// {totalRows}
																	output : '{startRow} to {endRow} ({totalRows})',

																	// apply
																	// disabled
																	// classname
																	// to the
																	// pager
																	// arrows
																	// when the
																	// rows at
																	// either
																	// extreme
																	// is
																	// visible -
																	// default
																	// is true
																	updateArrows : true,

																	// starting
																	// page of
																	// the pager
																	// (zero
																	// based
																	// index)
																	page : 0,

																	// Number of
																	// visible
																	// rows -
																	// default
																	// is 10
																	size : 10,

																	// if true,
																	// the table
																	// will
																	// remain
																	// the same
																	// height no
																	// matter
																	// how many
																	// records
																	// are
																	// displayed.
																	// The space
																	// is made
																	// up by an
																	// empty
																	// table row
																	// set to a
																	// height to
																	// compensate;
																	// default
																	// is false
																	fixedHeight : true,

																	// remove
																	// rows from
																	// the table
																	// to speed
																	// up the
																	// sort of
																	// large
																	// tables.
																	// setting
																	// this to
																	// false,
																	// only
																	// hides the
																	// non-visible
																	// rows;
																	// needed
																	// if you
																	// plan to
																	// add/remove
																	// rows with
																	// the pager
																	// enabled.
																	removeRows : false,

																	// css class
																	// names of
																	// pager
																	// arrows
																	// next page
																	// arrow
																	cssNext : '.next',
																	// previous
																	// page
																	// arrow
																	cssPrev : '.prev',
																	// go to
																	// first
																	// page
																	// arrow
																	cssFirst : '.first',
																	// go to
																	// last page
																	// arrow
																	cssLast : '.last',
																	// select
																	// dropdown
																	// to allow
																	// choosing
																	// a page
																	cssGoto : '.gotoPage',
																	// location
																	// of where
																	// the
																	// "output"
																	// is
																	// displayed
																	cssPageDisplay : '.pagedisplay',
																	// dropdown
																	// that sets
																	// the
																	// "size"
																	// option
																	cssPageSize : '.pagesize',
																	// class
																	// added to
																	// arrows
																	// when at
																	// the
																	// extremes
																	// (i.e.
																	// prev/first
																	// arrows
																	// are
																	// "disabled"
																	// when on
																	// the first
																	// page)
																	// Note
																	// there is
																	// no period
																	// "." in
																	// front of
																	// this
																	// class
																	// name
																	cssDisabled : 'disabled'
																});

											}
										}).done(function() {
									/*
									 * $("#resultado") .tablesorter({widthFixed:
									 * true, widgets: ['columns', 'filter'], //
									 * *** CSS CLASS NAMES *** tableClass:
									 * 'tablesorter', cssAsc:
									 * "tablesorter-headerSortUp", cssDesc:
									 * "tablesorter-headerSortDown", cssHeader:
									 * "tablesorter-header", cssHeaderRow:
									 * "tablesorter-headerRow", cssIcon:
									 * "tablesorter-icon", cssChildRow:
									 * "tablesorter-childRow", cssInfoBlock:
									 * "tablesorter-infoOnly", cssProcessing:
									 * "tablesorter-processing", // ***
									 * SELECTORS *** // jQuery selectors used to
									 * find the header cells. selectorHeaders: '>
									 * thead th, > thead td', // jQuery selector
									 * of content within selectorHeaders // that
									 * is clickable to trigger a sort.
									 * selectorSort: "th, td", // rows with this
									 * class name will be removed automatically //
									 * before updating the table cache - used by
									 * "update", // "addRows" and "appendCache"
									 * selectorRemove: "tr.remove-me",
									 * initialized : function(table) { var
									 * targetColumn = 0; // age column // get
									 * all values from the age column
									 * for(;targetColumn<4;targetColumn++){ var
									 * opts =
									 * $.tablesorter.filter.getOptionSource(
									 * table, targetColumn ); var html='<li>' +
									 * opts.map(function(elem){ var str='<input
									 * type="checkbox"
									 * data-column="'+targetColumn+'"
									 * value="'+elem.text+'">'+elem.text; return
									 * str; }).join('</li><li>') + '</li>';
									 * $('.search'+targetColumn) .append(html);
									 * $('.search'+targetColumn).on('change','input',function(){
									 * if($(this).is(":checked")){
									 * search[$(this).data("column")].push($(this).val()); }
									 * else{
									 * search[$(this).data("column")].splice(search[$(this).data("column")].indexOf($(this).val()),
									 * 1); }
									 * filters[$(this).data("column")]=search[$(this).data("column")].join("|");
									 * $.tablesorter.setFilters( $('table'),
									 * filters, true ); }); } } })
									 * .tablesorterPager({container:
									 * $("#pager"), // starting page of the
									 * pager (zero based index) // process ajax
									 * so that the data object is returned along
									 * with the // total number of rows;
									 * example: // { // "data" : [{ "ID": 1,
									 * "Name": "Foo", "Last": "Bar" }], //
									 * "total_rows" : 100 // } ajaxProcessing:
									 * function(ajax) { if (ajax &&
									 * ajax.hasOwnProperty('data')) { // return [
									 * "data", "total_rows" ]; return
									 * [ajax.data, ajax.total_rows]; } }, //
									 * output string - default is
									 * '{page}/{totalPages}'; // possible
									 * variables: // {page}, {totalPages},
									 * {startRow}, {endRow} and {totalRows}
									 * output: '{startRow} to {endRow}
									 * ({totalRows})', // apply disabled
									 * classname to the pager arrows when the
									 * rows at // either extreme is visible -
									 * default is true updateArrows: true, //
									 * starting page of the pager (zero based
									 * index) page: 0, // Number of visible rows -
									 * default is 10 size: 10, // if true, the
									 * table will remain the same height no
									 * matter how many // records are displayed.
									 * The space is made up by an empty // table
									 * row set to a height to compensate;
									 * default is false fixedHeight: true, //
									 * remove rows from the table to speed up
									 * the sort of large tables. // setting this
									 * to false, only hides the non-visible
									 * rows; needed // if you plan to add/remove
									 * rows with the pager enabled. removeRows:
									 * false, // css class names of pager arrows //
									 * next page arrow cssNext: '.next', //
									 * previous page arrow cssPrev: '.prev', //
									 * go to first page arrow cssFirst:
									 * '.first', // go to last page arrow
									 * cssLast: '.last', // select dropdown to
									 * allow choosing a page cssGoto:
									 * '.gotoPage', // location of where the
									 * "output" is displayed cssPageDisplay:
									 * '.pagedisplay', // dropdown that sets the
									 * "size" option cssPageSize: '.pagesize', //
									 * class added to arrows when at the
									 * extremes // (i.e. prev/first arrows are
									 * "disabled" when on the first page) //
									 * Note there is no period "." in front of
									 * this class name cssDisabled:
									 * 'disabled'});
									 */
								});

					}
					;

					function searchTokenDT(URL, data2, templateHandlebars,
							selector, currentPage, header, token) {
						var count = 0;
						var recordPerPage = 0;

						function callbackSuccess(data) {
							if (data.jsonList !== "[]"
									&& data.jsonList !== null) {
								template(data, templateHandlebars, selector);
								count = data.count;
								recordPerPage = data.recordPerPage;
								valoresGraficos(JSON.parse(data.graph));
							} else {
								$("#cthead").css("display", "none");
								$("#tabela").html(
										"<div align='center'><h4>"
												+ data.message + "</h4></div>");
							}
						}
						;
						function callbackError() {
						}
						;
						function callbackComplete(URL, data2,
								templateHandlebars, selector, count,
								recordPerPage, currentPage) {

							// $("#resultado").DataTable({
							// responsive: true,
							// "sPaginationType": "full_numbers",
							// "pageLength": 10
							// });
						}
						;

						$.ajax({
							type : 'GET',
							url : '../rest/callmonitor2/search',
							dataType : "json",
							data : data2,
							cache : false,
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
								callbackComplete('../rest/callmonitor', data2,
										templateHandlebars, selector, count,
										recordPerPage, currentPage);

								oTable = $("#resultado").DataTable({
									"bSort" : false,
									"bFilter" : false,
									"bLengthChange" : false,
									"columns" : [ null, {
										"width" : "6%"
									}, {
										"width" : "10%"
									}, {
										"width" : "10%"
									}, {
										"width" : "15%"
									}, {
										"width" : "8%"
									}, {
										"width" : "11%"
									}, {
										"width" : "19%"
									}, {
										"width" : "10%"
									}, {
										"width" : "10%"
									} ]

								});
								oTable.column(0).visible(false, false);

							}
						});
					}
					;
					
					searchToken(URL , {
								
							'page':1,
								'callSource' : [],
								'callProgress' : [],
								'priority' : [],
								'callClassification' : [],
								'entity' : [],
								'orderParam' : 4,
								'order' : 1
							},
							templateHandlebars(roleEdit), selector, 1, header,
							token);
/*
					$.ajax(
							{
								type : 'POST',
								url : '../rest/callminitor/search/1'
										+ $('#username').text(),
								dataType : "json",
								data : JSON
								.stringify({
									'callSource' : [],
									'callProgress' : [],
									'priority' : [],
									'classification' : [],
									'entity' : [],
									'orderParam' : 4,
									'order' : 1
								}),
								beforeSend : function(xhr) {
									xhr.setRequestHeader(header, token);
								},
								contentType : "application/json"
							})
					.done(
							function(data) {
								template(data2, templateHandlebars, selector);
								count = data2.count;
								valoresGraficos(JSON.parse(data2.graph));
							}).fail();
					
	*/				
					
		//			searchToken(URL + '/search/1', JSON
		//					.stringify(callMonitorFilter),
		//					templateHandlebars(roleEdit), selector, 1, header,
		//					token);

					// filters datatable
					$('input').on('ifToggled', function(event) {
						event.stopPropagation();

						if ($(this).is(":checked")) {
							oTable.columns(4).search($(this).val()).draw();

						} else {
							oTable.columns(4).search('').draw();
						}
					});

					var list = $('#list li').map(function() {
						return $(this).text();
					});

					// searchInicialMO(URL + '/search/', 'z/z/z/z/z/z/z/4/1/z',
					// templateHandlebars(roleEdit), selector, 1);

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
					;

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
					;

					selectListAll();

					var getCurrentPage = function() {
						return $("#pagination").pagination('getCurrentPage');
					}
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

					// Barra de divisão de seções.
					$("#escondeAcima").click(function() {
						$("#tabela").toggle();
						$("#graficos").show();
					});

					$("#escondeAbaixo").click(function() {
						$("#tabela").show();
						$("#graficos").toggle();
					});

					// Alteração da prioridade do chamado.
					function getRowData(botao) {
						return $(botao).closest("tr").find("td:nth-child(2)")
								.text();
					}

					$("#resultado").on(
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

					// Icone de ordenação das colunas da Tabela de Chamados.
					$("#resultado").on(
							'click',
							'.sorting',
							function(e) {
								if (!$(e.target).is('.btn-group')) {
									var lastColumnClicked = $("#lastColumn")
											.val();
									column = this.id;
									var columnOrder = $(this).find("div");
									var columnOrder2 = $(columnOrder).find(
											"div:nth-child(2)");
									var columnOrderId = $(columnOrder2).attr(
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

									// getFilters2();

									lastColumnClicked = $("#lastColumn").val(
											columnOrderId);
								}
							});
				});

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
	var valoresStatus = google.visualization.arrayToDataTable([
			[ 'Status', 'Quantidade' ], [ 'Novo', novo ],
			[ 'Encaminhado', encaminhado ], [ 'Visualizado', visualizado ],
			[ 'Em andamento', emandamento ], [ 'Processado', processado ] ]);

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
			[ 'Classificação', 'Quantidade' ], [ 'Reclamação', reclamacao ],
			[ 'Denúncia', denuncia ], [ 'Crítica', critica ],
			[ 'Solicitação', solicitacao ], [ 'Sugestão', sugestao ],
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

$(window)
		.resize(
				function() {
					drawChart(emdia, emalerta, atrasados, entidades, novo,
							encaminhado, processado, visualizado, emandamento,
							reclamacao, denuncia, solicitacao, sugestao,
							elogio, pedidosdeinformacao, critica);
				});

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
