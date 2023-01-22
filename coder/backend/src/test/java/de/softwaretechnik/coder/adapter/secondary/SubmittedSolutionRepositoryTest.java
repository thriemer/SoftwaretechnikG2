package de.softwaretechnik.coder.adapter.secondary;

import de.softwaretechnik.coder.domain.SubmittedSolution;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SubmittedSolutionRepositoryTest {

    @Autowired
    SubmittedSolutionRepository submittedSolutionRepository;

    @Test
    void save_saveSubmittedSolutionReadAgain_isEquals() {
        //arrange
        SubmittedSolution submittedSolution = new SubmittedSolution(0L, "userName", "myTask", "userAnswer");
        //act
        submittedSolutionRepository.save(submittedSolution);
        var actual = submittedSolutionRepository.findByUserNameAndTaskName(submittedSolution.getUserName(), submittedSolution.getTaskName()).get();
        //assert
        Assertions.assertEquals(submittedSolution.getUserName(), actual.getUserName());
        Assertions.assertEquals(submittedSolution.getTaskName(), actual.getTaskName());
        Assertions.assertEquals(submittedSolution.getSubmissionContent(), actual.getSubmissionContent());
    }

}
