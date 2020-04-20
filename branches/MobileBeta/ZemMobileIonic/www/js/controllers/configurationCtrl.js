angular.module('app.controllers')

.controller('configurationCtrl', function($scope, MobileFactory, $ionicPopup) {

    $scope.$on('$ionicView.enter', function() {
        //quando entrar na view
        console.log("Entrou config");
        $scope.confSenha = false;
        $scope.confMsg = false;

        //verificação se já possui configuração no localStorage
        //-1 ambos os chamados
        // 0 normal
        // 1 emergencial

        //inicializando variaveis
        $scope.broadcastConfig = { emergency: { checked: false }, normal: { checked: false } };
        $scope.passwordDif = true;

        $scope.localStorageBroadcast = window.localStorage.getItem("broadcastConfig");
        console.log($scope.localStorageBroadcast);
        if ($scope.localStorageBroadcast != null) {
            if ($scope.localStorageBroadcast == -1) {

                $scope.broadcastConfig.normal.checked = true;
                $scope.broadcastConfig.emergency.checked = true;

            } else if ($scope.localStorageBroadcast == 0) {

                $scope.broadcastConfig.normal.checked = true;
                $scope.broadcastConfig.emergency.checked = false;

            } else if ($scope.localStorageBroadcast == 1) {

                $scope.broadcastConfig.normal.checked == false;
                $scope.broadcastConfig.emergency.checked = true;

            } else {
                console.log("BroadcastConfig não encontrado no localStorage");
            }
        } else {

            $scope.broadcastConfig.emergency.checked = true;
            $scope.broadcastConfig.normal.checked = true;
        }
    });

    /* Parte de ocultar os configs */
    $scope.exibirConfSenha = function() {
        if (window.localStorage.getItem("starter_facebook_user") != null) {
            console.log("Tem facebook login");
            $ionicPopup.alert({
                title: 'Atenção',
                okType: 'button-assertive',
                template: 'Não e possível alterar a senha quando se esta logado com o facebook.'
            });
        } else {
            console.log("Não tem facebook");
            $scope.confSenha = !$scope.confSenha;
            return $scope.confSenha;
        }
    }
    $scope.verifyFacebookToken = function() {
        if (window.localStorage.getItem("starter_facebook_user") != null) {
            console.log("Tem facebook login");
            return true;
        } else {
            console.log("Não tem facebook");
            return false;
        }
    }


    $scope.exibirConfMsg = function() {
        $scope.confMsg = !$scope.confMsg;
        return $scope.confMsg;
    }

    $scope.changePassword = function(form, user) {
        console.log("Entrou config");
        if (form.$valid && $scope.passwordDif == false) {
            //console.log("As senhas são iguais, e o formulario e valido");
            var token = window.localStorage.getItem("token");
            console.log(token);
            MobileFactory.changePassword(user.old, user.newPassword, token).then(function(data) {
                console.log("Sucesso ao alterar senha");
                console.log(data.data.token);
                window.localStorage.setItem("token", data.data.token);
                $ionicPopup.alert({
                    title: 'Sucesso',
                    okType: 'button-zem',
                    template: 'A senha foi alterada com sucesso.'
                });
                delete form;

            }, function(error) {
                console.log("Erro de alterar senha");
                console.log(error);
                $ionicPopup.alert({
                    title: 'Erro',
                    okType: 'button-assertive',
                    template: 'Não foi possivel alterar a senha.'
                });
            });
        } else {
            $scope.enviado = true;
        }
    }

    $scope.limpar = function(form) {
        console.log(form);
        delete form;
        //$scope.password = {};
    }

    //verificando se as senhas são iguais
    $scope.getPattern = function(newPwd, confirm) {
        if (newPwd == confirm) {
            //console.log("Senhas iguais")
            $scope.passwordDif = false;
        } else {
            //console.log("Senhas diferentes")
            $scope.passwordDif = true;
        }
    }

    $scope.changeBroadcastConfig = function(broadcast) {
        //window.localStorage.setItem("token", token);
        //-1 ambos os chamados
        // 0 normal
        // 1 emergencial
        if (broadcast.emergency.checked == true && broadcast.normal.checked == true) {
            //Todas
            window.localStorage.setItem("broadcastConfig", -1);
            //console.log("Ambos os broadcast");
            $ionicPopup.alert({
                title: 'Sucesso',
                okType: 'button-zem',
                template: 'A partir de agora você ira visualizar as mensagens normais e emergênciais.'
            });

        } else if (broadcast.emergency.checked == false && broadcast.normal.checked == true) {
            //Apenas normal
            window.localStorage.setItem("broadcastConfig", 0);
            //console.log("Apenas normal");
            $ionicPopup.alert({
                title: 'Sucesso',
                okType: 'button-zem',
                template: 'A partir de agora você ira visualizar apenas as mensagens normais.'
            });

        } else if (broadcast.emergency.checked == true && broadcast.normal.checked == false) {
            //Apenas emergencial
            window.localStorage.setItem("broadcastConfig", 1);
            //console.log("Apenas Emergencial");
            $ionicPopup.alert({
                title: 'Sucesso',
                okType: 'button-zem',
                template: 'A partir de agora você ira visualizar apenas as mensagens emergênciais.'
            });

        } else {
            //nenhuma
            $ionicPopup.alert({
                title: 'Atenção',
                okType: 'button-assertive',
                template: 'E necessário selecionar ao menos um tipo de recebimento de mensagem para ser recebido.'
            });
        }
        //console.log($scope.broadcastConfig);
    }
});
