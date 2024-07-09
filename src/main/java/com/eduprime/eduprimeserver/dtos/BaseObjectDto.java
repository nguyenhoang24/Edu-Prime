package com.eduprime.eduprimeserver.dtos;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.util.Date;

@Data
@MappedSuperclass
public class BaseObjectDto {

    protected String id;

    protected Date createdDate;

    protected String createdBy;

    protected Date modifiedDate;

    protected String modifiedBy;
}
