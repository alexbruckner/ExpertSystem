(function() {
    'use strict';

    angular
        .module('expertSystemApp')
        .controller('ConclusionDetailController', ConclusionDetailController);

    ConclusionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Conclusion', 'Question'];

    function ConclusionDetailController($scope, $rootScope, $stateParams, previousState, entity, Conclusion, Question) {
        var vm = this;

        vm.conclusion = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('expertSystemApp:conclusionUpdate', function(event, result) {
            vm.conclusion = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
