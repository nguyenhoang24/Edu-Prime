package com.eduprime.eduprimeserver.dtos.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {

    private Long id;

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String address;

    private String profile;

    private Integer status;
}
