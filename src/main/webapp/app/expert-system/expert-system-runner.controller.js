(function() {
    'use strict';

    angular
        .module('expertSystemApp')
        .controller('ExpertSystemRunnerController', ExpertSystemRunnerController);

    ExpertSystemRunnerController.$inject = ['ExpertSystem', '$stateParams', 'Answer'];

    function ExpertSystemRunnerController (ExpertSystem, $stateParams, Answer) {
        var vm = this;
        vm.selectedAnswer = selectedAnswer;

        ExpertSystem.get({id : $stateParams.id}, onSuccess, onError);

        function onSuccess(data) {
            vm.expertSystem = data;
            newQuestion(vm.expertSystem.question);
        }

        function onError(error) {
            alert(error.data.message);
        }

        function selectedAnswer(answer) {
            if (answer.conclusion.question) {
                newQuestion(answer.conclusion.question);
            } else {
                vm.item = answer.conclusion;
                vm.answers = null;
            }
        }

        function newQuestion(question){
            vm.item = question;
            Answer.forQuestion({id : vm.item.id}, onAnswerSuccess, onError);

            function onAnswerSuccess(data) {
                vm.answers = data;
            }
        }

    }
})();
