(function() {
    'use strict';

    angular
        .module('expertSystemApp')
        .controller('ExpertSystemDeleteController',ExpertSystemDeleteController);

    ExpertSystemDeleteController.$inject = ['$uibModalInstance', 'entity', 'ExpertSystem'];

    function ExpertSystemDeleteController($uibModalInstance, entity, ExpertSystem) {
        var vm = this;

        vm.expertSystem = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ExpertSystem.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
