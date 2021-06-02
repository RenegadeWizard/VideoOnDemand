package com.renegade.videoondemand.api;

import com.renegade.videoondemand.domain.entity.Series;
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
    public void updateShow(@PathVariable String sid) {
        Optional<Series> optionalShow = showRepository.findById(Integer.parseInt(sid));
        if (optionalShow.isPresent()) {
            // TODO
        } else {
            throw new ObjectNotInDatabaseException(sid, "SHOWS");
        }
    }

    @DeleteMapping("/{sid}")
    public void deleteShow(@PathVariable String sid) {
        showRepository.deleteById(Integer.parseInt(sid));
    }

    @PatchMapping("/{sid}")
    public void patchShow(@PathVariable String sid) {
        Optional<Series> optionalShow = showRepository.findById(Integer.parseInt(sid));
        if (optionalShow.isPresent()) {
            // TODO
        } else {
            throw new ObjectNotInDatabaseException(sid, "SHOWS");
        }
    }

}
