(function() {
    'use strict';

    angular
        .module('expertSystemApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('expert-system-runner', {
            parent: 'app',
            url: '/run/:id',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/expert-system/expert-system-runner.html',
                    controller: 'ExpertSystemRunnerController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    //$translatePartialLoader.addPart('expert-system-runner'); //TODO i18n
                    return $translate.refresh();
                }]
            }

        });
    }
})();
