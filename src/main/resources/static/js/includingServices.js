var app = angular.module('viewIncludingService', ['ui.bootstrap', 'ui.router']);

app.controller('viewIncludingServicesCont', ['$scope', '$state', '$stateParams', '$http', function ($scope, $state, $stateParams, $http) {

    $scope.viewIncudingServiceMainData = [];

    $scope.submitIncludingServiceMain = function () {
        var data = {
            headerName: $scope.headerName,
            nameDetails: $scope.nameDetails
        };
        $http({
            method: 'POST',
            url: 'includingServiceName/addIncludingServiceMain',
            data: JSON.stringify(data),
            headers: {
                'Content-Type': 'application/json'
            },
            transformRequest: angular.identity
        }).then(function successCallback(response) {
            // Handle success response
            if (response.data.status === true) {
                Swal.fire({
                    title: response.data.message,
                    text: 'Thanks!!',
                    icon: 'success',
                    confirmButtonText: 'OK',
                }).then(() => {
                    location.reload();
                });
            } else {
                Swal.fire({
                    title: response.data.message,
                    text: 'Please try again!',
                    icon: 'warning',
                    confirmButtonText: 'OK',
                });
            }
        }, function errorCallback(response) {
            // Handle error response
            Swal.fire({
                title: 'Error',
                text: 'Something went wrong!',
                icon: 'error',
                confirmButtonText: 'OK',
            });
        });
    };

    $scope.handleAction = function (primaryKey, actionType) {    
        const endpoint = actionType === 'like' ? 'includingServiceName/includingServiceMainActive' : 'includingServiceName/includingServiceMainDisActive';
        $http.post(endpoint, { primaryKey: primaryKey }).then(
            function (response) {
                const data = response.data;
                if (data.status) {
                    Swal.fire({
                        icon: 'success',
                        title: 'Success',
                        text: data.message,
                        timer: 2000,
                    });
                    $scope.viewIncludingServiceMain(); // Refresh table/data
                } else {
                    Swal.fire({
                        icon: 'waring',
                        title: 'Failed',
                        text: data.message,
                    });
                }
            },
            function (error) {
                console.error('Error:', error);
                Swal.fire({
                    icon: 'error',
                    title: 'Server Error',
                    text: `Something went wrong while trying to ${actionType} the agent.`,
                });
            }
        );
    };
    

    $scope.viewIncludingServiceMain = function () {
        $http({
            method: 'GET',
            url: 'includingServiceName/viewIncludingServiceMain'
        }).then(function (response) {
            // Handle success response
            if (response.data.status) {
                $scope.viewIncudingServiceMainData = response.data.data;
            } else {
                $scope.viewIncudingServiceMainData = [];
            }
        }, function (error) {
            // Handle error response
            $scope.viewIncudingServiceMainData = [];
        });
    };



    $scope.viewIncludingServiceMain();
}]);
