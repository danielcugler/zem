angular.module('app.controllers')

.controller('respostaCtrl', function($scope, $ionicPopup, $ionicLoading, UserService, UnsolvedCallService, SolvedCallService, MobileFactory) {
    $scope.unsolvedCall = UnsolvedCallService.getUnsolvedCall();
    console.log($scope.unsolvedCall);
    //$scope.solvedCall = SolvedCallService.getSolvedCall();
    $scope.token = window.localStorage.getItem("token");
    if ($scope.token == null) {
        $scope.token = UserService.getUser();
    }

    if($scope.unsolvedCall.qualification == null){        
        var valueRating = 1;
    } else {
        var valueRating = $scope.unsolvedCall.qualification;
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
        callback: function(rating, index) { //Mandatory
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
})
