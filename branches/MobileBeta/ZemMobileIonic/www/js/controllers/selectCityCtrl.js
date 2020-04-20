angular.module('app.controllers')
    .controller('selectCityCtrl', function($scope, $state, MobileFactory, $ionicHistory, IpService, $ionicPopup) {
        $ionicHistory.nextViewOptions({
            disableAnimate: true
        });
        var ipLocal = window.localStorage.getItem("ipService");

        if (ipLocal != null) {
            $state.go('zEMZeladoriaMunicipal');
        }

        MobileFactory.getSelectCity().then(function(data) {
            //console.log("Em Andamento");
            $scope.cities = data.data;
            console.log($scope.cities);
        }, function(erro) {
            console.log(erro);
        });

        $scope.getIpServer = function(selectCity) {
            if (selectCity != null) {
                //parte responsavel por pegar o ip da cidade selecionada, setar no localStorage e no IP
                console.log(selectCity.webAddress);
                showConfirm(selectCity);
            } else {
                //quando a opção default e selecionada
                $ionicPopup.alert({
                    title: 'Atenção',
                    okType: 'button-zem',
                    template: 'E necessário selecionar uma cidade para registrar seus chamados.'
                });
                console.log("Não foi selecionada nenhuma cidade");
            }

        }

        function showConfirm(selectCity) {
            var confirmPopup = $ionicPopup.confirm({
                title: 'Confirmar cidade.',
                template: 'Você tem certeza que deseja registrar seus chamados para a cidade de ' + selectCity.name + '?'
            });
            confirmPopup.then(function(res) {
                if (res) {
                    IpService.setIp(selectCity.webAddress);
                    $state.go('zEMZeladoriaMunicipal');
                }
            });
        };
    })
