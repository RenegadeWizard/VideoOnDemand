package com.renegade.videoondemand.api;

import com.renegade.videoondemand.domain.entity.Series;
import com.renegade.videoondemand.domain.repository.FavoritesRepository;
import com.renegade.videoondemand.domain.repository.ShowRepository;
import com.renegade.videoondemand.exception.ObjectNotInDatabaseException;
import com.renegade.videoondemand.util.EtagHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Series> getShow(@PathVariable String sid) {
        Series show = showRepository
                .findById(Integer.parseInt(sid))
                .orElseThrow(ObjectNotInDatabaseException::new);
        return ResponseEntity.ok()
                .eTag(show.getVersion().toString())
                .body(show);
    }

    @PutMapping("/{sid}")
    public void updateShow(@PathVariable String sid, @RequestBody Series show,
                           @RequestHeader("If-Match") String ifMatch) {
        Series showInDatabase = showRepository.findById(Integer.parseInt(sid)).orElseThrow(ObjectNotInDatabaseException::new);
        EtagHelper.checkEtagCorrectness(showInDatabase.getVersion(), ifMatch);
        showRepository.save(showInDatabase.cloneAll(show));
    }

    @DeleteMapping("/{sid}")
    public void deleteShow(@PathVariable String sid) {
        Series show = showRepository.findById(Integer.parseInt(sid)).orElseThrow(ObjectNotInDatabaseException::new);
        favoritesRepository.findAllByVideoEquals(show).forEach(favoritesRepository::delete);
        showRepository.deleteById(Integer.parseInt(sid));
    }

    @PatchMapping("/{sid}")
    public void patchShow(@PathVariable String sid, @RequestBody Series show,
                          @RequestHeader("If-Match") String ifMatch) {
        Series showToUpdate = showRepository.findById(Integer.parseInt(sid)).orElseThrow(ObjectNotInDatabaseException::new);
        EtagHelper.checkEtagCorrectness(showToUpdate.getVersion(), ifMatch);
        showRepository.save(showToUpdate.cloneSome(show));
    }

}
