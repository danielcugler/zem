angular.module('app.controllers')

.controller('detalhamentoChamadoCtrl', function($scope, UnsolvedCallService, MobileFactory, $state, $ionicHistory) {

    $scope.unsolvedCall = UnsolvedCallService.getUnsolvedCall();
    $scope.photos = [];
    MobileFactory.getMedia($scope.unsolvedCall.unsolvedCallId).then(function(data) {
        $scope.photos = data.data.photos;
    });

    //console.log($scope.unsolvedCall);
    //alert("ok");

    $scope.returnMenu = function() {
        //limpar o cache e bloquear o back
        $ionicHistory.clearCache();
        $ionicHistory.nextViewOptions({
            disableBack: true
        });
        $state.go('mainPage');
    }

})
