angular.module('app.services')

.factory('CitizenService', function($rootScope) {
      var citizen = {};
 
    return {
        getCitizen: function () {
            return citizen;
        },
        setCitizen: function (newCitizen) {
            citizen = newCitizen;
        }
    };
})