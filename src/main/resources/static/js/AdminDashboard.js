var app = angular.module('adminDashboard', ['ui.bootstrap', 'ui.router']);

app.controller('admminDashboardCont', ['$scope', '$state', '$stateParams', '$http', function ($scope, $state, $stateParams, $http) {



  $scope.payNow = function () {
    // Call backend to create Razorpay order
    $http.post('paymentCont/create-order', {
      amount: 100
    }).then(function (response) {
      var order = response.data;

      var options = {
        "key": "rzp_live_WxJ77QnoxZtxrK", // Replace with Razorpay key ID
        "amount": order.amount, // e.g., 50000
        "currency": "INR",
        "name": "Plant Ropan",
        "description": "Test Transaction",
        "order_id": order.id, // order_id from backend
        "handler": function (response) {
          alert("Payment successful! Payment ID: " + response.razorpay_payment_id);

          // You can also verify the payment on server
          $http.post('paymentCont/verify-payment', response)
            .then(function (res) {
              alert("Payment Verified");
            });
        },
        "prefill": {
          "name": "Plant Ropan",
          "email": "plantRopan@example.com",
          "contact": "8081654589"
        },
        "theme": {
          "color": "#3399cc"
        }
      };

      var rzp = new Razorpay(options);
      rzp.open();
    }, function (error) {
      alert("Failed to initiate payment");
      console.error(error);
    });
  };

}]);