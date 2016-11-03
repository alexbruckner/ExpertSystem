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
                alert(error.data.message);
            }
        }

        function start(expertSystem) {
            // TODO send selection
            $state.go('expert-system-runner',{id : expertSystem.id});
        }
    }
})();
