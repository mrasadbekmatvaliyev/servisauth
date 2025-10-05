package com.example.user_auth.Repository;

import com.example.user_auth.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByTelegramId(Long telegramId);
    Optional<User> findByPhone(String phone);
}
