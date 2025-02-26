var app = angular.module('planManage', ['ui.bootstrap']);

app.controller('planManageController', ['$scope', '$http', function($scope, $http) {
    console.log("AngularJS controller initialized");
	
	
	$scope.MontheRecord = [];
	$scope.DailyRecord = [];
	$scope.allServiceName= [];
	
	$scope.plan = {
	    fertilizers: [
	        { fertilizerName: "Vermi Compost", amount: "" ,Kg:""},
	        { fertilizerName: "Bone Meal", amount: "",Kg:"" },
	        { fertilizerName: "Neem Khali", amount: "",Kg:"" }
	    ]
	};
		
		
	
    // Function to handle form submission
	$scope.submitPlan = function() {
	    console.log("Submitting form...");
		console.log("uptopo" +$scope.plan.UptoPots  )
	    var planData = {
	        servicesName: $scope.plan.servicesName.primaryKey,
	        plansName: $scope.plan.plansName,
	        plansRs: $scope.plan.plansRs,
	        timeDuration: $scope.plan.timeDuration,
	        UptoPots: $scope.plan.UptoPots,
	        includingServicesName: $scope.plan.includingServicesName,
	        planType: $scope.plan.planType,
	        planPacks: $scope.plan.planPacks,
	        isActive: $scope.plan.isActive,
	        fertilizers: $scope.plan.fertilizers
	    };

	    $http({
	        method: 'POST',
	        url: 'ShowPlans/addPlans',
	        data: planData,
	        headers: { 'Content-Type': 'application/json' } 
	    }).then(function successCallback(response) {
	        Swal.fire({
	            title: response.data.message,
	            text: 'Thanks!!',
	            icon: response.data.message === 'Plans Add Successfully' ? 'success' : 'warning',
	            confirmButtonText: 'OK',
	        });
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
			url: 'ShowPlans/dailyRecordFetch'
		}).then(function(response) {
			$scope.DailyRecord = response.data; // Assign response data to scope variable
		}, function(error) {
			console.error("Error occurred:", error);
		});
	};
	
    $scope.MonthlyRecordFetch = function() {
		$http({
			method: 'GET',
			url: 'ShowPlans/monthWiseRecordFetch'
		}).then(function(response) {
			$scope.MontheRecord = response.data; // Assign response data to scope variable
		}, function(error) {
			console.error("Error occurred:", error);
		});
	};
	
	$scope.getAllServiceName = function() {
	    $http({
	        method: 'GET',
	        url: 'ShowPlans/getServiceName'
	    }).then(function(response) {
	        if (response.data && response.data.status === "success") {
	            $scope.allServiceName = response.data.service;
	        } else {
	            console.error("Unexpected response format:", response.data);
	        }
	    }, function(error) {
	        console.error("Error occurred:", error);
	    });
	};

	
	$scope.addFertilizer = function () {
	    $scope.plan.fertilizers.push({ fertilizerName: "", amount: "",Kg:"" });
	};

	$scope.removeFertilizer = function (index) {
	    $scope.plan.fertilizers.splice(index, 1);
	};

	// Automatically load data when the controller is initialized
	$scope.MonthlyRecordFetch();
	$scope.DailyRecordFetch();
	$scope.getAllServiceName();
}]);
