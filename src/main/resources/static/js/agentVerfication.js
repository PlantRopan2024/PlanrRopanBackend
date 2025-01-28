var app = angular.module('agentVerfication', ['ui.bootstrap']);

app.controller('agentController', ['$scope', '$http', '$window', function($scope, $http, $window) {

	$scope.pendingVerification = [];
	$scope.ApprovedAgent =[];
	$scope.agentDetails = {};
	$scope.modalImageUrl = "";
	$scope.isModalOpen = false;
	$scope.selfieImg = '';
	$scope.aadharImgFrontSide ='';
	$scope.aadharImgBackSide ='';
	$scope.bankPassBookImage ='';
	
	$scope.seeImageDisplayId = function(data){
		
		console.log("agent ===="+ data.agentIDPk);
	}
	$scope.displayData = function(data) {
	    if (!data) {
	        console.error("No data found in the response");
	        return;
	    }

	    // Image base64 data
	    $scope.selfieImg = `data:image/jpeg;base64,${data.selfieImg_imageData}`;
		console.log("selfieImg  ---  " + $scope.selfieImg)
	    $scope.aadharImgFrontSide = `data:image/jpeg;base64,${data.aadharImgFrontSideimageData}`;
	    $scope.aadharImgBackSide = `data:image/jpeg;base64,${data.aadharImgBackSideimageData}`;
	    $scope.bankPassBookImage = `data:image/jpeg;base64,${data.bankPassBookImageImageData}`;

	    // Assign other data values directly to $scope for AngularJS binding
	    $scope.firstName = data.firstName;
	    $scope.lastName = data.lastName;
	    $scope.emailId = data.emailId;
	    $scope.mobileNumber = data.mobileNumber;
	    $scope.agentApproved = data.agentApproved;
	    $scope.activeAgent = data.activeAgent;
	    $scope.state = data.state;
	    $scope.city = data.city;
	    $scope.address = data.address;
	    $scope.aadhaarNumber = data.aadhaarNumber;
	    $scope.accHolderName = data.accHolderName;
	    $scope.accNumber = data.accNumber;
	    $scope.bankName = data.bankName;
	    $scope.accMobNumber = data.accMobNumber;
	    $scope.ifsccode = data.ifsccode;
	};

	
	$scope.openAgentDetailPage = function(pk) {
		$http({
			method: 'POST',
			url: 'findDetailAgent',
			data: pk
		}).then(function(response) {
			console.log("response: " + response.data);
			$scope.agentDetails = response.data;
			var url = 'agentDetailPage.html?pk=' + pk;
			$window.location.href = url;
		}, function(error) {
			// Handle any error that occurs during the request
			console.error("Error: " + error);
		});
	};

	$scope.verificationPending = function() {
		$http({
			method: 'GET',
			url: '/verificationPendingAgent'
		}).then(function(response) {
			$scope.pendingVerification = response.data; // Assign response data to scope variable
		}, function(error) {
			console.error("Error occurred:", error);
		});
	};
	
	$scope.approvedAgentRecord = function() {
			$http({
				method: 'GET',
				url: '/getApprovedAgent'
			}).then(function(response) {
				$scope.ApprovedAgent = response.data; // Assign response data to scope variable
			}, function(error) {
				console.error("Error occurred:", error);
			});
		};
	
	$scope.handleAction = function(agentIDPk, actionType) {
	    const endpoint = actionType === 'like' ? '/approvedAgent' : '/disApprovedAgent';
		
	    $http.post(endpoint, { agentIDPk: agentIDPk }).then(
	        function(response) {
	            // Handle the success response
	            console.log(`Agent ${actionType}d successfully`, response.data);
	            Swal.fire({
	                icon: 'success',
	                title: 'Success',
	                text: `Agent ${actionType}d successfully!`,
	                timer: 2000,
	            });

	            // Optionally refresh the data
				$scope.verificationPending();
				$scope.approvedAgentRecord();
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
	
	// Open Modal
	       $scope.openModal = function (imageUrl) {
	           $scope.modalImageUrl = imageUrl;
	           $scope.isModalOpen = true;
	       };

	       // Close Modal
	       $scope.closeModal = function (event) {
	           // Close only if the target is the modal itself or the close button
	           const target = event.target;
	           if (target.classList.contains("modal") || target.classList.contains("modal-close")) {
	               $scope.isModalOpen = false;
	               $scope.$apply(); // Apply scope changes
	           }
	       };
	
	// Automatically load data when the controller is initialized
	$scope.verificationPending();
	$scope.approvedAgentRecord();

}]);

function getQueryParam(param) {
	const urlParams = new URLSearchParams(window.location.search);
	return urlParams.get(param);
}

function displayAgentPKRecord() {
	const agentIDPk = getQueryParam('agentIDPk');
	console.log("agentIDPk " + agentIDPk);
	$.ajax({
		url: '/findDetailAgent',
		method: 'POST',
		data: agentIDPk,
		contentType: 'application/json',
		processData: false,
		success: function(response) {			        
			        if (response){
			            var scope = angular.element(document.querySelector('[ng-controller="agentController"]')).scope();
			            scope.displayData(response);
						scope.seeImageDisplayId(response);
			            scope.$apply(); 
			        } else {
			            console.error('Invalid response data:', response);
			        }
		}
	});
}

/*function displayData(data) {
	
	$scope.selfieImg = data.selfieImg;
	  document.getElementById('firstName').textContent = data.firstName;
	  document.getElementById('lastName').textContent = data.lastName;
	  document.getElementById('emailId').textContent = data.emailId;
	  document.getElementById('mobileNumber').textContent = data.mobileNumber;	
	  document.getElementById('agentApproved').textContent = data.agentApproved;
	  document.getElementById('activeAgent').textContent = data.activeAgent;
	 // document.getElementById('selfieImg').textContent = data.selfieImg;
	// document.getElementById('selfieImg').src = data.selfieImg;
	  document.getElementById('state').textContent = data.state;
	  document.getElementById('city').textContent = data.city;
	  document.getElementById('address').textContent = data.address;
	  document.getElementById('aadharImgFrontSide').textContent = data.aadharImgFrontSide;
	  document.getElementById('aadharImgBackSide').textContent = data.aadharImgBackSide;
	  document.getElementById('aadhaarNumber').textContent = data.aadhaarNumber;
	  document.getElementById('accHolderName').textContent = data.accHolderName;
	  document.getElementById('accNumber').textContent = data.accNumber;
	  document.getElementById('bankName').textContent = data.bankName;
	  document.getElementById('bankPassBookImage').textContent = data.bankPassBookImage;
	  document.getElementById('accMobNumber').textContent = data.accMobNumber;
	  document.getElementById('ifsccode').textContent = data.ifsccode; 
}*/

window.onload = displayAgentPKRecord;

function goBack() {
	window.history.back();
}