angular.module('app.routes', [])

.config(function($stateProvider, $urlRouterProvider) {

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

    .state('registrarChamado3', {
        url: '/chamado3',
        templateUrl: 'templates/registrarChamado3.html',
        controller: 'registrarChamado3Ctrl'
    })

    .state('registrarChamado4', {
        url: '/chamado4',
        templateUrl: 'templates/registrarChamado4.html',
        controller: 'registrarChamado4Ctrl'
    })

    .state('registrarChamado5', {
        url: '/chamado5',
        templateUrl: 'templates/registrarChamado5.html',
        controller: 'registrarChamado5Ctrl'
    })

    .state('registrarChamado6', {
        url: '/chamado6',
        templateUrl: 'templates/registrarChamado6.html',
        controller: 'registrarChamado6Ctrl'
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

    .state('bairrosU2', {
        url: '/bairrosU2',
        cache: false,
        templateUrl: 'templates/bairrosU2.html',
        controller: 'bairrosU2Ctrl'
    })

    .state('menu.bairros2', {
        url: '/bairros2',
        cache: false,
        views: {
            'menuContent': {
                templateUrl: 'templates/bairros2.html',
                controller: 'bairrosUCtrl'
            }
        }
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

    .state('mainPage', {
        url: '/mainPage',
        templateUrl: 'templates/mainPage.html',
        controller: 'mainPageCtrl'
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

    .state('detalhamentoChamado', {
        url: '/detalhamentoChamado',
        cache: false,
        templateUrl: 'templates/detalhamentoChamado.html',
        controller: 'detalhamentoChamadoCtrl'
    })

    .state('resposta', {
        url: '/resposta',
        cache: false,
        templateUrl: 'templates/resposta.html',
        controller: 'respostaCtrl'
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

    .state('menu.registrarChamadoMenu', {
        url: '/registrarChamadoMenu',
        views: {
            'menuContent': {
                templateUrl: 'templates/registrarChamadoMenu.html',
                controller: 'registrarChamadoMenuCtrl'
            }
        }
    })

    .state('menu.registrarChamado2Menu', {
        url: '/registrarChamado2Menu',
        views: {
            'menuContent': {
                templateUrl: 'templates/registrarChamado2Menu.html',
                controller: 'registrarChamado2MenuCtrl'
            }
        }
    })

    .state('menu.registrarChamado3Menu', {
        url: '/registrarChamado3Menu',
        views: {
            'menuContent': {
                templateUrl: 'templates/registrarChamado3Menu.html',
                controller: 'registrarChamado3MenuCtrl'
            }
        }
    })

    .state('menu.registrarChamado4Menu', {
        url: '/registrarChamado4Menu',
        views: {
            'menuContent': {
                templateUrl: 'templates/registrarChamado4Menu.html',
                controller: 'registrarChamado4MenuCtrl'
            }
        }
    })

    .state('menu.registrarChamado5Menu', {
        url: '/registrarChamado5Menu',
        views: {
            'menuContent': {
                templateUrl: 'templates/registrarChamado5Menu.html',
                controller: 'registrarChamado5MenuCtrl'
            }
        }
    })

    .state('menu.registrarChamado6Menu', {
        url: '/registrarChamado6Menu',
        views: {
            'menuContent': {
                templateUrl: 'templates/registrarChamado6Menu.html',
                controller: 'registrarChamado6MenuCtrl'
            }
        }
    })

    .state('menu.detalhamentoChamadoMenu', {
        url: '/detalhamentoChamadoMenu',
        views:{
            'menuContent':{
                templateUrl: 'templates/detalhamentoChamadoMenu.html',
                controller: 'detalhamentoChamadoMenuCtrl'
            }
        }
    })

    .state('menu.configuration', {
        url: '/configuration',
        views:{
            'menuContent':{
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
