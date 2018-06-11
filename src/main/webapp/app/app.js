(function () {

    var app = angular.module("MovieRamaUi", ["ui.bootstrap", "ui.router", "ngSanitize", "ngTable", "ngCookies",
        "ngCookies", "ngMaterial", "ui.sortable", "ngMessages", "md.time.picker"]);

    /**
     * Main Application Initialization and root scope setup
     */
    app.run(function ($rootScope, $modal, $q) {
        $rootScope.safeApply = function () {
            if (!$rootScope.$$phase)
                $rootScope.$apply();
        };

        /**
         * INFO Message Prompt
         *
         * @param message
         * @param size
         */
        $rootScope.modalInfo = function (message, size) {
            var sz = "sm";
            if (!!size)
                sz = size
            var modalInstance = $modal.open(angular.extend({
                templateUrl: 'common/views/infoModal.html',
                controller: 'MessageInstanceCtrl',
                size: sz,
                resolve: {
                    message: function () {
                        return message;
                    }
                }
            }, Settings.ModalSettings));
            return modalInstance.result;
        };

        /**
         * WARN Confirmation Prompt
         *
         * @param message
         * @param size
         */
        $rootScope.modalWarning = function (message, choice) {
            var deferred = $q.defer();
            var modalInstance = $modal.open(angular.extend({
                templateUrl: 'app/common/views/warningModal.html',
                controller: 'ConfirmInstanceCtrl',
                resolve: {
                    message: function () {
                        return message;
                    },
                    choice: function () {
                        return choice;
                    }
                }
            }));
            modalInstance.result.then(function (res) {
                if (res === 'yes') {
                    deferred.resolve(true);
                } else {
                    deferred.resolve(false);
                }
            });
            return deferred.promise;
        };

        /**
         * ALERT Confirmation Prompt
         *
         * @param message
         * @param size
         */
        $rootScope.modalAlert = function (message, choice) {
            var deferred = $q.defer();
            var modalInstance = $modal.open(angular.extend({
                templateUrl: 'app/common/views/alertModal.html',
                controller: 'ConfirmInstanceCtrl',
                resolve: {
                    message: function () {
                        return message;
                    },
                    choice: function () {
                        return choice;
                    }
                }
            }));
            modalInstance.result.then(function (res) {
                if (res === 'yes') {
                    deferred.resolve(true);
                } else {
                    deferred.resolve(false);
                }
            });
            return deferred.promise;
        };

        /**
         * ERROR Message Prompt
         *
         * @param message
         * @param size
         */
        $rootScope.modalError = function (message, size) {
            var sz = "sm";
            if (!!size)
                sz = size
            var modalInstance = $modal.open(angular.extend({
                templateUrl: 'app/common/views/errorModal.html',
                controller: 'MessageInstanceCtrl',
                size: sz,
                resolve: {
                    message: function () {
                        return message;
                    }
                }
            }));
            return modalInstance.result;
        };
    });

    /**
     * Main Application Configuration upon start
     */
    app.config(["$stateProvider", "$urlRouterProvider", function ($stateProvider, $urlRouterProvider) {
        // Movies Add after login
        $stateProvider.state("movies_add", {
            url: "/movies/add",
            templateUrl: "app/components/movies-add/views/moviesAdd.html",
        });
        // Movies Review (homepage)
        $stateProvider.state("movies_review", {
            url: "/movies",
            templateUrl: "app/components/movies-review/views/moviesReview.html",
        });
        // Movies Review after logout
        $stateProvider.state("logout", {
            url: "/movies",
            templateUrl: "app/components/movies-review/views/moviesReview.html",
            controller: "LogoutCtrl" ,
            // onEnter: function(){
            //     AuthenticationService.Logout()
            // }
        });
        // User Login form
        $stateProvider.state("login", {
            url: "/login",
            templateUrl: "app/components/login/views/login.html",
        });
        // User Registration form
        $stateProvider.state("register", {
            url: "/register",
            templateUrl: "app/components/register/views/register.html",
        });
        // Homepage
        $urlRouterProvider.otherwise("/movies");
    }]);

    run.$inject = ['$rootScope', '$location', '$cookies', '$http'];
    function run($rootScope, $location, $cookies, $http) {
        // keep user logged in after page refresh
        $rootScope.globals = $cookies.getObject('globals') || {};
        if ($rootScope.globals.currentUser) {
            $http.defaults.headers.common['Authorization'] = 'Bearer ' + $rootScope.globals.currentUser.token;
        }

        $rootScope.$on('$locationChangeStart', function (event, next, current) {
            // redirect to login page if not logged in and trying to access a restricted page
            var restrictedPage = $.inArray($location.path(), ['/login', '/register']) === -1;
            var loggedIn = $rootScope.globals.currentUser;
            if (restrictedPage && !loggedIn) {
                $location.path('/login');
            }
        });
    }

    /**
     * Main Application controller
     */
    app.controller("RootController", ["$rootScope", "$scope", "$cookies", "$http", "$mdToast", '$controller',
        function ($rootScope, $scope, $cookies, $http, $mdToast, $controller) {

            $controller('FooterCtrl', {
                $scope: $scope
            });
            $controller('HeaderCtrl', {
                $scope: $scope
            });
            $scope.scrollTop = function () {
                window.scrollTo(0, 0);
            };

            $scope.$on('$stateChangeSuccess', function (ev, to, toParams, from) {
                $scope.previousState = from.name;
                $scope.currentState = to.name;
            });

            $scope.initHeader();
            $scope.initFooter();
        }]
    );
}());

function htmlbodyHeightUpdate() {
    var height3 = $(window).height();
    var height1 = $('.nav').height() + 50;
    height2 = $('.main').height();
    if (height2 > height3) {
        $('html').height(Math.max(height1, height3, height2) + 10);
        $('body').height(Math.max(height1, height3, height2) + 10);
    } else {
        $('html').height(Math.max(height1, height3, height2));
        $('body').height(Math.max(height1, height3, height2));
    }
}

/**
 * Main Application
 */
$(document).ready(function () {
    htmlbodyHeightUpdate();
    $(window).resize(function () {
        htmlbodyHeightUpdate();
    });
    $(window).scroll(function () {
        height2 = $('.main').height();
        htmlbodyHeightUpdate();
    });
    moment.fn.toJsDateWithTimezone = function () {
        return new Date(this.year(), this.month(), this.date(), this.hour(), this.minute(), this.second(), this.millisecond());
    };
});
