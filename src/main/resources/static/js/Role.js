var app = angular.module('viewRole', ['ui.bootstrap', 'ui.router']);

app.config(function ($stateProvider, $urlRouterProvider, $locationProvider) {
    $locationProvider.hashPrefix('!');

    $urlRouterProvider.otherwise('/Role');

    $stateProvider
        .state('addUsers', {
            url: "/addUsers/:id",
            templateUrl: "addUsers",
            controller: "viewUsersContr"
        })
        .state('Role', {
            url: "/",
            templateUrl: "Role",
            controller: "viewRoleContr"
        })
        .state('SubRoleBack', {
            url: "/SubRoleBack/:id",
            templateUrl: "SubRole",
            controller: "viewSubRoleContr"
        })
        .state('SubRole', {
            url: "/SubRole/:id",
            templateUrl: "SubRole",
            controller: "viewSubRoleContr"
        });
});

app.controller('viewRoleContr', ['$scope', '$state', '$stateParams', '$http', function ($scope, $state, $stateParams, $http) {

    $scope.viewRoleData = [];
    $scope.showRoleSection = true;

    $scope.submitRoleName = function () {
        var data = {
            roleName: $scope.roleName
        };

        $http({
            method: 'POST',
            url: 'roleCont/addRoleName',
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

    $scope.getRoleBySubROle = function (rolePk) {
        $scope.showRoleSection = false;
        $state.go('SubRole', { id: rolePk });
    };

    $scope.viewRole = function () {
        $http({
            method: 'GET',
            url: 'roleCont/getViewRole'
        }).then(function (response) {
            // Handle success response
            if (response.data.status) {
                $scope.viewRoleData = response.data.data;
            } else {
                $scope.viewRoleData = [];
            }
        }, function (error) {
            // Handle error response
            $scope.viewRoleData = [];
        });
    };

    $scope.handleAction = function (primaryKey, actionType) {
        const endpoint = actionType === 'like' ? 'roleCont/getApprovedRole' : 'roleCont/getDisApprovedRole';
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
                    $scope.viewRole();
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

    $scope.viewRole();
}]);

app.controller('viewSubRoleContr', ['$scope', '$state', '$stateParams', '$http', function ($scope, $state, $stateParams, $http) {

    var rolePk = $stateParams.id;

    $scope.viewRoleData = [];
    $scope.RoleName = [];
    $scope.showSubRoleSection = true;

    $http.get('roleCont/getSubRoleWithRolePk/' + rolePk)
        .then(function (response) {
            if (response.data.status) {
                $scope.RoleName = response.data.RoleName;
                $scope.viewRoleData = response.data.data;
            } else {
                $scope.RoleName = response.data.RoleName;
                Swal.fire('Error', 'No data found for plan', 'error');
            }
        }, function (error) {
            Swal.fire('Error', 'Server error', 'error');
        });

    $scope.submitSubRole = function () {
        var data = {
            subRoleName: $scope.subRoleName,
            rolePk: rolePk
        };

        $http({
            method: 'POST',
            url: 'roleCont/addSubRole',
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

    $scope.getSubRoleByUser = function (rolePk) {
        $scope.showSubRoleSection = false;
        $state.go('addUsers', { id: rolePk });
    };



    $scope.handleAction = function (primaryKey, actionType) {
        const endpoint = actionType === 'like' ? 'roleCont/getApprovedSubRole' : 'roleCont/getDisApprovedSubRole';
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
                    $http.get('roleCont/getSubRoleWithRolePk/' + rolePk)
                    .then(function (response) {
                        if (response.data.status) {
                            $scope.RoleName = response.data.RoleName;
                            $scope.viewRoleData = response.data.data;
                        } else {
                            $scope.RoleName = response.data.RoleName;
                            Swal.fire('Error', 'No data found for plan', 'error');
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

    $scope.goBack = function () {
        $state.go('Role');
    };

}]);


app.controller('viewUsersContr', ['$scope', '$state', '$stateParams', '$http', function ($scope, $state, $stateParams, $http) {

    var subRolepk = $stateParams.id;
    
    $scope.viewUserData = [];
    $scope.getActiveRoleData = [];
    $scope.subRoleName = [];

    $http.get('roleCont/getUserWithSubRolePk/' + subRolepk)
        .then(function (response) {
            if (response.data.status) {
                $scope.subRoleName = response.data.subRoleName;
                $scope.viewUserData = response.data.data;
            } else {
                $scope.subRoleName = response.data.subRoleName;
                Swal.fire('Error', 'No data found for plan', 'error');
            }
        }, function (error) {
            Swal.fire('Error', 'Server error', 'error');
        });

    $scope.submitUsersAdd = function () {
        var data = {
            username: $scope.username,
            employeename: $scope.employeename,
            mobileNumber: $scope.mobileNumber,
            email: $scope.email,
            password: $scope.password,
            subRoleName: $scope.subRoleName
        };

        $http({
            method: 'POST',
            url: 'roleCont/addUserRegistration',
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
        const endpoint = actionType === 'like' ? 'roleCont/getApprovedUser' : 'roleCont/getDisApprovedUser';
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
                    $http.get('roleCont/getUserWithSubRolePk/' + subRolepk)
                        .then(function (response) {
                            if (response.data.status) {
                                $scope.subRoleName = response.data.subRoleName;
                                $scope.viewUserData = response.data.data;
                            } else {
                                $scope.subRoleName = response.data.subRoleName;
                                Swal.fire('Error', 'No data found for plan', 'error');
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

    $scope.goBack = function () {
        $state.go('SubRoleBack', { id: subRolepk });
    };

}]);
