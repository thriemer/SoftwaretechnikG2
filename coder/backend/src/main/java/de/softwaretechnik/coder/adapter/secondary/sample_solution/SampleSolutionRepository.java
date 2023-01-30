package de.softwaretechnik.coder.adapter.secondary.sample_solution;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleSolutionRepository extends CrudRepository<SampleSolutionSDTO, Long> {

    SampleSolutionSDTO findByTaskName(String taskName);

}
