angular.module('app.controllers')
    .controller('filterCtrl', function($scope, MobileFactory, UnsolvedCallService, SolvedCallService, $state, UserService, $ionicLoading, $ionicPopup) {
        $scope.citizenCalls = [];
        $scope.citizenCalls2 = [];

        var token = window.localStorage.getItem("token");
        var user = UserService.getUser().userID;
        var filt = {
            'token': token,
            'facebookId': user
        };

        //iniciar animação de loading
        $ionicLoading.show({
            content: 'Loading',
            animation: 'fade-in',
            showBackdrop: true,
            maxWidth: 200,
            showDelay: 0
        });

        //  $scope.filter={entityId:"z", entityCategoryId:"z", callProgress:"z",citizenCpf:"93278999005", description: "z"};
        $scope.filter = {
            entityId: "",
            entityCategoryId: "",
            callProgress: "",
            citizenToken: token,
            description: "",
            periodod: '',
            periodoa: '',
            facebookId: user,
            excluded: [311]
        };

        mostrar();

        function mostrar() {
            //iniciar animação de loading
            $ionicLoading.show({
                content: 'Loading',
                animation: 'fade-in',
                showBackdrop: true,
                maxWidth: 200,
                showDelay: 0
            });

            //validação ao apertar o aplicar varias vezes consecutivas
            if ($scope.filter.periodoa == null) {
                $scope.filter.periodoa = "";
            }
            if ($scope.filter.periodob == null) {
                $scope.filter.periodob = "";
            }
            if ($scope.filter.periodod == null) {
                $scope.filter.periodod = "";
            }

            console.log($scope.filter);

            //$scope.citizenCalls = [];



            // MobileFactory.getCall3($scope.filter).then(function(dataSolved) {
            MobileFactory.getSO($scope.filter).then(function(dataSolved) {
                console.log("Data Solved: ");
                console.log(dataSolved);

                // MobileFactory.getCall2($scope.filter).then(function(dataUnsolved) {
                MobileFactory.getUN($scope.filter).then(function(dataUnsolved) {
                    console.log("Data Unsolved: ");
                    console.log(dataUnsolved);

                    $scope.citizenCalls2 = dataSolved;
                    $scope.citizenCalls = dataUnsolved;
                    
                    $ionicLoading.hide();
                    //informar ao usuario quando ele ainda não possui chamado.
                    if ($scope.citizenCalls != null) {
                        if ($scope.citizenCalls.length == 0) {
                            console.log("Nenhum chamado cadastrado na Unsolved Call.");
                            /*$ionicPopup.alert({
                                title: 'Atenção',
                                template: 'Nenhum chamado cadastrado.',
                                okType: 'button-zem',
                            });*/
                        }
                    } else {
                        console.log("Nenhum chamado cadastrado na Unsolved Call.");
                        /*$ionicPopup.alert({
                            title: 'Atenção',
                            template: 'Nenhum chamado cadastrado.',
                            okType: 'button-zem',
                        });*/
                    }
                });

                $ionicLoading.hide();
                //informar ao usuario quando ele ainda não possui chamado.
                if ($scope.citizenCallsSolved != null) {
                    if ($scope.citizenCallsSolved.length == 0) {
                        console.log("Nenhum chamado cadastrado na Solved Call.");
                        /*$ionicPopup.alert({
                            title: 'Atenção',
                            template: 'Nenhum chamado cadastrado.',
                            okType: 'button-zem',
                        });*/
                    }
                } else {
                    console.log("Nenhum chamado cadastrado na Solved Call.");
                    /*$ionicPopup.alert({
                        title: 'Atenção',
                        template: 'Nenhum chamado cadastrado.',
                        okType: 'button-zem',
                    });*/
                }
            });
        }

        MobileFactory.getEntity2().then(function(data) {
            $scope.citizenCalls = [];
            $scope.entities = data;
        });

        MobileFactory.getEntityCategory2().then(function(data) {
            $scope.entityCategories = data;
        });

        MobileFactory.getCallProgress2().then(function(data) {
            $scope.callProgressl = data.list;
        });

        $scope.clean = function() {
            //iniciar animação de loading
            $ionicLoading.show({
                content: 'Loading',
                animation: 'fade-in',
                showBackdrop: true,
                maxWidth: 200,
                showDelay: 0
            });

            //validação ao apertar o aplicar varias vezes consecutivas

            $scope.filter.periodoa = "";
            $scope.filter.periodob = "";
            $scope.filter.periodod = "";

            console.log($scope.filter);

            MobileFactory.getSO($scope.filter).then(function(dataSolved) {

                console.log("Data Solved: ");
                console.log(dataSolved);

                MobileFactory.getUN($scope.filter).then(function(dataUnsolved) {
                    console.log("Data Unsolved: ");
                    console.log(dataUnsolved);

                    $scope.citizenCalls2 = dataSolved;
                    $scope.citizenCalls = dataUnsolved;

                    $ionicLoading.hide();
                    //informar ao usuario quando ele ainda não possui chamado.
                    if ($scope.citizenCalls != null) {
                        if ($scope.citizenCalls.length == 0) {
                            console.log("Nenhum chamado cadastrado na Unsolved Call.");
                            $ionicPopup.alert({
                                title: 'Atenção',
                                template: 'Nenhum chamado cadastrado.',
                                okType: 'button-zem',
                            });
                        }
                    } else {
                        console.log("Nenhum chamado cadastrado na Unsolved Call.");
                        $ionicPopup.alert({
                            title: 'Atenção',
                            template: 'Nenhum chamado cadastrado.',
                            okType: 'button-zem',
                        });
                    }
                });
            });
        }

        $scope.next = function(unsolvedCall) {
            UnsolvedCallService.setUnsolvedCall(unsolvedCall);
            $state.go("menu.chamados");
        }

        $scope.toggleGroup = function(entity) {
            if ($scope.isGroupShown(entity)) {
                $scope.shownGroup = null;
            } else {
                $scope.shownGroup = entity;
            }
        };

        $scope.isGroupShown = function(entity) {
            return $scope.shownGroup === entity;
        };

        $scope.callProgress = MobileFactory.getCallProgress2();

        $scope.showFilter = function() {
            //iniciar animação de loading
            $ionicLoading.show({
                content: 'Loading',
                animation: 'fade-in',
                showBackdrop: true,
                maxWidth: 200,
                showDelay: 0
            });

            //validação ao apertar o aplicar varias vezes consecutivas
            if ($scope.filter.periodoa == null) {
                $scope.filter.periodoa = "";
            }
            if ($scope.filter.periodob == null) {
                $scope.filter.periodob = "";
            }
            if ($scope.filter.periodod == null) {
                $scope.filter.periodod = "";
            }

            console.log($scope.filter);
            MobileFactory.getUN($scope.filter).then(function(data) {
                $scope.citizenCalls = data;
                $ionicLoading.hide();
                //informar ao usuario quando ele ainda não possui chamado.
                if ($scope.citizenCalls != null) {
                    if ($scope.citizenCalls.length == 0) {
                        console.log("Nenhum chamado cadastrado na Unsolved Call.");
                        /*$ionicPopup.alert({
                            title: 'Atenção',
                            template: 'Nenhum chamado cadastrado.',
                            okType: 'button-zem',
                        });*/
                    }
                } else {
                    console.log("Nenhum chamado cadastrado na Unsolved Call.");
                    /*$ionicPopup.alert({
                        title: 'Atenção',
                        template: 'Nenhum chamado cadastrado.',
                        okType: 'button-zem',
                    });*/
                }
            });

            MobileFactory.getSO($scope.filter).then(function(data) {
                $scope.citizenCalls2 = data;
                console.log($scope.citizenCalls);
                $ionicLoading.hide();
                //informar ao usuario quando ele ainda não possui chamado.
                if ($scope.citizenCalls != null) {
                    if ($scope.citizenCalls.length == 0) {
                        console.log("Nenhum chamado cadastrado na Solved Call.");
                        /*$ionicPopup.alert({
                            title: 'Atenção',
                            template: 'Nenhum chamado cadastrado.',
                            okType: 'button-zem',
                        });*/
                    }
                } else {
                    console.log("Nenhum chamado cadastrado na Solved Call.");
                    /*$ionicPopup.alert({
                        title: 'Atenção',
                        template: 'Nenhum chamado cadastrado.',
                        okType: 'button-zem',
                    });*/
                }
            });
        }
    });
