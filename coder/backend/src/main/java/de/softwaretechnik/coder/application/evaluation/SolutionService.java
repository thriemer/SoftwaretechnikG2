package de.softwaretechnik.coder.application.evaluation;

import de.softwaretechnik.coder.application.tasks.TaskService;
import de.softwaretechnik.coder.domain.CodeEvaluation;
import de.softwaretechnik.coder.domain.CodeTask;
import de.softwaretechnik.coder.domain.SubmittedSolution;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SolutionService {

    private final TaskService taskService;
    private final SolutionSaveService solutionSaveService;
    private final SolutionEvaluationService solutionEvaluationService;


    public record TaskAndSubmittedSolution(CodeTask task, CodeEvaluation[] evaluation, String submission) {
    }

    public TaskAndSubmittedSolution getTaskAndSolution(String taskName, String userName) {
        CodeTask codeTask = taskService.getTaskByName(taskName);
        Optional<SubmittedSolution> submittedSolution = solutionSaveService.findSubmission(userName, taskName);
        if (submittedSolution.isEmpty()) return new TaskAndSubmittedSolution(codeTask, null, null);
        CodeEvaluation[] evaluation = solutionEvaluationService.getEvaluation(submittedSolution.get().getId());
        return new TaskAndSubmittedSolution(codeTask, evaluation, submittedSolution.get().getSubmissionContent());
    }

}
