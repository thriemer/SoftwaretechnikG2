package de.softwaretechnik.coder.adapter.primary;

import de.softwaretechnik.coder.application.evaluation.SolutionEvaluationService;
import de.softwaretechnik.coder.application.evaluation.SolutionSaveService;
import de.softwaretechnik.coder.application.evaluation.SolutionSubmitService;
import de.softwaretechnik.coder.application.evaluation.compiler.TemplateModifiedException;
import de.softwaretechnik.coder.application.tasks.TaskService;
import de.softwaretechnik.coder.domain.CodeTask;
import de.softwaretechnik.coder.domain.CodeEvaluation;
import de.softwaretechnik.coder.domain.SubmittedSolution;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final SolutionSubmitService solutionSubmitService;
    private final TaskService taskService;
    private final SolutionSaveService solutionSaveService;
    private final SolutionEvaluationService solutionEvaluationService;

    @PostMapping("/api/task/submit")
    ResponseEntity<CodeEvaluation[]> submitSolution(@RequestParam String taskName, @RequestBody String solution, Principal principal) {
        return ResponseEntity.ok(solutionSubmitService.submitSolution(principal.getName(), solution, taskName));
    }

    @GetMapping("/api/task/listAll")
    ResponseEntity<CodeTask[]> listAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    private record TaskAndSubmittedSolution(CodeTask task, CodeEvaluation[] evaluation, String submission) {
    }

    @GetMapping("/api/task/{taskName}")
    ResponseEntity<TaskAndSubmittedSolution> getTaskByName(@PathVariable String taskName, Principal principal) {
        CodeTask codeTask = taskService.getTaskByName(taskName);
        Optional<SubmittedSolution> submittedSolution = solutionSaveService.findSubmission(principal.getName(), taskName);
        if (submittedSolution.isEmpty()) return ResponseEntity.ok(new TaskAndSubmittedSolution(codeTask, null, null));
        CodeEvaluation[] evaluation;
        try {
            evaluation = solutionEvaluationService.evaluateSubmission(submittedSolution.get().getSubmissionContent(), taskName);
        } catch (TemplateModifiedException x) {
            evaluation = new CodeEvaluation[]{new CodeEvaluation(false, handleTemplateModifiedException(x).getBody())};
        }
        return ResponseEntity.ok(new TaskAndSubmittedSolution(codeTask, evaluation, submittedSolution.get().getSubmissionContent()));
    }


    @ExceptionHandler(TemplateModifiedException.class)
    ResponseEntity<String> handleTemplateModifiedException(TemplateModifiedException ex) {
        ex.printStackTrace();
        if (ex.getCause() instanceof ClassNotFoundException cnf) {
            return ResponseEntity.badRequest().body(cnf.getMessage());
        }
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

}
