(function () {
	"use strict";
	angular
		.module("MovieRamaUi")
		.controller("MoviesReviewCtrl", ["$scope", "$http", "$state", MoviesReviewCtrl]);

	function MoviesReviewCtrl($scope, $http, $state) {
		var ctrl = this;
        var voteMoviesUrl = "http://" + backend_ip + ":" + backend_port + "/" + backend_context_path + "/votes";
        var listMoviesUrl = "http://" + backend_ip + ":" + backend_port + "/" + backend_context_path + "/movies";

        if ($state.params.publisher !== undefined && $state.params.publisher.id !== undefined) {
            listMoviesUrl = listMoviesUrl + "?userId=" + $state.params.publisher.id;
            $scope.publisher = $state.params.publisher.name
        }

        // Sorting states
        var sortedByLikesDesc = false;
        var sortedByHatesDesc = false;
        var sortedByDatesDesc = false;

        ctrl.sortLikes = function () {
		    if (ctrl.sortedByLikesDesc === undefined || ctrl.sortedByLikesDesc === false) {
                ctrl.sortLikesDesc();
            } else {
                ctrl.sortLikesAsc();
            }
        }

        ctrl.sortHates = function () {
            if (ctrl.sortedByHatesDesc === undefined || ctrl.sortedByHatesDesc === false) {
                ctrl.sortHatesDesc();
            } else {
                ctrl.sortHatesAsc();
            }
        }

        ctrl.sortDates = function () {
            if (ctrl.sortedByDatesDesc === undefined || ctrl.sortedByDatesDesc === false) {
                ctrl.sortDatesDesc();
            } else {
                ctrl.sortDatesAsc();
            }
        }

		ctrl.sortLikesDesc = function () {
            ctrl.sortedByLikesDesc = true;
			$scope.model.data.sort(function (a, b) {
				var likesA = a && a.likes ? a.likes : 0;
				var likesB = b && b.likes ? b.likes : 0;
				if (likesA < likesB)
					return 1;
				if (likesB < likesA)
					return -1;
				return 0;
			});
		}

        ctrl.sortHatesDesc = function () {
            ctrl.sortedByHatesDesc = true;
            $scope.model.data.sort(function (a, b) {
                var hatesA = a && a.hates ? a.hates : 0;
                var hatesB = b && b.hates ? b.hates : 0;
                if (hatesA < hatesB)
                    return 1;
                if (hatesB < hatesA)
                    return -1;
                return 0;
            });
        }

        ctrl.sortDatesDesc = function () {
            ctrl.sortedByDatesDesc = true;
            $scope.model.data.sort(function (a, b) {
                var dateA = a && a.publicationDate ? a.publicationDate : 0;
                var dateB = b && b.publicationDate ? b.publicationDate : 0;
                if (dateA < dateB)
                    return 1;
                if (dateB < dateA)
                    return -1;
                return 0;
            });
        }

        ctrl.sortLikesAsc = function () {
            ctrl.sortedByLikesDesc = false;
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

        ctrl.sortHatesAsc = function () {
            ctrl.sortedByHatesDesc = false;
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

        ctrl.sortDatesAsc = function () {
            ctrl.sortedByDatesDesc = false;
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

        /**
         *  List MovieRama movies!
         */
        ctrl.listMovies = function () {
            $http({
                url: listMoviesUrl
            }).then(function successCallback(response) {
                $scope.model = {data: response.data};
                ctrl.sortLikesDesc();
            }, function errorCallback(response) {
                $scope.model = {data: response.data};
            });
        }

        /**
         *  List MovieRama movies by passed Publisher!
         */
        ctrl.filterByPublisher = function (movie) {
            var publisher = movie.publisher;
            if ($state.params.publisher && publisher.id === $state.params.publisher.id) {
                $state.reload();
            } else {
                $state.go("movies_publisher_review", {
                    publisher: {
                        id: publisher.id,
                        name: publisher.name
                    }
                });
            }
        }

        /**
         *  Vote for a movie!
         *
         * @param item
         */
        ctrl.like = function (item) {
            $scope.modalWarning("Are you sure you like '" + item.title + "' ?", "LIKE")
                .then(function (response) {
                    if (response === true) {

                    	var headers = {
                            "Content-Type": "application/json",
							"Authorization": $http.defaults.headers.common.Authorization // $cookies.global.accessToken
						}
                    	var body = {
                            "like": true,
                            "title": item.title
						}
                        $http.put(voteMoviesUrl, body, { headers: headers })
                            .then(function successCallback(response) {
                                $scope.createToast(response.data.result + "! " + response.data.description)
                                ctrl.listMovies()
                                $scope.scrollTop();
                            }, function errorCallback(response) {
                                $scope.createToast(response.data.result + "! " + response.data.description)
                                $scope.scrollTop();
                            });
                    }
                });
        }

        /**
         *  Vote against a movie!
         *
         * @param item
         */
        ctrl.hate = function (item) {
            $scope.modalAlert("Are you sure you hate '" + item.title + "' ?", "HATE")
                .then(function (response) {
                    if (response === true) {

                        var headers = {
                            "Content-Type": "application/json",
                            "Authorization": $http.defaults.headers.common.Authorization
                        }
                        var body = {
                            "like": false,
                            "title": item.title
                        }
                        $http.put(voteMoviesUrl, body, { headers: headers })
                            .then(function successCallback(response) {
                                console.log("INFO:" + response.data);
                                $scope.createToast(response.data.result + "! " + response.data.description)
                                ctrl.listMovies()
                                $scope.scrollTop();
                            }, function errorCallback(response) {
                                console.log("ERROR: " + response.data);
                                $scope.createToast(response.data.result + "! " + response.data.description)
                                $scope.scrollTop();
                            });
                    }
                });
        }
	}
}());