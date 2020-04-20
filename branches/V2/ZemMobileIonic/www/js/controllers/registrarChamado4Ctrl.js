angular.module('app.controllers')

    .controller('registrarChamado4Ctrl', function ($scope, $cordovaGeolocation, $state, $window, $ionicPlatform, MobileFactory, UnsolvedCallService, NeighborhoodService, $ionicPopup, $ionicLoading, $ionicModal) {
        $scope.unsolvedCall = UnsolvedCallService.getUnsolvedCall();
        $ionicPlatform.ready(function () {
            function showLocation(position) {
                var latitude = position.coords.latitude;
                var longitude = position.coords.longitude;
                MobileFactory.getByCoordinates(latitude, longitude).then(function (data) {
                    //console.log(data);
                    $scope.address = data.data.results[0];
                    //console.log("Exibindo o tamanho do array");
                    //console.log($scope.address.address_components);
                    //console.log($scope.address.address_components.length);
                    //variavel temporaria para pegar o nome do bairro
                    var tempNeighborhoodName;
                    if ($scope.address.address_components.length == 8) {
                        //console.log("Endereço com numero");
                        $scope.unsolvedCall.addressId.streetName = $scope.address.address_components[1].long_name;
                        $scope.unsolvedCall.addressId.addressNumber = parseInt($scope.address.address_components[0].long_name);
                        tempNeighborhoodName = $scope.address.address_components[2].long_name;
                    } else {
                        //console.log("Endereço sem numero");
                        $scope.unsolvedCall.addressId.streetName = $scope.address.address_components[0].long_name;
                        $scope.unsolvedCall.addressId.addressNumber = "";
                        tempNeighborhoodName = $scope.address.address_components[1].long_name;
                    }
                    //$scope.unsolvedCall.addressId.streetName = $scope.address.address_components[0].long_name;
                    //console.log($scope.unsolvedCall.addressId.streetName);
                    $scope.unsolvedCall.addressId.geograficalCoordinates = latitude + "," + longitude;

                    //pegando a cidade a qual a pessoa selecionou na primeira tela do Mobile
                    var cityId = localStorage.getItem("cityId");

                    //manda o nome do bairro para retornar o id do bairro
                    MobileFactory.getNeighborhoodByNameByCityId(tempNeighborhoodName, cityId).then(function (data2) {
                        $ionicLoading.hide();
                        if (data2.data != null) {
                            $scope.unsolvedCall.addressId.neighborhoodId = data2.data.neighborhoodId;
                            //função de hide
                            $ionicLoading.hide();
                            //popup para verificar o endereço.
                            var confirmPopup = $ionicPopup.confirm({
                                title: 'Confirmação de endereço',
                                template: 'O endereço está correto?<br> ' + $scope.unsolvedCall.addressId.streetName + '\nnº. ' + $scope.unsolvedCall.addressId.addressNumber + ', bairro ' + tempNeighborhoodName,
                                okType: 'button-zem',
                                okText: 'Sim',
                                cancelText: 'Não'
                            });
                            confirmPopup.then(function (res) {
                                if (res) {
                                    //adicionando o endereço se tiver correto e passando para a proxima tela.
                                    $scope.unsolvedCall.addressId.neighborhoodId = data2.data.neighborhoodId;
                                    if ($state.current.name == "menu.registrarChamado4") {
                                        $state.go('menu.registrarChamado6');
                                    } else {
                                        $state.go('registrarChamado6');
                                    }
                                } else {
                                    //adiciona o endereço e manda para a tela de edição manual.
                                    if (data2.data.length == 0) {
                                        if ($state.current.name == "menu.registrarChamado4") {
                                            $state.go('menu.registrarChamado5');
                                        } else {
                                            $state.go('registrarChamado5');
                                        }
                                    } else {
                                        $scope.unsolvedCall.addressId.neighborhoodId = data2.data.neighborhoodId;
                                        $scope.unsolvedCall.addressId.addressNumber = parseInt($scope.unsolvedCall.addressId.addressNumber);
                                        //console.log($scope.unsolvedCall.addressId.addressNumber);
                                        NeighborhoodService.setNeighborhood(tempNeighborhoodName);
                                        if ($state.current.name == "menu.registrarChamado4") {
                                            $state.go('menu.registrarChamado5');
                                        } else {
                                            $state.go('registrarChamado5');
                                        }
                                    }
                                }
                            });
                        } else {
                            $ionicPopup.alert({
                                title: 'Erro',
                                template: 'Não foi possível obter sua localização.',
                                okType: 'button-assertive'
                            });
                        }
                    }).catch(function (callback) {
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
            $scope.noAdressView = function () {
                if ($scope.unsolvedCall.callClassificationId.addressRequired == true) {
                    return false;
                } else {
                    return true;
                }
            }
            $scope.otherAdress = function () {
                if ($state.current.name == "menu.registrarChamado4") {
                    $state.go('menu.registrarChamado5');
                } else {
                    $state.go('registrarChamado5');
                }
            }
            $scope.noAdress = function () {
                if ($state.current.name == "menu.registrarChamado4") {
                    $state.go('menu.registrarChamado6');
                } else {
                    $state.go('registrarChamado6');
                }
            }
            $scope.next = function () {
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
                        timeout: 10000,
                        enableHighAccuracy: false
                    };

                    /* Verificando a disponibilidade do GPS */
                    if (ionic.Platform.isIOS()) {
                        cordova.plugins.diagnostic.requestLocationAuthorization(function () {
                            console.log("Successfully requested location authorization always");
                        }, function (error) {
                            console.error(error);
                        }, "when in use");
                    }

                    cordova.plugins.diagnostic.isLocationEnabled(function (available) {
                        console.log(available);
                        console.log("Location is " + (available ? "available" : "not available"));
                        if (available) {
                            navigator.geolocation.getCurrentPosition(showLocation, errorHandler, options);
                        } else {
                            $ionicLoading.hide();
                            var alertPopupLocation = $ionicPopup.confirm({
                                title: 'Erro',
                                template: 'O GPS está desativado. Mostrar configurações de localização?',
                                okType: 'button-zem'
                            });
                            alertPopupLocation.then(function (res) {
                                if (res) {
                                    console.log("Ir para configurações");
                                    if (ionic.Platform.isIOS()) {
                                        cordova.plugins.diagnostic.switchToSettings();
                                    } else {
                                        cordova.plugins.diagnostic.switchToLocationSettings();
                                    }
                                }
                            });
                        }
                    }, function (error) {
                        console.error("The following error occurred: " + error);
                        $ionicLoading.hide();
                        $ionicPopup.alert({
                            title: 'Erro',
                            template: 'Não foi possível obter sua localização.',
                            okType: 'button-assertive'
                        });
                    });
                }
            }
        });

        /*Galeria de Imagens*/
        $scope.buttonHelp = function () {
            $ionicPopup.alert({
                title: 'Informações',
                template: 'Aqui você precisa nos dizer se o endereço é ou não uma informação relevante para o atendimento do seu chamado. Se não for, selecione a terceira opção. Se o endereço for importante, você pode optar por nos informá-lo pelo GPS do seu celular, através da primeira opção, ou digitá-lo manualmente, através da segunda opção.',
                okType: 'button-zem'
            });
        }
        $scope.showImages = function (index) {
            $scope.activeSlide = index;
        }
        $scope.showModal = function (templateUrl) {
            $ionicModal.fromTemplateUrl(templateUrl, {
                scope: $scope,
                animation: 'slide-in-up'
            }).then(function (modal) {
                $scope.modal = modal;
                $scope.modal.show();
            });
        }
        // Close the modal
        $scope.closeModal = function () {
            $scope.modal.hide();
            $scope.modal.remove();
        };
    });
