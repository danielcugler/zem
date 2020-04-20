angular.module('app.controllers')
    .controller('estadosCtrl', function($scope, $state, MobileFactory, CitizenService, AddressService, $ionicFilterBar, $ionicLoading, $ionicPopup) {
        $ionicLoading.show({
            content: 'Loading',
            animation: 'fade-in',
            showBackdrop: true,
            maxWidth: 200,
            showDelay: 0
        });
        $scope.estados;
        $scope.citizen = CitizenService.getCitizen();

        function listStates() {
            MobileFactory.getStates().then(function(data) {
                console.log(data);
                $scope.estados = data.data;
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
        $scope.setState = function(estado) {
            $scope.citizen.state = estado;
            AddressService.setStateId(estado.stateId);
            $state.go('cadastro2');
            //$window.location.href = '#/cadastro2';
        }

        function showFilterBar() {
            var filterBarInstance = $ionicFilterBar.show({
                cancelText: "<i class='ion-ios-close-outline'></i>",
                items: $scope.estados,
                filterProperties: ['name'],
                update: function(filteredItems, filterText) {
                    $scope.estados = filteredItems;
                }
            });
        };
        listStates();
    })
