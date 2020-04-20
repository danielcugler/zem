var Mobile = (function($) {
	var module = {};
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	var roleEdit = $.inArray('ROLE_MESSAGEMODEL_UPDATE', list) >= 0;
	var roleEnabled = $.inArray('ROLE_MESSAGEMODEL_ENABLE', list) >= 0;
	// cached DOM
	var $addForm = $("#addForm");
	var $loginForm = $("#loginForm");
	var $addBtn = $("#add");
	var $loginBtn = $("#login");
	// bind events
	$addBtn.on("click", add);
	$loginBtn.on("click", login);

	function getLogin() {
		return {
			email : $("#inLogin").val(),
			pass : $("#inLoginPass").val()
		}
	}
	function add(event) {
		sendData();
	}
	function getCitizen() {
		return {
			"addressId" : {
				"neighborhoodId" : {
					"neighborhood_id" : 8,
					"neighborhood_name" : $("#inNeighboorhood").val()
				}
			},
			"citizen_cpf" : $("#inCpf").val(),
			"city_name" : $("#selCity").val(),
			"email" : $("#inEmail").val(),
			"enabled" : 0,
			"name" : $("#inName").val(),
			"password" : $("#inPass").val(),
			"phone_number" : $("#inPhone").val()
		};
	}
	function sendData() {
		$.ajax({
			type : 'POST',
			url : "http://localhost:8080/rest/mobile/" + "login",
			dataType : "json",
			data : JSON.stringify(getCitizen()),
			beforeSend : function(xhr) {
				xhr.setRequestHeader(header, token);
			},
			contentType : "application/json",
		}).done(function(resp) {
			console.log(resp);
		}).fail(function(jqXHR, textStatus, errorThrown) {
			console.log(jqXHR.responseJSON.errorMessage);
		});
	}
	function login(event) {
		var user = getLogin();
		$.ajax(
				{
					type : 'GET',
					url : "http://localhost:8080/rest/mobile/login/"
							+ user.email + "/" + user.pass,
					dataType : "json",
					beforeSend : function(xhr) {
						xhr.setRequestHeader(header, token);
					},
				}).done(function(resp) {
			console.log(resp);
		}).fail(function(jqXHR, textStatus, errorThrown) {
			alert(jqXHR.responseJSON.errorMessage);
		});
	}
	module.getCitizen = getCitizen;
	module.sendData = sendData;
	module.getLogin = getLogin;
	return module;
})(jQuery);