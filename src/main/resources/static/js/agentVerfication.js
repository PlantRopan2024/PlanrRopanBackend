var app = angular.module('agentVerfication', ['ui.bootstrap']);

app.controller('agentController', ['$scope', '$http', '$window', '$timeout', function ($scope, $http, $window, $timeout) {

	$scope.pendingVerification = [];
	$scope.ApprovedAgent = [];
	$scope.agentDetails = {};
	$scope.modalImageUrl = "";
	$scope.isModalOpen = false;
	$scope.selfieImg = '';
	$scope.aadharImgFrontSide = '';
	$scope.aadharImgBackSide = '';
	$scope.bankPassBookImage = '';

	$scope.seeImageDisplayId = function (folderName, fileName, imageType) {
		$http({
			method: 'GET',
			url: 'downloadFile/' + encodeURIComponent(folderName) + '/' + encodeURIComponent(fileName),
			responseType: 'blob'
		}).then(function (response) {
			var blob = new Blob([response.data], { type: response.headers('Content-Type') });
			var url = URL.createObjectURL(blob);
			$timeout(function () {
				if (imageType === 'selfie') {
					$scope.selfieImg = url;
				} else if (imageType === 'aadharFront') {
					$scope.aadharImagFrontSidePath = url;
				} else if (imageType === 'aadharBack') {
					$scope.aadharImagBackSidePath = url;
				} else if (imageType === 'bankPassBook') {
					$scope.bankPassBookImagePath = url;
				}
			});
		});
	};
	$scope.displayData = function (data) {
		if (!data) {
			console.error("No data found in the response");
			return;
		}
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
		$scope.ifsccode = data.ifscCode;
	};


	$scope.openAgentDetailPage = function (pk) {
		$http({
			method: 'POST',
			url: 'findDetailAgent',
			data: pk
		}).then(function (response) {
			console.log("response: " + response.data);
			$scope.agentDetails = response.data;
			var url = 'agentDetailPage.html?pk=' + pk;
			$window.location.href = url;
		}, function (error) {
			// Handle any error that occurs during the request
			console.error("Error: " + error);
		});
	};

	$scope.verificationPending = function () {
		$http({
			method: 'GET',
			url: 'verificationPendingAgent'
		}).then(function (response) {
			$scope.pendingVerification = response.data; // Assign response data to scope variable
		}, function (error) {
			console.error("Error occurred:", error);
		});
	};

	$scope.approvedAgentRecord = function () {
		$http({
			method: 'GET',
			url: 'getApprovedAgent'
		}).then(function (response) {
			$scope.ApprovedAgent = response.data; // Assign response data to scope variable
		}, function (error) {
			console.error("Error occurred:", error);
		});
	};

	$scope.handleAction = function (agentIDPk, actionType) {
		const endpoint = actionType === 'like' ? 'approvedAgent' : 'disApprovedAgent';

		$http.post(endpoint, { agentIDPk: agentIDPk }).then(
			function (response) {
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
			function (error) {
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
		const target = event.target;
		if (target.classList.contains("modal") || target.classList.contains("modal-close")) {
			$scope.isModalOpen = false;
			$scope.$evalAsync(); // Ensures changes are applied in the next digest cycle
		}
	};

	$scope.verificationPending();
	$scope.approvedAgentRecord();


	$scope.downloadExcelFile = function (action) {
		const downloadUrl = 'downloadExcelPenVsApp?action=' + action;
		const fileName = action + '_agent_data.xlsx';

		const link = document.createElement('a');
		link.href = downloadUrl;
		link.download = fileName;
		document.body.appendChild(link);
		link.click();
		document.body.removeChild(link);
	};

	$scope.downloadExcel = function (agentId) {
		const downloadUrl = 'downloadExcel/' + agentId;

		const link = document.createElement('a');
		link.href = downloadUrl;
		link.download = 'agent_data.xlsx';
		document.body.appendChild(link);
		link.click();
		document.body.removeChild(link);
	};

}]);

function getQueryParam(param) {
	const urlParams = new URLSearchParams(window.location.search);
	return urlParams.get(param);
}

function displayAgentPKRecord() {
	const agentIDPk = getQueryParam('agentIDPk');
	console.log("agentIDPk " + agentIDPk);
	$.ajax({
		url: 'findDetailAgent',
		method: 'POST',
		data: agentIDPk,
		contentType: 'application/json',
		processData: false,
		success: function (response) {
			if (response) {
				var scope = angular.element(document.querySelector('[ng-controller="agentController"]')).scope();
				scope.displayData(response);
				scope.seeImageDisplayId(response.mobileNumber, response.selfieImg, 'selfie');
				scope.seeImageDisplayId(response.mobileNumber, response.aadharImgFrontSide, 'aadharFront');
				scope.seeImageDisplayId(response.mobileNumber, response.aadharImgBackSide, 'aadharBack');
				scope.seeImageDisplayId(response.mobileNumber, response.bankPassBookImage, 'bankPassBook'); // Bank Passbook Image
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