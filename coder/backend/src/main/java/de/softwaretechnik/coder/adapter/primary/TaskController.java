package de.softwaretechnik.coder.adapter.primary;

import de.softwaretechnik.coder.application.SolutionService;
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
    ResponseEntity<Task[]> listAllTasks() {
        return ResponseEntity.ok(solutionService.getAllTasks());
    }

}
