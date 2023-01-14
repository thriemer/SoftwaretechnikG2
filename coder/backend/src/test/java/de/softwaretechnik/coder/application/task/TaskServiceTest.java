package de.softwaretechnik.coder.application.task;

import de.softwaretechnik.coder.application.DBAbstraction;
import de.softwaretechnik.coder.application.tasks.TaskService;
import de.softwaretechnik.coder.domain.CodeTask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class TaskServiceTest {

    DBAbstraction dbAbstraction = Mockito.mock(DBAbstraction.class);

    private final TaskService cut = new TaskService(dbAbstraction);

    @Test
    void testGetAllTasks() {
        // Given
        var tasks = new CodeTask[]{new CodeTask("taskName", "taskDesc", "psvm")};
        Mockito.when(dbAbstraction.getAllTasks()).thenReturn(tasks);

        // When
        var result = cut.getAllTasks();

        // Then
        Assertions.assertArrayEquals(tasks, result);
    }

}
