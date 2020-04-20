angular.module('app.controllers')

    .controller('chamadosCtrl', function ($scope, UnsolvedCallService, MobileFactory, $state, $ionicModal, $ionicPopup) {
        $scope.unsolvedCall = UnsolvedCallService.getUnsolvedCall();
        $scope.photos = [];
        //console.log($scope.unsolvedCall);
        MobileFactory.getMedia($scope.unsolvedCall.parentCallId.unsolvedCallId).then(function (data) {
            //console.log("resposta getMedia");
            //console.log(data.data);
            $scope.photos = data.data;
            //console.log($scope.photos.length);
            if ($scope.photos.length === 0) {
                //console.log("não possui imagem");
                $scope.varShowImages = false; //esconder
            } else {
                //console.log("possui imagem");
                //console.log($scope)
                $scope.varShowImages = true; //mostrar
            }
        });

        //console.log("Array de photos");
        //console.log($scope.photos);

        $scope.verResposta = function () {
            $state.go('menu.resposta');
        }

        $scope.buttonHelp = function () {
            $ionicPopup.alert({
                title: 'Informações',
                template: 'Aqui você encontra um resumo referente ao seu chamado. Note que nós registramos a data de abertura do seu chamado, e saiba que iremos trabalhar para atendê-lo o mais rápido possível! Além disso, aqui você também consegue acompanhar o status de atendimento do seu chamado, desde a sua abertura (Status: Novo) até a sua conclusão (Status: Finalizado).',
                okType: 'button-zem'
            });
        }
    })
