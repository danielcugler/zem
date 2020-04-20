angular.module('app.controllers')

    .controller('mainPageCtrl', function ($scope, $state, $http, $ionicPopup, $ionicLoading, IpService, $ionicHistory, $ionicModal, $ionicSlideBoxDelegate, MobileFactory, CitizenService) {
        var url = IpService.getIp();
        $scope.readBM;
        $scope.notifyBM = false;
        $scope.notifyBMNumber = 0;

        $scope.cityName = window.localStorage.getItem("cityName");

        $scope.$on('$ionicView.enter', function () {
            //iniciar animação de loading
            $ionicLoading.show({
                content: 'Loading',
                animation: 'fade-in',
                showBackdrop: true,
                maxWidth: 200,
                showDelay: 0
            });
            //verificando se há mensagens lidas
            $scope.citizen = CitizenService.getCitizen();

            //função de exibição dos numero de mensagens
            function verificandoOsChamados(cpf) {
                //verificando os chamados
                MobileFactory.getAllBM().then(function (data) {
                    console.log(data);
                    MobileFactory.getReadBM(cpf).then(function (resp) {
                        console.log("GetReadBm");
                        console.log(resp);
                        $scope.readBM = resp;
                        for (var ii = 0; ii < data.length; ii++) {
                            var item = data[ii];
                            if (resp.indexOf(item.broadcastMessageId) == -1) {
                                $scope.notifyBM = true;
                                $scope.notifyBMNumber++;
                            }
                        }
                        $ionicLoading.hide();
                    }, function (error) {
                        console.log(errod);
                        $ionicLoading.hide();
                    });
                }, function (err) {
                    $ionicLoading.hide();
                });
            }


            //Notificacoes
            //verificando se e a primeira vez que a pessoa instalou o app e logou
            var firebase = window.localStorage.getItem("firebase");
            //verifica se o tutorial e igual first
            console.log('notificacao login ctrl');
            if (firebase !== "first") {
                var registrationId = window.localStorage.getItem('registrationId');
                var firebase = {
                    'token': registrationId,
                    'platform': ionic.Platform.platform()
                }
                MobileFactory.saveFcmId(firebase).then(function (data) {
                    console.log('registrationId saved')
                    window.localStorage.setItem("firebase", "first");
                }).catch(function (callback) {
                    console.log(callback);
                });
            }
            //Notificacoes

            //pegando o cpf que sera utilizado no BroadCast Mensseger
            var token = window.localStorage.getItem("token");
            if (token != null) {
                MobileFactory.getCitizent(token).then(function (data) {
                    console.log("Citizen Token")
                    console.log(data.data.cpf);
                    CitizenService.setCitizen({ 'citizenCpf': data.data.cpf });
                    verificandoOsChamados(data.data.cpf);
                });
            }
            var facebookLogin = window.localStorage.getItem("starter_facebook_user");
            if (facebookLogin != null) {
                var userId = JSON.parse(facebookLogin).userID;
                MobileFactory.getCitizenf(userId).then(function (data) {
                    CitizenService.setCitizen({ 'citizenCpf': data.data.cpf });
                    verificandoOsChamados(data.data.cpf);
                });
            }

            //limpando o cache assim que entrar na view
            limparCache();
            //verificando se e a primeira vez que a pessoa instalou o app e logou
            var firstTutorial = window.localStorage.getItem("firstTutorial");
            //verifica se o tutorial e igual v1
            if (firstTutorial != "v1") {
                $ionicModal.fromTemplateUrl('helpInicial.html', {
                    scope: $scope,
                    animation: 'slide-in-up'
                }).then(function (modal) {
                    $scope.modal = modal;
                    $scope.modal.show();
                });
            }
        });

        function limparCache() {
            //limpar os caches e historico
            $ionicHistory.clearCache();
            $ionicHistory.clearHistory();
        }

        $scope.chamados = function () {
            $state.go('menu.meusChamados');
        }

        $scope.messages = function () {
            $state.go('menu.filterBM');
        }

        $scope.callPage = function () {
            $state.go('menu.filter');
        }

        $scope.goAddCall = function () {
            $state.go('menu.registrarChamado');
            //$state.go('registrarChamado');
        }

        $scope.about = function () {
            //$state.go('menu.about');
            $state.go('menu.aboutAndHelp');
        }

        $scope.config = function () {
            $state.go('menu.configuration');
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
                                window.localStorage.removeItem("cityId");
                                window.localStorage.removeItem("cityName");
                                IpService.setIp("");
                                window.localStorage.removeItem("ipService");
                                //console.log("Token removido.");
                            }).error(function () {
                                //console.log("Tente novamente mais tarde");
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
        };

        /* Funções de exibir tutorial*/
        $scope.goBackHelp = function () {
            //criando um valor no localStorage, para que o tutorial n seja mais exibido
            //pode ser alterado a versão, se for alterado alguma coisa critica, e deseja mostra ao usuario o tutorial novamente sem ele ter que desinstalar o app
            window.localStorage.setItem("firstTutorial", "v1");
            $scope.modal.hide();
            $scope.modal.remove();
        };
        $scope.next = function () {
            $ionicSlideBoxDelegate.next();
        };
        $scope.previous = function () {
            $ionicSlideBoxDelegate.previous();
        };

        // Called each time the slide changes
        $scope.slideChanged = function (index) {
            $scope.slideIndex = index;
        };
    })
