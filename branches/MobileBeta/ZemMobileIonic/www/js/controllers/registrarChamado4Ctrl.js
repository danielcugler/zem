angular.module('app.controllers')

.controller('registrarChamado4Ctrl', function($scope, $cordovaGeolocation, $state, $window, $ionicPlatform, MobileFactory, UnsolvedCallService, $ionicPopup, $ionicLoading) {
    $scope.unsolvedCall = UnsolvedCallService.getUnsolvedCall();
    $ionicPlatform.ready(function() {
        function showLocation(position) {
            var latitude = position.coords.latitude;
            var longitude = position.coords.longitude;
            MobileFactory.getByCoordinates(latitude, longitude).then(function(data) {
                $scope.address = data.data.results[0];
                $scope.unsolvedCall.addressId.streetName = $scope.address.address_components[0].long_name;
                console.log($scope.unsolvedCall.addressId.streetName);
                $scope.unsolvedCall.addressId.geograficalCoordinates = latitude + "," + longitude;
                MobileFactory.getNeighborhoodByName($scope.address.address_components[1].long_name).then(function(data) {
                    //console.log("MobileFactory2");
                    //console.log(data.data[0].neighborhoodId);
                    $scope.unsolvedCall.addressId.neighborhoodId = data.data[0].neighborhoodId;
                    //console.log("MobileFactory3");
                    //console.log($scope.unsolvedCall.addressId.neighborhoodId);
                    //função de hide
                    $ionicLoading.hide();
                    $state.go("registrarChamado6");
                }).catch(function(callback) {
                    $scope.unsolvedCall.addressId.streetName = "";
                    $scope.unsolvedCall.addressId.addressNumber = "";
                    $scope.unsolvedCall.addressId.complement = "";
                    console.log($scope.unsolvedCall.addressId);
                    //console.log(callback);
                    $ionicLoading.hide();
                    $ionicPopup.alert({
                        title: 'Erro',
                        template: 'Não foi possível obter sua localização.',
                        okType: 'button-assertive'
                    });
                });
                // alert($scope.address.address_components[1].long_name);
            });
        }

        function errorHandler(err) {
            //função de hide loading
            $ionicLoading.hide();
            if (err.code == 1) {
                //console.log("Error: Access is denied!");
                $scope.unsolvedCall.addressId.streetName = "";
                $scope.unsolvedCall.addressId.addressNumber = "";
                $scope.unsolvedCall.addressId.complement = "";
                console.log($scope.unsolvedCall.addressId);
                var alertPopup = $ionicPopup.alert({
                    title: 'Erro',
                    template: 'Não foi possível acessar o GPS. O aplicativo não tem permissão para acessar sua localização. Ative as permissões e tente novamente.',
                    okType: 'button-assertive'
                });
            } else {
                //console.log("Error: GPS disable!");
                $scope.unsolvedCall.addressId.streetName = "";
                $scope.unsolvedCall.addressId.addressNumber = "";
                $scope.unsolvedCall.addressId.complement = "";
                console.log($scope.unsolvedCall.addressId);
                var alertPopup = $ionicPopup.alert({
                    title: 'Erro',
                    template: 'Não foi possível acessar sua localização. Ative o GPS nas configurações e tente novamente.',
                    okType: 'button-assertive'
                });
            }
        }
        $scope.next = function() {
            //iniciar animação de loading
            $ionicLoading.show({
                content: 'Loading',
                animation: 'fade-in',
                showBackdrop: true,
                maxWidth: 200,
                showDelay: 0
            });
            //    alert("Click");
            if (navigator.geolocation) {
                //          alert("nav");
                // timeout at 60000 milliseconds (60 seconds)
                var options = {
                    timeout: 30000,
                    enableHighAccuracy: false
                };
                navigator.geolocation.getCurrentPosition(showLocation, errorHandler, options);
                /*
                MobileFactory.getByCoordinates('-18.955783','-46.9829797').then(function(data){
                    $scope.address = data.data.results[0];
                    alert($scope.address.address_components[0].long_name);   //Rua
                    alert($scope.address.address_components[1].long_name);  //Bairro
                    alert($scope.address.address_components[2].long_name);  //Cidade
                    alert($scope.address.address_components[4].short_name); //Estado
                    MobileFactory.getNeighborhoodByName($scope.address.address_components[1].long_name).then(function(data){
                        $scope.unsolvedCall.addressId.neighborhoodId=data.data;
                        $state.go("registrarChamado6");
                    });
                });*/
            } else {
                //alert("Sorry, browser does not support geolocation!");
            }
        }
    });
});
