package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService service;

    @PostMapping
    private User createUser(@RequestBody User user) {
        validate(user);
        return service.createUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        validate(user);
        return service.updateUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return service.getAllUsers();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Integer userId) {
        return service.getUserById(userId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id,
                          @PathVariable Integer friendId) {
        service.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Integer id,
                             @PathVariable Integer friendId) {
        service.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@PathVariable Integer id) {
        return service.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<User> getCommonFriends(@PathVariable Integer id,
                                      @PathVariable Integer otherId) {
        return service.getCommonFriendsIds(id, otherId);
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
