angular.module('app.controllers')

.controller('registrarChamado3MenuCtrl', function($scope, UnsolvedCallService, $window, $location, MobileFactory, $state) {
    MobileFactory.getEntities().then(function(data) {
        $scope.entities = data.data;
        console.log($scope.entities);
    });
    
    $scope.unsolvedCall = UnsolvedCallService.getUnsolvedCall();

    $scope.next = function(entity, entityCategory) {
        $scope.unsolvedCall.entityEntityCategoryMaps = {'entityEntityCategoryMapsPK':{ 'entityId': entity.entityId, 'entityCategoryId': entityCategory.entityCategoryId },'entity': entity, 'entityCategory': entityCategory};
        UnsolvedCallService.setUnsolvedCall($scope.unsolvedCall);
        //$window.location.href = "#/chamado4";
        $state.go('menu.registrarChamado4Menu');
    }

    $scope.toggleGroup = function(entity) {
        if ($scope.isGroupShown(entity)) {
            $scope.shownGroup = null;
        } else {
            $scope.shownGroup = entity;
        }
    };

    $scope.viewIcon = function(entity) {
        return entity.icon;
    };

    $scope.isGroupShown = function(entity) {
        return $scope.shownGroup === entity;
    };

});
