angular.module('app.services')

.factory('IpService', function($rootScope) {
    var ip = window.localStorage.getItem("ipService");
    //var ip = "http://192.168.1.107:8080/rest/mobile";
    //var ip = "http://www.souzem.com.br/rest/mobile";
    console.log(ip);
    return {
        getIp: function () {
            return ip;
        },
        setIp: function (newIp) {
            localStorage.setItem("ipService", newIp);
            ip = newIp;
        }
    };
})