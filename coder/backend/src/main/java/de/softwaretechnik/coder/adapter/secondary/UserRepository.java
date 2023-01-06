package de.softwaretechnik.coder.adapter.secondary;

import de.softwaretechnik.coder.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User,Long> {
    Optional<User> findByUserName(String userName);
}
