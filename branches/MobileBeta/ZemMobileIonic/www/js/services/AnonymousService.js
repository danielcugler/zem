angular.module('app.services')

.factory('AnonymousService', function($rootScope) {
      var anonymous = 0;
 
    return {
        getAnonymous: function () {
            return anonymous;
        },
        setAnonymous: function (newAnonymous) {
            anonymous = newAnonymous;
        }
    };
})