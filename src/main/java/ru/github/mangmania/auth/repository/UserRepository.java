package ru.github.mangmania.auth.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.github.mangmania.auth.entity.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    List<User> findAllByDeletionDateBefore(Date date);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.settings WHERE u.id = :id")
    Optional<User> findByIdWithSettings(@Param("id") Long id);
}
