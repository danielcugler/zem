angular.module('app.controllers')

.controller('registrarChamado2Ctrl', function($scope, $state, $ionicPopup, AnonymousService) {
  function anCall() {
    AnonymousService.setAnonymous(0);
    $state.go("registrarChamado3");
  };

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
