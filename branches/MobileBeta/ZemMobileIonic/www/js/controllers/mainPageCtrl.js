angular.module('app.controllers')

.controller('mainPageCtrl', function($scope, $state, $http, $ionicPopup, $ionicLoading, IpService, $ionicHistory) {
    var url = IpService.getIp();

    $scope.$on('$ionicView.enter', function() {
        //limpando o cache assim que entrar na view
        limparCache();
    });

    function limparCache() {
        //limpar os caches e historico
        $ionicHistory.clearCache();
        $ionicHistory.clearHistory();
    }

    $scope.chamados = function(){
        $state.go('menu.meusChamados');
    }

    $scope.messages = function(){
        $state.go('menu.filterBM');
    }

    $scope.callPage = function() {
        $state.go('menu.filter');
    }

    $scope.goAddCall = function() {
        $state.go('menu.registrarChamadoMenu');
        //$state.go('registrarChamado');
    }

    $scope.about = function() {
        $state.go('menu.about');
    }

    $scope.config = function() {
        $state.go('menu.configuration');
    }

    $scope.logout = function() {
        var confirmPopup = $ionicPopup.confirm({
            title: 'Sair',
            template: 'Tem certeza de que quer sair do aplicativo?',
            cancelText: 'Não',
            cancelType: 'button-light',
            okText: 'Sim',
            okType: 'button-assertive'
        });

        confirmPopup.then(function(res) {
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
                        }).success(function() {
                            window.localStorage.removeItem("token");
                            console.log("Token removido.");
                        }).error(function() {
                            console.log("Tente novamente mais tarde");
                        });
                    }

                    if (facebookLogin != null) {
                        //facebook logout
                        facebookConnectPlugin.logout(function() {
                                window.localStorage.removeItem('starter_facebook_user');
                                console.log("Logout no Facebook efetuado.");
                                $ionicLoading.hide();
                            },
                            function(fail) {
                                $ionicLoading.hide();
                            });
                    }
                    //limpar o cache e bloquear o back
                    $ionicHistory.clearCache();
                    $ionicHistory.nextViewOptions({
                        disableBack: true
                    });
                    window.localStorage.removeItem('ipService');
                    $state.go('selectCity');
                }
            }
        });
    };
})
