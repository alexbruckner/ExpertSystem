(function() {
    'use strict';

    angular
        .module('expertSystemApp')
        .controller('ConclusionDialogController', ConclusionDialogController);

    ConclusionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Conclusion', 'Question'];

    function ConclusionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Conclusion, Question) {
        var vm = this;

        vm.conclusion = entity;
        vm.clear = clear;
        vm.save = save;
        vm.questions = Question.query({filter: 'conclusion-is-null'});
        $q.all([vm.conclusion.$promise, vm.questions.$promise]).then(function() {
            if (!vm.conclusion.question || !vm.conclusion.question.id) {
                return $q.reject();
            }
            return Question.get({id : vm.conclusion.question.id}).$promise;
        }).then(function(question) {
            vm.questions.push(question);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.conclusion.id !== null) {
                Conclusion.update(vm.conclusion, onSaveSuccess, onSaveError);
            } else {
                Conclusion.save(vm.conclusion, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('expertSystemApp:conclusionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
