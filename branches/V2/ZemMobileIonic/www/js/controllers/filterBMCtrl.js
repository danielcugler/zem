angular.module('app.controllers')
    .controller('filterBMCtrl', function ($scope, MobileFactory, BroadcastMessageService, $state, $ionicLoading, $ionicPopup, CitizenService, $ionicModal) {
        $scope.$on('$ionicView.enter', function () {
            $scope.cardNoBM = true;
        });
        $scope.max = false;
        $scope.moreDataCanBeLoaded = function () {
            console.log('max');
            return !max;
        };
        $scope.citizen = CitizenService.getCitizen();
        $scope.bmList = [];
        $scope.readBM = [];
        MobileFactory.getReadBM($scope.citizen.citizenCpf).then(function (resp) {
            console.log("GetReadBm");
            console.log(resp);
            $scope.readBM = resp;
        });
        $scope.isRead = function (bmid) {
            return $scope.readBM.indexOf(bmid) == -1;
        }
        var page = 1;
        $scope.loadMore = function () {
            if ($scope.max != true)
                //-1 ambos os chamados
                // 0 normal
                // 1 emergencial
                var broadcastConfig = window.localStorage.getItem("broadcastConfig");
            if (broadcastConfig == null) {
                broadcastConfig = -1;
            }
            //console.log("broadcastConfig");
            //console.log(broadcastConfig);
            MobileFactory.getBM2(page, broadcastConfig).then(function (data) {
                //console.log(data);
                if (data.length > 0) {
                    console.log(data);
                    $scope.bmList = $scope.bmList.concat(data);
                    $scope.$broadcast('scroll.infiniteScrollComplete');
                } else {
                    $scope.max = true;
                    $scope.$broadcast('scroll.infiniteScrollComplete');
                    if($scope.bmList.length != 0){
                        $scope.cardNoBM = true;
                    }else{
                        $scope.cardNoBM = false;
                    }
                }
            }, function (err) {
                //console.log('noResults');
                $scope.max = true;
                // called asynchronously if an error occurs
                // or server returns response with an error status.
            });
            page++;
        };
        $scope.$on('$stateChangeSuccess', function () {
            $scope.loadMore();
        });
        $scope.next = function (broadcastMessage) {
            console.log($scope.citizen.citizenCpf);
            //Verificar se o chamado não estiver lido, marcar como lido
            if ($scope.readBM.indexOf(broadcastMessage.broadcastMessageId) == -1) {
                MobileFactory.setReadBM($scope.citizen.citizenCpf, broadcastMessage.broadcastMessageId).then(function (resp) {
                    $scope.readBM.push(broadcastMessage.broadcastMessageId);
                    BroadcastMessageService.setBroadcastMessage(broadcastMessage);
                    $state.go("menu.messages");
                });
            } else {
                BroadcastMessageService.setBroadcastMessage(broadcastMessage);
                $state.go("menu.messages");
            }
        }
        $scope.update = function () {
            $scope.bmList = [];
            max = false;
            page = 1;
            $scope.loadMore();
        }

        /* Exibindo o tutorial*/
        $scope.buttonHelp = function () {
            $ionicPopup.alert({
                title: 'Informações',
                template: 'Aqui você encontra todos os comunicados importantes enviados pela prefeitura. De fato, esse é o canal que a prefeitura tem para se comunicar com você de forma rápida e eficiente. Lembre-se, dê uma atenção especial para os comunicados destacados com um ponto de exclamação vermelho, pois esses possuem caráter emergencial.',
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
