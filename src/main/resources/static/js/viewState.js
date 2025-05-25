var app = angular.module('viewState', ['ui.bootstrap', 'ui.router']);

app.config(function ($stateProvider, $urlRouterProvider, $locationProvider) {
    $locationProvider.hashPrefix('!');

    $urlRouterProvider.otherwise('/viewState');

    $stateProvider
        .state('viewCity', {
            url: "/viewCity/:id",
            templateUrl: "viewCity",
            controller: "viewCityCtrl"
        })
        .state('viewState', {
            url: "/",
            templateUrl: "viewState",
            controller: "viewStateCtrl"
        });
});


app.controller('viewStateCtrl', ['$scope', '$sce', '$state', '$stateParams', '$http', '$timeout', function ($scope, $sce, $state, $stateParams, $http, $timeout) {

    $scope.viewStateData = [];
    $scope.selectedMessage = '';

    $scope.showStateSection = true;

    $scope.submitState = function () {
        if ($scope.isEdit) {
            const updatedTemplate = {
                primarykey: $scope.editPrimaryKey,
                stateName: $scope.stateName,
            };
            $http({
                method: 'POST',
                url: 'stateCtrl/updateState',
                data: JSON.stringify(updatedTemplate),
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(function successCallback(response) {
                if (response.data.status === true) {
                    Swal.fire({
                        title: 'Updated!',
                        text: response.data.message,
                        icon: 'success',
                        confirmButtonText: 'OK',
                    }).then(() => {
                        location.reload();
                    });
                } else {
                    Swal.fire({
                        title: 'Update Failed',
                        text: response.data.message,
                        icon: 'warning',
                        confirmButtonText: 'OK',
                    });
                }
            }, function errorCallback(response) {
                Swal.fire({
                    title: 'Error',
                    text: 'Something went wrong!',
                    icon: 'error',
                    confirmButtonText: 'OK',
                });
            });

        } else {
            // Prepare data for adding new template
            const newTemplate = {
                stateName: $scope.stateName,
            };

            $http({
                method: 'POST',
                url: 'stateCtrl/addState',
                data: JSON.stringify(newTemplate),
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(function successCallback(response) {
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
                Swal.fire({
                    title: 'Error',
                    text: 'Something went wrong!',
                    icon: 'error',
                    confirmButtonText: 'OK',
                });
            });
        }
    };


    $scope.viewStateDataList = function () {
        $http({
            method: 'GET',
            url: 'stateCtrl/ViewStateList'
        }).then(function (response) {
            // Handle success response
            if (response.data.status) {
                $scope.viewStateData = response.data.data;
            } else {
                $scope.viewStateData = [];
            }
        }, function (error) {
            // Handle error response
            $scope.viewStateData = [];
        });
    };

    $scope.handleAction = function (primaryKey, actionType) {
        const endpoint = actionType === 'like' ? 'stateCtrl/getApprovedState' : 'stateCtrl/getDisApprovedState';
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
                    $scope.viewStateDataList();
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

    $scope.getStateByCity = function (statePk) {
        $scope.showStateSection = false;
        $state.go('viewCity', { id: statePk });
    };

    $scope.clearForm = function () {
        $scope.stateName = '';
        $scope.isEdit = false;
        $scope.editPrimaryKey = null;
    };

    $scope.updateState = function (primaryKey) {
        const selected = $scope.viewStateData.find(item => item.primaryKey === primaryKey);
        if (selected) {
            $scope.stateName = selected.stateName;
            $scope.isEdit = true;
            $scope.editPrimaryKey = selected.primaryKey;
            $('#addState').modal('show');
        }
    };

    $('#addState').modal('hide');
    $scope.clearForm();

    $scope.viewStateDataList();

}]);


app.controller('viewCityCtrl', ['$scope', '$sce', '$state', '$stateParams', '$http', '$timeout', function ($scope, $sce, $state, $stateParams, $http, $timeout) {

    var id = $stateParams.id;
    $scope.viewCityData = [];
    $scope.getStateName = [];


    $scope.goBack = function () {
        $state.go('viewState');
    };

    $scope.submitCity = function () {
        if ($scope.isEdit) {
            const updatedTemplate = {
                primarykey: $scope.editPrimaryKey,
                cityName: $scope.cityName,
            };
            $http({
                method: 'POST',
                url: 'stateCtrl/updateCity',
                data: JSON.stringify(updatedTemplate),
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(function successCallback(response) {
                if (response.data.status === true) {
                    Swal.fire({
                        title: 'Updated!',
                        text: response.data.message,
                        icon: 'success',
                        confirmButtonText: 'OK',
                    }).then(() => {
                        location.reload();
                    });
                } else {
                    Swal.fire({
                        title: 'Update Failed',
                        text: response.data.message,
                        icon: 'warning',
                        confirmButtonText: 'OK',
                    });
                }
            }, function errorCallback(response) {
                Swal.fire({
                    title: 'Error',
                    text: 'Something went wrong!',
                    icon: 'error',
                    confirmButtonText: 'OK',
                });
            });

        } else {
            // Prepare data for adding new template
            const newTemplate = {
                cityName: $scope.cityName,
                statePk: id || '',
            };

            $http({
                method: 'POST',
                url: 'stateCtrl/addCity',
                data: JSON.stringify(newTemplate),
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(function successCallback(response) {
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
                Swal.fire({
                    title: 'Error',
                    text: 'Something went wrong!',
                    icon: 'error',
                    confirmButtonText: 'OK',
                });
            });
        }
    };

    $http.get('stateCtrl/stateByCity/' + id)
        .then(function (response) {
            if (response.data.status) {
                $scope.getStateName = response.data.stateName;
                $scope.viewCityData = response.data.data;
            } else {
                $scope.getStateName = response.data.stateName;
                Swal.fire('Error', 'No data found for City', 'error');
            }
        }, function (error) {
            Swal.fire('Error', 'Server error', 'error');
        });

    $scope.handleAction = function (primaryKey, actionType) {
        const endpoint = actionType === 'like' ? 'stateCtrl/getApprovedCity' : 'stateCtrl/getDisApprovedCity';
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
                    $http.get('stateCtrl/stateByCity/' + id)
                        .then(function (response) {
                            if (response.data.status) {
                                $scope.getStateName = response.data.stateName;
                                $scope.viewCityData = response.data.data;
                            } else {
                                $scope.getStateName = response.data.stateName;
                                Swal.fire('Error', 'No data found for City', 'error');
                            }
                        }, function (error) {
                            Swal.fire('Error', 'Server error', 'error');
                        });
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

    $scope.clearForm = function () {
        $scope.stateName = '';
        $scope.isEdit = false;
        $scope.editPrimaryKey = null;
    };

    $scope.updateCity = function (primaryKey) {
        const selected = $scope.viewCityData.find(item => item.primaryKey === primaryKey);
        if (selected) {
            $scope.cityName = selected.cityName;
            $scope.isEdit = true;
            $scope.editPrimaryKey = selected.primaryKey;
            $('#addCity').modal('show');
        }
    };

    $('#addCity').modal('hide');
    $scope.clearForm();

}]);
