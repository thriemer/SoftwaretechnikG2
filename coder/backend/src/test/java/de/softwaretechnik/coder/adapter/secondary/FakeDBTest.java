package de.softwaretechnik.coder.adapter.secondary;

import de.softwaretechnik.coder.domain.CodeSampleSolution;
import de.softwaretechnik.coder.domain.CodeTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FakeDBTest {
    @InjectMocks
    private FakeDB fakeDB;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetTaskNames() {
        // arrange
        List<String> expectedTaskNames = List.of("add", "reverse");

        // act
        List<String> taskNames = fakeDB.getTaskNames();

        // assert
        assertEquals(expectedTaskNames, taskNames);
    }

    @Test
    void testGetTaskByName_found() {
        // arrange
        String taskName = "add";
        CodeTask expectedTask = new CodeTask("add", "Complete the function so that it adds two numbers.", """
                public class Calculator {
                    public static int add(int a, int b){
                        return /*TODO*/;
                    }
                }
                    """);

        // act
        CodeTask task = fakeDB.getTaskByName(taskName);

        // assert
        assertEquals(expectedTask, task);
    }

    @Test
    void testGetTaskByName_notFound() {
        // arrange
        String taskName = "notFound";

        // act
        CodeTask task = fakeDB.getTaskByName(taskName);

        // assert
        assertNull(task);
    }

    @Test
    void testGetCodeSampleSolutionByName_found() {
        // arrange
        String taskName = "add";
        CodeSampleSolution expected = new CodeSampleSolution("add", "add",
                new Object[][]{{5, 4}, {11, 12}},
                new Object[]{9, 23}
        );

        // act
        CodeSampleSolution result = fakeDB.getCodeSampleSolutionByName(taskName);

        // assert
        assertEquals(expected.taskName(), result.taskName());
    }

    @Test
    void testGetCodeSampleSolutionByName_notFound() {
        // arrange
        String taskName = "notFound";

        // act
        CodeSampleSolution solution = fakeDB.getCodeSampleSolutionByName(taskName);

        // assert
        assertNull(solution);
    }
    @Test
    void testGetAllTasks() {
        // act
        CodeTask[] allTasks = fakeDB.getAllTasks();

        // assert
        assertEquals(2, allTasks.length);
        assertEquals("add", allTasks[0].name());
        assertEquals("reverse", allTasks[1].name());
    }

}