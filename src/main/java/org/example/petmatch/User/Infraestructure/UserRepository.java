package org.example.petmatch.User.Infraestructure;

import org.example.petmatch.User.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Long, User> {
    Optional<User> findByName(String username);
}
