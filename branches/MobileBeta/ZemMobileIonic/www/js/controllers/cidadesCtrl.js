   angular.module('app.controllers')

   .controller('cidadesCtrl', function($scope, $state, MobileFactory, CitizenService, AddressService, $ionicFilterBar, $ionicLoading, $ionicPopup) {
       $ionicLoading.show({
           content: 'Loading',
           animation: 'fade-in',
           showBackdrop: true,
           maxWidth: 200,
           showDelay: 0
       });
       $scope.cidades;
       $scope.citizen = CitizenService.getCitizen();

       function listCity(stateId) {
           MobileFactory.getCity(stateId).then(function(data) {
               $scope.cidades = data.data;
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
       $scope.setCity = function(cidade) {
           $scope.citizen.cityId = cidade;
           AddressService.setCityId(cidade.cityId);
           //console.log(cidade.cityId);
           $state.go('cadastro2');
       }

       function showFilterBar() {
           var filterBarInstance = $ionicFilterBar.show({
               cancelText: "<i class='ion-ios-close-outline'></i>",
               items: $scope.cidades,
               filterProperties: ['name'],
               update: function(filteredItems, filterText) {
                   $scope.cidades = filteredItems;
               }
           });
       };
       listCity(AddressService.getStateId());
   })
