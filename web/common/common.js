// Generated by LiveScript 1.2.0
var hotelcommon;
this.processRemoteValidation = function(form, errorMap){
  var noError, key, elem, path, error;
  errorMap = errorMap.originalElement;
  noError = true;
  for (key in form) {
    elem = form[key];
    if (elem.$invalid) {
      elem.$setValidity('remote', true);
    }
    elem.message = null;
  }
  form.errors = {};
  for (path in errorMap) {
    error = errorMap[path];
    if (_.isString(error)) {
      if ((form != null ? form[path] : void 8) != null) {
        form[path].$setValidity('remote', false);
      }
      form.errors[path] = error;
      noError = false;
    }
  }
  return noError;
};
hotelcommon = angular.module('hotel.common', ['restangular']);
hotelcommon.factory('UserService', [
  'Restangular', function(Restangular){
    var loggedIn, users, loadLoggedIn;
    loggedIn = null;
    users = Restangular.all('users');
    loadLoggedIn = function(){
      loggedIn = users.one('loggedIn').get();
    };
    loadLoggedIn();
    return {
      loggedIn: function(){
        return loggedIn;
      },
      login: function(email, password){
        return users.all('authenticate').post({
          'email': email,
          'password': password
        }).then(function(result){
          if (result.result === 'OK') {
            loadLoggedIn();
          }
          return result;
        });
      },
      update: function(data){
        return data.put();
      }
    };
  }
]);
hotelcommon.controller('LoginModalCtrl', [
  '$scope', 'UserService', '$modal', '$rootScope', function($scope, UserService, $modal, $rootScope){
    $scope.user = {
      email: '',
      password: ''
    };
    $scope.login = function(){
      UserService.login($scope.user.email, $scope.user.password).then(function(res){
        if (res.result === 'OK') {
          try {
            $scope.$close(null);
          } catch (e$) {}
        } else {
          $scope.loginError = true;
        }
      });
    };
  }
]);
hotelcommon.factory('UnauthorizedInterceptor', [
  '$q', '$injector', function($q, $injector){
    var $rootScope, errorHandler;
    $rootScope = $injector.get('$rootScope');
    $rootScope.failedRequests = [];
    errorHandler = function(error){
      var $http, $modal, deferred;
      if (error.status === 401) {
        $http = $injector.get('$http');
        if (!$rootScope.loginModalOpen) {
          $rootScope.loginModalOpen = true;
          $modal = $injector.get('$modal');
          $rootScope.loginModalResult = $modal.open({
            templateUrl: 'public/common/partials/login_modal.html',
            backdrop: 'static',
            keyboard: false
          }).result;
        }
        $rootScope.loginModalResult.then(function(){
          $rootScope.loginModalOpen = false;
        });
        deferred = $q.defer();
        $rootScope.loginModalResult.then(function(){
          $http(error.config).then(function(success){
            deferred.resolve(success);
          }, function(error){
            return errorHandler(error);
          });
        });
        return deferred.promise;
      } else {
        return $q.reject(error);
      }
    };
    return {
      responseError: errorHandler
    };
  }
]);