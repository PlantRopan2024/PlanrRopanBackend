var app = angular.module('UploadimageController', []);

app.controller('UploadController', ['$scope', '$http', function($scope, $http) {
    $scope.imageArray = [];
    $scope.isModalOpen = false;
    $scope.modalImageUrl = '';

    // Fetch uploaded images
	$scope.getImages = function() {
	    $http.get('getImagelist')
	        .then(function(response) {
	            if (response.data && response.data.length > 0) {
	                $scope.imageArray = response.data.map(imgBase64 => 'data:image/jpeg;base64,' + imgBase64);
	            } else {
	                console.warn("No images available.");
	                $scope.imageArray = [];
	            }
	        }, function(error) {
	            console.error("Error fetching images:", error);
	        });
	};


    // Upload file
	$scope.uploadFile = function(files) {
	    if (!files || files.length === 0) {
	        swal("Warning", "No file selected!", "error");
	        return;
	    }

	    const file = files[0];

	    // Basic validation (e.g., file size limit: 5MB, allowed types: JPEG/PNG)
	   /* if (file.size > 5 * 1024 * 1024) {
	        swal("Warning", "File size exceeds 5MB limit!", "error");
	        return;
	    }*/
	   /* if (!['image/jpeg', 'image/png'].includes(file.type)) {
	        swal("Error", "Only JPEG and PNG files are allowed!", "error");
	        return;
	    }*/

	    let formData = new FormData();
	    formData.append('file', file);

	    $http.post('Uploadimage', formData, {
	        headers: { 'Content-Type': undefined }
	    }).then(function(response) {
	        swal("Success", "File uploaded successfully!", "success");
	        $scope.getImages(); // Refresh the image list after successful upload
	    }).catch(function(error) {
	        const errorMessage = error.data && error.data.message ? error.data.message : "Failed to upload file!";
	        swal("Error", errorMessage, "error");
	    });
	};


    // Open modal to preview image
    $scope.openModal = function(imageUrl) {
        $scope.modalImageUrl = imageUrl;
        $scope.isModalOpen = true;
    };

    // Close modal
    $scope.closeModal = function(event) {
        const target = event.target;
        if (target.classList.contains('modal') || target.classList.contains('close')) {
            $scope.isModalOpen = false;
        }
    };

    // Initialize images
    $scope.getImages();
}]);
	