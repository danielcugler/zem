   angular.module('app.services')
       .service('UnsolvedCallService', function() {

           //for the purpose of this example I will store user data on ionic local storage but you should save it on a database
           var unsolvedCall;
           var setUnsolvedCall = function(call) {
               unsolvedCall = call;
               //console.log("setUser Service: LocalStorage FacebookUser" + window.localStorage.starter_facebook_user);
           };

           var getUnsolvedCall = function() {
               //console.log("getUser Service: LocalStorage FacebookUser" + window.localStorage.starter_facebook_user);
               return unsolvedCall;
           };

           return {
               getUnsolvedCall: getUnsolvedCall,
               setUnsolvedCall: setUnsolvedCall
           };
       });
