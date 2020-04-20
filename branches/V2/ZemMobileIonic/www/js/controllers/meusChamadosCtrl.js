angular.module('app.controllers')
    .controller('meusChamadosCtrl', function ($scope, $ionicPopover, MobileFactory, UnsolvedCallService, SolvedCallService, $state, UserService, $ionicLoading, $ionicPopup, $ionicModal) {
        //criando os vetores de Solved e Unsolved
        $scope.citizenCallsUnsolved = [];
        $scope.citizenCallsSolved = [];

        //variavel para exibir o card se há ou não chamados
        $scope.cardNoCall = true;

        //variaveis do filtro
        var filterExcluded;
        var arrayFilter = [];

        //pegando os tokens
        var token = window.localStorage.getItem("token");
        var user = UserService.getUser().userID;
        var filt = {
            'token': token,
            'facebookId': user
        };

        // filtros
        $scope.filter = {
            entityId: "",
            entityCategoryId: "",
            callProgress: "",
            citizenToken: token,
            description: "",
            periodod: '',
            periodoa: '',
            facebookId: user
        };

        /* Iniciado ao entrar a view*/
        $scope.$on('$ionicView.enter', function () {
            $scope.filter.excluded = criarFiltro();
            mostrar();
        });

        function criarFiltro() {
            //pegando os valores do localStorage
            filterExcluded = window.localStorage['filterExcluded'] || [];
            if (filterExcluded.length != 0) {
                arrayFilter = filterExcluded.split(",");
            }

            for (var i = 0; i < arrayFilter.length; i++) {
                arrayFilter[i] = parseInt(arrayFilter[i]);
            }
            //console.log("localStorage");
            //console.log(arrayFilter);
            return arrayFilter;
        }

        /* Parte de ocultar os filtros */
        $scope.filtros = false;
        $scope.exibirFiltros = function () {
            $scope.filtros = !$scope.filtros;
        }

        //iniciar animação de loading
        $ionicLoading.show({
            content: 'Loading',
            animation: 'fade-in',
            showBackdrop: true,
            maxWidth: 200,
            showDelay: 0
        });

        /* Combo de Andamento */
        MobileFactory.getCallProgress2().then(function (data) {
            $scope.callProgress1 = data.list;
            //console.log("Em Andamento");
            //console.log($scope.callProgressl);
        });

        /* Combo de Entidade (entityId)*/
        MobileFactory.getEntity2().then(function (data) {
            //$scope.citizenCalls = [];
            $scope.entities = data;
            //console.log("ID das Entidade");
            //console.log($scope.entities);
        });

        //adicionando um chamado a lista de exclusão
        $scope.deleteCitizen = function (parentId) {
            var confirmPopup = $ionicPopup.confirm({
                title: 'Excluir',
                template: 'Tem certeza que deseja excluir?',
                okType: 'button-assertive'
            });

            confirmPopup.then(function (res) {
                if (res) {
                    //console.log("Botao excluir apertado\nData");
                    //console.log('data');
                    //iniciar animação de loading
                    $ionicLoading.show({
                        content: 'Loading',
                        animation: 'fade-in',
                        showBackdrop: true,
                        maxWidth: 200,
                        showDelay: 0
                    });

                    MobileFactory.removeCitizen(parentId).then(function (data) {
                        console.log("Deletado com sucesso");
                        $ionicLoading.hide();
                        $scope.update();
                    }, function (error) {
                        console.log("Erro  ao deletar");
                        console.log(error);
                        $ionicLoading.hide();
                        $ionicPopup.alert({
                            title: 'Erro',
                            template: 'Não foi possível remover o chamado, tente novamente mais tarde.',
                            okType: 'button-assertive',
                        });
                    });
                }
            });
        }
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


            //
            MobileFactory.getSO($scope.filter).then(function (dataSolved) {
                console.log("Data Solved: ");
                //dataSolved.callList   todos os chamados
                //dataSolved.unreadList ver se foi lido ou n
                console.log(dataSolved);

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
                MobileFactory.getUN($scope.filter).then(function (dataUnsolved) {
                    $ionicLoading.hide();
                    console.log("Data Unsolved: ");
                    console.log(dataUnsolved);
                    if (dataUnsolved != null) {
                        $scope.citizenCallsUnsolved = dataUnsolved.callList;
                        $scope.readUnsolved = dataUnsolved.unreadList;
                    } else {
                        $scope.citizenCallsUnsolved = [];
                        $scope.readUnsolved = [];
                    }
                    if (dataSolved != null) {
                        $scope.citizenCallsSolved = dataSolved.callList;
                        $scope.readSolved = dataSolved.unreadList;
                    } else {
                        $scope.citizenCallsSolved = [];
                        $scope.readSolved = [];
                    }

                    //informar ao usuario quando ele ainda não possui chamado.
                    if ($scope.citizenCallsUnsolved == null && $scope.citizenCallsSolved == null) {
                        console.log("Nenhum chamado cadastrado.");
                        $scope.cardNoCall = false;
                    } else {
                        console.log("Há chamados cadastrados.");
                        $scope.cardNoCall = true;
                    }
                }, function (errorSolved) {
                    console.log("Erro solvedCall");
                    console.log(errorSolved);
                    $ionicLoading.hide();
                });
            }, function (errorUnsolved) {
                console.log("Erro unsolvedCall");
                console.log(errorUnsolved);
                $ionicLoading.hide();
            });
        }

        /* Botão atualizar */
        $scope.update = function () {
            //iniciar animação de loading
            $ionicLoading.show({
                content: 'Loading',
                animation: 'fade-in',
                showBackdrop: true,
                maxWidth: 200,
                showDelay: 0
            });

            //$scope.filter.excluded = criarFiltro();

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
            mostrar();
        }

        /* Função limpar */
        $scope.clean = function () {
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
            $scope.filter.periodod = "";
            $scope.filter.entityId = "";
            $scope.filter.callProgress = "";

            mostrar();
        }

        /* Aplicar o filtro */
        $scope.showFilter = function () {
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

            mostrar();
        }

        $scope.isReadUnsolved = function (id) {
            for (var ii in $scope.readUnsolved) {
                var row = $scope.readUnsolved[ii];
                //console.log(row);
                //console.log(ii);
                if (row.unreadCallId == id && row.read == false) {
                    //console.log(cont);
                    return true;
                }
                //cont++;
            }
            return false;
            //console.log(cont);
        }

        $scope.isReadSolved = function (id) {
            for (var ii in $scope.readSolved) {
                var row = $scope.readSolved[ii];
                //console.log(row);
                //console.log(ii);
                if (row.unreadCallId == id && row.read == false) {
                    //console.log(cont);
                    return true;
                }
                //cont++;
            }
            return false;
        }

        //readSolved
        $scope.nextS = function (solvedCall) {
            UnsolvedCallService.setUnsolvedCall(solvedCall);
            console.log("next");
            console.log(solvedCall);
            //verificando se o chamado e valido
            var readTempS = false;
            for (var ii in $scope.readSolved) {
                if ($scope.readSolved.length > 0) {
                    var rowS = $scope.readSolved[ii];
                    if (rowS.unreadCallId == solvedCall.parentCallId.unsolvedCallId && rowS.read == false) {
                        console.log("Não lido");
                        console.log()
                        readTempS = true;
                        break;
                    } else {
                        console.log("Lido");
                        readTempS = false;
                    }
                }
            }
            if (readTempS) {
                console.log("Chamar function readcall");
                MobileFactory.readCall(solvedCall.parentCallId.unsolvedCallId).then(function (data) {
                    console.log("Marcado como lido");
                    console.log(data);
                }, function (error) {
                    console.log("Error");
                    console.log(error);
                });
            }
            console.log(solvedCall.parentCallId.unsolvedCallId);
            $state.go("menu.chamados");
        }

        //readUnsolved
        $scope.nextU = function (unsolvedCall) {
            UnsolvedCallService.setUnsolvedCall(unsolvedCall);
            //console.log("next");
            //console.log(unsolvedCall);
            //verificando se o chamado e valido
            var readTempU = false;
            for (var ii in $scope.readUnsolved) {
                if ($scope.readUnsolved.length > 0) {
                    var rowU = $scope.readUnsolved[ii];
                    if (rowU.unreadCallId == unsolvedCall.parentCallId.unsolvedCallId && rowU.read == false) {
                        console.log("Não lido");
                        console.log()
                        readTempU = true;
                        break;
                    } else {
                        console.log("Lido");
                        readTempU = false;
                    }
                }
            }
            if (readTempU) {
                console.log("Chamar function readcall");
                MobileFactory.readCall(unsolvedCall.parentCallId.unsolvedCallId).then(function (data) {
                    console.log("Marcado como lido");
                    console.log(data);
                }, function (error) {
                    console.log("Error");
                    console.log(error);
                });
            }
            console.log(unsolvedCall.parentCallId.unsolvedCallId);
            $state.go("menu.chamados");
        }

        $scope.buttonHelp = function () {
            $ionicPopup.alert({
                title: 'Informações',
                template: 'Aqui você encontra a lista com todos os seus chamados. Note que o título do chamado é composto por um identificador único, pela sua categoria, e pela data da última atualização realizada pela prefeitura. Na figura circular antes do título do chamado você consegue acompanhar o progresso de atendimento do mesmo.  Lembre-se, para obter mais informações sobre um determinado chamado, basta selecioná-lo.',
                okType: 'button-zem'
            });

        }

        $scope.showImages = function (index) {
            $scope.activeSlide = index;
        }
        $scope.showModal = function (templateUrl) {
            $ionicModal.fromTemplateUrl(templateUrl, {
                scope: $scope,
                animation: 'slide-in-up'
            }).then(function (modal) {
                $scope.modal = modal;
                $scope.modal.show();
            });
        }
        // Close the modal
        $scope.closeModal = function () {
            $scope.modal.hide();
            $scope.modal.remove();
        };
    });
