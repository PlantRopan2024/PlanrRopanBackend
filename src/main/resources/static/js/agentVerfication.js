var app = angular.module('agentVerfication', ['ui.bootstrap']);

app.controller('agentController', ['$scope', '$http', '$window', function($scope, $http, $window) {

	$scope.pendingVerification = [];
	$scope.agentDetails = {};
	
	$scope.openAgentDetailPage = function(pk) {
		
		$http({
			method: 'POST',
			url: 'findDetailAgent',
			data: pk
		}).then(function(response) {
			console.log("response: " + response.data);
			$scope.agentDetails = response.data;

			// Once the response is received, redirect the user to the new page
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

	// Automatically load data when the controller is initialized
	$scope.verificationPending();

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
			displayData(response);
		}
	});
}

function displayData(data) {
	  document.getElementById('firstName').textContent = data.firstName;
	  document.getElementById('lastName').textContent = data.lastName;
	  document.getElementById('emailId').textContent = data.emailId;
	  document.getElementById('mobileNumber').textContent = data.mobileNumber;	
	  document.getElementById('agentVerified').textContent = data.agentVerified;
	  document.getElementById('activeAgent').textContent = data.activeAgent;
	  document.getElementById('selfiImage').textContent = data.selfiImage;
	  document.getElementById('state').textContent = data.state;
	  document.getElementById('city').textContent = data.city;
	  document.getElementById('address').textContent = data.address;
	  document.getElementById('aadharImg').textContent = data.aadharImg;
	  document.getElementById('aadhaarNumber').textContent = data.aadhaarNumber;
	  document.getElementById('accHolderName').textContent = data.accHolderName;
	  document.getElementById('accNumber').textContent = data.accNumber;
	  document.getElementById('bankName').textContent = data.bankName;
	  document.getElementById('bankAccPassBookImage').textContent = data.bankAccPassBookImage;
	  document.getElementById('accMobNumber').textContent = data.accMobNumber;
	  document.getElementById('ifsccode').textContent = data.ifsccode; 
}

window.onload = displayAgentPKRecord;

function goBack() {
	window.history.back();
}