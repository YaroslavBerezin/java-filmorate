package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectArgumentException;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
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
        getUserById(id).getFriendsIds().add(Long.valueOf(friendId));
        log.debug("Friend with id '" + friendId + "' is added");
        return getUserById(id);
    }

    public User deleteFriend(Integer id, Integer friendId) throws IncorrectIdException, IncorrectArgumentException {
        if (!getUserById(id).getFriendsIds().contains(Long.valueOf(friendId))) {
            log.debug("User has not friend with id '" + friendId + "'");
            throw new IncorrectArgumentException("User has not friend with id '" + friendId + "'");
        }

        getUserById(id).getFriendsIds().remove(Long.valueOf(friendId));
        log.debug("Friend with id '" + friendId + "' is deleted");
        return getUserById(id);
    }

    public Set<Long> getAllFriendsIds(Integer id) throws IncorrectIdException, IncorrectArgumentException {
        if (getUserById(id).getFriendsIds() == null) {
            log.debug("User '" + getUserById(id).getName() + "' has not friends list");
            throw new IncorrectArgumentException("User '" + getUserById(id).getName() + "' has not friends list");
        }

        log.debug("Friends ids of '" + getUserById(id).getName() + "' returned");
        return getUserById(id).getFriendsIds();
    }


    public Set<Long> getCommonFriendsIds(Integer id, Integer otherId) throws IncorrectIdException, IncorrectArgumentException {
        User user = getUserById(id);
        User otherUser = getUserById(otherId);
        Set<Long> commonFriends = new HashSet<>();

        if (user.getFriendsIds() == null) {
            log.debug("User '" + user.getName() + "' has not friends list");
            throw new IncorrectArgumentException("User '" + user.getName() + "' has not friends list");
        }
        if (otherUser.getFriendsIds() == null) {
            log.debug("User '" + otherUser.getName() + "' has not friends list");
            throw new IncorrectArgumentException("User '" + otherUser.getName() + "' has not friends list");
        }

        for (Long idFriend : user.getFriendsIds()) {
            for (Long otherIdFriend : otherUser.getFriendsIds()) {
                if (Objects.equals(idFriend, otherIdFriend)) {
                    commonFriends.add(idFriend);
                }
            }
        }

        log.debug("Common friends ids returned");
        return commonFriends;
    }
}
