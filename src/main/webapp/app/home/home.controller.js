(function() {
    'use strict';

    angular
        .module('expertSystemApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', 'ExpertSystem'];

    function HomeController ($scope, Principal, LoginService, ExpertSystem) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
                getExpertSystems();

            });
        }

        function getExpertSystems () {
            ExpertSystem.query(onSuccess, onError);
            function onSuccess(data) {
                vm.expertSystems = data;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }
    }
})();
