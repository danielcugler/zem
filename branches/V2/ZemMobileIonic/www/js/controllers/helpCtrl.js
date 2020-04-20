angular.module('app.controllers')

.controller('helpCtrl', function($scope, $state, $ionicModal, $ionicHistory, $ionicSlideBoxDelegate) {
    $scope.view = function() {
        $state.go('helpView');
    }
    $scope.registrandoChamado = function() {
        $ionicModal.fromTemplateUrl('registrarChamadoHelp.html', {
            scope: $scope,
            animation: 'slide-in-up'
        }).then(function(modal) {
            $scope.modal = modal;
            $scope.modal.show();
        });
    }

    /* Registrando um chamado */
    $scope.goBackHelp = function() {
        //criando um valor no localStorage, para que o tutorial n seja mais exibido
        $scope.modal.hide();
        $scope.modal.remove();
        $scope.slideIndex = 0;
   };
    $scope.next = function() {
        $ionicSlideBoxDelegate.next();
    };
    $scope.previous = function() {
        $ionicSlideBoxDelegate.previous();
    };

    // Called each time the slide changes
    $scope.slideChanged = function(index) {
        $scope.slideIndex = index;
    };
})
