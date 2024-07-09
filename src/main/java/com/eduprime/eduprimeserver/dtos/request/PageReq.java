package com.eduprime.eduprimeserver.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageReq {

    private Integer pageIndex;

    private Integer pageSize;

    private String code;

    private String name;

    private String text;

    private Date fromDate;

    private Date toDate;

    private Date startDate;

    private Date endDate;
}
