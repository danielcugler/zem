   angular.module('app.services')
       .service('BroadcastMessageService', function() {

           //for the purpose of this example I will store user data on ionic local storage but you should save it on a database
           var broadcastMessage;
           var setBroadcastMessage = function(message) {
               broadcastMessage = message;
               //console.log("setUser Service: LocalStorage FacebookUser" + window.localStorage.starter_facebook_user);
           };

           var getBroadcastMessage = function() {
               //console.log("getUser Service: LocalStorage FacebookUser" + window.localStorage.starter_facebook_user);
               return broadcastMessage;
           };

           return {
               getBroadcastMessage: getBroadcastMessage,
               setBroadcastMessage: setBroadcastMessage
           };
       });
