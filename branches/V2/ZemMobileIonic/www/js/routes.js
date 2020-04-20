angular.module('app.routes', [])

    .config(function ($stateProvider, $urlRouterProvider) {

        // Ionic uses AngularUI Router which uses the concept of states
        // Learn more here: https://github.com/angular-ui/ui-router
        // Set up the various states which the app can be in.
        // Each state's controller can be found in controllers.js
        $stateProvider



            .state('zEMZeladoriaMunicipal', {
                url: '/loginOptions',
                cache: false,
                templateUrl: 'templates/zEMZeladoriaMunicipal.html',
                controller: 'zEMZeladoriaMunicipalCtrl'
            })

            .state('login', {
                url: '/loginZEM',
                templateUrl: 'templates/login.html',
                controller: 'loginCtrl'
            })

            .state('inserirCPF', {
                url: '/pageCpf',
                templateUrl: 'templates/inserirCPF.html',
                controller: 'inserirCPFCtrl'
            })

            .state('cadastro', {
                url: '/cadastroPage1',
                templateUrl: 'templates/cadastro.html',
                controller: 'cadastroCtrl'
            })

            .state('cadastro2', {
                url: '/cadastroPage2',
                templateUrl: 'templates/cadastro2.html',
                controller: 'cadastro2Ctrl'
            })

            .state('homeLogin', {
                url: "/homeLogin",
                templateUrl: "templates/homeLogin.html",
                controller: 'homeLoginCtrl'
            })

            .state('cidades', {
                url: '/cidades',
                cache: false,
                templateUrl: 'templates/cidades.html',
                controller: 'cidadesCtrl'
            })

            .state('estados', {
                url: '/estados',
                cache: false,
                templateUrl: 'templates/estados.html',
                controller: 'estadosCtrl'
            })

            .state('bairros', {
                url: '/bairros',
                cache: false,
                templateUrl: 'templates/bairros.html',
                controller: 'bairrosCtrl'
            })

            .state('menu.bairros2', {
                url: '/bairros2',
                cache: false,
                views: {
                    'menuContent': {
                        templateUrl: 'templates/bairros2.html',
                        controller: 'bairros2Ctrl'
                    }
                }
            })

            .state('bairros2', {
                url: '/bairro2',
                cache: false,
                templateUrl: 'templates/bairros2.html',
                controller: 'bairros2Ctrl'
            })

            .state('cidades2', {
                url: '/cidades2',
                cache: false,
                templateUrl: 'templates/cidades2.html',
                controller: 'cidades2Ctrl'
            })

            .state('estados2', {
                url: '/estados2',
                cache: false,
                templateUrl: 'templates/estados2.html',
                controller: 'estados2Ctrl'
            })

            .state('menu.mainPage', {
                url: '/mainPage',
                cache: false,
                views: {
                    'menuContent': {
                        templateUrl: 'templates/mainPage.html',
                        controller: 'mainPageCtrl'
                    }
                }
            })

            .state('menu.filter', {
                url: '/filter',
                cache: false,
                views: {
                    'menuContent': {
                        templateUrl: 'templates/filter.html',
                        controller: 'filterCtrl'
                    }
                }
            })

            .state('menu.chamados', {
                url: '/chamados',
                views: {
                    'menuContent': {
                        templateUrl: 'templates/chamados.html',
                        controller: 'chamadosCtrl'
                    }
                }
            })

            .state('menu.filterBM', {
                url: '/filterBM',
                cache: false,
                views: {
                    'menuContent': {
                        templateUrl: 'templates/filterBM.html',
                        controller: 'filterBMCtrl'
                    }
                }
            })

            .state('menu.messages', {
                url: '/messages',
                views: {
                    'menuContent': {
                        templateUrl: 'templates/messages.html',
                        controller: 'messagesCtrl'
                    }
                }
            })

            .state('menu.resposta', {
                url: '/resposta',
                cache: false,
                views: {
                    'menuContent': {
                        templateUrl: 'templates/resposta.html',
                        controller: 'respostaCtrl'
                    }
                }
            })

            .state('menu.about', {
                url: '/about',
                views: {
                    'menuContent': {
                        templateUrl: 'templates/about.html',
                        controller: 'aboutCtrl'
                    }
                }
            })

            .state('menu.aboutAndHelp', {
                url: '/aboutAndHelp',
                views: {
                    'menuContent': {
                        templateUrl: 'templates/aboutAndHelp.html',
                        controller: 'aboutAndHelpCtrl'
                    }
                }
            })

            .state('menu.help', {
                url: '/help',
                views: {
                    'menuContent': {
                        templateUrl: 'templates/help.html',
                        controller: 'helpCtrl'
                    }
                }
            })

            .state('helpView', {
                url: '/helpView',
                templateUrl: 'templates/helpView.html',
                controller: 'helpViewCtrl'

            })

            .state('menu', {
                url: '/menu',
                abstract: true,
                templateUrl: 'templates/menu.html',
                controller: 'menuCtrl'
            })

            .state('menu.meusChamados', {
                url: '/meusChamados',
                views: {
                    'menuContent': {
                        templateUrl: 'templates/meusChamados.html',
                        controller: 'meusChamadosCtrl'
                    }
                }
            })




            .state('menu.registrarChamado', {
                url: '/registrarChamado',
                views: {
                    'menuContent': {
                        templateUrl: 'templates/registrarChamado.html',
                        controller: 'registrarChamadoCtrl'
                    }
                }
            })

            .state('registrarChamado', {
                url: '/chamado1',
                templateUrl: 'templates/registrarChamado.html',
                controller: 'registrarChamadoCtrl'
            })

            .state('registrarChamado2', {
                url: '/chamado2',
                templateUrl: 'templates/registrarChamado2.html',
                controller: 'registrarChamado2Ctrl'
            })

            .state('menu.registrarChamado2', {
                url: '/registrarChamado2',
                views: {
                    'menuContent': {
                        templateUrl: 'templates/registrarChamado2.html',
                        controller: 'registrarChamado2Ctrl'
                    }
                }
            })

            .state('registrarChamado3', {
                url: '/chamado3',
                templateUrl: 'templates/registrarChamado3.html',
                controller: 'registrarChamado3Ctrl'
            })

            .state('menu.registrarChamado3', {
                url: '/registrarChamado3',
                views: {
                    'menuContent': {
                        templateUrl: 'templates/registrarChamado3.html',
                        controller: 'registrarChamado3Ctrl'
                    }
                }
            })

            .state('registrarChamado4', {
                url: '/chamado4',
                templateUrl: 'templates/registrarChamado4.html',
                controller: 'registrarChamado4Ctrl'
            })

            .state('menu.registrarChamado4', {
                url: '/registrarChamado4',
                views: {
                    'menuContent': {
                        templateUrl: 'templates/registrarChamado4.html',
                        controller: 'registrarChamado4Ctrl'
                    }
                }
            })

            .state('registrarChamado5', {
                url: '/chamado5',
                templateUrl: 'templates/registrarChamado5.html',
                controller: 'registrarChamado5Ctrl'
            })

            .state('menu.registrarChamado5', {
                url: '/registrarChamado5',
                views: {
                    'menuContent': {
                        templateUrl: 'templates/registrarChamado5.html',
                        controller: 'registrarChamado5Ctrl'
                    }
                }
            })

            .state('registrarChamado6', {
                url: '/chamado6',
                templateUrl: 'templates/registrarChamado6.html',
                controller: 'registrarChamado6Ctrl'
            })

            .state('menu.registrarChamado6', {
                url: '/registrarChamado6',
                views: {
                    'menuContent': {
                        templateUrl: 'templates/registrarChamado6.html',
                        controller: 'registrarChamado6Ctrl'
                    }
                }
            })

            .state('menu.detalhamentoChamado', {
                url: '/detalhamentoChamado',
                views: {
                    'menuContent': {
                        templateUrl: 'templates/detalhamentoChamado.html',
                        controller: 'detalhamentoChamadoCtrl'
                    }
                }
            })







            .state('menu.configuration', {
                url: '/configuration',
                views: {
                    'menuContent': {
                        templateUrl: 'templates/configuration.html',
                        controller: 'configurationCtrl'
                    }
                }
            })

            .state('selectCity', {
                url: '/selectCity',
                templateUrl: 'templates/selectCity.html',
                controller: 'selectCityCtrl'
            })

        $urlRouterProvider.otherwise('/selectCity');
    });
