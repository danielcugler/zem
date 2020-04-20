angular.module('app.services')
    .service('NeighborhoodService', function () {
        //for the purpose of this example I will store user data on ionic local storage but you should save it on a database
        var neighborhood;
        var setNeighborhood = function (value) {
            neighborhood = value;
            //console.log("setUser Service: LocalStorage FacebookUser" + window.localStorage.starter_facebook_user);
        };

        var getNeighborhood = function () {
            //console.log("getUser Service: LocalStorage FacebookUser" + window.localStorage.starter_facebook_user);
            return neighborhood;
        };

        return {
            getNeighborhood: getNeighborhood,
            setNeighborhood: setNeighborhood
        };
    });