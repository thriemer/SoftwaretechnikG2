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

    @GetMapping("/logoutSuccessful")
    String showLogoutSuccessfulPage() {
        return "logoutSuccessful";
    }
    @GetMapping("/")
    String showHomePage(Model model, Principal principal) {
        model.addAttribute("userName", principal.getName());

        var tasks = taskService.getAllTasks();
        var codeTasks = accumulate(tasks, CodeTask.CODE_TASK_TYPE, principal.getName());
        var outputTasks = accumulate(tasks, CodeTask.OUTPUT_TASK_TYPE, principal.getName());

        model.addAttribute("codeTasks", codeTasks);
        model.addAttribute("outputTasks", outputTasks);
        return "index";
    }

    @GetMapping("/admin")
    String showAdminHomePage(Model model, Principal principal) {
        model.addAttribute("userName", principal.getName());

        var tasks = taskService.getAllTasks();
        var codeTasks = accumulate(tasks, CodeTask.CODE_TASK_TYPE, principal.getName());
        var outputTasks = accumulate(tasks, CodeTask.OUTPUT_TASK_TYPE, principal.getName());

        model.addAttribute("codeTasks", codeTasks);
        model.addAttribute("outputTasks", outputTasks);
        return "admin";
    }

    @GetMapping("/createCodingTask")
    String showCreateCodingTaskPage(Model model, Principal principal) {
        model.addAttribute("userName", principal.getName());

        var tasks = taskService.getAllTasks();
        var codeTasks = accumulate(tasks, CodeTask.CODE_TASK_TYPE, principal.getName());
        var outputTasks = accumulate(tasks, CodeTask.OUTPUT_TASK_TYPE, principal.getName());

        return "createCodingTask";
    }

    @GetMapping("/editCodingTask")
    String showEditCodingTaskPage(Model model, Principal principal) {
        model.addAttribute("userName", principal.getName());

        var tasks = taskService.getAllTasks();
        var codeTasks = accumulate(tasks, CodeTask.CODE_TASK_TYPE, principal.getName());
        var outputTasks = accumulate(tasks, CodeTask.OUTPUT_TASK_TYPE, principal.getName());

        model.addAttribute("codeTasks", codeTasks);
        model.addAttribute("outputTasks", outputTasks);
        return "editCodingTask";
    }

    @GetMapping("/createOutputTask")
    String showCreateOutputTaskPage(Model model, Principal principal) {
        model.addAttribute("userName", principal.getName());

        var tasks = taskService.getAllTasks();
        var codeTasks = accumulate(tasks, CodeTask.CODE_TASK_TYPE, principal.getName());
        var outputTasks = accumulate(tasks, CodeTask.OUTPUT_TASK_TYPE, principal.getName());

        model.addAttribute("codeTasks", codeTasks);
        model.addAttribute("outputTasks", outputTasks);
        return "createOutputTask";
    }

    @GetMapping("/editOutputTask")
    String showEditOutputTaskPage(Model model, Principal principal) {
        model.addAttribute("userName", principal.getName());

        var tasks = taskService.getAllTasks();
        var codeTasks = accumulate(tasks, CodeTask.CODE_TASK_TYPE, principal.getName());
        var outputTasks = accumulate(tasks, CodeTask.OUTPUT_TASK_TYPE, principal.getName());

        model.addAttribute("codeTasks", codeTasks);
        model.addAttribute("outputTasks", outputTasks);
        return "editOutputTask";
    }

    private TaskPair[] accumulate(CodeTask[] tasks, String type, String username) {
        return Arrays.stream(tasks)
                .filter(t -> t.getTaskType().equals(type))
                .map(t -> {
                    var taskAndSolution = solutionService.getTaskAndSolution(t.getName(), username);
                    String taskState = getState(taskAndSolution.evaluation());
                    return new TaskPair(taskAndSolution.task(), taskState);
                }).toArray(TaskPair[]::new);
    }

    private String getState(CodeEvaluation[] evaluations) {
        if (evaluations == null) return "not_attempted.png";
        return Arrays.stream(evaluations).filter(e -> !e.isCorrect()).findFirst().map(e -> "not_solved.png").orElse("correct.png");
    }

}
