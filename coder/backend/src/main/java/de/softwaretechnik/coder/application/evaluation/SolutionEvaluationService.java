package de.softwaretechnik.coder.application.evaluation;

import de.softwaretechnik.coder.adapter.secondary.EvaluationRepository;
import de.softwaretechnik.coder.application.DBAbstraction;
import de.softwaretechnik.coder.application.evaluation.compiler.JavaSourceValidator;
import de.softwaretechnik.coder.domain.CodeEvaluation;
import de.softwaretechnik.coder.domain.CodeSampleSolution;
import de.softwaretechnik.coder.domain.CodeTask;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class SolutionEvaluationService {

    private final DBAbstraction dbAbstraction;
    private final JavaSourceValidator javaSourceValidator;
    private final EvaluationRepository evaluationRepository;

    @Transactional
    public CodeEvaluation[] evaluateSubmission(String code, String taskName, long solutionId) {
        var sampleSolution = dbAbstraction.getCodeSampleSolutionByName(taskName);
        var codeTask = dbAbstraction.getTaskByName(taskName);
        var evaluations = evaluateSolution(codeTask, code, sampleSolution);
        evaluationRepository.deleteBySubmittedSolutionId(solutionId);
        for (var evaluation : evaluations) {
            evaluation.setSubmittedSolutionId(solutionId);
            evaluationRepository.save(evaluation);
        }
        return evaluations;
    }

    private CodeEvaluation[] evaluateSolution(CodeTask codeTask, String submission, CodeSampleSolution sampleSolution) {
        if (codeTask.getTaskType().equals(CodeTask.CODE_TASK_TYPE)) {
            return javaSourceValidator.testProgram(submission, sampleSolution);
        }
        // output of task
        return new CodeEvaluation[]{
                evaluateOutputOfCode(sampleSolution, submission)
        };
    }

    private CodeEvaluation evaluateOutputOfCode(CodeSampleSolution sampleSolution, String submission) {
        String expected = (String) sampleSolution.expectedOutput()[0];
        boolean correct = expected.equals(submission);
        String wrongMessage = CodeEvaluation.ERROR_MESSAGE_TEMPLATE.formatted("[]", expected, submission);
        String correctMessage = CodeEvaluation.CORRECT_MESSAGE_TEMPLATE.formatted("[]", submission);
        return new CodeEvaluation(correct, correct ? correctMessage : wrongMessage);
    }

    public CodeSampleSolution getCodeSampleSolution(String taskName) {
        return dbAbstraction.getCodeSampleSolutionByName(taskName);
    }

    public CodeEvaluation[] getEvaluation(long solutionId) {
        return evaluationRepository.findAllBySubmittedSolutionId(solutionId);
    }

    public void createSampleSolution(CodeSampleSolution codeSampleSolution) {
        dbAbstraction.saveSampleSolution(codeSampleSolution);
    }
}
