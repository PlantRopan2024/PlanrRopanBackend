var app = angular.module('viewCityOrder', ['ui.bootstrap']);

app.controller('viewCityOrderCtrl', ['$scope', '$http', '$timeout', function ($scope, $http, $timeout) {

    // Example Maali list — you can fetch this via API as well
    $scope.cityList = [
        { id: 1, name: 'GHAZIABAD' },
        { id: 2, name: 'Noida' },
        { id: 3, name: 'New Delhi' }
    ];

    $scope.earnings = {
        orderCompleted: 5,
        daily: 520,
        weekly: 3250,
        monthly: 14000,
        total: 45000
    };

    $scope.selectedMaali = null;
    $scope.selectedDate = null;

    $scope.filterCity = function () {
        console.log("Selected City:", $scope.selectedCity);
        console.log("Selected Date:", $scope.selectedDate);
        // Add your filtering or API call logic here
    };


    $scope.dailyData = [
        { date: '2025-05-10', orders: 25, earnings: 250 },
        { date: '2025-05-11', orders: 30, earnings: 300 },
        { date: '2025-05-10', orders: 25, earnings: 250 },
        { date: '2025-05-11', orders: 30, earnings: 300 },
        { date: '2025-05-10', orders: 25, earnings: 250 },
        { date: '2025-05-11', orders: 30, earnings: 300 },
        { date: '2025-05-10', orders: 25, earnings: 250 },
        { date: '2025-05-11', orders: 30, earnings: 300 },
    ];

    $scope.weeklyData = [
        { range: '2025-05-06 to 2025-05-12', orders: 180, earnings: 1800 },
    ];

    $scope.monthlyData = [
        { month: 'May 2025', orders: 720, earnings: 7200 },
    ];

}]);
