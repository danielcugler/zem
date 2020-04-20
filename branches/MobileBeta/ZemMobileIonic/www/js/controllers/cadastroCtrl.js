angular.module('app.controllers')

.controller('cadastroCtrl', function($scope, CitizenService, $state, $ionicPlatform) {
    $scope.citizen = { 'state': '', 'cityId': '', 'neighborhoodId': '' };
    $scope.citizen.enabled = 0;
    CitizenService.setCitizen($scope.citizen);

    $scope.proximo = function(form) {
        if (form.$valid) {
            $state.go('cadastro2');
        } else {
            $scope.enviado = true;
        }
    }
});
