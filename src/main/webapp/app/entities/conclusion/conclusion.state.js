(function() {
    'use strict';

    angular
        .module('expertSystemApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('conclusion', {
            parent: 'entity',
            url: '/conclusion?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'expertSystemApp.conclusion.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/conclusion/conclusions.html',
                    controller: 'ConclusionController',
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
                    $translatePartialLoader.addPart('conclusion');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('conclusion-detail', {
            parent: 'entity',
            url: '/conclusion/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'expertSystemApp.conclusion.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/conclusion/conclusion-detail.html',
                    controller: 'ConclusionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('conclusion');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Conclusion', function($stateParams, Conclusion) {
                    return Conclusion.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'conclusion',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('conclusion-detail.edit', {
            parent: 'conclusion-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/conclusion/conclusion-dialog.html',
                    controller: 'ConclusionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Conclusion', function(Conclusion) {
                            return Conclusion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('conclusion.new', {
            parent: 'conclusion',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/conclusion/conclusion-dialog.html',
                    controller: 'ConclusionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                text: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('conclusion', null, { reload: 'conclusion' });
                }, function() {
                    $state.go('conclusion');
                });
            }]
        })
        .state('conclusion.edit', {
            parent: 'conclusion',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/conclusion/conclusion-dialog.html',
                    controller: 'ConclusionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Conclusion', function(Conclusion) {
                            return Conclusion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('conclusion', null, { reload: 'conclusion' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('conclusion.delete', {
            parent: 'conclusion',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/conclusion/conclusion-delete-dialog.html',
                    controller: 'ConclusionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Conclusion', function(Conclusion) {
                            return Conclusion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('conclusion', null, { reload: 'conclusion' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
