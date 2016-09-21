(function() {
    'use strict';

    angular
        .module('expertSystemApp')
        .controller('QuestionDetailController', QuestionDetailController);

    QuestionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Question', 'Answer'];

    function QuestionDetailController($scope, $rootScope, $stateParams, previousState, entity, Question, Answer) {
        var vm = this;

        vm.question = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('expertSystemApp:questionUpdate', function(event, result) {
            vm.question = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
