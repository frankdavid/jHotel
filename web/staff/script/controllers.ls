@app.controller 'ReservationCtrl', ['$scope', 'Restangular', ($scope, Restangular) ->
  $scope.reservations = Restangular.all('reservations/current').getList().$object
  $scope.searchCheck = 'both'
  $scope.search = {}
  $scope.checkIn = (reservation) !->
    Restangular.one('reservations', reservation.id).post('checkIn')
    reservation.checkInAvailable = false
    reservation.checkedIn = true
  $scope.checkOut = (reservation) !->
    if confirm "#{reservation.fullPrice} Ft a fizetendő"
      Restangular.one('reservations', reservation.id).post('checkOut')
      reservation.checkOutAvailable = false
      reservation.checkedOut = true
  $scope.$watch 'searchCheck', (value) !->
    switch value
      when 'both'
        delete $scope.search.checkInAvailable
        delete $scope.search.checkOutAvailable
      when 'in'
        $scope.search.checkInAvailable = true
        $scope.search.checkOutAvailable = false
      when 'out'
        $scope.search.checkInAvailable = false
        $scope.search.checkOutAvailable = true
]

@app.controller 'PurchaseCtrl', ['$scope', 'Restangular', '$state', '$stateParams',
($scope, Restangular, $state, $stateParams) ->
  $scope.submitPurchase = !->
    Restangular.all('purchases').post($scope.purchase).then !->
      $scope.success = true
    $scope.purchaseSubmitted = true
  $scope.reset = !->
    $state.go($state.$current, null, { reload: true })
  $scope.purchase =
    roomNumber: $stateParams.roomNumber
  $scope.$watch "purchase.roomNumber", (roomNumber) !->
    if roomNumber? && roomNumber != ""
      Restangular.all('rooms').one('number', roomNumber).one('guest').get().then (guest) !->
        if guest?
          $scope.guest = guest
          $scope.purchaseForm.roomNumber.$setValidity 'correct', true
        else
          $scope.guest = null
          $scope.purchaseForm.roomNumber.$setValidity 'correct', false
    else
      $scope.guest = null
      $scope.purchaseForm.roomNumber.$setValidity 'correct', true
]

@app.controller 'RoomCtrl', ['$scope', 'Restangular'
($scope, Restangular) ->
  rooms = Restangular.all('rooms')
  $scope.loadRooms = !->
    $scope.rooms = rooms.getList().$object
  $scope.loadRooms()
  $scope.edit = (room) !->
    $scope.editedRoomOrig = room
    $scope.editedRoom = Restangular.copy(room)
  $scope.save = !->
    if !$scope.editedRoom.id?
      rooms.post($scope.editedRoom).then !->
        $scope.loadRooms()
    else
      $scope.editedRoom.put()
    angular.copy($scope.editedRoom, $scope.editedRoomOrig)
    $scope.editedRoom = null
  $scope.cancel = !->
    if !$scope.editedRoom.id?
      $scope.rooms.splice(0, 1)
    $scope.editedRoom = null
  $scope.new = !->
#    edited =
#      number: ""
#      numberOfBeds: 2
#      smoking: false
    editedRoom = {}
    $scope.rooms.unshift(editedRoom)
    $scope.editedRoom = editedRoom

]