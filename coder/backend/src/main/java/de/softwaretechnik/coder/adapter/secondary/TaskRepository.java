package de.softwaretechnik.coder.adapter.secondary;

import de.softwaretechnik.coder.domain.CodeTask;
import de.softwaretechnik.coder.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends CrudRepository<CodeTask,Long> {
    Optional<CodeTask> findByName(String name);
}
