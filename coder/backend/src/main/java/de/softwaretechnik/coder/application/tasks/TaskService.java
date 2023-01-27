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


    private final DBAbstraction dbAbstraction;

    public CodeTask[] getAllTasks() {
        return dbAbstraction.getAllTasks();
    }

    public void createTask(String name, String displayName, String shortDescription, String taskType, String taskDescription, String codeTemplate) {
        CodeTask task = new CodeTask(name, displayName, shortDescription, taskType, taskDescription, codeTemplate);
        dbAbstraction.saveTask(task);
    }

    public CodeTask getTaskByName(String taskName) {
        return dbAbstraction.getTaskByName(taskName);
    }
}
