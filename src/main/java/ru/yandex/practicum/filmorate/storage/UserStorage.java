package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User createUser(User user) throws ValidationException;

    User updateUser(User user) throws ValidationException, IncorrectIdException;

    List<User> getAllUsers();

    User getUserById(Integer id) throws IncorrectIdException;
}
