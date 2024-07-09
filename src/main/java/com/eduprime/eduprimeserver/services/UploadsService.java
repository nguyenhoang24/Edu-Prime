package com.eduprime.eduprimeserver.services;

import com.eduprime.eduprimeserver.dtos.BaseResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UploadsService {

    BaseResponse<?> storeFile(MultipartFile file);
}
