package de.softwaretechnik.coder.application.compiler;

import de.softwaretechnik.coder.application.DBAbstraction;
import de.softwaretechnik.coder.application.SolutionService;
import de.softwaretechnik.coder.domain.CodeEvaluation;
import de.softwaretechnik.coder.domain.TestResult;
import de.softwaretechnik.coder.domain.Task;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class SolutionServiceTests {

    private final DBAbstraction dbAbstraction = Mockito.mock(DBAbstraction.class);
    private final JavaSourceValidator javaSourceValidator = Mockito.mock(JavaSourceValidator.class);
    private final SolutionService solutionService = new SolutionService(dbAbstraction, javaSourceValidator);

    @Test
    void testTestResults() {
        // Given
        var codeEvaluation = new CodeEvaluation("TaskName", "methodName", new Object[][] {{1, 2}, {3, 4}}, new Object[] {3, 7});
        var testResults = new TestResult[] { new TestResult(true, ""), new TestResult(false, "expected X but got Y") };
        var code = "public class Solution { /* code */ }";
        var taskName = "Some task name";
        Mockito.when(dbAbstraction.getCodeEvaluationByName(taskName)).thenReturn(codeEvaluation);
        Mockito.when(javaSourceValidator.testProgram(code, codeEvaluation)).thenReturn(testResults);

        // When
        var result = solutionService.testResults(code, taskName);

        // Then
        assertArrayEquals(testResults, result);
    }

    @Test
    void testGetAllTasks() {
        // Given
        var tasks = new Task[] {new Task("taskName", "taskDesc", "psvm")};
        Mockito.when(dbAbstraction.getAllTasks()).thenReturn(tasks);

        // When
        var result = solutionService.getAllTasks();

        // Then
        assertArrayEquals(tasks, result);
    }
}
