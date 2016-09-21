(function() {
    'use strict';
    angular
        .module('expertSystemApp')
        .factory('Conclusion', Conclusion);

    Conclusion.$inject = ['$resource'];

    function Conclusion ($resource) {
        var resourceUrl =  'api/conclusions/:id';

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
            'update': { method:'PUT' }
        });
    }
})();
