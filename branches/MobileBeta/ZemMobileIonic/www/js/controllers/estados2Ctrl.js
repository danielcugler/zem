angular.module('app.controllers')
.controller('estados2Ctrl', function($scope, $state, MobileFactory, UnsolvedCallService, AddressService, $ionicFilterBar) {
$scope.estados;
$scope.unsolvedCall= UnsolvedCallService.getUnsolvedCall();
     function listStates(){
      MobileFactory.getStates().then(function(data){
  $scope.estados = data.data;
  showFilterBar();
});
}
$scope.setState = function(estado){
$scope.unsolvedCall.addressId.state=estado;
$state.go('registrarChamado5');
}
 function showFilterBar () {
    var filterBarInstance = $ionicFilterBar.show({
      cancelText: "<i class='ion-ios-close-outline'></i>",
      items: $scope.estados,
      filterProperties: ['ufeNo'],
      update: function (filteredItems, filterText) {
        $scope.estados = filteredItems;
      }
    });
  };
    listStates();
})