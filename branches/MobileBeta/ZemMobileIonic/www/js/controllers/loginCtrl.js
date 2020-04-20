angular.module('app.controllers')

.controller('loginCtrl', function($scope, $http, $state, IpService, MobileFactory, CitizenService, $ionicPopup, $ionicHistory, $ionicLoading) {
    // var url = "http://192.168.1.109:8080/rest/mobile/";
    var url = IpService.getIp();
    $scope.login = function(user, form) {
        //iniciar animação de loading
        $ionicLoading.show({
            content: 'Loading',
            animation: 'fade-in',
            showBackdrop: true,
            maxWidth: 200,
            showDelay: 0
        });
        if (form.$valid) {
            $http.get(url + "/login/" + user.username + "/" + user.password).success(function(data) {
                user.password = '';
                if (data.message === "token") {
                    var token = data.code;
                    window.localStorage.setItem("token", token);
                    MobileFactory.getCitizent(token).then(function(data) {
                        CitizenService.setCitizen({ 'citizenCpf': data.data });
                    });
                    //função de hide loading
                    $ionicLoading.hide();
                    //limpar o cache e bloquear o back
                    $ionicHistory.clearCache();
                    $ionicHistory.nextViewOptions({
                        disableBack: true
                    });

                    $state.go('mainPage');
                }
            }).error(function() {
                //função de hide loading
                $ionicLoading.hide();
                console.log("Usuário ou senha inválidos");
                var alertPopup = $ionicPopup.alert({
                    title: 'Erro',
                    template: 'Não foi possível efetuar o login. Tente novamente mais tarde.',
                    okType: 'button-zem',
                });
            });
        } else {
            //função de hide loading
            $ionicLoading.hide();
            $scope.enviado = true;
        }
    };

    $scope.forgotPassword = function() {
        $scope.cpf = {};
        $ionicPopup.show({
            title: 'Esqueci minha senha',
            template: 'Digite seu CPF para a recuperação da senha\n<input type="tel" ng-model="cpf.cpfCitizen"/>',
            scope: $scope,
            buttons: [{
                text: 'Cancelar'
            }, {
                text: 'OK',
                type: 'button-zem',
                onTap: function() {
                    $ionicLoading.show({
                        content: 'Loading',
                        animation: 'fade-in',
                        showBackdrop: true,
                        maxWidth: 200,
                        showDelay: 0
                    });
                    $http.get(url + "/password/" + $scope.cpf.cpfCitizen)
                        .then(function(data) {
                            $ionicLoading.hide();
                            $ionicPopup.alert({
                                title: 'E-mail de confirmação',
                                template: data.data.code
                            });
                            console.log(data);
                        }, function(error) {
                            $ionicLoading.hide();
                            $ionicPopup.alert({
                                title: 'Erro',
                                template: "O CPF digitado não foi encontrado.",
                                okType: 'button-assertive'
                            });
                            console.log("Erro ao buscar cpf no banco.")
                            console.log(error);

                        });
                }
            }]
        });
    }

    //função que verifica se o usuario esta logado
    /*$scope.verifyToken = function() {
        console.log("OK");
        var token = window.localStorage.getItem("token");
        $http.get(url + "/token/" + token).success(function(data) {
            if (data.code === "1") {
                console.log("O Token e valido");
                //limpar o cache e bloquear o back
                $ionicHistory.clearCache();
                $ionicHistory.nextViewOptions({
                    disableBack: true
                });
                $state.go('homeLogin');
            }
        }).error(function() {
            console.log("E necessario autenticar novamente");
        });
    };*/
})
