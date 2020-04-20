   angular.module('app.services')
       .service('SolvedCallService', function() {

           //for the purpose of this example I will store user data on ionic local storage but you should save it on a database
           var solvedCall;
           var setSolvedCall = function(call) {
               SolvedCall = call;
               //console.log("setUser Service: LocalStorage FacebookUser" + window.localStorage.starter_facebook_user);
           };

           var getSolvedCall = function() {
               //console.log("getUser Service: LocalStorage FacebookUser" + window.localStorage.starter_facebook_user);
               return solvedCall;
           };

           return {
               getSolvedCall: getSolvedCall,
               setSolvedCall: setSolvedCall
           };
       });
