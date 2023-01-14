package de.softwaretechnik.coder.application.evaluation;

import de.softwaretechnik.coder.domain.CodeEvaluation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SolutionSubmitService {

    private final SolutionEvaluationService solutionEvaluationService;
    private final SolutionSaveService solutionSaveService;

    public CodeEvaluation[] submitSolution(String userId, String code, String taskName) {
        solutionSaveService.saveSolution(userId, taskName, code);
        return solutionEvaluationService.evaluateSubmission(code, taskName);
    }

}
