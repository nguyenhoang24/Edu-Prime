package com.eduprime.eduprimeserver.domains;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tbl_note")
public class Note extends BaseObject{

    @Column(name = "item_type")
    private String itemType;

    @Column(name = "content")
    private String content;

    @Column(name = "timestamp")
    private String timestamp;

    @Column(name = "user_id")
    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
}
