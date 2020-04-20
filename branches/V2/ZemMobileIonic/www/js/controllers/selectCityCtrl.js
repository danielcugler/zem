angular.module('app.controllers')
	.controller('selectCityCtrl', function ($scope, $state, MobileFactory, $ionicHistory, IpService, $ionicPopup, SelectCityService, $timeout) {
		// $ionicHistory.nextViewOptions({
		//     disableAnimate: true
		// });
		$scope.ipConnect = true;
		$scope.$on('$ionicView.enter', function () {
			$scope.selectedCity = SelectCityService.getCity();
			var ipLocal = window.localStorage.getItem("ipService");
			if (ipLocal != null) {
				$state.go('zEMZeladoriaMunicipal');
			}
		});

		$scope.getIpServer = function (selectCity) {
			if (selectCity.length != 0) {
				//parte responsavel por pegar o ip da cidade selecionada, setar no localStorage e no IP
				console.log(selectCity.webAddress);
				var confirmPopup = $ionicPopup.confirm({
					title: 'Confirmar cidade.',
					template: 'Você tem certeza que deseja registrar seus chamados para a cidade de ' + selectCity.name + '?',
					okType: 'button-zem',
				});
				confirmPopup.then(function (res) {
					if (res) {

						/*		
									$timeout(function () {
										IpService.setIp(selectCity.webAddress);
										localStorage.setItem("cityId", selectCity.cityId);
									}, 1000).then(function (success) {
										$state.go('zEMZeladoriaMunicipal');
									});
						*/

						new Promise(function (resolve, reject) {
							IpService.setIp(selectCity.webAddress);
							localStorage.setItem("cityId", selectCity.cityId);
							localStorage.setItem("cityName", selectCity.name);
							resolve('IpService completed');
						}).then(function (success) {
							$state.go('zEMZeladoriaMunicipal');
						});



					}
				});
			} else {
				//quando a opção default e selecionada
				$ionicPopup.alert({
					title: 'Atenção',
					okType: 'button-zem',
					template: 'E necessário selecionar uma cidade para registrar seus chamados.'
				});
				console.log("Não foi selecionada nenhuma cidade");
			}

		}

		$scope.selecionarCidade = function () {
			$state.go('cidades2');
		}
	})
