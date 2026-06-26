package ru.github.mangmania.auth.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.github.mangmania.auth.entity.User;
import ru.github.mangmania.auth.repository.UserRepository;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCleanupScheduler {
    private final UserRepository userRepository;

    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional
    public void deleteExpiredAccounts() {
        Date now = new Date();

        List<User> usersToDelete = userRepository.findAllByDeletionDateBefore(now);

        if (!usersToDelete.isEmpty()) {
            userRepository.deleteAll(usersToDelete);
            System.out.println("Удалено пользователей: " + usersToDelete.size());
        }
    }
}
