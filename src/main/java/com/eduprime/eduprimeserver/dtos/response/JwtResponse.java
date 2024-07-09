package com.eduprime.eduprimeserver.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {

    private String token;

    private String type = "Bearer";

    private String userId;

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private List<String> roleList;

    private String refreshToken;

    private String avatar;

    public JwtResponse(String token, String userId, String username, String firstName, String lastName, String email, String phoneNumber, List<String> roleList, String refreshToken, String avatar) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.roleList = roleList;
        this.refreshToken = refreshToken;
        this.avatar = avatar;
    }
}
