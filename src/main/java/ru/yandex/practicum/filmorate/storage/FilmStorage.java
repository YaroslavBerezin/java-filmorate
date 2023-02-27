package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film addFilm(Film film);

    Film updateFilm(Film film) throws ValidationException, IncorrectIdException;

    List<Film> getAllFilms();

    Film getFilmById(Integer id) throws IncorrectIdException;
}
