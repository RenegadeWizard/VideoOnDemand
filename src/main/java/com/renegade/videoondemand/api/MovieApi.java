package com.renegade.videoondemand.api;

import com.renegade.videoondemand.domain.entity.Movie;
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
    public void updateMovie(@PathVariable String mid) {
        Optional<Movie> optionalVideo = movieRepository.findById(Integer.parseInt(mid));
        if (optionalVideo.isPresent()) {
            // TODO
        } else {
            throw new ObjectNotInDatabaseException(mid, "MOVIES");
        }
    }

    @DeleteMapping("/{mid}")
    public void deleteMovie(@PathVariable String mid) {
        movieRepository.deleteById(Integer.parseInt(mid));
    }

    @PatchMapping("/{mid}")
    public void patchMovie(@PathVariable String mid) {
        Optional<Movie> optionalVideo = movieRepository.findById(Integer.parseInt(mid));
        if (optionalVideo.isPresent()) {
            // TODO
        } else {
            throw new ObjectNotInDatabaseException(mid, "MOVIES");
        }
    }
}
