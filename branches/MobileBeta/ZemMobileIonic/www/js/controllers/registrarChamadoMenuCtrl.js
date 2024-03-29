angular.module('app.controllers')

.controller('registrarChamadoMenuCtrl', function($scope, UnsolvedCallService, $window, $location, MobileFactory, CitizenService, UserService, AnonymousService, $state) {
    $scope.callClassifications = [];

    MobileFactory.getCallClassification().then(function(data) {
        $scope.callClassifications = data.data;
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
            //$window.location.href = "#/chamado3";
            $state.go('menu.registrarChamado3Menu');
        } else {
            AnonymousService.setAnonymous(1);
            //$window.location.href = "#/chamado2";
            $state.go('menu.registrarChamado2Menu');
        }
    }
});
