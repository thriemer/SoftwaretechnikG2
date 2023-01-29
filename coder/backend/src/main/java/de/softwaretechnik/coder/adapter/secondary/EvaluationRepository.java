package de.softwaretechnik.coder.adapter.secondary;

import de.softwaretechnik.coder.domain.CodeEvaluation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluationRepository extends CrudRepository<CodeEvaluation,Long> {

    void deleteBySubmittedSolutionId(long submittedSolutionId);

    CodeEvaluation[] findAllBySubmittedSolutionId(long submittedSolutionId);

}
