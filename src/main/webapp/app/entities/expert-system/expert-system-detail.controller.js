(function() {
    'use strict';

    angular
        .module('expertSystemApp')
        .controller('ExpertSystemDetailController', ExpertSystemDetailController);

    ExpertSystemDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ExpertSystem', 'Question'];

    function ExpertSystemDetailController($scope, $rootScope, $stateParams, previousState, entity, ExpertSystem, Question) {
        var vm = this;

        vm.expertSystem = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('expertSystemApp:expertSystemUpdate', function(event, result) {
            vm.expertSystem = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
