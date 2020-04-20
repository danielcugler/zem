angular.module('app.controllers')

    .controller('registrarChamado2Ctrl', function ($scope, $state, $ionicPopup, AnonymousService, $ionicModal) {
        function anCall() {
            AnonymousService.setAnonymous(0);
            if ($state.current.name == "menu.registrarChamado2") {
                $state.go('menu.registrarChamado3');
            } else {
                $state.go('registrarChamado3');
            }
        };

        $scope.next = function () {
            if ($state.current.name == "menu.registrarChamado2") {
                $state.go('menu.registrarChamado3');
            } else {
                $state.go('registrarChamado3');
            }
        }

        $scope.confirmAnonymous = function () {
            // A confirm dialog
            var confirmPopup = $ionicPopup.confirm({
                title: 'Atenção!',
                template: 'Caso você registre um chamado anônimo, não será possível acompanhá-lo futuramente. Deseja continuar?',
                cancelText: 'Cancelar', // String (default: 'Cancel'). The text of the Cancel button.
                okText: 'Confirmar', // String (default: 'OK'). The text of the OK button.
                okType: 'button-zem'
            });

            confirmPopup.then(function (res) {
                if (res) {
                    console.log('Confirmar');
                    anCall();
                } else {
                    console.log('Cancelar');
                }
            });
        };

        /* Popup Help */
        $scope.buttonHelp = function () {
            $ionicPopup.alert({
                title: 'Ajuda',
                template: '<pre>    Ao selecionar a opção "Eu mesmo" é possível acompanhar todas as etapas do chamado, e eles serão armazenados mesmo após sua conclusão.</pre><pre>    Se a opção "Anônimo" for escolhida, não será possível acompanhar o progresso do chamado ou visualizá-lo após sua conclusão.</pre>',
                okType: 'button-clear button-balanced'
            });
        }

        /*Galeria de Imagens*/
        $scope.buttonHelp = function () {
            $ionicPopup.alert({
                title: 'Informações',
                template: 'Aqui você nos diz se deseja ou não se identificar no registro do seu chamado. É importante que saiba que só conseguirá acompanhar o atendimento do seu chamado se optar pela primeira opção, de modo a tornar possível o nosso retorno para você. Mas fique tranquilo, caso opte pela segunda opção não manteremos nenhum vinculo seu com o chamado.',
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
