angular.module('app.controllers')

.controller('registrarChamado5MenuCtrl', function($scope, UnsolvedCallService, $window, $state, $ionicPopup) {
    $scope.unsolvedCall = UnsolvedCallService.getUnsolvedCall();
    $scope.next = function(form) {
        if (form.$valid) {
            if ($scope.unsolvedCall.addressId.neighborhoodId != null) {
                UnsolvedCallService.setUnsolvedCall($scope.unsolvedCall);
                //console.log($scope.unsolvedCall);
                $state.go("menu.registrarChamado6Menu");
            } else {
                $ionicPopup.alert({
                    title: 'Atenção',
                    template: 'E necessário selecione um bairro.',
                    okType: 'button-zem'
                });
            }
        } else {
            $scope.enviado = true;
        }
    }
    $scope.setBairro = function() {
        $state.go("menu.bairros2");
        //$window.location.href = '#/bairros2';
    }
});
