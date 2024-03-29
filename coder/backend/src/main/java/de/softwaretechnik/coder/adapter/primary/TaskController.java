package de.softwaretechnik.coder.adapter.primary;

import de.softwaretechnik.coder.application.evaluation.SolutionEvaluationService;
import de.softwaretechnik.coder.application.evaluation.SolutionSaveService;
import de.softwaretechnik.coder.application.evaluation.SolutionService;
import de.softwaretechnik.coder.application.evaluation.SolutionSubmitService;
import de.softwaretechnik.coder.application.evaluation.compiler.TemplateModifiedException;
import de.softwaretechnik.coder.application.tasks.TaskService;
import de.softwaretechnik.coder.domain.CodeTask;
import de.softwaretechnik.coder.domain.CodeEvaluation;
import de.softwaretechnik.coder.domain.SubmittedSolution;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class TaskController {

    private final SolutionService solutionService;
    private final SolutionSubmitService solutionSubmitService;


    @PostMapping("/api/task/submit")
    ResponseEntity<CodeEvaluation[]> submitSolution(@RequestParam String taskName, @RequestBody String solution, Principal principal) {
        return ResponseEntity.ok(solutionSubmitService.submitSolution(principal.getName(), solution, taskName));
    }

    @GetMapping("/api/task/{taskName}")
    ResponseEntity<SolutionService.TaskAndSubmittedSolution> getTaskByName(@PathVariable String taskName, Principal principal) {
        return ResponseEntity.ok(solutionService.getTaskAndSolution(taskName, principal.getName()));
    }

    @GetMapping("/outputTask")
    String showOutputTaskPage(Model model, Principal principal, @RequestParam String taskName){
        model.addAttribute("userName", principal.getName());
        model.addAttribute("codeTaskAndSolution",solutionService.getTaskAndSolution(taskName,principal.getName()));
        return "outputTask";
    }

    @PostMapping("/outputTask")
    String showEvaluation(Model model, Principal principal, @RequestParam String taskName, @RequestParam String submittedSolution){
        solutionSubmitService.submitSolution(principal.getName(),submittedSolution,taskName);
        model.addAttribute("userName", principal.getName());
        model.addAttribute("codeTaskAndSolution",solutionService.getTaskAndSolution(taskName,principal.getName()));
        return "outputTask";
    }


}
