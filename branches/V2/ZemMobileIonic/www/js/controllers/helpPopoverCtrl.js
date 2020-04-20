angular.module('app.controllers')
    .controller('helpPopoverCtrl', function($scope, UnsolvedCallService, MobileFactory, $ionicModal, $ionicSlideBoxDelegate) {
        $scope.photosTeste = [];
        var img1 = 'img/tutorial/MINHASMENSAGENS.png';
        var img2 = 'img/tutorial/TELAMEUSCHAMADOS.png';
        $scope.photosTeste.push(img1);
        $scope.photosTeste.push(img2);

        /*Galeria de Imagens*/
        $scope.showImages = function(index) {
            $scope.activeSlide = index;
            $scope.showModal('templates/helpPopover.html');
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

        //botão avançar e voltar
        $scope.next = function() {
            $ionicSlideBoxDelegate.next();
        };
        $scope.previous = function() {
            $ionicSlideBoxDelegate.previous();
        };

    })
