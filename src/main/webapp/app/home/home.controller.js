(function() {
    'use strict';

    angular
        .module('expertSystemApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', 'ExpertSystem', '$state'];

    function HomeController ($scope, Principal, LoginService, ExpertSystem, $state) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.start = start;
        vm.gotoExpertSystem=gotoExpertSystem;

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
            ExpertSystem.query(onSuccess);
            function onSuccess(data) {
                vm.expertSystems = data;
            }
        }

        function start(expertSystem) {
            $state.go('expert-system-runner',{id : expertSystem.id});
        }

        function gotoExpertSystem() {
            $state.go('expert-system');
        }
    }
})();
