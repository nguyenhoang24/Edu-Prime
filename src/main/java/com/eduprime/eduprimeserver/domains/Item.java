package com.eduprime.eduprimeserver.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Table(name = "tbl_item")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Item extends BaseObject{

    @Column(name = "type")
    private String type;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "itemOrder")
    private Integer itemOrder;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @Column(name = "fileType")
    private String fileType;

    @Column(name = "fileName")
    private String fileName;

    @Column(name = "filePath")
    private String filePath;

    @Column(name = "fileUrl")
    private String fileUrl;

    @Column(name = "duration")
    private String duration; // time video (if have)

    @Column(name = "image")
    private String image;

    @JsonIgnore
    @OneToMany(mappedBy = "item", fetch = FetchType.EAGER)
    private List<Note> notes;
}
