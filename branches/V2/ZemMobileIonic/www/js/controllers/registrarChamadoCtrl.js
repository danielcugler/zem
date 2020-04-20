angular.module('app.controllers')

.controller('registrarChamadoCtrl', function($scope, UnsolvedCallService, $window, $location, MobileFactory, CitizenService, UserService, AnonymousService, $ionicModal,  $ionicLoading, $ionicPopup, $state) {
    //iniciar animação de loading
    $ionicLoading.show({
        content: 'Loading',
        animation: 'fade-in',
        showBackdrop: true,
        maxWidth: 200,
        showDelay: 0
    });

    $scope.callClassifications = [];

    MobileFactory.getCallClassification().then(function(data) {
        $scope.callClassifications = data.data;
        //função de hide loading
        $ionicLoading.hide();
    }, function(error) {
        //console.log(error);
        //função de hide loading
        $ionicPopup.alert({
            title: 'Erro',
            template: 'Não foi possível conectar ao servidor. Tente novamente mais tarde.',
            okType: 'button-zem',
        });
        $ionicLoading.hide();
    });

    $scope.unsolvedCall = {
        callClassificationId: null,
        'description': {
            'information': null
        },
        'entityEntityCategoryMaps': null,
        'addressId': {
            'neighborhoodId': null
        },
        'callStatus': 0,
        'anonymity': 1,
        'mediasPath': null,
        'citizenCpf': null
    };

    $scope.next = function(callClassification) {
        $scope.unsolvedCall.callClassificationId = callClassification;
        UnsolvedCallService.setUnsolvedCall($scope.unsolvedCall);
        var user = UserService.getUser().userID;
        var token = window.localStorage.getItem("token");
        if (user == undefined && token == null) {
            if($state.current.name == "menu.registrarChamado"){
                $state.go('menu.registrarChamado3');
            }else{
                $state.go('registrarChamado3');
            }
        } else {
            AnonymousService.setAnonymous(1);
            if($state.current.name == "menu.registrarChamado"){
                $state.go('menu.registrarChamado2');
            }else{
                $state.go('registrarChamado2');
            }
        }
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

    $scope.teste = function(){
        console.log($state.current.name);
    }
});
