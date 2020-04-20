angular.module('app.controllers')

.controller('detalhamentoChamadoCtrl', function($scope, UnsolvedCallService, MobileFactory, $state, $ionicHistory, $ionicModal) {

    $scope.unsolvedCall = UnsolvedCallService.getUnsolvedCall();
    $scope.photos = [];
    MobileFactory.getMedia($scope.unsolvedCall.unsolvedCallId).then(function(data) {
        $scope.photos = data.data.photos;
    });

    $scope.returnMenu = function() {
        //limpar o cache e bloquear o back
        $ionicHistory.clearCache();
        $ionicHistory.nextViewOptions({
            disableBack: true
        });
        $state.go('menu.mainPage');
    }

    /*Galeria de Imagens*/
    $scope.buttonHelp = function(index) {
        $scope.activeSlide = index;
        $scope.showModal('helpRC01.html');
    }
    $scope.showImages = function(index) {
        $scope.activeSlide = index;
    }
    $scope.showModal = function(templateUrl) {
            $ionicModal.fromTemplateUrl(templateUrl, {
                scope: $scope,
                animation: 'slide-in-up'
            }).then(function(modal) {
                $scope.modal = modal;
                $scope.modal.show();
            });
        }
    // Close the modal
    $scope.closeModal = function() {
        $scope.modal.hide();
        $scope.modal.remove();
    };

})
