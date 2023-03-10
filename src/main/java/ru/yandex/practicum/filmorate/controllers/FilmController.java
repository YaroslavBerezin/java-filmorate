package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private int id = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    private Film addFilm(@RequestBody Film film) {
        validate(film);
        film.setId(++id);
        films.put(film.getId(), film);
        log.debug("Film '" + film.getName() + "' added successfully");
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws ValidationException, IncorrectIdException {
        validate(film);

        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.debug("Film '" + film.getName() + "' updated successfully");
            return film;
        } else {
            log.debug("Incorrect id");
            throw new IncorrectIdException("Incorrect id");
        }
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    public static void validate(Film film) {
        if (film.getName().isBlank()) {
            log.debug("Validation error: Films name can't be blank");
            throw new ValidationException("Films name can't be blank");
        }

        if (film.getDescription().length() > 200) {
            log.debug("Validation error: Films description can't be longer than 200 chars");
            throw new ValidationException("Films description can't be longer than 200 chars");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.debug("Validation error: Films release date can't be before 28.12.1895");
            throw new ValidationException("Films release date can't be before 28.12.1895");
        }

        if (film.getDuration() < 0) {
            log.debug("Validation error: Films duration can't be negative");
            throw new ValidationException("Films duration can't be negative");
        }
    }
}
