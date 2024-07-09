package com.eduprime.eduprimeserver.services.impl;

import com.eduprime.eduprimeserver.common.HttpStatusCodes;
import com.eduprime.eduprimeserver.common.HttpStatusMessages;
import com.eduprime.eduprimeserver.domains.User;
import com.eduprime.eduprimeserver.dtos.BaseResponse;
import com.eduprime.eduprimeserver.dtos.UserDto;
import com.eduprime.eduprimeserver.dtos.request.LoginRequest;
import com.eduprime.eduprimeserver.dtos.response.JwtResponse;
import com.eduprime.eduprimeserver.services.AuthenticationService;
import com.eduprime.eduprimeserver.services.JwtService;
import com.eduprime.eduprimeserver.services.TokenService;
import com.eduprime.eduprimeserver.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final TokenService tokenService;

    private final UserService userService;

    @Override
    public BaseResponse<?> loginUser(LoginRequest request) {
        if (request.getUsername() == null || request.getUsername().isEmpty()) {
            return BaseResponse.error(HttpStatusCodes.BAD_REQUEST, HttpStatusMessages.LOGIN_FAIL + " username not null!");
        }
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            return BaseResponse.error(HttpStatusCodes.BAD_REQUEST, HttpStatusMessages.LOGIN_FAIL + " password not null!");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User customUserDetails = (User) authentication.getPrincipal();
        String jwt = jwtService.generateToken(customUserDetails);
        String refreshToken = jwtService.generateRefreshToken(customUserDetails);
        List<String> roleList = customUserDetails.getAuthorities().stream()
                .map(item -> item.getAuthority()).collect(Collectors.toList());

        tokenService.createToken(this.userService.getByUserName(customUserDetails.getUsername()), jwt, refreshToken);

        return BaseResponse.of(new JwtResponse(jwt, customUserDetails.getId(), customUserDetails.getUsername(),
                        customUserDetails.getFirstName(), customUserDetails.getLastName(), customUserDetails.getEmail(),
                        customUserDetails.getPhoneNumber(), roleList, refreshToken, customUserDetails.getProfile()),
                HttpStatusCodes.OK, HttpStatusMessages.LOGIN_SUCCESS);
    }

    @Override
    public BaseResponse<?> register(UserDto userDto) {
        UserDto userCheck = this.userService.findByUserName(userDto.getUsername());
        if (userCheck != null){
            throw new IllegalArgumentException("%s, User with username = %s already exists."
                    .formatted(HttpStatusMessages.REGISTER_FAIL, userDto.getUsername()));
        }
        UserDto user = this.userService.createUser(userDto);
        return BaseResponse.of(user, HttpStatusCodes.OK, HttpStatusMessages.REGISTER_SUCCESS);
    }

    @Override
    public BaseResponse<?> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return BaseResponse.error(HttpStatusCodes.BAD_REQUEST, HttpStatusMessages.LOGOUT_FAIL);
        }
        jwt = authHeader.substring(7);
        var token = this.tokenService.findByToken(jwt);
        if (token != null) {
            token.setExpired(true);
            token.setRevoked(true);
            this.tokenService.updateToken(token);
            SecurityContextHolder.clearContext();
        }
        return BaseResponse.message(HttpStatusCodes.OK, HttpStatusMessages.LOGOUT_SUCCESS);
    }
}
