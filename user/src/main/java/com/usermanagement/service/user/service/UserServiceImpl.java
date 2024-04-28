package com.usermanagement.service.user.service;

import com.usermanagement.service.user.entity.User;
import com.usermanagement.service.user.payload.UserEvent;
import com.usermanagement.service.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaTemplate<String, UserEvent> kafkaTemplate;

    @Override
    public User createUser(User user) {
        User createdUser = userRepository.save(user);
        UserEvent userEvent = new UserEvent(UserEvent.EventType.CREATED, createdUser, LocalDateTime.now());
        kafkaTemplate.send("user-events", userEvent);
        logger.info("User created: " + createdUser);

        return createdUser;
    }

    @Override
    public User updateUser(User user) {
        User updatedUser = userRepository.save(user);
        UserEvent userEvent = new UserEvent(UserEvent.EventType.UPDATED, updatedUser, LocalDateTime.now());
        kafkaTemplate.send("user-events", userEvent);
        logger.info("User updated: " + updatedUser);
        return updatedUser;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        users.stream().map(user -> new UserEvent(UserEvent.EventType.RETRIEVED, user, LocalDateTime.now()))
                .forEach(userEvent -> kafkaTemplate.send("user-events", userEvent));
        return users;
    }

    @Override
    public User getUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            UserEvent userEvent = new UserEvent(UserEvent.EventType.RETRIEVED, user, LocalDateTime.now());
            kafkaTemplate.send("user-events", userEvent);
            logger.info("User retrieved: " + user);
        }
        return user;
    }

    @Override
    public boolean deleteUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
