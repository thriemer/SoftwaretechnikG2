package de.softwaretechnik.coder.application.evaluation;

import de.softwaretechnik.coder.adapter.secondary.SubmittedSolutionRepository;
import de.softwaretechnik.coder.domain.SubmittedSolution;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SolutionSaveService {

    private final SubmittedSolutionRepository submittedSolutionRepository;

    public void saveSolution(String userName, String taskName, String code) {
        SubmittedSolution submittedSolution = submittedSolutionRepository
                .findByUserNameAndTaskName(userName, taskName)
                .orElseGet(SubmittedSolution::new);
        submittedSolution.setUserName(userName);
        submittedSolution.setTaskName(taskName);
        submittedSolution.setSubmissionContent(code);
        submittedSolutionRepository.save(submittedSolution);
    }

    public Optional<SubmittedSolution> findSubmission(String userName, String taskName) {
        return submittedSolutionRepository.findByUserNameAndTaskName(userName, taskName);
    }

}
