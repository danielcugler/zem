angular.module('app.controllers')

//Início controller ZemZeladoriaMunicipalCtrl
.controller('zEMZeladoriaMunicipalCtrl', function($scope, $state, $q, UserService, $ionicLoading, $http, IpService, $ionicPopup, $ionicHistory, $ionicLoading, $cordovaSplashscreen, MobileFactory, CitizenService) {
    //iniciar animação de loading
    $ionicLoading.show({
        content: 'Loading',
        animation: 'fade-in',
        showBackdrop: true,
        maxWidth: 200,
        showDelay: 0
    });

    var url = IpService.getIp();

    $scope.$on('$ionicView.enter', function() {
        verifyToken(); //chamar função que verifica se existe algum tipo de token.
    });

    //Início login facebook
    var fbLoginSuccess = function(response) {

        if (!response.authResponse) {
            fbLoginError("Não é possível encontrar authResponse");
            return;
        }

        var authResponse = response.authResponse;

        getFacebookProfileInfo(authResponse)
            .then(function(profileInfo) {
                //for the purpose of this example I will store user data on local storage
                console.log("Facebook ID do usuário que acabou de logar:" + profileInfo.id);

                UserService.setUser({
                    authResponse: authResponse,
                    userID: profileInfo.id,
                    name: profileInfo.name,
                    email: profileInfo.email,
                    picture: "http://graph.facebook.com/" + authResponse.userID + "/picture?type=large"
                });

                var user = UserService.getUser();

                $ionicLoading.hide();

                console.log("fbLoginSuccess");
                //função que verifica se o facebookID já está cadastrado no BD.
                verificaFacebookID(user.userID);

            }, function(fail) {
                //fail get profile info
                console.log('profile info fail', fail);
            });
    };

    //Esta função é chamada quando ocorre erro no login pelo Facebook
    var fbLoginError = function(error) {
        console.log('fbLoginError', error);
        $ionicLoading.hide();
        var $errorAlertPopUp = $ionicPopup.alert({
            title: 'Erro',
            okType: 'button-assertive',
            template: 'Ops... Algum erro aconteceu.\nVerifique sua conexão e tente novamente mais tarde.'
        });
    };

    //Este método permite obter as informações do usuário a partir do perfil do Facebook
    var getFacebookProfileInfo = function(authResponse) {
        var info = $q.defer();

        //console.log("Access Token: " + authResponse.accessToken);

        facebookConnectPlugin.api('/me?fields=email,name&access_token=' + authResponse.accessToken, null,
            function(response) {
                console.log(response);
                info.resolve(response);
            },
            function(response) {
                console.log(response);
                info.reject(response);
            }
        );
        return info.promise;
    };

    //Este método é executado quando o usuário clica no botão "Login com facebook" na tela inicial.
    $scope.facebookSignIn = function() {
        facebookConnectPlugin.getLoginStatus(function(success) {
            if (success.status === 'connected') {
                // the user is logged in and has authenticated your app, and response.authResponse supplies
                // the user's ID, a valid access token, a signed request, and the time the access token
                // and signed request each expire
                console.log('getLoginStatus', success.status);

                //verifica se existe um usuário salvo.
                var user = UserService.getUser('facebook');
                console.log("USER (quando o status for connected" + user);

                if (!user.userID) //se não existe um usuário salvo(um userID)
                {
                    getFacebookProfileInfo(success.authResponse)
                        .then(function(profileInfo) {

                            console.log("Não possui userID");
                            //função que verifica se o facebookID já está cadastrado no BD.
                            verificaFacebookID(user.userID);

                            //armazenando os dados do usuário no LocalStorage
                            UserService.setUser({
                                authResponse: success.authResponse,
                                userID: profileInfo.id,
                                name: profileInfo.name,
                                email: profileInfo.email,
                                picture: "http://graph.facebook.com/" + success.authResponse.userID + "/picture?type=large"
                            });

                        }, function(fail) {
                            //Falha ao obter informações de perfil.
                            console.log('profile info fail', fail);
                        });
                } else {
                    $state.go('mainPage');
                }
                console.log("userID: " + user.userID);
            } else {
                //if (success.status === 'not_authorized') the user is logged in to Facebook, but has not authenticated your app
                //else The person is not logged into Facebook, so we're not sure if they are logged into this app or not.
                console.log('getLoginStatus', success.status);

                $ionicLoading.show({
                    template: 'Logando...'
                });

                //ask the permissions you need. You can learn more about FB permissions here: https://developers.facebook.com/docs/facebook-login/permissions/v2.4
                facebookConnectPlugin.login(['email', 'public_profile'], fbLoginSuccess, fbLoginError);
            }
        });
    };

    var verificaFacebookID = function(facebookId) {
        //chamada ajax para verificar se o usuário do facebook já se conectou outra vez.
        $http.get(url + "/citizenf/" + facebookId).then(
            function(data) { //Success
                //           MobileFactory.getCitizenf(facebookId).then(function(resp) {
                //    CitizenService.setCitizen({ 'citizenCpf': resp.data });
                //  });
                console.log("VerificaFacebookID - Retorno da consulta de Facebook ID: " + JSON.stringify(data.data));
                //se o facebookID do usuário não estiver no BD, significa que ele nunca logou com o facebook
                //então é necessário pedir o CPF dele para ver se ele já possui conta ZEM ou não.
                if (data.data.code == "0") {
                    console.log("Usuário completamente novo");
                    $state.go('inserirCPF'); //vai pra página de inserção de CPF.
                } else { //se o usuário já possui facebookID no banco é pq também já possui CPF então basta enviá-lo para a tela principal.
                    console.log("Este usuário já existe.");
                    $state.go('mainPage');
                }
            },
            function(data) { //Error

            });
    };
    //fim login facebook.

    //chamar função de verificar autenticação ZEM, que encaminha diretamente pra tela inicial
    function verifyToken() {
        //limpar os caches e historico
        $ionicHistory.clearCache();
        $ionicHistory.clearHistory();

        //console.log("VerifyToken");
        var token = window.localStorage.getItem("token");
        MobileFactory.getCitizent(token).then(function(data) {
            CitizenService.setCitizen({ 'citizenCpf': data.data });
        });
        //console.log("Token:" + token);
        $http.get(url + "/token/" + token).then(
            function(data) { //Success
                if (data.data.code == "1") {
                    //console.log("O Token e valido");
                    //limpar o cache e bloquear o back
                    $ionicHistory.clearCache();
                    $ionicHistory.nextViewOptions({
                        disableBack: true
                    });
                    $ionicLoading.hide();
                    $state.go('mainPage');
                }
            },
            function(data) { //Error
                //console.log("O token ZEM é invalido.");
                verifyFacebookLogin(); //caso o token ZEM não seja valido, verifica o facebook
            });
    };

    function verifyFacebookLogin() {
        //console.log("verifyFacebookLogin");
        var facebookLogin = window.localStorage.getItem("starter_facebook_user");
        //     if(facebookLogin)
        //    var userId = JSON.parse(facebookLogin).userID;
        //   MobileFactory.getCitizenf(userId).then(function(data) {
        //      CitizenService.setCitizen({ 'citizenCpf': data.data.citizen_cpf });
        //  });
        //console.log("Facebook Login:" + facebookLogin);
        if (facebookLogin != null) {
            //console.log("O token Facebook é válido.");
            //limpar o cache e bloquear o back
            $ionicHistory.clearCache();
            $ionicHistory.nextViewOptions({
                disableBack: true
            });
            $ionicLoading.hide();
            $state.go('mainPage');
        } else {
            $ionicLoading.hide();
        }
    };

    $scope.showAlertAnonimo = function() {
        var alertPopup = $ionicPopup.alert({
            title: 'Atenção!',
            okType: 'button button-zem',
            template: 'Caso você registre um chamado anônimo, não será possível acompanhá-lo futuramente.\nDeseja continuar?',
            buttons: [{
                text: 'Não',
                type: 'button-light',
                onTap: function() {
                    return false;
                }
            }, {
                text: 'Sim',
                type: 'button-zem',
                onTap: function() {
                    $state.go('registrarChamado');
                    return true;
                }
            }]
        });
        //$state.go('registrarChamado');
    };

    $scope.teste = function() {
        $state.go('selectCity');
    }

});
