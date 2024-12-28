var app = angular.module('UploadimageController', []);

app.controller('UploadController', ['$scope', '$http', function($scope, $http) {
    $scope.imageArray = [];
    $scope.isModalOpen = false;
    $scope.modalImageUrl = '';

    // Fetch uploaded images
    $scope.getImages = function() {
        $http.get('/getImage')
            .then(function(response) {
                $scope.imageArray = response.data;
            }, function(error) {
                console.error("Error fetching images:", error);
            });
    };

    // Upload file
    $scope.uploadFile = function(files) {
        let formData = new FormData();
        formData.append('file', files[0]);

        $http.post('/Uploadimage', formData, {
            headers: { 'Content-Type': undefined }
        }).then(function(response) {
            swal("Success", response, "success");
            $scope.getImages(); // Refresh the image list
        }).catch(function(error) {
            swal("Error", "Failed to upload file!", "error");
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
	