@processRemoteValidation = (form, errorMap) ->
  errorMap = errorMap.originalElement # remove restangular
  noError = true
  for key, elem of form
    if elem.$invalid
      elem.$setValidity 'remote', true
    elem.message = null
  form.errors = {}
  for path, error of errorMap
    if _.isString(error)
      if form?[path]?
        form[path].$setValidity 'remote', false
      form.errors[path] = error
      noError := false
  noError

hotelcommon = angular.module 'hotel.common', ['restangular']

hotelcommon.factory 'UserService', ['Restangular', (Restangular) ->
  loggedIn = null
  users = Restangular.all('users')

  loadLoggedIn = !->
    loggedIn := users.one('loggedIn').get()

  loadLoggedIn()
  # api
  {
    loggedIn: -> loggedIn

    login: (email, password) ->
      users.all('authenticate').post {'email': email, 'password': password} .then (result) ->
        if result.result == 'OK'
          loadLoggedIn()
        result

    update: (data) ->
      data.put()
  }
]

hotelcommon.controller 'LoginModalCtrl', ['$scope', 'UserService', '$modal', '$rootScope'
($scope, UserService, $modal, $rootScope) !->
  $scope.user = {email: '', password: ''}
  $scope.login = !->
    UserService.login $scope.user.email, $scope.user.password
    .then (res) !->
      if res.result == 'OK'
        try
          $scope.$close(null)
      else
        $scope.loginError = true

]

hotelcommon.factory 'UnauthorizedInterceptor', ['$q', '$injector', ($q, $injector) ->
  $rootScope = $injector.get('$rootScope')
  $rootScope.failedRequests = []

  errorHandler = (error) ->
    if error.status == 401
      $http = $injector.get('$http')
      if !$rootScope.loginModalOpen
        $rootScope.loginModalOpen = true
        $modal = $injector.get('$modal')
        $rootScope.loginModalResult = $modal.open({
          templateUrl: 'public/common/partials/login_modal.html'
          backdrop: 'static'
          keyboard: false
        }).result
      $rootScope.loginModalResult.then !->
        $rootScope.loginModalOpen = false
      deferred = $q.defer()
#        $rootScope.loginModalResult.then(deferred.resolve, deferred.reject)
      $rootScope.loginModalResult.then !->
        $http(error.config).then (success)!->
          deferred.resolve(success)
        , (error) ->
          errorHandler(error)
      deferred.promise
    else
      $q.reject(error)

  responseError: errorHandler
]