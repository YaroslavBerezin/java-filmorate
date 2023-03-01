package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectArgumentException;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    private User createUser(@RequestBody User user) throws ValidationException {
        return service.createUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) throws ValidationException, IncorrectIdException {
        return service.updateUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return service.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) throws IncorrectIdException {
        return service.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id,
                          @PathVariable Integer friendId) throws IncorrectIdException {
        service.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Integer id,
                             @PathVariable Integer friendId) throws IncorrectIdException, IncorrectArgumentException {
        service.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@PathVariable Integer id) throws IncorrectIdException {
        return service.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<User> getCommonFriends(@PathVariable Integer id,
                                       @PathVariable Integer otherId) throws IncorrectIdException {
        return service.getCommonFriendsIds(id, otherId);
    }
}
