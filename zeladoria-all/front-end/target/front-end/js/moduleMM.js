var moduleMM = (function($) {
	var module = {};
// security rules
		var list = $('#list li').map(function() {
			return $(this).text();
		});

	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	var roleEdit = $.inArray('ROLE_MESSAGEMODEL_UPDATE', list) >= 0;
	var roleEnabled = $.inArray('ROLE_MESSAGEMODEL_ENABLE', list) >= 0;

	// filters
	//var $serachFilters = $searchModule.find(".searchFilters");
	var $comboName = $searchModule.find("#comboName");
	var $comboSubject = $searchModule.find("#comboSubject");
	var $radioEnabled = $searchModule.find(".radio-button");
	// buttons
	//var $searchButtons = $searchModule.find("#searchButtons");
	var $addButton = $searchModule.find("#add");
	var $searchButton = $searchModule.find("#search");
	//templates
	var $comboNameTpl = $("#combo-name-template").html();
	var $comboSubjectTpl = $("#combo-subject-template").html();
	

	
	
	function makeCombos() {
		$.ajax({
			type : 'GET',
			url : "../rest/messagemodel2/combos",
			dataType : "json",
			contentType : "application/json"
		}).done(function(resp, status, xhr) {
			MainModule._render($comboNameTpl, $comboName, resp);
			MainModule._render($comboSubjectTpl, $comboSubject, resp);
		}).fail(function(jqXHR, textStatus) {
		});
	}
	
	function getFilters(){
		
	}
function setFilters(){
		
	}
function validate(){
	
}
function makeFilters(){
	makeCombos();
}

function makeTable(){
	
}
	module.makeFilters= makeFilters;
	module.makeTable = makeTable;
	module.setFilters = setFilters;
	module.getFilters= getFilters;
	module.validate = validate;
	module.token = token;
	module.header = header;
	module.roleEdit = roleEdit;
	module.roleEnabled = roleEnabled;
	return module;
})(jQuery)