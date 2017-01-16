(function() {
    'use strict';

    angular
        .module('expertSystemApp')
        .controller('ExpertSystemRunnerController', ExpertSystemRunnerController);

    ExpertSystemRunnerController.$inject = ['ExpertSystem', '$stateParams', 'Answer'];

    function ExpertSystemRunnerController (ExpertSystem, $stateParams, Answer) {
        var vm = this;
        vm.selectedAnswer = selectedAnswer;
        vm.concluded = false;
        vm.restart = restart;
        vm.back = back;

        ExpertSystem.get({id : $stateParams.id}, onSuccess, onError);

        vm.questions = [];

        function onSuccess(data) {
            vm.expertSystem = data;
            restart();
        }

        function restart() {
            newQuestion(vm.expertSystem.question);
        }

        function back() {
            if (vm.questions.length > 0) {
                newQuestion(vm.questions.pop());
            }
        }

        function onError(error) {
            alert(error.data.message);
        }

        function selectedAnswer(answer) {
            vm.questions.push(answer.question);
            if (answer.conclusion.question) {
                newQuestion(answer.conclusion.question);
            } else {
                vm.item = answer.conclusion;
                vm.answers = null;
                vm.concluded = true;
            }
        }

        function newQuestion(question){
            vm.concluded = false;
            vm.item = question;

            Answer.forQuestion({id : vm.item.id}, onAnswerSuccess, onError);

            function onAnswerSuccess(data) {
                vm.answers = data;
            }
        }

    }
})();
