package com.eduprime.eduprimeserver.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.nio.file.FileSystems;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoStreamingService {

    private static final String FORMAT = "file:uploads/videos/%s.mp4";

    private final ItemService itemService;

    @Qualifier("gridFsTemplate")
    private final ResourceLoader resourceLoader;

    public Mono<Resource> getVideo(String videoId) {
        String videoTitle = FileSystems.getDefault().getPath(this.itemService.findItemById(videoId)
                .getFilePath()).getFileName().toString().replace(".mp4", "");
        return Mono.fromSupplier(() -> this.resourceLoader.getResource(String.format(FORMAT, videoTitle)));
    }
}
