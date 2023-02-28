package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectArgumentException;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) throws ValidationException {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) throws ValidationException, IncorrectIdException {
        return userStorage.updateUser(user);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(Integer id) throws IncorrectIdException {
        return userStorage.getUserById(id);
    }

    public User addFriend(Integer id, Integer friendId) throws IncorrectIdException {
        if (getUserById(id).getFriendsIds() == null) {
            getUserById(id).setFriendsIds(new HashSet<>());
        }
        if (getUserById(friendId).getFriendsIds() == null) {
            getUserById(friendId).setFriendsIds(new HashSet<>());
        }

        getUserById(id).getFriendsIds().add(Long.valueOf(friendId));
        getUserById(friendId).getFriendsIds().add(Long.valueOf(id));
        return getUserById(id);
    }

    public User deleteFriend(Integer id, Integer friendId) throws IncorrectIdException, IncorrectArgumentException {
        if (getUserById(id).getFriendsIds() == null) {
            getUserById(id).setFriendsIds(new HashSet<>());
        }
        if (getUserById(friendId).getFriendsIds() == null) {
            getUserById(friendId).setFriendsIds(new HashSet<>());
        }

        if (!getUserById(id).getFriendsIds().contains(Long.valueOf(friendId))) {
            throw new IncorrectArgumentException("User has not friend with id '" + friendId + "'");
        }

        getUserById(id).getFriendsIds().remove(Long.valueOf(friendId));
        getUserById(friendId).getFriendsIds().remove(Long.valueOf(id));
        return getUserById(id);
    }

    public Set<Long> getAllFriendsIds(Integer id) throws IncorrectIdException {
        if (getUserById(id).getFriendsIds() == null) {
            getUserById(id).setFriendsIds(new HashSet<>());
        }

        return getUserById(id).getFriendsIds();
    }


    public Set<Long> getCommonFriendsIds(Integer id, Integer otherId) throws IncorrectIdException, IncorrectArgumentException {
        User user = getUserById(id);
        User otherUser = getUserById(otherId);
        Set<Long> commonFriends = new HashSet<>();

        if (user.getFriendsIds() == null | otherUser.getFriendsIds() == null) {
            return commonFriends;
        }

        for (Long idFriend : user.getFriendsIds()) {
            for (Long otherIdFriend : otherUser.getFriendsIds()) {
                if (Objects.equals(idFriend, otherIdFriend)) {
                    commonFriends.add(idFriend);
                }
            }
        }

        return commonFriends;
    }
}
