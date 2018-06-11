(function () {
	"use strict";
	angular
		.module("MovieRamaUi")
		.controller("MoviesReviewCtrl", ["$scope", "$http", "$mdToast", "$cookies", MoviesReviewCtrl]);

	function MoviesReviewCtrl($scope, $http, $mdToast, $cookies) {
		var ctrl = this;
		var listMoviesUrl = "http://" + backend_ip + ":" + backend_port + "/" + backend_context_path + "/movies";
        var voteMoviesUrl = "http://" + backend_ip + ":" + backend_port + "/" + backend_context_path + "/votes";

        ctrl.init = function () {
            $scope.setToolbarTitle("Movies");
            ctrl.listMovies();
        }

		ctrl.listMovies = function () {
			$http({
				url: listMoviesUrl
			}).then(function successCallback(response) {
				$scope.model = {data: response.data};
			}, function errorCallback(response) {
				$scope.model = {data: response.data};
			});
		}

		ctrl.sortLikes = function () {
			$scope.model.data.sort(function (a, b) {
				var likesA = a && a.likes ? a.likes : 0;
				var likesB = b && b.likes ? b.likes : 0;
				if (likesA < likesB)
					return -1;
				if (likesB < likesA)
					return 1;
				return 0;
			});
		}

        ctrl.sortHates = function () {
            $scope.model.data.sort(function (a, b) {
                var hatesA = a && a.hates ? a.hates : 0;
                var hatesB = b && b.hates ? b.hates : 0;
                if (hatesA < hatesB)
                    return -1;
                if (hatesB < hatesA)
                    return 1;
                return 0;
            });
        }

        ctrl.sortDates = function () {
            $scope.model.data.sort(function (a, b) {
                var dateA = a && a.publicationDate ? a.publicationDate : 0;
                var dateB = b && b.publicationDate ? b.publicationDate : 0;
                if (dateA < dateB)
                    return -1;
                if (dateB < dateA)
                    return 1;
                return 0;
            });
        }

        ctrl.like = function (item) {
            $scope.modalWarning("Are you sure you like '" + item.title + "' ?", "LIKE")
                .then(function (response) {
                    if (response === true) {

                    	var headers = {
                            "Content-Type": "application/json",
							"Authorization": "Bearer " +  $cookies
						}
                    	var body = {
                            "like": true,
                            "title": item.title
						}
                        $http.post(voteMoviesUrl, body, { headers: headers })
                            .then(function successCallback(response) {
                                console.log("INFO:" + response.data);
                                $scope.createToast(response.data.result + "! " + response.data.description)
                                $scope.scrollTop();
                            }, function errorCallback(response) {
                                console.log("ERROR: " + response.data);
                                $scope.createToast(response.data.result + "! " + response.data.description)
                                $scope.scrollTop();
                            });
                    }
                });
        }

        ctrl.hate = function (item) {
            $scope.modalAlert("Are you sure you hate '" + item.title + "' ?", "HATE")
                .then(function (response) {
                    if (response === true) {

                        var headers = {
                            "Content-Type": "application/json",
                            "Authorization": "Bearer " +  $cookies
                        }
                        var body = {
                            "like": false,
                            "title": item.title
                        }
                        $http.post(voteMoviesUrl, body, { headers: headers })
                            .then(function successCallback(response) {
                                console.log("INFO:" + response.data);
                                $scope.createToast(response.data.result + "! " + response.data.description)
                                $scope.scrollTop();
                            }, function errorCallback(response) {
                                console.log("ERROR: " + response.data);
                                $scope.createToast(response.data.result + "! " + response.data.description)
                                $scope.scrollTop();
                            });
                    }
                });
        }

        $scope.createToast = function(message) {
            $mdToast.show(
                $mdToast.simple()
                    .textContent(message)
                    .parent(document.querySelectorAll('#toaster'))
                    .hideDelay(5000)
                    .action('x')
                    .capsule(true)
            );
        };
	}
}());