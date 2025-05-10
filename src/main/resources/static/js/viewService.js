var app = angular.module('viewService', ['ui.bootstrap', 'ui.router']);

app.config(function ($stateProvider, $urlRouterProvider, $locationProvider) {
    $locationProvider.hashPrefix('!');

    $urlRouterProvider.otherwise('/viewService');

    $stateProvider
        .state('viewPlans', {
            url: "/viewPlans/:id",
            templateUrl: "viewPlans",
            controller: "viewPlansCont"
        })
        .state('viewService', {
            url: "/",
            templateUrl: "viewService",
            controller: "viewServiceCont"
        });
});

app.controller('viewServiceCont', ['$scope', '$state', '$stateParams', '$http', function ($scope, $state, $stateParams, $http) {

    $scope.viewServiceData = [];
    $scope.showServiceSection = true;

    $scope.imageSrc = null;
    $scope.modalImageUrl = "";
    $scope.isModalOpen = false;
    $scope.serviceImageShow = '';


    $scope.previewImage = function (input) {
        if (input.files && input.files[0]) {
            const reader = new FileReader();
            reader.onload = function (e) {
                $scope.$apply(function () {
                    $scope.imageSrc = e.target.result;
                });
            };
            reader.readAsDataURL(input.files[0]);
            $scope.plan.imageFile = input.files[0];
        }
    };

    $scope.submitServiceName = function () {
        var formData = new FormData();

        var data = {
            serviceName: $scope.serviceName
        };
        formData.append('serviceName', $scope.serviceName);

        // Sending POST request to add service name
        $http({
            method: 'POST',
            url: 'serviceNameCont/addServiceName',
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

    $scope.viewServiceName = function () {
        $http({
            method: 'GET',
            url: 'serviceNameCont/viewServiceName'
        }).then(function (response) {
            // Handle success response
            if (response.data.status) {
                $scope.viewServiceData = response.data.data;
            } else {
                $scope.viewServiceData = [];
            }
        }, function (error) {
            // Handle error response
            $scope.viewServiceData = [];
        });
    };

    $scope.getServiceByPlan = function (servicePk) {
        $scope.showServiceSection = false;
        $state.go('viewPlans', { id: servicePk });
    };

    $scope.handleAction = function (primaryKey, actionType) {
        const endpoint = actionType === 'like' ? 'serviceNameCont/getApprovedServiceName' : 'serviceNameCont/getDisApprovedServiceName';
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
                    $scope.viewServiceName(); // Refresh table/data
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

    $scope.viewServiceName();
}]);


app.controller('viewPlansCont', ['$scope', '$state', '$stateParams', '$timeout', '$http', function ($scope, $state, $stateParams, $timeout, $http) {

    var id = $stateParams.id;
    $scope.viewPlansData = [];
    $scope.serviceNamePlans = [];
    $scope.includingServiceMainData = [];
    $scope.selectedIncludingServices = [];
    $scope.viewPlansPkByData = [];
    $scope.plan = {
        includingServicesList: []
    };
    $scope.imageSrc = null;
    $scope.modalImageUrl = "";
    $scope.isModalOpen = false;
    $scope.planImageShow = '';


    $scope.previewImage = function (input) {
        if (input.files && input.files[0]) {
            const reader = new FileReader();
            reader.onload = function (e) {
                $scope.$apply(function () {
                    $scope.imageSrc = e.target.result;
                });
            };
            reader.readAsDataURL(input.files[0]);
            $scope.plan.imageFile = input.files[0];
        }
    };

    $scope.addIncludingServices = function () {
        if (!$scope.selectedIncludingServices || $scope.selectedIncludingServices.length === 0) return;

        $scope.selectedIncludingServices.forEach(function (service) {
            var alreadyExists = $scope.plan.includingServicesList.some(function (item) {
                return item.primaryKey === service.primaryKey;
            });

            if (!alreadyExists) {
                $scope.plan.includingServicesList.push(angular.copy(service));
            }
        });

        $scope.selectedIncludingServices = [];
    };

    $scope.removeIncludingService = function (index) {
        $scope.plan.includingServicesList.splice(index, 1);
    };

    $http.get('serviceNameCont/serviceByPlanPk/' + id)
        .then(function (response) {
            if (response.data.status) {
                $scope.serviceNamePlans = response.data.serviceName;
                $scope.viewPlansData = response.data.data;
            } else {
                $scope.serviceNamePlans = response.data.serviceName;
                Swal.fire('Error', 'No data found for plan', 'error');
            }
        }, function (error) {
            Swal.fire('Error', 'Server error', 'error');
        });

    $scope.viewIncludingServiceData = function () {
        $http.get('includingServiceName/getActiveIncludingServiceMain')
            .then(function (response) {
                if (response.data.status) {
                    $scope.includingServiceMainData = response.data.data;
                } else {
                    Swal.fire('Error', 'No data found for plan', 'error');
                }
            }, function (error) {
                Swal.fire('Error', 'Server error', 'error');
            });
    };

    $scope.submitPlans = function () {
        var formData = new FormData();
        var plansObj = {
            plansName: $scope.plan.plansName || '',
            plansRs: $scope.plan.plansRs || '',
            timeDuration: $scope.plan.timeDuration || '',
            UptoPots: $scope.plan.UptoPots || '',
            planType: $scope.plan.planType || '',
            planPacks: $scope.plan.planPacks || '',
            serviceNamePk: id || '',
            includingServicesList: $scope.plan.includingServicesList || []
        };
        formData.append('plans', JSON.stringify(plansObj));

        if ($scope.plan.imageFile) {
            formData.append('planImage', $scope.plan.imageFile);
        }

        $http({
            method: 'POST',
            url: 'serviceNameCont/addPlans',
            data: formData,
            headers: { 'Content-Type': undefined },
            transformRequest: angular.identity
        }).then(function successCallback(response) {
            if (response.data.status === true) {
                Swal.fire({
                    title: response.data.message,
                    text: 'Plan added successfully!',
                    icon: 'success',
                    confirmButtonText: 'OK',
                }).then(() => {
                    location.reload();
                });
            } else {
                Swal.fire('Warning', response.data.message || 'Please try again.', 'warning');
            }
        }, function errorCallback(response) {
            Swal.fire('Error', 'Something went wrong!', 'error');
        });
    };

    $scope.resetForm = function () {
        $scope.plan = {};
        $scope.imageSrc = null;
        $scope.myPlansForm.$setPristine();
        $scope.myPlansForm.$setUntouched();
        document.querySelector('input[type="file"]').value = null;
    };

    $scope.goBack = function () {
        $state.go('viewService');
    };

    $scope.getPlanRecords = function (planPk) {
        $http.get('serviceNameCont/viewPlansByPk/' + planPk)
            .then(function (response) {
                if (response.data.status) {
                    $scope.viewPlansPkByData = response.data.data;
                    $scope.seeImageDisplayId($scope.serviceNamePlans, response.data.data.planImage, 'plan');

                } else {
                    Swal.fire('Error', 'No data found for plan', 'error');
                }
            }, function (error) {
                Swal.fire('Error', 'Server error', 'error');
            });
    };

    // Open Modal
    $scope.openModal = function (imageUrl) {
        $scope.modalImageUrl = imageUrl;
        $scope.isModalOpen = true;
    };

    // Close Modal
    $scope.closeModal = function (event) {
        const target = event.target;
        if (target.classList.contains("modal") || target.classList.contains("modal-close")) {
            $scope.isModalOpen = false;
            $scope.$evalAsync();
        }
    };

    $scope.seeImageDisplayId = function (folderName, fileName, imageType) {
        $http({
            method: 'GET',
            url: 'downloadFile/' + encodeURIComponent(folderName) + '/' + encodeURIComponent(fileName),
            responseType: 'blob'
        }).then(function (response) {
            var blob = new Blob([response.data], { type: response.headers('Content-Type') });
            var url = URL.createObjectURL(blob);
            $timeout(function () {
                if (imageType === 'plan') {
                    $scope.planImageShow = url;
                }
            });
        });
    };


    $scope.handleAction = function (primaryKey, actionType) {
        const endpoint = actionType === 'like' ? 'includingServiceName/getApprovedPlans' : 'includingServiceName/getDisApprovedPlans';
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
                    // $scope.viewIncludingServiceMain(); // Refresh table/data
                    $http.get('serviceNameCont/serviceByPlanPk/' + id)
                        .then(function (response) {
                            if (response.data.status) {
                                $scope.serviceNamePlans = response.data.serviceName;
                                $scope.viewPlansData = response.data.data;
                            } else {
                                $scope.serviceNamePlans = response.data.serviceName;
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

}]);