package de.softwaretechnik.coder.application;

import de.softwaretechnik.coder.domain.CodeSampleSolution;
import de.softwaretechnik.coder.domain.CodeTask;

import java.util.List;

public interface DBAbstraction {
    List<String> getTaskNames();

    CodeTask getTaskByName(String name);

    CodeSampleSolution getCodeSampleSolutionByName(String taskName);

    CodeTask[] getAllTasks();

    void saveTask(CodeTask task);

    void saveSampleSolution(CodeSampleSolution sampleSolution);

}
