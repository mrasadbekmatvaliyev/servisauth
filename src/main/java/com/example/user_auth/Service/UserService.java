package com.example.user_auth.Service;

import com.example.user_auth.Model.User;
import com.example.user_auth.Repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addOrGetUser(Long telegramId, String firstName, String lastName, String phone) {
        Optional<User> existing = userRepository.findByTelegramId(telegramId);
        if (existing.isPresent()) {
            return existing.get();
        } else {
            User user = new User();
            user.setTelegramId(telegramId);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPhone(phone);
            return userRepository.save(user);
        }
    }
}
