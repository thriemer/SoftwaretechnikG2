package de.softwaretechnik.coder.adapter.primary;

import de.softwaretechnik.coder.application.evaluation.SolutionService;
import de.softwaretechnik.coder.application.tasks.TaskService;
import de.softwaretechnik.coder.domain.CodeEvaluation;
import de.softwaretechnik.coder.domain.CodeSampleSolution;
import de.softwaretechnik.coder.domain.CodeTask;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/denied")
    String showAccessDeniedPage() {
        return "denied";
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
        return "admin/admin";
    }

    @GetMapping("/admin/editCodingTask")
    String showEditCodingTaskPage(Model model, Principal principal, @RequestParam(required = false, defaultValue = "false") boolean createNewTask, @RequestParam(required = false) String taskName) {
        model.addAttribute("userName", principal.getName());

        CodeTask codeTask;
        String sampleSolutionCSV;
        if (createNewTask) {
            codeTask = new CodeTask("new-task", "New Task", "Short description", CodeTask.CODE_TASK_TYPE, "long description", "public class SolveThisCode{}");
            sampleSolutionCSV = "method\ntype|type|type\nval1|val2|outputVal";
        } else {
            codeTask = taskService.getTaskByName(taskName);
            sampleSolutionCSV = solutionService.getCodeSampleSolution(taskName).toCSVString();
        }
        model.addAttribute("task", codeTask);
        model.addAttribute("sampleSolution", sampleSolutionCSV);
        return "admin/editCodingTask";
    }

    record EditedCodeTask(String taskname, String displayname, String description, String shortdescription, String code,
                          String test) {
    }

    @PostMapping("/admin/editCodingTask")
    String saveEditedCodingTask(@ModelAttribute EditedCodeTask codeTask, Model model, Principal principal) {
        model.addAttribute("userName", principal.getName());

        String cleanTaskName = codeTask.taskname.replace("\n", "").replace("\r", "");

        CodeTask createdCodetask = new CodeTask(cleanTaskName, codeTask.displayname, codeTask.shortdescription, CodeTask.CODE_TASK_TYPE, codeTask.description, codeTask.code);
        CodeSampleSolution codeSampleSolution = CodeSampleSolution.fromCSVString(cleanTaskName, codeTask.test.replace("\r", ""));

        model.addAttribute("task", createdCodetask);
        model.addAttribute("sampleSolution", codeTask.test);

        taskService.createTask(createdCodetask);
        solutionService.createSampleSolution(codeSampleSolution);

        return "admin/editCodingTask";
    }

    @GetMapping("/admin/createOutputTask")
    String showCreateOutputTaskPage(Model model, Principal principal) {
        model.addAttribute("userName", principal.getName());

        var tasks = taskService.getAllTasks();
        var codeTasks = accumulate(tasks, CodeTask.CODE_TASK_TYPE, principal.getName());
        var outputTasks = accumulate(tasks, CodeTask.OUTPUT_TASK_TYPE, principal.getName());

        model.addAttribute("codeTasks", codeTasks);
        model.addAttribute("outputTasks", outputTasks);
        return "admincreateOutputTask";
    }

    @GetMapping("/admin/editOutputTask")
    String showEditOutputTaskPage(Model model, Principal principal) {
        model.addAttribute("userName", principal.getName());

        var tasks = taskService.getAllTasks();
        var codeTasks = accumulate(tasks, CodeTask.CODE_TASK_TYPE, principal.getName());
        var outputTasks = accumulate(tasks, CodeTask.OUTPUT_TASK_TYPE, principal.getName());

        model.addAttribute("codeTasks", codeTasks);
        model.addAttribute("outputTasks", outputTasks);
        return "admin/editOutputTask";
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
