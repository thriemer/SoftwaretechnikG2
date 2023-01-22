package de.softwaretechnik.coder.application.tasks;

import de.softwaretechnik.coder.adapter.secondary.TaskRepository;
import de.softwaretechnik.coder.application.DBAbstraction;
import de.softwaretechnik.coder.domain.CodeTask;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    private final DBAbstraction dbAbstraction;

    public CodeTask[] getAllTasks(){
        return dbAbstraction.getAllTasks();
    }
    private static void saveTask(TaskRepository taskRepository,String name, String displayName, String shortDescription, String taskType, String taskDescription, String codeTemplate) {
        CodeTask task = new CodeTask(name,displayName,shortDescription,taskType,taskDescription,codeTemplate);
        taskRepository.save(task);
    }

    public void createTask(String name, String displayName, String shortDescription, String taskType, String taskDescription, String codeTemplate) {
        saveTask(this.taskRepository,name,displayName,shortDescription,taskType,taskDescription,codeTemplate);
    }

}
