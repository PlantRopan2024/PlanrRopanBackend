var app = angular.module('order', ['ui.bootstrap']);

app.controller('orders', ['$scope', '$http', function($scope, $http) {
   
	
	 console.log("AngularJS controller initialized");
	
	 $scope.pendingOrders = [];
	 $scope.accpetedOrders = [];
	 $scope.completedOrders = [];
	 $scope.rejectedOrders = [];
	
	 
	 $scope.loadPendingOrders = function() {
	     $http({
	         method: 'GET',
	         url: 'orderWebPage/upComingOrders'
	     }).then(function(response) {
	         if (response.data && response.data.success === true) {
	             $scope.pendingOrders = response.data.response.data || [];
	             $scope.pendingMessage = response.data.response.message || '';
	         } else {
	             $scope.pendingOrders = [];
	             $scope.pendingMessage = "Something went wrong or no orders found.";
	         }
	     }, function(error) {
	         console.error("Error occurred:", error);
	         $scope.pendingOrders = [];
	         $scope.pendingMessage = "Failed to fetch data.";
	     });
	 };
	 
	 $scope.loadAccpetedOrders = function() {
	 	 	     $http({
	 	 	         method: 'GET',
	 	 	         url: 'orderWebPage/getListAccpetedOrderList'
	 	 	     }).then(function(response) {
	 	 	         if (response.data && response.data.success === true) {
	 	 	             $scope.accpetedOrders = response.data.response.data || [];
	 	 	             $scope.accpetedMessage = response.data.response.message || '';
	 	 	         } else {
	 	 	             $scope.accpetedOrders = [];
	 	 	             $scope.accpetedMessage = "Something went wrong or no orders found.";
	 	 	         }
	 	 	     }, function(error) {
	 	 	         console.error("Error occurred:", error);
	 	 	         $scope.accpetedOrders = [];
	 	 	         $scope.accpetedMessage = "Failed to fetch data.";
	 	 	     });
	 	 	 };
			 
	 $scope.loadCompletedOrders = function() {
	 	     $http({
	 	         method: 'GET',
	 	         url: 'orderWebPage/getCompletedOrderList'
	 	     }).then(function(response) {
	 	         if (response.data && response.data.success === true) {
	 	             $scope.completedOrders = response.data.response.data || [];
	 	             $scope.completedMessage = response.data.response.message || '';
	 	         } else {
	 	             $scope.completedOrders = [];
	 	             $scope.completedMessage = "Something went wrong or no orders found.";
	 	         }
	 	     }, function(error) {
	 	         console.error("Error occurred:", error);
	 	         $scope.completedOrders = [];
	 	         $scope.completedMessage = "Failed to fetch data.";
	 	     });
	 	 };
		 
		 
		 $scope.loadRejectedOrders = function() {
		 	 	     $http({
		 	 	         method: 'GET',
		 	 	         url: 'orderWebPage/getRejectedOrderList'
		 	 	     }).then(function(response) {
		 	 	         if (response.data && response.data.success === true) {
		 	 	             $scope.rejectedOrders = response.data.response.data || [];
		 	 	             $scope.rejectedMessage = response.data.response.message || '';
		 	 	         } else {
		 	 	             $scope.rejectedOrders = [];
		 	 	             $scope.rejectedMessage = "Something went wrong or no orders found.";
		 	 	         }
		 	 	     }, function(error) {
		 	 	         console.error("Error occurred:", error);
		 	 	         $scope.rejectedOrders = [];
		 	 	         $scope.rejectedMessage = "Failed to fetch data.";
		 	 	     });
		 	 	 };

		
		
		/*$ js onload this method data is coming*/
		$scope.loadPendingOrders();
		$scope.loadAccpetedOrders();
		$scope.loadCompletedOrders();
		$scope.loadRejectedOrders();
	
}]);
