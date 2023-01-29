package de.softwaretechnik.coder.adapter.primary;

import de.softwaretechnik.coder.application.evaluation.SolutionService;
import de.softwaretechnik.coder.application.tasks.TaskService;
import de.softwaretechnik.coder.domain.CodeEvaluation;
import de.softwaretechnik.coder.domain.CodeTask;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Arrays;

@Controller
@RequiredArgsConstructor
public class SiteController {

    private final TaskService taskService;
    private final SolutionService solutionService;

    record TaskPair(CodeTask codeTask, String attempted) {
    }

    @GetMapping("/")
    String showHomePage(Model model, Principal principal) {
        model.addAttribute("userName", principal.getName());

        var tasks = taskService.getAllTasks();
        var tasksPairArray = Arrays.stream(tasks).map(t -> {
            var taskAndSolution = solutionService.getTaskAndSolution(t.getName(), principal.getName());
            String taskState = getState(taskAndSolution.evaluation());
            return new TaskPair(taskAndSolution.task(), taskState);
        }).toArray(TaskPair[]::new);

        model.addAttribute("tasks", tasksPairArray);
        return "index";
    }

    private String getState(CodeEvaluation[] evaluations) {
        if (evaluations == null) return "not_attempted.png";
        return Arrays.stream(evaluations).filter(e -> !e.isCorrect()).findFirst().map(e -> "not_solved.png").orElse("correct.png");
    }

}
