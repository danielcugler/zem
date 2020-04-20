angular.module('app.services')
.service('AddressService', function(){
//for the purpose of this example I will store user data on ionic local storage but you should save it on a database
  var state;
  var city;
  var setStateId = function(estado) {
     state = estado;
  };
  var getStateId = function(){
    return state || null;
  };
    var setCityId = function(cidade) {
     city = cidade;
  };
  var getCityId = function(){
    return city || null;
  };
  return {
    'getStateId': getStateId,
    'setStateId': setStateId,
    'getCityId': getCityId,
    'setCityId': setCityId  
  };
});