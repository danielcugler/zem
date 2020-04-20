// Ionic Starter App

// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'
// 'starter.services' is found in services.js
// 'starter.controllers' is found in controllers.js
angular.module('app.controllers', []);
angular.module('app.routes', []);
angular.module('app.services', []);
angular.module('app.directives', []);
angular.module('app.factories', []);


angular.module('app', ['ionic', 'app.controllers', 'app.routes', 'app.services', 'app.directives', 'app.factories', 'jett.ionic.filter.bar', 'ngCordova', 'ui.utils.masks', 'ionic-ratings'])

.run(function($rootScope, $ionicPlatform, $cordovaStatusbar) {
  $ionicPlatform.ready(function() {



    //Inicio Notificações

    var push = PushNotification.init({
      android: {
        senderID: "322504266123"
      },
      browser: {
      },
      ios: {
        alert: "true",
        badge: "true",
        sound: "true"
      },
      windows: {}
    });

    push.on('registration', function (data) {
      console.log(data.registrationId);
      console.log("reg");
      window.localStorage.setItem('registrationId', data.registrationId);
    });

    push.on('notification', function (data) {
      console.log('notification event');
      console.log(data.message + data.title);
    });

    push.on('error', function (e) {
      console.log("error");
      console.log(e);
      // e.message
    });
    //fim notificaçãoes
    // a Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
    // for form inputs)
    if (window.cordova && window.cordova.plugins && window.cordova.plugins.Keyboard) {
      cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
      cordova.plugins.Keyboard.disableScroll(true);
    }
    if (window.StatusBar) {
      // org.apache.cordova.statusbar required
      StatusBar.styleDefault();
    }

    //ocultando a splash screen quando o aplicativo for totalmente carregado
    if (navigator && navigator.splashscreen) {
      navigator.splashscreen.hide();
    }

    //alteração da cor da statusBar
    $cordovaStatusbar.styleHex('#407662');
  });
})
