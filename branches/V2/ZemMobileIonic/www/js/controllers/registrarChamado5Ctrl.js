angular.module('app.controllers')

    .controller('registrarChamado5Ctrl', function ($scope, UnsolvedCallService, NeighborhoodService, $window, $state, $ionicPopup, $ionicModal) {
        $scope.unsolvedCall = UnsolvedCallService.getUnsolvedCall();
        $scope.tempNeighborhoodName = NeighborhoodService.getNeighborhood();
        $scope.next = function (form) {
            if (form.$valid) {
                if ($scope.unsolvedCall.addressId.neighborhoodId != null) {
                    UnsolvedCallService.setUnsolvedCall($scope.unsolvedCall);
                    //console.log($scope.unsolvedCall);
                    if ($state.current.name == "menu.registrarChamado5") {
                        $state.go('menu.registrarChamado6');
                    } else {
                        $state.go('registrarChamado6');
                    }
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
        $scope.setBairro = function () {
            if ($state.current.name == "menu.registrarChamado5") {
                $state.go('menu.bairros2');
            } else {
                $state.go('bairros2');
            }
        }

        /*Galeria de Imagens*/
        $scope.buttonHelp = function () {
            $ionicPopup.alert({
                title: 'Informações',
                template: 'Aqui você nos informa o endereço referente ao seu chamado. É muito importante que os dados informados sejam precisos, pois estes servirão para direcionar os agentes da prefeitura no local, se for o caso. Lembre-se de informar o complemento (se houver), de forma a nos ajudar a agilizar o atendimento do seu chamado.',
                okType: 'button-zem'
            });
        }
        $scope.showImages = function (index) {
            $scope.activeSlide = index;
        }
        $scope.showModal = function (templateUrl) {
            $ionicModal.fromTemplateUrl(templateUrl, {
                scope: $scope,
                animation: 'slide-in-up'
            }).then(function (modal) {
                $scope.modal = modal;
                $scope.modal.show();
            });
        }
        // Close the modal
        $scope.closeModal = function () {
            $scope.modal.hide();
            $scope.modal.remove();
        };
    });
