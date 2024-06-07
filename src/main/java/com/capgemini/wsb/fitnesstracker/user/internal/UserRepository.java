package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    default List<User> findByEmailContainingIgnoreCase(String emailFragment) {
        return findAll().stream()
                .filter(user -> user.getEmail().toLowerCase().contains(emailFragment.toLowerCase()))
                .collect(Collectors.toList());
    }

    default List<User> findByAgeGreaterThan(LocalDate cutoffDate) {
        return findAll().stream()
                .filter(user -> user.getBirthdate().isBefore(cutoffDate))
                .collect(Collectors.toList());
    }
}
