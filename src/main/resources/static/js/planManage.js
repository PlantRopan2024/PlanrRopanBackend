var app = angular.module('planManage', ['ui.bootstrap']);

app.controller('planManageController', ['$scope', '$http', function($scope, $http) {
    console.log("AngularJS controller initialized");
	
	
	$scope.MontheRecord = [];
	$scope.DailyRecord = [];
	
    // Function to handle form submission
    $scope.submitPlan = function() {
        console.log("Submitting form...");

        // Create a FormData object to send data as multipart
        var formData = new FormData();
        formData.append('plansRs', $scope.plan.plansRs);
        formData.append('timeDuration', $scope.plan.timeDuration);
        formData.append('UptoPots', $scope.plan.UptoPots);
        formData.append('servicesName', $scope.plan.servicesName); 
        formData.append('planType', $scope.plan.planType);
        formData.append('planPacks', $scope.plan.planPacks);
        formData.append('isActive', $scope.plan.isActive);
  
        $http({
            method: 'POST',
            url: '/ShowPlans/addPlans',
            data: formData,
            headers: { 'Content-Type': undefined  }, 
            transformRequest: angular.identity 
        }).then(function successCallback(response) {
            if (response.data.message === 'Plans Add Successfully') {
                Swal.fire({
                    title: response.data.message,
                    text: 'Thanks!!',
                    icon: 'success',
                    confirmButtonText: 'OK',
                });
            } else {
                Swal.fire({
                    title: response.data.message,
                    text: 'Thanks!!',
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
      
    };
    
    $scope.DailyRecordFetch = function() {
		$http({
			method: 'GET',
			url: '/ShowPlans/dailyRecordFetch'
		}).then(function(response) {
			$scope.DailyRecord = response.data; // Assign response data to scope variable
		}, function(error) {
			console.error("Error occurred:", error);
		});
	};
	
    $scope.MonthlyRecordFetch = function() {
		$http({
			method: 'GET',
			url: '/ShowPlans/monthWiseRecordFetch'
		}).then(function(response) {
			$scope.MontheRecord = response.data; // Assign response data to scope variable
		}, function(error) {
			console.error("Error occurred:", error);
		});
	};

	// Automatically load data when the controller is initialized
	$scope.MonthlyRecordFetch();
	$scope.DailyRecordFetch();
}]);
