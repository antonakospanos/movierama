(function () {
    'use strict';

    angular
        .module('MovieRamaUi')
        .factory('UserService', UserService);

    UserService.$inject = ['$http'];
    function UserService($http) {
        var service = {};
        service.GetUrl = GetUrl;
        service.GetById = GetById;
        service.Create = Create;

        return service;

        function GetUrl() {
            return "http://" + backend_ip + ":" + backend_port + "/" + backend_context_path + "/users";
        }

        function GetById(id) {
            return $http
                .get(service.GetUrl() + '/' + id)
                .then(handleResponse, handleResponse());
        }

        function Create(user) {
           var headers = {
                'Content-Type': 'application/json;charset=utf-8;'
            }
            return $http
                .post(service.GetUrl(), user, { headers: headers })
                // .then(handleResponse, handleResponse());
                .then(function successCallback(response) {
                    return response;
                }, function errorCallback(response) {
                    return response;
                });
        }

        // Private Util
        function handleResponse(res) {
            return res;
        }
    }
})();