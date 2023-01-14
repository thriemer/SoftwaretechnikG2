package de.softwaretechnik.coder.application.evaluation;


import de.softwaretechnik.coder.application.DBAbstraction;
import de.softwaretechnik.coder.application.evaluation.compiler.JavaSourceValidator;
import de.softwaretechnik.coder.domain.CodeEvaluation;
import de.softwaretechnik.coder.domain.CodeSampleSolution;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class SolutionEvaluationServiceTest {

    private final DBAbstraction dbAbstraction = Mockito.mock(DBAbstraction.class);
    private final JavaSourceValidator javaSourceValidator = Mockito.mock(JavaSourceValidator.class);

    private final SolutionEvaluationService cut = new SolutionEvaluationService(dbAbstraction, javaSourceValidator);

    @Test
    void testTestResults() {
        // Given
        var codeEvaluation = new CodeSampleSolution("TaskName", "methodName", new Object[][]{{1, 2}, {3, 4}}, new Object[]{3, 7});
        var testResults = new CodeEvaluation[]{new CodeEvaluation(true, ""), new CodeEvaluation(false, "expected X but got Y")};
        var code = "public class Solution { /* code */ }";
        var taskName = "Some task name";
        Mockito.when(dbAbstraction.getCodeSampleSolutionByName(taskName)).thenReturn(codeEvaluation);
        Mockito.when(javaSourceValidator.testProgram(code, codeEvaluation)).thenReturn(testResults);

        // When
        var result = cut.evaluateSubmission(code, taskName);

        // Then
        Assertions.assertArrayEquals(testResults, result);
    }

}
