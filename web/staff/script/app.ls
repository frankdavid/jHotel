app = angular.module 'hotel.staff', ['hotel.common', 'ui.router', 'restangular', 'ui.bootstrap', 'LocalStorageModule']

app.config ['$httpProvider', '$locationProvider', '$stateProvider', '$urlRouterProvider',
($httpProvider, $locationProvider, $stateProvider, $urlRouteProvider) !->

  $locationProvider.html5Mode(true).hashPrefix('!')
  $stateProvider
      .state 'login',
        url: '/staff/login'
        templateUrl: "public/staff/partials/login.html"
      .state 'reservations',
        url: "/staff/reservations"
        templateUrl: "public/staff/partials/reservations.html"
      .state 'purchase',
        url: "/staff/purchase/{roomNumber}"
        templateUrl: "public/staff/partials/purchase.html"
      .state 'rooms',
        url: "/staff/rooms"
        templateUrl: "public/staff/partials/rooms.html"
  $httpProvider.interceptors.push('UnauthorizedInterceptor')

]

app.run ['Restangular', '$state', '$modal', (Restangular, $state, $modal) !->
  Restangular.setBaseUrl 'rest'
  Restangular.setErrorInterceptor (response, promise) ->
      if response.status == 401
          modal = $modal.open {
            templateUrl: 'public/common/partials/login_modal.html'
            backdrop: 'static'
            keyboard: false
          }
      false
]