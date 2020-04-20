var moduleENadd = (function($, module) {
	// security rules
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	// cached DOM
	var $generalModule = $('#generalModule');
	//title
	var $title = $generalModule.find("#title2");
	//fields
	var $fieldModule = $generalModule.find('#fieldModule');
	var $formModule = $generalModule.find('#form-cadastro-entity');
	var $inEntityId = $fieldModule.find('#inEntityId');
	var $inName = $fieldModule.find('#inName');
	var $inEnabled = $fieldModule.find('#inEnabled');
	var $inLow = $fieldModule.find('#inLow');
	var $inMedium = $fieldModule.find('#inMedium');
	var $inHigh = $fieldModule.find('#inHigh');
	var $ckPriority = $fieldModule.find('#ckPriority');
	var $selectEntityCategory = $fieldModule.find('#selectEntityCategory');
	// buttons
	var $buttonModule = $fieldModule.find("#botoes");
	var $btAdd = $buttonModule.find("#btAdd");
	var $btEdit = $buttonModule.find("#btEdit");
	// modal
	var $modalE = $generalModule.find("#modalE");
	var $notificacoesE = $generalModule.find("#notificacoesE");
	// bind actions
	$btAdd.on('click', save);
	$btEdit.on('click', edit);
/*	 $(".ms-selectEntityCategory .disabled").on( "click",function(event){
		 event.preventDefault();		 
		 modal("Entidade","Categoria de entidade já associada a um chamado, não é possível remover");
	 });
	 */
	// templates
	$(".icone").on("click", function(e){
		e.preventDefault();
		
		var classIcon = $(this).children().attr('class');
		$('#entityIcon').removeClass().addClass(classIcon);
	});
	var $messageTpl = $("#message-template").html();
	// functions

	$ckPriority.on('ifChecked', function(event) {
		$inLow.val($ckPriority.data('low'));
		$inMedium.val($ckPriority.data('medium'));
		$inHigh.val($ckPriority.data('high'));
		$inLow.prop('disabled', true);
		$inMedium.prop('disabled', true);
		$inHigh.prop('disabled', true);
	});

	$ckPriority.on('ifUnchecked', function(event) {
		$inLow.val("");
		$inMedium.val("");
		$inHigh.val("");
		$inMedium.prop('disabled', false);
		$inHigh.prop('disabled', false);
		$inLow.prop('disabled', false);
	});

	$formModule.validate({

		errorPlacement : function(error, element) {
			$(element).parent().addClass("has-error");
			error.insertAfter(element);
			error.wrap("<p>");
			error.css('color', 'red');
		},
		rules : {
			inName : {
				required : true,
				maxlength : 40
			},
			inHigh : {
				required : true
			},
			inMedium : {
				required : true

			},
			inLow : {
				required : true

			}
		},
		messages : {
			inLow : "Insira a prioridade baixa",
			inMedium : "Insira a prioridade média",
			inHigh : "Insira a prioridade alta",
			inName : {
				required : "Insira o nome",
				maxlength : $.validator
						.format("Por favor insira menos que {0} caracteres.")
			}

		}
	});

	var changeButton = function() {
		$btAdd.hide();
		$btEdit.show();
	}
	function restError(jqXHR, textStatus) {
		var body = jqXHR.responseJSON.errorCode + ", "
				+ jqXHR.responseJSON.errorMessage;
		modal("Entidade", body, 2);
	}
	// render
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
	$selectEntityCategory
			.multiSelect({
				selectableHeader : "<div class='custom-header'>Categorias existentes</div>",
				selectionHeader : "<div class='custom-header'>Categorias selecionadas</div>",
				selectableFooter : "<div class='custom-footer'><a id='selecionarTodos'>Selecionar todas</a></div>",
				selectionFooter : "<div class='custom-footer'><a id='removerTodos'>Remover todas</a></div>",
				afterInit: function(){
					$("#selecionarTodos").on("click", selecionaTudo);
					$("#removerTodos").on("click", removeTudo);
				}
			
			});
	
	function getUrl(key) {
		var result = new RegExp(key + "=([^&]*)", "i")
				.exec(window.location.search);
		return result && unescape(result[1]) || "";
	}
	function getFields() {
		var entity = {};
		entity.entityId = $inEntityId.val();
		entity.name = $inName.val();
		entity.attendanceTime = {};
		entity.attendanceTime.highPriorityTime = $inHigh.val();
		entity.attendanceTime.mediumPriorityTime = $inMedium.val();
		entity.attendanceTime.lowPriorityTime = $inLow.val();
		entity.attendanceTime.enabled = 0;
		entity.enabled = $inEnabled.val();
		entity.icon = $("#entityIcon").attr('class');
		entity.entityCategoryCollection = [];
		//var ec = $selectEntityCategory.val();
		console.log($selectEntityCategory.val());
		var ec = $("#selectEntityCategory :selected").each(function( key,value) {
			entity.entityCategoryCollection.push({"entityCategoryId" : parseInt(value.value)});
		});
	//	entity.entityCategoryCollection = ec;
	//	for ( var key in ec)
		//	entity.entityCategoryCollection.push({
		//		"entityCategoryId" : parseInt(ec[key])
		//	});
		return JSON.stringify(entity);
	}
	function setFields(resp) {
		$inEntityId.val(resp.entityId);
		$inName.val(resp.name);
		$inHigh.val(resp.attendanceTime.highPriorityTime);
		$inMedium.val(resp.attendanceTime.mediumPriorityTime);
		$inLow.val(resp.attendanceTime.lowPriorityTime);
		$inEnabled.val(resp.enabled);
		$('#entityIcon').removeClass().addClass(resp.icon);
	}

	function getEntity(id) {

		$
				.ajax({
					type : 'GET',
					async : false,
					url : '../rest/entitycategory2/combosativos',
					dataType : "json",
					contentType : "application/json"
				})
				.done(
						function(data) {
							for (cont = 0; cont < data.length; cont++)
								$selectEntityCategory
										.multiSelect(
												'addOption',
												{
													value : data[cont].entityCategoryId,
													text : data[cont].name,
													index : cont
												});

							$
									.ajax({
										type : 'GET',
										url : "../rest/entity2/" + id,
										dataType : "json",
										contentType : "application/json"
									})
									.done(
											function(resp) {
												setFields(resp);
												for (cont = 0; cont < resp.entityCategoryCollection.length; cont++)
													$selectEntityCategory
															.multiSelect(
																	'select',
																	resp.entityCategoryCollection[cont].entityCategoryId
																			.toString());

												$
														.ajax(
																{
																	type : 'GET',
																	url : '../rest/entity2/lock/'
																			+ id,
																	dataType : "json",
																	contentType : "application/json"
																})
														.done(
																function(data) {
																	for ( var ec in data) {
																		var $option = $selectEntityCategory
																				.find('option[value='
																						+ data[ec].entityCategoryId
																						+ ']');
																		$option
																				.prop(
																						"disabled",
																						true);
																	}
																	$selectEntityCategory
																			.multiSelect('refresh');

																}).fail(
																restError);

											}).fail(
											function(jqXHR, textStatus) {
											});
						}).fail(restError);
	}

	// Função selecionar todos os elementos do select
	function selecionaTudo() {
		$selectEntityCategory.multiSelect('select_all');
	}

	// Função remover todos os elementos do select
	function removeTudo() {
		$selectEntityCategory.multiSelect('deselect_all');
	}

	// Preencher o select com as categorias de entidade disponíveis
	function mSelect(data) {
		var cont = 0;
		for (cont = 0; cont < data.length; cont++)
			$selectEntityCategory.multiSelect('addOption', {
				value : data[cont].entityCategoryId,
				text : data[cont].name,
				index : cont
			});
	}

	// Preencher o select com as categorias de entidade selecionadas
	function mSelected(data) {
		var cont = 0;
		var temp;
		for (cont = 0; cont < data.length; cont++) {
			temp = data[cont].entityCategoryId.toString();
			$selectEntityCategory.multiSelect('select', temp);
		}
	}

	// AJAX | Busca todos as categorias de entidade
	function selectListAll() {
		$.ajax({
			type : 'GET',
			async : false,
			url : '../rest/entitycategory2/combosativos',
			dataType : "json",
			contentType : "application/json"
		}).done(function(data) {
			for (cont = 0; cont < data.length; cont++)
				$selectEntityCategory.multiSelect('addOption', {
					value : data[cont].entityCategoryId,
					text : data[cont].name,
					index : cont
				});
		}).fail(restError);
	}

	// AJAX | Busca os valores do tempo de atendimento padrão
	function getDefaultAt() {
		$.ajax({
			type : 'GET',
			async : false,
			url : '../rest/attendancetime2/at',
			dataType : "json",
			contentType : "application/json",
			success : function(resp) {
				$ckPriority.data("low", resp.lowPriorityTime);
				$ckPriority.data("medium", resp.mediumPriorityTime);
				$ckPriority.data("high", resp.highPriorityTime);
			},
			error : function(jqxhr, status, errorMsg) {
				// Mensagem de erro
			}
		});
	}

	function edit(event) {
		event.preventDefault();
		// validaISalvar();
		var ec = $selectEntityCategory.val();
		if ($formModule.valid() && ec) {
			$.ajax({
				type : 'PUT',
				url : '../rest/entity2/' + $('#username').text(),
				dataType : "json",
				data : getFields(),
				beforeSend : function(xhr) {
					xhr.setRequestHeader(header, token);
				},
				contentType : "application/json"
			}).done(function(data) {
				modal("Entidade", data.message, 1);
				return true;
			}).fail(restError);
		} else {
			if (ec)
				modal(
						"Entidade",
						"Por favor, selecione pelo menos uma categoria de entidade na lista!",
						3);
			else
				modal(
						"Entidade",
						"Por favor, preencha os campos destacados em vermelho!",
						3);
			return false;
		}
	}

	function save(event) {
		event.preventDefault();
		// validaISalvar();
		var ec = $selectEntityCategory.val();
		if ($formModule.valid() && ec) {
			$.ajax({
				type : 'POST',
				url : '../rest/entity2/' + $('#username').text(),
				dataType : "json",
				data : getFields(),
				beforeSend : function(xhr) {
					xhr.setRequestHeader(header, token);
				},
				contentType : "application/json"
			}).done(function(data) {
				modal("Entidade", data.message, 1);
				return true;
			}).fail(restError);
		} else {
			if (ec)
				modal(
						"Entidade",
						"Por favor, preencha os campos destacados em vermelho!",
						3);
			else
				modal(
						"Entidade",
						"Por favor, selecione pelo menos uma categoria de entidade na lista!",
						3);
			return false;

		}
	}
	function init() {
		$inEnabled.val('0');
		var id = getUrl('id');
		getDefaultAt();
		if (id) {
			document.title = "Edição de Entidade";
			$title.text("").append("Edição de <strong>Entidade</strong>");
			changeButton();
			getEntity(id)
		} else
			selectListAll();
	}
	init();

	module.getFields = getFields;
	return module;

})(jQuery, moduleENadd || {});