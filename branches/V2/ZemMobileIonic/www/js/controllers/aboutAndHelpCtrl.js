angular.module('app.controllers')

.controller('aboutAndHelpCtrl', function($scope, $state) {
    $scope.about = function() {
        $state.go('menu.about');
    }

    $scope.help = function() {
        $state.go('menu.help');

    }
})
