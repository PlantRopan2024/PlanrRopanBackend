var app = angular.module('addOffers', ['ui.bootstrap']);
app.controller('addOffersController', ['$scope', '$http', function($scope, $http) {
    console.log("AngularJS controller initialized");
	
	$scope.currentDate = new Date().toISOString().split('T')[0];
	$scope.offersAgents = [];
	$scope.offersCus = [];
	
	$scope.selectedOffer = {}; // To store the selected offer details


    // Function to handle form submission
    $scope.submitOffers = function() {
        console.log("Submitting form...");
		
		var formattedValidity = new Date($scope.offer.validity).toISOString().split('T')[0];
        // Create a FormData object to send data as multipart
        var formData = new FormData();
        formData.append('title', $scope.offer.title);
        formData.append('description', $scope.offer.description);
        formData.append('validity',formattedValidity);
        formData.append('discount', $scope.offer.discount); 
        formData.append('conditions', $scope.offer.conditions);
        formData.append('TypeID', $scope.offer.TypeID);
        formData.append('isNewActive', $scope.offer.isNewActive);
        $http({
            method: 'POST',
            url: 'offerApi/addOffer',
            data: formData,
            headers: { 'Content-Type': undefined  }, 
            transformRequest: angular.identity 
        }).then(function successCallback(response) {
            if (response.data.message === 'Offer Added Successfully') {
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
	
	$scope.getOfferAgents = function() {
	    $http({
	        method: 'GET',
	        url: 'offerApi/getoffersAgents'
	    }).then(function(response) {
	        if (response.data && response.data.Offer) {
	            $scope.offersAgents = response.data.Offer; 
	        } else {
	            console.error("Invalid response format:", response.data);
	            $scope.offersAgents = []; 
	        }
	    }, function(error) {
	        console.error("Error occurred:", error);
	        $scope.offersAgents = []; 
	    });
	};

		
	$scope.getOfferCustomer = function() {
	    $http({
	        method: 'GET',
	        url: 'offerApi/getoffersCus'
	    }).then(function(response) {
	        if (response.data && response.data.Offer) {
	            $scope.offersCus = response.data.Offer; 
	        } else {
	            $scope.offersCus = [];
	        }
	    }, function(error) {
	        console.error("Error occurred:", error);
	        $scope.offersCus = []; 
	    });
	};
	
	$scope.openOfferModal = function(offer) {
		    $scope.selectedOffer = offer; // Set the selected offer details
	};
	
	$scope.handleAction = function(primaryKey, actionType) {
		console.log(" primaryKe  offers sy --- "  + primaryKey);
		    const endpoint = actionType === 'like' ? 'offerApi/offersAgentVsCusApproved' : 'offerApi/offersAgentVsCusDisApprovedAgent';
			
		    $http.post(endpoint, { primaryKey: primaryKey }).then(
		        function(response) {
		            // Handle the success response
  					console.log(`Agent ${actionType}d successfully`, response);		            
  					Swal.fire({
		                icon: 'success',
		                title: 'Success',
		                text: `Agent ${actionType}d successfully!`,
		                timer: 2000,
		            });

		            // Optionally refresh the data
					$scope.getOfferAgents();
					$scope.getOfferCustomer();
		        },
		        function(error) {
		            // Handle errors
		            console.error('Error:', error);
		            Swal.fire({
		                icon: 'error',
		                title: 'Error',
		                text: `Failed to ${actionType} the agent. Please try again.`,
		            });
		        }
		    );
		};

	$scope.getOfferAgents();
	$scope.getOfferCustomer();
		
}]);