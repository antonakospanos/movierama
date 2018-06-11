(function () {
    'use strict';

    angular
        .module('MovieRamaUi')
        .controller('RegisterCtrl', RegisterCtrl);

    RegisterCtrl.$inject = ['UserService', '$location', '$scope', "$state"];
    function RegisterCtrl(UserService, $location, $scope, $state) {
        var ctrl = this;
        ctrl.register = register;

        function register() {
            UserService.Create(ctrl.user)
                .then(function (response) {
                    console.log(response)
                    $scope.createToast(response.data.result + "! " + response.data.description)
                    if (response.data.result === 'SUCCESS') {
                        AuthenticationService.Authorize(ctrl.username, response.data.id);
                        $scope.loggedIn();
                        $state.go($scope.previousState ? $scope.previousState : "movies_review", {});
                    }
                });
        }
    }
})();