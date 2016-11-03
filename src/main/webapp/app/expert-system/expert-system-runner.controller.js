(function() {
    'use strict';

    angular
        .module('expertSystemApp')
        .controller('ExpertSystemRunnerController', ExpertSystemRunnerController);

    ExpertSystemRunnerController.$inject = ['ExpertSystem', '$stateParams'];

    function ExpertSystemRunnerController (ExpertSystem, $stateParams) {
        var vm = this;

        ExpertSystem.get({id : $stateParams.id}, onSuccess, onError);

        function onSuccess(data) {
            vm.expertSystem = data;
        }

        function onError(error) {
            alert(error.data.message);
        }

    }
})();
