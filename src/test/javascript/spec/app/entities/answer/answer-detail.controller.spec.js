'use strict';

describe('Controller Tests', function() {

    describe('Answer Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockAnswer, MockQuestion, MockConclusion;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockAnswer = jasmine.createSpy('MockAnswer');
            MockQuestion = jasmine.createSpy('MockQuestion');
            MockConclusion = jasmine.createSpy('MockConclusion');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Answer': MockAnswer,
                'Question': MockQuestion,
                'Conclusion': MockConclusion
            };
            createController = function() {
                $injector.get('$controller')("AnswerDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'expertSystemApp:answerUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
