var app = angular.module('addOffers', ['ui.bootstrap']);
app.controller('addOffersController', ['$scope', '$http', function($scope, $http) {
    console.log("AngularJS controller initialized");
	
	
    // Function to handle form submission
    $scope.submitOffers = function() {
        console.log("Submitting form...");

        // Create a FormData object to send data as multipart
        var formData = new FormData();
        formData.append('title', $scope.offer.title);
        formData.append('description', $scope.offer.description);
        formData.append('validity', $scope.offer.validity);
        formData.append('discount', $scope.offer.discount); 
        formData.append('conditions', $scope.offer.conditions);
        formData.append('TypeID', $scope.offer.TypeID);
        formData.append('isActive', $scope.offer.isActive);
   console.log(isActive);
        $http({
            method: 'POST',
            url: '/offerApi/addOffer',
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
    }]);