package de.softwaretechnik.coder.application.tasks;
import de.softwaretechnik.coder.application.tasks.TaskService;
import de.softwaretechnik.coder.application.DBAbstraction;
import de.softwaretechnik.coder.domain.CodeTask;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {


    private final TaskService taskService;

    private final DBAbstraction dbAbstraction;


    public void createTask(String name, String displayName, String shortDescription, String taskType, String taskDescription, String codeTemplate) {
        taskService.createTask(name, displayName, shortDescription, taskType, taskDescription, codeTemplate);
    }




}
