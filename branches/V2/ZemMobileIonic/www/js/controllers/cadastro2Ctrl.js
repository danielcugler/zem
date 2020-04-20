angular.module('app.controllers')

.controller('cadastro2Ctrl', function($scope, $state, MobileFactory, CitizenService, $ionicFilterBar, $window, AddressService, $http, $ionicPopup, IpService, $ionicHistory, $ionicLoading) {

    //var url = "http://192.168.1.115:8080/rest/mobile/";
    var url = IpService.getIp();

    $scope.estados;
    $scope.cidades;
    $scope.bairros;
    $scope.token;
    $scope.header;
    $scope.citizen = CitizenService.getCitizen();
    $scope.filteredArrayCity = [];

    $scope.setEstado = function() {
        $state.go('estados');
        //$window.location.href = '#/estados';
    }

    $scope.setcity2 = function() {
        if ($scope.citizen.state == "") {
            $ionicPopup.alert({
                title: 'Atenção',
                template: 'E necessário selecione um estado primeiro.',
                okType: 'button-zem'
            });
        } else {
            AddressService.setCityId($scope.citizen.state.stateId);
            //$window.location.href = '#/cidades';
            $state.go('cidades');
        }
    }

    $scope.setBairro = function() {
        //console.log($scope.citizen.cityId);
        if ($scope.citizen.cityId == "") {
            var alertPopup = $ionicPopup.alert({
                title: 'Atenção',
                template: 'E necessário selecione um estado e cidade primeiro.',
                okType: 'button-zem'
            });
        } else {
            AddressService.setCityId($scope.citizen.cityId.cityId);
            //$window.location.href = '#/bairros';
            $state.go('bairros');
        }
    }

    $scope.showFilterBar = function() {
        var filterBarInstance = $ionicFilterBar.show({
            cancelText: "<i class='ion-ios-close-outline'></i>",
            items: $scope.cidades,
            //filterProperties: ['name'],
            update: function(filteredItems, filterText) {
                $scope.cidades = filteredItems;
            }
        });
    };


    $scope.getCidade2 = function(query) {
        var returnValue = { items: [], selectedItems: [] };
        $scope.cidades.forEach(function(item) {

            if (item.nome.indexOf(query) > -1) {
                returnValue.items.push(item);
            }
        });

        return returnValue;
    }

    $scope.getCidade = function(cidade) {
        $scope.filteredArrayCity = [];
        $scope.filteredArrayCity.push(cidade);
    };
    $scope.filterCity = function(city) {
        if (city === "") {
            $scope.filteredArrayCity = [];
            return;
        }
        $scope.filteredArrayCity = [];
        angular.forEach($scope.cidades, function(cidade) {
            if (cidade.nome.indexOf(city) >= 0) {
                $scope.filteredArrayCity.push(cidade)
            }
        });
        //     $interval(function() {
        //     $scope.filteredArrayCity;
        // }, 500);
    };
    $scope.isActive = false;

    $scope.setGender = function(gender) {
        $scope.citizen.gender = gender;
        if (gender == 0)
            $scope.isActive = true;
        else
            $scope.isActive = false;
    }

    $scope.listCity = function(stateId) {
        MobileFactory.getCity(stateId).then(function(data) {
            $scope.cidades = data.data;
        });
    }
    $scope.listNeighborhood = function(cityId) {
        MobileFactory.getNeighborhood(cityId).then(function(data) {
            $scope.bairros = data.data;
        });
    }

    $scope.save = function(citizen) {
        if ($scope.citizen.neighborhoodId != "") {
            //iniciar animação de loading
            $ionicLoading.show({
                content: 'Loading',
                animation: 'fade-in',
                showBackdrop: true,
                maxWidth: 200,
                showDelay: 0
            });

            if($scope.citizen.phone_number == ""){
                delete $scope.citizen.phone_number;
            }
            delete $scope.citizen.state;
            delete $scope.citizen.cityId;
            console.log($scope.citizen);
            MobileFactory.saveCitizen($scope.citizen, $scope.header, $scope.token).then(function(data) {
                //console.log(data);
                //$scope.citizen={'state':'','cityId':'','neighborhoodId':''};
                //$scope.citizen.enabled=0;
                //console.log("sucesso");
                //chamando metodo para autenticar o novo usuario. email senha
                //console.log(citizen.password);
                //console.log(citizen.email);
                autentication(citizen);

                //$window.location.href = '#/homeLogin';
            }).catch(function(callback){
                //console.log(callback.data.errorMessage);
                console.log(callback);
                $ionicLoading.hide();
                if (callback.data.errorMessage === "Este e-mail já está cadastrado!") {
                    var alertPopup = $ionicPopup.alert({
                        title: 'Atenção',
                        template: 'Não foi possível efetuar o cadastro. Este e-mail já está cadastrado.',
                        okType: 'button-assertive'
                    });
                    delete $scope.citizen.email;
                    //cEmail.focus();
                    $state.go('cadastro');
                } else if (callback.data.errorMessage === "Este CPF já está cadastrado!") {
                    var alertPopup = $ionicPopup.alert({
                        title: 'Atenção',
                        template: 'Não foi possível efetuar o cadastro. Este CPF já está cadastrado.',
                        okType: 'button-assertive'
                    });
                    delete $scope.citizen.citizen_cpf;
                    //cCPF.focus();
                    $state.go('cadastro');
                }
            });
        }else{
            $ionicPopup.alert({
                title: 'Atenção',
                template: 'E necessário selecione o endereço.',
                okType: 'button-zem'
            });
        }
    }

    MobileFactory.getStates().then(function(data) {
        $scope.estados = data.data;
    });

    MobileFactory.getToken().then(function(data) {
        $scope.token = data.data.token;
        //console.log($scope.token)
    });
    MobileFactory.getHeader().then(function(data) {
        $scope.header = data.data;
        //console.log(header)
    });

    //parte dos toggle de sexo
    $scope.buttons = [
        { text: 'Masculino' },
        { text: 'Feminino' },
    ];
    $scope.activeButton = 0;
    $scope.setActiveButton = function(index) {
        $scope.activeButton = index;
    };
    //fim dos toggle

    function autentication(citizen) {
        $http.get(url + "/login/" + citizen.email + "/" + citizen.password).success(function(data) {
            //console.log("Estou na autenticação.");
            if (data.message === "token") {
                var token = data.code;
                window.localStorage.setItem("token", token);
                //limpar o cache e bloquear o back
                $ionicHistory.clearCache();
                $ionicHistory.nextViewOptions({
                    disableBack: true
                });
                $ionicLoading.hide();

      //Notificacoes
      //verificando se e a primeira vez que a pessoa instalou o app e logou
      var firebase = window.localStorage.getItem("firebase");
      //verifica se o tutorial e igual first
      console.log('notificacao');
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



                $state.go('menu.mainPage');
            }
        }).error(function() {
            $ionicPopup.alert({
                title: 'Erro ao efetuar login',
                template: 'Tente novamente mais tarde.',
                okType: 'button-assertive'
            });
            //limpar o cache e bloquear o back
            $ionicHistory.clearCache();
            $ionicHistory.nextViewOptions({
                disableBack: true
            });
            $ionicLoading.hide();
            $state.go('login');
        });
    };

});
