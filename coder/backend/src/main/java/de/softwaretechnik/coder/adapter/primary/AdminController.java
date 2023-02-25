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

@RequiredArgsConstructor
@Controller
public class AdminController {

    private final TaskService taskService;
    private final SolutionService solutionService;


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
                          String test, String taskType) {
    }

    @PostMapping("/admin/editTask")
    String saveEditedCodingTask(@ModelAttribute EditedCodeTask codeTask, Model model, Principal principal) {
        model.addAttribute("userName", principal.getName());

        String cleanTaskName = codeTask.taskname.replace("\n", "").replace("\r", "");

        CodeTask createdCodetask = new CodeTask(cleanTaskName, codeTask.displayname, codeTask.shortdescription, codeTask.taskType, codeTask.description, codeTask.code);
        CodeSampleSolution codeSampleSolution;
        if (codeTask.taskType.equals(CodeTask.CODE_TASK_TYPE)) {
            codeSampleSolution = CodeSampleSolution.fromCSVString(cleanTaskName, codeTask.test.replace("\r", ""));
        } else {
            codeSampleSolution = new CodeSampleSolution(cleanTaskName, "main", new Object[][]{{}},
                    new Object[]{codeTask.test.replace("\r", "")});
        }
        model.addAttribute("task", createdCodetask);
        model.addAttribute("sampleSolution", codeTask.test);

        taskService.createTask(createdCodetask);
        solutionService.createSampleSolution(codeSampleSolution);

        return codeTask.taskType.equals(CodeTask.CODE_TASK_TYPE) ? "admin/editCodingTask" : "admin/editOutputTask";
    }

    @GetMapping("/admin/editOutputTask")
    String showEditOutputTaskPage(Model model, Principal principal, @RequestParam(required = false, defaultValue = "false") boolean createNewTask, @RequestParam(required = false) String taskName) {
        model.addAttribute("userName", principal.getName());

        CodeTask codeTask;
        String correctOuput;
        if (createNewTask) {
            codeTask = new CodeTask("new-task", "New Task", "Short description", CodeTask.OUTPUT_TASK_TYPE, "long description", "public class SolveThisCode{}");
            correctOuput = "correctOuput";
        } else {
            codeTask = taskService.getTaskByName(taskName);
            correctOuput = solutionService.getCodeSampleSolution(taskName).expectedOutput()[0].toString();
        }
        model.addAttribute("task", codeTask);
        model.addAttribute("sampleSolution", correctOuput);
        return "admin/editOutputTask";
    }

}
