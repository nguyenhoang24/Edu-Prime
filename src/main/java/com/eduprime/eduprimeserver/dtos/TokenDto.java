package com.eduprime.eduprimeserver.dtos;

import com.eduprime.eduprimeserver.domains.Token;
import com.eduprime.eduprimeserver.utils.ObjectMapperUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TokenDto extends BaseObjectDto{

    private UserDto user;

    private String token;

    private String tokenType;

    private Date expirationDate;

    private boolean revoked;

    private boolean expired;

    private String refreshToken;

    private Date expirationRefresh;

    public static final TokenDto of(final Token token) {
        if (token != null)
            return ObjectMapperUtil.OBJECT_MAPPER.convertValue(token, TokenDto.class);
        return null;
    }
}
