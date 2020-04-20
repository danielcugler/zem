   angular.module('app.controllers')

   .controller('cidades2Ctrl', function($scope, $state, MobileFactory, CitizenService, AddressService, $ionicFilterBar) {
$scope.cidades;
$scope.citizen=CitizenService.getCitizen();
     function listCity(ufeSg){
      MobileFactory.getCity(ufeSg).then(function(data){
  $scope.cidades = data.data;
  showFilterBar();
});
}
$scope.setCity = function(cidade){
$scope.citizen.cityId=cidade;
AddressService.setCityId(cidade.cidadeId);
$state.go('cadastro2');
}
 function showFilterBar () {
    var filterBarInstance = $ionicFilterBar.show({
      cancelText: "<i class='ion-ios-close-outline'></i>",
      items: $scope.cidades,
      filterProperties: ['nome'],
      update: function (filteredItems, filterText) {
        $scope.cidades = filteredItems;
      }
    });
  };
    listCity(AddressService.getStateId());   
 })