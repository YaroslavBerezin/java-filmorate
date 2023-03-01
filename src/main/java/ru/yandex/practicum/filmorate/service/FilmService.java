package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectArgumentException;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) throws ValidationException, IncorrectIdException {
        return filmStorage.updateFilm(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(Integer id) throws IncorrectIdException, IncorrectArgumentException {
        return filmStorage.getFilmById(id);
    }

    public Film likeFilm(Integer id, Integer userId) throws IncorrectIdException, IncorrectArgumentException {
        Film film = getFilmById(id);
        film.getLikesIds().add(userId);
        log.debug("Film with ID '" + id + "' successfully liked by user with ID '" + userId + "'");
        return film;
    }

    public Film unlikeFilm(Integer id, Integer userId) throws IncorrectIdException, IncorrectArgumentException {
        Film film = getFilmById(id);

        if (!film.getLikesIds().contains(userId)) {
            log.debug("Incorrect Argument error: Film '" + film + "' has not likes from user with ID '" + userId + "' when unliking");
            throw new IncorrectArgumentException("Film '" + film + "' has not likes from user with ID '" + userId + "' when unliking");
        }

        film.getLikesIds().remove(userId);
        log.debug("Film with id '" + id + "' successfully unliked by user with ID '" + userId + "'");
        return film;
    }

    public List<Film> getMostLikedFilms(Integer count) {
        List<Film> films = new ArrayList<>(getAllFilms());

        log.debug("Most popular films returned successfully");
        return films.stream()
                .sorted(this::compare)
                .skip(0)
                .limit(count)
                .collect(Collectors.toList());
    }

    private int compare(Film f0, Film f1) {
        int result = f0.getLikesIds().size() - (f1.getLikesIds().size());
        result = -1 * result;
        return result;
    }
}
