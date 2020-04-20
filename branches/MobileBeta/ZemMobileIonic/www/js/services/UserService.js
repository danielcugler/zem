angular.module('app.services')

.service('UserService', function() {
    //for the purpose of this example I will store user data on ionic local storage but you should save it on a database
    var anonymous = 0;

    var getAnonymous = function() {
        return anonymous;
    };
    var setAnonymous = function(newAnonymous) {
        anonymous = newAnonymous;
    };

    var getToken = function() {
        window.localStorage.getItem("token");
    }

    var setUser = function(user_data) {
        window.localStorage.starter_facebook_user = JSON.stringify(user_data);
        //console.log("setUser Service: LocalStorage FacebookUser" + window.localStorage.starter_facebook_user);
    };

    var getUser = function() {
        //console.log("getUser Service: LocalStorage FacebookUser" + window.localStorage.starter_facebook_user);
        return JSON.parse(window.localStorage.starter_facebook_user || '{}');
    };

    return {
        'getAnonymous' : getAnonymous,
        'setAnonymous' : setAnonymous,
        'getToken': getToken,
        'getUser': getUser,
        'setUser': setUser
    };
});
