(function() {
    'use strict';

    angular
        .module('expertSystemApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('expert-system', {
            parent: 'entity',
            url: '/expert-system?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'expertSystemApp.expertSystem.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/expert-system/expert-systems.html',
                    controller: 'ExpertSystemController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('expertSystem');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('expert-system-detail', {
            parent: 'entity',
            url: '/expert-system/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'expertSystemApp.expertSystem.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/expert-system/expert-system-detail.html',
                    controller: 'ExpertSystemDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('expertSystem');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ExpertSystem', function($stateParams, ExpertSystem) {
                    return ExpertSystem.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'expert-system',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('expert-system-detail.edit', {
            parent: 'expert-system-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/expert-system/expert-system-dialog.html',
                    controller: 'ExpertSystemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ExpertSystem', function(ExpertSystem) {
                            return ExpertSystem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('expert-system.new', {
            parent: 'expert-system',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/expert-system/expert-system-dialog.html',
                    controller: 'ExpertSystemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                title: null,
                                xml: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('expert-system', null, { reload: 'expert-system' });
                }, function() {
                    $state.go('expert-system');
                });
            }]
        })
        .state('expert-system.edit', {
            parent: 'expert-system',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/expert-system/expert-system-dialog.html',
                    controller: 'ExpertSystemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ExpertSystem', function(ExpertSystem) {
                            return ExpertSystem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('expert-system', null, { reload: 'expert-system' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('expert-system.delete', {
            parent: 'expert-system',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/expert-system/expert-system-delete-dialog.html',
                    controller: 'ExpertSystemDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ExpertSystem', function(ExpertSystem) {
                            return ExpertSystem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('expert-system', null, { reload: 'expert-system' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
