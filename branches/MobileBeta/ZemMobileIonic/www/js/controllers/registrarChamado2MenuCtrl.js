angular.module('app.controllers')

.controller('registrarChamado2MenuCtrl', function($scope, $state, $ionicPopup, AnonymousService) {
    function anCall() {
        AnonymousService.setAnonymous(0);
        console.log("");
        $state.go("menu.registrarChamado3Menu");
    };

    $scope.next = function(){
        $state.go("menu.registrarChamado3Menu");
    }

    $scope.confirmAnonymous = function() {
        // A confirm dialog
        var confirmPopup = $ionicPopup.confirm({
            title: 'Atenção!',
            template: 'Caso você registre um chamado anônimo, não será possível acompanhá-lo futuramente. Deseja continuar?',
            cancelText: 'Cancelar', // String (default: 'Cancel'). The text of the Cancel button.
            okText: 'Confirmar', // String (default: 'OK'). The text of the OK button.
            okType: 'button-zem'
        });

        confirmPopup.then(function(res) {
            if (res) {
                console.log('Confirmar');
                anCall();
            } else {
                console.log('Cancelar');
            }
        });
    };
});
