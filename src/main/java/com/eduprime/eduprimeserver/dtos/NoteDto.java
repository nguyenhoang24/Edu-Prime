package com.eduprime.eduprimeserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NoteDto extends BaseObjectDto {

    private String itemType;

    private String content;

    private String timestamp;

    private String userId;

    private ItemDto item;
}
