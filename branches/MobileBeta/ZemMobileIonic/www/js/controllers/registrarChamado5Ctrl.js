angular.module('app.controllers')

.controller('registrarChamado5Ctrl', function($scope, UnsolvedCallService, $window, $state, $ionicPopup) {
    $scope.unsolvedCall = UnsolvedCallService.getUnsolvedCall();
    $scope.next = function(form) {
        if (form.$valid) {
            if ($scope.unsolvedCall.addressId.neighborhoodId != null) {
                UnsolvedCallService.setUnsolvedCall($scope.unsolvedCall);
                //console.log($scope.unsolvedCall);
                $state.go("registrarChamado6");
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
        //$window.location.href = '#/bairrosU2';
        $state.go("bairrosU2");
    }
});
