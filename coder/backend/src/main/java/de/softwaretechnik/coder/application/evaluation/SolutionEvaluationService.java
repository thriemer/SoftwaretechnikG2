package de.softwaretechnik.coder.application.evaluation;

import de.softwaretechnik.coder.adapter.secondary.EvaluationRepository;
import de.softwaretechnik.coder.application.DBAbstraction;
import de.softwaretechnik.coder.application.evaluation.compiler.JavaSourceValidator;
import de.softwaretechnik.coder.domain.CodeEvaluation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SolutionEvaluationService {

    private final DBAbstraction dbAbstraction;
    private final JavaSourceValidator javaSourceValidator;
    private final EvaluationRepository evaluationRepository;

    @Transactional
    public CodeEvaluation[] evaluateSubmission(String code, String taskName, long solutionId) {
        var codeEvaluation = dbAbstraction.getCodeSampleSolutionByName(taskName);
        var evaluations = javaSourceValidator.testProgram(code, codeEvaluation);
        evaluationRepository.deleteBySubmittedSolutionId(solutionId);
        for (var evaluation : evaluations) {
            evaluation.setSubmittedSolutionId(solutionId);
            evaluationRepository.save(evaluation);
        }
        return evaluations;
    }

    public CodeEvaluation[] getEvaluation(long solutionId) {
        return evaluationRepository.findAllBySubmittedSolutionId(solutionId);
    }

}
