package com.renegade.videoondemand.api;

import com.renegade.videoondemand.domain.entity.Series;
import com.renegade.videoondemand.domain.repository.FavoritesRepository;
import com.renegade.videoondemand.domain.repository.ShowRepository;
import com.renegade.videoondemand.exception.ObjectNotInDatabaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/shows")
@RequiredArgsConstructor
public class ShowApi {
    private final ShowRepository showRepository;
    private final FavoritesRepository favoritesRepository;

    @GetMapping()
    public Page<Series> getAllShows(Pageable pageable) {
        return showRepository.findAll(pageable);
    }

    @PostMapping
    public void addNewShow(@RequestBody Series series) {
        showRepository.save(series);
    }

    @GetMapping("/{sid}")
    public Series getShow(@PathVariable String sid) {
        Optional<Series> optionalShow = showRepository.findById(Integer.parseInt(sid));
        return optionalShow.orElse(null);
    }

    @PutMapping("/{sid}")
    public void updateShow(@PathVariable String sid, @RequestBody Series show) {
        showRepository.findById(Integer.parseInt(sid)).orElseThrow(ObjectNotInDatabaseException::new);
        show.setId(Integer.parseInt(sid));
        showRepository.save(show);
    }

    @DeleteMapping("/{sid}")
    public void deleteShow(@PathVariable String sid) {
        Series show = showRepository.findById(Integer.parseInt(sid)).orElseThrow(ObjectNotInDatabaseException::new);
        favoritesRepository.findAllByVideoEquals(show).forEach(favoritesRepository::delete);
        showRepository.deleteById(Integer.parseInt(sid));
    }

    @PatchMapping("/{sid}")
    public void patchShow(@PathVariable String sid, @RequestBody Series show) {
        Series showToUpdate = showRepository.findById(Integer.parseInt(sid)).orElseThrow(ObjectNotInDatabaseException::new);
        if (show.getName() != null) {
            showToUpdate.setName(show.getName());
        }
        if (show.getSeasons() != null) {
            showToUpdate.setSeasons(show.getSeasons());
        }
        if (show.getReleaseYear() != null) {
            showToUpdate.setReleaseYear(show.getReleaseYear());
        }
        showRepository.save(showToUpdate);
    }

}
