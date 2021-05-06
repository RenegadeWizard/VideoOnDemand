package com.renegade.videoondemand.api;

import com.renegade.videoondemand.domain.entity.Video;
import com.renegade.videoondemand.domain.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/video")
@RequiredArgsConstructor
public class VideoApi {

    private final VideoRepository videoRepository;

    @GetMapping
    public Iterable<Video> getVideos() {
        return videoRepository.findAll();
    }

    @PostMapping
    public void addNewVideo(@RequestBody Video video) {
        videoRepository.save(video); // TODO: handle new actors and new genres
    }

    @GetMapping("/{id}")
    public Video getVideo(@PathVariable String id) {
        Optional<Video> optionalVideo = videoRepository.findById(Integer.parseInt(id));
        if(optionalVideo.isPresent()) {
            return optionalVideo.get();
        }
        throw new RuntimeException(); // TODO
    }

    @PutMapping("/{id}")
    public void updateVideo(@PathVariable String id) {
        // TODO
    }

    @DeleteMapping("/{id}")
    public void deleteVideo(@PathVariable String id) {
        videoRepository.deleteById(Integer.parseInt(id));
    }

}
