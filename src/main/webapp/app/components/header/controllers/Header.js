/* global angular, _, configData,moment,$,waitingDialog */
(function () {
    "use strict";
    angular
        .module("MovieRamaUi")
        .controller("HeaderCtrl", ["$scope", "$http", HeaderCtrl]);

    function HeaderCtrl($scope, $http) {

        // Auto refresh footer every 5 seconds to re-calculate session changes
        $scope.intervalTimer = setInterval(function () {
            $scope.refreshHeader()
        }, 5000);

        $scope.refreshHeader = function() {
            refreshHeader();
        }

        function refreshHeader() {
            // check Authorization
        }

        $scope.initHeader = function initFooter() {
            $scope.loggedOut()
            $scope.refreshHeader()
        }

        $scope.loggedIn = function() {
            setLoginButton('none')
            setRegisterButton('none')
            setLogoutButton('display')
        }

        $scope.loggedOut = function() {
            setLoginButton('display')
            setRegisterButton('display')
            setLogoutButton('none')
        }

        function setLoginButton(display) {
            var alert = document.getElementById("login");
            alert.style.display = display;
        }

        function setRegisterButton(display) {
            var alert = document.getElementById("register");
            alert.style.display = display;
        }

        function setLogoutButton(display) {
            var alert = document.getElementById("logout");
            alert.style.display = display;
        }
    }
}());
