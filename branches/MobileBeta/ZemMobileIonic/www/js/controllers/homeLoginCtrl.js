angular.module('app.controllers')

//tela de teste do FacebookLogin
.controller('homeLoginCtrl', function($scope, UserService, $ionicActionSheet, $state, $ionicLoading, $ionicPopup, $http, IpService){

  //var url = "http://192.168.1.109:8080/rest/mobile/";
var url = IpService.getIp();
  $scope.customers = [];

  //Logout
  $scope.user = UserService.getUser();

  $scope.showLogOutMenu = function() {
    var hideSheet = $ionicActionSheet.show({
      destructiveText: 'Logout',
      titleText: 'Tem certeza que deseja efetuar Logout?',
      cancelText: 'Cancelar',
      cancel: function() {},
      buttonClicked: function(index) {
        return true;
      },
      destructiveButtonClicked: function(){
        $ionicLoading.show({
          template: 'Logging out...'
        });

        //facebook logout
        facebookConnectPlugin.logout(function(){
          $ionicLoading.hide();
          $state.go('zEMZeladoriaMunicipal');
        },
        function(fail){
          $ionicLoading.hide();
        });
      }
    });
  };

  $scope.createNewCall = function(){
    $state.go('registrarChamado');
  };

      //Função de Logout.
    $scope.logout = function(){
      var confirmPopup = $ionicPopup.confirm({
        title: 'Sair',
        template: 'Tem certeza de que quer sair do aplicativo?',
        cancelText: 'Cancelar',
      });

      confirmPopup.then(function(res) {
        if(res) {
          var token = window.localStorage.getItem("token");
          var facebookLogin = window.localStorage.getItem("starter_facebook_user");
          console.log("Token: " + token + " - FacebookLogin: " + facebookLogin);

          //função para apagar o token do banco e fazer logout no facebook.
          if(token != null || facebookLogin != null){
            if(token != null){
              $http({
                method: "DELETE",
                url: url+"/token",
                params: {
                  "token" : token
                },
                headers: {'Content-Type': 'application/json'},
                responseType: 'json'
              }).success(function(){
                window.localStorage.removeItem("token");
                console.log("Token removido.");
              }).error(function(){
                console.log("Tente novamente mais tarde");
              });
            }

            if(facebookLogin != null){
              //facebook logout
              facebookConnectPlugin.logout(function(){
                $ionicLoading.hide();              
              },
              function(fail){
                $ionicLoading.hide();
              });
            }
            $state.go('zEMZeladoriaMunicipal');
          }        
        }
      });
    };

});
