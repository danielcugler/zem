var Templates = {
	compile : function(baseTemplate, destination, objectJson) {
		var template = Handlebars.compile(baseTemplate);
		if (typeof (objectJson.jsonList) !== 'undefined')
			objectJson.jsonList = JSON.parse(objectJson.jsonList);
		var html = template(objectJson);
		$(destination).html(html);
	},
	compile2 : function(baseTemplate, destination, objectJson) {
		var template = Handlebars.compile(baseTemplate);
		var html = template(objectJson);
		$(destination).html(html);
	},
	compile3 : function(baseTemplate, destination, objectJson) {
		var template = Handlebars.compile(baseTemplate);
		if (typeof (objectJson.jsonList) !== 'undefined')
			objectJson.jsonList = JSON.parse(objectJson.jsonList);
		var html = template(objectJson);
		$(destination).html(html);
	}
};
function getByRel(arr, rel) {
	  var result  = arr.filter(function(o){return o.rel == rel;} );
	  return result? result[0] : null; // or undefined
	}
function checkCondition(v1, operator, v2) {
	switch (operator) {
	case '==':
		return (v1 == v2);
	case '===':
		return (v1 === v2);
	case '!==':
		return (v1 !== v2);
	case '<':
		return (v1 < v2);
	case '<=':
		return (v1 <= v2);
	case '>':
		return (v1 > v2);
	case '>=':
		return (v1 >= v2);
	case '&&':
		return (v1 && v2);
	case '||':
		return (v1 || v2);
	default:
		return false;
	}
};

Handlebars.registerHelper('ifCond', function(v1, operator, v2, options) {
	return checkCondition(v1, operator, v2) ? options.fn(this) : options
			.inverse(this);
});

Handlebars.registerHelper('ifUndefined', function(v1, options) {
	return (v1 === undefined) ? options.fn(this) : options.inverse(this);
});

Handlebars.registerHelper('ifNull', function(v1, options) {
	return (v1 !== null) ? options.fn(this) : options.inverse(this);
});

Handlebars.registerHelper('ifAuth', function(v1, v2, options) {
	if (typeof (v2) !== 'undefined')
		v2 = JSON.parse(v2);
	var bool = false;
	for (var ii = 0; ii < v2.length; ii++) {
		if (v2[ii].authority === v1) {
			bool = true;
		}
	}
	if (bool) {
		return options.fn(this);
	}
});

Handlebars.registerHelper('substr', function(passedString, startstring,
		endstring) {
	var out = passedString.substring(startstring, endstring);
	if (passedString.length > endstring)
		out += '(...)';
	return new Handlebars.SafeString(out)
});

Handlebars.registerHelper('getLink', function(links, position) {	
	var out = getByRel(links,position).link;
	return new Handlebars.SafeString(out)
});

Handlebars.registerHelper('replaceJson', function(passedString, startstring,
		endstring) {
	passedString = passedString.replace(/"/g, "");
	passedString = passedString.replace(/{/g, "");
	passedString = passedString.replace(/}/g, "");
	passedString = passedString.replace(/\//g, "");
	passedString = passedString.replace(/,/g, ", ");
	passedString = passedString.replace(/:/g, ": ");
	passedString = passedString.replace(/\\/g, "");
	passedString = passedString.replace(/defe12aad396f90e6b179c239de260d4/g,
			"******");

	var out = passedString.substring(startstring, endstring);
	if (passedString.length > endstring)
		out += '(...)';
	return new Handlebars.SafeString(out)
});

Handlebars
		.registerHelper('byteTobase64',
				function(passedString) {
					var base64String;
					if (passedString !== null) {
						var binaryString = '', bytes = new Uint8Array(
								passedString[0]), length = bytes.length;
						for (var i = 0; i < length; i++) {
							binaryString += String.fromCharCode(bytes[i]);
						}
						base64String = 'data:image/png;base64,'
								+ btoa(binaryString);
					} else {

						base64String = "../images/noImage.png";

					}
					return new Handlebars.SafeString(base64String)
				});


Handlebars
.registerHelper('imgShow',
		function(images) {
	var base64String;
	if (images !== null) {
			base64String='data:image/png;base64,'+images[0];
		} else {
			base64String = "../images/noImage.png";
		}
			return new Handlebars.SafeString(base64String)
		});


var editButton = "<a type = 'button' target = '_self' data-toggle='tooltip' title='Editar' class='btn btn-xs btn-primary edit-button'><i class='glyphicon glyphicon-pencil'></i></a>";
var enabledButton = "<a type = 'button' data-toggle='tooltip' title='Inativar' class='btn btn-xs btn-danger enabled-button'><i class='glyphicon glyphicon-remove'></i></a>";
var rejectButton = "<a type = 'button' data-toggle='tooltip' title='Rejeitar' class='btn btn-xs btn-danger enabled-button'><i class='glyphicon glyphicon-remove'></i></a>";
var disabledButton = "<a type = 'button' data-toggle='tooltip' title='Ativar' class='btn btn-xs btn-success disabled-button'><i class='glyphicon glyphicon-ok'></i></a>";
var listButton = "<a type = 'button'  data-toggle='tooltip' title='Detalhar' target = '_self' class='btn btn-xs btn-primary list-button'><i class='glyphicon glyphicon-list'></i></a>";
var publicationButton = "<a type = 'button' target = '_self' data-toggle='tooltip' title='Publicar' class='btn btn-xs btn-darkblue-2 publication-button'><i class='glyphicon glyphicon-volume-up'></i></a>";
var replyButton = "<a type='button' data-toggle='tooltip' title='Responder' class='btn btn-xs btn-green-3 reply-button'><i class='glyphicon glyphicon-send'></i></a>";
var historyButton = "<a type='button' data-toggle='tooltip' title='Histórico' class='btn btn-xs btn-darkblue-1 history-button'><i class='icon-hourglass-1'></i></a>";
var sendButton = "<a type='button' data-toggle='tooltip' title='Encaminhar' class='btn btn-xs btn-blue-3 send-button'><i class='glyphicon glyphicon-circle-arrow-right'></i></a>";

var editDisabledButton = "<a type = 'button' target = '_self' data-toggle='tooltip' title='Editar' class='btn btn-xs btn-darkblue-1 edit-button disabled'><i class='glyphicon glyphicon-pencil'></i></a>";
var replyDisabledButton = "<a type='button' data-toggle='tooltip' title='Responder' class='btn btn-xs btn-green-3 reply-button disabled'><i class='glyphicon glyphicon-send'></i></a>";
var enabledDisabledButton = "<a type = 'button' data-toggle='tooltip' title='Inativar' class='btn btn-xs btn-danger enabled-button disabled'><i class='glyphicon glyphicon-remove'></i></a>";
var rejectDisabledButton = "<a type = 'button' data-toggle='tooltip' title='Rejeitar' class='btn btn-xs btn-danger enabled-button disabled'><i class='glyphicon glyphicon-remove'></i></a>";
var publicationDisabledButton = "<a type = 'button' target = '_self' data-toggle='tooltip' title='Publicar' class='btn btn-xs btn-darkblue-2 publication-button disabled'><i class='glyphicon glyphicon-volume-up'></i></a>";
var disabledDisabledButton = "<a type = 'button' data-toggle='tooltip' title='Ativar' class='btn btn-xs btn-success disabled-button disabled'><i class='glyphicon glyphicon-ok'></i></a>";
var sendDisabledButton = "<a type='button' data-toggle='tooltip' title='Encaminhar' class='btn btn-xs btn-blue-3 send-button disabled'><i class='glyphicon glyphicon-circle-arrow-right'></i></a>";

var ModelsTmpl = {

	tmplReplyCall : "<div style=\"display:none;\"><h5  id=\"callId\">{{jsonList.unsolvedCallId}}</h5></div>"
			+ "<div class=\"col-md-12\">"
			+ "<h5>"
			+ "<strong> Origem: </strong> {{jsonList.callSource}} <h5>"
			+ "</h5>"
			+ "</div>"
			+ "<div class=\"col-md-12\">"
			+ "<h5>"
			+ "<strong> Data: </strong> {{jsonList.creationOrUpdateDate}}"
			+ "</h5>"
			+ "</div>"
			+ "<div class=\"col-md-12\">"
			+ "<h5>"
			+ "<strong> Usuário: </strong> {{jsonList.updatedOrModeratedBy.systemUserUsername}}"
			+ "</h5>"
			+ "</div>"
			+ "<div class=\"col-md-12\">"
			+ "<h5>"
			+ "<strong> Origem: </strong> {{jsonList.entityCategoryTarget.name}}"
			+ "</h5>"
			+ "</div>"
			+ "<div class=\"col-md-12\">"
			+ "<h5>"
			+ "<strong> Classificação: </strong> {{jsonList.callClassificationId.name}}"
			+ "</h5>"
			+ "</div>"
			+ "<br />"
			+ "<div class=\"col-md-12\">"
			+ "	<h5>"
			+ "<strong> Prioridade: </strong> {{jsonList.priority}}"
			+ "</h5>"
			+ "</div>"
			+ "<div class=\"col-md-12\">"
			+ "<br> <h5> <strong> Parecer:</strong></h5>"
			+ "<textarea id=\"CComments\" rows=\"4\" cols=\"40\""
			+ "class=\"form-control\" maxlength=\"500\" name=\"CComments\""
			+ "disabled>{{jsonList.observation.information}}</textarea>"
			+ "</div>"
			+ "<div class=\"col-md-12\">"
			+ "<h5> <strong> Resposta:</strong></h5>"
			+ "<textarea id=\"CAnswer\" rows=\"8\" cols=\"40\""
			+ "class=\"form-control\" maxlength=\"1000\" name=\"CAnswer\"></textarea>"
			+ "</div>",

	tmplComboEntity : "<select class=\"form-control selectEntity selectpicker\" id=\"selectEntity\" name=\"selectCheckEntidade\">"
			+ "<option value=\"-1\">Selecione uma Entidade</option>"
			+ "{{#each jsonList}}"
			+ "<option value=\"{{entityId}}\">{{name}}</option>" + "{{/each}}"
			+ "</select></div>",

	tmplComboAttendanceTime : "<select class=\"form-control selectAT selectpicker\" id=\"selectAT\">"
			+ "<option value=\"-1\">Selecione um Tempo de Atendimento</option>"
			+ "{{#each jsonList}}"
			+ "<option value=\"{{entityId}}\">{{entity.name}}</option>"
			+ "{{/each}}" + "</select>",

	tmplComboNeighborhood : "<select class=\"form-control selectNeighborhood selectpicker\" id=\"selectNeighborhood\">"
			+ "<option value=\"-1\">Selecione um Bairro</option>"
			+ "{{#each .}}"
			+ "<option value=\"{{neighborhoodId}}\">{{name}}</option>"
			+ "{{/each}}" + "</select></div>",
	tmplComboIT : "<label>Tipo de Informação</label><select class=\"form-control selectIT selectpicker\" id=\"selectIT\">"
			+ "<option value=\"-1\">Todas</option>"
			+ "{{#each jsonList}}"
			+ "<option value=\"{{code}}\">{{name}}</option>"
			+ "{{/each}}"
			+ "</select></div>",
	tmplComboOT : "<label>Tipo de Operação</label><select class=\"form-control selectOT selectpicker\" id=\"selectOT\">"
			+ "<option value=\"-1\">Todas</option>"
			+ "{{#each jsonList}}"
			+ "<option value=\"{{code}}\">{{name}}</option>"
			+ "{{/each}}"
			+ "</select></div>",
	tmplComboSU : "<label>Usuário</label><select class=\"form-control selectSU selectpicker\" id=\"selectSU\">"
			+ "<option value=\"-1\">Selecione uma opção</option>"
			+ "{{#each jsonList}}"
			+ "<option value=\"{{systemUserUsername}}\">{{systemUserUsername}}</option>"
			+ "{{/each}}" + "</select></div>",
	tmplComboSUSearch : "<label>Usuário</label><select class=\"form-control selectSU selectpicker\" id=\"selectSU\">"
			+ "<option value=\"-1\">Todas</option>"
			+ "{{#each jsonList}}"
			+ "<option value=\"{{systemUserUsername}}\">{{systemUserUsername}}</option>"
			+ "{{/each}}" + "</select></div>",

	tmplComboEntityCategory : "<select class=\"form-control selectEntityCategory selectpicker\" id=\"selectEntityCategory\" name=\"selectEntityCategory\">"
			+ "<option value=\"-1\">Selecione uma Categoria</option>"
			+ "{{#each jsonList}}"
			+ "<option value=\"{{entityCategoryId}}\">{{name}}</option>"
			+ "{{/each}}" + "</select></div>",
	tmplComboPriority : "<select class=\"form-control selectPriority selectpicker\" id=\"selectPriority\" name=\"selectPriority\">"
			+ "<option value=\"-1\">Selecione uma Prioridade</option>"
			+ "<option value=\"0\">Baixa</option>"
			+ "<option value=\"1\">Média</option>"
			+ "<option value=\"2\">Alta</option>" + "</select>",
	tmplComboCallSource : "<div class=''> <label>Origem</label><br>"
			+ "<select class=\"form-control selectCallSource selectpicker\" id=\"selectCallSource\" name=\"selectCallSource\">"
			+ "<option value=\"-1\">Selecione a Origem</option>"
			+ "<option value=\"1\">Móvel</option>"
			+ "<option value=\"0\">Web</option>" + "</select></div>",
	tmplComboCallClassification :"<select class=\"form-control selectCallClassification selectpicker\" id=\"selectCallClassification\" name=\"selectCallClassification\">"
			+ "<option value=\"-1\">Selecione uma Classificação</option>"
			+ "{{#each jsonList}}"
			+ "<option value=\"{{callClassificationId}}\">{{name}}</option>"
			+ "{{/each}}" + "</select>",
	tmplComboName : "<label>Nome</label><br>"
			+ "<select class=\"form-control selectNome selectpicker\" id=\"selectName\">"
			+ "<option value=\"-1\">Todas</option>" + "{{#each jsonList}}"
			+ "<option value=\"{{name}}\">{{name}}</option>" + "{{/each}}"
			+ "</select>",
	tmplComboSubject : "<label>Assunto</label><br>"
			+ "<select class=\"form-control selectAssunto selectpicker\" id=\"selectAssunto\">"
			+ "<option value=\"-1\">Todas</option>" + "{{#each jsonList}}"
			+ "<option value=\"{{subject}}\"> {{subject}}</option>"
			+ "{{/each}}" + "</select>",
	tmplComboBroadcastMessageCategory : "<div class='col-md-6'> <label>Broadcast Message Category</label><br>"
			+ "<select class=\"form-control selectBroadcastMessage selectpicker\" id=\"selectBroadcastMessageCategory\">"
			+ "<option value=\"-1\">Selecione uma Categoria</option>"
			+ "{{#each jsonList}}"
			+ "<option value=\"{{broadcastMessageCategoryId}}\">{{name}}</option>"
			+ "{{/each}}" + "</select></div>",
	tmplComboPerfil : "<select class=\"form-control selectPerfil selectpicker\" id=\"selectPerfil\" name=\"selectCheckPerfil\">"
			+ "<option value=\"-1\">Selecione um Perfil</option>"
			+ "{{#each jsonList}}"
			+ "<option value=\"{{systemUserProfileId}}\">{{name}}</option>"
			+ "{{/each}}" + "</select></div>",
	tmplComboPerfilSearch : "<select class=\"form-control selectPerfil selectpicker\" id=\"selectPerfil\" name=\"selectCheckPerfil\">"
			+ "<option value=\"-1\">Todos</option>"
			+ "{{#each jsonList}}"
			+ "<option value=\"{{systemUserProfileId}}\">{{name}}</option>"
			+ "{{/each}}" + "</select></div>",
	tmplComboSystemUser : "<div class=\"col-md-12\"> <label>Usuário</label><br>"
			+ "<select class=\"form-control selectSystemUser selectpicker\" id=\"selectSystemUser\" name=\"selectSystemUser\">"
			+ "<option value=\"-1\">Selecione uma opção</option>"
			+ "{{#each jsonList}}"
			+ "<option value=\"{{systemUserId}}\">{{name}}</option>"
			+ "{{/each}}" + "</select></div>",
	tmplComboInformationType : "<div class=\"col-md-12\"> <label>Tipo de Informação</label><br>"
			+ "<select class=\"form-control selectInformationType selectpicker\" id=\"selectInformationType\" name=\"selectInformationType\">"
			+ "<option value=\"-1\">Selecione uma opção</option>"
			+ "{{#each jsonList}}"
			+ "<option value=\"{{informationType}}\">{{name}}</option>"
			+ "{{/each}}" + "</select></div>",
	tmplComboOperationType : "<div class=\"col-md-12\"> <label>Tipo de Operação</label><br>"
			+ "<select class=\"form-control selectOperationType selectpicker\" id=\"selectOperationType\" name=\"selectOperationType\">"
			+ "<option value=\"-1\">Selecione uma opção</option>"
			+ "{{#each jsonList}}"
			+ "<option value=\"{{operationType}}\">{{name}}</option>"
			+ "{{/each}}" + "</select></div>",
	tmplReportChartType : "<label>Gráfico</label>"
			+ "<select class=\"form-control selectChartType selectpicker\" id=\"selectChartType\">"
			+ "<option value=\"1\">Barra</option>"
			+ "<option value=\"2\">Pizza</option>" + "</select></div>",
	tmplReportProgress : "<div> <label>Andamento</label><br>"
			+ "<select class=\"form-control selectProgress selectpicker\" id=\"selectProgress\" name=\"selectProgress\">"
			+ "<option value=\"-1\">Todas</option>"
			+ "<option value=\"0\">Novo</option>"
			+ "<option value=\"1\">Encaminhado</option>"
			+ "<option value=\"2\">Processado</option>"
			+ "<option value=\"3\">Finalizado</option>"
			+ "<option value=\"4\">Visualizado</option>"
			+ "<option value=\"5\">Em Andamento</option>"
			+ "<option value=\"6\">Rejeitado</option>" + "</select></div>",
	tmplReportPriority : "<div class=''> <label>Prioridade</label><br>"
			+ "<select class=\"form-control selectPriority selectpicker\" id=\"selectPriority\" name=\"selectPriority\">"
			+ "<option value=\"-1\">Todas</option>"
			+ "<option value=\"0\">Baixa</option>"
			+ "<option value=\"1\">Média</option>"
			+ "<option value=\"2\">Alta</option>" + "</select></div>",
	tmplReportEntityCategory : "<div class=> <label>Categoria de Entidade</label><br>"
			+ "<select class=\"form-control selectEntityCategory selectpicker\" id=\"selectEntityCategory\" name=\"selectEntityCategory\">"
			+ "<option value=\"-1\">Todas</option>"
			+ "{{#each jsonList}}"
			+ "<option value=\"{{entityCategoryId}}\">{{name}}</option>"
			+ "{{/each}}" + "</select></div>",
	tmplReportEntity : "<label>Entidade</label>"
			+ "<select class=\"form-control selectEntity selectpicker\" id=\"selectEntity\">"
			+ "<option value=\"-1\">Todas</option>" + "{{#each jsonList}}"
			+ "<option value=\"{{entityId}}\">{{name}}</option>" + "{{/each}}"
			+ "</select></div>",
	tmplReportCallSource : "<div class=''> <label>Origem</label><br>"
			+ "<select class=\"form-control selectCallSource selectpicker\" id=\"selectCallSource\" name=\"selectCallSource\">"
			+ "<option value=\"-1\">Todas</option>"
			+ "<option value=\"1\">Móvel</option>"
			+ "<option value=\"0\">Web</option>" + "</select></div>",
	tmplReportCallClassification : "<div class=> <label>Classificação</label><br>"
			+ "<select class=\"form-control selectCallClassification selectpicker\" id=\"selectCallClassification\" name=\"selectCallClassification\">"
			+ "<option value=\"-1\">Todas</option>"
			+ "{{#each jsonList}}"
			+ "<option value=\"{{callClassificationId}}\">{{name}}</option>"
			+ "{{/each}}" + "</select></div>",			
	tmplListEntity : "Entidade"
		+ "<div class=\"btn-group\">"
		+ "<div class=\"icon-filter btn btn-group dropdown-toggle\" data-toggle=\"dropdown\" aria-haspopup=\"true\" aria-expanded=\"false\">"
		+ "<span class=\"sr-only\">Toggle Dropdown</span>"
		+ "</div>"
		+ "<ul class=\"dropdown-menu col2\" id=\"filterEntity\" role=\"menu\">"		
		+ "<input type=\"hidden\" value=\"z\" id=\"valueEntity\"/>"
		+ "{{#each jsonList}}"
		+ "<li value=\"{{entityId}}\"><div class=\"\"><input class=\"filter filter2\" type=\"checkbox\" id=\"{{entityId}}\" />{{name}}</div></li>"
		+ "{{/each}}"
		+ "<li><span class=\"btn btn-success btn-block btnFilter\">Filtrar</span></li>"
		+ "</ul>"
		+ "<div class=\"fa fa-sort\" id=\"orderentity\" align=\"right\"></div>"
		+ "</div>",

	tmplUserResult : function(roleEdit, roleEnabled) {

		var string = "{{#each jsonList}}" + "<tr>"
				+ "<td class=\"hideTD\">{{systemUserUsername}}</td>"
				+ "<td>{{name}}</td>	" + "<td>{{email}}</td>	"
				+ "<td>{{systemUserProfileId.name}}</td>	"
				+ "<tdclass=\"hideTD\">{{enabled}}</td>"
				+ "<td> <div class='btn-group' role='group' aria-label='...'>";
		if (roleEdit)
			string += editButton;
		if (roleEnabled)
			string += "{{#ifCond enabled '===' 'Enabled'}}" + enabledButton
					+ "{{else}}" + disabledButton + "{{/ifCond}}";
		string += "</div></td></tr>" + "{{/each}}";
		return string;
	},
	tmplEntityResult : function(roleEdit, roleEnabled) {

		var string = "{{#each jsonList}}" + "<tr>"
				+ "<td class=\"hideTD\">{{entityId}}</td>"
				+ "<td>{{name}}</td>	"
				+ "<td>{{attendanceTime.lowPriorityTime}}</td>	"
				+ "<td>{{attendanceTime.mediumPriorityTime}}</td>	"
				+ "<td>{{attendanceTime.highPriorityTime}}</td>	"
				+ "<td class=\"hideTD\">{{enabled}}</td>"
				+ "<td><div class='btn-group' role='group' aria-label='...'>	";
		if (roleEdit)
			string += editButton;
		if (roleEnabled)
			string += "{{#ifCond enabled '===' 'Enabled'}}" + enabledButton
					+ "{{else}}" + disabledButton + "{{/ifCond}}";
		string += "</div></td></tr>" + "{{/each}}";
		return string;
	},
	tmplAttendanceTimeResult : "{{#each jsonList}}" + "<tr>"
			+ "<td class=\"hideTD\">{{entityId}}</td>"
			+ "<td>{{entity.name}}</td>	" + "<td>{{highPriorityTime}}</td>	"
			+ "<td>{{mediumPriorityTime}}</td>	"
			+ "<td>{{lowPriorityTime}}</td>	"
			+ "<td class=\"hideTD\">{{attendanceTime.enabled}}</td>"
			+ "<td><div class='btn-group' role='group' aria-label='...'>	"
			+ editButton + "{{#ifCond enabled '===' 'Enabled'}}"
			+ enabledButton + "{{else}}" + disabledButton + "{{/ifCond}}"
			+ "</div></td></tr>" + "{{/each}}",
	tmplEntityCategoryResult : function(roleEdit, roleEnabled) {
		var string = "{{#each jsonList}}	"
				+ "<tr><td style='display:none;'>{{entityCategoryId}}</td>"
				+ "<td>{{name}}</td>"
				+ "{{#ifCond send_message '===' 'Enabled'}}"
				+ "<td>Sim</td>"
				+ "{{else}}"
				+ "<td>Não</td>"
				+ "{{/ifCond}}<td><div class='btn-group' role='group' aria-label='...'>";
		if (roleEdit)
			string += editButton;
		if (roleEnabled)
			string += "{{#ifCond enabled '===' 'Enabled'}}" + enabledButton
					+ "{{else}}" + disabledButton + "{{/ifCond}}";
		string += "</div></td></tr>" + "{{/each}}";
		return string;
	},
	tmplSUPResult : function(roleEdit, roleEnabled) {
		var string = "{{#each jsonList}}	"
				+ "<tr><td style='display:none;'>{{systemUserProfileId}}</td>"
				+ "<td>{{name}}</td>"
				+ "<td><div class='btn-group' role='group' aria-label='...'>";
		if (roleEdit)
			string += editButton;
		if (roleEnabled)
			string += "{{#ifCond enabled '===' 'Enabled'}}" + enabledButton
					+ "{{else}}" + disabledButton + "{{/ifCond}}";

		string += "</div></td></tr>" + "{{/each}}";
		return string;
	},
	tmplCitizenResult : function(roleEnabled) {
		var string = "{{#each jsonList}}"
				+ "<tr>"
				+ "<td class=\"hideTD\">{{citizen_cpf}}</td>"
				+ "<td class=\"hideTD\"> {{address_number}}</td>"
				+ "<td class=\"hideTD\">{{city_name}}</td>"
				+ "<td>{{name}}</td>"
				+ "<td>{{email}}</td>"
				+ "<td class=\"hideTD\"> {{enabled}}</td>"
				+ "<td class=\"hideTD\">{{home_address_geograpical_coordinates}}</td>"
				+ "<td class=\"hideTD\">{{neighborhood.neighborhood_id}}</td>"
				+ "<td class=\"hideTD\">{{neighborhood.neighborhood_name}}</td>"
				+ "<td>{{phone_number}}</td>"
				+ "<td class=\"hideTD\">{{photo}}</td>"
				+ "<td class=\"hideTD\">{{street_name}}</td>"
				+ "<td><div class='btn-group' role='group' aria-label='...'>"
				+ listButton;
		if (roleEnabled)
			string += "{{#ifCond enabled '===' 'Enabled'}}" + enabledButton
					+ "{{else}}" + disabledButton + "{{/ifCond}}";
		string += "</div></td></tr>" + "{{/each}}"
		return string;
	},
	tmplCallFollowResult : function(roleEdit) {
		var string = "{{#each jsonList}}"
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
				+ "<td><div data-toggle='collapse' data-target='#{{unsolvedCallId}}' class='accordion-toggle cursor'><i class='glyphicon glyphicon-chevron-down'></i></div></td>"
				+ "{{#ifUndefined parentCallId}}"
				+ "<td>{{unsolvedCallId}}</td>"
				+ "{{else}}"
				+ "<td>{{parentCallId.unsolvedCallId}}</td>"
				+ "{{/ifUndefined}}"
				+ "{{#ifCond callSource '===' 'Web'}}"
				+ "<td>&nbsp;&nbsp;<i class='icon-monitor-1'></i></td>"
				+ "{{else}}"
				+ "<td>&nbsp;&nbsp;<i class='icon-mobile-2'></i></td>"
				+ "{{/ifCond}}"
				+ "<td>{{callClassificationId.name}}</td>"
				+ "<td>{{parentCallId.creationOrUpdateDate}}</td>"
				+ "<td>{{expirationDate}}</td>"
				+ "<td>{{priority}}</td>"
				+ "<td>{{description.information}}</td>"
				+ "{{#ifNull mediasPath}}"
				+ "<td>&nbsp;&nbsp;&nbsp;<a type = 'button' target = '_self' class='view-media'><img src=\"{{mediasPath.[0]}}\" height=\"25\" width=\"25\"></a></td>"
				+ "{{else}}"
				+ "<td>&nbsp;&nbsp;&nbsp;<a type = 'button' target = '_self'><img src=\"../images/noImage.png\" height=\"25\" width=\"25\"></a></td>"
				+ "{{/ifNull}}" + "<td class=\"hideTD\">{{enabled}}</td>	"
				+ "<td><div class='btn-group' role='group' aria-label='...'>";
		if (roleEdit)
			string += editButton;
		string += historyButton
				+ "</div></td></tr>"
				+ "<tr> <td colspan='9' class='hiddenRow'><div id='{{unsolvedCallId}}' class='accordion-body collapse'><span> Observação: {{observation.information}} </span> <br/> <span>Usuário: {{updatedOrModeratedBy.systemUserUsername}}</span>  </div> </td> </tr>"
				+ "{{/each}}";
		return string;
	},
	tmplCallMonitorResult: function(roleEdit) {
		var string = "{{#each jsonList}}"
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
				+ "{{#ifUndefined parentCallId}}"
				+ "<td class=\"hideTD\">{{unsolvedCallId}}</td>"
				+ "<td><a href=\"http://localhost:8080/callsHistory.jsp?id={{unsolvedCallId}}\"><u>{{unsolvedCallId}}</u></a></td>" 
				+ "{{else}}"
				+ "<td class=\"hideTD\">{{unsolvedCallId}}</td>"
				+ "<td><a href=\"http://localhost:8080/callsHistory.jsp?id={{parentCallId.unsolvedCallId}}\"><u>{{parentCallId.unsolvedCallId}}</u></a></td>" 
				+ "{{/ifUndefined}}"
				+ "{{#ifCond callSource '===' 'Web'}}"
				+ "<td><span class=\"hideTD\">{{callSource}}</span>&nbsp;&nbsp;<i class='icon-monitor-1'></i></td>" //coluna origem
				+ "{{else}}"
				+ "<td><span class=\"hideTD\">{{callSource}}</span>&nbsp;&nbsp;<i class='icon-mobile-2'></i></td>" //coluna origem
				+ "{{/ifCond}}"
				+ "<td>{{entityEntityCategoryMaps.entity.name}}</td>" //coluna entidade
				+ "<td>{{callClassificationId.name}}</td>" //coluna classificação
				+ "<td>{{parentCallId.creationOrUpdateDate}}</td>" //coluna data
				//+ "<td>{{priority}}</td>" // coluna prioridade
				//+ "<div class='btn-group' role='group' aria-label='...'></div>"				
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
				+"</select></td>"
				/*
				+"<td><div class=\"btn-group\">"
				  +"<button type=\"button\" class=\"btn dropdown-toggle\" data-toggle=\"dropdown\">"
				  +"<i class=\"fa fa-cog\"></i> {{priority}} <span class=\"caret\"></span>"
				  +"</button>"
				  +"<ul class=\"dropdown-menu\" role=\"menu\">"
				  +"<li><a class=\"priorityChange\">Baixa</a></li>"
				  +"<li><a class=\"priorityChange\">Média</a></li>"
				  +"<li><a class=\"priorityChange\">Alta</a></li>"
				  +"</ul>"
				  +"</div></td>"
				*/
				+ "<td>{{description.information}}</td>" // coluna descrição
				+ "{{#ifNull medias}}"
			//	+ "<td>&nbsp;&nbsp;&nbsp;<a type = 'button' target = '_self' class='view-media'><img src=\"{{byteTobase64 medias}}\" height=\"25\" width=\"25\"></a></td>"
				+ "<td>&nbsp;&nbsp;&nbsp;img</td>"
				
				+ "{{else}}"
			//	+ "<td>&nbsp;&nbsp;&nbsp;<a type = 'button' target = '_self'><img src=\"{{byteTobase64 medias}}\" height=\"25\" width=\"25\"></a></td>"
				+ "<td>&nbsp;&nbsp;&nbsp;img</a></td>"
				
				+ "{{/ifNull}}"
				+ "<td class=\"hideTD\">{{enabled}}</td>"
				+ "<td>{{callProgress}}</td>" // coluna status
				+ "</tr>"
				//+ "<tr> <td colspan='9' class='hiddenRow'><div id='{{unsolvedCallId}}' class='accordion-body collapse'><span> Observação: {{observation.information}} </span> <br/> <span>Usuário: {{updatedOrModeratedBy.systemUserUsername}}</span>  </div> </td> </tr>"
				+ "{{/each}}";
		return string;
		},		tmplCallMonitorResult2: function(roleEdit) {
		var string = "{{#each jsonList}}"
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
				+ "{{#ifUndefined parentCallId}}"
				+ "<td >{{unsolvedCallId}}</td>"
				+ "{{else}}"
				+ "<td class=\"hideTD\">{{parentCallId.unsolvedCallId}}</td>"
				+ "{{/ifUndefined}}"
				+ "<td><a href=\"http://localhost:8080/callsHistory.jsp?id={{parentCallId.unsolvedCallId}}\"><u>{{unsolvedCallId}}</u></a></td>" //coluna ID (ID do último filho)
				//+ "<td><div data-toggle='collapse' data-target='#{{unsolvedCallId}}' class='accordion-toggle cursor'><i class='glyphicon glyphicon-chevron-down'></i></div></td>"
				+ "{{#ifCond callSource '===' 'Web'}}"
				+ "<td><span class=\"hideTD\">{{callSource}}</span>&nbsp;&nbsp;<i class='icon-monitor-1'></i></td>" //coluna origem
				+ "{{else}}"
				+ "<td><span class=\"hideTD\">{{callSource}}</span>&nbsp;&nbsp;<i class='icon-mobile-2'></i></td>" //coluna origem
				+ "{{/ifCond}}"
				+ "<td>{{entityEntityCategoryMaps.entity.name}}</td>" //coluna entidade
				+ "<td>{{callClassificationId.name}}</td>" //coluna classificação
				+ "<td>{{parentCallId.creationOrUpdateDate}}</td>" //coluna data
				//+ "<td>{{priority}}</td>" // coluna prioridade
				//+ "<div class='btn-group' role='group' aria-label='...'></div>"				
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
				+"</select></td>"
				/*
				+"<td><div class=\"btn-group\">"
				  +"<button type=\"button\" class=\"btn dropdown-toggle\" data-toggle=\"dropdown\">"
				  +"<i class=\"fa fa-cog\"></i> {{priority}} <span class=\"caret\"></span>"
				  +"</button>"
				  +"<ul class=\"dropdown-menu\" role=\"menu\">"
				  +"<li><a class=\"priorityChange\">Baixa</a></li>"
				  +"<li><a class=\"priorityChange\">Média</a></li>"
				  +"<li><a class=\"priorityChange\">Alta</a></li>"
				  +"</ul>"
				  +"</div></td>"
				*/
				+ "<td>{{description.information}}</td>" // coluna descrição
				+ "{{#ifNull images}}"
				+ "<td>&nbsp;&nbsp;&nbsp;<a type = 'button' target = '_self' class='view-media'><img src=\"{{imgShow images}}\" height=\"25\" width=\"25\"></a></td>"
			//	+ "<td>&nbsp;&nbsp;&nbsp;img</td>"
				
				+ "{{else}}"
				+ "<td>&nbsp;&nbsp;&nbsp;<a type = 'button' target = '_self'><img src=\"{{imgShow images}}\" height=\"25\" width=\"25\"></a></td>"
			//	+ "<td>&nbsp;&nbsp;&nbsp;img</a></td>"
				
				+ "{{/ifNull}}"
				+ "<td>{{callProgress}}</td>" // coluna status
				+ "</tr>"
				//+ "<tr> <td colspan='9' class='hiddenRow'><div id='{{unsolvedCallId}}' class='accordion-body collapse'><span> Observação: {{observation.information}} </span> <br/> <span>Usuário: {{updatedOrModeratedBy.systemUserUsername}}</span>  </div> </td> </tr>"
				+ "{{/each}}";
		return string;
		},		
	tmplMassCommunicationsResult : function(roleEdit, roleEnabled, roleSend,
			roleReply) {
		var string = "{{#each jsonList}}"
				+ "<tr>"
				+ "<td class=\"hideTD\">{{unsolvedCallId}}</td>"
				+ "<td style='text-align:center'><div data-toggle='collapse' data-target='#{{unsolvedCallId}}' class='accordion-toggle cursor'><i class='glyphicon glyphicon-chevron-down'></i></div></td>"
				+ "{{#ifUndefined parentCallId}}"
				+ "<td>{{unsolvedCallId}}</td>"
				+ "{{else}}"
				+ "<td>{{parentCallId.unsolvedCallId}}</td>"
				+ "{{/ifUndefined}}"
				+ "{{#ifCond callSource '===' 'Web'}}"
				+ "<td style='text-align:center'><i class='icon-monitor-1'></i></td>"
				+ "{{else}}"
				+ "<td style='text-align:center'><i class='icon-mobile-2'></i></td>"
				+ "{{/ifCond}}"
				+ "<td>{{entityEntityCategoryMaps.entity.name}}</td>	"
				+ "<td>{{callClassificationId.name}}</td>	"
				+ "<td>{{priority}}</td>"
				+ "<td>{{substr description.information 0 60}}</td>	"
				+ "{{#ifNull mediasPath}}"
				+ "<td>&nbsp;&nbsp;&nbsp;<a type = 'button' target = '_self' class='view-media'><img src=\"{{mediasPath.[0]}}\" height=\"26\" width=\"26\"></a></td>"
				+ "{{else}}"
//				+ "<td>&nbsp;&nbsp;&nbsp;<a type = 'button' target = '_self'><img src=\"{{byteTobase64 medias}}\" height=\"26\" width=\"26\"></a></td>"
				+ "<td>&nbsp;&nbsp;&nbsp;<a type = 'button' target = '_self'><img src=\"../images/noImage.png\" height=\"26\" width=\"26\"></a></td>"
				+ "{{/ifNull}}"
				+ "{{#ifCond callProgress '===' 'Processado'}}"
				+ "<td><span  class='btn btn-xs btn-blue-3 btn-label'>Processado</span></td>"
				+ "{{/ifCond}}"
				+ "{{#ifCond callProgress '===' 'Finalizado'}}"
				+ "<td><span class='btn btn-xs btn-green-2 btn-label'>Finalizado</span></td>"
				+ "{{/ifCond}}"
				+ "{{#ifCond callProgress '===' 'Rejeitado'}}"
				+ "<td><span class='btn btn-xs btn-danger btn-label'>Rejeitado</span></td>"
				+ "{{/ifCond}}"
				+ "{{#ifCond callProgress '===' 'Encaminhado'}}"
				+ "<td><span class='btn btn-xs btn-orange-2 btn-label'>Encaminhado</span></td>"
				+ "{{/ifCond}}"
				+ "{{#ifCond callProgress '===' 'Novo'}}"
				+ "<td><span  class='btn btn-xs btn-orange-1 btn-label'>Novo</span></td>"
				+ "{{/ifCond}}" + "<td class=\"hideTD\">{{enabled}}</td>	"
				+ "<td><div class='btn-group' role='group' aria-label='...'>"
				+ "{{#ifCond callProgress '===' 'Encaminhado'}}"
				+ editDisabledButton + "{{#ifCond callStatus '===' 'Ativo'}}"
				+ rejectDisabledButton + "{{else}}" + disabledDisabledButton
				+ "{{/ifCond}}" + sendDisabledButton + replyDisabledButton
				+ "{{/ifCond}}" + "{{#ifCond callProgress '===' 'Finalizado'}}"
				+ editDisabledButton + "{{#ifCond callStatus '===' 'Ativo'}}"
				+ rejectDisabledButton + "{{else}}" + disabledDisabledButton
				+ "{{/ifCond}}" + sendDisabledButton + replyDisabledButton
				+ "{{/ifCond}}";
		string += "{{#ifCond callProgress '===' 'Novo'}}";
		if (roleEdit)
			string += editButton;
		if (roleEnabled)
			string += "{{#ifCond callStatus '===' 'Ativo'}}" + rejectButton
					+ "{{else}}" + disabledButton + "{{/ifCond}}";
		if (roleSend)
			string += "{{#ifCond callStatus '===' 'Ativo'}}" + sendButton
					+ "{{else}}" + sendDisabledButton + "{{/ifCond}}";
		if (roleReply)
			string += replyButton
		string += "{{/ifCond}}" + "{{#ifCond callProgress '===' 'Processado'}}";
		if (roleEdit)
			string += editButton;
		string += "{{#ifCond callStatus '===' 'Ativo'}}" + rejectButton;
		string += "{{else}}"
				+ "<a type = 'button' data-toggle='tooltip' title='Ativar' class='btn btn-xs btn-success disabled-button'><i class='glyphicon glyphicon-ok'></i></a>"
				+ "<a type='button' data toggle='tooltip' title='Encaminhar' class='btn btn-xs btn-blue-3 send-button disabled'><i class='glyphicon glyphicon-circle-arrow-right'></i></a>"
				+ "{{/ifCond}}";
		if (roleReply)
			string += replyButton;
		string += "{{/ifCond}}"
				+ historyButton
				+ "</div></td></tr>"
				+ "<tr> <td colspan='12' class='hiddenRow'><div id='{{unsolvedCallId}}' class='accordion-body collapse'><span> Descrição: {{description.information}} </span> <br/> <span>Usuário: {{updatedOrModeratedBy.systemUserUsername}}</span> <br/> <span>Data: {{creationOrUpdateDate}}</span>  </div> </td> </tr>"
				+ "{{/each}}";
		return string;
	},
	tmplDetailedCitizenResult : "<div class = \"col-md-6\">"
			+ "<h5><strong> Nome</strong></h5>"
			+ "<h5>{{jsonList.name}}</h5>"
			+ "<br/>"
			+ "<h5><strong> E-mail</strong></h5>"
			+ "<h5>{{jsonList.email}}</h5>"
			+ "<br/><h5><strong> Endereço</strong></h5>"
			+ "<br/>"
			+ "<h5><strong>Bairro: </strong></h5><h5>{{jsonList.neighborhoodId.nome}}</h5>"
			+ "</div>"
			+ "<div class = \"col-md-6\">"
			+ "<h5><strong> CPF</strong></h5>"
			+ "<h5> {{jsonList.citizen_cpf}}</h5>"
			+ "<br/>"
			+ "<h5><strong> Telefone</strong></h5>"
			+ "<h5>{{jsonList.phone_number}}</h5>"
			+ "<br/><h5/><br/>"
			+ "<h5><strong>Estado: </strong></h5><h5>{{jsonList.state.ufeNo}}</h5>"
			+ "<br/>"
			+ "<h5><strong>Cidade: </strong></h5><h5>{{jsonList.cityId.nome}}</h5>"
			+ "</div>",
	tmplHistory : "{{#each jsonList}}" + "<tr>"
			+ "<td>{{creationOrUpdateDate}}</td>"
			+ "<td>{{entityEntityCategoryMaps.entityCategory.name}}</td>"
			+ "<td>{{callClassificationId.name}}</td>	"
			+ "<td>{{priority}}</td>	"
			+ "<td>{{updatedOrModeratedBy.systemUserUsername}}</td>	"
			+ "<td>{{description.information}}</td>	"
			+ "<td>{{observation.information}}</td>" + "</tr>" + "{{/each}}",
	tmplMessageModelResult : function(roleEdit, roleEnabled) {
		var string = "{{#each jsonList}}" + "<tr>"
				+ "<td class=\"hideTD\">{{messageModelId}}</td>"
				+ "<td>{{name}}</td>	" + "<td>{{subject}}</td>	"
				+ "<td class=\"hideTD\">{{messageBody}}</td>	"
				+ "<td>{{substr messageBody 0 30}}</td>	"
				+ "<tdclass=\"hideTD\">{{enabled}}</td>"
				+ "<td> <div class='btn-group' role='group' aria-label='...'>	";
		if (roleEdit)
			string += editButton;
		if (roleEnabled)
			string += "{{#ifCond enabled '===' 'Enabled'}}" + enabledButton
					+ "{{else}}" + disabledButton + "{{/ifCond}}";
		string += "</div></td></tr>" + "{{/each}}";
		return string;
	},
	tmplLogResult : "{{#each jsonList}}" + "<tr>"
			+ "<td class=\"hideTD\">{{logId}}</td>"
			+ "<td>{{changeDate}}</td>	"
			+ "<td>{{systemUserUsername.systemUserUsername}}</td>	"
			+ "<td>{{informationType}}</td>	" + "<td>{{operationType}}</td>	"
		//	+ "<td>{{replaceJson content 0 80}}</td>"
			+ "<td>{{replaceJson content2 0 80}}</td>"
			+ "<td align='right'><div class='btn-group' role='group' aria-label='...'>"
			+ listButton + "</div></td>" + "</tr>" + "{{/each}}",
	tmplBroadcastMessageResult : function(roleEdit, roleEnabled,
			rolePublication) {
		var string = "{{#each jsonList}}" + "<tr>"
				+ "<td class=\"hideTD\">{{broadcastMessageId}}</td>"
				+ "<td>{{subject}}</td>	"
				+ "<td class=\"hideTD\">{{messageBody}}</td>	"
				+ "<td>{{substr messageBody 0 30}}</td>	"
				+ "<td>{{broadcastMessageCategoryId.name}}</td>	"
				+ "<td>{{creationDate}}</td>	"
				+ "<td>{{publicationDate}}</td>	"
				+ "<td class=\"hideTD\">{{enabled}}</td>	"
				+ "<td><div class='btn-group' role='group' aria-label='...'>";
		if (rolePublication)
			string += "{{#ifNull publicationDate}}" + publicationDisabledButton
					+ "{{else}}" + publicationButton + "{{/ifNull}}";
		if (roleEdit)
			string += editButton;
		if (roleEnabled)
			string += "{{#ifCond enabled '===' 'Enabled'}}" + enabledButton
					+ "{{else}}" + disabledButton + "{{/ifCond}}";
		string += "</div></td>" + "</tr>" + "{{/each}}";
		return string;
	},

	tmplLogOperationResult : "{{#each jsonList}}" + "<tr>"
			+ "<td class=\"hideTD\">{{systemUserProfileId}}</td>"
			+ "<td>{{subject}}</td>	" + "<td>{{messageBody}}</td>	"
			+ "<td>{{broadcastMessageCategoryId.name}}</td>	"
			+ "<td>{{creationDate}}</td>	" + "<td>{{publicationDate}}</td>	"
			+ "<td class=\"hideTD\">{{enabled}}</td>	"
			+ "<td><div class='btn-group' role='group' aria-label='...'>"
			+ "</div></td>" + "</tr>" + "{{/each}}",
	tmplDetailedLogResult : "<div class = \"col-md-6\">"
			+ "<h5><strong> Data/Hora</strong></h5>"
			+ "<h5>{{jsonList.changeDate}}</h5>" + "<br/>"
			+ "<h5><strong> Tipo de Operação</strong></h5>"
			+ "<h5>{{jsonList.operationType}}</h5>" + "</div>"
			+ "<div class = \"col-md-6\">"
			+ "<h5><strong> Usuário</strong></h5>"
			+ "<h5> {{jsonList.systemUserUsername.systemUserUsername}}</h5>"
			+ "<br/>" + "<h5><strong> Tipo de Informação</strong></h5>"
			+ "<h5>{{jsonList.informationType}}</h5>" + "<br/>" + "</div>"
			+ "<div class= \"col-md-12\">"
			+ "<h5><strong> Conteúdo</strong></h5>" + "<pre id= \"json\">"
			+ "{{jsonList.content2}}" + "<br/><br/><br/>" + "</pre>" + "</div>",

	messages : "<h2>{{message}}</h2>",
	validateError : "<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>×</button>"
			+ "Erro</div>"
			+ "<div class='modal-body'>Por favor, preencha os campos destacados em vermelho!"
			+ "</div>"
			+ "<div class='modal-footer'><a class='btn btn-danger' data-dismiss='modal' role='button'>Ok!</a>"
			+ "</div>",
	saveMessageMobile : "<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>×</button>"
			+ "Registro de Chamado Público</div>"
			+ "<div class='modal-body'>Chamado Registrado com sucesso."
			+ "</div>"
			+ "<div class='modal-footer new-call-button'><a class='btn btn-danger' data-dismiss='modal' role='button'>Novo Chamado</a>"
			+ "<a class='btn btn-danger exit-button' role='button'>Sair</a>"
			+ "</div>",

	saveMessageWeb : function(url, urlWeb) {
		return "<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>×</button>"
				+ "Registro de Chamado Público</div>"
				+ "<div class='modal-body '><p> Chamado Púbico registrado com sucesso.</p><br/>"
				+ "<p>Em breve o usuário informado receberá um e-mail com o link para acompanhamento.</p>"
				+ "</div>"
				+ "<div class='modal-footer new-call-button'><a onClick='window.location.reload()' class='btn btn-danger'  role='button'>Novo Chamado</a>"
				+ "<a class='btn btn-danger exit-button' data-dismiss='modal' role='button'>Sair</a>"
				+ "</div>"
	},
	
	saveMessageInternalCall : function(url, urlWeb) {
		return "<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>×</button>"
				+ "Registro de Chamado Interno</div>"
				+ "<div class='modal-body '><p> Chamado Interno registrado com sucesso.</p><br/>"
				+ "<p>Em breve o usuário informado receberá um e-mail com o link para acompanhamento.</p>"
				+ "</div>"
				+ "<div class='modal-footer new-call-button'><a onClick='window.location.reload()' class='btn btn-danger'  role='button'>Novo Chamado</a>"
				+ "<a class='btn btn-danger exit-button' data-dismiss='modal' role='button'>Sair</a>"
				+ "</div>"
	},

	enabledMessage : function(str1, str2, str3) {
		return "<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>×</button>"
				+ " Inativar "
				+ str1
				+ "</div>"
				+ "<div class='modal-body'> Você tem certeza que deseja inativar "
				+ str3
				+ " "
				+ str2
				+ " {{name}}?"
				+ "</div>"
				+ "<div class='modal-footer'><a class='btn btn-success ebSim' data-dismiss='modal' role='button' id='bSim'>Sim</a>&nbsp&nbsp&nbsp"
				+ "<a class='btn btn-danger' data-dismiss='modal' role='button' id='bNao'>Não</a>"
				+ "</div>"
	},

	disabledMessage : function(str1, str2, str3) {
		return "<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>×</button> Ativar "
				+ str1
				+ "</div>"
				+ "<div class='modal-body'> Você tem certeza que deseja ativar "
				+ str3
				+ " "
				+ str2
				+ " {{name}}?"
				+ "</div>"
				+ "<div class='modal-footer'>"
				+ "<a class='btn btn-success dbSim' role='button' data-dismiss='modal' id='bSim'>Sim</a>&nbsp&nbsp&nbsp"
				+ "<a class='btn btn-danger' data-dismiss='modal' role='button' id='bNao'>Não</a>"
				+ "</div>"
	},
	publishMessage : function(str1, str2, str3) {
		return "<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>×</button> Publicar "
				+ str1
				+ "</div>"
				+ "<div class='modal-body'> Você tem certeza que deseja publicar "
				+ str3
				+ " "
				+ str2
				+ " {{name}}?"
				+ "</div>"
				+ "<div class='modal-footer'>"
				+ "<a class='btn btn-success dbSim' role='button' data-dismiss='modal' id='bSim'>Sim</a>&nbsp&nbsp&nbsp"
				+ "<a class='btn btn-danger' data-dismiss='modal' role='button' id='bNao'>Não</a>"
				+ "</div>"
	},
	saveMessage : function(str1, str2, str3) {
		return "<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>×</button>Salvar "
				+ str1
				+ "</div>"
				+ "<div class='modal-body'>"
				+ str2
				+ " inserid"
				+ str3
				+ " com sucesso!"
				+ "<br>"
				+ "</div>"
				+ "<div class='modal-footer'>"
				+ "<a class='btn btn-success okbutton' role='button'>Ok!</a>"
				+ "</div>"
	},
	updateMessage : function(str1, str2, str3) {
		return "<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>×</button>Salvar "
				+ str1
				+ "</div>"
				+ "<div class='modal-body'>"
				+ str2
				+ " atualizad"
				+ str3
				+ " com sucesso!"
				+ "<br>"
				+ "</div>"
				+ "<div class='modal-footer'>"
				+ "<a class='btn btn-success okbutton' role='button'>Ok!</a>"
				+ "</div>"
	},
	replyMessage : function(str1, resp) {
		return "<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>×</button>Salvar "
				+ str1
				+ "</div>"
				+ "<div class='modal-body'>"
				+ resp.message
				+ "<br>"
				+ "</div>"
				+ "<div class='modal-footer'>"
				+ "<a class='btn btn-success okbutton' role='button'>Ok!</a>"
				+ "</div>"
	},
	duplicatedMessage : function(resp) {
		return "<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>×</button>Erro! "
				+ "</div>"
				+ "<div class='modal-body'>"
				+ resp.message
				+ "<br>"
				+ "</div>"
				+ "<div class='modal-footer'>"
				+ "<a class='btn btn-success okerrorButton' data-dismiss='modal' role='button'>Ok!</a>"
				+ "</div>"
	},
	editMessage : function(str1, str2, str3) {
		return "<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>×</button>Atualizar "
				+ str1
				+ "</div>"
				+ "<div class='modal-body'>"
				+ str2
				+ " atualizad"
				+ str3
				+ " com sucesso!"
				+ "<br>"
				+ "</div>"
				+ "<div class='modal-footer'>"
				+ "<a class='btn btn-success okbutton' role='button'>Ok!</a>"
				+ "</div>"
	},
	enabledSucessMessage : function(str1, str2, str3) {
		return "<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>×</button>Ativar "
				+ str1
				+ "</div>"
				+ "<div class='modal-body'>"
				+ str2
				+ " ativad"
				+ str3
				+ " com sucesso!"
				+ "</div>"
				+ "<div class='modal-footer'>"
				+ "<a class='btn btn-success' data-dismiss='modal' role='button' id='buttonOke'>Ok!</a>"
				+ "</div>"
	},
	disabledSucessMessage : function(str1, str2, str4) {
		return "<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>×</button>Inativar "
				+ str1
				+ "</div>"
				+ "<div class='modal-body'>"
				+ str2
				+ " inativad"
				+ str3
				+ " com sucesso!"
				+ "</div>"
				+ "<div class='modal-footer'>"
				+ "<a class='btn btn-success' data-dismiss='modal' role='button' id='buttonOkd'>Ok!</a>"
				+ "</div>"
	},
	publishSucessMessage : function(str1, str2, str4) {
		return "<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>×</button>Publicar "
				+ str1
				+ "</div>"
				+ "<div class='modal-body'>"
				+ str2
				+ " publicad"
				+ str3
				+ " com sucesso!"
				+ "</div>"
				+ "<div class='modal-footer'>"
				+ "<a class='btn btn-success' data-dismiss='modal' role='button' id='buttonOkd'>Ok!</a>"
				+ "</div>"
	},
	sendMessage : "<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>×</button>Encaminhar Chamado</div>"
			+ "<div class='modal-body'> Tem certeza que deseja encaminhar este chamado para {{entity}}?"
			+ "<br>"
			+ "</div>"
			+ "<div class='modal-footer'><a class='btn btn-success ebSim' data-dismiss='modal' role='button' id='bSim'>Sim</a>&nbsp&nbsp&nbsp"
			+ "<a class='btn btn-danger' data-dismiss='modal' role='button' id='bNao'>Não</a>"
			+ "</div>",
	sendSucessMessage : "<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>×</button>Sucesso</div>"
			+ "<div class='modal-body'>Chamado encaminhado com sucesso!"
			+ "<br>"
			+ "</div>"
			+ "<div class='modal-footer'>"
			+ "<a class='btn btn-success' data-dismiss='modal' role='button' id='buttonOkd'>Ok!</a>"
			+ "</div>",
	sendErrorMessage : "<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>×</button>Erro</div>"
			+ "<div class='modal-body'>Não foi possível encaminhar o chamado!"
			+ "<br>"
			+ "</div>"
			+ "<div class='modal-footer'>"
			+ "<a class='btn btn-success' data-dismiss='modal' role='button' id='buttonOkd'>Ok!</a>"
			+ "</div>",
	saveSucessMessage : "<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>×</button>Sucesso</div>"
			+ "<div class='modal-body'>{{message}}"
			+ "<br>"
			+ "</div>"
			+ "<div class='modal-footer'>"
			+ "<a class='btn btn-success' data-dismiss='modal' role='button' id='buttonOkd'>Ok!</a>"
			+ "</div>",
	saveErrorMessage : "<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>×</button>Erro</div>"
			+ "<div class='modal-body'>{{message}}"
			+ "<br>"
			+ "</div>"
			+ "<div class='modal-footer'>"
			+ "<a class='btn btn-success' data-dismiss='modal' role='button' id='buttonOkd'>Ok!</a>"
			+ "</div>",
	resultNotFound : "<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>×</button> Atenção </div>"
			+ "<div class='modal-body'> {{message}} "
			+ "</div>"
			+ "<div class='modal-footer'>"
			+ "<a class='btn btn-success' data-dismiss='modal' role='button' id='buttonOkd'>Ok!</a>"
			+ "</div>",

	resultMedia : "<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>×</button>Erro</div>"
			+ "<div class='modal-body'>{{#each medias}}"
			+ "<img src=\"{{img}}\" height=\"22\" width=\"22\">"
			+ "{{/each}}"
			+ "<br>"
			+ "</div>"
			+ "<div class='modal-footer'>"
			+ "<a class='btn btn-success' data-dismiss='modal' role='button' id='buttonOkd'>Ok!</a>"
			+ "</div>",
	erroSelect : "<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>×</button>"
			+ "Erro</div>"
			+ "<div class='modal-body'>Por favor, selecione pelo menos uma opção!"
			+ "</div>"
			+ "<div class='modal-footer'><a class='btn btn-danger' data-dismiss='modal' role='button'>Ok!</a>"
			+ "</div>",
	erroSelectPerfil : "<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>×</button>"
			+ "Erro</div>"
			+ "<div class='modal-body'>Existe uma ou mais permissões de funcionalidades adicionais (como por exemplo: “Inclusão”, “Edição”, etc.) "
			+ "sem a respectiva permissão do tipo “Consulta”. Antes de incluir permissões de funcionalidades adicionais, "
			+ "deve-se incluir a permissão do tipo “Consulta”!”"
			+ "</div>"
			+ "<div class='modal-footer'><a class='btn btn-danger' data-dismiss='modal' role='button'>Ok!</a>"
			+ "</div>",
	loginErrorMessage : "<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>×</button>"
			+ "Erro</div>"
			+ "<div class='modal-body'>{{message}}"
			+ "</div>"
			+ "<div class='modal-footer'><a class='btn btn-danger' data-dismiss='modal' role='button'>Ok!</a>"
			+ "</div>",
			errorMessage : function(message){return "<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>×</button>"
				+ "Erro</div>"
				+ "<div class='modal-body'>"+message
				+ "</div>"
				+ "<div class='modal-footer'><a class='btn btn-danger' data-dismiss='modal' role='button'>Ok!</a>"
				+ "</div>";},
	disableProfileErrorMessage : "<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>×</button>"
			+ "Erro</div>"
			+ "<div class='modal-body'>{{message}}"
			+ "</div>"
			+ "<div class='modal-footer'><a class='btn btn-danger' data-dismiss='modal' role='button'>Ok!</a>"
			+ "</div>",
	viewMedia : "<div class='item active'>"
			+ "<img src='http://www.clickerzoneuk.co.uk/cz/wp-content/uploads/2010/10/PuppySmall.jpg' alt='Cachorrinho 1'>"
			+ "</div>"
			+ "<div class='item'>"
			+ "<img src='http://www.pawderosa.com/images/puppies.jpg' alt='Cachorrinho 2'>"
			+ "</div>"
			+ "<div class='item'>"
			+ "<img src='http://cdn.sheknows.com/articles/2013/04/Puppy_2.jpg' alt='Cachorrinho 3'>"
			+ "</div>",
	modalMessageModel : "<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>×</button>"
			+ "Mensagens Padrão</div>"
			+ "<div class='modal-body'>"
			+ "<fieldset>"
			+ "<table class='table table-hover'>"
			+ "<thead><tr><th>Mensagens</th></tr></thead>"
			+ "<tbody>"
			+ "{{#each jsonList}}"
			+ "<tr>"
			+ "<td>{{name}}</td>"
			+ "<td class=\"hideTD\">{{messageBody}}</td>"
			+ "</tr>"
			+ "{{/each}}"
			+ "</tbody></table>"
			+ "</fieldset>"
			+ "<br />"
			+ "</div>",
	forgotPasswordSuccessMessage : "<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>×</button>"
			+ "Recuperação de Senha</div>"
			+ "<div class='modal-body'>{{message}}"
			+ "</div>"
			+ "<div class='modal-footer'><a class='btn btn-success' data-dismiss='modal' role='button'>Ok!</a>"
			+ "</div>",
	alterPasswordSuccessMessage : "<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>×</button>"
			+ "Alteração de Senha</div>"
			+ "<div class='modal-body'>{{message}}"
			+ "</div>"
			+ "<div class='modal-footer'><a data-dismiss='modal' data-toggle='modal' href='#myModal' class='btn btn-success' role='button'>Ok!</a>"
			+ "</div>",
	forgotPasswordError : "<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>×</button>"
			+ "Erro</div>"
			+ "<div class='modal-body'>{{message}}"
			+ "</div>"
			+ "<div class='modal-footer'><a data-dismiss='modal' data-toggle='modal' href='#myModal' class='btn btn-success'  role='button'>Ok!</a>"
			+ "</div>",
	errorResultReportMessage : "<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>×</button>"
			+ "Erro</div>"
			+ "<div class='modal-body'>Nenhum registro encontrado.</br>"
			+ "</div>"
			+ "<div class='modal-footer'><a class='btn btn-danger' data-dismiss='modal' role='button'>Ok!</a>"
			+ "</div>",
	successReportGeneratedMessage : "<div class='modal-header'><button class='close' aria-hidden='true' data-dismiss='modal' type='button'>×</button>"
			+ "Relatório Gerado</div>"
			+ "<div class='modal-body'>O relatório foi gerado com sucesso e salvo no diretório C:/temp/ </br>"
			+ "</div>"
			+ "<div class='modal-footer'><a class='btn btn-danger' data-dismiss='modal' role='button'>Ok!</a>"
			+ "</div>",

	mediaList : function(photo2) {
		return photo2
	},
	viewMedia : function(photo1) {
		return photo1
	},

};

ModelsTempl = {

};

function forgotPError(str1) {
	Templates.compile(ModelsTmpl.forgotPasswordError, "#notificacoes", str1);
}

function forgotPSuccess(str1) {
	Templates.compile(ModelsTmpl.forgotPasswordSuccessMessage, "#notificacoes",
			str1);
}

function alterPSuccess(str1) {
	Templates.compile(ModelsTmpl.alterPasswordSuccessMessage, "#notificacoes",
			str1);
}

function saveMessageMobile(str1, str2, str3) {
	Templates.compile(ModelsTmpl.saveMessageMobile, "#notificacoes", {});
}

function enabledSucessMessage(str1, str2, str3) {
	Templates.compile(ModelsTmpl.enabledSucessMessage(str1, str2, str3),
			"#notificacoes", {});
}
function disabledSucessMessage(str1, str2, str3) {
	Templates.compile(ModelsTmpl.disabledSucessMessage(str1, str2, str3),
			"#notificacoes", {});
}
function publishSucessMessage(str1, str2, str3) {
	Templates.compile(ModelsTmpl.publishSucessMessage(str1, str2, str3),
			"#notificacoes", {});
}
function saveMessage(str1, str2, str3) {
	Templates.compile(ModelsTmpl.saveMessage(str1, str2, str3),
			"#notificacoes", {});
}

function updateMessage(str1, str2, str3) {
	Templates.compile(ModelsTmpl.updateMessage(str1, str2, str3),
			"#notificacoes", {});
}

function replyMessage(str1, resp) {
	Templates.compile(ModelsTmpl.replyMessage(str1, resp), "#notificacoes", {});
}

function editMessage(str1, str2, str3) {
	Templates.compile(ModelsTmpl.editMessage(str1, str2, str3),
			"#notificacoes", {});
}

function passwordErrorMessage() {
	Templates.compile(ModelsTmpl.passwordErrorMessage, "#notificacoes", {});
}

function loginErrorMessage(data) {
	Templates.compile(ModelsTmpl.loginErrorMessage, "#notificacoes", data);
}

function disableProfileErrorMessage(data) {
	Templates.compile(ModelsTmpl.disableProfileErrorMessage,
			"#notificacoesError", data);
}

function mediaModal(media) {
	Templates.compile(ModelsTmpl.resultMedia, "#notificacoes", media);
}

function validateError() {
	Templates.compile(ModelsTmpl.validateError, "#notificacoes", {});
}

function saveMessageMobile() {
	Templates.compile(ModelsTmpl.saveMessageMobile, "#notificacoes", {});
}

function saveMessageWeb(url) {
	Templates.compile(ModelsTmpl.saveMessageWeb(url), "#notificacoes", {});
}

function saveMessageInternalCall(url) {
	Templates.compile(ModelsTmpl.saveMessageInternalCall(url), "#notificacoes", {});
}

function replyCall(data) {
	Templates.compile(ModelsTmpl.tmplReplyCall, "#camposI", data);
}

function enabledMessage(name, str1, str2, str3) {
	var nameObj = {
		'name' : name
	};
	Templates.compile(ModelsTmpl.enabledMessage(str1, str2, str3),
			"#notificacoesE", nameObj);
}

function disabledMessage(name, str1, str2, str3) {
	var nameObj = {
		'name' : name
	};
	Templates.compile(ModelsTmpl.disabledMessage(str1, str2, str3),
			"#notificacoesD", nameObj);
}

function publishMessage(name, str1, str2, str3) {
	var nameObj = {
		'name' : name
	};
	Templates.compile(ModelsTmpl.publishMessage(str1, str2, str3),
			"#notificacoesD", nameObj);
}

function confirmSend(entity) {
	var entityObj = {
		'entity' : entity
	};
	Templates.compile(ModelsTmpl.sendMessage, "#notificacoesD", entityObj);

}
function duplicatedMessage(resp) {
	Templates
			.compile(ModelsTmpl.duplicatedMessage(resp), "#notificacoes", resp);
}

function resultNotFound(data) {
	Templates.compile(ModelsTmpl.resultNotFound, "#notificacoes", data);
}

function errorResultReportMessage(data) {
	Templates.compile(ModelsTmpl.errorResultReportMessage, "#notificacoes",
			data);
}

function successReportGeneratedMessage(data) {
	Templates.compile(ModelsTmpl.successReportGeneratedMessage, "#notificacoes", data);
}

function erroSelect() {
	Templates.compile(ModelsTmpl.erroSelect, "#notificacoes", {});
}
function errorMessage(message) {
	Templates.compile(ModelsTmpl.errorMessage(message), "#notificacoes", message);
}
function erroSelectPerfil() {
	Templates.compile(ModelsTmpl.erroSelectPerfil, "#notificacoes", {});
}

function tempViewMedia(photo1, photo2) {
	Templates.compile(ModelsTmpl.mediaList(photo2), "#lista-midia", {});
	Templates.compile(ModelsTmpl.viewMedia(photo1), "#carousel-inner", {});
}

function modalMessageModel(data) {
	Templates.compile(ModelsTmpl.modalMessageModel, "#notificacoes", data);
}
