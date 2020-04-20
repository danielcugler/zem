angular.module('app.controllers')

.controller('registrarChamado3Ctrl', function($scope, UnsolvedCallService, $window, $location, MobileFactory, $state) {
    MobileFactory.getEntities().then(function(data) {
        $scope.entities = data.data;
    });
    $scope.unsolvedCall = UnsolvedCallService.getUnsolvedCall();
    $scope.next = function(entity, entityCategory) {
        $scope.unsolvedCall.entityEntityCategoryMaps = {'entityEntityCategoryMapsPK':{ 'entityId': entity.entityId, 'entityCategoryId': entityCategory.entityCategoryId },'entity': entity, 'entityCategory': entityCategory};
        UnsolvedCallService.setUnsolvedCall($scope.unsolvedCall);
        //$window.location.href = "#/chamado4";
        $state.go('registrarChamado4');
    }


    $scope.toggleGroup = function(entity) {
        if ($scope.isGroupShown(entity)) {
            $scope.shownGroup = null;
        } else {
            $scope.shownGroup = entity;
        }
    };
    $scope.isGroupShown = function(entity) {
        return $scope.shownGroup === entity;
    };

});
