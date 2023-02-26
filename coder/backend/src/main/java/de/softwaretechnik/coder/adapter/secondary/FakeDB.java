package de.softwaretechnik.coder.adapter.secondary;

import de.softwaretechnik.coder.adapter.secondary.sample_solution.SampleSolutionConverter;
import de.softwaretechnik.coder.adapter.secondary.sample_solution.SampleSolutionRepository;
import de.softwaretechnik.coder.application.DBAbstraction;
import de.softwaretechnik.coder.domain.CodeSampleSolution;
import de.softwaretechnik.coder.domain.CodeTask;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

@Component
@RequiredArgsConstructor
public class FakeDB implements DBAbstraction {

    private final TaskRepository taskRepository;
    private final SampleSolutionConverter sampleSolutionConverter;
    private final SampleSolutionRepository sampleSolutionRepository;

    public List<String> getTaskNames() {
        return StreamSupport.stream(taskRepository.findAll().spliterator(), false).map(CodeTask::getName).toList();
    }

    public CodeTask getTaskByName(String name) {
        return taskRepository.findByName(name).orElse(null);
    }

    public CodeSampleSolution getCodeSampleSolutionByName(String taskName) {
        return sampleSolutionConverter.convertToDomainObject(
                sampleSolutionRepository.findByTaskName(taskName)
        );
    }

    public CodeTask[] getAllTasks() {
        return StreamSupport.stream(taskRepository.findAll().spliterator(), false).toArray(CodeTask[]::new);
    }

    @Transactional
    @Override
    public void saveTask(CodeTask task) {
        deleteTask(task);
        taskRepository.save(task);
    }

    @Transactional
    @Override
    public void saveSampleSolution(CodeSampleSolution sampleSolution) {
        if (sampleSolutionRepository.findByTaskName(sampleSolution.taskName()) != null) {
            sampleSolutionRepository.deleteByTaskName(sampleSolution.taskName());
        }
        sampleSolutionRepository.save(
                sampleSolutionConverter.convertToAdapterObject(sampleSolution)
        );
    }

    @Transactional
    @Override
    public void deleteTask(CodeTask task) {
        if (taskRepository.findByName(task.getName()).isPresent()) {
            taskRepository.deleteByName(task.getName());
        }
        if (sampleSolutionRepository.findByTaskName(task.getName()) != null) {
            sampleSolutionRepository.deleteByTaskName(task.getName());
        }
    }

}
