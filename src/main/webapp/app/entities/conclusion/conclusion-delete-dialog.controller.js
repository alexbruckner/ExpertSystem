(function() {
    'use strict';

    angular
        .module('expertSystemApp')
        .controller('ConclusionDeleteController',ConclusionDeleteController);

    ConclusionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Conclusion'];

    function ConclusionDeleteController($uibModalInstance, entity, Conclusion) {
        var vm = this;

        vm.conclusion = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Conclusion.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
