package com.eduprime.eduprimeserver.controllers;

import com.eduprime.eduprimeserver.services.VideoStreamingService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/play-videos")
@RequiredArgsConstructor
public class VideoStreamingController {

    private final VideoStreamingService service;

    @GetMapping(value = "video-details/{videoId}", produces = "video/mp4")
    public Mono<Resource> getVideoDetails(@PathVariable String videoId) {
        return service.getVideo(videoId);
    }

}
