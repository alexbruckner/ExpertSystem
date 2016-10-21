(function() {
    'use strict';
    angular
        .module('expertSystemApp')
        .factory('ExpertSystem', ExpertSystem);

    ExpertSystem.$inject = ['$resource'];

    function ExpertSystem ($resource) {
        var resourceUrl =  'api/expert-systems/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' },
            'upload': { url: 'api/expert-systems/upload', method: 'POST', params: {xml : '@xml'} }
        });
    }
})();
