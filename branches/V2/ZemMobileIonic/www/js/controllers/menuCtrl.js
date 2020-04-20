angular.module('app.controllers')
    .controller('menuCtrl', function ($scope, $state, $ionicPopup, $http, IpService, $ionicHistory, $ionicLoading, MobileFactory) {
        var url = IpService.getIp();

        $scope.$on("$ionicView.beforeEnter", function (event, viewData) {
            viewData.enableBack = true;
        });

        $scope.home = function () {
            $state.go('menu.mainPage');
        }
        $scope.registrarChamado = function () {
            $state.go('menu.registrarChamado');
        }
        $scope.meusChamados = function () {
            $state.go('menu.meusChamados');
        }
        $scope.minhasMensagens = function () {
            $state.go('menu.filterBM');
        }
        $scope.configuracao = function () {
            $state.go('menu.configuration')
        }
        $scope.about = function () {
            $state.go('menu.about');
        }
        $scope.help = function () {
            $state.go('menu.help');
        }
        $scope.logout = function () {
            var confirmPopup = $ionicPopup.confirm({
                title: 'Sair',
                template: 'Tem certeza de que quer sair do aplicativo?',
                cancelText: 'Não',
                cancelType: 'button-light',
                okText: 'Sim',
                okType: 'button-assertive'
            });

            confirmPopup.then(function (res) {
                if (res) {
                    var token = window.localStorage.getItem("token");
                    var facebookLogin = window.localStorage.getItem("starter_facebook_user");
                    console.log("Token: " + token + " - FacebookLogin: " + facebookLogin);

                    //função para apagar o token do banco e fazer logout no facebook.
                    if (token != null || facebookLogin != null) {
                        if (token != null) {
                            $http({
                                method: "DELETE",
                                url: url + "/token/",
                                params: {
                                    "token": token
                                },
                                headers: {
                                    'Content-Type': 'application/json'
                                },
                                responseType: 'json'
                            }).success(function () {
                                window.localStorage.removeItem("token");
                                console.log("Token removido.");
                            }).error(function () {
                                console.log("Tente novamente mais tarde");
                            });
                        }

                        if (facebookLogin != null) {
                            //facebook logout
                            facebookConnectPlugin.logout(function () {
                                window.localStorage.removeItem('starter_facebook_user');
                                console.log("Logout no Facebook efetuado.");
                                $ionicLoading.hide();
                            },
                                function (fail) {
                                    $ionicLoading.hide();
                                });
                        }
                        //limpar o cache e bloquear o back
                        $ionicHistory.clearCache();
                        $ionicHistory.nextViewOptions({
                            disableBack: true
                        });

                        var registrationId = window.localStorage.getItem('registrationId');
                        MobileFactory.rmFirebase(registrationId).then(function (data) {
                            window.localStorage.removeItem("firebase");
                        }).catch(function (callback) {
                            console.log(callback);
                        });
                        window.localStorage.removeItem('ipService');
                        $state.go('selectCity');
                    }
                }
            });
        }
    });
