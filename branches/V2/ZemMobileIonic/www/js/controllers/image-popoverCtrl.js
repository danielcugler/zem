angular.module('app.controllers')
  .controller('image-popoverCtrl', function($scope, UnsolvedCallService, MobileFactory, $ionicModal) {
    $scope.photos = [];
    MobileFactory.getMedia($scope.unsolvedCall.parentCallId.unsolvedCallId).then(function(data) {
      $scope.photos = data.data;
    });

		/*Galeria de Imagens*/
    $scope.showImages = function(index) {
      $scope.activeSlide = index;
      $scope.showModal('templates/image-popover.html');
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
