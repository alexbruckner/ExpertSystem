(function() {
    'use strict';

    angular
        .module('expertSystemApp')
        .controller('ExpertSystemDialogController', ExpertSystemDialogController);

    ExpertSystemDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'ExpertSystem', 'Question'];

    function ExpertSystemDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, ExpertSystem, Question) {
        var vm = this;

        vm.expertSystem = entity;
        if (vm.expertSystem.xml == null) {
            vm.expertSystem.xml = "?";
        }
        vm.clear = clear;
        vm.save = save;
        vm.questions = Question.query({filter: 'expertsystem-is-null'});
        $q.all([vm.expertSystem.$promise, vm.questions.$promise]).then(function() {
            if (!vm.expertSystem.question || !vm.expertSystem.question.id) {
                return $q.reject();
            }
            return Question.get({id : vm.expertSystem.question.id}).$promise;
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
            if (vm.expertSystem.id !== null) {
                ExpertSystem.update(vm.expertSystem, onSaveSuccess, onSaveError);
            } else {
                ExpertSystem.save(vm.expertSystem, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('expertSystemApp:expertSystemUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
