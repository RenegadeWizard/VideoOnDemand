package com.renegade.videoondemand.api;

import com.renegade.videoondemand.domain.entity.Movie;
import com.renegade.videoondemand.domain.repository.FavoritesRepository;
import com.renegade.videoondemand.domain.repository.MovieRepository;
import com.renegade.videoondemand.exception.ObjectNotInDatabaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieApi {
    private final MovieRepository movieRepository;
    private final FavoritesRepository favoritesRepository;

    @GetMapping
    public Page<Movie> getMovies(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    @PostMapping
    public void addNewMovie(@RequestBody Movie movie) {
        movieRepository.save(movie);
    }

    @GetMapping("/{mid}")
    public Movie getMovie(@PathVariable String mid) {
        Optional<Movie> optionalVideo = movieRepository.findById(Integer.parseInt(mid));
        return optionalVideo.orElse(null);
    }

    @PutMapping("/{mid}")
    public void updateMovie(@PathVariable String mid, @RequestBody Movie movie) {
        movieRepository.findById(Integer.parseInt(mid)).orElseThrow(ObjectNotInDatabaseException::new);
        movie.setId(Integer.parseInt(mid));
        movieRepository.save(movie);
    }

    @DeleteMapping("/{mid}")
    public void deleteMovie(@PathVariable String mid) {
        Movie movie = movieRepository.findById(Integer.parseInt(mid)).orElseThrow(ObjectNotInDatabaseException::new);
        favoritesRepository.findAllByVideoEquals(movie).forEach(favoritesRepository::delete);
        movieRepository.deleteById(Integer.parseInt(mid));
    }

    @PatchMapping("/{mid}")
    public void patchMovie(@PathVariable String mid, @RequestBody Movie movie) {
        Movie movieToUpdate = movieRepository.findById(Integer.parseInt(mid)).orElseThrow(ObjectNotInDatabaseException::new);
        if (movie.getName() != null) {
            movieToUpdate.setName(movie.getName());
        }
        if (movie.getDescription() != null) {
            movieToUpdate.setDescription(movie.getDescription());
        }
        if (movie.getTime() != null) {
            movieToUpdate.setTime(movie.getTime());
        }
        if (movie.getReleaseYear() != null) {
            movieToUpdate.setReleaseYear(movie.getReleaseYear());
        }
        movieRepository.save(movieToUpdate);
    }
}
