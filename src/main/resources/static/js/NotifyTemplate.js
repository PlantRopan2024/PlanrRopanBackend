var app = angular.module('viewNotification', ['ui.bootstrap', 'ui.router']);


app.directive('ckeditor', function () {
    return {
        require: '?ngModel',
        link: function (scope, element, attrs, ngModel) {
            var ck = CKEDITOR.replace(element[0]);

            if (!ngModel) return;

            ck.on('instanceReady', function () {
                ck.setData(ngModel.$viewValue || '');
            });

            function updateModel() {
                scope.$applyAsync(function () {
                    ngModel.$setViewValue(ck.getData());
                });
            }

            ck.on('change', updateModel);
            ck.on('key', updateModel);
            ck.on('dataReady', updateModel);

            ngModel.$render = function () {
                ck.setData(ngModel.$viewValue || '');
            };
        }
    };
});


app.controller('viewNotificationCtrl', ['$scope', '$sce', '$state', '$stateParams', '$http', function ($scope, $sce, $state, $stateParams, $http) {

    $scope.viewNotifyData = [];
    $scope.selectedMessage = '';

    $scope.submitTemplateNotify = function () {
        if ($scope.isEdit) {
            const updatedTemplate = {
                primarykey: $scope.editPrimaryKey,
                title: $scope.title,
                messageText: $scope.messageText.replace(/<\/?p>/g, '')
            };
            $http({
                method: 'POST',
                url: 'notifyTemplateCtrl/updateNotifyTemplate',
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
                title: $scope.title,
                messageText: $scope.messageText.replace(/<\/?p>/g, '') // optional cleaner
            };

            $http({
                method: 'POST',
                url: 'notifyTemplateCtrl/addNotifyTemplate',
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


    $scope.viewNotifyTemplate = function () {
        $http({
            method: 'GET',
            url: 'notifyTemplateCtrl/ViewNotifyTemplate'
        }).then(function (response) {
            // Handle success response
            if (response.data.status) {
                $scope.viewNotifyData = response.data.data;
            } else {
                $scope.viewNotifyData = [];
            }
        }, function (error) {
            // Handle error response
            $scope.viewNotifyData = [];
        });
    };

    $scope.handleAction = function (primaryKey, actionType) {
        const endpoint = actionType === 'like' ? 'notifyTemplateCtrl/getApprovedNotifyTemplate' : 'notifyTemplateCtrl/getDisApprovedNotifyTemplate';
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
                    $scope.viewNotifyTemplate();
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
        $scope.title = '';
        $scope.messageText = '';
        $scope.isEdit = false;
        $scope.editPrimaryKey = null;
    };

    $scope.updateTemplateNotify = function (primaryKey) {
        const selected = $scope.viewNotifyData.find(item => item.primarykey === primaryKey);
        if (selected) {
            $scope.title = selected.title;
            $scope.messageText = selected.messageText;
            $scope.isEdit = true;
            $scope.editPrimaryKey = selected.primarykey;
            $('#sendNotification').modal('show');
        }
    };

    $('#sendNotification').modal('hide');
    $scope.clearForm();

    $scope.viewNotifyTemplate();

}]);