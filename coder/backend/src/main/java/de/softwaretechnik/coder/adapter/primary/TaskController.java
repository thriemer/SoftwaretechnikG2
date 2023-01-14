package de.softwaretechnik.coder.adapter.primary;

import de.softwaretechnik.coder.application.evaluation.SolutionSubmitService;
import de.softwaretechnik.coder.application.evaluation.compiler.TemplateModifiedException;
import de.softwaretechnik.coder.application.tasks.TaskService;
import de.softwaretechnik.coder.domain.CodeTask;
import de.softwaretechnik.coder.domain.CodeEvaluation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final SolutionSubmitService solutionSubmitService;
    private final TaskService taskService;

    @PostMapping("/api/task/submit")
    ResponseEntity<CodeEvaluation[]> submitSolution(@RequestParam String taskName, @RequestBody String solution, Principal principal) {
        return ResponseEntity.ok(solutionSubmitService.submitSolution(principal.getName(), solution, taskName));
    }

    @GetMapping("/api/task/listAll")
    ResponseEntity<CodeTask[]> listAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
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
