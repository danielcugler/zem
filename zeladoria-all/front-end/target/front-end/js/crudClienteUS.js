var URL = systemProperties.serverPath+'../rest/systemuser';
var URL2 = systemProperties.serverPath+'../rest/systemuserprofile';
var selector = '#ctbody';
var str1 = "Usuário";
var str2 = "Usuário";
var str3 = "o";
var templateHandlebars = ModelsTmpl.tmplUserResult;

function search(name, perfil, enabled) {
	abstractCRUD('GET', URL, '/search/'
			+ name + '/' + perfil + '/' + enabled, templateHandlebars,
			'#ctbody');
};

function mergeED(id, buttonED, bool) {

	$.ajax({
		type : 'PUT',
		url : URL + /ed/ + id,
		dataType : "json", // data type of response

		contentType : "application/json",
		success : function(data) {
			alteraED(buttonED, bool);
			if (!bool)
				enabledSucessMessage(str1, str2, str3);
			else
				disabledSucessMessage(str1, str2, str3);

			$('#modaldemensagem').modal('show');

		},
		error : function(data) {

		}
	});
};

// Funcoes rest client
function findAll() {
	abstractCRUD('GET', URL, '', templateHandlebars, selector);
};
function findAllSort() {
	abstractCRUD('GET', URL, '', templateHandlebars, selector);
};

function findAllCombos() {
	abstractCRUD('GET', URL2, '', ModelsTmpl.tmplComboPerfil, "#comboPerfil");
};
function findByEnabled(bool) {
	abstractCRUD('GET', URL, '/enabled/' + bool, templateHandlebars, selector);
};
function findByName(name) {
	abstractCRUD('GET', URL, '/name/' + name, templateHandlebars, selector);
}
function findByIdP(id) {
	abstractCRUD('GET', URL2, '/' + id, templateHandlebars, selector);
}
function findByEnabledAndName(bool, name) {
	abstractCRUD('GET', URL, '/en/' + bool + '/' + name, templateHandlebars,
			selector);
};

function findByEnabledAndPerfil(bool, perfil) {
	abstractCRUD('GET', URL, '/ep/' + bool + '/' + perfil, templateHandlebars,
			selector);
};
function findByPerfil(perfil) {
	abstractCRUD('GET', URL, '/perfil/' + perfil, templateHandlebars, selector);
};
function findByNameAndPerfil(name, perfil) {
	abstractCRUD('GET', URL, '/np/' + name + '/' + perfil, templateHandlebars,
			selector);
};
function findByEnabledAndNameAndPerfil(bool, name, perfil) {
	abstractCRUD('GET', URL, '/enp/' + bool + '/' + name + '/' + perfil,
			templateHandlebars, selector);
};

function findById(id) {
	abstractCRUD('GET', URL, '/' + id, templateHandlebars, selector);
};
function save(data) {

	$.ajax({
		type : 'POST',
		url : URL + '',
		dataType : "json", // data type of response
		data : data,
		contentType : "application/json",
		success : function(data) {
			saveMessage(str1, str2, str3);
			$('#modaldemensagem').modal('show');
		},
		error : function(data) { // Se a chamada AJAX | jqxhr, status,
									// errorMsg
		}
	});
};

function merge(data) {
	$.ajax({
		type : 'PUT',
		url : URL + '/merge',
		dataType : "json", // data type of response
		data : data,
		contentType : "application/json",
		success : function(data) {
			editMessage(str1, str2, str3);
			$('#modaldemensagem').modal('show');
		},
		error : function(data) { // Se a chamada AJAX | jqxhr, status,
									// errorMsg
		}
	});
};
function update(data) {
	abstractCRUD4('PUT', URL, '', templateHandlebars, selector, data);
};
function remove() {
	abstractCRUD('DELETE', URL, '/' + id, templateHandlebars, selector);
}
