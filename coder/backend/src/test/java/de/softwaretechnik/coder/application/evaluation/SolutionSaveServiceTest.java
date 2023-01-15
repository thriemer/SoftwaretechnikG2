package de.softwaretechnik.coder.application.evaluation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SolutionSaveServiceTest {
    private SolutionSaveService solutionSaveService;

    @BeforeEach
    void setUp() {
        solutionSaveService = new SolutionSaveService();
    }

    @Test
    void testSaveSolution() {
        // Arrange
        String userId = "testuser";
        String taskName = "task1";
        String code = "System.out.println(\"Hello World\");";

        // Act
        solutionSaveService.saveSolution(userId, taskName, code);

        // Assert
        //TODO: assert that the solution has been saved correctly
    }
}

