package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private int id = 0;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User createUser(User user) throws ValidationException {
        validate(user);
        user.setId(++id);
        users.put(user.getId(), user);
        log.debug("User '" + user.getName() + "' created successfully");
        return user;
    }

    @Override
    public User updateUser(User user) throws ValidationException, IncorrectIdException {
        validate(user);

        if (!users.containsKey(user.getId())) {
            log.debug("Incorrect id");
            throw new IncorrectIdException("Incorrect id");
        }

        users.put(user.getId(), user);
        log.debug("User '" + user.getName() + "' updated successfully");
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        log.debug("All users returned");
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Integer id) throws IncorrectIdException {
        if (!users.containsKey(id)) {
            log.debug("User with id '" + id + "' does not exist");
            throw new IncorrectIdException("User with id '" + id + "' does not exist");
        }

        log.debug("User with id '" + id + "' returned");
        return users.get(id);
    }

    public static void validate(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        if (user.getEmail().isBlank()) {
            log.debug("Validation error: Email name can't be blank");
            throw new ValidationException("Email name can't be blank");
        }

        if (!user.getEmail().contains("@")) {
            log.debug("Validation error: Email name should contain '@'");
            throw new ValidationException("Email name should contain '@'");
        }

        if (user.getLogin().isBlank()) {
            log.debug("Validation error: Login can't be blank");
            throw new ValidationException("Login can't be blank");
        }

        if (user.getLogin().contains(" ")) {
            log.debug("Validation error: Login can't contain spaces");
            throw new ValidationException("Login can't contain spaces");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Validation error: Users birthday can't be in the future");
            throw new ValidationException("Users birthday can't be in the future");
        }
    }
}
