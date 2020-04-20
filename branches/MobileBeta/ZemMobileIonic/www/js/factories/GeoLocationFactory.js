angular.module('app.factories')
.factory('GeoLocationFactory',function($q, $cordovaGeolocation) {
	// Variable, etc.
	var factory = {};
	var deferred = $q.defer();
	var position = {};
	var posOptions = {timeout: 10000, enableHighAccuracy: false};
  // Ermittlung der Smartphone Position
  factory.getCurrentPosition = function() {
    $cordovaGeolocation
      .getCurrentPosition(posOptions)
      .then(function(position) {
      	deferred.resolve(position);
      }, function(err) {
        // error
      });
      return deferred.promise;
  }
	return factory;
});