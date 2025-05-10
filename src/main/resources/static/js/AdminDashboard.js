var app = angular.module('adminDashboard', ['ui.bootstrap', 'ui.router']);


// app.config(['$stateProvider', '$urlRouterProvider', '$locationProvider',
//     function ($stateProvider, $urlRouterProvider, $locationProvider) {
        
//     $locationProvider.hashPrefix('!');

//     $urlRouterProvider.otherwise('/AdminDashboard');
//       // Optional: if you want clean URLs (without #)
//     $locationProvider.html5Mode(false); // or true if server is configured properly
  
//     $stateProvider
//           .state('role', {
//               url: '/Role',
//               templateUrl: 'Role',
//               controller: 'viewRoleContr'
//           });
  
//   }]);


app.controller('admminDashboardCont', ['$scope', '$state', '$stateParams', '$http', function ($scope, $state, $stateParams, $http) {

    // $scope.goToRole = function () {
    //     $state.go('role');
    // };
    
}]);