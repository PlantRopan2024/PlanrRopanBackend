var app = angular.module('viewPlans', ['ui.router','ui.bootstrap']);

app.controller('viewPlansContsss', ['$scope', '$stateParams', '$http', function($scope, $stateParams, $http) {
    console.log("viewPlansCont loaded"); // should appear in console

    $scope.viewPlansData = [];
    
    var id = $stateParams.id;
    console.log("Inside viewPlansCont, ID:", id);


    $http.get('serviceNameCont/serviceByPlanPk/' + id)
        .then(function(response) {
            if (response.data.status) {
                $scope.viewPlansData = response.data.data;
            } else {
                Swal.fire('Error', 'No data found for plan', 'error');
            }
        }, function(error) {
            Swal.fire('Error', 'Server error', 'error');
        });
	
}]);