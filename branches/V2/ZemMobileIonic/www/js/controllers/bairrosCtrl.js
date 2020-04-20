angular.module('app.controllers')
    .controller('bairrosCtrl', function($scope, $state, MobileFactory, CitizenService, $ionicFilterBar, AddressService, $ionicLoading, $ionicPopup) {
        $ionicLoading.show({
            content: 'Loading',
            animation: 'fade-in',
            showBackdrop: true,
            maxWidth: 200,
            showDelay: 0
        });
        $scope.bairros;
        $scope.citizen = CitizenService.getCitizen();

        function listNeighborhood(cityId) {
            //console.log(cityId);
            MobileFactory.getNeighborhood(cityId).then(function(data) {
                $scope.bairros = data.data;
                //console.log("bairros");
                //console.log($scope.bairros);
                showFilterBar();
                $ionicLoading.hide();
            }).catch(function(fallback) {
                $ionicLoading.hide();
                var alertPopup = $ionicPopup.alert({
                    title: 'Erro',
                    template: 'Não foi possível se conectar ao servidor. Tente novamente mais tarde.'
                });
                $state.go('cadastro2');
            });
        }
        $scope.setNeighborhood = function(bairro) {
            $scope.citizen.neighborhoodId = bairro;
            $state.go('cadastro2');
        }

        function showFilterBar() {
            var filterBarInstance = $ionicFilterBar.show({
                cancelText: "<i class='ion-ios-close-outline'></i>",
                items: $scope.bairros,
                filterProperties: ['name'],
                update: function(filteredItems, filterText) {
                    $scope.bairros = filteredItems;
                }
            });
        };
        listNeighborhood(AddressService.getCityId());

    })
