package de.softwaretechnik.coder.application.evaluation;

import de.softwaretechnik.coder.adapter.secondary.SubmittedSolutionRepository;
import de.softwaretechnik.coder.domain.SubmittedSolution;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SolutionSaveService {

    private final SubmittedSolutionRepository submittedSolutionRepository;

    public void saveSolution(String userName, String taskName, String code) {
        SubmittedSolution submittedSolution = new SubmittedSolution();
        submittedSolution.setUserName(userName);
        submittedSolution.setTaskName(taskName);
        submittedSolution.setSubmissionContent(code);
        submittedSolutionRepository.save(submittedSolution);
    }

}
