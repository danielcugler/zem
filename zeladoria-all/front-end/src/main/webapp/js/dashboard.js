//google.charts.load('current', {'packages': ['corechart']});
//google.charts.setOnLoadCallback(drawPieChart);

loadCountsDashboard("../rest/dashboard/");
loadProgressChart("../rest/dashboard/progressChart");
loadNeighborhoodChart("../rest/dashboard/neighborhoodChart");
loadQualificationChart("../rest/dashboard/qualificationChart");
var bairros = null;

var colors = Highcharts.getOptions().colors;
var categories = ["Nota 1", "Nota 2", "Nota 3", "Nota 4", "Nota 5"];
var qualification = [];

function loadCountsDashboard(URL) {
	$.ajax({
		type : 'GET',
		async : false,
		url : URL,
		dataType : "json", 
		contentType : "application/json",
		success : function(data) {
			$("#countCitizen").attr('data-value', data.countCitizen);
			$("#countCallOpen").attr('data-value', data.countInProgress);
			$("#countFinalizedCall").attr('data-value', data.countSolved);
			$("#countCallDelay").attr('data-value', data.countDelayed);
		},
		error : function(jqxhr, status, errorMsg) { 
			// Mensagem de erro
		}
	});
}

function loadProgressChart(URL) {
	$.ajax({
		type : 'GET',
		async : false,
		url : URL,
		dataType : "json", 
		contentType : "application/json",
		success : function(data) {
			$("#newCall").append(data.newCall + " / " + data.allCall);
			$("#newCallBar").css("width", (data.newCall*100)/data.allCall + "%");
			$("#forwardedCall").append(data.forwardedCall + " / " + data.allCall);
			$("#forwardedCallBar").css("width", (data.forwardedCall*100)/data.allCall + "%");
			$("#visualizedCall").append(data.visualizedCall + " / " + data.allCall);
			$("#visualizedCallBar").css("width", (data.visualizedCall*100)/data.allCall + "%");
			$("#inProgressCall").append(data.inProgressCall + " / " + data.allCall);
			$("#inProgressCallBar").css("width", (data.inProgressCall*100)/data.allCall + "%");
			$("#proccessedCall").append(data.proccessedCall + " / " + data.allCall);
			$("#proccessedCallBar").css("width", (data.proccessedCall*100)/data.allCall + "%");
			$("#rejectedCall").append(data.rejectedCall + " / " + data.allCall);
			$("#rejectedCallBar").css("width", (data.rejectedCall*100)/data.allCall + "%");
			$("#finalizedCall").append(data.finalizedCall + " / " + data.allCall);
			$("#finalizedCallBar").css("width", (data.finalizedCall*100)/data.allCall + "%");
		},
		error : function(jqxhr, status, errorMsg) { 
			// Mensagem de erro
		}
	});
}

function loadNeighborhoodChart(URL) {
	$.ajax({
		type : 'GET',
		async : false,
		url : URL,
		dataType : "json", 
		contentType : "application/json",
		success : function(data){
			bairros = data;
			Morris.Bar({
				element: 'neighborhood-chart',
				  data: bairros,
				  xkey: 'nameNeighborhood',
				  ykeys: ['countNeighborhood'],
				  redraw: true,
				  labels: ['Chamados'],
				  resize: true,				  
				  barColors: ['#45B29D'],
			      gridTextColor: ['#777'],
				  hideHover: 'auto',
				  grid: false,
				  xLabelMargin: 5
				}).on('click', function(i, row){
				  console.log(i, row);
				});
		},
		error : function(jqxhr, status, errorMsg) { 
			console.log("Erro");
			console.log(errorMsg);
		}
	});
}

function loadQualificationChart(URL) {
	$.ajax({
		type : 'GET',
		async : false,
		url : URL,
		dataType : "json", 
		contentType : "application/json",
		success : function(data){
			var colors = ["#ed4138", "#ff9254", "#f7c471", "#669d31", "#21724a"];
			var categories = ["Nota 1", "Nota 2", "Nota 3", "Nota 4", "Nota 5"];
			var qualification = [];
			var qualificationSum = 0; // Soma de todas as qualificações.
			var qualificationCount = 0; // Soma de todas as qualificações já multiplicadas pela nota.
			var qualificationMedia = 0; // Média das notas
			
			for(var i=0; i<data.length; i++) {
				qualification[i] = {
						name: "Nota " + data[i].valueQualification,
						y: data[i].countQualification,
						color: colors[i]
				}
				qualificationSum += data[i].countQualification;
				qualificationCount += data[i].countQualification * data[i].valueQualification;
			}
			
			qualificationMedia = qualificationCount/qualificationSum;
			qualificationMedia = parseFloat(qualificationMedia.toFixed(2));
			
			$("#media-label").append("<strong>" + qualificationMedia + "</strong>");

			//Highchart
			var chart = new Highcharts.Chart({
			    chart: {
			        renderTo: 'qualification-chart',
			    	type: 'pie'
			    },
			    credits: {
			    	enabled: false
			    },
			    legend: {
			    	    align: 'right',
			    	    layout: 'vertical',
			    	    verticalAlign: 'middle',
			    	    x: -50,
			    	    y: 0
			    },
			    title: {
			    	text: ''
			    },
			    navigation: {
			    	buttonOptions: {
			    		enabled: false
			    	}
			    },
			    plotOptions: {
			        pie: {
			            innerSize: '60%',
			            allowPointSelect: true,
		                cursor: 'pointer',
		                dataLabels: {
		                    enabled: true	                   
		                },
		                showInLegend: true
			        }
			    },
			    tooltip: {
			        valueSuffix: ''
			    },
			    series: [{
			        name: 'Avaliações',
			        data: qualification,
			        size: '100%',
			        dataLabels: {			        	
			        	formatter: function(){
	                    	var pcnt = (this.y / qualificationSum) * 100;
	                        return Highcharts.numberFormat(pcnt) + '%';
	                    },
			            color: '#ffffff',
			            distance: -30,
			            borderWidth: 0,
			            style: {
			            	textOutline: '0px'
			            }
			        }
			    }]
			});
		},
		error : function(jqxhr, status, errorMsg) { 
			console.log("Erro");
			console.log(errorMsg);
		}
	});
}


