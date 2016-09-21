(function() {
    'use strict';

    angular
        .module('expertSystemApp')
        .controller('AnswerDialogController', AnswerDialogController);

    AnswerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Answer', 'Question', 'Conclusion'];

    function AnswerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Answer, Question, Conclusion) {
        var vm = this;

        vm.answer = entity;
        vm.clear = clear;
        vm.save = save;
        vm.questions = Question.query();
        vm.conclusions = Conclusion.query({filter: 'answer-is-null'});
        $q.all([vm.answer.$promise, vm.conclusions.$promise]).then(function() {
            if (!vm.answer.conclusion || !vm.answer.conclusion.id) {
                return $q.reject();
            }
            return Conclusion.get({id : vm.answer.conclusion.id}).$promise;
        }).then(function(conclusion) {
            vm.conclusions.push(conclusion);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.answer.id !== null) {
                Answer.update(vm.answer, onSaveSuccess, onSaveError);
            } else {
                Answer.save(vm.answer, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('expertSystemApp:answerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
