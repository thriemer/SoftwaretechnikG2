package de.softwaretechnik.coder.application;

import de.softwaretechnik.coder.domain.CodeEvaluation;
import de.softwaretechnik.coder.domain.Task;

import java.util.List;

public interface DBAbstraction {
    List<String> getTaskNames();

    Task getTaskByName(String name);

    CodeEvaluation getCodeEvaluationByName(String taskName);

    Task[] getAllTasks();
}
