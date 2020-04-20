angular.module('app.controllers')

    .controller('registrarChamado3Ctrl', function ($scope, UnsolvedCallService, $window, $location, MobileFactory, $state, $ionicModal, $ionicLoading, $ionicPopup) {

        //iniciar animação de loading
        $ionicLoading.show({
            content: 'Loading',
            animation: 'fade-in',
            showBackdrop: true,
            maxWidth: 200,
            showDelay: 0
        });

        MobileFactory.getEntities().then(function (data) {
            //função de hide loading
            $ionicLoading.hide();
            $scope.entities = data.data;
            console.log($scope.entities);
        }, function (error) {
            //console.log(error);
            $ionicPopup.alert({
                title: 'Erro',
                template: 'Não foi possível conectar ao servidor. Tente novamente mais tarde.',
                okType: 'button-zem',
            });
            //função de hide loading
            $ionicLoading.hide();
        });

        $scope.unsolvedCall = UnsolvedCallService.getUnsolvedCall();

        $scope.next = function (entity, entityCategory) {
            $scope.unsolvedCall.entityEntityCategoryMaps = { 'entityEntityCategoryMapsPK': { 'entityId': entity.entityId, 'entityCategoryId': entityCategory.entityCategoryId }, 'entity': entity, 'entityCategory': entityCategory };
            UnsolvedCallService.setUnsolvedCall($scope.unsolvedCall);

            if ($state.current.name == "menu.registrarChamado3") {
                $state.go('menu.registrarChamado4');
            } else {
                $state.go('registrarChamado4');
            }
        }

        $scope.toggleGroup = function (entity) {
            if ($scope.isGroupShown(entity)) {
                $scope.shownGroup = null;
            } else {
                $scope.shownGroup = entity;
            }
        };

        $scope.viewIcon = function (entity) {
            return entity.icon;
        };

        $scope.isGroupShown = function (entity) {
            return $scope.shownGroup === entity;
        };

        /*Galeria de Imagens*/
        $scope.buttonHelp = function () {
            $ionicPopup.alert({
                title: 'Informações',
                template: 'Aqui você seleciona a categoria do seu chamado, que serve para direcioná-lo ao setor responsável na prefeitura. É muito importante que você analise as opções disponíveis, de forma a encaminhá-lo para a equipe adequada, e com isso agilizar o seu atendimento.',
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
