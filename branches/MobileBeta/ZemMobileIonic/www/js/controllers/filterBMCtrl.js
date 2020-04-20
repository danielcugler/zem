angular.module('app.controllers')
    .controller('filterBMCtrl', function($scope, MobileFactory, BroadcastMessageService, $state, $ionicLoading, $ionicPopup, CitizenService) {
        $scope.max = false;
        $scope.moreDataCanBeLoaded = function() {
            console.log('max');
            return !max;
        };
        $scope.citizen = CitizenService.getCitizen();
        $scope.bmList = [];
        $scope.readBM = [];
        MobileFactory.getReadBM("12345678913").then(
            function(resp) {
                $scope.readBM = resp;
            });
        $scope.isRead = function(bmid) {
            return $scope.readBM.indexOf(bmid) == -1;
        }
        var page = 1;
        $scope.loadMore = function() {
            if ($scope.max != true)
                //-1 ambos os chamados
                // 0 normal
                // 1 emergencial
                var broadcastConfig = window.localStorage.getItem("broadcastConfig");
                if (broadcastConfig == null) {
                    broadcastConfig = -1;
                }
                console.log("broadcastConfig");
                console.log(broadcastConfig);
                MobileFactory.getBM2(page, broadcastConfig).then(function(data) {
                    if (data.length > 0) {
                        $scope.bmList = $scope.bmList.concat(data);
                        $scope.$broadcast('scroll.infiniteScrollComplete');
                    } else {
                        $scope.max = true;
                        $scope.$broadcast('scroll.infiniteScrollComplete');
                    }
                }, function(err) {
                    console.log('noResults');
                    $scope.max = true;
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                });
            page++;
        };
        $scope.$on('$stateChangeSuccess', function() {
            $scope.loadMore();
        });
        $scope.next = function(broadcastMessage) {
            //console.log($scope.citizen);
            MobileFactory.setReadBM($scope.citizen.citizenCpf, broadcastMessage.broadcastMessageId).then(
                function(resp) {
                    $scope.readBM.push(broadcastMessage.broadcastMessageId);
                    BroadcastMessageService.setBroadcastMessage(broadcastMessage);
                    $state.go("menu.messages");
                });
        }
        $scope.update = function() {
                $scope.bmList = [];
                max = false;
                page = 1;
                $scope.loadMore();
            }
            /*
            MobileFactory.getBM(1).then(function(data) {
                $scope.bmList = data;
                $ionicLoading.hide();
                //informar ao usuario quando ele ainda não possui chamado.
                if ($scope.bmList != null) {
                    console.log("Nenhuma mensagem cadastrada.");
                }
            });
            */
    });
