(function () {

    var app = angular.module("MovieRamaUi", ["ui.bootstrap", "ui.router", "ngSanitize", "ngTable",
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
                templateUrl: 'common/directives/infoModal.html',
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
                templateUrl: 'app/common/directives/warningModal.html',
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
                templateUrl: 'app/common/directives/alertModal.html',
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
                templateUrl: 'app/common/directives/errorModal.html',
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
        // Movies Add
        $stateProvider.state("movies_add", {
            url: "/movies/add",
            templateUrl: "app/components/movies-add/views/moviesAdd.html",
        });
        // Movies Review
        $stateProvider.state("movies_review", {
            url: "/movies",
            templateUrl: "app/components/movies-review/views/moviesReview.html",
        });
        // Home page
        $urlRouterProvider.otherwise("/movies");
    }]);

    /**
     * Main Application controller
     */
    app.controller("RootController", ["$rootScope", "$scope", "$cookies", "$http", "$mdToast", '$controller',
        function ($rootScope, $scope, $cookies, $http, $mdToast, $controller) {

            $controller('FooterCtrl', {
                $scope: $scope
            });
            $scope.scrollTop = function () {
                window.scrollTo(0, 0);
            };

            $scope.setToolbarTitle = function(title) {
                var divElement = document.getElementById("pageTitleId")
                for (var i = 0; i < divElement.childNodes.length; ++i) {
                    divElement.removeChild(divElement.childNodes[i]);
                }
                var textNode = document.createTextNode(title);
                divElement.appendChild(textNode);
            }

            $scope.$on('$stateChangeSuccess', function (ev, to, toParams, from) {
                $scope.previousState = from.name;
                $scope.currentState = to.name;
            });

            $scope.initFooter();
        }]
    );

    /**
     * Application wide Modal Message Controller
     */
    angular.module('MovieRamaUi').controller('MessageInstanceCtrl', function ($scope, $modalInstance, message, $sce) {
        $scope.message = $sce.trustAsHtml(message);
        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    });

    /**
     * Application wide Confirmation Message Controller
     */
    angular.module('MovieRamaUi').controller('ConfirmInstanceCtrl', function ($scope, $modalInstance, message, choice, $sce) {
        $scope.message = $sce.trustAsHtml(message);
        $scope.choice = choice;

        $scope.ok = function (choice) {
            $modalInstance.close('yes');
        };

        $scope.cancel = function (choice) {
            $modalInstance.close('');
        };
    });
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
