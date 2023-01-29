package de.softwaretechnik.coder.application.evaluation;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class SolutionSubmitServiceTest {

    private SolutionEvaluationService evaluationMock = Mockito.mock(SolutionEvaluationService.class);
    private SolutionSaveService saveMock = Mockito.mock(SolutionSaveService.class);

    private SolutionSubmitService cut = new SolutionSubmitService(evaluationMock, saveMock);

    @Test
    void submitSolution_solutionSavedAndEvaluated() {
        //arrange
        //act
        cut.submitSolution("myUser", "code", "taskName");
        //assert
        Mockito.verify(evaluationMock, Mockito.times(1)).evaluateSubmission("code", "taskName", 0);
        Mockito.verify(saveMock, Mockito.times(1)).saveSolution("myUser", "taskName", "code");
    }

}
