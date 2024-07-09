package com.eduprime.eduprimeserver.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest {

    private String type;

    private String title;

    private String content;

    private Integer itemOrder;

    private String lessonId;

    private String fileType;

    private String fileName;

    private String filePath;

    private String fileUrl;

    private String duration; // time video (if have)

    private String image;

//    private List<NoteDto> notes;
}
