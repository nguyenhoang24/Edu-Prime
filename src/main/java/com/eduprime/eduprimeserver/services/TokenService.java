package com.eduprime.eduprimeserver.services;


import com.eduprime.eduprimeserver.domains.Token;
import com.eduprime.eduprimeserver.domains.User;
import com.eduprime.eduprimeserver.dtos.TokenDto;

public interface TokenService {

    Token createToken(User user, String token, String refreshToken);

    TokenDto findByToken(String jwt);

    TokenDto updateToken(TokenDto tokenDto);
}
