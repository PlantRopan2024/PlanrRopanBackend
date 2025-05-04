var app = angular.module('PlantRopanApp', ['ngRoute']);

// Configure routes using $routeProvider
app.config(function($routeProvider) {
    $routeProvider
		.state('viewPlans', {
			  url: '/viewPlans/:id',
		      templateUrl: 'templates/viewPlans.html',
			  controller: 'viewPlansCont'
		})
        // Optionally add other routes as needed
        .otherwise({
            redirectTo: '/'
        });
});