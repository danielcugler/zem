angular.module('app.controllers')

	.controller('inserirCPFCtrl', function ($scope, $state, $http, UserService, $ionicPopup, MobileFactory, IpService, $ionicHistory, CitizenService) {

		//var url = "http://192.168.1.109:8080/rest/mobile/";
		var url = IpService.getIp();
		$scope.inserirCpf = function (cpf, form) {
			console.log("CPF digitado:" + cpf);
			if (form.$valid) {
				verificaCpf(cpf);
			} else {
				$scope.enviado = true;
			}
		}

		//O método "verificaCpf" é responsável por verificar se o usuário já possui conta ZEM ou não.
		function verificaCpf(cpf) {
			$http.get(url + "/citizen/" + cpf).then(
				function (data) { //Success
					var user = UserService.getUser();
					insertNewUser(cpf, user.name, user.userID, user.email);
					//se for "1" o cpf já existe no banco, o que significa que o usuário já possui login no ZEM mas nunca logou com facebook.
					// se for "0" o cpf não existe no banco, o que significa que o usuário nunca logou nem com o ZEM nem com o facebook.
					if (data.data.code == "1") {
						console.log("Usuário já possui conta ZEM.");
						//Envia e-mail para confirmação da validação.
						sendConfirmationEmail(cpf);
						//insertFacebookId(cpf, user.userID);
					}
				},
				function (data) { //Error

				});
		}

		//O método "insertFacebookId" é chamado caso o usuário já possua conta ZEM e deseja vincular sua conta do Facebook a ela.
		function insertFacebookId(cpf, facebookId) {
			console.log("Inserindo facebookID em usuário que já possui conta ZEM.");
			$http({
				method: "PUT",
				url: url + "/citizen",
				params: {
					"cpf": cpf,
					"facebookId": facebookId
				},
				headers: { 'Content-Type': 'application/json' },
				responseType: 'json'
			}).success(function () {
				$state.go('menu.mainPage');
			}).error(function () {
				$state.go('zEMZeladoriaMunicipal');
			});
		}

		//O método "insertNewUser" é chamado quando o usuário loga pela primeira vez com sua conta do Facebook e não possui conta ZEM.
		function insertNewUser(cpf, name, facebookId, email) {
			console.log("Inserindo um novo usuário no sistema.");
			$http({
				method: 'POST',
				url: url + "/citizen",
				data: {
					"citizen_cpf": cpf + "f",
					"name": name,
					"email": email,
					"facebookId": facebookId,
					"enabled": 0
				},
				headers: { 'Content-Type': 'application/json' },
				responseType: 'json'
			}).then(function (data) { //Success
				//limpar o cache e bloquear o back
				$ionicHistory.clearCache();
				$ionicHistory.nextViewOptions({
					disableBack: true
				});
				//pegando o cpf cpf do usuário, e adicionando no citizem service
				CitizenService.setCitizen({ 'citizenCpf': cpf + 'f' });





				$state.go('menu.mainPage');
			}, function (data) { //Error
				//limpar o cache e bloquear o back
				$ionicHistory.clearCache();
				$ionicHistory.nextViewOptions({
					disableBack: true
				});
				$ionicPopup.alert({
					title: 'Erro',
					template: 'Não foi possivel logar com o facebook, tente novamente mais tarde.',
					okType: 'button-assertive'
				});
				$state.go('zEMZeladoriaMunicipal');
			});
		}

		function sendConfirmationEmail(cpf) {
			$http.get(url + "/email/" + cpf).then(function (data) {
				$ionicPopup.alert({
					title: 'Atenção',
					template: 'E-mail enviado para ' + data.data.email + '. Para confirmar o vínculo de sua conta ZEM com o Facebook, acesse o link enviado.',
					okType: 'button-zem'
				});
			}, function (data) {

			});
		}

		$scope.showAlertCpf = function () {
			var alertPopup = $ionicPopup.alert({
				title: 'Por que informar meu CPF?',
				okType: 'button-zem',
				template: 'Prezado Cidadão,<br><br>O aplicativo ZEM interage diretamente com a prefeitura, um órgão público que, através da sua ajuda, irá direcionar esforços com o intuito de melhorar a sua cidade. Sendo assim, informar o seu CPF é necessário para garantir a credibilidade do seu pedido. Além disso, com o seu CPF em mãos a prefeitura te garantirá muito mais agilidade nos atendimentos por telefone.<br><br>Mas fique tranquilo! A segurança do seu chamado é garantida pela prefeitura. Em nenhuma hipótese os seus dados serão fornecidos a terceiros, e somente a prefeitura terá acesso ao seu chamado.<br><br>Atenciosamente,<br><br>Equipe ZEM.'
			});
		};

	})
