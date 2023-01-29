package de.softwaretechnik.coder.application.evaluation;

import de.softwaretechnik.coder.adapter.secondary.SubmittedSolutionRepository;
import de.softwaretechnik.coder.domain.SubmittedSolution;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SolutionSaveServiceTest {
    private SubmittedSolutionRepository submittedSolutionRepository = Mockito.mock(SubmittedSolutionRepository.class);
    private SolutionSaveService solutionSaveService;

    @BeforeEach
    void setUp() {
        solutionSaveService = new SolutionSaveService(submittedSolutionRepository);
        when(submittedSolutionRepository.save(any())).thenReturn(new SubmittedSolution(1L,"","",""));
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
        Mockito.verify(submittedSolutionRepository, times(1)).save(any());
    }
}

