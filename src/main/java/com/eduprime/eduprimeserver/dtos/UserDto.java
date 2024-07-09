package com.eduprime.eduprimeserver.dtos;

import com.eduprime.eduprimeserver.domains.Role;
import com.eduprime.eduprimeserver.domains.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;

@JsonIgnoreProperties
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto extends BaseObjectDto{

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String address;

    private String profile;

    private Integer status;

    private Set<String> roleList;

    public static final UserDto of(final User user) {
        if (user != null) {
            UserDto dto = new UserDto();
            dto.setUsername(user.getUsername());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setEmail(user.getEmail());
            dto.setPhoneNumber(user.getPhoneNumber());
            dto.setAddress(user.getAddress());
            dto.setProfile(user.getProfile());
            dto.setStatus(user.getStatus());
            dto.setRoleList(user.getRoleList().stream()
                    .map(Role::getRoleName)
                    .collect(Collectors.toSet()));

            return dto;
        }
        return null;
    }
}
