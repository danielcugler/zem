angular.module('app.controllers')

	.controller('cidades2Ctrl', function ($scope, $state, MobileFactory, $ionicFilterBar, SelectCityService, $ionicPopup, $ionicLoading) {
		$scope.cidades;
		$scope.$on('$ionicView.enter', function () {
			$ionicLoading.show({
				content: 'Loading',
				animation: 'fade-in',
				showBackdrop: true,
				maxWidth: 200,
				showDelay: 0
			});
			MobileFactory.getSelectCity().then(function (data) {
				$scope.cidades = data.data;
				console.log($scope.cidades);
				//mostrar a barra de pesquisa
				showFilterBar();
				$ionicLoading.hide();
			}, function (erro) {
				$scope.ipConnect = false;
				console.log(erro);
				$ionicLoading.hide();
				$ionicPopup.alert({
					title: 'Erro',
					template: 'Não foi possível conectar ao servidor. Tente novamente mais tarde.',
					okType: 'button-assertive',
				});
				$state.go('selectCity');
			});
		});

		//apos clicar na cidade
		$scope.setCity = function (cidade) {
			SelectCityService.setCity(cidade);
			$state.go('selectCity');
		}

		//parte da busca
		function showFilterBar() {
			var filterBarInstance = $ionicFilterBar.show({
				cancelText: "<i class='ion-ios-close-outline'></i>",
				items: $scope.cidades,
				filterProperties: ['name'],
				update: function (filteredItems, filterText) {
					$scope.cidades = filteredItems;
				}
			});
		};
	})