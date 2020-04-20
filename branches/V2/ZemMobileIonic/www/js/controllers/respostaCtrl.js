angular.module('app.controllers')

    .controller('respostaCtrl', function ($scope, $ionicPopup, $ionicLoading, UserService, UnsolvedCallService, SolvedCallService, MobileFactory, $ionicModal) {
        $scope.unsolvedCall = UnsolvedCallService.getUnsolvedCall();
        console.log($scope.unsolvedCall);

        if ($scope.unsolvedCall.qualification == null) {
            var valueRating = 0;
        } else {
            var valueRating = $scope.unsolvedCall.qualification;
        }
        
        $scope.token = window.localStorage.getItem("token");
        if ($scope.token == null) {
            $scope.token = UserService.getUser();
        }

        console.log($scope.token);
        //ratings
        $scope.ratingsObject = {
            iconOn: 'ion-ios-star', //Optional
            iconOff: 'ion-ios-star-outline', //Optional
            iconOnColor: 'rgb(255, 201, 0)', //Optional
            iconOffColor: 'rgb(255, 201, 0)', //Optional
            rating: valueRating, //Optional
            minRating: 1, //Optional
            //readOnly: true, //Optional
            callback: function (rating, index) { //Mandatory
                //$scope.ratingsCallback(rating, index);
                $ionicLoading.show({
                    content: 'Loading',
                    animation: 'fade-in',
                    showBackdrop: true,
                    maxWidth: 200,
                    showDelay: 0
                });
                console.log($scope.unsolvedCall.unsolvedCallId);
                MobileFactory.evaluation($scope.unsolvedCall.unsolvedCallId, rating, $scope.token).then(function successCallback(data) {
                    console.log("Success");
                    console.log(data);
                    $ionicLoading.hide();
                    $ionicPopup.alert({
                        title: 'Sucesso',
                        template: 'Obrigado por qualificar o seu chamado!',
                        okType: 'button-zem'
                    });
                    //Salvar o qualificação no unsolved
                    $scope.unsolvedCall.qualification = rating;
                    UnsolvedCallService.setUnsolvedCall($scope.unsolvedCall);
                }, function errorCallback(data) {
                    console.log("Error");
                    console.log(data);
                    $ionicLoading.hide();
                    $ionicPopup.alert({
                        title: 'Erro',
                        template: 'Não foi possível qualificar o seu chamado. Tente novamente!',
                        okType: 'button-assertive'
                    });
                });
            }
        };

        /*Galeria de Imagens*/
        $scope.buttonHelp = function () {
            $ionicPopup.alert({
                title: 'Informações',
                template: 'Aqui você tem acesso à resposta enviada pela prefeitura para o seu chamado. Veja que é possível avaliar a qualidade do atendimento do seu chamado. Marque uma estrela para avaliar um atendimento ruim e cinco estrelas para avaliar um atendimento excelente!',
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
    })
