package com.eduprime.eduprimeserver.dtos;

import com.eduprime.eduprimeserver.domains.Item;
import com.eduprime.eduprimeserver.utils.ObjectMapperUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto extends BaseObjectDto {

    private String type;

    private String title;

    private String content;

    private Integer itemOrder;

    private LessonDto lesson;

    private String fileType;

    private String fileName;

    private String filePath;

    private String fileUrl;

    private String duration; // time video (if have)

    private String image;

    private List<NoteDto> notes;

    public static final ItemDto of(final Item entity) {
        return ObjectMapperUtil.OBJECT_MAPPER.convertValue(entity, ItemDto.class);
    }
}
