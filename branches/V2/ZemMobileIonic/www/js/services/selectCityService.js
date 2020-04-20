angular.module('app.services')

.factory('SelectCityService', function($rootScope) {
      var city = [];
 
    return {
        getCity: function () {
            return city;
        },
        setCity: function (newCity) {
            city = newCity;
        }
    };
})