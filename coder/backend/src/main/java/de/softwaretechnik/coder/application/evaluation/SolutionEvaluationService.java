package de.softwaretechnik.coder.application.evaluation;

import de.softwaretechnik.coder.application.DBAbstraction;
import de.softwaretechnik.coder.application.evaluation.compiler.JavaSourceValidator;
import de.softwaretechnik.coder.domain.CodeEvaluation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SolutionEvaluationService {

    private final DBAbstraction dbAbstraction;
    private final JavaSourceValidator javaSourceValidator;

    public CodeEvaluation[] evaluateSubmission(String code, String taskName){
        var codeEvaluation = dbAbstraction.getCodeSampleSolutionByName(taskName);
        return javaSourceValidator.testProgram(code, codeEvaluation);
    }

}
