var app = angular.module('adminDashboard', ['ui.bootstrap', 'ui.router']);


// app.config(['$stateProvider', '$urlRouterProvider', '$locationProvider',
//     function ($stateProvider, $urlRouterProvider, $locationProvider) {
  
//       // Optional: if you want clean URLs (without #)
//       $locationProvider.html5Mode(false); // or true if server is configured properly
  
//       $stateProvider
//           .state('role', {
//               url: '/Role',
//               templateUrl: 'Role',
//               controller: 'viewRoleContr'
//           });
  
//       $urlRouterProvider.otherwise('');
//   }]);


app.controller('admminDashboardCont', ['$scope', '$state', '$stateParams', '$http', function ($scope, $state, $stateParams, $http) {

    console.log(" aaaaaa-a--- the  admin contrller ")

   
    
}]);

// app.controller('viewRoleContr', ['$scope', '$state', '$stateParams', '$http', function ($scope, $state, $stateParams, $http) {

//     console.log(" aaaaaa-a--- the  admin contrller ")

    
    
// }]);