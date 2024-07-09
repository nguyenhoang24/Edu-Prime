package com.eduprime.eduprimeserver.dtos.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginRequest {

    private String username;

    private String password;
}
