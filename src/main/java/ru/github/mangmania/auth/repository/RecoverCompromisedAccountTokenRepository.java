package ru.github.mangmania.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.github.mangmania.auth.entity.RecoverCompromisedAccountToken;

public interface RecoverCompromisedAccountTokenRepository extends JpaRepository<RecoverCompromisedAccountToken, String> {
}
