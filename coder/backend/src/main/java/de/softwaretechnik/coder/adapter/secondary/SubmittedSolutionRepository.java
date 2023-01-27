package de.softwaretechnik.coder.adapter.secondary;

import de.softwaretechnik.coder.domain.SubmittedSolution;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SubmittedSolutionRepository extends CrudRepository<SubmittedSolution, Long> {

    Optional<SubmittedSolution> findByUserNameAndTaskName(String userName, String taskName);

}
