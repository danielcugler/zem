		function validateError() {
						Templates.compile(ModelsTmpl.validateError, "#notificacoes",{});
					}
		function enabledSucessMessage() {
			Templates.compile(ModelsTmpl.enabledSucessMessage, "#notificacoes",{});
		}
		function disabledSucessMessage() {
			Templates.compile(ModelsTmpl.disabledSucessMessage, "#notificacoes",{});
		}
		function saveMessage() {
			Templates.compile(ModelsTmpl.saveMessage, "#notificacoes",{});
		}
		function editMessage() {
			Templates.compile(ModelsTmpl.editMessage, "#notificacoes",{});
		}

		function validateError() {
			Templates.compile(ModelsTmpl.validateError, "#notificacoes",{});
		}
		function erroSelect() {
			Templates.compile(ModelsTmpl.erroSelect, "#notificacoes",{});
		}

		function enabledMessage(name) {
			var nameObj = {'name': name};
			Templates.compile(ModelsTmpl.enabledMessage,"#notificacoes", nameObj);
		}

		function disabledMessage(name) {
			var nameObj = {'name': name};
			Templates.compile(ModelsTmpl.disabledMessage,"#notificacoes", nameObj);
		}