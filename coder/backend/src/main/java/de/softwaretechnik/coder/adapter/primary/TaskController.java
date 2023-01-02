package de.softwaretechnik.coder.adapter.primary;

import de.softwaretechnik.coder.application.SolutionService;
import de.softwaretechnik.coder.application.compiler.TemplateModifiedException;
import de.softwaretechnik.coder.domain.Task;
import de.softwaretechnik.coder.domain.TestResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final SolutionService solutionService;

    @PostMapping("/api/task/submit")
    ResponseEntity<TestResult[]> submitSolution(@RequestParam String taskName, @RequestBody String solution) {
        return ResponseEntity.ok(solutionService.testResults(solution, taskName));
    }

    @GetMapping("/api/task/listAll")
    ResponseEntity<Task[]> getTaskByName() {
        return ResponseEntity.ok(solutionService.getAllTasks());
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
