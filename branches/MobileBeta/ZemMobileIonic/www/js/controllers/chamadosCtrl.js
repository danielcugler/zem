angular.module('app.controllers')

.controller('chamadosCtrl', function($scope, UnsolvedCallService, MobileFactory, $state, $ionicModal) {
    $scope.unsolvedCall = UnsolvedCallService.getUnsolvedCall();
    $scope.photos = [];
    //console.log($scope.unsolvedCall);
    MobileFactory.getMedia($scope.unsolvedCall.parentCallId.unsolvedCallId).then(function(data) {
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

    $scope.verResposta = function() {
        $state.go('resposta');
    }
})
