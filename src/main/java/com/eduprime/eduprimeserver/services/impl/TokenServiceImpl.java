package com.eduprime.eduprimeserver.services.impl;

import com.eduprime.eduprimeserver.domains.Token;
import com.eduprime.eduprimeserver.domains.User;
import com.eduprime.eduprimeserver.dtos.TokenDto;
import com.eduprime.eduprimeserver.repositories.TokenRepository;
import com.eduprime.eduprimeserver.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;

    @Value("${jwt.expiration}")
    private int JWT_EXPIRATION;

    @Value("${jwt.expiration-refresh-token}")
    private int JWT_EXPIRATION_REFRESH_TOKEN;

    @Transactional
    @Override
    public Token createToken(User user, String token, String refreshToken) {
        Date expirationDate = new Date(System.currentTimeMillis() + (long) JWT_EXPIRATION * 1000);
        Token newToken = Token.builder()
                .user(user)
                .token(token)
                .revoked(false)
                .expired(false)
                .tokenType("Bearer")
                .expirationDate(expirationDate)
                .refreshToken(refreshToken)
                .build();

        newToken.setExpirationRefresh(new Date(System.currentTimeMillis() + (long) JWT_EXPIRATION_REFRESH_TOKEN * 1000));

        tokenRepository.save(newToken);

        return newToken;
    }

    @Override
    public TokenDto findByToken(String jwt) {
        Assert.notNull(jwt, "jwt null! [User-service: TokenService]");

        var byToken = this.tokenRepository.findByToken(jwt).orElse(null);

        return TokenDto.of(byToken);
    }

    @Override
    @Transactional
    public TokenDto updateToken(TokenDto tokenDto) {
        Assert.notNull(tokenDto, "token not null! [User-service: TokenService");

        Token token = this.tokenRepository.findByToken(tokenDto.getToken()).orElse(null);
        token.setExpired(tokenDto.isExpired());
        token.setRevoked(tokenDto.isRevoked());

        return TokenDto.of(token);
    }
}