package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectArgumentException;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
@Slf4j
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

    public void addFriend(Integer id, Integer friendId) throws IncorrectIdException {
        User user = getUserById(id);
        User friend = getUserById(friendId);

        if (user == null | friend == null) {
            log.debug("Incorrect ID error: Users do not exist when adding friend");
            throw new IncorrectIdException("Users do not exist when adding friend");
        }

        user.getFriendsIds().add(friendId);
        friend.getFriendsIds().add(id);
        log.debug("Friend with ID '" + friendId + "' successfully added to user '" + id + "'");
        log.debug("Friend with ID '" + id + "' successfully added to user '" + friendId + "'");
    }

    public void deleteFriend(Integer id, Integer friendId) throws IncorrectIdException, IncorrectArgumentException {
        User user = getUserById(id);
        User friend = getUserById(friendId);

        if (user == null | friend == null) {
            log.debug("Incorrect ID error: Users do not exist when deleting friend");
            throw new IncorrectIdException("Users do not exist when deleting friend");
        }
        if (!user.getFriendsIds().contains(friendId)) {
            log.debug("Incorrect ID error: User has not friend with id '" + friendId + "' when deleting friend");
            throw new IncorrectArgumentException("User has not friend with id '" + friendId + "' when deleting friend");
        }

        user.getFriendsIds().remove(friendId);
        friend.getFriendsIds().remove(id);
    }

    public List<User> getAllFriends(Integer id) throws IncorrectIdException {
        User user = getUserById(id);
        List<User> users = new ArrayList<>();

        for (Integer friendsId : user.getFriendsIds()) {
            users.add(getUserById(friendsId));
        }

        log.debug("All friends returned successfully");
        return users;
    }

    public Set<User> getCommonFriendsIds(Integer id, Integer otherId) throws IncorrectIdException {
        User user = getUserById(id);
        User otherUser = getUserById(otherId);
        Set<User> commonFriends = new HashSet<>();

        for (Integer idFriend : user.getFriendsIds()) {
            for (Integer otherIdFriend : otherUser.getFriendsIds()) {
                if (Objects.equals(idFriend, otherIdFriend)) {
                    commonFriends.add(getUserById(idFriend));
                }
            }
        }

        log.debug("Common friends returned successfully");
        return commonFriends;
    }
}
