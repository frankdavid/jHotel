app = angular.module 'hotel.guest', ['ui.router', 'restangular', 'ui.bootstrap', 'LocalStorageModule', 'ngAnimate', 'hotel.common']

app.config [
'$locationProvider', '$stateProvider', '$urlRouterProvider', 'RestangularProvider', '$httpProvider',
($locationProvider,   $stateProvider,   $urlRouteProvider,    Restangular,           $httpProvider) !->

  $locationProvider.html5Mode(true).hashPrefix('!')
  $stateProvider
      .state 'home',
        url: "/"
        templateUrl: "public/guest/partials/home.html"

      .state 'registration-completed',
        url: "/registration/completed"
        templateUrl: "public/guest/partials/registration.html"

      .state 'reservations',
        url: "/reservations"
        templateUrl: "public/guest/partials/profile.html"

      .state 'reservations.new',
        url: "/new"
        templateUrl: "public/guest/partials/profile.html"
        data:
          showNewModal: true

      .state 'reservations.new.confirm',
        url: "/confirm"
        templateUrl: "public/guest/partials/profile.html"
        data:
          showNewModal: true
          isConfirm: true

  Restangular.setBaseUrl 'rest'
  Restangular.setResponseExtractor (response) ->
    newResponse = response;
    if angular.isArray(response)
      angular.forEach newResponse, (value, key) !->
        newResponse[key].originalElement = angular.copy(value)
    else
      newResponse.originalElement = angular.copy(response)
    newResponse;

  $httpProvider.interceptors.push('UnauthorizedInterceptor')

]

app.controller 'HomeCtrl', ['$scope', 'Restangular', '$state', '$rootScope', ($scope, Restangular, $state, $rootScope) !->
  $scope.guest =
    email: ''
    password: ''
    password2: ''
    phone: ''
    name: ''

  $scope.signup = false

  $scope.submit = !->
    if $scope.signup
      $scope.doSignup()
    else
      $scope.doLogin()

  $scope.doLogin = !->
    Restangular.all('users/authenticate').post($scope.guest).then (result) !->
      switch result.result
        when 'INCORRECT_PASSWORD'
          $scope.incorrectPassword = true
        when 'USER_NOT_FOUND'
          $scope.signup = true
          $('#signup').slideDown()
        when 'OK'
          $rootScope.user = result.user
          if result.user.type == 'EMPLOYEE'
            window.location.href = 'staff'
          if result.user.type == 'GUEST'
            $state.go 'reservations'

  $scope.signupSuccess = false
  $scope.doSignup = !->
    $scope.submitted = true
    $scope.incorrectPassword = false
    Restangular.all('guests').post($scope.guest).then (errorMap) !->
      noError = processRemoteValidation($scope.login, errorMap)
      if noError
        $scope.signupSuccess = true

]

app.controller 'ProfileCtrl', ['$scope', 'Restangular', '$state', '$modal', 'localStorageService', 'UserService',
($scope, Restangular, $state, $modal, localStorageService, UserService) !->
  $scope.loadReservations = !->
    $scope.reservations = Restangular.one('guests', 'loggedIn').getList('reservations').$object

  $scope.openModal = !->
    modal = $modal.open {
      templateUrl: 'public/guest/partials/new_reservation_modal.html'
    }
    modal.result.then !->
      $scope.loadReservations()
      $state.go 'reservations'
    , !->
      $state.go 'reservations'

  UserService.loggedIn().then (user) ->
    $scope.user = Restangular.copy(user)

  $scope.loadReservations()

  $scope.cancel = (reservation) !->
    if confirm('Are you sure you want to cancel the reservation?')
      Restangular.one('reservations', reservation.id).remove()
      $scope.reservations.splice($scope.reservations.indexOf(reservation), 1)

  $scope.saveUserData = !->
    $scope.user.put().then (errorMap) !->
      if processRemoteValidation($scope.form, errorMap)
        $scope.saveSuccess = true

  $scope.openNewReservationModal = !->
    $state.go('reservations.new')
    $scope.openModal()


  $scope.viewReservation = (reservation) !->
    $scope.reservation = reservation
    modal = $modal.open {
      templateUrl: 'public/guest/partials/view_reservation_modal.html'
      controller: 'ViewReservationCtrl'
      scope: $scope
    }

  if $state.current.data?.showNewModal
    $scope.openModal()
]

app.controller 'ViewReservationCtrl', ['$scope', 'Restangular', '$state', '$modal', 'localStorageService',
($scope, Restangular, $state, $modal) !->
  $scope.purchases = Restangular.one('reservations', $scope.reservation.id).getList('purchases').$object
]

app.controller 'NewReservationCtrl', ['$scope', 'Restangular', '$state', '$modal', 'localStorageService',
($scope, Restangular, $state, $modal, localStorageService) !->
  $scope.createReservation = ->
    reservation = {}
    reservation.startDate = new Date()
    reservation.endDate = new Date()
    reservation.endDate.setDate(new Date().getDate() + 2)
    reservation.smoking = false
    reservation.numberOfBeds = 2
    reservation

  $scope.findRooms = !->
    Restangular.all('rooms/available').post($scope.reservation).then (results) !->
      $scope.searchSubmitted = true
      $scope.searchResults = results

  $scope.showConfirm = (room) !->
    $state.go('reservations.new.confirm')
    $scope.reservation.room = room
    $scope.confirmReservation = true

  $scope.hideConfirm = !->
    $state.go('reservations.new')
    $scope.confirmReservation = false

  $scope.doConfirm = !->
    Restangular.all('reservations').post($scope.reservation).then (success) !->
      if success == "true" # success is a string :\
        $scope.confirmSuccess = true
      else
        $scope.confirmFail = true

  $scope.isConfirm = ->
    $scope.reservation.room && $state.current.data?.isConfirm

  $scope.closeModal = ->
    $scope.$close()


  $scope.minStartDate = new Date()
  $scope.reservation = localStorageService.get('currentReservation') ? $scope.createReservation()
  if new Date($scope.reservation.startDate) < $scope.minStartDate
    $scope.reservation.startDate = $scope.minStartDate
  $scope.$watch 'reservation.startDate', (startDate) ->
    $scope.minEndDate = new Date(startDate)
    $scope.minEndDate.setDate($scope.minEndDate.getDate() + 1)
    if new Date($scope.reservation.endDate) < $scope.minEndDate
      $scope.reservation.endDate = $scope.minEndDate

  $scope.$watch 'reservation', (reservation) ->
    $scope.findRooms()
    localStorageService.add 'currentReservation', reservation
  , true # deep watch
]
