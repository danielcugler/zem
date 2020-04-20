angular.module('app.controllers')

.controller('messagesCtrl', function($scope, BroadcastMessageService, $state, $ionicModal) {
    $scope.bm = BroadcastMessageService.getBroadcastMessage();
 
    /*$scope.next = function() {
        $state.go('filterBM');
    }*/
})