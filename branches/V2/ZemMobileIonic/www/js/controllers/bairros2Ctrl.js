angular.module('app.controllers')
    .controller('bairros2Ctrl', function($scope, $state, MobileFactory, UnsolvedCallService, $ionicFilterBar, AddressService, CitizenService, $ionicLoading, $ionicPopup) {
        $scope.bairros;
        $scope.unsolvedCall = UnsolvedCallService.getUnsolvedCall();
        $scope.citizen = CitizenService.getCitizen();

        $ionicLoading.show({
            content: 'Loading',
            animation: 'fade-in',
            showBackdrop: true,
            maxWidth: 200,
            showDelay: 0
        });

        function listNeighborhood() {
            var cityId = localStorage.getItem("cityId");
            MobileFactory.getNeighborhood2(cityId).then(function(data) {
                $scope.bairros = data.data;
                showFilterBar();
                $ionicLoading.hide();
            }).catch(function(callback) {
                console.log(callback);
                $ionicLoading.hide();
                var alertPopup = $ionicPopup.alert({
                    title: 'Erro',
                    template: 'Não foi possível se conectar ao servidor. Tente novamente mais tarde.'
                });
                $state.go("menu.registrarChamado5Menu");
            });
        }
        $scope.setNeighborhood = function(bairro) {
            $scope.unsolvedCall.addressId.neighborhoodId = bairro;
            UnsolvedCallService.setUnsolvedCall($scope.unsolvedCall);
            if ($state.current.name == "menu.bairros2") {
                $state.go('menu.registrarChamado5');
            } else {
                $state.go('registrarChamado5');
            }
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
        listNeighborhood();

    })
