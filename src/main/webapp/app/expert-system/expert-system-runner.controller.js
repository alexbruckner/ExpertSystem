(function() {
    'use strict';

    angular
        .module('expertSystemApp')
        .controller('ExpertSystemRunnerController', ExpertSystemRunnerController);

    ExpertSystemRunnerController.$inject = ['ExpertSystem', '$stateParams', 'Answer'];

    function ExpertSystemRunnerController (ExpertSystem, $stateParams, Answer) {
        var vm = this;

        ExpertSystem.get({id : $stateParams.id}, onSuccess, onError);

        function onSuccess(data) {
            vm.expertSystem = data;
            vm.item = vm.expertSystem.question;
            Answer.forQuestion({id : vm.item.id}, onAnswerSuccess, onError);

            function onAnswerSuccess(data) {
                vm.answers = data;
            }

        }

        function onError(error) {
            alert(error.data.message);
        }

    }
})();
