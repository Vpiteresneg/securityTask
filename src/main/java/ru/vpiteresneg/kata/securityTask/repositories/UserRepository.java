package ru.vpiteresneg.kata.securityTask.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vpiteresneg.kata.securityTask.model.User;


import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
