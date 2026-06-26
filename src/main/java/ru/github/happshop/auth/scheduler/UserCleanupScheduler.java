package ru.github.happshop.auth.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.github.happshop.auth.entity.User;
import ru.github.happshop.auth.repository.UserRepository;

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
