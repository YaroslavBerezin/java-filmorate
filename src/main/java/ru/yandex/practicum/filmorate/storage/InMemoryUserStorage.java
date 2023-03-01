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

@Component
@Slf4j
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
            log.debug("Incorrect ID error: User with this ID does not exist when updating");
            throw new IncorrectIdException("User with this ID does not exist when updating");
        }

        users.put(user.getId(), user);
        log.debug("User '" + user.getName() + "' updated successfully");
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        log.debug("All users returned successfully");
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Integer id) throws IncorrectIdException {
        if (!users.containsKey(id)) {
            log.debug("Incorrect ID error: User with ID '" + id + "' does not exist when getting by ID");
            throw new IncorrectIdException("User with ID '" + id + "' does not exist when getting by ID");
        }

        log.debug("User with ID '" + id + "' returned successfully");
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
