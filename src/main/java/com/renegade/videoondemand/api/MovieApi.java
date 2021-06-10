package com.renegade.videoondemand.api;

import com.renegade.videoondemand.domain.entity.Movie;
import com.renegade.videoondemand.domain.repository.FavoritesRepository;
import com.renegade.videoondemand.domain.repository.MovieRepository;
import com.renegade.videoondemand.exception.ObjectNotInDatabaseException;
import com.renegade.videoondemand.util.EtagHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Movie> getMovie(@PathVariable String mid) {
        Movie movie = movieRepository
                .findById(Integer.parseInt(mid))
                .orElseThrow(ObjectNotInDatabaseException::new);
        return ResponseEntity.ok()
                .eTag(movie.getVersion().toString())
                .body(movie);
    }

    @PutMapping("/{mid}")
    public void updateMovie(@PathVariable String mid, @RequestBody Movie movie,
                            @RequestHeader("If-Match") String ifMatch) {
        Movie movieInDatabase = movieRepository
                .findById(Integer.parseInt(mid))
                .orElseThrow(ObjectNotInDatabaseException::new);
        EtagHelper.checkEtagCorrectness(movieInDatabase.getVersion(), ifMatch);
        movieRepository.save(movieInDatabase.cloneAll(movie));
    }

    @DeleteMapping("/{mid}")
    public void deleteMovie(@PathVariable String mid) {
        Movie movie = movieRepository.findById(Integer.parseInt(mid)).orElseThrow(ObjectNotInDatabaseException::new);
        favoritesRepository.findAllByVideoEquals(movie).forEach(favoritesRepository::delete);
        movieRepository.deleteById(Integer.parseInt(mid));
    }

    @PatchMapping("/{mid}")
    public void patchMovie(@PathVariable String mid, @RequestBody Movie movie, @RequestHeader("If-Match") String ifMatch) {
        Movie movieToUpdate = movieRepository.findById(Integer.parseInt(mid)).orElseThrow(ObjectNotInDatabaseException::new);
        EtagHelper.checkEtagCorrectness(movieToUpdate.getVersion(), ifMatch);
        movieRepository.save(movieToUpdate.cloneSome(movie));
    }
}
