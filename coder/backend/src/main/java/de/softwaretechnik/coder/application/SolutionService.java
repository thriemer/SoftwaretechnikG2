package de.softwaretechnik.coder.application;

import de.softwaretechnik.coder.application.compiler.JavaSourceValidator;
import de.softwaretechnik.coder.domain.Task;
import de.softwaretechnik.coder.domain.TestResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SolutionService {

    private final DBAbstraction dbAbstraction;
    private final JavaSourceValidator javaSourceValidator;

    public TestResult[] testResults(String code, String taskName) {
        var codeEvaluation = dbAbstraction.getCodeEvaluationByName(taskName);
        return javaSourceValidator.testProgram(code, codeEvaluation);
    }

    public List<String> getAllTaskNames() {
        return dbAbstraction.getTaskNames();
    }

    public Task getTaskByName(String name) {
        return dbAbstraction.getTaskByName(name);
    }

}
