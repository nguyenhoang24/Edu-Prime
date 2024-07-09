package com.eduprime.eduprimeserver.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "file")
@Getter
@Setter
public class FileStorageProperties {

    private UploadDir uploadDir;

    @Getter
    @Setter
    public static class UploadDir {
        private String image;

        private String video;
    }
}
