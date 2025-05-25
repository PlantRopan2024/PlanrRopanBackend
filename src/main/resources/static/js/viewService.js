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

app.controller('viewServiceCont', ['$scope', '$state', '$stateParams', '$timeout', '$http', function ($scope, $state, $stateParams, $timeout, $http) {

    $scope.viewServiceData = [];
    $scope.showServiceSection = true;
    $scope.service = {

    };
    $scope.imageSrc = null;
    $scope.modalImageUrl = "";
    $scope.isModalOpen = false;
    $scope.serviceImageShow = '';

    $scope.clearForm = function () {
        $scope.imageSrc = null;
        $scope.serviceName = '';
        $scope.serviceId = null;
        $scope.isEdit = false;
        $scope.service = { imageFile: null };
    };

    $scope.updateServiceRecord = function (serviceId) {
        $scope.isEdit = true;
        $scope.serviceId = serviceId;

        const service = $scope.viewServiceData.find(s => s.primaryKey === serviceId);
        if (service) {
            $scope.serviceName = service.name;
            $scope.service = { imageFile: null }; // Reset file input

            // Fetch and display image blob
            $scope.seeImageDisplayId(service.name, service.serviceImageName, 'service');
        }

        $('#addServiceName').modal('show');
    };


    $scope.clearForm();
    $('#addServiceName').modal('hide');


    $scope.seeImageDisplayId = function (folderName, fileName, imageType) {
        $http({
            method: 'GET',
            url: 'downloadFile/' + encodeURIComponent(folderName) + '/' + encodeURIComponent(fileName),
            responseType: 'blob'
        }).then(function (response) {
            var blob = new Blob([response.data], { type: response.headers('Content-Type') });
            var url = URL.createObjectURL(blob);

            $timeout(function () {
                if (imageType === 'service') {
                    $scope.imageSrc = url; // ✅ Set preview image
                }
            });
        }, function (error) {
            console.error('Error fetching image:', error);
        });
    };



    $scope.previewImage = function (input) {
        if (input.files && input.files[0]) {
            const originalFile = input.files[0];
            const reader = new FileReader();

            reader.onload = function (e) {
                const img = new Image();
                img.src = e.target.result;

                img.onload = function () {
                    const canvas = document.createElement('canvas');
                    const ctx = canvas.getContext('2d');

                    const size = Math.min(img.width, img.height);
                    const startX = (img.width - size) / 2;
                    const startY = (img.height - size) / 2;

                    canvas.width = 400;
                    canvas.height = 400;

                    ctx.drawImage(img, startX, startY, size, size, 0, 0, 400, 400);

                    // Show preview
                    $scope.$apply(function () {
                        $scope.imageSrc = canvas.toDataURL('image/png');
                    });

                    // Convert canvas to blob (cropped image)
                    canvas.toBlob(function (blob) {
                        // Create a new File from the blob
                        const croppedFile = new File([blob], originalFile.name, {
                            type: 'image/png',
                            lastModified: Date.now()
                        });

                        // Set the cropped file as the one to upload
                        $scope.$apply(function () {
                            $scope.service.imageFile = croppedFile;
                        });
                    }, 'image/png');
                };
            };

            reader.readAsDataURL(originalFile);
        }
    };



    $scope.submitServiceName = function () {
        var formData = new FormData();

        var data = {
            serviceName: $scope.serviceName,
            servicePk: $scope.isEdit ? $scope.serviceId : undefined
        };

        formData.append('services', JSON.stringify(data));

        if ($scope.service.imageFile) {
            formData.append('serviceImage', $scope.service.imageFile);
        }

        var endpoint = $scope.isEdit ? 'serviceNameCont/updateServiceName' : 'serviceNameCont/addServiceName';

        $http({
            method: 'POST',
            url: endpoint,
            data: formData,
            headers: { 'Content-Type': undefined },
            transformRequest: angular.identity
        }).then(function successCallback(response) {
            Swal.fire({
                title: response.data.message,
                text: response.data.status ? 'Thanks!!' : 'Please try again!',
                icon: response.data.status ? 'success' : 'warning',
                confirmButtonText: 'OK',
            }).then(() => {
                if (response.data.status) location.reload();
            });
        }, function errorCallback() {
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

    $scope.updatePlanRecord = function (planId) {
        $scope.isEdit = true;
        $scope.planId = planId;

        const plan = $scope.viewPlansData.find(p => p.primaryKey === planId);
        if (plan) {
            $scope.plan = angular.copy(plan);  // Copy to avoid reference mutation
            $scope.seeImageDisplayId($scope.serviceNamePlans, plan.planImage, 'previousPlan');
            $scope.plan.imageFile = null;  // You can re-upload if needed
            $scope.viewIncludingServiceData();
        }

        $('#addPlans').modal('show');
    };

    $scope.resetForm = function () {
        $scope.plan = {};
        $scope.imageSrc = '';
        $scope.planId = null;
        $scope.isEdit = false;
        $scope.selectedIncludingServices = [];
        //  $scope.myPlansForm.$setPristine();
        // $scope.myPlansForm.$setUntouched();
    };

    $scope.resetForm();
    $('#addPlans').modal('hide');

    $scope.previewImage = function (input) {
        if (input.files && input.files[0]) {
            const originalFile = input.files[0];
            const reader = new FileReader();

            reader.onload = function (e) {
                const img = new Image();
                img.src = e.target.result;

                img.onload = function () {
                    const canvas = document.createElement('canvas');
                    const ctx = canvas.getContext('2d');

                    const size = Math.min(img.width, img.height);
                    const startX = (img.width - size) / 2;
                    const startY = (img.height - size) / 2;

                    canvas.width = 400;
                    canvas.height = 400;

                    ctx.drawImage(img, startX, startY, size, size, 0, 0, 400, 400);

                    // Show preview
                    $scope.$apply(function () {
                        $scope.imageSrc = canvas.toDataURL('image/png');
                    });

                    // Convert canvas to blob (cropped image)
                    canvas.toBlob(function (blob) {
                        // Create a new File from the blob
                        const croppedFile = new File([blob], originalFile.name, {
                            type: 'image/png',
                            lastModified: Date.now()
                        });

                        // Set the cropped file as the one to upload
                        $scope.$apply(function () {
                            $scope.plan.imageFile = croppedFile;
                        });
                    }, 'image/png');
                };
            };

            reader.readAsDataURL(originalFile);
        }
    };

    $scope.addIncludingServices = function () {
        if (!$scope.selectedIncludingServices || $scope.selectedIncludingServices.length === 0) return;

        // Ensure plan and includingServicesList are initialized
        $scope.plan = $scope.plan || {};
        $scope.plan.includingServicesList = $scope.plan.includingServicesList || [];

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

        // Include planId if in edit mode
        if ($scope.isEdit && $scope.planId) {
            console.log(" plan pk  " + $scope.planId + "$scope.isEdit  " + $scope.isEdit);
            plansObj.planKey = $scope.planId;
        }

        formData.append('plans', JSON.stringify(plansObj));

        if ($scope.plan.imageFile) {
            formData.append('planImage', $scope.plan.imageFile);
        }

        const url = $scope.isEdit ? 'serviceNameCont/updatePlans' : 'serviceNameCont/addPlans';

        $http({
            method: 'POST',
            url: url,
            data: formData,
            headers: { 'Content-Type': undefined },
            transformRequest: angular.identity
        }).then(function successCallback(response) {
            if (response.data.status === true) {
                Swal.fire({
                    title: response.data.message,
                    text: $scope.isEdit ? 'Plan updated successfully!' : 'Plan added successfully!',
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


    // $scope.resetForm = function () {
    //     $scope.plan = {};
    //     $scope.imageSrc = null;
    //     $scope.myPlansForm.$setPristine();
    //     $scope.myPlansForm.$setUntouched();
    //     document.querySelector('input[type="file"]').value = null;
    // };

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
                if (imageType === 'previousPlan') {
                    $scope.imageSrc = url;
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