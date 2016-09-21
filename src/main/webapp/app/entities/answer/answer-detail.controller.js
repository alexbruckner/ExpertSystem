(function() {
    'use strict';

    angular
        .module('expertSystemApp')
        .controller('AnswerDetailController', AnswerDetailController);

    AnswerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Answer', 'Question', 'Conclusion'];

    function AnswerDetailController($scope, $rootScope, $stateParams, previousState, entity, Answer, Question, Conclusion) {
        var vm = this;

        vm.answer = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('expertSystemApp:answerUpdate', function(event, result) {
            vm.answer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
