package de.softwaretechnik.coder.adapter.secondary;

import de.softwaretechnik.coder.application.DBAbstraction;
import de.softwaretechnik.coder.domain.CodeSampleSolution;
import de.softwaretechnik.coder.domain.CodeTask;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

@Component
@RequiredArgsConstructor
public class FakeDB implements DBAbstraction {

    private final TaskRepository taskRepository;

    private final List<CodeSampleSolution> allEvaluations = List.of(
            new CodeSampleSolution("addTwoNumbers", "add",
                    new Object[][]{{5, 4}, {11, 12}},
                    new Object[]{9, 23}
            ),
            new CodeSampleSolution("reverseString", "reverseString",
                    new Object[][]{{"HelloWorld!"}, {"I've seen enough of Ba Sing Sei. And I can't even see! ~ Toph"}},
                    new Object[]{"!dlroWolleH", "hpoT ~ !ees neve t'nac I dnA .ieS gniS aB fo hguone nees ev'I"}
            )
    );

    public List<String> getTaskNames() {
        return StreamSupport.stream(taskRepository.findAll().spliterator(), false).map(CodeTask::getName).toList();
    }

    public CodeTask getTaskByName(String name) {
        return taskRepository.findByName(name).orElse(null);
    }

    public CodeSampleSolution getCodeSampleSolutionByName(String taskName) {
        return allEvaluations.stream().filter(e -> e.taskName().equals(taskName)).findFirst().orElse(null);
    }

    public CodeTask[] getAllTasks() {
        return StreamSupport.stream(taskRepository.findAll().spliterator(), false).toArray(CodeTask[]::new);
    }

    @Override
    public void saveTask(CodeTask task) {
        taskRepository.save(task);
    }

}
