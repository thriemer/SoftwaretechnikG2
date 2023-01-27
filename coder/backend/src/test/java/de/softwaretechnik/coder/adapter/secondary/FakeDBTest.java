package de.softwaretechnik.coder.adapter.secondary;

import de.softwaretechnik.coder.domain.CodeSampleSolution;
import de.softwaretechnik.coder.domain.CodeTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FakeDBTest {
    private TaskRepository taskRepository = mock(TaskRepository.class);

    @InjectMocks
    private FakeDB fakeDB = new FakeDB(taskRepository);

    @BeforeEach
    void setUp() {
        when(taskRepository.findAll()).thenReturn(List.of(
                new CodeTask("add", "add","Complete the function so that it adds two numbers.","","", """
                public class Calculator {
                    public static int add(int a, int b){
                        return /*TODO*/;
                    }
                }
                    """),
                new CodeTask("reverse","reverse", "Complete the function so that it reverses a string","","", """
                public class StringManipulator {
                    public static String reverse(String s){
                        return /*TODO*/;
                    }
                }
                    """)));
        when(taskRepository.findByName("add")).thenReturn(Optional.of(new CodeTask("add", "add", "Complete the function so that it adds two numbers.", "", "", """
                public class Calculator {
                    public static int add(int a, int b){
                        return /*TODO*/;
                    }
                }
                    """)));
        when(taskRepository.findByName("reverse")).thenReturn(Optional.of(new CodeTask("reverse", "reverse", "Complete the function so that it reverses a string", "", "", """
                public class StringManipulator {
                    public static String reverse(String s){
                        return /*TODO*/;
                    }
                }
                    """)));
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
    public void testGetTaskByName_found() {
        // Arrange
        CodeTask expectedTask = new CodeTask("add", "add","Complete the function so that it adds two numbers.","","", """
                public class Calculator {
                    public static int add(int a, int b){
                        return /*TODO*/;
                    }
                }
                    """);

        // Act
        CodeTask actualTask = fakeDB.getTaskByName("add");

        // Assert

        assertEquals(expectedTask.getName(), actualTask.getName());
        assertEquals(expectedTask.getDisplayName(), actualTask.getDisplayName());
        assertEquals(expectedTask.getShortDescription(), actualTask.getShortDescription());
        assertEquals(expectedTask.getTaskType(), actualTask.getTaskType());
        assertEquals(expectedTask.getTaskDescription(), actualTask.getTaskDescription());
        assertEquals(expectedTask.getCodeTemplate(), actualTask.getCodeTemplate());

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
        CodeSampleSolution result = fakeDB.getCodeSampleSolutionByName("addTwoNumbers");
        if(result != null){
            assertEquals("addTwoNumbers", result.taskName());
        }else{
            fail("no solution found");
        }
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
        assertEquals("add", allTasks[0].getName());
        assertEquals("reverse", allTasks[1].getName());
    }

}